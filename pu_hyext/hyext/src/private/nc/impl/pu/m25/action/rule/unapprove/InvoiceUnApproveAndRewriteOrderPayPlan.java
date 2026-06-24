package nc.impl.pu.m25.action.rule.unapprove;

import java.util.HashMap;
import java.util.Map;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.bd.payment.IPaymentUtil;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m25.entity.InvoiceItemVO;
import nc.vo.pu.m25.entity.InvoiceVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 发票弃审时回写付款计划的起算日期
 * 
 * @author yechd5
 * @since 2017-07-03 下午 13:56:43
 */
public class InvoiceUnApproveAndRewriteOrderPayPlan implements IRule<InvoiceVO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(InvoiceVO[] vos) {
		// 20210826 jiuzhou_zhian.ye	取消审批时出空指针，经检查后，本方法参数进来为空值，下边循环直接报错，
				if(vos == null || vos.length == 0 ){
					return;
				}
		// 由于时间问题，下面这段代码写的惨不忍睹!有心人可重构一下！
		for (InvoiceVO invoicevo : vos) {
			InvoiceItemVO[] itemvos = invoicevo.getChildrenVO();
			UFDate approvedate = invoicevo.getParentVO().getTaudittime();// 发票审批日期
			for (InvoiceItemVO itemvo : itemvos) {
				if (itemvo.getPk_order() != null) {
					String orderid = itemvo.getPk_order();// 采购订单pk
					// 1.获取采购订单付款计划
					PayPlanVO[] payplans = (PayPlanVO[]) new VOQuery(
							PayPlanVO.class).query(" and dr=0 and pk_order = '"
							+ orderid + "'", null);
					if (payplans == null || payplans.length == 0) {
						continue;
					}
					// 2. 根据订单pk查采购订单VO，从而得到采购订单表体付款协议
					OrderVO[] order = (OrderVO[]) new BillQuery(OrderVO.class)
							.query(new String[] { itemvo.getPk_order() });
					if (order == null || order.length == 0) {
						continue;
					}
					// （1）获取付款协议
					OrderPaymentVO[] paymentvos = order[0].getPaymentVO();
					// （2）根据付款协议获取各起效依据的延迟天数；Map<账期主键，延迟天数>
					Map<String, Integer> map = new HashMap<String, Integer>();
					if (paymentvos != null) {
						for (OrderPaymentVO paymentvo : paymentvos) {
							map.put(paymentvo.getPk_payment(),
									paymentvo.getEffectdateadddate());
						}
					}

					// 3.更新付款计划
					for (PayPlanVO payplanvo : payplans) {
						// （1）根据map通过账期主键获取延迟天数(付款协议的pk_ct_pu_payment与付款计划的pk_paytermch对应)
						// 界面没有设置（null）：延迟天数为0;界面账期号不存在：延迟天数为0
						int settedDelay;
						if ((null == map.get(payplanvo.getPk_paymentch()))
								|| (null == payplanvo.getIaccounttermno())) {
							settedDelay = 0;
						} else {
							settedDelay = map.get(
									payplanvo.getPk_paymentch()).intValue();
						}
						
						// （2）更新
						if (payplanvo.getDbegindate() != null) {
							if (approvedate.equals(payplanvo.getDbegindate()
									.getDateBefore(settedDelay))
									&& IPaymentUtil.PURCHASE_INVOICE_APPROVE_DATE
											.equals(payplanvo.getFeffdatetype())
									&& (payplanvo.getNaccumpayapporgmny() == null || payplanvo
											.getNaccumpayapporgmny().compareTo(
													UFDouble.ZERO_DBL) <= 0)) {
								payplanvo.setDbegindate(null);
								payplanvo.setDenddate(null);
								// 在此请不要清空 “账期天数”，否则重新审批时，日期计算不对
							}
						}
					}
					new VOUpdate().update(payplans);
				}
			}
		}
	}
}

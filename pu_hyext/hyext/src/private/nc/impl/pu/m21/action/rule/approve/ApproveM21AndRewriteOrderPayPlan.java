package nc.impl.pu.m21.action.rule.approve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.bd.payment.IPaymentUtil;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.lang.UFDate;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.vo.pubapp.pattern.data.IRowSet;

/**
 * 采购订单审批时回写采购订单付款计划（采购合同生效日期起算日期）
 * 
 * @author yechd5
 * @since 2017-07-05 上午 09:49:13
 */
public class ApproveM21AndRewriteOrderPayPlan implements IRule<OrderVO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(OrderVO[] vos) {
		// 20210826 jiuzhou_zhian.ye	取消审批时出空指针，经检查后，本方法参数进来为空值，下边循环直接报错，
		if(vos == null || vos.length == 0 ){
			return;
		}
		// 由于时间问题，下面这段代码写的惨不忍睹!有心人可重构一下！
		for (OrderVO order : vos) {
			// 1.获取订单表体付款协议
			OrderPaymentVO[] paymentvos = order.getPaymentVO();
			// Map<账期主键，延迟天数>
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (paymentvos != null) {
				for (OrderPaymentVO paymentvo : paymentvos) {
					map.put(paymentvo.getPk_payment(),
							paymentvo.getEffectdateadddate());
				}
			}

			OrderItemVO[] itemvos = order.getBVO();
			for (OrderItemVO itemvo : itemvos) {
				if (itemvo.getCcontractid() != null) {// ccontractid 对应合同主键
														// pk_ct_pu
					UFDate actualvalidate = this.qryContractApproveDate(itemvo
							.getCcontractid());
					PayPlanVO[] payplans = (PayPlanVO[]) new VOQuery(
							PayPlanVO.class).query(" and dr=0 and pk_order = '"
							+ itemvo.getPk_order() + "'", null);
					if (payplans == null || payplans.length == 0) {
						continue;
					}

					for (PayPlanVO payplanvo : payplans) {
						if (payplanvo.getDbegindate() == null
								&& IPaymentUtil.PURCHASE_CONTRACT_EFFECTIVE_DATE
										.equals(payplanvo.getFeffdatetype())) {
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
							payplanvo.setDbegindate(actualvalidate
									.getDateAfter(settedDelay));// 设置起算日期（要加上延迟天数）
							
							// （2）计算账期到期日
							int iitermdays;
							if (payplanvo.getIitermdays() == null) {
								iitermdays = 0;
								payplanvo.setIitermdays(null);
							} else {
								iitermdays = payplanvo.getIitermdays()
										.intValue();
								payplanvo.setIitermdays(iitermdays);
							}

							// （3）设置账期到期日期
							UFDate denddate = new UFDate(payplanvo
									.getDbegindate().toString())
									.getDateAfter(iitermdays);
							payplanvo.setDenddate(denddate);
						}
					}
					new VOUpdate().update(payplans);
				}
			}
		}
	}

	/**
	 * 根据采购合同主键查询采购合同实际生效日期
	 * 
	 * @param pk_ct_pu
	 * @return UFDate
	 * @since 2017-07-05 上午 10:32:18
	 */
	public UFDate qryContractApproveDate(String pk_ct_pu) {
		StringBuffer sql = new StringBuffer();
		sql.append("select actualvalidate from ct_pu where dr=0 and pk_ct_pu=");
		sql.append("'");
		sql.append(pk_ct_pu);
		sql.append("'");

		DataAccessUtils util = new DataAccessUtils();
		IRowSet rowset = util.query(sql.toString());
		List<UFDate> list = new ArrayList<UFDate>();
		while (rowset.next()) {
			list.add(rowset.getUFDate(0));
		}
		return list.get(0);
	}
}

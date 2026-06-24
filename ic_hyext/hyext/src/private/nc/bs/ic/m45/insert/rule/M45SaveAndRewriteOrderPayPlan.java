package nc.bs.ic.m45.insert.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.bd.payment.IPaymentUtil;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pu.m21.entity.OrderPaymentVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pu.m21.entity.PayPlanVO;
import nc.vo.pub.lang.UFDate;

/**
 * 采购入库单保存时回写采购订单付款计划
 * 
 * @author yechd5
 * @since 2017-07-03 下午13:39:19
 */
public class M45SaveAndRewriteOrderPayPlan implements IRule<PurchaseInVO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(PurchaseInVO[] vos) {
		// 由于时间问题，下面这段代码写的惨不忍睹!有心人可重构一下！
		for (PurchaseInVO vo : vos) {
			// 取最小日期/最先入库的作为入库日期
			ICBillBodyVO[] bVos = vo.getChildrenVO();
			List<UFDate> list = new ArrayList<UFDate>();
			for (ICBillBodyVO bVo : bVos) {
				list.add(bVo.getDbizdate());
			}
			UFDate minAssignDate = Collections.min(list);
			//没有录入实收时，没有业务日期，此时不做处理直接返回
			if (null ==minAssignDate){
				return;
			}
			for (PurchaseInBodyVO bodyvo : vo.getBodys()) {
				// 1.获取采购订单付款计划
				PayPlanVO[] payplans = (PayPlanVO[]) new VOQuery(
						PayPlanVO.class).query(
						" and pk_order = '" + bodyvo.getCfirstbillhid() + "'",
						null);
				if (payplans == null || payplans.length == 0) {
					continue;
				}
				// 2. 根据订单pk查采购订单VO，从而得到采购订单表体付款协议
				OrderVO[] order = (OrderVO[]) new BillQuery(OrderVO.class)
						.query(new String[] { bodyvo.getCfirstbillhid() });
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
					if (payplanvo.getDbegindate() == null
							&& IPaymentUtil.STORE_RECEIPT_DATE.equals(payplanvo
									.getFeffdatetype())) {
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
						payplanvo.setDbegindate(minAssignDate
								.getDateAfter(settedDelay));// 设置起算日期（要加上延迟天数）
						
						// （2）设置账期天数
						int iitermdays;
						if (payplanvo.getIitermdays() == null) {
							iitermdays = 0;
							payplanvo.setIitermdays(null);
						} else {
							iitermdays = payplanvo.getIitermdays().intValue();
							payplanvo.setIitermdays(iitermdays);
						}

						// （3）设置账期到期日期
						UFDate denddate = new UFDate(payplanvo.getDbegindate()
								.toString()).getDateAfter(iitermdays);
						payplanvo.setDenddate(denddate);
					}
				}
				new VOUpdate().update(payplans);
			}
		}
	}
}

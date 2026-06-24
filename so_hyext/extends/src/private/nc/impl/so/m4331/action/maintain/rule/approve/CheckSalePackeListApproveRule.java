package nc.impl.so.m4331.action.maintain.rule.approve;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.uap.busibean.SysinitAccessor;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;
import nc.vo.so.pub.enumeration.BillStatus;

public class CheckSalePackeListApproveRule implements IRule<DeliveryVO> {
	public void process(DeliveryVO[] vos) {
		for (DeliveryVO vo : vos) {
			DeliveryHVO hvo = vo.getParentVO();
			Integer status = hvo.getFstatusflag();
			boolean expr1 = BillStatus.FREE.equalsValue(status);
			boolean expr2 = BillStatus.AUDITING.equalsValue(status);
			DeliveryBVO[] bvos = vo.getChildrenVO();
			try {
				boolean yf635 = SysinitAccessor.getInstance()
						.getParaBoolean(vo.getParentVO().getPk_org(), "YF635")
						.booleanValue();
				if (yf635) {
					if ((!expr1) && (!expr2)) {
						for (DeliveryBVO bvo : bvos) {// 不是客户样品需要关联包装清单
							if((bvo.getNastnum() != null && bvo.getNastnum().doubleValue()>0)
									|| (bvo.getNnum() != null && bvo.getNnum().doubleValue()>0)){
								if (!"Y".equals(bvo.getVbdef8())) {
									if (!"Y".equals(bvo.getVbdef19())) {
										ExceptionUtils
												.wrappBusinessException("发货单["
														+ hvo.getVbillcode()
														+ "]存在没有生成包装清单的数据，不能审批通过！");
									}
								}
							}
						}
					}
				}
			} catch (BusinessException e) {
				ExceptionUtils.wrappException(e);
			}

		}
	}
}
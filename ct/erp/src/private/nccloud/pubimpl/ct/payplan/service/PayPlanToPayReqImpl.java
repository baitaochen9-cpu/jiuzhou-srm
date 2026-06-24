package nccloud.pubimpl.ct.payplan.service;

import nc.impl.pubapp.pattern.data.view.ViewQuery;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.vo.cmp.pub.IApplyConst;
import nc.vo.ct.purdaily.entity.AggPayPlanVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.pubitf.ct.payplan.service.IPayPlanToPayReq;

/**
 * 
 * @Description 付款计划推付款申请实现
 * @author zhangshqb
 * @since 2018-6-24
 * @version V1.0
 * 
 */
public class PayPlanToPayReqImpl implements IPayPlanToPayReq {

	@Override
	public AggregatedValueObject[] getPayReqAggVO(String[] ids) throws BusinessException {
		ViewQuery<PayPlanViewVO> query = new ViewQuery<PayPlanViewVO>(PayPlanViewVO.class);
		PayPlanViewVO[] viewVOs = query.query(ids);
		AggPayPlanVO[] vos = PayPlanViewVO.getAggPayPlanVO(viewVOs);
		AggregatedValueObject[] destVOs = PfServiceScmUtil.exeVOChangeByBillItfDef(CTBillType.PurDaily.getCode(),
				IApplyConst.CMP_36D1, vos);
		return destVOs;
	}

}

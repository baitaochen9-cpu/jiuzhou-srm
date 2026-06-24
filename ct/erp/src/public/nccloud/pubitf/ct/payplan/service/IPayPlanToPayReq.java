package nccloud.pubitf.ct.payplan.service;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 
 * @Description 采购合同付款计划推付款申请
 * @author xiahui
 * @since 2018-6-22
 * @version V1.0
 * 
 */
public interface IPayPlanToPayReq {
	/**
	 * 获取付款申请AggVO
	 * 
	 * @param ids
	 *          付款计划主键数组
	 * @return 付款申请AggVO
	 * 
	 */
	public AggregatedValueObject[] getPayReqAggVO(String[] ids) throws BusinessException ;
}

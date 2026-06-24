package nccloud.pubitf.ct.payplan.service;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 
 * @Description 采购合同付款计划推付款单接口
 * @author xiahui
 * @since 2018-6-22
 * @version V1.0
 * 
 */
public interface IPayPlanToPay {
	/**
	 * 获取付款单AggVO
	 * 
	 * @param ids
	 *          付款计划主键数组
	 * @return 付款单AggVO
	 * 
	 */
	public AggregatedValueObject[] getPayAggVO(String[] ids) throws BusinessException;
}

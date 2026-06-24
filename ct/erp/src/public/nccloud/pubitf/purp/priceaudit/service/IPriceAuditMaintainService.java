package nccloud.pubitf.purp.priceaudit.service;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 
 * @description 价格审批单服务接口
 * @author zhangchqf
 * @date 2019年5月15日 下午2:58:10 
 * @version ncc1.0
 */
public interface IPriceAuditMaintainService {
	/**
	 * 
	 * 价格审批单：推委外订单
	 * 
	 * @param ids
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 * 
	 */
	public AggregatedValueObject[] m28PriceAudit61(String[] ids) throws BusinessException;

	/**
	 * 
	 * 价格审批单：推采购合同
	 * 
	 * @param ids
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 * 
	 */
	
	public AggregatedValueObject[] m28PriceAuditZ2(String[] ids) throws BusinessException;
	/**
	 * 
	 * 价格审批单：推采购订单
	 * 
	 * @param ids
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 * 
	 */
	public AggregatedValueObject[] m28PriceAudit21(String[] ids) throws BusinessException;

}

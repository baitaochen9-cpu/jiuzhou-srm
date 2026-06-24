package nccloud.pubitf.ct.report.service;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;

public interface IPurexecution {
	/**
	 * 联查采购合同执行情况订单明细url
	 * 
	 * @param drillConfigMap
	 * @param condition
	 * @param opents
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getUrlBuyConditions(
			QueryTreeFormatVO conditionTree, Map serializedObjMap)
			throws BusinessException;

	/**
	 * 联查采购合同执行情况订单明细
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getPurExecutionDetail(String pk_org, String ctbillcode,
			String pk_material) throws BusinessException;

	public AggCtPuVO[] getAggCtPuVo(String pk_org, String ctbillcode, String pk_material)
			throws BusinessException;
}

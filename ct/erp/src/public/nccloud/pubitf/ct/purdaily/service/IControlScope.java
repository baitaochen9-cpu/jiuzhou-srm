package nccloud.pubitf.ct.purdaily.service;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;

/**
 * @description 采购合同维护，合同控制范围
 * @author xiahui
 * @date 创建时间：2019-3-7 下午2:00:44
 * @version ncc1.0
 **/
public interface IControlScope {

	/**
	 * 获取合同控制范围显示数据
	 * 
	 * @param bill
	 *          单据
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getCtrlScopeList(AggCtPuVO bill) throws BusinessException;

	/**
	 * 更新合同控制范围
	 * 
	 * @param id
	 * @param sCtrlScopeNew
	 * @throws BusinessException
	 */
	public void updateCtrlScope(String id, String[] sCtrlScopeNew) throws BusinessException;

	/**
	 * 获取合同批量控制范围显示数据
	 * 
	 * @param bills
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, Object> getBatchCtrlScopeList(AggCtPuVO[] bills) throws BusinessException;

	/**
	 * 新增合同批量控制范围
	 * 
	 * @param bills
	 * @throws BusinessException
	 */
	public void addBatchCtrlScope(String[] pks, String[] sCtrlScopeNew) throws BusinessException;

	/**
	 * 删除合同批量控制范围
	 * 
	 * @param pks
	 * @param sCtrlScopeNew
	 * @throws BusinessException
	 */
	public void deleteBatchCtrlScope(String[] pks, String[] sCtrlScopeNew, Map<String, String> puorgmap)
			throws BusinessException;
}

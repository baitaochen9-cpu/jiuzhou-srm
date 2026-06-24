package nccloud.pubitf.ct.purdaily.service;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;

/**
 * @description 采购合同提交服务和取消服务
 * @author guozhq
 * @date 2019年6月20日 下午9:05:51
 * @version ncc1.0
 */
public interface IPurdailyService {

	/**
	 * 
	 * 提交
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 *
	 */
	SCMScriptResultDTO commit(AggCtPuVO[] vos) throws BusinessException;

	/**
	 * 
	 * 取消提交
	 * 
	 * @param vos
	 * @return
	 * @throws BusinessException
	 *
	 */
	SCMScriptResultDTO uncommit(AggCtPuVO[] vos) throws BusinessException;

	/**
	 * 根据历史版本，查询最新版本主键
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	String queryLatestId(String id) throws BusinessException;
}

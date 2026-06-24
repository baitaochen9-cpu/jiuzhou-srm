package nccloud.pubitf.scmpub.pub.service;

import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

/**
 * @description SCMPUB公共查询
 * @author guozhq
 * @date 2018-8-8 下午7:24:45
 * @version ncc1.0
 */
public interface ISCMPubQueryService {
	
	/**
	 * 
	 * 单据查询
	 * @param clazz
	 * @param ids
	 * @return
	 * @throws BusinessException
	 *
	 */
	public <T extends IBill> T[] billquery(Class<T> clazz, String[] ids) throws BusinessException;
	
	/**
	 * 
	 * 获取当前语种顺序
	 * @return
	 * @throws BusinessException
	 *
	 */
	public Integer getCurrentLangSeq() throws BusinessException;
	
	/**
	 * 根据主键查询VO数组
	 * 
	 * @param clazz
	 *            VO类型
	 * @param ids
	 *            主键数组
	 * @return
	 */
	public <T extends ISuperVO> T[] queryByIDs(Class<T> clazz, String[] ids);

}

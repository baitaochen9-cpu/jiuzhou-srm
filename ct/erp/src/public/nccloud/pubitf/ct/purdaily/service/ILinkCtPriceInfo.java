package nccloud.pubitf.ct.purdaily.service;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.pub.BusinessException;

/**
 * @description 联查价格明细
 * @author xiahui
 * @date 创建时间：2019-3-8 上午9:11:56
 * @version ncc1.0
 **/
public interface ILinkCtPriceInfo {

	/**
	 * 获取价格信息AggVO
	 * 
	 * @param ctMap
	 * @return
	 * @throws BusinessException
	 */
	public AggCtPriceVO getCtPriceAggVO(Map<String, Object> ctMap) throws BusinessException;
}

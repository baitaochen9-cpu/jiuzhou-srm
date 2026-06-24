package nccloud.pubitf.ct.price.service;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;

/**
 * 
 * @description 根据oid查询最新版本的合同
 * @author zhaoypm
 * @time 2019年7月9日 上午11:00:13
 * @since ncc1.0
 */
public interface ICtPriceQueryRecentlyService {
	/**
	 * 根据oid查询最新版本的合同
	 * 
	 * @param pk_oid
	 *            CtPriceHeaderVO.PK_OID 原始版本
	 * @return aggvo {@link CtPriceHeaderVO}
	 */
	AggCtPriceVO queryRecentlyBillByPkOid(String pk_oid) throws Exception;

}

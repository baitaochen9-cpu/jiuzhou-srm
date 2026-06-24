package nccloud.pubitf.ct.price.service;

import nc.vo.pub.BusinessException;
import nccloud.dto.ct.price.entity.CtPriceDynamicColumn;

/**
 * 
 * @description 合同价格信息表动态列获取接口
 * @author zhaoypm
 * @time 2019年6月24日 下午7:13:35
 * @since ncc1.0
 */
public interface ICtPriceDynamicColumnService {
	CtPriceDynamicColumn[] getDynamicColumn(String pk_ct_price, String pk_priceTemplate) throws BusinessException;
}

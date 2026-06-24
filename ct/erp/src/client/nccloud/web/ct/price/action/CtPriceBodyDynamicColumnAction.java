package nccloud.web.ct.price.action;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.scmpub.util.StringUtil;
import nccloud.dto.ct.price.entity.CtPriceDynamicColumn;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.price.service.ICtPriceDynamicColumnService;

/**
 * 
 * @description 价格模板表体动态列数据查询
 * @author zhaoypm
 * @time 2019-3-27 下午3:40:44
 * @since ncc1.0
 */
public class CtPriceBodyDynamicColumnAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// 获取价格模板主键pk_priceTemplate
		String json = request.read();
		IJson factory = JsonFactory.create();
		Map<String, String> queryInfoMap = factory.fromJson(json, Map.class);
		String pk_priceTemplate = queryInfoMap.get("pk_pricetemplet");
		String pk_ct_price = queryInfoMap.get("pk_ct_price");
		if (StringUtil.isNullStringOrNull(pk_priceTemplate)) {
			return null;
		}
		try {
			// 根据pk_priceTemplate查出价格模板表体行数据
			CtPriceDynamicColumn[] items = ServiceLocator.find(ICtPriceDynamicColumnService.class)
					.getDynamicColumn(pk_ct_price, pk_priceTemplate);

			Map<String, CtPriceDynamicColumn[]> returnMap = new HashMap<>();
			returnMap.put(pk_priceTemplate, items);
			return returnMap;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

}

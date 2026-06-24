package nccloud.web.ct.price.action;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
//import nccloud.pubitf.ct.price.service.ICtPriceQueryRecentlyService;
import nccloud.pubitf.ct.price.service.ICtPriceQueryRecentlyService;

/**
 * 
 * @description 根据oid查询最近版本的合同价格信息表
 * @author zhaoypm
 * @time 2019年7月9日 下午12:37:59
 * @since ncc1.0
 */
public class CtPriceQueryRecentlyBillAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		Map<String, String> map = factory.fromJson(json, Map.class);
		String pk_oid = map.get(CtPriceHeaderVO.PK_OID);
		if (null == pk_oid || "".equals(pk_oid)) {
			return null;
		}
		ICtPriceQueryRecentlyService service = ServiceLocator.find(ICtPriceQueryRecentlyService.class);
		AggCtPriceVO recentlyBill = null;
		try {
			recentlyBill = service.queryRecentlyBillByPkOid(pk_oid);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		if (null == recentlyBill) {
			return null;
		}
		return recentlyBill;
	}

}

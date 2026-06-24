package nccloud.web.ct.price.action;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.util.VOEntityUtil;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.ct.price.action.dto.QueryInfo;
import nccloud.web.scmpub.pub.utils.grid.MultiGridUtils;

/**
 * 
 * @description 价格信息表修订历史查询
 * @author zhaoypm
 * @time 2019-3-29 上午9:43:16
 * @since ncc1.0
 */
public class CtPriceQueryHistroyAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		QueryInfo info = factory.fromJson(json, QueryInfo.class);
		// 原始版本
		String[] pk_oid = info.getPk_oid();
		String pageId = info.getPageId();
		String areacode = info.getAreacode();
		if (null == pk_oid || pk_oid.length == 0) {
			return null;
		}
		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		Grid grid = null;
		try {
			AggCtPriceVO[] aggvos = service.queryHisVersion(pk_oid[0]);
			if (null == aggvos || aggvos.length == 0) {
				return null;
			}
			// 表头
			grid = MultiGridUtils.toGrid(pageId, areacode,
					VOEntityUtil.getHeadVOs(aggvos));
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}
}

package nccloud.web.ct.price.action;

import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.ct.price.action.dto.QueryInfo;

public class CtPricePageQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		QueryInfo queryInfo = factory.fromJson(json, QueryInfo.class);
		String[] pks = queryInfo.getPks();
		String pageId = queryInfo.getPageId();
		if (null == pks || pks.length == 0) {
			return null;
		}
		ISCMPubQueryService findService = ServiceLocator.find(ISCMPubQueryService.class);
		CtPriceHeaderVO[] vos = findService.queryByIDs(CtPriceHeaderVO.class, pks);
		GridOperator operator = new GridOperator(pageId);
		Grid grid = operator.toGrid(vos);
		return grid;
	}

}

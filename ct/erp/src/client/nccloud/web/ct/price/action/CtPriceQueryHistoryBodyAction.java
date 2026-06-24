package nccloud.web.ct.price.action;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.util.VOEntityUtil;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.ct.price.action.dto.QueryInfo;
import nccloud.web.scmpub.pub.utils.grid.MultiGridUtils;
import nccloud.web.scmpub.pub.utils.scale.SCMGridPrecisionOperator;

public class CtPriceQueryHistoryBodyAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson factory = JsonFactory.create();
		QueryInfo queryInfo = factory.fromJson(read, QueryInfo.class);
		String pk = null;
		// ªÒ»°pk
		String[] pks = queryInfo.getPks();
		String pageId = queryInfo.getPageId();
		String areacode = queryInfo.getAreacode();
		if (null == pks || pks.length == 0) {
			return null;
		} else {
			pk = pks[0];
		}
		ISCMPubQueryService service = ServiceLocator.find(ISCMPubQueryService.class);
		AggCtPriceVO[] vos;
		try {
			vos = service.billquery(AggCtPriceVO.class, new String[] { pk });
			if (null == vos || vos.length == 0) {
				return null;
			}
			Grid grid = MultiGridUtils.toGrid(pageId, areacode, VOEntityUtil.getBodyVOs(vos));
			this.processScale(grid);
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private void processScale(Grid grid) {
		SCMGridPrecisionOperator operator = new SCMGridPrecisionOperator(grid);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, CtPriceHeaderVO.CORIGCURRENCYID);
		operator.processPrecision();
	}
}

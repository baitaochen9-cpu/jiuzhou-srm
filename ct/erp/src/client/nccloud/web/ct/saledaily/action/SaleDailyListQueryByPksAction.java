package nccloud.web.ct.saledaily.action;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

/**
 * @description 根据pk查询销售合同
 * @author wangshrc
 * @date 2019年3月5日 上午9:54:24
 * @version ncc1.0
 */
public class SaleDailyListQueryByPksAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo info = json.fromJson(read, SaleDailyQueryInfo.class);
		String[] pks = info.getPks();
		if (null == pks || pks.length == 0) {
			return null;
		}
		ISaledailyMaintainApp service = ServiceLocator
				.find(ISaledailyMaintainApp.class);
		AggCtSaleVO[] vos = null;
		try {
			vos = service.queryMZ3App(pks);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		if (vos == null)
			return null;
		CtSaleVO[] hvos = new CtSaleVO[vos.length];
		for (int i = 0; i < hvos.length; i++) {
			hvos[i] = vos[i].getParentVO();
		}
		GridOperator go = new GridOperator("400600200_list");
		Grid grid = go.toGrid(hvos);
		SaleDailyPrecisionUtil.dealPrecision(grid);
		return grid;
	}

}

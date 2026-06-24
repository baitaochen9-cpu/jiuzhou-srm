package nccloud.web.ct.purdaily.action;

import java.util.ArrayList;
import java.util.List;

import nc.itf.ct.purdaily.IPurdailyMaintainApp;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.translate.Translator;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.web.ct.pub.action.QueryInfo;

/**
 * @description 列表分页查询
 * @author xiahui
 * @date 创建时间：2019-1-15 上午9:54:19
 * @version ncc1.0
 **/
public class PageQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		QueryInfo queryInfo = json.fromJson(read, QueryInfo.class);

		try {
			IPurdailyMaintainApp service = ServiceLocator.find(IPurdailyMaintainApp.class);
			AggCtPuVO[] vos = service.queryMZ2App(queryInfo.getPks());

			List<CtPuVO> hvoslist = new ArrayList<CtPuVO>();
			for (AggCtPuVO vo : vos) {
				hvoslist.add(vo.getParentVO());
			}
			CtPuVO[] hvos = hvoslist.toArray(new CtPuVO[0]);
			
			GridOperator operator = new GridOperator(queryInfo.getPagecode());
			Grid grid = operator.toGrid(hvos);
			Translator translator = new Translator();
			translator.translate(grid);
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}

		return null;
	}

}

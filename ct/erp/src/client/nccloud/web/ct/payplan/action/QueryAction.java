package nccloud.web.ct.payplan.action;

import nc.itf.ct.purdaily.ICtPayPlanQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.dto.ct.payplan.constance.CommonConst;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.translate.Translator;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.platform.query.INCCloudQueryService;

/**
 * @description 采购合同付款计划列表查询
 * @author xiahui
 * @date 创建时间：2019-1-15 上午9:13:59
 * @version ncc1.0
 **/
public class QueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		QueryTreeFormatVO formatVO = json.fromJson(read, QueryTreeFormatVO.class);
		// 获取查询模板查询条件
		INCCloudQueryService queryutil = ServiceLocator.find(INCCloudQueryService.class);
		IQueryScheme scheme = queryutil.convertCondition(formatVO);

		try {
			ICtPayPlanQuery service = ServiceLocator.find(ICtPayPlanQuery.class);
			PayPlanViewVO[] bills = service.queryByQueryScheme(scheme);
			
			if (bills == null || bills.length == 0) {
				return null;
			}
			
			GridOperator operator = new GridOperator(CommonConst.PAGECODE_LIST);
			Grid grid = operator.toGrid(bills);
			Translator translator = new Translator();
			translator.translate(grid);
			return grid;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}

		return null;
	}

}

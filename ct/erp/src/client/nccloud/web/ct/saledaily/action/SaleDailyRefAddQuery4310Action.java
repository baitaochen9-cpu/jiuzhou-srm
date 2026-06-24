package nccloud.web.ct.saledaily.action;

import nc.itf.so.salequotation.ISalequotationQry;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.so.salequotation.entity.AggSalequotationHVO;
import nccloud.dto.baseapp.querytree.dataformat.QueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billgrid.BillGrid;
import nccloud.framework.web.ui.pattern.billgrid.BillGridOperator;
import nccloud.pubitf.platform.query.INCCloudQueryService;
import nccloud.web.scmpub.pub.action.TempletQueryAction;
import nccloud.web.so.salequotation.action.scale.SaleQuotationScaleProcess;

/**
 * @description 销售合同拉单查询
 * @author wangshrc
 * @date 2019年3月6日 下午4:26:58
 * @version ncc1.0
 */
public class SaleDailyRefAddQuery4310Action implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// 解析前台请求
		String read = request.read();
		IJson json = JsonFactory.create();
		QueryTreeFormatVO queryTreeFormatVO = json.fromJson(read,
				QueryTreeFormatVO.class);
		INCCloudQueryService queryutil = ServiceLocator
				.find(INCCloudQueryService.class);
		IQueryScheme queryScheme = queryutil
				.convertCondition(queryTreeFormatVO);
		ISalequotationQry service = ServiceLocator
				.find(ISalequotationQry.class);
		AggSalequotationHVO[] vos = null;
		try {
			vos = service.queryByQuerySchemeForZ3(queryScheme);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		if (vos == null)
			return null;
		String templetid = TempletQueryAction.getTempletIdByAppCode(
				"400600100", "400600100_4310toZ3");
		BillGridOperator operator = new BillGridOperator(templetid,
				"400600100_4310toZ3");
		BillGrid[] billGrids = operator.toBillGrids(vos);
		// 精度处理
		SaleQuotationScaleProcess.processBillGrid(billGrids);
		return billGrids;
	}

}

package nccloud.web.ct.purdaily.action;

import nc.pubitf.pp.m28.IPriceAuditQueryForZ2;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.lang.UFBoolean;
import nccloud.dto.scmpub.page.entity.SCMQueryTreeFormatVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billgrid.BillGrid;
import nccloud.framework.web.ui.pattern.billgrid.BillGridOperator;
import nccloud.pubitf.platform.query.INCCloudQueryService;

/**
 * @description 价格审批单查询
 * @author xiahui
 * @date 创建时间：2019-2-16 下午5:01:01
 * @version ncc1.0
 **/
public class Query28Action implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// 获取前台json
		String read = request.read();
		IJson json = JsonFactory.create();
		SCMQueryTreeFormatVO info = json.fromJson(read, SCMQueryTreeFormatVO.class);
		// 转换成queryscheme
		INCCloudQueryService queryutil = ServiceLocator.find(INCCloudQueryService.class);
		IQueryScheme scheme = queryutil.convertCondition(info.getQueryInfo());
		
		BillGrid[] grids = null;
		try {

			PriceAuditVO[] vos = ServiceLocator.find(IPriceAuditQueryForZ2.class).queryForZ2(scheme, UFBoolean.FALSE);
			// 转换成dto
			if (vos != null && vos.length > 0) {
				BillGridOperator operator = new BillGridOperator(info.getTempletid(), info.getPageCode());
				grids = operator.toBillGrids(vos);
				// PrecisionUtil.setM5APrecision(grids);
			}
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}

		return grids;
	}

}

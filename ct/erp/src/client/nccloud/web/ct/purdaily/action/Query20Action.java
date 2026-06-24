package nccloud.web.ct.purdaily.action;

import nc.pubitf.pu.m20.ct.z2.IQuery20ForZ2;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.scmpub.util.ArrayUtil;
import nc.vo.scmpub.util.StringUtil;
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
 * @description 请购单查询
 * @author xiahui
 * @date 创建时间：2019-2-16 下午5:01:01
 * @version ncc1.0
 **/
public class Query20Action implements ICommonAction {
	protected static final String OPERATE_EQS = "=";
	/** 采购组织fieldcode **/
	public static final String PK_PRAYBILL_B_PK_PURCHASEORG = "pk_praybill_b.pk_purchaseorg";

	@Override
	public Object doAction(IRequest request) {
		// 获取前台json
		String read = request.read();
		IJson json = JsonFactory.create();
		SCMQueryTreeFormatVO info = json.fromJson(read, SCMQueryTreeFormatVO.class);
		// 转换成queryscheme
		INCCloudQueryService queryutil = ServiceLocator.find(INCCloudQueryService.class);
		IQueryScheme scheme = queryutil.convertCondition(info.getQueryInfo());

		this.checkQueryCond(scheme);

		BillGrid[] grids = null;
		try {

			PraybillVO[] vos = ServiceLocator.find(IQuery20ForZ2.class).queryPrayBills(scheme);
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

	/**
	 * 转单检查查询条件:下游主组织是否已经录入唯一值
	 * 
	 * @param qs
	 */
	protected void checkQueryCond(IQueryScheme scheme) {
		String orgFieldCode = this.getRefOrgFieldCode();
		if (StringUtil.isEmptyTrimSpace(orgFieldCode)) {
			return;
		}
		QueryCondition qc = new QuerySchemeProcessor(scheme).getQueryCondition(orgFieldCode);
		if (null == qc || !Query20Action.OPERATE_EQS.equals(qc.getOperator()) || ArrayUtil.isEmpty(qc.getValues())
				|| null == qc.getValues()[0]) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004000_0",
					"04004000-0040")/* @res "参照生单，下游主组织是必输唯一条件，请修改查询条件！" */);
		}
	}

	/**
	 * 得到下游组织查询模板字段名称
	 * 
	 * @return
	 */
	protected String getRefOrgFieldCode() {
		return Query20Action.PK_PRAYBILL_B_PK_PURCHASEORG;
	}

}

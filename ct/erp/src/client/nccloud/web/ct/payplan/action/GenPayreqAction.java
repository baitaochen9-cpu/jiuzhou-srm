package nccloud.web.ct.payplan.action;

import java.util.List;
import java.util.Map;

import nc.bs.pub.relatedApp.RelatedAppVO;
import nc.itf.ct.purdaily.ICtPayPlanQuery;
import nc.itf.scmpub.reference.uap.bd.accesor.FinanceorgAccessor;
import nc.scmmm.pub.scmpub.report.baseutil.SCMNCBaseTypeUtils;
import nc.vo.cmp.pub.IApplyConst;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.scmpub.relateapp.service.IBillRelateAppService;
import nccloud.web.ct.pub.action.OperateInfo;
import nccloud.web.ct.pub.utils.CTSysParamUtil;

/**
 * @description 生成付款申请
 * @author xiahui
 * @date 创建时间：2019-3-6 上午8:41:29
 * @version ncc1.0
 **/
public class GenPayreqAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		if (!CTSysParamUtil.isCMPEnabled()) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
					"04001001-0314")); // 现金管理未启用
		}

		String read = request.read();
		IJson json = JsonFactory.create();
		OperateInfo[] info = json.fromJson(read, OperateInfo[].class);
		Map<String, UFDateTime> map = OperateInfo.convertToMap(info);

		try {
			PayPlanViewVO[] viewVOs = ServiceLocator.find(ICtPayPlanQuery.class).queryPayPlanViews(
					map.keySet().toArray(new String[map.keySet().size()]));

			OperateExceptionUtils.checkVo(viewVOs, null);			// 检验单据删除
			this.checkViewVOs(viewVOs);
			
			IBillRelateAppService service = ServiceLocator.find(IBillRelateAppService.class);
			List<RelatedAppVO> relatedAppVos = service.getRelatedAppVos(CTBillType.PurDaily.getCode(), null,
					IApplyConst.CMP_36D1, 1);
			return relatedAppVos;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 数据校验
	 * 
	 * @param viewVOs
	 */
	private void checkViewVOs(PayPlanViewVO[] viewVOs) {
		for (PayPlanViewVO viewVO : viewVOs) {
			String pk_org = viewVO.getPk_financeorg();
			if (!CTSysParamUtil.getPO88(pk_org)) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
						"04001001-0315", null, new String[] { FinanceorgAccessor.getDocByPk(pk_org).getName().toString() })); // 应付财务组织{0}中，参数“采购订单和合同付款是否需做付款申请”值为否，不能生成付款申请！
			}

			if (null == viewVO.getDenddate()) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
						"04001001-0083")); // "存在账期到期日为空的行，请检查!"

			}

			if (viewVO != null && SCMNCBaseTypeUtils.isEquals(viewVO.getNorigmny(), viewVO.getNaccumpayapporgmny())) {
				ExceptionUtils
						.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0331")); // 当付款计划金额等于付款申请的金额时，不可以再生成付款申请
			}
			if (viewVO != null && !CtFlowEnum.VALIDATE.value().equals(viewVO.getFstatusflag())) {
				ExceptionUtils
						.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0332")); // 非生效态合同不能生成付款申请，请检查！
			}
		}
	}
}

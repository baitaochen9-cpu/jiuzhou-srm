package nccloud.web.ct.payplan.action;

import java.util.List;
import java.util.Map;

import nc.bs.arap.util.IArapBillTypeCons;
import nc.bs.pub.relatedApp.RelatedAppVO;
import nc.itf.ct.purdaily.ICtPayPlanQuery;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.MathTool;
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
 * @description 付款
 * @author xiahui
 * @date 创建时间：2019-3-6 上午10:52:40
 * @version ncc1.0
 **/
public class PaymentAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		if (!CTSysParamUtil.isAPEnabled()) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
					"04001001-0076")); // 应付未启用
		}

		String read = request.read();
		IJson json = JsonFactory.create();
		OperateInfo[] info = json.fromJson(read, OperateInfo[].class);
		Map<String, UFDateTime> map = OperateInfo.convertToMap(info);

		try {
			PayPlanViewVO[] viewVOs = ServiceLocator.find(ICtPayPlanQuery.class).queryPayPlanViews(
					map.keySet().toArray(new String[map.keySet().size()]));

			OperateExceptionUtils.checkVo(viewVOs, null); // 检验单据删除
			this.checkViewVOs(viewVOs);

			IBillRelateAppService service = ServiceLocator.find(IBillRelateAppService.class);
			// 获取下游交易类型 通过单据接口定义获取
			List<RelatedAppVO> relatedAppVos = service.getRelatedAppVos(CTBillType.PurDaily.getCode(), null,
					IArapBillTypeCons.D3, 1);
			return relatedAppVos;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private void checkViewVOs(PayPlanViewVO[] viewVOs) {
		String pk_org = viewVOs[0].getPk_financeorg();
		if (CTSysParamUtil.getPO88(pk_org)) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
					"04001001-0078")); // 参数“采购订单和合同付款是否需做付款申请”值为是，不能生成付款单
		}

		boolean isAllZero = true;
		for (PayPlanViewVO viewVO : viewVOs) {
			String pk_financeorg = viewVO.getPk_financeorg();
			if (!pk_org.equals(pk_financeorg)) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
						"04001001-0077")); // 不可以同时针对多个应付财务组织进行付款
			}

			if (UFDouble.ZERO_DBL.equals(viewVO.getNorigmny())
					|| MathTool.greaterThan(UFDouble.ZERO_DBL, viewVO.getNorigmny())) {
				String rows = viewVO.getCrowno();
				ExceptionUtils.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0", "04001001-0266", null,
						new String[] { rows })); // {0}行原币金额小于等于0，不能付款。
			}

			UFDouble ncanpayorgmny = MathTool.sub(viewVO.getNorigmny(), viewVO.getNaccumpayorgmny());
			if (MathTool.compareTo(ncanpayorgmny, UFDouble.ZERO_DBL) <= 0) {
				continue;
			}

			if (viewVO != null && !CtFlowEnum.VALIDATE.value().equals(viewVO.getFstatusflag())) {
				ExceptionUtils
						.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0332"));// 非生效态合同不能生成付款申请，请检查！
			}
			
			isAllZero = false;
		}

		if (isAllZero) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4001001_0",
					"04001001-0079")); // 所选满足条件行已经生成付款！
			return;
		}
	}

}

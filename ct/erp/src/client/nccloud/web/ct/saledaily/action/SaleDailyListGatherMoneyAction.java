package nccloud.web.ct.saledaily.action;

import nc.itf.ct.saledaily.ISaledailyMaintain;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.RecvPlanVO;
import nc.vo.ml.NCLangRes4VoTransl;
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

/**
 * @description 联查收款计划
 * @author wangshrc
 * @date 2019年2月26日 上午10:56:58
 * @version ncc1.0
 */
public class SaleDailyListGatherMoneyAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		GridOperator operator = new GridOperator("400600200_payplan");
		SaleDailyQueryInfo info = json.fromJson(str, SaleDailyQueryInfo.class);
		String id = info.getPk();
		ISaledailyMaintain service = ServiceLocator
				.find(ISaledailyMaintain.class);
		AggCtSaleVO[] aggVos = null;
		Grid grid = null;
		try {
			aggVos = service.queryCtApVoByIds(new String[] { id });
			if (aggVos.length != 1)
				return null;
			AggCtSaleVO saleVO = aggVos[0];
			IBusinessTypeService busiService = ServiceLocator
					.find(IBusinessTypeService.class);
			BusinessSetVO businessvo = busiService.queryBusinessVO(saleVO
					.getParentVO().getCtrantypeid());
			if (businessvo.getBshowpayterm().booleanValue()) {
				ExceptionUtils.wrapBusinessException(NCLangRes4VoTransl
						.getNCLangRes().getStrByID(
								"4020003_0",
								"04020003-0442",
								null,
								new String[] { aggVos[0].getParentVO()
										.getVbillcode() }));// 合同{0}不支持收款计划！);
			}
			String vbillcode = saleVO.getParentVO().getVbillcode();
			RecvPlanVO[] planvos = service.queryRecPlanVO(saleVO.getParentVO()
					.getPk_ct_sale(), vbillcode);
			if (planvos == null)
				return null;
			grid = operator.toGrid(planvos);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return grid;
	}

}

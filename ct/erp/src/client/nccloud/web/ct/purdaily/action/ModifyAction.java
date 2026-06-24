package nccloud.web.ct.purdaily.action;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuChangeVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.pub.action.ExtBaseSaveAction;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;

/**
 * @description 变更
 * @author xiahui
 * @date 创建时间：2019-1-21 上午9:26:46
 * @version ncc1.0
 **/
public class ModifyAction extends ExtBaseSaveAction<AggCtPuVO> {

	@Override
	public AggCtPuVO excute(AggCtPuVO bill) {
		this.beforeProcess(bill);

		CloudPFlowContext context = new CloudPFlowContext();
		context.setActionName("MODIFY");
		context.setBillType("Z2");
		context.setBillVos(new AggCtPuVO[] { bill });
		// 执行提交动作脚本
		SCMScriptResultDTO result = ServiceLocator.find(IBatchRunScriptService.class).runBacth(context,
				AggCtPuVO.class);
		AbstractBill[] retvos = result.getSucessVOs();
		return (AggCtPuVO) retvos[0];
	}

	private void beforeProcess(AggCtPuVO bill) {
		this.setChangeVOWhenModify(bill);
	}

	private void setChangeVOWhenModify(AggCtPuVO ctVO) {
		CtPuVO ctHeadVo = ctVO.getParentVO();
		if (ctHeadVo.getFstatusflag() != CtFlowEnum.VALIDATE.toIntValue())
			return;

		// 补充ChangeVO信息
		CtPuChangeVO changeVO = ctVO.getCtPuChangeVO()[ctVO.getCtPuChangeVO().length - 1];
		changeVO.setPk_group(ctHeadVo.getPk_group());
		changeVO.setPk_org(ctHeadVo.getPk_org());
		changeVO.setPk_org_v(ctHeadVo.getPk_org_v());
		changeVO.setVchgdate(new UFDate(SessionContext.getInstance().getClientInfo().getBizDateTime()));
		changeVO.setVchgpsn(SessionContext.getInstance().getClientInfo().getUserid());
	}

	@Override
	protected void afterProcess(ExtBillCard retCard) {
		// 精度处理
		PrecisionUtil.setExtCardPrecision(retCard);
	}

}

package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.itf.jzqc.ILabelprintapplyMaintain;

public class N_JZ11_UNAPPROVE extends AbstractPfAction<AggLabelprintapplyHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelprintapplyHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelprintapplyHVO> processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
				LabelprintapplyPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggLabelprintapplyHVO[] processBP(Object userObj,
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggLabelprintapplyHVO[] bills = null;
		try {
			ILabelprintapplyMaintain operator = NCLocator.getInstance()
					.lookup(ILabelprintapplyMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

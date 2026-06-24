package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.jzqc.ILabelprintapplyMaintain;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class N_JZ11_APPROVE extends AbstractPfAction<AggLabelprintapplyHVO> {

	public N_JZ11_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggLabelprintapplyHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelprintapplyHVO> processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
				LabelprintapplyPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggLabelprintapplyHVO[] processBP(Object userObj,
			AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) {
		AggLabelprintapplyHVO[] bills = null;
		ILabelprintapplyMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintapplyMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (Exception e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}

		return bills;
	}
}

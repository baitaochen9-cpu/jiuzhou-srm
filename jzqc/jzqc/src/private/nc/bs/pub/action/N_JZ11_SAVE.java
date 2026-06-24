package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.itf.jzqc.ILabelprintapplyMaintain;

public class N_JZ11_SAVE extends AbstractPfAction<AggLabelprintapplyHVO> {

	protected CompareAroundProcesser<AggLabelprintapplyHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelprintapplyHVO> processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
				LabelprintapplyPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggLabelprintapplyHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggLabelprintapplyHVO[] processBP(Object userObj,
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills) {
		ILabelprintapplyMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintapplyMaintain.class);
		AggLabelprintapplyHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

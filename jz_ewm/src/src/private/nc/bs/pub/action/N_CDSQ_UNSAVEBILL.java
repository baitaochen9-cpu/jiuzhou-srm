package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.ewm.printapply.plugin.bpplugin.PrintapplyPluginPoint;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.itf.ewm.IPrintapplyMaintain;

public class N_CDSQ_UNSAVEBILL extends AbstractPfAction<AggPrintapply> {

	@Override
	protected CompareAroundProcesser<AggPrintapply> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPrintapply> processor = new CompareAroundProcesser<AggPrintapply>(
				PrintapplyPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggPrintapply[] processBP(Object userObj,
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills) {
		IPrintapplyMaintain operator = NCLocator.getInstance().lookup(
				IPrintapplyMaintain.class);
		AggPrintapply[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

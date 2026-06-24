package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.ewm.printapply.plugin.bpplugin.PrintapplyPluginPoint;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.itf.ewm.IPrintapplyMaintain;

public class N_CD11_SAVEBASE extends AbstractPfAction<AggPrintapply> {

	@Override
	protected CompareAroundProcesser<AggPrintapply> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPrintapply> processor = null;
		AggPrintapply[] clientFullVOs = (AggPrintapply[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggPrintapply>(
					PrintapplyPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggPrintapply>(
					PrintapplyPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggPrintapply> rule = null;

		return processor;
	}

	@Override
	protected AggPrintapply[] processBP(Object userObj,
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills) {

		AggPrintapply[] bills = null;
		try {
			IPrintapplyMaintain operator = NCLocator.getInstance()
					.lookup(IPrintapplyMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}

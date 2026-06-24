package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;

public class N_GMP1_SAVE extends AbstractPfAction<AggGmpLogConfigHvo> {

	protected CompareAroundProcesser<AggGmpLogConfigHvo> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGmpLogConfigHvo> processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggGmpLogConfigHvo> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggGmpLogConfigHvo[] processBP(Object userObj,
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills) {
		IGmpLogMaintain operator = NCLocator.getInstance().lookup(
				IGmpLogMaintain.class);
		AggGmpLogConfigHvo[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

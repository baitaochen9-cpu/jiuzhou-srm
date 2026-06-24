package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;

public class N_GMP1_APPROVE extends AbstractPfAction<AggGmpLogConfigHvo> {

	public N_GMP1_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggGmpLogConfigHvo> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGmpLogConfigHvo> processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggGmpLogConfigHvo[] processBP(Object userObj,
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills) {
		AggGmpLogConfigHvo[] bills = null;
		IGmpLogMaintain operator = NCLocator.getInstance().lookup(
				IGmpLogMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

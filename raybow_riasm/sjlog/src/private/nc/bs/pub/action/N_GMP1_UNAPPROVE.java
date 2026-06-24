package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;

public class N_GMP1_UNAPPROVE extends AbstractPfAction<AggGmpLogConfigHvo> {

	@Override
	protected CompareAroundProcesser<AggGmpLogConfigHvo> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGmpLogConfigHvo> processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggGmpLogConfigHvo[] processBP(Object userObj,
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggGmpLogConfigHvo[] bills = null;
		try {
			IGmpLogMaintain operator = NCLocator.getInstance()
					.lookup(IGmpLogMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

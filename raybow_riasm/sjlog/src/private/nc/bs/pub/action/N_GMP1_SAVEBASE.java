package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;

public class N_GMP1_SAVEBASE extends AbstractPfAction<AggGmpLogConfigHvo> {

	@Override
	protected CompareAroundProcesser<AggGmpLogConfigHvo> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGmpLogConfigHvo> processor = null;
		AggGmpLogConfigHvo[] clientFullVOs = (AggGmpLogConfigHvo[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
					GmpLogPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
					GmpLogPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggGmpLogConfigHvo> rule = null;

		return processor;
	}

	@Override
	protected AggGmpLogConfigHvo[] processBP(Object userObj,
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills) {

		AggGmpLogConfigHvo[] bills = null;
		try {
			IGmpLogMaintain operator = NCLocator.getInstance()
					.lookup(IGmpLogMaintain.class);
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

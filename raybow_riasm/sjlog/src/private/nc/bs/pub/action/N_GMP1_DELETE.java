package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;

public class N_GMP1_DELETE extends AbstractPfAction<AggGmpLogConfigHvo> {

	@Override
	protected CompareAroundProcesser<AggGmpLogConfigHvo> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGmpLogConfigHvo> processor = new CompareAroundProcesser<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggGmpLogConfigHvo[] processBP(Object userObj,
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills) {
		IGmpLogMaintain operator = NCLocator.getInstance().lookup(
				IGmpLogMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

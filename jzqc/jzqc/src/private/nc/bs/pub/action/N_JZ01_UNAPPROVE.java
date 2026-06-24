package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.itf.jzqc.ILabelprintMaintain;

public class N_JZ01_UNAPPROVE extends AbstractPfAction<AggLabelPrintHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelPrintHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelPrintHVO> processor = new CompareAroundProcesser<AggLabelPrintHVO>(
				LabelprintPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggLabelPrintHVO[] processBP(Object userObj,
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggLabelPrintHVO[] bills = null;
		try {
			ILabelprintMaintain operator = NCLocator.getInstance()
					.lookup(ILabelprintMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

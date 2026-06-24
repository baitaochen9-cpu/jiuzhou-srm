package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.itf.jzqc.ILabelprintMaintain;

public class N_JZ01_APPROVE extends AbstractPfAction<AggLabelPrintHVO> {

	public N_JZ01_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggLabelPrintHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelPrintHVO> processor = new CompareAroundProcesser<AggLabelPrintHVO>(
				LabelprintPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggLabelPrintHVO[] processBP(Object userObj,
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills) {
		AggLabelPrintHVO[] bills = null;
		ILabelprintMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

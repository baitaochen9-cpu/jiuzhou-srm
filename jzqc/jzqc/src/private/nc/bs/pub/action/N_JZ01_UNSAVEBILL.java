package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.itf.jzqc.ILabelprintMaintain;

public class N_JZ01_UNSAVEBILL extends AbstractPfAction<AggLabelPrintHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelPrintHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelPrintHVO> processor = new CompareAroundProcesser<AggLabelPrintHVO>(
				LabelprintPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggLabelPrintHVO[] processBP(Object userObj,
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills) {
		ILabelprintMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintMaintain.class);
		AggLabelPrintHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.itf.jzqc.ILabelprintMaintain;

public class N_JZ01_DELETE extends AbstractPfAction<AggLabelPrintHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelPrintHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelPrintHVO> processor = new CompareAroundProcesser<AggLabelPrintHVO>(
				LabelprintPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggLabelPrintHVO[] processBP(Object userObj,
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills) {
		ILabelprintMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

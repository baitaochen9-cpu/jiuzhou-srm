package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.itf.jzqc.ILabelprintMaintain;

public class N_JZ01_SAVEBASE extends AbstractPfAction<AggLabelPrintHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelPrintHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelPrintHVO> processor = null;
		AggLabelPrintHVO[] clientFullVOs = (AggLabelPrintHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggLabelPrintHVO>(
					LabelprintPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggLabelPrintHVO>(
					LabelprintPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggLabelPrintHVO> rule = null;

		return processor;
	}

	@Override
	protected AggLabelPrintHVO[] processBP(Object userObj,
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills) {

		AggLabelPrintHVO[] bills = null;
		try {
			ILabelprintMaintain operator = NCLocator.getInstance()
					.lookup(ILabelprintMaintain.class);
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

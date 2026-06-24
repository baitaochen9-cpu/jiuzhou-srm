package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.itf.jzqc.ILabelprintapplyMaintain;

public class N_JZ11_SAVEBASE extends AbstractPfAction<AggLabelprintapplyHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelprintapplyHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelprintapplyHVO> processor = null;
		AggLabelprintapplyHVO[] clientFullVOs = (AggLabelprintapplyHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
					LabelprintapplyPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
					LabelprintapplyPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggLabelprintapplyHVO> rule = null;

		return processor;
	}

	@Override
	protected AggLabelprintapplyHVO[] processBP(Object userObj,
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills) {

		AggLabelprintapplyHVO[] bills = null;
		try {
			ILabelprintapplyMaintain operator = NCLocator.getInstance()
					.lookup(ILabelprintapplyMaintain.class);
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

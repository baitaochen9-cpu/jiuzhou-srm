package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.itf.jzqc.ILabelprintapplyMaintain;

public class N_JZ11_DELETE extends AbstractPfAction<AggLabelprintapplyHVO> {

	@Override
	protected CompareAroundProcesser<AggLabelprintapplyHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggLabelprintapplyHVO> processor = new CompareAroundProcesser<AggLabelprintapplyHVO>(
				LabelprintapplyPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggLabelprintapplyHVO[] processBP(Object userObj,
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills) {
		ILabelprintapplyMaintain operator = NCLocator.getInstance().lookup(
				ILabelprintapplyMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

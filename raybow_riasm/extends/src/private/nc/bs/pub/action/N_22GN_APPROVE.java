package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.itf.riasm.IElectronicsignatureMaintain;

public class N_22GN_APPROVE extends AbstractPfAction<AggElectronicSignatureVO> {

	public N_22GN_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggElectronicSignatureVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggElectronicSignatureVO> processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
				ElectronicsignaturePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggElectronicSignatureVO[] processBP(Object userObj,
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills) {
		AggElectronicSignatureVO[] bills = null;
		IElectronicsignatureMaintain operator = NCLocator.getInstance().lookup(
				IElectronicsignatureMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

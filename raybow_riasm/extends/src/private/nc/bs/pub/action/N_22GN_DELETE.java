package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.itf.riasm.IElectronicsignatureMaintain;

public class N_22GN_DELETE extends AbstractPfAction<AggElectronicSignatureVO> {

	@Override
	protected CompareAroundProcesser<AggElectronicSignatureVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggElectronicSignatureVO> processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
				ElectronicsignaturePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggElectronicSignatureVO[] processBP(Object userObj,
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills) {
		IElectronicsignatureMaintain operator = NCLocator.getInstance().lookup(
				IElectronicsignatureMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

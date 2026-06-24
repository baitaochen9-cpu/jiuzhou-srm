package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.itf.riasm.IElectronicsignatureMaintain;

public class N_22GN_UNAPPROVE extends AbstractPfAction<AggElectronicSignatureVO> {

	@Override
	protected CompareAroundProcesser<AggElectronicSignatureVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggElectronicSignatureVO> processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
				ElectronicsignaturePluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggElectronicSignatureVO[] processBP(Object userObj,
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggElectronicSignatureVO[] bills = null;
		try {
			IElectronicsignatureMaintain operator = NCLocator.getInstance()
					.lookup(IElectronicsignatureMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

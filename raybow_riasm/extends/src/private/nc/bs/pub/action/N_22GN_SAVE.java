package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.itf.riasm.IElectronicsignatureMaintain;

public class N_22GN_SAVE extends AbstractPfAction<AggElectronicSignatureVO> {

	protected CompareAroundProcesser<AggElectronicSignatureVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggElectronicSignatureVO> processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
				ElectronicsignaturePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggElectronicSignatureVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggElectronicSignatureVO[] processBP(Object userObj,
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills) {
		IElectronicsignatureMaintain operator = NCLocator.getInstance().lookup(
				IElectronicsignatureMaintain.class);
		AggElectronicSignatureVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

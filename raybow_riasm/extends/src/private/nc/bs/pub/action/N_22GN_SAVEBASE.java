package nc.bs.pub.action;

import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.riasm.IElectronicsignatureMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureBVO;

public class N_22GN_SAVEBASE extends AbstractPfAction<AggElectronicSignatureVO> {

	@Override
	protected CompareAroundProcesser<AggElectronicSignatureVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggElectronicSignatureVO> processor = null;
		AggElectronicSignatureVO[] clientFullVOs = (AggElectronicSignatureVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
					ElectronicsignaturePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggElectronicSignatureVO>(
					ElectronicsignaturePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggElectronicSignatureVO> rule = null;

		return processor;
	}

	@Override
	protected AggElectronicSignatureVO[] processBP(Object userObj,
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills) {

		AggElectronicSignatureVO[] bills = null;
		try {
			for(AggElectronicSignatureVO aggvo :clientFullVOs){
				CircularlyAccessibleValueObject[] vos =aggvo.getChildrenVO();
				if(vos == null)
					continue;
				List<CircularlyAccessibleValueObject> list = new ArrayList<>();
				for(CircularlyAccessibleValueObject vo : vos){
					ElectronicSignatureBVO bvo =(ElectronicSignatureBVO) vo;
					if(!StringUtil.isEmpty(bvo.getBtnname()))
						list.add(bvo);
				}
				aggvo.setChildrenVO(list.toArray(new ElectronicSignatureBVO[list.size()]));
			}
			
			IElectronicsignatureMaintain operator = NCLocator.getInstance()
					.lookup(IElectronicsignatureMaintain.class);
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

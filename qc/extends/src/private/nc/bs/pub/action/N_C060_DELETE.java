package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.itf.qc.ISupplierqualityapplyMaintain;

public class N_C060_DELETE extends AbstractPfAction<AggSupplierQualityApplyVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierQualityApplyVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierQualityApplyVO> processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggSupplierQualityApplyVO[] processBP(Object userObj,
			AggSupplierQualityApplyVO[] clientFullVOs, AggSupplierQualityApplyVO[] originBills) {
		ISupplierqualityapplyMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualityapplyMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

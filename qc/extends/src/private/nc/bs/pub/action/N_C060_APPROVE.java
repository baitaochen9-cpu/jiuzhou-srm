package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.itf.qc.ISupplierqualityapplyMaintain;

public class N_C060_APPROVE extends AbstractPfAction<AggSupplierQualityApplyVO> {

	public N_C060_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSupplierQualityApplyVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierQualityApplyVO> processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggSupplierQualityApplyVO[] processBP(Object userObj,
			AggSupplierQualityApplyVO[] clientFullVOs, AggSupplierQualityApplyVO[] originBills) {
		AggSupplierQualityApplyVO[] bills = null;
		ISupplierqualityapplyMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualityapplyMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

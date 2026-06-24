package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.itf.qc.ISupplierqualityapplyMaintain;

public class N_C060_UNAPPROVE extends AbstractPfAction<AggSupplierQualityApplyVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierQualityApplyVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierQualityApplyVO> processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSupplierQualityApplyVO[] processBP(Object userObj,
			AggSupplierQualityApplyVO[] clientFullVOs, AggSupplierQualityApplyVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggSupplierQualityApplyVO[] bills = null;
		try {
			ISupplierqualityapplyMaintain operator = NCLocator.getInstance()
					.lookup(ISupplierqualityapplyMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

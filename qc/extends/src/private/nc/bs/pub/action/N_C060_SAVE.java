package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.itf.qc.ISupplierqualityapplyMaintain;

public class N_C060_SAVE extends AbstractPfAction<AggSupplierQualityApplyVO> {

	protected CompareAroundProcesser<AggSupplierQualityApplyVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierQualityApplyVO> processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggSupplierQualityApplyVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggSupplierQualityApplyVO[] processBP(Object userObj,
			AggSupplierQualityApplyVO[] clientFullVOs, AggSupplierQualityApplyVO[] originBills) {
		ISupplierqualityapplyMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualityapplyMaintain.class);
		AggSupplierQualityApplyVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

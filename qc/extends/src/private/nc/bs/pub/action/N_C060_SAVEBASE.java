package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.itf.qc.ISupplierqualityapplyMaintain;

public class N_C060_SAVEBASE extends AbstractPfAction<AggSupplierQualityApplyVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierQualityApplyVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierQualityApplyVO> processor = null;
		AggSupplierQualityApplyVO[] clientFullVOs = (AggSupplierQualityApplyVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
					SupplierqualityapplyPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSupplierQualityApplyVO>(
					SupplierqualityapplyPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSupplierQualityApplyVO> rule = null;

		return processor;
	}

	@Override
	protected AggSupplierQualityApplyVO[] processBP(Object userObj,
			AggSupplierQualityApplyVO[] clientFullVOs, AggSupplierQualityApplyVO[] originBills) {

		AggSupplierQualityApplyVO[] bills = null;
		try {
			ISupplierqualityapplyMaintain operator = NCLocator.getInstance()
					.lookup(ISupplierqualityapplyMaintain.class);
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

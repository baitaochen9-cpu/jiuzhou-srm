package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.itf.qc.ISupplierqualitystatusMaintain;

public class N_C055_SAVEBASE extends AbstractPfAction<AggSupplierqualityHVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierqualityHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierqualityHVO> processor = null;
		AggSupplierqualityHVO[] clientFullVOs = (AggSupplierqualityHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
					SupplierqualitystatusPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
					SupplierqualitystatusPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSupplierqualityHVO> rule = null;

		return processor;
	}

	@Override
	protected AggSupplierqualityHVO[] processBP(Object userObj,
			AggSupplierqualityHVO[] clientFullVOs, AggSupplierqualityHVO[] originBills) {

		AggSupplierqualityHVO[] bills = null;
		try {
			ISupplierqualitystatusMaintain operator = NCLocator.getInstance()
					.lookup(ISupplierqualitystatusMaintain.class);
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

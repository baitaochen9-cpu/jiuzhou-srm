package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.itf.qc.ISupplierqualitystatusMaintain;

public class N_C055_APPROVE extends AbstractPfAction<AggSupplierqualityHVO> {

	public N_C055_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSupplierqualityHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierqualityHVO> processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggSupplierqualityHVO[] processBP(Object userObj,
			AggSupplierqualityHVO[] clientFullVOs, AggSupplierqualityHVO[] originBills) {
		AggSupplierqualityHVO[] bills = null;
		ISupplierqualitystatusMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualitystatusMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

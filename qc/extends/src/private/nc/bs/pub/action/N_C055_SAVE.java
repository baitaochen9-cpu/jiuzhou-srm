package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.itf.qc.ISupplierqualitystatusMaintain;

public class N_C055_SAVE extends AbstractPfAction<AggSupplierqualityHVO> {

	protected CompareAroundProcesser<AggSupplierqualityHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierqualityHVO> processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggSupplierqualityHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggSupplierqualityHVO[] processBP(Object userObj,
			AggSupplierqualityHVO[] clientFullVOs, AggSupplierqualityHVO[] originBills) {
		ISupplierqualitystatusMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualitystatusMaintain.class);
		AggSupplierqualityHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.itf.qc.ISupplierqualitystatusMaintain;

public class N_C055_UNAPPROVE extends AbstractPfAction<AggSupplierqualityHVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierqualityHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierqualityHVO> processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSupplierqualityHVO[] processBP(Object userObj,
			AggSupplierqualityHVO[] clientFullVOs, AggSupplierqualityHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggSupplierqualityHVO[] bills = null;
		try {
			ISupplierqualitystatusMaintain operator = NCLocator.getInstance()
					.lookup(ISupplierqualitystatusMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

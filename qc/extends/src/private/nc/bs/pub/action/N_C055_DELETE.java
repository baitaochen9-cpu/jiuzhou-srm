package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.itf.qc.ISupplierqualitystatusMaintain;

public class N_C055_DELETE extends AbstractPfAction<AggSupplierqualityHVO> {

	@Override
	protected CompareAroundProcesser<AggSupplierqualityHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSupplierqualityHVO> processor = new CompareAroundProcesser<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggSupplierqualityHVO[] processBP(Object userObj,
			AggSupplierqualityHVO[] clientFullVOs, AggSupplierqualityHVO[] originBills) {
		ISupplierqualitystatusMaintain operator = NCLocator.getInstance().lookup(
				ISupplierqualitystatusMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}

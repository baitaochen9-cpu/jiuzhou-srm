package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.itf.so.ISalepacklistMaintain;

public class N_4345_APPROVE extends AbstractPfAction<AggSalePackListHVO> {

	public N_4345_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSalePackListHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalePackListHVO> processor = new CompareAroundProcesser<AggSalePackListHVO>(
				SalepacklistPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggSalePackListHVO[] processBP(Object userObj,
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills) {
		AggSalePackListHVO[] bills = null;
		ISalepacklistMaintain operator = NCLocator.getInstance().lookup(
				ISalepacklistMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

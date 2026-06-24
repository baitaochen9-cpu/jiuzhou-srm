package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.itf.so.ISalepacklistMaintain;

public class N_4345_UNSAVEBILL extends AbstractPfAction<AggSalePackListHVO> {

	@Override
	protected CompareAroundProcesser<AggSalePackListHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalePackListHVO> processor = new CompareAroundProcesser<AggSalePackListHVO>(
				SalepacklistPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSalePackListHVO[] processBP(Object userObj,
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills) {
		ISalepacklistMaintain operator = NCLocator.getInstance().lookup(
				ISalepacklistMaintain.class);
		AggSalePackListHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

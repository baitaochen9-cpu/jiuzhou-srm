package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.itf.so.ISalepacklistMaintain;

public class N_4345_SAVEBASE extends AbstractPfAction<AggSalePackListHVO> {

	@Override
	protected CompareAroundProcesser<AggSalePackListHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalePackListHVO> processor = null;
		AggSalePackListHVO[] clientFullVOs = (AggSalePackListHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSalePackListHVO>(
					SalepacklistPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSalePackListHVO>(
					SalepacklistPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSalePackListHVO> rule = null;

		return processor;
	}

	@Override
	protected AggSalePackListHVO[] processBP(Object userObj,
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills) {

		AggSalePackListHVO[] bills = null;
		try {
			ISalepacklistMaintain operator = NCLocator.getInstance()
					.lookup(ISalepacklistMaintain.class);
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

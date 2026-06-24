package nc.bs.pub.action;

import nc.bd.ewm.print.IProcessService;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.ewm.printapply.plugin.bpplugin.PrintapplyPluginPoint;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.itf.ewm.IPrintapplyMaintain;

public class N_CDSQ_APPROVE extends AbstractPfAction<AggPrintapply> {

	public N_CDSQ_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggPrintapply> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPrintapply> processor = new CompareAroundProcesser<AggPrintapply>(
				PrintapplyPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggPrintapply[] processBP(Object userObj,
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills) {
		AggPrintapply[] bills = null;
		IPrintapplyMaintain operator = NCLocator.getInstance().lookup(
				IPrintapplyMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
			IProcessService service = NCLocator.getInstance().lookup(IProcessService.class);
			//更新为可打印
			service.printDefUpdate(bills);
			//更新
			String qryCount = service.qryCount(bills);
			service.printCountUpdate(Integer.parseInt(qryCount),bills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

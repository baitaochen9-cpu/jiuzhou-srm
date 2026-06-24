package nc.bs.pub.action;

import org.apache.commons.lang3.StringUtils;

import nc.bd.ewm.print.IProcessService;
import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.ewm.printapply.plugin.bpplugin.PrintapplyPluginPoint;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.itf.ewm.IPrintapplyMaintain;

public class N_CDSQ_UNAPPROVE extends AbstractPfAction<AggPrintapply> {

	@Override
	protected CompareAroundProcesser<AggPrintapply> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPrintapply> processor = new CompareAroundProcesser<AggPrintapply>(
				PrintapplyPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggPrintapply[] processBP(Object userObj,
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggPrintapply[] bills = null;
		try {
			IPrintapplyMaintain operator = NCLocator.getInstance()
					.lookup(IPrintapplyMaintain.class);
			IProcessService service = NCLocator.getInstance().lookup(IProcessService.class);
			String qryCount = service.qryCount(clientFullVOs);
			String def2 = clientFullVOs[0].getParentVO().getDef2();
			if(StringUtils.isBlank(def2) || Integer.parseInt(qryCount)>Integer.parseInt(def2)){
				throw new BusinessException("工单已打印，请重新申请");
			}
			bills = operator.unapprove(clientFullVOs, originBills);
			service.printDefUnUpdate(bills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}

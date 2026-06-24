package nc.bs.ewm.printapply.ace.bp;

import nc.bs.ewm.printapply.plugin.bpplugin.PrintapplyPluginPoint;
import nc.vo.ewm.printapply.AggPrintapply;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AcePrintapplyDeleteBP {

	public void delete(AggPrintapply[] bills) {

		DeleteBPTemplate<AggPrintapply> bp = new DeleteBPTemplate<AggPrintapply>(
				PrintapplyPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggPrintapply> processer) {
		// TODO 前规则
		IRule<AggPrintapply> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggPrintapply> processer) {
		// TODO 后规则

	}
}

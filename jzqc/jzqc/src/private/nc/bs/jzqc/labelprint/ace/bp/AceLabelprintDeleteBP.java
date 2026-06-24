package nc.bs.jzqc.labelprint.ace.bp;

import nc.bs.jzqc.labelprint.ace.bp.rule.DeleteLablePrintCheckStatusRule;
import nc.bs.jzqc.labelprint.ace.bp.rule.WriteBackArriveNumRule;
import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;


/**
 * 标准单据删除BP
 */
public class AceLabelprintDeleteBP {

	public void delete(AggLabelPrintHVO[] bills) {

		DeleteBPTemplate<AggLabelPrintHVO> bp = new DeleteBPTemplate<AggLabelPrintHVO>(
				LabelprintPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		WriteBackArriveNumRule rule1 = new WriteBackArriveNumRule();
		rule1.process(bills, null, true);
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggLabelPrintHVO> processer) {
		// TODO 前规则
		IRule<AggLabelPrintHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
		
		rule = new DeleteLablePrintCheckStatusRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggLabelPrintHVO> processer) {
		// TODO 后规则

	}
}

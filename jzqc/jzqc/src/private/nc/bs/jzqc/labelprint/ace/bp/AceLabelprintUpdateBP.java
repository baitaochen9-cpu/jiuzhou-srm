package nc.bs.jzqc.labelprint.ace.bp;

import nc.bs.jzqc.labelprint.ace.bp.rule.MaterialVerionBeforeRule;
import nc.bs.jzqc.labelprint.ace.bp.rule.UpdateSerialNORule;
import nc.bs.jzqc.labelprint.ace.bp.rule.WriteBackArriveNumRule;
import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;

/**
 * 修改保存的BP
 * 
 */
public class AceLabelprintUpdateBP {

	public AggLabelPrintHVO[] update(AggLabelPrintHVO[] bills,
			AggLabelPrintHVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggLabelPrintHVO> bp = new UpdateBPTemplate<AggLabelPrintHVO>(
				LabelprintPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		WriteBackArriveNumRule rule1 = new WriteBackArriveNumRule();
		rule1.process(bills, originBills, false);
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggLabelPrintHVO> processer) {
		// TODO 后规则
		IRule<AggLabelPrintHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("JZ01");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggLabelPrintHVO> processer) {
		// TODO 前规则
		IRule<AggLabelPrintHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggLabelPrintHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("JZ01");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
		
		rule = new MaterialVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new UpdateSerialNORule();
		processer.addBeforeRule(rule);
	}

}

package nc.bs.jzqc.labelprint.ace.bp;

import nc.bs.jzqc.labelprint.ace.bp.rule.FillBillTailInfoRuleForIns;
import nc.bs.jzqc.labelprint.ace.bp.rule.FillBillTypeRuleForIns;
import nc.bs.jzqc.labelprint.ace.bp.rule.MaterialVerionBeforeRule;
import nc.bs.jzqc.labelprint.ace.bp.rule.UpdateICAdjustStateRule;
import nc.bs.jzqc.labelprint.ace.bp.rule.UpdateSerialNORule;
import nc.bs.jzqc.labelprint.ace.bp.rule.WriteBackArriveNumRule;
import nc.bs.jzqc.labelprint.plugin.bpplugin.LabelprintPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pubapp.util.SetAddAuditInfoRule;

/**
 * 标准单据新增BP
 */
public class AceLabelprintInsertBP {

	public AggLabelPrintHVO[] insert(AggLabelPrintHVO[] bills) {

		InsertBPTemplate<AggLabelPrintHVO> bp = new InsertBPTemplate<AggLabelPrintHVO>(
				LabelprintPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		WriteBackArriveNumRule rule1 = new WriteBackArriveNumRule();
		rule1.process(bills, null, false);
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggLabelPrintHVO> processor) {
		// TODO 新增后规则
		IRule<AggLabelPrintHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("JZ01");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		
		processor.addAfterRule(rule);
		
		 rule = new UpdateICAdjustStateRule();
		 processor.addAfterRule(rule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggLabelPrintHVO> processer) {
		// TODO 新增前规则
		IRule<AggLabelPrintHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("JZ01");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);
		
		rule = new FillBillTailInfoRuleForIns();
		processer.addBeforeRule(rule);

		rule = new SetAddAuditInfoRule();
		processer.addBeforeRule(rule);
		
		rule = new MaterialVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new UpdateSerialNORule();
		processer.addBeforeRule(rule);
		
		rule = new FillBillTypeRuleForIns();
		processer.addBeforeRule(rule);
	}
}

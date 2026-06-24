package nc.bs.riasm.gmplog.ace.bp;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;

/**
 * 标准单据新增BP
 */
public class AceGmpLogInsertBP {

	public AggGmpLogConfigHvo[] insert(AggGmpLogConfigHvo[] bills) {

		InsertBPTemplate<AggGmpLogConfigHvo> bp = new InsertBPTemplate<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggGmpLogConfigHvo> processor) {
		// TODO 新增后规则
		IRule<AggGmpLogConfigHvo> rule = null;
//		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
//		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("GMP1");
//		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
//				.setCodeItem("billno");
//		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
//				.setGroupItem("pk_group");
//		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
//		processor.addAfterRule(rule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggGmpLogConfigHvo> processer) {
		// TODO 新增前规则
		IRule<AggGmpLogConfigHvo> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("GMP1");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);
	}
}

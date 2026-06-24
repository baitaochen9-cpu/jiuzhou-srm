package nc.bs.so.salepacklist.ace.bp;

import nc.bs.so.salepacklist.ace.bp.rule.CheckValityRule;
import nc.bs.so.salepacklist.ace.bp.rule.FillBillTailInfoRuleForIns;
import nc.bs.so.salepacklist.ace.bp.rule.FillNewDefaultRule;
import nc.bs.so.salepacklist.ace.bp.rule.MaterialVerionBeforeRule;
import nc.bs.so.salepacklist.ace.bp.rule.WriteBackCostBillRule;
import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pubapp.util.SetAddAuditInfoRule;
import nc.vo.so.salepacklist.AggSalePackListHVO;

/**
 * 标准单据新增BP
 */
public class AceSalepacklistInsertBP {

	public AggSalePackListHVO[] insert(AggSalePackListHVO[] bills) {

		InsertBPTemplate<AggSalePackListHVO> bp = new InsertBPTemplate<AggSalePackListHVO>(
				SalepacklistPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggSalePackListHVO> processor) {
		// TODO 新增后规则
		IRule<AggSalePackListHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("4345");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processor.addAfterRule(rule);
		
		WriteBackCostBillRule wrule = new WriteBackCostBillRule();
		wrule.setFlag(true);
		processor.addAfterRule(wrule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggSalePackListHVO> processer) {
		// TODO 新增前规则
		IRule<AggSalePackListHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("4345");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);

		rule = new CheckValityRule();
		processer.addBeforeRule(rule);

		rule = new FillNewDefaultRule();
		processer.addBeforeRule(rule);

		rule = new FillBillTailInfoRuleForIns();
		processer.addBeforeRule(rule);

		rule = new SetAddAuditInfoRule();
		processer.addBeforeRule(rule);
		
		rule = new MaterialVerionBeforeRule();
		processer.addBeforeRule(rule);
	}

}

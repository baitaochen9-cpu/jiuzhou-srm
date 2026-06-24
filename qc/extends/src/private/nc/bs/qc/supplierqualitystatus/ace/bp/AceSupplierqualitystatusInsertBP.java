package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusFieldNotNullCheck;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusInsertHistoyRule;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusSupplyVerionBeforeRule;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusUniqueCheck;
import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;

/**
 * 标准单据新增BP
 */
public class AceSupplierqualitystatusInsertBP {

	public AggSupplierqualityHVO[] insert(AggSupplierqualityHVO[] bills) {

		InsertBPTemplate<AggSupplierqualityHVO> bp = new InsertBPTemplate<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggSupplierqualityHVO> processor) {
		// TODO 新增后规则
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("C055");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processor.addAfterRule(rule);
		rule = new AceSupplierqualitystatusInsertHistoyRule();
		processor.addAfterRule(rule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggSupplierqualityHVO> processer) {
		// TODO 新增前规则
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("C055");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualitystatusFieldNotNullCheck();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualitystatusSupplyVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualitystatusUniqueCheck();
		processer.addBeforeRule(rule);
	}
}

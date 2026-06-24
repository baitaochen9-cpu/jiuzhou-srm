package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyFieldNotNullCheck;
import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyVerionBeforeRule;
import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.InsertBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 标准单据新增BP
 */
public class AceSupplierqualityapplyInsertBP {

	public AggSupplierQualityApplyVO[] insert(AggSupplierQualityApplyVO[] bills) {

		InsertBPTemplate<AggSupplierQualityApplyVO> bp = new InsertBPTemplate<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.INSERT);
		this.addBeforeRule(bp.getAroundProcesser());
		this.addAfterRule(bp.getAroundProcesser());
		return bp.insert(bills);

	}

	/**
	 * 新增后规则
	 * 
	 * @param processor
	 */
	private void addAfterRule(AroundProcesser<AggSupplierQualityApplyVO> processor) {
		// TODO 新增后规则
		IRule<AggSupplierQualityApplyVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("C060");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processor.addAfterRule(rule);
	}

	/**
	 * 新增前规则
	 * 
	 * @param processor
	 */
	private void addBeforeRule(AroundProcesser<AggSupplierQualityApplyVO> processer) {
		// TODO 新增前规则
		IRule<AggSupplierQualityApplyVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillInsertDataRule();
		processer.addBeforeRule(rule);
		rule = new nc.bs.pubapp.pub.rule.CreateBillCodeRule();
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setCbilltype("C060");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.CreateBillCodeRule) rule).setOrgItem("pk_org");
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualityapplyVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualityapplyFieldNotNullCheck();
		processer.addBeforeRule(rule);
		
		
	}
}

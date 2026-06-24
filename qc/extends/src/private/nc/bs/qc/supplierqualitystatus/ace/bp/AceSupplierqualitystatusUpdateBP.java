package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusFieldNotNullCheck;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusInsertHistoyRule;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusSupplyVerionBeforeRule;
import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusUniqueCheck;
import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;

/**
 * 修改保存的BP
 * 
 */
public class AceSupplierqualitystatusUpdateBP {

	public AggSupplierqualityHVO[] update(AggSupplierqualityHVO[] bills,
			AggSupplierqualityHVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggSupplierqualityHVO> bp = new UpdateBPTemplate<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggSupplierqualityHVO> processer) {
		// TODO 后规则
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("C055");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);
		
		rule = new AceSupplierqualitystatusInsertHistoyRule();
		processer.addAfterRule(rule);
	}

	private void addBeforeRule(CompareAroundProcesser<AggSupplierqualityHVO> processer) {
		// TODO 前规则
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		
		nc.impl.pubapp.pattern.rule.ICompareRule<AggSupplierqualityHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("C055");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
		
		rule = new AceSupplierqualitystatusFieldNotNullCheck();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualitystatusSupplyVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualitystatusUniqueCheck();
		processer.addBeforeRule(rule);

	}

}

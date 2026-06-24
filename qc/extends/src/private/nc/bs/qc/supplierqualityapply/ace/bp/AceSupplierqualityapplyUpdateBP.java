package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyFieldNotNullCheck;
import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyVerionBeforeRule;
import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 修改保存的BP
 * 
 */
public class AceSupplierqualityapplyUpdateBP {

	public AggSupplierQualityApplyVO[] update(AggSupplierQualityApplyVO[] bills,
			AggSupplierQualityApplyVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggSupplierQualityApplyVO> bp = new UpdateBPTemplate<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggSupplierQualityApplyVO> processer) {
		// TODO 后规则
		IRule<AggSupplierQualityApplyVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("C060");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);

	}

	private void addBeforeRule(CompareAroundProcesser<AggSupplierQualityApplyVO> processer) {
		// TODO 前规则
		IRule<AggSupplierQualityApplyVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggSupplierQualityApplyVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("C060");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
		
		rule = new AceSupplierqualityapplyVerionBeforeRule();
		processer.addBeforeRule(rule);
		
		rule = new AceSupplierqualityapplyFieldNotNullCheck();
		processer.addBeforeRule(rule);
	}

}

package nc.bs.so.salepacklist.ace.bp;

import nc.bs.so.salepacklist.ace.bp.rule.MaterialVerionBeforeRule;
import nc.bs.so.salepacklist.ace.bp.rule.WriteBackCostBillRule;
import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.UpdateBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.so.salepacklist.AggSalePackListHVO;

/**
 * 修改保存的BP
 * 
 */
public class AceSalepacklistUpdateBP {

	public AggSalePackListHVO[] update(AggSalePackListHVO[] bills,
			AggSalePackListHVO[] originBills) {
		// 调用修改模板
		UpdateBPTemplate<AggSalePackListHVO> bp = new UpdateBPTemplate<AggSalePackListHVO>(
				SalepacklistPluginPoint.UPDATE);
		// 执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 执行后规则
		this.addAfterRule(bp.getAroundProcesser());
		return bp.update(bills, originBills);
	}

	private void addAfterRule(CompareAroundProcesser<AggSalePackListHVO> processer) {
		// TODO 后规则
		IRule<AggSalePackListHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillCodeCheckRule();
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setCbilltype("4345");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.BillCodeCheckRule) rule).setOrgItem("pk_org");
		processer.addAfterRule(rule);
		WriteBackCostBillRule wrule = new WriteBackCostBillRule();
		wrule.setFlag(true);
		processer.addAfterRule(wrule);
	}

	private void addBeforeRule(CompareAroundProcesser<AggSalePackListHVO> processer) {
		// TODO 前规则
		IRule<AggSalePackListHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.FillUpdateDataRule();
		processer.addBeforeRule(rule);
		nc.impl.pubapp.pattern.rule.ICompareRule<AggSalePackListHVO> ruleCom = new nc.bs.pubapp.pub.rule.UpdateBillCodeRule();
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCbilltype("4345");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setCodeItem("billno");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setGroupItem("pk_group");
		((nc.bs.pubapp.pub.rule.UpdateBillCodeRule) ruleCom)
				.setOrgItem("pk_org");
		processer.addBeforeRule(ruleCom);
		
		rule = new MaterialVerionBeforeRule();
		processer.addBeforeRule(rule);
	}

}

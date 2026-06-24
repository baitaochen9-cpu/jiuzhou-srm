package nc.bs.so.salepacklist.ace.bp;

import nc.bs.so.salepacklist.ace.bp.rule.WriteBackCostBillRule;
import nc.bs.so.salepacklist.plugin.bpplugin.SalepacklistPluginPoint;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;

/**
 * 标准单据删除BP
 */
public class AceSalepacklistDeleteBP {

	public void delete(AggSalePackListHVO[] bills) {

		DeleteBPTemplate<AggSalePackListHVO> bp = new DeleteBPTemplate<AggSalePackListHVO>(
				SalepacklistPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSalePackListHVO> processer) {
		// TODO 前规则
		IRule<AggSalePackListHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSalePackListHVO> processer) {
		// TODO 后规则
		WriteBackCostBillRule wrule = new WriteBackCostBillRule();
		wrule.setFlag(false);
		processer.addAfterRule(wrule);
	}
}

package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.bs.qc.supplierqualityapply.plugin.bpplugin.SupplierqualityapplyPluginPoint;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceSupplierqualityapplyDeleteBP {

	public void delete(AggSupplierQualityApplyVO[] bills) {

		DeleteBPTemplate<AggSupplierQualityApplyVO> bp = new DeleteBPTemplate<AggSupplierQualityApplyVO>(
				SupplierqualityapplyPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSupplierQualityApplyVO> processer) {
		// TODO 前规则
		IRule<AggSupplierQualityApplyVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSupplierQualityApplyVO> processer) {
		// TODO 后规则

	}
}

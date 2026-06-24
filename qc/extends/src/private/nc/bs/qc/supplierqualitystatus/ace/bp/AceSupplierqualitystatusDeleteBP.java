package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.bs.qc.supplierqualitystatus.ace.bp.rule.AceSupplierqualitystatusInsertHistoyRule;
import nc.bs.qc.supplierqualitystatus.plugin.bpplugin.SupplierqualitystatusPluginPoint;
import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;


/**
 * 标准单据删除BP
 */
public class AceSupplierqualitystatusDeleteBP {

	public void delete(AggSupplierqualityHVO[] bills) {

		DeleteBPTemplate<AggSupplierqualityHVO> bp = new DeleteBPTemplate<AggSupplierqualityHVO>(
				SupplierqualitystatusPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggSupplierqualityHVO> processer) {
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggSupplierqualityHVO> processer) {
		IRule<AggSupplierqualityHVO> rule = null;
		rule = new AceSupplierqualitystatusInsertHistoyRule();
		processer.addAfterRule(rule);
	}
}

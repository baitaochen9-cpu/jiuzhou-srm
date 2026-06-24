package nc.bs.jzqc.labelprintapply.ace.bp;

import nc.bs.jzqc.labelprintapply.plugin.bpplugin.LabelprintapplyPluginPoint;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceLabelprintapplyDeleteBP {

	public void delete(AggLabelprintapplyHVO[] bills) {

		DeleteBPTemplate<AggLabelprintapplyHVO> bp = new DeleteBPTemplate<AggLabelprintapplyHVO>(
				LabelprintapplyPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggLabelprintapplyHVO> processer) {
		// TODO 前规则
		IRule<AggLabelprintapplyHVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggLabelprintapplyHVO> processer) {
		// TODO 后规则

	}
}

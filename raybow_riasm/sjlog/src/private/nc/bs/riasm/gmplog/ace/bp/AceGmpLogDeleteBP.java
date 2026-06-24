package nc.bs.riasm.gmplog.ace.bp;

import nc.bs.riasm.gmplog.plugin.bpplugin.GmpLogPluginPoint;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceGmpLogDeleteBP {

	public void delete(AggGmpLogConfigHvo[] bills) {

		DeleteBPTemplate<AggGmpLogConfigHvo> bp = new DeleteBPTemplate<AggGmpLogConfigHvo>(
				GmpLogPluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggGmpLogConfigHvo> processer) {
		// TODO 前规则
		IRule<AggGmpLogConfigHvo> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggGmpLogConfigHvo> processer) {
		// TODO 后规则

	}
}

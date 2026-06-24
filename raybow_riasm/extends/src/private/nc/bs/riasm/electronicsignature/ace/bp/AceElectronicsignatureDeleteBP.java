package nc.bs.riasm.electronicsignature.ace.bp;

import nc.bs.riasm.electronicsignature.plugin.bpplugin.ElectronicsignaturePluginPoint;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;

import nc.impl.pubapp.pattern.data.bill.template.DeleteBPTemplate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.pubapp.pattern.rule.IRule;


/**
 * 标准单据删除BP
 */
public class AceElectronicsignatureDeleteBP {

	public void delete(AggElectronicSignatureVO[] bills) {

		DeleteBPTemplate<AggElectronicSignatureVO> bp = new DeleteBPTemplate<AggElectronicSignatureVO>(
				ElectronicsignaturePluginPoint.DELETE);
		// 增加执行前规则
		this.addBeforeRule(bp.getAroundProcesser());
		// 增加执行后业务规则
		this.addAfterRule(bp.getAroundProcesser());
		bp.delete(bills);
	}

	private void addBeforeRule(AroundProcesser<AggElectronicSignatureVO> processer) {
		// TODO 前规则
		IRule<AggElectronicSignatureVO> rule = null;
		rule = new nc.bs.pubapp.pub.rule.BillDeleteStatusCheckRule();
		processer.addBeforeRule(rule);
	}

	/**
	 * 删除后业务规则
	 * 
	 * @param processer
	 */
	private void addAfterRule(AroundProcesser<AggElectronicSignatureVO> processer) {
		// TODO 后规则

	}
}

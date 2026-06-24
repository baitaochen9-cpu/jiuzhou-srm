/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.impl.ct.purdaily.action;
/*    */
/*    */import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.ct.purdaily.action.rule.sendapprove.ApproveFlowCheckRule;
import nc.impl.ct.purdaily.action.rule.sendapprove.PurdailyStateChgRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.rule.CtSendOARule;
import nc.vo.ct.rule.PurdailyEventRule;
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */public class PurdailySendApproveAction
/*    */{
	/*    */public AggCtPuVO[] sendapprove(AggCtPuVO[] vos,
			AbstractCompiler2 script)
	/*    */{
		/* 39 */PfParameterUtil tool = new PfParameterUtil(
				script.getPfParameterVO(), vos);
		/*    */
		/* 43 */AggCtPuVO[] clientFullVos = (AggCtPuVO[]) tool
				.getClientFullInfoBill();
		/* 44 */AggCtPuVO[] orgVos = (AggCtPuVO[]) tool.getOrginBills();
		/* 45 */AroundProcesser processer = new AroundProcesser(null);
		/* 46 */addRule(processer);
		/* 47 */processer.before(clientFullVos);
		/* 48 */AggCtPuVO[] updatedVos = (AggCtPuVO[]) new BillUpdate()
				.update(clientFullVos, orgVos);
		/*    */
		/* 50 */processer.after(updatedVos);
		/* 51 */return updatedVos;
		/*    */}
	/*    */
	/*    */private void addRule(AroundProcesser<AggCtPuVO> processer)
	/*    */{
		/* 58 */processer.addBeforeFinalRule(new ApproveFlowCheckRule());
		/*    */
		/* 61 */processer.addBeforeFinalRule(new PurdailyStateChgRule());
		/*    */
		/* 64 */processer.addBeforeRule(new PurdailyEventRule("40202041"));
		/*    */
		/* 68 */processer.addAfterRule(new PurdailyEventRule("40202042"));
		
		processer.addAfterRule(new CtSendOARule(""));
		/*    */}
	/*    */
}
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.impl.ct.saledaily.action;
/*    */
/*    */import nc.bs.pub.compiler.AbstractCompiler2;
/*    */
import nc.bs.scmpub.pf.PfParameterUtil;
/*    */
import nc.impl.ct.saledaily.action.rule.sendapprove.ApproveFlowCheckRule;
/*    */
import nc.impl.ct.saledaily.action.rule.sendapprove.SaledailyStateChgRule;
/*    */
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
/*    */
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.ct.rule.PushOaRule;
/*    */
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
/*    */
/*    */public class SaledailySendApproveAction
/*    */{
	/*    */public AggCtSaleVO[] sendapprove(AggCtSaleVO[] vos,
			AbstractCompiler2 script)
	/*    */{
		/* 28 */PfParameterUtil tool = new PfParameterUtil(
				script.getPfParameterVO(), vos);
		/*    */
		/* 33 */AggCtSaleVO[] cliVos = (AggCtSaleVO[]) tool
				.getClientFullInfoBill();
		/* 34 */AggCtSaleVO[] orgVos = (AggCtSaleVO[]) tool.getOrginBills();
		/* 35 */AroundProcesser processer = new AroundProcesser(null);
		/*    */
		/* 37 */addRule(processer);
		/* 38 */processer.before(cliVos);
		/* 39 */AggCtSaleVO[] updatedVos = (AggCtSaleVO[]) new BillUpdate()
				.update(cliVos, orgVos);
		/*    */new PushOaRule().process(updatedVos);
		/* 41 */return updatedVos;
		/*    */}
	/*    */
	/*    */private void addRule(AroundProcesser<AggCtSaleVO> processer)
	/*    */{
		/* 51 */processer.addBeforeFinalRule(new ApproveFlowCheckRule());
		/*    */
		/* 53 */processer.addBeforeFinalRule(new SaledailyStateChgRule());
		/*    */}
	/*    */
}
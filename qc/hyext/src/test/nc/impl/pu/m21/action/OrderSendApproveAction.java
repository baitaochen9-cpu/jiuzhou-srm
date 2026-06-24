/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.impl.pu.m21.action;
/*    */
/*    */import nc.bs.pu.m21.maintain.rule.save.SendOAAfterRule;
import nc.bs.pu.m21.plugin.OrderPluginPoint;
/*    */
import nc.bs.pub.compiler.AbstractCompiler2;
/*    */
import nc.bs.scmpub.pf.PfParameterUtil;
/*    */
import nc.impl.pu.m21.action.rule.approve.SendAppoveVOValidateRule;
/*    */
import nc.impl.pu.m21.action.rule.approve.SendApproveAfterRule;
/*    */
import nc.impl.pu.m21.action.rule.approve.SendApproveBeforeEventRule;
/*    */
import nc.impl.pu.m21.action.rule.approve.SendApproveFlowCheckRule;
/*    */
import nc.impl.pu.m21.action.rule.approve.SendApproveStatusChangeRule;
/*    */
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
/*    */
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
/*    */
import nc.vo.pu.m21.entity.OrderVO;
/*    */
/*    */public class OrderSendApproveAction
/*    */{
	/*    */public OrderVO[] sendApprove(OrderVO[] vos,
			AbstractCompiler2 script)
	/*    */{
		/* 41 */PfParameterUtil util = new PfParameterUtil(
				script.getPfParameterVO(), vos);
		/*    */
		/* 43 */OrderVO[] originBills = (OrderVO[]) util.getClientOrignBills();
		/* 44 */OrderVO[] clientBills = (OrderVO[]) util
				.getClientFullInfoBill();
		/*    */
		/* 46 */AroundProcesser processor = new AroundProcesser(
				OrderPluginPoint.SEND_APPROVE);
		/*    */
		/* 49 */addRule(processor);
		/*    */
		/* 51 */processor.before(clientBills);
		/*    */
		/* 53 */OrderVO[] returnVos = (OrderVO[]) new BillUpdate().update(
				clientBills, originBills);
		/*    */
		/* 56 */processor.after(returnVos);
		/*    */
		/* 58 */return returnVos;
		/*    */}
	/*    */
	/*    */private void addRule(AroundProcesser<OrderVO> processer)
	/*    */{
		/* 63 */processer.addBeforeFinalRule(new SendApproveFlowCheckRule());
		/*    */
		/* 65 */processer.addBeforeFinalRule(new SendAppoveVOValidateRule());
		/*    */
		/* 67 */processer
				.addBeforeFinalRule(new SendApproveStatusChangeRule());
		/*    */
		/* 69 */processer.addBeforeRule(new SendApproveBeforeEventRule());
			    processer.addAfterRule(new SendOAAfterRule());
		/* 70 */processer.addAfterRule(new SendApproveAfterRule());
		
			
		
		/*    */}
	/*    */
}
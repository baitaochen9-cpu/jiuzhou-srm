/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */package nc.impl.so.m30.action.main;
/*    */
/*    */import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.so.m30.action.main.approve.PushOaRule;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.pub.rule.SOPfStatusChgRule;
/*    */
/*    */
/*    */
/*    */public class CommitSaleOrderAction
/*    */{
	/*    */public SaleOrderVO[] sendApprove(SaleOrderVO[] clientBills,
			SaleOrderVO[] originBills)
	/*    */{
		/* 17 */for (SaleOrderVO newvo : clientBills) {
			/* 18 */SOPfStatusChgRule statuschgrule = new SOPfStatusChgRule();
			/* 19 */statuschgrule.changePfToBillStatus(newvo);
			/*    */}
		/*    */
		/* 23 */BillUpdate update = new BillUpdate();
		/* 24 */SaleOrderVO[] returnVos = (SaleOrderVO[]) update.update(
				clientBills, originBills);
		PushOaRule rule = new PushOaRule();
        rule.process(returnVos);
		/* 25 */return returnVos;
		/*    */}
	/*    */
}
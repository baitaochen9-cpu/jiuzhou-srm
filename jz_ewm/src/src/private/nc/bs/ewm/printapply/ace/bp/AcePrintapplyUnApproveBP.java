package nc.bs.ewm.printapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AcePrintapplyUnApproveBP {

	public AggPrintapply[] unApprove(AggPrintapply[] clientBills,
			AggPrintapply[] originBills) {
		for (AggPrintapply clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggPrintapply> update = new BillUpdate<AggPrintapply>();
		AggPrintapply[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

package nc.bs.ewm.printapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.ewm.printapply.AggPrintapply;

/**
 * 标准单据审核的BP
 */
public class AcePrintapplyApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggPrintapply[] approve(AggPrintapply[] clientBills,
			AggPrintapply[] originBills) {
		for (AggPrintapply clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggPrintapply> update = new BillUpdate<AggPrintapply>();
		AggPrintapply[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

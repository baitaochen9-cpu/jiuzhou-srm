package nc.bs.ewm.printapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AcePrintapplyUnSendApproveBP {

	public AggPrintapply[] unSend(AggPrintapply[] clientBills,
			AggPrintapply[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggPrintapply> update = new BillUpdate<AggPrintapply>();
		AggPrintapply[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggPrintapply[] clientBills) {
		for (AggPrintapply clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

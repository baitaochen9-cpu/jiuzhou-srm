package nc.bs.jzqc.labelprint.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceLabelprintUnApproveBP {

	public AggLabelPrintHVO[] unApprove(AggLabelPrintHVO[] clientBills,
			AggLabelPrintHVO[] originBills) {
		for (AggLabelPrintHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggLabelPrintHVO> update = new BillUpdate<AggLabelPrintHVO>();
		AggLabelPrintHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

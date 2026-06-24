package nc.bs.jzqc.labelprint.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;

/**
 * 标准单据审核的BP
 */
public class AceLabelprintApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggLabelPrintHVO[] approve(AggLabelPrintHVO[] clientBills,
			AggLabelPrintHVO[] originBills) {
		for (AggLabelPrintHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggLabelPrintHVO> update = new BillUpdate<AggLabelPrintHVO>();
		AggLabelPrintHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

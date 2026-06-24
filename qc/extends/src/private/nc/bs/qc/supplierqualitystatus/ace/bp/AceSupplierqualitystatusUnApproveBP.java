package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceSupplierqualitystatusUnApproveBP {

	public AggSupplierqualityHVO[] unApprove(AggSupplierqualityHVO[] clientBills,
			AggSupplierqualityHVO[] originBills) {
		for (AggSupplierqualityHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSupplierqualityHVO> update = new BillUpdate<AggSupplierqualityHVO>();
		AggSupplierqualityHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

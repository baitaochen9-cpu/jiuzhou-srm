package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;

/**
 * 标准单据审核的BP
 */
public class AceSupplierqualitystatusApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggSupplierqualityHVO[] approve(AggSupplierqualityHVO[] clientBills,
			AggSupplierqualityHVO[] originBills) {
		for (AggSupplierqualityHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSupplierqualityHVO> update = new BillUpdate<AggSupplierqualityHVO>();
		AggSupplierqualityHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

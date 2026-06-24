package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyUnApproveCheckBP;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 标准单据弃审的BP
 */
public class AceSupplierqualityapplyUnApproveBP {

	public AggSupplierQualityApplyVO[] unApprove(AggSupplierQualityApplyVO[] clientBills,
			AggSupplierQualityApplyVO[] originBills) {
		for (AggSupplierQualityApplyVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSupplierqualityapplyUnApproveCheckBP checkbp = new AceSupplierqualityapplyUnApproveCheckBP();
		checkbp.checkAggSupplierQualityApplyVO(clientBills);
		
		BillUpdate<AggSupplierQualityApplyVO> update = new BillUpdate<AggSupplierQualityApplyVO>();
		AggSupplierQualityApplyVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

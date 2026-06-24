package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.bs.qc.supplierqualityapply.ace.bp.rule.UpdateAceSupplierqualitystatusRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 标准单据审核的BP
 */
public class AceSupplierqualityapplyApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggSupplierQualityApplyVO[] approve(AggSupplierQualityApplyVO[] clientBills,
			AggSupplierQualityApplyVO[] originBills) {
		for (AggSupplierQualityApplyVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSupplierQualityApplyVO> update = new BillUpdate<AggSupplierQualityApplyVO>();
		AggSupplierQualityApplyVO[] returnVos = update.update(clientBills, originBills);
		
		UpdateAceSupplierqualitystatusRule rule = new UpdateAceSupplierqualitystatusRule();
		rule.process(returnVos);
		return returnVos;
	}

}

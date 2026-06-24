package nc.bs.jzqc.labelprintapply.ace.bp;

import nc.bs.jzqc.labelprintapply.ace.bp.rule.UpdateLablePrintStatusFalseRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceLabelprintapplyUnApproveBP {

	public AggLabelprintapplyHVO[] unApprove(
			AggLabelprintapplyHVO[] clientBills,
			AggLabelprintapplyHVO[] originBills) {
		for (AggLabelprintapplyHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		UpdateLablePrintStatusFalseRule rule = new UpdateLablePrintStatusFalseRule();
		rule.process(clientBills);
		
		BillUpdate<AggLabelprintapplyHVO> update = new BillUpdate<AggLabelprintapplyHVO>();
		AggLabelprintapplyHVO[] returnVos = update.update(clientBills,
				originBills);
		return returnVos;
	}
}

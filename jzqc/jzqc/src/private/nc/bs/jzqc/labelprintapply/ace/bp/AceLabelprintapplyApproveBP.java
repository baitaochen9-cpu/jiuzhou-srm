package nc.bs.jzqc.labelprintapply.ace.bp;

import nc.bs.jzqc.labelprintapply.ace.bp.rule.UpdateLablePrintStatusTrueRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.VOStatus;
/**
 * 标准单据审核的BP
 */
public class AceLabelprintapplyApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggLabelprintapplyHVO[] approve(AggLabelprintapplyHVO[] clientBills,
			AggLabelprintapplyHVO[] originBills) {
		for (AggLabelprintapplyHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		
		UpdateLablePrintStatusTrueRule rule = new UpdateLablePrintStatusTrueRule();
		rule.process(clientBills);
		
		BillUpdate<AggLabelprintapplyHVO> update = new BillUpdate<AggLabelprintapplyHVO>();
		AggLabelprintapplyHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

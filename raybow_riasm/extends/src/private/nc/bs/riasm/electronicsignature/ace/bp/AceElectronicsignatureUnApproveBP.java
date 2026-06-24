package nc.bs.riasm.electronicsignature.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceElectronicsignatureUnApproveBP {

	public AggElectronicSignatureVO[] unApprove(AggElectronicSignatureVO[] clientBills,
			AggElectronicSignatureVO[] originBills) {
		for (AggElectronicSignatureVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggElectronicSignatureVO> update = new BillUpdate<AggElectronicSignatureVO>();
		AggElectronicSignatureVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

package nc.bs.riasm.electronicsignature.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;

/**
 * 标准单据审核的BP
 */
public class AceElectronicsignatureApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggElectronicSignatureVO[] approve(AggElectronicSignatureVO[] clientBills,
			AggElectronicSignatureVO[] originBills) {
		for (AggElectronicSignatureVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggElectronicSignatureVO> update = new BillUpdate<AggElectronicSignatureVO>();
		AggElectronicSignatureVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

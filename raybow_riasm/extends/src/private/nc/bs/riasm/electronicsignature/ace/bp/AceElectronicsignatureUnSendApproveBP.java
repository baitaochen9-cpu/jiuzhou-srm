package nc.bs.riasm.electronicsignature.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceElectronicsignatureUnSendApproveBP {

	public AggElectronicSignatureVO[] unSend(AggElectronicSignatureVO[] clientBills,
			AggElectronicSignatureVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggElectronicSignatureVO> update = new BillUpdate<AggElectronicSignatureVO>();
		AggElectronicSignatureVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggElectronicSignatureVO[] clientBills) {
		for (AggElectronicSignatureVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

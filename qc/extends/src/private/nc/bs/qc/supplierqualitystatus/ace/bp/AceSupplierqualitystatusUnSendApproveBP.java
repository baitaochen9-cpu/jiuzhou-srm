package nc.bs.qc.supplierqualitystatus.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceSupplierqualitystatusUnSendApproveBP {

	public AggSupplierqualityHVO[] unSend(AggSupplierqualityHVO[] clientBills,
			AggSupplierqualityHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggSupplierqualityHVO> update = new BillUpdate<AggSupplierqualityHVO>();
		AggSupplierqualityHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggSupplierqualityHVO[] clientBills) {
		for (AggSupplierqualityHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

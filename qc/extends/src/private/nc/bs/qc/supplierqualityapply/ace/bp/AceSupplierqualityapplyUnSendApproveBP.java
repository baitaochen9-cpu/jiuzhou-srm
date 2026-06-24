package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceSupplierqualityapplyUnSendApproveBP {

	public AggSupplierQualityApplyVO[] unSend(AggSupplierQualityApplyVO[] clientBills,
			AggSupplierQualityApplyVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggSupplierQualityApplyVO> update = new BillUpdate<AggSupplierQualityApplyVO>();
		AggSupplierQualityApplyVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggSupplierQualityApplyVO[] clientBills) {
		for (AggSupplierQualityApplyVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

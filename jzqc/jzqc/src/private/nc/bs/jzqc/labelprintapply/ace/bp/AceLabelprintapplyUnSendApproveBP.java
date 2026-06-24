package nc.bs.jzqc.labelprintapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceLabelprintapplyUnSendApproveBP {

	public AggLabelprintapplyHVO[] unSend(AggLabelprintapplyHVO[] clientBills,
			AggLabelprintapplyHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggLabelprintapplyHVO> update = new BillUpdate<AggLabelprintapplyHVO>();
		AggLabelprintapplyHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggLabelprintapplyHVO[] clientBills) {
		for (AggLabelprintapplyHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

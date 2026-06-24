package nc.bs.so.salepacklist.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceSalepacklistUnSendApproveBP {

	public AggSalePackListHVO[] unSend(AggSalePackListHVO[] clientBills,
			AggSalePackListHVO[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggSalePackListHVO> update = new BillUpdate<AggSalePackListHVO>();
		AggSalePackListHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggSalePackListHVO[] clientBills) {
		for (AggSalePackListHVO clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

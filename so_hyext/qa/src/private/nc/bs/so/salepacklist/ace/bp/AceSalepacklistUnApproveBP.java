package nc.bs.so.salepacklist.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceSalepacklistUnApproveBP {

	public AggSalePackListHVO[] unApprove(AggSalePackListHVO[] clientBills,
			AggSalePackListHVO[] originBills) {
		for (AggSalePackListHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSalePackListHVO> update = new BillUpdate<AggSalePackListHVO>();
		AggSalePackListHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

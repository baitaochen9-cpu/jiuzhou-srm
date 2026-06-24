package nc.bs.so.salepacklist.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.so.salepacklist.AggSalePackListHVO;

/**
 * 标准单据审核的BP
 */
public class AceSalepacklistApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggSalePackListHVO[] approve(AggSalePackListHVO[] clientBills,
			AggSalePackListHVO[] originBills) {
		for (AggSalePackListHVO clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggSalePackListHVO> update = new BillUpdate<AggSalePackListHVO>();
		AggSalePackListHVO[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

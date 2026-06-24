package nc.bs.riasm.gmplog.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据收回的BP
 */
public class AceGmpLogUnSendApproveBP {

	public AggGmpLogConfigHvo[] unSend(AggGmpLogConfigHvo[] clientBills,
			AggGmpLogConfigHvo[] originBills) {
		// 把VO持久化到数据库中
		this.setHeadVOStatus(clientBills);
		BillUpdate<AggGmpLogConfigHvo> update = new BillUpdate<AggGmpLogConfigHvo>();
		AggGmpLogConfigHvo[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

	private void setHeadVOStatus(AggGmpLogConfigHvo[] clientBills) {
		for (AggGmpLogConfigHvo clientBill : clientBills) {
			clientBill.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.FREE.value());
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
	}
}

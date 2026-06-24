package nc.bs.riasm.gmplog.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.pub.VOStatus;

/**
 * 标准单据弃审的BP
 */
public class AceGmpLogUnApproveBP {

	public AggGmpLogConfigHvo[] unApprove(AggGmpLogConfigHvo[] clientBills,
			AggGmpLogConfigHvo[] originBills) {
		for (AggGmpLogConfigHvo clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggGmpLogConfigHvo> update = new BillUpdate<AggGmpLogConfigHvo>();
		AggGmpLogConfigHvo[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}
}

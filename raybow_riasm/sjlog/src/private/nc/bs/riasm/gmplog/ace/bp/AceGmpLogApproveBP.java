package nc.bs.riasm.gmplog.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.pub.VOStatus;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;

/**
 * 标准单据审核的BP
 */
public class AceGmpLogApproveBP {

	/**
	 * 审核动作
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public AggGmpLogConfigHvo[] approve(AggGmpLogConfigHvo[] clientBills,
			AggGmpLogConfigHvo[] originBills) {
		for (AggGmpLogConfigHvo clientBill : clientBills) {
			clientBill.getParentVO().setStatus(VOStatus.UPDATED);
		}
		BillUpdate<AggGmpLogConfigHvo> update = new BillUpdate<AggGmpLogConfigHvo>();
		AggGmpLogConfigHvo[] returnVos = update.update(clientBills, originBills);
		return returnVos;
	}

}

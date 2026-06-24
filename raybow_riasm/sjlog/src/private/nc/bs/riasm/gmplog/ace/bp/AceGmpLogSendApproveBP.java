package nc.bs.riasm.gmplog.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceGmpLogSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggGmpLogConfigHvo[] sendApprove(AggGmpLogConfigHvo[] clientBills,
			AggGmpLogConfigHvo[] originBills) {
		for (AggGmpLogConfigHvo clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("${vmObject.billstatus}",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggGmpLogConfigHvo[] returnVos = new BillUpdate<AggGmpLogConfigHvo>().update(
				clientBills, originBills);
		return returnVos;
	}
}

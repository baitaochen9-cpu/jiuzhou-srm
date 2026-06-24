package nc.bs.so.salepacklist.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.so.salepacklist.AggSalePackListHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceSalepacklistSendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggSalePackListHVO[] sendApprove(AggSalePackListHVO[] clientBills,
			AggSalePackListHVO[] originBills) {
		for (AggSalePackListHVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setApprovestatus(BillStatusEnum.COMMIT.toIntValue());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggSalePackListHVO[] returnVos = new BillUpdate<AggSalePackListHVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}

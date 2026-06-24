package nc.bs.qc.supplierqualityapply.ace.bp;

import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.BillStatusEnum;

/**
 * 标准单据送审的BP
 */
public class AceSupplierqualityapplySendApproveBP {
	/**
	 * 送审动作
	 * 
	 * @param vos
	 *            单据VO数组
	 * @param script
	 *            单据动作脚本对象
	 * @return 送审后的单据VO数组
	 */

	public AggSupplierQualityApplyVO[] sendApprove(AggSupplierQualityApplyVO[] clientBills,
			AggSupplierQualityApplyVO[] originBills) {
		for (AggSupplierQualityApplyVO clientFullVO : clientBills) {
			clientFullVO.getParentVO().setAttributeValue("approvestatus",
					BillStatusEnum.COMMIT.value());
			clientFullVO.getParentVO().setStatus(VOStatus.UPDATED);
		}
		// 数据持久化
		AggSupplierQualityApplyVO[] returnVos = new BillUpdate<AggSupplierQualityApplyVO>().update(
				clientBills, originBills);
		return returnVos;
	}
}

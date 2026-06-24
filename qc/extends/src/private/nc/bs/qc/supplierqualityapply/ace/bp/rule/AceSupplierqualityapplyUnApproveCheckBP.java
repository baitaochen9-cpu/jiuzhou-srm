package nc.bs.qc.supplierqualityapply.ace.bp.rule;

import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

/**
 * 标准单据的BP
 */
public class AceSupplierqualityapplyUnApproveCheckBP {

	/**
	 * 弃审校验
	 * 
	 * @param vos
	 * @param script
	 * @return
	 */
	public void checkAggSupplierQualityApplyVO(
			AggSupplierQualityApplyVO[] clientBills) {

		ExceptionUtils.wrappBusinessException("已经审批通过，不能弃审！");
		// VOQuery<SupplierQualityApplyVO> query = new
		// VOQuery<SupplierQualityApplyVO>(
		// SupplierQualityApplyVO.class);
		// String condition = " and pk_org = '" + pk_org + "' and  transtype ='"
		// + transtype + "'";
		//
		// if (isMul(transtype)) {
		// String[] ids = billid[0].split(",");
		// int len = ids.length;
		// for (int i = 0; i < len; i++) {
		// if (i == 0) {
		// condition = condition + " and ( srcbillrowid like '%"
		// + ids[i] + "%'";
		// } else {
		// condition = condition + " or srcbillrowid like '%" + ids[i]
		// + "%'";
		// }
		// if (i == len - 1) {
		// condition = condition + "  )";
		// }
		// }
		// } else {
		//
		// String where = append("srcbillrowid", billid);
		// condition = condition + " and " + where;
		// }
		// SupplierQualityApplyVO[] hvos = query.query(condition, null);
		// if (hvos != null && hvos.length > 0) {
		// ExceptionUtils.asBusinessRuntimeException(new BusinessException(
		// "已经生产了对应的标签档案不能打印,标签档案号：" + hvos[0].getBillno() + "！"));
		// }
	}
}

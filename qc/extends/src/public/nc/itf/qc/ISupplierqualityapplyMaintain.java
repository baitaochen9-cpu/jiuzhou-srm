package nc.itf.qc;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.vo.pub.BusinessException;

public interface ISupplierqualityapplyMaintain {

	public void delete(AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] insert(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] update(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSupplierQualityApplyVO[] save(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] unsave(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] approve(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	public AggSupplierQualityApplyVO[] unapprove(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException;

	// 定时任务生成生产厂商物料质量状态申请单
	public AggSupplierQualityApplyVO[] createApply(String[] pk_orgs,
			int yearnum,int unit , String[] pk_materials, String after_qualitylevel)
			throws BusinessException;
}

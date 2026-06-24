package nc.impl.qc;

import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyCreateBP;
import nc.impl.pub.ace.AceSupplierqualityapplyPubServiceImpl;
import nc.itf.qc.ISupplierqualityapplyMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;

public class SupplierqualityapplyMaintainImpl extends
		AceSupplierqualityapplyPubServiceImpl implements
		ISupplierqualityapplyMaintain {

	@Override
	public void delete(AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] insert(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] update(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggSupplierQualityApplyVO[] save(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] unsave(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] approve(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierQualityApplyVO[] unapprove(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

	// 定时任务生成生产厂商物料质量状态申请单
	public AggSupplierQualityApplyVO[] createApply(String[] pk_orgs,
			int yearnum,int unit , String[] pk_materials, String after_qualitylevel)
			throws BusinessException {
		AceSupplierqualityapplyCreateBP bp = new AceSupplierqualityapplyCreateBP();
		return bp.createApply(pk_orgs, yearnum,unit , pk_materials, after_qualitylevel);
	}
}

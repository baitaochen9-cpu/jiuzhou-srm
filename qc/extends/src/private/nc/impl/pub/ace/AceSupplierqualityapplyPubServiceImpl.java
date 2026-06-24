package nc.impl.pub.ace;

import java.util.Map;

import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyInsertBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyUpdateBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyDeleteBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplySendApproveBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyUnSendApproveBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyApproveBP;
import nc.bs.qc.supplierqualityapply.ace.bp.AceSupplierqualityapplyUnApproveBP;
import nc.bs.qc.supplierqualityapply.ace.bp.rule.AceSupplierqualityapplyCreateBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusCheckBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.qc.supplierqualityapply.AggSupplierQualityApplyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceSupplierqualityapplyPubServiceImpl {
	// 新增
	public AggSupplierQualityApplyVO[] pubinsertBills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggSupplierQualityApplyVO> transferTool = new BillTransferTool<AggSupplierQualityApplyVO>(
					clientFullVOs);
			// 调用BP
			AceSupplierqualityapplyInsertBP action = new AceSupplierqualityapplyInsertBP();
			AggSupplierQualityApplyVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceSupplierqualityapplyDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggSupplierQualityApplyVO[] pubupdateBills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggSupplierQualityApplyVO> transferTool = new BillTransferTool<AggSupplierQualityApplyVO>(
					clientFullVOs);
			AceSupplierqualityapplyUpdateBP bp = new AceSupplierqualityapplyUpdateBP();
			AggSupplierQualityApplyVO[] retvos = bp.update(clientFullVOs,
					originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggSupplierQualityApplyVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggSupplierQualityApplyVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggSupplierQualityApplyVO> query = new BillLazyQuery<AggSupplierQualityApplyVO>(
					AggSupplierQualityApplyVO.class);
			bills = query.query(queryScheme, null);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}

	/**
	 * 由子类实现，查询之前对queryScheme进行加工，加入自己的逻辑
	 * 
	 * @param queryScheme
	 */
	protected void preQuery(IQueryScheme queryScheme) {
		// 查询之前对queryScheme进行加工，加入自己的逻辑
	}

	// 提交
	public AggSupplierQualityApplyVO[] pubsendapprovebills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		AceSupplierqualityapplySendApproveBP bp = new AceSupplierqualityapplySendApproveBP();
		AggSupplierQualityApplyVO[] retvos = bp.sendApprove(clientFullVOs,
				originBills);
		return retvos;
	}

	// 收回
	public AggSupplierQualityApplyVO[] pubunsendapprovebills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		AceSupplierqualityapplyUnSendApproveBP bp = new AceSupplierqualityapplyUnSendApproveBP();
		AggSupplierQualityApplyVO[] retvos = bp.unSend(clientFullVOs,
				originBills);
		return retvos;
	};

	// 审批
	public AggSupplierQualityApplyVO[] pubapprovebills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSupplierqualityapplyApproveBP bp = new AceSupplierqualityapplyApproveBP();
		AggSupplierQualityApplyVO[] retvos = bp.approve(clientFullVOs,
				originBills);
		return retvos;
	}

	// 弃审

	public AggSupplierQualityApplyVO[] pubunapprovebills(
			AggSupplierQualityApplyVO[] clientFullVOs,
			AggSupplierQualityApplyVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSupplierqualityapplyUnApproveBP bp = new AceSupplierqualityapplyUnApproveBP();
		AggSupplierQualityApplyVO[] retvos = bp.unApprove(clientFullVOs,
				originBills);
		return retvos;
	}
}
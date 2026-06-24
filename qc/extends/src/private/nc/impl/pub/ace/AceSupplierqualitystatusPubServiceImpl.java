package nc.impl.pub.ace;

import java.util.Map;
import java.util.Set;

import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusApproveBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusBatchSaveBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusCheckBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusDeleteBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusInsertBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusSendApproveBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusUnApproveBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusUnSendApproveBP;
import nc.bs.qc.supplierqualitystatus.ace.bp.AceSupplierqualitystatusUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceSupplierqualitystatusPubServiceImpl {
	// 新增
	public AggSupplierqualityHVO[] pubinsertBills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggSupplierqualityHVO> transferTool = new BillTransferTool<AggSupplierqualityHVO>(
					clientFullVOs);
			// 调用BP
			AceSupplierqualitystatusInsertBP action = new AceSupplierqualitystatusInsertBP();
			AggSupplierqualityHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceSupplierqualitystatusDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggSupplierqualityHVO[] pubupdateBills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggSupplierqualityHVO> transferTool = new BillTransferTool<AggSupplierqualityHVO>(
					clientFullVOs);
			AceSupplierqualitystatusUpdateBP bp = new AceSupplierqualitystatusUpdateBP();
			AggSupplierqualityHVO[] retvos = bp.update(clientFullVOs,
					originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggSupplierqualityHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggSupplierqualityHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggSupplierqualityHVO> query = new BillLazyQuery<AggSupplierqualityHVO>(
					AggSupplierqualityHVO.class);
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
	public AggSupplierqualityHVO[] pubsendapprovebills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		AceSupplierqualitystatusSendApproveBP bp = new AceSupplierqualitystatusSendApproveBP();
		AggSupplierqualityHVO[] retvos = bp.sendApprove(clientFullVOs,
				originBills);
		return retvos;
	}

	// 收回
	public AggSupplierqualityHVO[] pubunsendapprovebills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		AceSupplierqualitystatusUnSendApproveBP bp = new AceSupplierqualitystatusUnSendApproveBP();
		AggSupplierqualityHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggSupplierqualityHVO[] pubapprovebills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSupplierqualitystatusApproveBP bp = new AceSupplierqualitystatusApproveBP();
		AggSupplierqualityHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggSupplierqualityHVO[] pubunapprovebills(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSupplierqualitystatusUnApproveBP bp = new AceSupplierqualitystatusUnApproveBP();
		AggSupplierqualityHVO[] retvos = bp.unApprove(clientFullVOs,
				originBills);
		return retvos;
	}

	// 校驗供應商狀態
	public void checkSupplierStatus(Map<String, String> pk_material_vs,String pk_group, String pk_org,
			String pk_supplier, String pk_billtype,String supgradestatus) throws BusinessException {
		AceSupplierqualitystatusCheckBP bp = new AceSupplierqualitystatusCheckBP();
		bp.checkStatus(pk_material_vs, pk_group, pk_org, pk_supplier, pk_billtype, supgradestatus);
	}

	public BatchOperateVO batchSaveVO(BatchOperateVO vo)
			throws BusinessException {
		AceSupplierqualitystatusBatchSaveBP bp = new AceSupplierqualitystatusBatchSaveBP();
		return bp.batchSaveVO(vo);
	}

}
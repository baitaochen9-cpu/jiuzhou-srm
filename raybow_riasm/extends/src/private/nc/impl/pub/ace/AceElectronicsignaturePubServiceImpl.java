package nc.impl.pub.ace;

import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureInsertBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureUpdateBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureDeleteBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureSendApproveBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureUnSendApproveBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureApproveBP;
import nc.bs.riasm.electronicsignature.ace.bp.AceElectronicsignatureUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceElectronicsignaturePubServiceImpl {
	// 新增
	public AggElectronicSignatureVO[] pubinsertBills(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggElectronicSignatureVO> transferTool = new BillTransferTool<AggElectronicSignatureVO>(
					clientFullVOs);
			// 调用BP
			AceElectronicsignatureInsertBP action = new AceElectronicsignatureInsertBP();
			AggElectronicSignatureVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceElectronicsignatureDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggElectronicSignatureVO[] pubupdateBills(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggElectronicSignatureVO> transferTool = new BillTransferTool<AggElectronicSignatureVO>(
					clientFullVOs);
			AceElectronicsignatureUpdateBP bp = new AceElectronicsignatureUpdateBP();
			AggElectronicSignatureVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggElectronicSignatureVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggElectronicSignatureVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggElectronicSignatureVO> query = new BillLazyQuery<AggElectronicSignatureVO>(
					AggElectronicSignatureVO.class);
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
	public AggElectronicSignatureVO[] pubsendapprovebills(
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills)
			throws BusinessException {
		AceElectronicsignatureSendApproveBP bp = new AceElectronicsignatureSendApproveBP();
		AggElectronicSignatureVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggElectronicSignatureVO[] pubunsendapprovebills(
			AggElectronicSignatureVO[] clientFullVOs, AggElectronicSignatureVO[] originBills)
			throws BusinessException {
		AceElectronicsignatureUnSendApproveBP bp = new AceElectronicsignatureUnSendApproveBP();
		AggElectronicSignatureVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggElectronicSignatureVO[] pubapprovebills(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceElectronicsignatureApproveBP bp = new AceElectronicsignatureApproveBP();
		AggElectronicSignatureVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggElectronicSignatureVO[] pubunapprovebills(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceElectronicsignatureUnApproveBP bp = new AceElectronicsignatureUnApproveBP();
		AggElectronicSignatureVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}
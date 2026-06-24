package nc.impl.pub.ace;

import nc.bs.ewm.printapply.ace.bp.AcePrintapplyInsertBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplyUpdateBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplyDeleteBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplySendApproveBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplyUnSendApproveBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplyApproveBP;
import nc.bs.ewm.printapply.ace.bp.AcePrintapplyUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AcePrintapplyPubServiceImpl {
	// 新增
	public AggPrintapply[] pubinsertBills(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggPrintapply> transferTool = new BillTransferTool<AggPrintapply>(
					clientFullVOs);
			// 调用BP
			AcePrintapplyInsertBP action = new AcePrintapplyInsertBP();
			AggPrintapply[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AcePrintapplyDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggPrintapply[] pubupdateBills(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggPrintapply> transferTool = new BillTransferTool<AggPrintapply>(
					clientFullVOs);
			AcePrintapplyUpdateBP bp = new AcePrintapplyUpdateBP();
			AggPrintapply[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggPrintapply[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggPrintapply[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggPrintapply> query = new BillLazyQuery<AggPrintapply>(
					AggPrintapply.class);
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
	public AggPrintapply[] pubsendapprovebills(
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills)
			throws BusinessException {
		AcePrintapplySendApproveBP bp = new AcePrintapplySendApproveBP();
		AggPrintapply[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggPrintapply[] pubunsendapprovebills(
			AggPrintapply[] clientFullVOs, AggPrintapply[] originBills)
			throws BusinessException {
		AcePrintapplyUnSendApproveBP bp = new AcePrintapplyUnSendApproveBP();
		AggPrintapply[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggPrintapply[] pubapprovebills(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePrintapplyApproveBP bp = new AcePrintapplyApproveBP();
		AggPrintapply[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggPrintapply[] pubunapprovebills(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AcePrintapplyUnApproveBP bp = new AcePrintapplyUnApproveBP();
		AggPrintapply[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}
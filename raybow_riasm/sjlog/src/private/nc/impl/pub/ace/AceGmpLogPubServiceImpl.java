package nc.impl.pub.ace;

import nc.bs.riasm.gmplog.ace.bp.AceGmpLogInsertBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogUpdateBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogDeleteBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogSendApproveBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogUnSendApproveBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogApproveBP;
import nc.bs.riasm.gmplog.ace.bp.AceGmpLogUnApproveBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceGmpLogPubServiceImpl {
	// 新增
	public AggGmpLogConfigHvo[] pubinsertBills(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggGmpLogConfigHvo> transferTool = new BillTransferTool<AggGmpLogConfigHvo>(
					clientFullVOs);
			// 调用BP
			AceGmpLogInsertBP action = new AceGmpLogInsertBP();
			AggGmpLogConfigHvo[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceGmpLogDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggGmpLogConfigHvo[] pubupdateBills(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggGmpLogConfigHvo> transferTool = new BillTransferTool<AggGmpLogConfigHvo>(
					clientFullVOs);
			AceGmpLogUpdateBP bp = new AceGmpLogUpdateBP();
			AggGmpLogConfigHvo[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggGmpLogConfigHvo[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggGmpLogConfigHvo[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggGmpLogConfigHvo> query = new BillLazyQuery<AggGmpLogConfigHvo>(
					AggGmpLogConfigHvo.class);
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
	public AggGmpLogConfigHvo[] pubsendapprovebills(
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills)
			throws BusinessException {
		AceGmpLogSendApproveBP bp = new AceGmpLogSendApproveBP();
		AggGmpLogConfigHvo[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggGmpLogConfigHvo[] pubunsendapprovebills(
			AggGmpLogConfigHvo[] clientFullVOs, AggGmpLogConfigHvo[] originBills)
			throws BusinessException {
		AceGmpLogUnSendApproveBP bp = new AceGmpLogUnSendApproveBP();
		AggGmpLogConfigHvo[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggGmpLogConfigHvo[] pubapprovebills(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceGmpLogApproveBP bp = new AceGmpLogApproveBP();
		AggGmpLogConfigHvo[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggGmpLogConfigHvo[] pubunapprovebills(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceGmpLogUnApproveBP bp = new AceGmpLogUnApproveBP();
		AggGmpLogConfigHvo[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}
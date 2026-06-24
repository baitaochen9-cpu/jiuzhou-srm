package nc.impl.pub.ace;

import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyApproveBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyDeleteBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyInsertBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplySendApproveBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyUnApproveBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyUnSendApproveBP;
import nc.bs.jzqc.labelprintapply.ace.bp.AceLabelprintapplyUpdateBP;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.jzqc.labelprintapply.LabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.scmpub.util.VOEntityUtil;

public abstract class AceLabelprintapplyPubServiceImpl {
	// 新增
	public AggLabelprintapplyHVO[] pubinsertBills(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggLabelprintapplyHVO> transferTool = new BillTransferTool<AggLabelprintapplyHVO>(
					clientFullVOs);
			// 调用BP
			AceLabelprintapplyInsertBP action = new AceLabelprintapplyInsertBP();
			AggLabelprintapplyHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceLabelprintapplyDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggLabelprintapplyHVO[] pubupdateBills(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggLabelprintapplyHVO> transferTool = new BillTransferTool<AggLabelprintapplyHVO>(
					clientFullVOs);
			AceLabelprintapplyUpdateBP bp = new AceLabelprintapplyUpdateBP();
			AggLabelprintapplyHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggLabelprintapplyHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggLabelprintapplyHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggLabelprintapplyHVO> query = new BillLazyQuery<AggLabelprintapplyHVO>(
					AggLabelprintapplyHVO.class);
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
	
	public AggLabelprintapplyHVO[] pubquerybills(String where)
			throws BusinessException {
		AggLabelprintapplyHVO[] bills = null;
		try {

			VOQuery<LabelprintapplyHVO> query = new VOQuery<LabelprintapplyHVO>(
					LabelprintapplyHVO.class);

			LabelprintapplyHVO[] hvos = query.query(where, null);

			if (hvos == null || hvos.length == 0)
				return null;
			
			String[] pks = VOEntityUtil.getVOsNotRepeatValue(hvos, "pk_labelprintapply");
			SCMBillQuery<AggLabelprintapplyHVO> query1 = new SCMBillQuery<AggLabelprintapplyHVO>(
					AggLabelprintapplyHVO.class);
			return query1.queryVOByIDs(pks);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return bills;
	}
	

	// 提交
	public AggLabelprintapplyHVO[] pubsendapprovebills(
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills)
			throws BusinessException {
		AceLabelprintapplySendApproveBP bp = new AceLabelprintapplySendApproveBP();
		AggLabelprintapplyHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggLabelprintapplyHVO[] pubunsendapprovebills(
			AggLabelprintapplyHVO[] clientFullVOs, AggLabelprintapplyHVO[] originBills)
			throws BusinessException {
		AceLabelprintapplyUnSendApproveBP bp = new AceLabelprintapplyUnSendApproveBP();
		AggLabelprintapplyHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggLabelprintapplyHVO[] pubapprovebills(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceLabelprintapplyApproveBP bp = new AceLabelprintapplyApproveBP();
		AggLabelprintapplyHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggLabelprintapplyHVO[] pubunapprovebills(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceLabelprintapplyUnApproveBP bp = new AceLabelprintapplyUnApproveBP();
		AggLabelprintapplyHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

}
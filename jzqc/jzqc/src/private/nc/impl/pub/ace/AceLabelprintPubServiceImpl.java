package nc.impl.pub.ace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintApproveBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintCheckBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintDeleteBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintInsertBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintSendApproveBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintUnApproveBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintUnSendApproveBP;
import nc.bs.jzqc.labelprint.ace.bp.AceLabelprintUpdateBP;
import nc.bs.jzqc.labelprint.ace.bp.LabelprintInvalidBP;
import nc.bs.jzqc.labelprint.ace.bp.LabelprintPrintBP;
import nc.bs.jzqc.labelprint.ace.bp.LabelprintRepeatPrintApplyBP;
import nc.bs.jzqc.labelprint.ace.bp.LabelprintRepeatPrintRecordsBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.printcheck.PrintResultVO;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public abstract class AceLabelprintPubServiceImpl {
	// 新增
	public AggLabelPrintHVO[] pubinsertBills(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggLabelPrintHVO> transferTool = new BillTransferTool<AggLabelPrintHVO>(
					clientFullVOs);
			// 调用BP
			AceLabelprintInsertBP action = new AceLabelprintInsertBP();
			AggLabelPrintHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceLabelprintDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggLabelPrintHVO[] pubupdateBills(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggLabelPrintHVO> transferTool = new BillTransferTool<AggLabelPrintHVO>(
					clientFullVOs);
			AceLabelprintUpdateBP bp = new AceLabelprintUpdateBP();
			AggLabelPrintHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggLabelPrintHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggLabelPrintHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggLabelPrintHVO> query = new BillLazyQuery<AggLabelPrintHVO>(
					AggLabelPrintHVO.class);
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
	public AggLabelPrintHVO[] pubsendapprovebills(
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills)
			throws BusinessException {
		AceLabelprintSendApproveBP bp = new AceLabelprintSendApproveBP();
		AggLabelPrintHVO[] retvos = bp.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggLabelPrintHVO[] pubunsendapprovebills(
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills)
			throws BusinessException {
		AceLabelprintUnSendApproveBP bp = new AceLabelprintUnSendApproveBP();
		AggLabelPrintHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggLabelPrintHVO[] pubapprovebills(AggLabelPrintHVO[] clientFullVOs,
			AggLabelPrintHVO[] originBills) throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceLabelprintApproveBP bp = new AceLabelprintApproveBP();
		AggLabelPrintHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggLabelPrintHVO[] pubunapprovebills(
			AggLabelPrintHVO[] clientFullVOs, AggLabelPrintHVO[] originBills)
			throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceLabelprintUnApproveBP bp = new AceLabelprintUnApproveBP();
		AggLabelPrintHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 打印次数
	public Map<String, PrintResultVO> getPrintResultVO(Object[] datas)
			throws BusinessException {
		List<AggLabelPrintHVO> list = new ArrayList<AggLabelPrintHVO>();
		for (int i = 0; datas != null && i < datas.length; i++) {
			AggLabelPrintHVO aggvo = (AggLabelPrintHVO) datas[i];
			aggvo.getParentVO().setStatus(VOStatus.UPDATED);
			list.add(aggvo);
		}
		LabelprintPrintBP bp = new LabelprintPrintBP();
		Map<String, PrintResultVO> map = bp.getPrintResultVO(list
				.toArray(new AggLabelPrintHVO[list.size()]));
		return map;
	}

	public AggLabelPrintHVO[] updatePrintResultVO(Object[] datas)
			throws BusinessException {
		List<AggLabelPrintHVO> list = new ArrayList<AggLabelPrintHVO>();
		for (int i = 0; datas != null && i < datas.length; i++) {
			AggLabelPrintHVO aggvo = (AggLabelPrintHVO) datas[i];
			aggvo.getParentVO().setStatus(VOStatus.UPDATED);
			list.add(aggvo);
		}
		LabelprintPrintBP bp = new LabelprintPrintBP();
		AggLabelPrintHVO[] retvos = bp.updatePrintResultVO(list
				.toArray(new AggLabelPrintHVO[list.size()]));
		return retvos;
	}

	// 失效
	public AggLabelPrintHVO[] printInvalid(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		LabelprintInvalidBP bp = new LabelprintInvalidBP();
		AggLabelPrintHVO[] retvos = bp.printInvalid(clientFullVOs);
		return retvos;
	}

	// 重打申请
	public AggLabelPrintHVO[] repeatPrintApply(AggLabelPrintHVO[] clientFullVOs)
			throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		LabelprintRepeatPrintApplyBP bp = new LabelprintRepeatPrintApplyBP();
		AggLabelPrintHVO[] retvos = bp.repeatPrintApply(clientFullVOs);
		return retvos;
	}

	// 重打申请记录
	public AggLabelprintapplyHVO[] repeatPrintRecords(
			AggLabelPrintHVO clientFullVOs) throws BusinessException {
		clientFullVOs.getParentVO().setStatus(VOStatus.UPDATED);
		LabelprintRepeatPrintRecordsBP bp = new LabelprintRepeatPrintRecordsBP();
		AggLabelprintapplyHVO[] retvos = bp.repeatPrintRecords(clientFullVOs);
		return retvos;
	}

	public void checkExistsAggLabelPrintHVO(String pk_org, String[] billid,
			String transtype) throws BusinessException {
		AceLabelprintCheckBP bp = new AceLabelprintCheckBP();
		bp.checkAggLabelPrintHVO(pk_org, billid, transtype);
	}
}
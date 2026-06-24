package nc.impl.pub.ace;

import nc.bs.so.salepacklist.ace.bp.AceSalepacklistApproveBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistDeleteBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistInsertBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistRefQueryBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistSendApproveBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistUnApproveBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistUnSendApproveBP;
import nc.bs.so.salepacklist.ace.bp.AceSalepacklistUpdateBP;
import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.tool.BillTransferTool;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.salepacklist.AggSalePackListHVO;

public abstract class AceSalepacklistPubServiceImpl {
	// 新增
	public AggSalePackListHVO[] pubinsertBills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		try {
			// 数据库中数据和前台传递过来的差异VO合并后的结果
			BillTransferTool<AggSalePackListHVO> transferTool = new BillTransferTool<AggSalePackListHVO>(
					clientFullVOs);
			// 调用BP
			AceSalepacklistInsertBP action = new AceSalepacklistInsertBP();
			AggSalePackListHVO[] retvos = action.insert(clientFullVOs);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	// 删除
	public void pubdeleteBills(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		try {
			// 调用BP
			new AceSalepacklistDeleteBP().delete(clientFullVOs);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
	}

	// 修改
	public AggSalePackListHVO[] pubupdateBills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		try {
			// 加锁 + 检查ts
			BillTransferTool<AggSalePackListHVO> transferTool = new BillTransferTool<AggSalePackListHVO>(
					clientFullVOs);
			AceSalepacklistUpdateBP bp = new AceSalepacklistUpdateBP();
			AggSalePackListHVO[] retvos = bp.update(clientFullVOs, originBills);
			// 构造返回数据
			return transferTool.getBillForToClient(retvos);
		} catch (Exception e) {
			ExceptionUtils.marsh(e);
		}
		return null;
	}

	public AggSalePackListHVO[] pubquerybills(IQueryScheme queryScheme)
			throws BusinessException {
		AggSalePackListHVO[] bills = null;
		try {
			this.preQuery(queryScheme);
			BillLazyQuery<AggSalePackListHVO> query = new BillLazyQuery<AggSalePackListHVO>(
					AggSalePackListHVO.class);
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
	public AggSalePackListHVO[] pubsendapprovebills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		AceSalepacklistSendApproveBP bp = new AceSalepacklistSendApproveBP();
		AggSalePackListHVO[] retvos = bp
				.sendApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 收回
	public AggSalePackListHVO[] pubunsendapprovebills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		AceSalepacklistUnSendApproveBP bp = new AceSalepacklistUnSendApproveBP();
		AggSalePackListHVO[] retvos = bp.unSend(clientFullVOs, originBills);
		return retvos;
	};

	// 审批
	public AggSalePackListHVO[] pubapprovebills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSalepacklistApproveBP bp = new AceSalepacklistApproveBP();
		AggSalePackListHVO[] retvos = bp.approve(clientFullVOs, originBills);
		return retvos;
	}

	// 弃审

	public AggSalePackListHVO[] pubunapprovebills(
			AggSalePackListHVO[] clientFullVOs, AggSalePackListHVO[] originBills)
			throws BusinessException {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AceSalepacklistUnApproveBP bp = new AceSalepacklistUnApproveBP();
		AggSalePackListHVO[] retvos = bp.unApprove(clientFullVOs, originBills);
		return retvos;
	}

	// 查询

	public AggSalePackListHVO[] queryAllBillDatas(String where)
			throws BusinessException {
		AceSalepacklistRefQueryBP bp = new AceSalepacklistRefQueryBP();
		AggSalePackListHVO[] retvos = bp.queryAllBillDatas(where);
		return retvos;
	}

}
package nc.ui.ewm.workorder.action;

import java.awt.event.ActionEvent;
import java.util.Map;

import nc.ui.am.action.info.ActionInfoInitalizer;
import nc.ui.am.action.info.IAMActionCode;
import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.action.taskbatch.NetFlowMode;
import nc.ui.am.action.taskself.IBillOperateService;
import nc.ui.am.action.taskself.TaskSelfAction;
import nc.ui.am.model.BillManageModel;
import nc.ui.ewm.workorder.dlg.WoStatusDialog;
import nc.ui.ewm.workorder.model.UpdateStatusService;
import nc.ui.ewm.workorder.view.WorkOrderViewUtil;
import nc.ui.pub.beans.UIDialog;
import nc.ui.uif2.UIState;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOHisVO;
import nc.vo.pub.AggregatedValueObject;

/**
 * 工单更改状态动作
 * 
 * @author zhengss
 * @author cuikai
 */
@SuppressWarnings("serial")
public class AlterStatusAction extends TaskSelfAction {
	public AlterStatusAction() {
		super();
		ActionInfoInitalizer.initializeAction(this, IAMActionCode.AlterStatus);
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		// 取得选中数据
		BillManageModel billManageModel = getModel();
		if (billManageModel == null)
			return;
		AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) DataProcess
				.getSelectedViewData(billManageModel);
		if (ArrayUtils.isEmpty(billVOs)) {
			return;
		}
		// 校验选择的状态类别必须相同
		if (!isSameStatusType(billVOs)) {
			showErrorMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("workorder_0", "04560003-0077")/*
																 * @res
																 * "必须选择相同工单状态"
																 */);
			return;
		}
		// 卡片界面需要先校验,校验通过后然后弹出对话框
		this.validate(billVOs);
		
		// 用于解决复制时，没有选择当前单据问题
		AggWorkOrderVO billVO = (AggWorkOrderVO)billManageModel.getSelectedData();
		
		String currStatusTypeid = (billVO.getParentVO()).getPk_wo_status();

		// 根据当前状态类别获得下一个可用的工单状态类别
		Integer[] statustype = getUsableStatusType(billVO);
		
		WoStatusDialog dlg = new WoStatusDialog(getModel().getContext()
				.getEntranceUI(), statustype, currStatusTypeid, getModel()
				.getContext());
		if (UIDialog.ID_OK == dlg.showModal()) {
			Map<String, String> m_para = dlg.getRetMap();
			WOHisVO paraVO = new WOHisVO();
			paraVO.setPk_wostatus(m_para.get(WoStatusDialog.RET_PK_WO_STATUS));
			paraVO.setMemo(m_para.get(WoStatusDialog.RET_MEMO));
			paraVO.setWo_statustype(new Integer(m_para
					.get(WoStatusDialog.RET_STATUS_TYPE)));
			setUserObject(paraVO);

			// 执行状态更改相关业务
			Object retObj = procOperation(billVOs, e);
			// 刷新界面ts和工单状态
			getModel().directlyUpdate(retObj);
			// 切换数据状态
			getModel().toDataStatus();
		}
	}

	@Override
	protected AggregatedValueObject[] filterBillVOByValidate(
			AggregatedValueObject[] billVOs, ActionEvent e) {
		//doAction方法已做过滤，此处不再做
		return billVOs;
	}

	/**
	 * 根据当前工单状态类型确定下一个可用工单状态类型
	 * 
	 * @param AggBillVo
	 * @return
	 */
	private Integer[] getUsableStatusType(AggWorkOrderVO AggBillVo) {
		// 取可用状态类别
		Integer statusType = (AggBillVo.getParentVO()).getWo_statustype();
		Integer[] statusTypes = WorkOrderViewUtil.getInstance()
				.getStatusTypeTransTO(statusType);
		Integer[] newStatusTypes = new Integer[statusTypes.length + 1];
		System.arraycopy(statusTypes, 0, newStatusTypes, 0, statusTypes.length);
		newStatusTypes[statusTypes.length] = statusType;
		return newStatusTypes;
	}
	
	/**
	 * 
	 * 方法功能:工单状态类别是否相同。
	 * 
	 * @author:cuikai
	 * @date:2011-4-27
	 * @version:60
	 * @param billVOs
	 * @return
	 */
	private boolean isSameStatusType(AggWorkOrderVO[] billVOs) {
		Integer statusType = (billVOs[0].getParentVO()).getWo_statustype();
		for (int i = 1; i < billVOs.length; i++) {
			Integer statusType2 = (billVOs[i].getParentVO()).getWo_statustype();
			if (!statusType.equals(statusType2)) {
				return false;
			}
		}
		return true;
	}


	@Override
	protected boolean isActionEnable() {
		return getModel().getUiState() == UIState.NOT_EDIT
				&& getModel().getSelectedData() != null;
	}

	@Override
	public IBillOperateService getBillOperateService() {
		return new UpdateStatusService();
	}

	@Override
	protected NetFlowMode getNetFlowMode() {
		return NetFlowMode.NOTREAT;
	}

	@Override
	public String getActionCode() {
		return null;
	}
}
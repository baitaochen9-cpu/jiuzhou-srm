package nc.ui.ewm.workorder.action;

import java.awt.event.ActionEvent;
import java.util.Map;

import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.action.taskbatch.NetFlowMode;
import nc.ui.am.action.taskself.IBillOperateService;
import nc.ui.am.action.taskself.TaskSelfAction;
import nc.ui.am.base.manager.DataModelDelegator;
import nc.ui.am.model.BillManageModel;
import nc.ui.ewm.workorder.dlg.WoDateDialog;
import nc.ui.ewm.workorder.model.UpdateStatusService;
import nc.ui.ewm.workorder.view.WorkOrderViewUtil;
import nc.ui.uif2.UIState;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDateTime;

public class AlterDateAction extends TaskSelfAction {
	public AlterDateAction() {
		this.setBtnName("批量修改");
		this.setCode("AlterDate");
	}

    protected DataModelDelegator dataManager;

	// UFDateTimeTextField
	public void doAction(ActionEvent e) throws Exception {
		BillManageModel billManageModel = getModel();
		if (billManageModel == null)
			return;
		AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) DataProcess
				.getSelectedViewData(billManageModel);

		if (ArrayUtils.isEmpty(billVOs)) {
			return;
		}
		validate(billVOs);

		AggWorkOrderVO billVO = (AggWorkOrderVO) billManageModel
				.getSelectedData();

		String currStatusTypeid = billVO.getParentVO().getPk_wo_status();

		Integer[] statustype = getUsableStatusType(billVO);

		WoDateDialog dlg = new WoDateDialog(getModel().getContext()
				.getEntranceUI(), statustype, currStatusTypeid, getModel()
				.getContext());

		if (1 == dlg.showModal()) {
			Map<String, String> m_para = dlg.getRetMap();
			String begin = m_para.get("begin");
			String end = m_para.get("end");
			for (AggWorkOrderVO aggvo : billVOs) {
				if (begin != null)
					aggvo.getParentVO().setPlan_start_time(
							new UFDateTime(begin));
				if (end != null)
					aggvo.getParentVO().setPlan_end_time(new UFDateTime(end));
			}

			Object retObj = new UpdateStatusService()
					.updateWorkOrderHeadVO(billVOs);
			getModel().directlyUpdate(retObj);
			nc.ui.am.action.support.AMRefreshAction bean = new nc.ui.am.action.support.AMRefreshAction();
			bean.setModel(getModel());
			bean.setDataManager(getDataManager());
			ActionEvent el = new ActionEvent(bean, 1001, "刷新");
			bean.doAction(el);
			nc.ui.am.action.support.AMRefreshAllAction bean1 = new nc.ui.am.action.support.AMRefreshAllAction();
			bean1.setModel(getModel());
			bean1.setDataManager(getDataManager());
			ActionEvent el1 = new ActionEvent(bean1, 1001, "刷新");
			bean1.doAction(el1);
			getModel().toDataStatus();
		}
	}

	protected AggregatedValueObject[] filterBillVOByValidate(
			AggregatedValueObject[] billVOs, ActionEvent e) {
		return billVOs;
	}

	private Integer[] getUsableStatusType(AggWorkOrderVO AggBillVo) {
		Integer statusType = AggBillVo.getParentVO().getWo_statustype();
		Integer[] statusTypes = WorkOrderViewUtil.getInstance()
				.getStatusTypeTransTO(statusType);

		Integer[] newStatusTypes = new Integer[statusTypes.length + 1];
		System.arraycopy(statusTypes, 0, newStatusTypes, 0, statusTypes.length);
		newStatusTypes[statusTypes.length] = statusType;
		return newStatusTypes;
	}

	protected boolean isActionEnable() {
		return (getModel().getUiState() == UIState.NOT_EDIT)
				&& (getModel().getSelectedData() != null);
	}

	public IBillOperateService getBillOperateService() {
		return new UpdateStatusService();
	}

	protected NetFlowMode getNetFlowMode() {
		return NetFlowMode.NOTREAT;
	}

	public String getActionCode() {
		return null;
	}

	public DataModelDelegator getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataModelDelegator dataManager) {
		this.dataManager = dataManager;
	}
	
	
}

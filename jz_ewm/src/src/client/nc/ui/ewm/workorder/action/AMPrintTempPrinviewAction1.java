package nc.ui.ewm.workorder.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nc.bd.ewm.print.IProcessService;
import nc.bs.framework.common.NCLocator;
import nc.ui.am.action.support.AMPrintTempPrinviewAction;
import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.model.BillManageModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.RefreshSingleAction;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;

public class AMPrintTempPrinviewAction1 extends AMPrintTempPrinviewAction {

	private boolean isCheckStatusType(AggWorkOrderVO[] billVOs) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);// 批准
		list.add(2);// 進行中
		list.add(3);// 完成
		list.add(4);// 已報告
		for (int i = 0; i < billVOs.length; i++) {
			Integer statusType2 = billVOs[i].getParentVO().getWo_statustype();
			if (!list.contains(statusType2)) {
				return true;
			}
		}
		return false;
	}

	public void doAction(ActionEvent e) throws Exception {

		BillManageModel billManageModel = ((BillManageModel) getModel());
		AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) DataProcess
				.getSelectedViewData(billManageModel);
		String def9 = billVOs[0].getParentVO().getDef9();
		String def1 = billVOs[0].getParentVO().getDef1();
		// 判断可重复打印参数
		IProcessService ip = NCLocator.getInstance().lookup(
				IProcessService.class);
		Boolean isprint = ip.isPrint(billVOs[0].getParentVO().getPk_org());
		if (isprint) {
			if (StringUtils.contains(def9, "N") || def9 == null) {
				showErrorMessage("提示", "可打印标识为否,请点击打印申请按钮提交申请单");
				//刷新
				RefreshSingleAction refreshaction = new RefreshSingleAction();
				refreshaction.setModel(this.getModel());
				ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
				refreshaction.doAction(e1);	
			} else {
				super.doAction(e);
				// 打印次数更新
				if (def1 == null) {
					ip.printUpdate(1, billVOs);
					//刷新
					RefreshSingleAction refreshaction = new RefreshSingleAction();
					refreshaction.setModel(this.getModel());
					ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
					refreshaction.doAction(e1);	
				} else {
					int def = Integer.valueOf(def1);
					def++;
					ip.printUpdate(def, billVOs);
					//刷新
					RefreshSingleAction refreshaction = new RefreshSingleAction();
					refreshaction.setModel(this.getModel());
					ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
					refreshaction.doAction(e1);	
				}
				if (isprint) {
					ip.printCountUpdate(billVOs);
					//刷新
					RefreshSingleAction refreshaction = new RefreshSingleAction();
					refreshaction.setModel(this.getModel());
					ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
					refreshaction.doAction(e1);	
				}
			}
		}else{
			//刷新
			RefreshSingleAction refreshaction = new RefreshSingleAction();
			refreshaction.setModel(this.getModel());
			ActionEvent e1 = new ActionEvent(refreshaction, 1001, "刷新");
			refreshaction.doAction(e1);	
			super.doAction(e);
		}

	}

	public void showErrorMessage(String title, String err) {
		ShowStatusBarMsgUtil.showErrorMsg(title, err, getModel().getContext());
	}

	protected boolean isActionEnable() {

		BillManageModel billManageModel = ((BillManageModel) getModel());
		if (billManageModel == null)
			return false;
		AggWorkOrderVO[] billVOs = (AggWorkOrderVO[]) DataProcess
				.getSelectedViewData(billManageModel);

		if (ArrayUtils.isEmpty(billVOs)) {
			return false;
		}
		if (isCheckStatusType(billVOs)) {
			return false;
		}
		return (getModel().getUiState() == UIState.NOT_EDIT)
				&& (getModel().getSelectedData() != null);
	}
}

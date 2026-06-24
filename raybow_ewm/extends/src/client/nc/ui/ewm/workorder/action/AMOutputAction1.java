package nc.ui.ewm.workorder.action;

import java.util.ArrayList;
import java.util.List;

import nc.ui.am.action.support.AMOutputAction;
import nc.ui.am.action.taskbatch.DataProcess;
import nc.ui.am.model.BillManageModel;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.ewm.workorder.AggWorkOrderVO;

public class AMOutputAction1 extends AMOutputAction {

	private boolean isCheckStatusType(AggWorkOrderVO[] billVOs) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);//批准
		list.add(2);//進行中
		list.add(3);//完成
		list.add(4);//已報告
		for (int i = 0; i < billVOs.length; i++) {
			Integer statusType2 = billVOs[i].getParentVO().getWo_statustype();
			if (!list.contains(statusType2)) {
				return true;
			}
		}
		return false;
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
			return  false;
		}
		if(isCheckStatusType(billVOs)){
			return false;
		}
		return (getModel().getUiState() == UIState.NOT_EDIT)
				&& (getModel().getSelectedData() != null);
	}
}

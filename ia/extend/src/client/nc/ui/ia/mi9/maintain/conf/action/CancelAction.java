package nc.ui.ia.mi9.maintain.conf.action;

import java.awt.event.ActionEvent;

import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.vo.ml.NCLangRes4VoTransl;

public class CancelAction extends nc.ui.uif2.actions.CancelAction {
	private static final long serialVersionUID = 6772677649190155374L;
	/* 17 */protected boolean canceled = false;

	public void doAction(ActionEvent e) throws Exception {

		getModel().setUiState(UIState.NOT_EDIT);
		getModel().setOtherUiState(UIState.NOT_EDIT);

		doResetSelectedData();
		ShowStatusBarMsgUtil.showStatusBarMsg(NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("pubapp_0", "0pubapp-0125"), getModel()
				.getContext());

		canceled = true;
	}

	public boolean isCanceled() {
		return canceled;
	}
}

package nc.ui.jzqc.labelcontrol.action;

import java.awt.event.ActionEvent;

import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.batch.BatchEditAction;
import nc.ui.uif2.model.HierachicalDataAppModel;

public class ReceiverConfBatchEditAction extends BatchEditAction {
	private static final long serialVersionUID = 7063105451755026891L;
	private HierachicalDataAppModel treeModel;

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

	public void doAction(ActionEvent e) throws Exception {

		super.doAction(e);
		 getModel().setUiState(UIState.EDIT);
	}

}

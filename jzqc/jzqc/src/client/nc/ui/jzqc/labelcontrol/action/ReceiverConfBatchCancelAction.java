package nc.ui.jzqc.labelcontrol.action;

import java.awt.event.ActionEvent;

import nc.buzimsg.view.RcvconfBatchBillTable;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.batch.BatchCancelAction;
import nc.ui.uif2.model.HierachicalDataAppModel;

public class ReceiverConfBatchCancelAction extends BatchCancelAction {
	private static final long serialVersionUID = 5597482769890348763L;
	private HierachicalDataAppModel treeModel;

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

	public void doAction(ActionEvent e) throws Exception {
		super.doAction(e);

		((RcvconfBatchBillTable) getEditor()).displayReceiverColumnValue();
		 getModel().setUiState(UIState.NOT_EDIT);
	}
}

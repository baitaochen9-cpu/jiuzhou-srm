package nc.ui.jzqc.labelcontrol.action;

import java.awt.event.ActionEvent;

import nc.buzimsg.view.RcvconfBatchBillTable;
import nc.ui.uif2.actions.batch.BatchDelLineAction;

public class ReceiverConfBatchDelLineAction extends BatchDelLineAction {
	private static final long serialVersionUID = 3218783315215962150L;

	public void doAction(ActionEvent e) throws Exception {

		super.doAction(e);
		RcvconfBatchBillTable.clearMap();
	}

}

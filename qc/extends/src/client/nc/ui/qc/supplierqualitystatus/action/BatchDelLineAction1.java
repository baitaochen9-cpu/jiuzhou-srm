package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.bs.logging.Logger;
import nc.ui.uif2.actions.batch.BatchDelLineAction;

public class BatchDelLineAction1 extends BatchDelLineAction {
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Logger.debug("Entering " + super.getClass().toString()
				+ ".actionPerformed");
		boolean ischeck = checkElectronicSignature(this.getModel().getContext(), "É¾³ý",this.getModel());
		if (!ischeck)
			return;
		super.doAction(e);
	}
	
}

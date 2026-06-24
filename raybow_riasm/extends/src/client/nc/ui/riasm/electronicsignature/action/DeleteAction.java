package nc.ui.riasm.electronicsignature.action;

import java.awt.event.ActionEvent;

import nc.ui.mmgp.uif2.actions.MMGPDeleteAction;
import nc.ui.uif2.model.IQueryAndRefreshManager;

public class DeleteAction extends MMGPDeleteAction {
	private static final long serialVersionUID = -8223180975614827L;
	private IQueryAndRefreshManager dataManager = null;
	public void doAction(ActionEvent e) throws Exception {

		super.doAction(e);
		getDataManager().refresh();
	}
	public IQueryAndRefreshManager getDataManager() {
		return dataManager;
	}
	public void setDataManager(IQueryAndRefreshManager dataManager) {
		this.dataManager = dataManager;
	}
}

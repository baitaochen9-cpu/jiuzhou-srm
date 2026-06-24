package nc.ui.qc.supplierqualitystatus.lazilyload;

import nc.ui.pubapp.uif2app.event.AppUiStateChangeEvent;
import nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;

public class SupplierLazilyLoadManager extends LazilyLoadManager {
	private BillManageModel model;

	@Override
	public void setModel(BillManageModel billManageModel) {
		model = billManageModel;
		model.addAppEventListener(new AppEventListener() {
			public void handleEvent(AppEvent event) {
				if (event.getType() == "Selection_Changed") {
					loadCurChildren();
				} else if ((event.getType() == "UiState_Changed")
						&& (((AppUiStateChangeEvent) event).getNewState() == UIState.EDIT)
						&& (((AppUiStateChangeEvent) event).getOldState() == UIState.NOT_EDIT)) {
					loadCurChildren();
				}
			}
		});
	}

	void loadCurChildren() {

	}

}

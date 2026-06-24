package nc.ui.qc.supplierqualitystatus.view;

import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.TangramContainer;
import nc.ui.uif2.model.AppEventConst;
import nc.ui.uif2.model.BillManageModel;

/**
 * 控制哪些子页签进行显示的中间调节器
 * 
 * @author lkp
 * 
 */
public class TableDisplayMediator implements AppEventListener {

	private BillManageModel funcModel = null;

	private TangramContainer container = null;
	
	private BodyInfoEditor btnInfoEditor = null;

	private Object lastSelectObj = null;

	@Override
	public void handleEvent(AppEvent event) {

		if (event.getType() == AppEventConst.SELECTION_CHANGED) {
			Object obj = getFuncModel().getSelectedData();
			lastSelectObj = obj;
			btnInfoEditor.setComponentDisplayable(true);
			container.resetLayout();
		}
	}

	public BillManageModel getFuncModel() {
		return funcModel;
	}

	public void setFuncModel(BillManageModel funcModel) {
		this.funcModel = funcModel;
		this.funcModel.addAppEventListener(this);
	}

	public TangramContainer getContainer() {
		return container;
	}

	public void setContainer(TangramContainer container) {
		this.container = container;
	}

	public BodyInfoEditor getBtnInfoEditor() {
		return btnInfoEditor;
	}

	public void setBtnInfoEditor(BodyInfoEditor btnInfoEditor) {
		this.btnInfoEditor = btnInfoEditor;
	}
	
}

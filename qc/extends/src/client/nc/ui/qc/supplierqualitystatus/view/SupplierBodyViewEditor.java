package nc.ui.qc.supplierqualitystatus.view;

import java.awt.CardLayout;

import nc.ui.mmgp.uif2.model.MMGPBillManageModel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.AppEventListener;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillListView;
import nc.ui.uif2.model.AppEventConst;

/**
 * 功能节点和页签信息的编辑器
 * 
 * @author lkp
 * 
 */
public class SupplierBodyViewEditor extends UIPanel implements AppEventListener {

	private static final long serialVersionUID = 3074100989006637904L;

	private BillListView funcEditor = null;

	private static final String FUNCEDITORVIEW = "funceditorview";

	private String currentView = FUNCEDITORVIEW;

	private CardLayout layout = null;
	/**
	 * 通过IoC注入
	 */
	private MMGPBillManageModel model;

	public void initUI() {
		layout = new CardLayout();
		setLayout(layout);
		add(funcEditor, FUNCEDITORVIEW);
		layout.show(this, FUNCEDITORVIEW);
	}
	public void handleEvent(AppEvent event) {

		/**
		 * 选择节点发生了变化或者模型状态转换成非编辑状态时，需要判断是否切换视图。
		 */
		if (AppEventConst.SELECTION_CHANGED == event.getType()
				|| AppEventConst.UISTATE_CHANGED == event.getType()
				&& model.getUiState() == UIState.NOT_EDIT)
			this.changeViewIfNeed();

		if (AppEventConst.UISTATE_CHANGED == event.getType()
				&& model.getUiState() == UIState.ADD) {
			this.turnToFuncView();
		}
	}

	public BillListView getFuncEditor() {
		return funcEditor;
	}

	public void setFuncEditor(BillListView funcEditor) {
		this.funcEditor = funcEditor;
	}

	public MMGPBillManageModel getModel() {
		return model;
	}

	public void setModel(MMGPBillManageModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	private void changeViewIfNeed() {
		//
		this.turnToFuncView();

	}

	private void turnToFuncView() {
		layout.show(this, FUNCEDITORVIEW);
		currentView = FUNCEDITORVIEW;
	}

}

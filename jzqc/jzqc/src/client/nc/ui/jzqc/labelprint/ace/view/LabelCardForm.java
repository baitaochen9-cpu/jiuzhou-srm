package nc.ui.jzqc.labelprint.ace.view;

import nc.ui.ic.batchcode.ref.BatchRefPane;
import nc.ui.jzqc.labelprint.ace.view.ref.BatchCodeRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;

public class LabelCardForm extends ShowUpableBillForm {
	private static final long serialVersionUID = 6251861701753657625L;

	public void initUI() {
		super.initUI();
		setBatchCodeRef();
	}

	private void setBatchCodeRef() {
		BillCardPanel panel = getBillCardPanel();

		UIRefPane uipanel = (UIRefPane) panel.getHeadItem("pk_batchcode")
				.getComponent();
		uipanel.setRefModel(new BatchCodeRefModel());
//		BatchRefPane pane = new BatchRefPane();
//		panel.getHeadItem("pk_batchcode").setComponent(pane);
//		UFRefBillItemEditor editor = new UFRefBillItemEditor(
//				panel.getHeadItem("pk_batchcode"));
//		editor.setComponent(pane);
//		panel.getHeadItem("pk_batchcode").setItemEditor(editor);
//		panel.getHeadItem("pk_batchcode").
//		((UIRefPaneTextField) uipanel.getUITextField()).setUIRefPane(pane);
		// uipanel.getUIButton().addActionListener(paramActionListener);
		// uipanel.setRefEditable(true);
		// panel.getHeadItem("pk_batchcode").setEnabled(false);
	}

	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			BillCardPanel panel = getBillCardPanel();
			BatchRefPane uipanel = (BatchRefPane) panel.getHeadItem(
					"pk_batchcode").getComponent();
			uipanel.onButtonClicked();
		};
	};

}

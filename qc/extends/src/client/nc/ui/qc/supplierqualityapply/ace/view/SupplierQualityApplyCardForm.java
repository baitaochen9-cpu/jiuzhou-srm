package nc.ui.qc.supplierqualityapply.ace.view;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;

public class SupplierQualityApplyCardForm extends ShowUpableBillForm {
	private static final long serialVersionUID = 6251861701753657625L;

	public void initUI() {
		super.initUI();
	}
	
	public void setValue(Object object) {
		super.setValue(object);
//		setBatchCodeRef();
	}

	private void setBatchCodeRef() {
		BillCardPanel panel = getBillCardPanel();

		String pk_org =  panel.getHeadItem("pk_org").getValue();
		UIRefPane uipanel = (UIRefPane) panel.getHeadItem("pk_suppliergrade")
				.getComponent();
		uipanel.getRefModel().setPk_org(pk_org);
		
		UIRefPane uipanel1 = (UIRefPane) panel.getHeadItem("pk_grade_info")
				.getComponent();
		uipanel1.getRefModel().setPk_org(pk_org);
	}
}

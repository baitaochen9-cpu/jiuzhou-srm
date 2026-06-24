package nc.ui.qc.supplierqualitystatus.ace.handler;

import nc.ui.bd.ref.model.SupplierDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 费用单表头编辑前事件
 * @author hgs
 *
 */
public class CardHeadBeforeHandler  implements IAppEventHandler<CardHeadTailBeforeEditEvent>{
	private BillForm billform;
	
	public BillForm getBillform() {
		return billform;
	}

	public void setBillform(BillForm billform) {
		this.billform = billform;
	}
	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		e.setReturnValue(true);
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if("pk_supplier".equals(key)){//供应商
			String def1 = keyValue.getHeadStringValue("pk_parent");//供应商
			UIRefPane refpanel = (UIRefPane) panel.getHeadItem(key).getComponent();
			SupplierDefaultRefModel gatherref = (SupplierDefaultRefModel)refpanel.getRefModel();
			String sqlwhere = "";
			if(def1!=null){
				sqlwhere = sqlwhere + " and pk_supplierclass='" + def1 + "' ";
			}
			gatherref.addWherePart(sqlwhere);
		}
	}
}

package nc.ui.riasm.electronicsignature.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.riasm.electronicsignature.view.TreeCardBillForm;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uif.pub.exception.UifException;

/**
 * 表体编辑后事件
 * 
 * 
 */
public class CardItemAfterHandler implements
		IAppEventHandler<CardBodyAfterEditEvent> {

	private TreeCardBillForm billForm;
	private AbstractAppModel model;

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public TreeCardBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(TreeCardBillForm billForm) {
		this.billForm = billForm;
	}

	@SuppressWarnings("all")
	@Override
	public void handleAppEvent(CardBodyAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		String key = e.getKey();
		if ("btnid".equals(e.getKey())) {
			Object value = e.getValue();
			try {
				Object o = HYPubBO_Client.findColValue("sm_butnregister", "btnname", " nvl(dr,0) = 0 and pk_btn = '"+value+"'");
				panel.getBillModel().setValueAt(o, e.getRow(), "btnname");
			} catch (UifException e1) {
				e1.printStackTrace();
			}
			
		}
	}

}

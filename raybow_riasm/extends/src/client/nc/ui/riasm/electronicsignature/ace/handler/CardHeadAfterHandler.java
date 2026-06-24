package nc.ui.riasm.electronicsignature.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.riasm.electronicsignature.view.TreeCardBillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 表头编辑后事件
 * 
 */
public class CardHeadAfterHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {

	private TreeCardBillForm billform;
	private AbstractAppModel model;

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	public TreeCardBillForm getBillform() {
		return billform;
	}

	public void setBillform(TreeCardBillForm billform) {
		this.billform = billform;
	}

	@SuppressWarnings("all")
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if ("currency".equals(key)) {// 币种编辑后带出汇率

		}
	}
}

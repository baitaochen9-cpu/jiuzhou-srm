package nc.ui.riasm.electronicsignature.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.riasm.electronicsignature.view.TreeCardBillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 表头编辑前事件
 * 
 */
public class CardHeadBeforeHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {
	private TreeCardBillForm billform;

	public TreeCardBillForm getBillform() {
		return billform;
	}

	public void setBillform(TreeCardBillForm billform) {
		this.billform = billform;
	}

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		e.setReturnValue(true);
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if ("pk_consign_pact".equals(key)) {// 托运合同
			
		}
	}
}

package nc.ui.riasm.electronicsignature.ace.handler;

import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 表体编辑前事件
 * 
 * 
 */
public class CardItemBeforeHandler implements
		IAppEventHandler<CardBodyBeforeEditEvent> {

	@SuppressWarnings("all")
	@Override
	public void handleAppEvent(CardBodyBeforeEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if ("btnid".equals(e.getKey())) {
			UIRefPane billref = (UIRefPane) panel.getBodyItem(e.getKey())
					.getComponent();
			if (billref.getRefModel() instanceof nc.ui.uap.funcreg.ref.BtnRefModel) {
				nc.ui.uap.funcreg.ref.BtnRefModel refmodel = (nc.ui.uap.funcreg.ref.BtnRefModel) billref
						.getRefModel();
				UIRefPane billref1 = (UIRefPane) panel.getHeadItem("pk_parent")
						.getComponent();
				if (billref1.getRefModel() instanceof nc.ui.uap.funcreg.ref.FunctionRefModel) {
					nc.ui.uap.funcreg.ref.FunctionRefModel refmodel1 = (nc.ui.uap.funcreg.ref.FunctionRefModel) billref1
							.getRefModel();
					String code = refmodel1.getPkValue();
					Object o = null;
					try {
						o = HYPubBO_Client.findColValue("sm_funcregister", "cfunid", " nvl(dr,0) = 0 and funcode = '"+code+"'");
					} catch (UifException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					refmodel.addWherePart(" and parent_id = '"
							+ o  + "'");
				}
			}
		}
		e.setReturnValue(true);
	}
}

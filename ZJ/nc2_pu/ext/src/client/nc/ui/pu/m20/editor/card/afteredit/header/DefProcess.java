package nc.ui.pu.m20.editor.card.afteredit.header;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pu.pub.editor.card.listener.ICardHeadTailAfterEditEventListener;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pub.beans.UIRefPane;


public class DefProcess implements ICardHeadTailAfterEditEventListener {

	@Override
	public void afterEdit(CardHeadTailAfterEditEvent event) {
		// TODO Auto-generated method stub
		UIRefPane refPanel = (UIRefPane) event.getBillCardPanel().getHeadItem("vdef17").getComponent();
		String bsctype = (String) event.getBillCardPanel().getHeadItem("vtrantypecode").getValueObject();
		AbstractRefModel refModel = refPanel.getRefModel();
		refModel.clearData();
		refModel.addWherePart("and code like '"+bsctype+"%'");
	}

}

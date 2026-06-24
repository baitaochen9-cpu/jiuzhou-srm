package nc.ui.ic.m4n.handler;

import nc.ui.ic.special.handler.VbatchcodeHandlerForSpecial;
import nc.ui.pubapp.uif2app.event.card.CardBodyBeforeEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;

public class VbatchcodeHandlerForSpecialFor4N extends VbatchcodeHandlerForSpecial{

	@Override
	public void beforeCardBodyEdit(CardBodyBeforeEditEvent event) {
		super.beforeCardBodyEdit(event);
		String vtrantypecode = (String) getEditorModel().getICBizView()
				.getBillCardPanel().getHeadItem("vdef20")
				.getValueObject();
		String code = null;
		try {
			code = (String)HYPubBO_Client.findColValue("bd_defdoc", "code", " nvl(dr,0) = 0 and pk_defdoc = '"+vtrantypecode+"'");
		} catch (UifException e) {
			e.printStackTrace();
		}
		if (!"02".equals(code)) {// ¹ÞÇøÈÜ¼ÁºÏÅú
			return;
		}
	}
}

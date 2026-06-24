package nc.ui.ic.m4n.handler;

import nc.ui.ic.special.handler.MainNumHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m4n.entity.TransformBodyVO;

public class MainNumHandlerFor4N extends MainNumHandler {

	@Override
	public void afterCardBodyEdit(CardBodyAfterEditEvent event) {
		super.afterCardBodyEdit(event);
		String vtrantypecode = (String) getEditorModel().getICBizView()
				.getBillCardPanel().getHeadItem("vdef20")
				.getValueObject();
		String code = null;
		try {
			code = (String)HYPubBO_Client.findColValue("bd_defdoc", "code", " nvl(dr,0) = 0 and pk_defdoc = '"+vtrantypecode+"'");
		} catch (UifException e) {
			e.printStackTrace();
		}
		if (!"01".equals(code)) {// 序列号拆分
			return;
		}
		if (event.getRow() != 0)
			return;
		// 清空其他行数量
		clearOtherRowNum();
	}

	private void clearOtherRowNum() {
		int count = getEditorModel().getICBizView().getBillCardPanel()
				.getRowCount();
		if (count > 1) {
			for (int i = 1; i < count; i++) {
				getEditorModel().getICBizView().getBillCardPanel()
						.getBillModel()
						.setValueAt(null, i, TransformBodyVO.NNUM);
				BillEditEvent billEditEnent = new BillEditEvent(
						getEditorModel().getICBizView().getBillCardPanel(),
						null, "nnum", i);
				CardBodyAfterEditEvent cardEditEvent = new CardBodyAfterEditEvent(
						getEditorModel().getICBizView().getBillCardPanel(),
						billEditEnent, null);
				getEditorModel().getICBizView().getModel()
						.fireEvent(cardEditEvent);
			}
		}
	}
}

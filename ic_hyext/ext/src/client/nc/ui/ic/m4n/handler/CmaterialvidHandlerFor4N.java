package nc.ui.ic.m4n.handler;

import java.util.Set;

import nc.ui.ic.special.handler.CmaterialvidHandler;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pubapp.uif2app.event.card.CardBodyAfterEditEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m4n.entity.TransformBodyVO;

/**
 * 物料的编辑处理
 * 
 * @since 6.0
 * @version 2011-2-24 上午10:11:21
 * @author chennn
 */

public class CmaterialvidHandlerFor4N extends CmaterialvidHandler {

	@Override
	public Set<String> getNotClearItemKeys() {
		Set<String> notclearKeys = super.getNotClearItemKeys();
		notclearKeys.add(TransformBodyVO.FBILLROWFLAG);
		return notclearKeys;
	}

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
		copyOtherRowCmaterialvid(event);
	}

	private void copyOtherRowCmaterialvid(CardBodyAfterEditEvent event) {
		int count = getEditorModel().getICBizView().getBillCardPanel()
				.getRowCount();
		if (count > 1) {
			String cmaterialvid = (String) event.getValue();
			for (int i = 1; i < count; i++) {
				getEditorModel().getICBizView().getBillCardPanel()
						.getBillModel()
						.setValueAt(null, i, TransformBodyVO.CMATERIALVID);
				getEditorModel()
						.getICBizView()
						.getBillCardPanel()
						.getBillModel()
						.setValueAt(cmaterialvid, i,
								TransformBodyVO.CMATERIALVID);
				BillEditEvent billEditEnent = new BillEditEvent(
						getEditorModel().getICBizView().getBillCardPanel(),
						null, TransformBodyVO.CMATERIALVID, i);
				CardBodyAfterEditEvent cardEditEvent = new CardBodyAfterEditEvent(
						getEditorModel().getICBizView().getBillCardPanel(),
						billEditEnent, null);
				getEditorModel().getICBizView().getModel()
						.fireEvent(cardEditEvent);
			}
		}
	}

}

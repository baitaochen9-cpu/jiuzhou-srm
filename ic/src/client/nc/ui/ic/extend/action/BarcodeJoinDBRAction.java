package nc.ui.ic.extend.action;

import java.awt.event.ActionEvent;

import nc.ui.ic.general.model.ICGenBizEditorModel;
import nc.ui.ic.general.model.ICGenBizModel;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.pub.BusinessException;

/**
 * 调拨入条码联查
 * @author tts20
 *
 */
public class BarcodeJoinDBRAction extends NCAction{
	
	private ICGenBizModel model;
	private ICGenBizEditorModel editorModel;
	public BarcodeJoinDBRAction() {
		super();
		setBtnName("条码联查");
		setCode("barcodeJoinAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		BillCardPanel carcpanel = getEditorModel().getCardPanelWrapper().getBillCardPanel();
		int [] selectrow=carcpanel.getBillTable().getSelectedRows();
		if(selectrow.length > 1 || selectrow.length == 0){
			throw new BusinessException("请选中一行数据");
		}

		Object pk_item =  carcpanel.getBillModel().getValueAt(selectrow[0], "cgeneralbid");
		LinkAtpQueryDlg dlg = new LinkAtpQueryDlg(getModel().getContext().getEntranceUI(), (String) pk_item,getModel().getContext());
        dlg.show();
	}
	@Override
	protected boolean isActionEnable() {
		try {
			BillCardPanel cardPanel = getEditorModel().getCardPanelWrapper().getBillCardPanel();
			int [] selectrow=cardPanel.getBillTable().getSelectedRows();
			if(selectrow.length > 1 || selectrow.length == 0){
				return false;
			}

			Object pk_item = cardPanel.getBillModel().getValueAt(selectrow[0], "cgeneralbid");
			Object bar = HYPubBO_Client.findColValue("qilibc_barcodeflow", "vbarcode", "pk_item='"+pk_item+"'");
			if(bar == null){
				return false;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model.getUiState() == UIState.NOT_EDIT && model.getSelectedData() != null;
	}
	public ICGenBizModel getModel() {
		return model;
	}
	public void setModel(ICGenBizModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}
	public ICGenBizEditorModel getEditorModel() {
		return editorModel;
	}
	public void setEditorModel(ICGenBizEditorModel editorModel) {
		this.editorModel = editorModel;
	}
}

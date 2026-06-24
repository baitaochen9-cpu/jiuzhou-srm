package nc.ui.ic.extend.action;

import java.awt.event.ActionEvent;

import nc.ui.ic.general.model.ICGenBizModel;
import nc.ui.ic.m4y.view.TransOutBizView;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.UIState;
import nc.vo.pub.BusinessException;

/**
 * 调拨出入库扩展联查按钮
 * @author tts20
 *
 */
public class BarcodeJoinDBAction extends NCAction{
	
	private ICGenBizModel model;
	private TransOutBizView editor;
	public BarcodeJoinDBAction() {
		super();
		setBtnName("条码联查");
		setCode("barcodeJoinAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		BillCardPanel carcpanel = editor.getBillCardPanel();
		
		int [] selectrow=carcpanel.getBillTable().getSelectedRows();
		if(selectrow.length > 1 || selectrow.length == 0){
			throw new BusinessException("请选中一行数据");
		}

		Object pk_item = carcpanel.getBillModel().getValueAt(selectrow[0], "cgeneralbid");
		LinkAtpQueryDlg dlg = new LinkAtpQueryDlg(getModel().getContext().getEntranceUI(), (String) pk_item,getModel().getContext());
        dlg.show();
	}
	@Override
	protected boolean isActionEnable() {
		try {
			BillCardPanel carcpanel = editor.getBillCardPanel();
			int [] selectrow=editor.getBillCardPanel().getBillTable().getSelectedRows();
			if(selectrow.length > 1 || selectrow.length == 0){
				return false;
			}

			Object pk_item = carcpanel.getBillModel().getValueAt(selectrow[0], "cgeneralbid");
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
	public TransOutBizView getEditor() {
		return editor;
	}
	public void setEditor(TransOutBizView editor) {
		this.editor = editor;
	}
}

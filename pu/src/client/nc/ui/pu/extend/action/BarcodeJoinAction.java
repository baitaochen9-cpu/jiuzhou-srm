package nc.ui.pu.extend.action;

import java.awt.event.ActionEvent;

import nc.ui.ic.extend.action.LinkAtpQueryDlg;
import nc.ui.pu.pub.view.PUBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.vo.ic.general.define.ICBillBodyVO;
import nc.vo.pub.BusinessException;

public class BarcodeJoinAction extends NCAction{
	
	private PUBillForm editorModel;
	private BillManageModel model;
	public BarcodeJoinAction() {
		super();
		setBtnName("条码联查");
		setCode("barcodeJoinAction");
	}
	@Override
	public void doAction(ActionEvent e) throws Exception {
		BillCardPanel cardPanel = editorModel.getBillCardPanel();
		int [] selectrow = cardPanel.getBillTable().getSelectedRows();
		if(selectrow.length > 1 || selectrow.length == 0){
			throw new BusinessException("请选中一行数据");
		}
		ICBillBodyVO selectbodyvo = (ICBillBodyVO) this.getEditorModel().getBillCardPanel().getBillModel()
                .getBodyValueRowVO(selectrow[0], ICBillBodyVO.class.getName());
		Object pk_item = selectbodyvo.getPrimaryKey();
		LinkAtpQueryDlg dlg = new LinkAtpQueryDlg(getModel().getContext().getEntranceUI(), (String) pk_item,getModel().getContext());
        dlg.show();
	}
	@Override
	protected boolean isActionEnable() {
		try {
			int [] selectrow=editorModel.getBillCardPanel().getBillTable().getSelectedRows();
			if(selectrow.length > 1 || selectrow.length == 0){
				return false;
			}
			ICBillBodyVO selectbodyvo = (ICBillBodyVO) this.getEditorModel().getBillCardPanel().getBillModel()
	                .getBodyValueRowVO(selectrow[0], ICBillBodyVO.class.getName());
			Object pk_item = selectbodyvo.getPrimaryKey();
			Object bar = HYPubBO_Client.findColValue("qilibc_barcodeflow", "vbarcode", "pk_item='"+pk_item+"'");
			if(bar == null){
				return false;
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public BillManageModel getModel() {
		return model;
	}
	public void setModel(BillManageModel model) {
		this.model = model;
//		this.model.addAppEventListener(this);
	}
	public PUBillForm getEditorModel() {
		return editorModel;
	}
	public void setEditorModel(PUBillForm editorModel) {
		this.editorModel = editorModel;
	}
}

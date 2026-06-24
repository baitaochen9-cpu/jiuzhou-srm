package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.qc.supplierqualitystatus.view.ShowHistoryDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pu.supqualistatus.SupplierqualityHistoryVO;

public class LoadHistoryAction extends NCAction {
	private static final long serialVersionUID = 1L;
	private ShowHistoryDialog dlg = null;
	private AbstractAppModel model;
	private BodyInfoEditor editor = null;

	public LoadHistoryAction() {
		String str = "变更历史查询";
		this.setBtnName(str);
		this.setCode("LOADHIS");
	}

	private ShowHistoryDialog getDlg() {
		if (dlg == null) {
			dlg = new ShowHistoryDialog(getModel().getContext().getEntranceUI(),"data");
		}
		return dlg;
	}

	public void doAction(ActionEvent e) throws Exception {

		StringBuffer strb = new StringBuffer();

		Object o = getEditor().getModel().getSelectedData();
		if (o != null) {
			SupplierqualityHVO hvo = (SupplierqualityHVO) o;
			if (hvo != null) {
				strb.append(" isnull(dr,0) = 0 and pk_supplierquality = '"
						+ hvo.getPk_supplierquality() + "'");
			}
		} else {
			SupStockVO hvo = (SupStockVO) getModel().getSelectedData();
			if (hvo != null) {
				strb.append(" isnull(dr,0) = 0 and pk_supplier = '"
						+ hvo.getPk_supplier() + "'");
			}
		}

		if (strb.length() == 0) {
			ShowStatusBarMsgUtil.showStatusBarMsg("请先选中数据！", getModel()
					.getContext());
		} else {
			SupplierqualityHistoryVO[] bodyvos = (SupplierqualityHistoryVO[]) HYPubBO_Client
					.queryByCondition(SupplierqualityHistoryVO.class,
							strb.toString());
			getDlg().loadData(bodyvos);
			getDlg().showModal();
		}
	}

	protected boolean isActionEnable() {
		if (getEditor().getModel().getRows().isEmpty()) {
			Object obj = getModel().getSelectedData();
			if (obj == null)
				return false;
		}
		return model.getUiState() == UIState.NOT_EDIT;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public BodyInfoEditor getEditor() {
		return editor;
	}

	public void setEditor(BodyInfoEditor editor) {
		this.editor = editor;
	}

}

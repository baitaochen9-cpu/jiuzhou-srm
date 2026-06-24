package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ElectronicsignatureHisDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.pub.BusinessException;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureHisAction extends NCAction {
	private AbstractAppModel model;
	private BodyInfoEditor editor = null;
	//nc/ui/pubapp/plugin/action/psndoc_org_extends.xml
		//PluginBeanConfigFilePath_1
	public ElectronicsignatureHisAction() {
		setCode("ElectronicsignatureHIS");
		setBtnName("电子签名查询");
	}


	protected boolean isActionEnable() {
		return model.getUiState() == UIState.NOT_EDIT;
	}

	public void doAction(ActionEvent e) throws Exception {
		openOrgBrowseDialog();
	}

	private void openOrgBrowseDialog() throws BusinessException {
		ToftPanelAdaptor adaptor = (ToftPanelAdaptor) model.getContext()
				.getEntranceUI();
		model.getContext().getPk_loginUser();
		BillCardPanel card = this.editor.getBillCardPanel();
		ElectronicsignatureHisDialog dialog = new ElectronicsignatureHisDialog(card,model);
		Object  o = getModel().getSelectedData();
		String billid = null;
		if(o != null){
			if(o instanceof  SupStockVO){
				SupStockVO vo1 = (SupStockVO)o;
				billid  = vo1.getPk_supplier();
			}
		}
		String strWhere = " nvl(dr,0) = 0 and billid = '"+billid+"' order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		dialog.initData(vos);
		int showModal = dialog.showModal();
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
package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ElectronicsignatureDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.pub.BusinessException;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureAction extends NCAction {
	private AbstractAppModel model;
	private BodyInfoEditor editor = null;
	//nc/ui/pubapp/plugin/action/release_action_extends.xml
		//PluginBeanConfigFilePath_1
	public ElectronicsignatureAction() {
		setCode("Electronicsignature");
		setBtnName("µç×ÓÇ©ÃûÒ³");
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
		ElectronicsignatureDialog dialog = new ElectronicsignatureDialog(card,model);
		Object  o = getModel().getSelectedData();
		String billid = null;
		if(o != null){
			if(o instanceof  SupStockVO){
				SupStockVO vo1 = (SupStockVO)o;
				billid  = vo1.getPk_supplier();
			}
		}
		String strWhere = " nvl(dr,0) = 0 and billid = '"+billid+"' and	billmaker is not null order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		ElectronicsignatureHisVO vo = null;
		if(vos != null && vos.length>0){
			vo = vos[0];
		}
		ElectronicsignatureHisVO vo1 = null;
		String strWhere1 = " nvl(dr,0) = 0 and billid = '"+billid+"' and approver is not null order by ts desc" ;
		ElectronicsignatureHisVO[] vos1 = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere1);
		if(vos1 != null && vos1.length>0){
			vo1 = vos1[0];
		}
		dialog.initData(vo,vo1);
		int showModal = dialog.showModal();
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

	public AbstractAppModel getModel() {
		return model;
	}
	
	

}
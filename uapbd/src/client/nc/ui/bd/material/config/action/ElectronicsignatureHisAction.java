package nc.ui.bd.material.config.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureHisAction extends NCAction {
	private AbstractAppModel model;
	private String funnode;
	private BillForm editor;
	//nc/ui/pubapp/plugin/action/psndoc_org_extends.xml
		//PluginBeanConfigFilePath_1
	public ElectronicsignatureHisAction() {
		setCode("Electronicsignature");
		setBtnName("电子签名查询");
		putValue("ShortDescription", "电子签名查询");
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
		Object  o = model.getSelectedData();
		String billid = "";
		String str = "";
		if(o != null){
			if(o instanceof MaterialVO){
				MaterialVO aggvo = (MaterialVO)o;
				billid = aggvo.getPrimaryKey();
				 str = " billid = '"+billid+"'";
			}else if(o instanceof  SuperVO){
				SuperVO vo1 = (SuperVO)o;
				billid  = vo1.getPrimaryKey();
				 str = " def1 = '"+billid+"'";
			}
		}
		String strWhere = " nvl(dr,0) = 0 and  "+str+" order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		dialog.initData(vos);
		int showModal = dialog.showModal();
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public void setFunnode(String funnode) {
		this.funnode = funnode;
	}

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}
	
	

}
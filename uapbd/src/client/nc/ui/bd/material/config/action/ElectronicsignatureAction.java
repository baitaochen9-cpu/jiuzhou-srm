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
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureAction extends NCAction {
	private AbstractAppModel model;
	private String funnode;
	private BillForm editor;
	//nc/ui/pubapp/plugin/action/release_action_extends.xml
		//PluginBeanConfigFilePath_1
	public ElectronicsignatureAction() {
		setCode("Electronicsignature");
		setBtnName("电子签名页");
		putValue("ShortDescription", "电子签名页");
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
		String strWhere = " nvl(dr,0) = 0  and  "+str+" and	billmaker is not null order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		ElectronicsignatureHisVO vo = null;
		if(vos != null && vos.length>0){
			vo = vos[0];
		}
		ElectronicsignatureHisVO vo1 = null;
		String strWhere1 = " nvl(dr,0) = 0 and def1 = '"+billid+"' and approver is not null order by ts desc" ;
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
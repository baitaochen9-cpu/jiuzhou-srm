package nc.ui.ic.m4460;

import java.awt.event.ActionEvent;

import nc.ui.ic.m4460.model.StateAdjustModel;
import nc.ui.ic.m4460.view.StateAdjustBillListView;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureAction extends NCAction {
	private StateAdjustModel model;
	private StateAdjustBillListView editor;
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
		BillListPanel card = this.editor.getBillListPanel();
		ElectronicsignatureDialog dialog = new ElectronicsignatureDialog(card,null);
		Object  o = model.getSelectedData();
		String billid = "";
		if(o != null){
			if(o instanceof AggregatedValueObject){
				AggregatedValueObject aggvo = (AggregatedValueObject)o;
				SuperVO vo1 = (SuperVO)o;
				billid  = (String)aggvo.getParentVO().getAttributeValue("pk_onhanddim_adj");
				if(StringUtil.isEmpty(billid)){
					billid  = (String)aggvo.getParentVO().getAttributeValue("pk_onhanddim");
				}
			}else if(o instanceof  SuperVO){
				SuperVO vo1 = (SuperVO)o;
				billid  = (String)vo1.getAttributeValue("pk_onhanddim_adj");
				if(StringUtil.isEmpty(billid)){
					billid  = (String)vo1.getAttributeValue("pk_onhanddim");
				}
			}
		}
//		billid ="库存状态调整";
		String strWhere = " nvl(dr,0) = 0 and billid = '"+billid+"' and  billmaker is not null order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
//		vos[0].setBillmaker(vos[0].getApprover());
//		vos[0].setModifiedtime(vos[0].getApprovedate());
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

	public void setModel(StateAdjustModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public StateAdjustBillListView getEditor() {
		return editor;
	}

	public void setEditor(StateAdjustBillListView editor) {
		this.editor = editor;
	}
	
	

}
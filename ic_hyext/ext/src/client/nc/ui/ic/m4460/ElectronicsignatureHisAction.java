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

public class ElectronicsignatureHisAction extends NCAction {
	private StateAdjustModel model;
	private StateAdjustBillListView editor;
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
		BillListPanel card = this.editor.getBillListPanel();
		ElectronicsignatureHisDialog dialog = new ElectronicsignatureHisDialog(card,null);
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
		String strWhere = " nvl(dr,0) = 0 and billid = '"+billid+"' order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		dialog.initData(vos);
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
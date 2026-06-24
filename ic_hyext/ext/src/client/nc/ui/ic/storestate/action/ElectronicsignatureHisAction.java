package nc.ui.ic.storestate.action;

import java.awt.event.ActionEvent;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ElectronicsignatureHisDialog;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.UIState;
import nc.ui.uif2.editor.BatchBillTable;
import nc.ui.uif2.model.AbstractBatchAppModel;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO;

public class ElectronicsignatureHisAction extends NCAction {
	private AbstractBatchAppModel model;
	private BatchBillTable editor;
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
		ElectronicsignatureHisDialog dialog = new ElectronicsignatureHisDialog(card,null);
		Object  o = model.getSelectedData();
		String billid = "";
		if(o != null){
			if(o instanceof AggregatedValueObject){
				AggregatedValueObject aggvo = (AggregatedValueObject)o;
				billid = aggvo.getParentVO().getPrimaryKey();
			}else if(o instanceof  SuperVO){
				SuperVO vo1 = (SuperVO)o;
				billid  = vo1.getPrimaryKey();
			}
		}
		String strWhere = " nvl(dr,0) = 0 and billid = '"+billid+"' order by ts desc" ;
		ElectronicsignatureHisVO[] vos = (ElectronicsignatureHisVO[])HYPubBO_Client.queryByCondition(ElectronicsignatureHisVO.class, strWhere);
		dialog.initData(vos);
		int showModal = dialog.showModal();
	}

	public void setModel(AbstractBatchAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public BatchBillTable getEditor() {
		return editor;
	}

	public void setEditor(BatchBillTable editor) {
		this.editor = editor;
	}
	
	

}
package nc.ui.qc.supplierqualitystatus.action;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import nc.bs.framework.common.NCLocator;
import nc.bs.uif2.IActionCode;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.ui.qc.supplierqualitystatus.ace.dialog.QualityMarketDialog;
import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ToftPanelAdaptor;
import nc.ui.uif2.actions.ActionInitializer;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang3.StringUtils;

public class EditQualityMarketAction extends NCAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2525524774774532387L;
	private AbstractAppModel model;
	private BodyInfoEditor editor = null;
	
	public EditQualityMarketAction() {
		setCode("EditQualityMarket");
		setBtnName("选择质量市场");
//		putValue("EditQualityMarket", "选择质量市场");
		this.setCode("EditQualityMarket"); 
		this.putValue(Action.SMALL_ICON, getIcon("themeres/ui/toolbaricons/paste2foot.png"));
		ActionInitializer.initializeAction(this, IActionCode.PASTELINETOTAIL);
		putValue("ShortDescription", "选择质量市场");
	}
	
	private Icon getIcon(String icon_url)
	{
		Icon icon= null;
		if(icon==null && !StringUtil.isEmptyWithTrim(icon_url))
		{
			icon = ThemeResourceCenter.getInstance().getImage(icon_url);
		}
		return icon;
	}

	@Override
	protected boolean isActionEnable() {
//		return this.editor.getModel().getUiState() == UIState.EDIT;
		return Boolean.TRUE.booleanValue();
	}

	@Override
	public void doAction(ActionEvent e) throws Exception {

		Object o = getEditor().getModel().getSelectedData();
		if (o != null) {
			SupplierqualityHVO hvo = (SupplierqualityHVO) o;
			String pk_org = hvo.getPk_org();
			openQualityMarketDialog(pk_org);
		}
	}

	private void openQualityMarketDialog(String pk_org) throws Exception {
		Object  o = getModel().getSelectedData();
		ToftPanelAdaptor adaptor = (ToftPanelAdaptor) model.getContext()
				.getEntranceUI();
		model.getContext().getPk_loginUser();
		BillCardPanel card = this.editor.getBillCardPanel();
		QualityMarketDialog qualityMarketDialog = new QualityMarketDialog(pk_org);
		int showModal = qualityMarketDialog.showModal();
		if(showModal==1){
			String[] refNames = qualityMarketDialog.getQualityMarketRefPane().getRefNames();
			if (refNames == null || refNames.length == 0) {
				ExceptionUtils.wrappBusinessException("质量市场不能为空!");
			}
			String joinStr = StringUtils.join(refNames,"&");
			//设置名称
			int[] selectedRows = card.getBillTable().getSelectedRows();
			BillModel billModel = card.getBillModel();
			SupplierqualityHVO[] bodyValueVOs = (SupplierqualityHVO[]) billModel.getBodyValueVOs("nc.vo.pu.supqualistatus.SupplierqualityHVO");
			for (int billItemRow : selectedRows) {
				SupplierqualityHVO supplierqualityHVO = bodyValueVOs[billItemRow];
				supplierqualityHVO.setDef1(joinStr);
				if (StringUtils.isNotBlank(supplierqualityHVO.getPrimaryKey())) {
					boolean ischeck = checkElectronicSignature(this.getModel().getContext(), "保存",this.getModel());
					if (!ischeck)
						return;
					IVOPersistence voPersistence = NCLocator.getInstance().lookup(IVOPersistence.class);
					supplierqualityHVO.setStatus(VOStatus.UPDATED);
					voPersistence.updateVO(supplierqualityHVO);
					IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
					supplierqualityHVO = (SupplierqualityHVO) queryBS.retrieveByPK(SupplierqualityHVO.class, supplierqualityHVO.getPrimaryKey());
				}
				
				billModel.setValueAt(joinStr, billItemRow, "def1");
				card.setBodyValueAt(joinStr, billItemRow, "def1");
				
				
				
				//设置表体VO 重新渲染参照名称
				card.getBillModel().setBodyRowVO(supplierqualityHVO, billItemRow);
				card.getBillModel().loadLoadRelationItemValue(billItemRow);
				
				
//				if (supplierqualityHVO.getPrimaryKey() != null) {
//					billModel.setRowState(billItemRow, BillModel.ADD);
//				}
			}
			RefreshSingleAction refreshSingleAction = new RefreshSingleAction();
			refreshSingleAction.setModel(this.getModel());
			ActionEvent actionEvent = new ActionEvent(refreshSingleAction, 1001, "刷新");
			refreshSingleAction.doAction(actionEvent);
		}
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
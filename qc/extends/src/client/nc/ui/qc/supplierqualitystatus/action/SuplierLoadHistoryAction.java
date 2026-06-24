package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.query2.DefaultQueryConditionDLG;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction;
import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.qc.supplierqualitystatus.view.ShowHistoryDialog;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.pu.supqualistatus.SupplierqualityHistoryVO;
import nc.vo.querytemplate.TemplateInfo;

public class SuplierLoadHistoryAction extends DefaultQueryAction {
	private static final long serialVersionUID = 1L;
	private ShowHistoryDialog dlg = null;
	private AbstractUIAppModel model;
	private BodyInfoEditor editor = null;

	public SuplierLoadHistoryAction() {
		String str = "生产厂商查询";
		this.setBtnName(str);
		this.setCode("SUPHIS"); 
	}
	private ShowHistoryDialog getDlg() {
		if (dlg == null) {
			dlg = new ShowHistoryDialog(getModel().getContext().getEntranceUI(),getNodeKey());
		}
		return dlg;
	}

	public void doAction(ActionEvent e) throws Exception {


		if (getQryDLGDelegator().showModal() == 1) {
			processQuery();
		}

	}

	private void processQuery() throws UifException {
		IQueryScheme queryScheme = getQryDLGDelegator().getQueryScheme();
		StringBuffer strb = new StringBuffer();

		strb.append(queryScheme.getWhereSQLOnly());
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
	
	public QueryConditionDLGDelegator getQryDLGDelegator() {
		if (this.iQueryDlg != null) {
			DefaultQueryConditionDLG qcd = (DefaultQueryConditionDLG) this.iQueryDlg
					.createQCDByIQCD(this.iQueryDlg);

			qcd.getQryCondEditor().getQueryContext()
					.setReloadQuickAreaValue(isReloadQuickAreaValue());

			this.qryCondDLGDelegator = createQryDLGDelegator(qcd);
			return this.qryCondDLGDelegator;
		}

		if (this.qryCondDLGDelegator == null) {
			TemplateInfo tempinfo = getTemplateInfo();

			this.qryCondDLGDelegator = createQryDLGDelegator(tempinfo);
			this.qryCondDLGDelegator.getQce().getQueryContext()
					.setReloadQuickAreaValue(isReloadQuickAreaValue());
		}

		return this.qryCondDLGDelegator;
	}

	private TemplateInfo getTemplateInfo() {
		TemplateInfo tempinfo = new TemplateInfo();
		tempinfo.setPk_Org(getModel().getContext().getPk_group());
		tempinfo.setFunNode(getFunNode());
		tempinfo.setUserid(getModel().getContext().getPk_loginUser());
		tempinfo.setNodekey(getNodeKey());
		tempinfo.setSealedDataShow(true);
		tempinfo.setTemplateId("1001V010000000089ZMU");
		return tempinfo;
	}
	protected boolean isActionEnable() {
		if(model == null)
			return true;
		return model.getUiState() == UIState.NOT_EDIT;
	}

	public AbstractUIAppModel getModel() {
		return model;
	}

	public void setModel(AbstractUIAppModel model) {
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

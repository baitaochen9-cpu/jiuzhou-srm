package nc.ui.qc.supplierqualitystatus.action;

import java.awt.event.ActionEvent;

import nc.itf.uap.IUAPQueryBS;
import nc.ui.pubapp.uif2app.query2.DefaultQueryConditionDLG;
import nc.ui.pubapp.uif2app.query2.QueryConditionDLGDelegator;
import nc.ui.pubapp.uif2app.query2.action.DefaultQueryAction;
import nc.ui.qc.supplierqualitystatus.view.BodyInfoEditor;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.uif.pub.exception.UifException;
import nc.vo.querytemplate.TemplateInfo;

public class LoadDataAction extends DefaultQueryAction {
	private static final long serialVersionUID = 1L;
	private AbstractUIAppModel model;
	private BodyInfoEditor editor = null;
	private IUAPQueryBS service;

	public LoadDataAction() {
		String str = "≤È—Ø";
		this.setBtnName(str);
		this.setCode("INVQUERY");
	}

	// delete from pub_query_templet t where t.node_code='C01055';
	//
	//
	// delete from pub_query_condition n where n.pk_templet in (select id from
	// pub_query_templet t where t.node_code='C01055');
	//
	// delete from pub_billtemplet_b b where b.pk_billtemplet
	// ='1001ZZ1000000003X493';
	//
	// select * from pub_query_templet t where t.node_code='C01055';
	//
	//
	// select * from pub_query_condition n where n.pk_templet in (select id from
	// pub_query_templet t where t.node_code='C01055');
	//
	// select * from pub_billtemplet_b b where b.pk_billtemplet
	// ='1001ZZ1000000003X493';
	//select * from pub_systemplate_base  where upper(funnode) = upper('C01055') and tempstyle = 1
	public void doAction(ActionEvent e) throws Exception {

		if (getQryDLGDelegator().showModal() == 1) {
			processQuery();
		}

	}

	private void processQuery() throws UifException {
		IQueryScheme queryScheme = getQryDLGDelegator().getQueryScheme();
		getDataManager().initModelByQueryScheme(queryScheme);
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
//		tempinfo.setQSMode(QSMode.ADMIN);
		tempinfo.setTemplateId("1001V010000000089ZMP");
		return tempinfo;
	}

	protected boolean isActionEnable() {
		if (model == null)
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

	public IUAPQueryBS getService() {
		return service;
	}

	public void setService(IUAPQueryBS service) {
		this.service = service;
	}

}

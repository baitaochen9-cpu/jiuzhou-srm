package nc.ui.riasm.electronicsignature.action;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.mmgp.uif2.actions.MMGPAddAction;
import nc.ui.mmgp.uif2.view.MMGPTreeCardBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.OrgChangedEvent;
import nc.ui.uif2.AppEvent;
import nc.ui.uif2.editor.IEditor;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;

public class TreeCardAddAction extends MMGPAddAction {
	private String pk_org;
	private IEditor editor;

	protected boolean isActionEnable() {
		return (super.isActionEnable()) && (MMStringUtil.isNotEmpty(pk_org));
	}

	public void handleEvent(AppEvent event) {
		super.handleEvent(event);
		if ("Model_Initialized".equals(event.getType())) {
			pk_org = getModel().getContext().getPk_org();
			updateStatus();
		}
		if (OrgChangedEvent.class.getName().equals(event.getType())) {
			OrgChangedEvent orgChangedEvent = (OrgChangedEvent) event;
			pk_org = orgChangedEvent.getNewPkOrg();
			updateStatus();
		}
		setDefaultValue();
	}

	private void setDefaultValue() {
		String pk_group = getModel().getContext().getPk_group();
		String pk_org = getModel().getContext().getPk_org();
		BillCardPanel panel = ((MMGPTreeCardBillForm) getEditor())
				.getBillCardPanel();
		// 设置主组织默认值
		panel.setHeadItem("pk_group", pk_group);
		panel.setHeadItem("pk_org", pk_org);
		panel.setHeadItem("pkorg", pk_org);
		// 设置单据状态、单据业务日期默认值
		panel.setHeadItem("approvestatus", BillStatusEnum.FREE.value());
		panel.setHeadItem("billdate", new UFDate(System.currentTimeMillis()));
		panel.setHeadItem("billtype", "22GN");
		panel.setHeadItem("transtype", "22GN");
		panel.setHeadItem("transtypepk",
				PfServiceScmUtil.getTrantypeidByCode(new String[] { "22GN" })
						.get("22GN"));
		panel.setHeadItem("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setHeadItem("creator", getModel().getContext().getPk_loginUser());
		panel.setHeadItem("maketime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setHeadItem("billmaker", getModel().getContext()
				.getPk_loginUser());

	}

	public IEditor getEditor() {
		return editor;
	}

	public void setEditor(IEditor editor) {
		this.editor = editor;
	}
}

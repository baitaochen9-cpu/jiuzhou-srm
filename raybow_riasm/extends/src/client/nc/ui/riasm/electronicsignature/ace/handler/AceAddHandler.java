package nc.ui.riasm.electronicsignature.ace.handler;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.ui.uif2.model.AbstractAppModel;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	protected AbstractAppModel model = null;
	
	@Override
	public void handleAppEvent(AddEvent e) {
		String pk_group = e.getContext().getPk_group();
		String pk_org = e.getContext().getPk_org();
		BillCardPanel panel = e.getBillForm().getBillCardPanel();
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
		panel.setHeadItem("creator", e.getContext().getPk_loginUser());
		panel.setHeadItem("maketime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setHeadItem("billmaker", e.getContext().getPk_loginUser());
//		if( treeModel.getSelectedData() != null){
//			panel.setHeadItem("pk_parent",((SupplierclassVO)treeModel.getSelectedData()).getPk_supplierclass());
//		}
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
	}

	
}

package nc.ui.qc.supplierqualitystatus.ace.handler;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.supplier.supplierclass.SupplierclassVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

	private HierachicalDataAppModel treeModel = null;

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
		panel.setHeadItem("billtype", "C055");
		panel.setHeadItem("transtype", "C055");
		panel.setHeadItem("transtypepk",
				PfServiceScmUtil.getTrantypeidByCode(new String[] { "C055" })
						.get("C055"));
		panel.setTailItem("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("creator", e.getContext().getPk_loginUser());
		panel.setTailItem("maketime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("billmaker", e.getContext().getPk_loginUser());
		
		if( treeModel.getSelectedData() != null){
			panel.setHeadItem("pk_parent",((SupplierclassVO)treeModel.getSelectedData()).getPk_supplierclass());
		}
	}
	
	public HierachicalDataAppModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}
}

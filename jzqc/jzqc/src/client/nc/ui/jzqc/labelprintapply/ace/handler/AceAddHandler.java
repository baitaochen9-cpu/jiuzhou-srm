package nc.ui.jzqc.labelprintapply.ace.handler;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.BillStatusEnum;

public class AceAddHandler implements IAppEventHandler<AddEvent> {

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
		panel.setHeadItem("billtype", "JZ11");
		panel.setHeadItem("transtype", "JZ11");
		panel.setHeadItem("transtypepk",
				PfServiceScmUtil.getTrantypeidByCode(new String[] { "JZ11" })
						.get("JZ11"));
		panel.setHeadItem("proposer", e.getContext().getPk_loginUser());
		panel.setHeadItem("iprintcount", 0);
		panel.setTailItem("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("creator", e.getContext().getPk_loginUser());
		panel.setTailItem("maketime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("billmaker", e.getContext().getPk_loginUser());
	}
}

package nc.ui.jzqc.labelprint.ace.handler;

import java.util.Map;

import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.ToftPanelAdaptorEx;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.billform.AddEvent;
import nc.vo.pub.lang.UFBoolean;
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
		panel.setHeadItem("billtype", "JZ01");
		// panel.setHeadItem("srcbilltype", getSrcbilltype(e));

		Map<String, String> map = ((ToftPanelAdaptorEx) e.getContext()
				.getEntranceUI()).getFuncletContext().getFuncletModel()
				.getParamMap();
		if (map != null && map.size() > 0) {//
			panel.setHeadItem(
					"transtype",
					map.get("transtype") == null ? "JZ01" : map
							.get("transtype"));
			panel.setHeadItem(
					"transtypepk",
					map.get("pk_transtype") == null ? PfServiceScmUtil
							.getTrantypeidByCode(new String[] { "JZ01" }).get(
									"JZ01") : map.get("pk_transtype"));
		} else {
			panel.setHeadItem("transtype", "JZ01");
			panel.setHeadItem("transtypepk", PfServiceScmUtil
					.getTrantypeidByCode(new String[] { "JZ01" }).get("JZ01"));
		}

		panel.setHeadItem("iprintcount", 0);
		panel.setHeadItem("blabelstatus", UFBoolean.TRUE);
		panel.setHeadItem("bprintstatus", UFBoolean.TRUE);
		panel.setTailItem("creationtime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("creator", e.getContext().getPk_loginUser());
		panel.setTailItem("maketime",
				new UFDateTime(System.currentTimeMillis()));
		panel.setTailItem("billmaker", e.getContext().getPk_loginUser());
		
		
		panel.getHeadItem("pk_batchcode").setEnabled(true);
		panel.getHeadItem("vbatchcode").setEnabled(true);
		
	}

	private String getSrcbilltype(AddEvent e) {
		String funcode = e.getContext().getNodeCode();

		String type = "23";
		if ("H3010200402".equals(funcode)) {
			type = "55A2";
		} else if ("H3010200403".equals(funcode)) {
			type = "55A4";
		}
		return type;
	}
}

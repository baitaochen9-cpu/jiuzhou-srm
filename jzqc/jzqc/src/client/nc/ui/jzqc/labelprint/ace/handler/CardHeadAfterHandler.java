package nc.ui.jzqc.labelprint.ace.handler;

import java.util.Vector;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 表头编辑前事件
 * 
 * @author zhw
 * 
 */
public class CardHeadAfterHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {
	private BillForm billform;

	public BillForm getBillform() {
		return billform;
	}

	public void setBillform(BillForm billform) {
		this.billform = billform;
	}

	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if ("pk_material".equals(key)) {// 物料
			String[] fields = new String[] { "pk_material", "pk_org", "code",
					"name", "pk_group", "pk_measdoc", "pk_source" };
			MaterialVO vo = MaterialPubService.queryMaterialBaseInfoByPk(
					(String) e.getValue(), fields);
			keyValue.setHeadValue("cunitid", vo.getPk_measdoc());
			keyValue.setHeadValue("pk_srcmaterial", vo.getPk_source());
		} else if ("pk_batchcode".equals(key)) {//批次号
			String editvalue = (String) e.getValue();
			if (PubAppTool.isNull(editvalue)) {
				keyValue.setHeadValue("vbatchcode", null);
				keyValue.setHeadValue("pk_batchcode", null);
				return;
			}
			BillItem batchcodeitem = panel.getHeadItem("pk_batchcode");
			UIRefPane batchref = (UIRefPane) batchcodeitem.getComponent();
			Vector v11 = batchref.getRefModel().getSelectedData();
			if(v11  == null || v11.size()==0){
				keyValue.setHeadValue("vbatchcode", null);
				keyValue.setHeadValue("pk_batchcode", null);
				keyValue.setHeadValue("vvendbatchcode", null);
				keyValue.setHeadValue("dproducedate",  null);
				keyValue.setHeadValue("dinbounddate",  null);
			}else{
				Vector v =(Vector)v11.get(0);
				keyValue.setHeadValue("vbatchcode", v.get(0));
				keyValue.setHeadValue("bc_vvendbatchcode", v.get(1));
				keyValue.setHeadValue("dproducedate", v.get(2));
				keyValue.setHeadValue("enddate", v.get(3));
				keyValue.setHeadValue("pk_batchcode", v.get(4));
			}
		}
	}
}

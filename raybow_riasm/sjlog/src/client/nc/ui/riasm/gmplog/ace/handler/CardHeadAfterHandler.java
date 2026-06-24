package nc.ui.riasm.gmplog.ace.handler;

import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailAfterEditEvent;
import nc.ui.riasm.gmplog.ace.view.GspLogBillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.vo.so.pub.keyvalue.IKeyValue;

/**
 * 表头编辑后事件
 * 
 */
public class CardHeadAfterHandler implements
		IAppEventHandler<CardHeadTailAfterEditEvent> {


	private GspLogBillForm billForm;



	public GspLogBillForm getBillForm() {
		return billForm;
	}

	public void setBillForm(GspLogBillForm billForm) {
		this.billForm = billForm;
	}

	@SuppressWarnings("all")
	@Override
	public void handleAppEvent(CardHeadTailAfterEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		Object value = e.getValue();
		if ("pk_parent".equals(key)) {
			
			//批次档案没有关联元数据,代码设置一下
			if("40010815".equals(value)){
				//select * from MD_CLASS where DISPLAYNAME like '%批次档案%'
				keyValue.setHeadValue("billid", "84d728b3-ec15-43db-ac73-c8d83b7a6ece");
				
			}
			//调用一下，用来重构billcard
			getBillForm().getBillCardPanel();
		}
	}
}

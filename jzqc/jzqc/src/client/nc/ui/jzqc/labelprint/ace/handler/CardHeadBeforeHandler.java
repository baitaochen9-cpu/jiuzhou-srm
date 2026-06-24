package nc.ui.jzqc.labelprint.ace.handler;

import nc.ui.pu.pub.editor.ClientContext;
import nc.ui.pu.pub.util.VBatchCodeUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pubapp.uif2app.event.IAppEventHandler;
import nc.ui.pubapp.uif2app.event.card.CardHeadTailBeforeEditEvent;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.so.pub.keyvalue.CardKeyValue;
import nc.vo.ic.batchcode.BatchDlgParam;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.pu.batchcode.PUBatchDlgParam;
import nc.vo.pu.onhand.entity.OnhandDlgPUHeaderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.view.DataViewMeta;
import nc.vo.so.pub.keyvalue.IKeyValue;
import nc.vo.uif2.LoginContext;

/**
 * 表头编辑前事件
 * 
 * @author zhw
 * 
 */
public class CardHeadBeforeHandler implements
		IAppEventHandler<CardHeadTailBeforeEditEvent> {
	private BillForm billform;

	public BillForm getBillform() {
		return billform;
	}

	public void setBillform(BillForm billform) {
		this.billform = billform;
	}

	@Override
	public void handleAppEvent(CardHeadTailBeforeEditEvent e) {
		BillCardPanel panel = e.getBillCardPanel();

		e.setReturnValue(true);
		IKeyValue keyValue = new CardKeyValue(panel);
		String key = e.getKey();
		if ("pk_batchcode".equals(key)) {// 批次号
			String pk_material = (String) panel.getHeadItem("pk_material")
					.getValueObject();
			String pk_org = (String) panel.getHeadItem("pk_org")
					.getValueObject();

			if (!VBatchCodeUtil.canEdit(pk_material, pk_org)) {
				e.setReturnValue(Boolean.FALSE);
				return;
			}
			BillItem batchcodeitem = panel.getHeadItem(key);
			UIRefPane batchref = (UIRefPane) batchcodeitem.getComponent();
			batchref.getRefModel().setPk_org(pk_org);
			batchref.getRefModel().addWherePart(
					" and cmaterialoid ='" + pk_material + "' ");
		}
	}

	public BatchDlgParam getBatchDlgParam(CardHeadTailBeforeEditEvent event,
			IKeyValue keyValue)
	/*     */{
		/* 55 */BatchDlgParam param = new PUBatchDlgParam();
		/* 56 */OnhandDimVO dimvo = param.getOnhandDim();
		/* 57 */if (dimvo == null) {
			/* 58 */dimvo = new OnhandDimVO();
			/* 59 */param.setOnhandDim(dimvo);
			/*     */}
		/* 68 */LoginContext context = event.getContext();
		/* 69 */if ((context instanceof ClientContext)) {
			/* 70 */context = ((ClientContext) event.getContext())
					.convertLoginContext();
			/*     */}
		/* 72 */param.setLoginContext(context);
		OnhandDimVO dimVO = param.getOnhandDim();
		param.setAttributeValue("csendstockorgid", null);
		param.setAttributeValue("csendstordocid", null);
		param.setAttributeValue("pk_org", keyValue.getHeadStringValue("pk_org"));

		/* 79 */String materialoid = keyValue.getHeadStringValue("pk_material");

		/* 81 */param.setCmaterialoid(materialoid);
		/* 82 */dimVO.setCmaterialvid(keyValue
				.getHeadStringValue("pk_srcmaterial"));

		/* 88 */dimVO.setAttributeValue("pk_group", context.getPk_group());
		/* 89 */dimVO.setCffileid(keyValue.getHeadStringValue("cmffileid"));
		/* 86 */OnhandDlgPUHeaderVO headVO = new OnhandDlgPUHeaderVO();
		/* 87 */DataViewMeta dataViewMeta = new DataViewMeta(dimvo.getClass());
		/* 88 */headVO.setDataViewMeta(dataViewMeta);
		/* 89 */headVO.setVO(dimvo);
		/* 91 */headVO.setCrowno("10");

		/* 98 */headVO.setCunitid(keyValue.getHeadStringValue("cunitid"));

		/* 100 */UFDouble nnum = keyValue.getHeadUFDoubleValue("nnum");
		/*     */
		/* 99 */headVO.setOnhandshouldnum(nnum);
		/* 103 */headVO.setOnhandshouldassnum(nnum);
		/*     */
		/*     */
		/* 102 */param.setHeadVO(headVO);
		/* 103 */param.setIsNewBatchRef(UFBoolean.TRUE);
		/*     */
		/* 105 */return param;
		/*     */}
}

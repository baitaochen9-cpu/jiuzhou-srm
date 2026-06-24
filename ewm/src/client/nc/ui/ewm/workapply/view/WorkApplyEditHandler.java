/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */package nc.ui.ewm.workapply.view;
/*     */
/*     */import java.util.ArrayList;
/*     */
import java.util.List;
/*     */
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
/*     */
import nc.itf.am.pub.IFailurereasonService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
/*     */
import nc.ui.am.action.support.AMBatchAlterAction;
/*     */
import nc.ui.am.editor.AMBillForm;
/*     */
import nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler;
import nc.ui.am.editor.multiselect.MultiSelectUtils;
/*     */
import nc.ui.am.psninfo.PsnAndDeptInfo;
/*     */
import nc.ui.am.ref.util.RefMultiSelectedUtils;
/*     */
import nc.ui.am.ref.util.RefSingleSelectedUtils;
/*     */
import nc.ui.am.util.BillCardPanelUtils;
/*     */
import nc.ui.bd.ref.AbstractRefModel;
/*     */
import nc.ui.eampub.orgutil.MaintainOrgUtil;
/*     */
import nc.ui.pub.beans.UIRefPane;
/*     */
import nc.ui.pub.bill.BillCardPanel;
/*     */
import nc.ui.pub.bill.BillData;
/*     */
import nc.ui.pub.bill.BillEditEvent;
/*     */
import nc.ui.pub.bill.BillItem;
/*     */
import nc.ui.pub.bill.BillItemEvent;
/*     */
import nc.ui.pub.bill.BillModel;
/*     */
import nc.ui.uif2.ShowStatusBarMsgUtil;
/*     */
import nc.vo.am.common.util.CollectionUtils;
/*     */
import nc.vo.am.common.util.MultiLanguageUtil;
/*     */
import nc.vo.am.failurereason.FailurereasonVO;
/*     */
import nc.vo.am.proxy.AMProxy;
import nc.vo.eom.failure.FailureBodyVO;
import nc.vo.ewm.workapply.WorkApplyBodyVO;
/*     */
import nc.vo.ml.AbstractNCLangRes;
/*     */
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFBoolean;
/*     */
import org.apache.commons.lang.StringUtils;
/*     */
import org.granite.lang.util.Collections;
/*     */
import nc.vo.pub.BusinessException;
import nc.ui.pub.bill.IBillItem;
/*     */public class WorkApplyEditHandler
		extends
			WithRefInitCardEditEventHandler
/*     */{
	
	
	
	/*     */public void handleHeadAfterEditEvent(AMBillForm billForm,
			BillEditEvent e)
	/*     */{
		/*  46 */if (e.getKey().equalsIgnoreCase("pk_applyer"))
			/*  47 */afterEditApplyer(billForm, e);
		/*     */}
	/*     */
	/*     */private void afterEditApplyer(AMBillForm billForm, BillEditEvent e)
	/*     */{
		/*  52 */BillCardPanel card = billForm.getBillCardPanel();
		/*     */
		/*  54 */UIRefPane refPsnPane = (UIRefPane) card.getHeadItem(
				"pk_applyer").getComponent();
		/*     */
		/*  56 */String psnPk = refPsnPane.getRefModel().getPkValue();
		/*     */
		/*  58 */Object oOrgPk = card.getHeadItem("pk_org").getValueObject();
		/*  59 */String orgPk = null;
		/*  60 */if (oOrgPk != null) {
			/*  61 */orgPk = (String) oOrgPk;
			/*     */}
		/*  63 */String[] psnAndDeptPk = PsnAndDeptInfo.getPKOfDeptAndPsn(
				psnPk, orgPk);
		/*     */
		/*  66 */if ((psnAndDeptPk != null) && (psnAndDeptPk.length > 0)) {
			/*  67 */boolean isMatch = BillCardPanelUtils
					.isHeadItemMatchCurr_orgForDept(billForm,
							"pk_apply_dept_v", psnAndDeptPk[1]);
			/*  68 */if ((!(isMatch))
					||
					/*  70 */(card.getHeadItem("pk_apply_dept")
							.getValueObject() != null))
				/*     */return;
			/*  72 */card.getHeadItem("pk_apply_dept").setValue(
					psnAndDeptPk[1]);
			/*     */
			/*  74 */card.getBillData().loadEditHeadRelation("pk_apply_dept");
			/*     */}
		/*     */}
	/*     */
	/*     */public boolean handleHeadBeforeEditEvent(AMBillForm billForm,
			BillItemEvent e)
	/*     */throws Exception
	/*     */{
		/*  83 */super.handleHeadBeforeEditEvent(billForm, e);
		/*  84 */BillCardPanel card = billForm.getBillCardPanel();
		/*  85 */String pk_org = ((UIRefPane) card.getHeadItem("pk_org")
				.getComponent()).getRefPK();
		/*     */
		/*  87 */if (("pk_apply_dept".equalsIgnoreCase(e.getItem().getKey()))
				&&
				/*  88 */(pk_org != null)) {
			/*  89 */((UIRefPane) e.getItem().getComponent()).getRefModel()
					.setPk_org(pk_org);
			/*     */}
		/*     */
		/*  93 */if (("pk_applyer".equalsIgnoreCase(e.getItem().getKey())) &&
		/*  94 */(pk_org != null)) {
			/*  95 */((UIRefPane) e.getItem().getComponent()).getRefModel()
					.setPk_org(pk_org);
			/*     */}
		/*     */
		/*  98 */return true;
		/*     */}
	/*     */
	/*     */public void handleBodyAfterEditEvent(AMBillForm billForm,
			BillEditEvent e)
	/*     */throws Exception
	/*     */{
		/* 104 */if (e.getKey().equals("pk_location")) {
			/* 105 */afterEditBodyLocaction(billForm.getBillCardPanel(), e);
			/* 106 */RefMultiSelectedUtils
					.multiSelectedWithRowNum(billForm, e);
				afterEditBodyLocations(billForm, e);
				billForm.getBillCardPanel().getBodyPanel().getTableModel().getBillModelData();
			/*     */}
		/* 109 */else if (e.getKey().equals("pk_equip")) {
			/* 110 */afterEditEquip(billForm, e);
			/*     */}
		/* 113 */else if (e.getKey().equalsIgnoreCase("consign_flag")) {
			/* 114 */afterEditConsignFlag(billForm, e);
			/*     */}
		/* 117 */else if (e.getKey().equalsIgnoreCase("pk_failure_type")) {
			/* 118 */afterEditFailureType(billForm, e);
			/* 119 */} else if (e.getKey().equalsIgnoreCase(
				"failure_reason_name")) {
			/* 120 */afterEditFailureReason(billForm, e);
			/*     */}
				
			
		/*     */}
	/*     */
	
	/*********************************bbt 20250306**************************************************************************************************/
	/**
	 * 
	 * @param tableName ±íÃû
	 * @param columnName ×Ö¶ÎÃû
	 * @param whereColumnName
	 * @param whereValue
	 * @return
	 * @throws BusinessException
	 */
	private Object[] sqlExecute(String tableName,String columnName,String whereColumnName,String whereValue) {
		try {
			Object[] obj_result = null;
			IUAPQueryBS query = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			StringBuilder sql = new StringBuilder();
			sql.append("select "  + columnName + " from " + tableName + " where " + whereColumnName + " = '" + whereValue + "';");
			obj_result = (Object[])query.executeQuery(sql.toString(),new ArrayProcessor());
			return obj_result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void afterEditBodyLocations(AMBillForm billForm,BillEditEvent e){
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		Vector rows = (Vector)(billForm.getBillCardPanel().getBodyPanel().getTableModel().getBillModelData().toArray())[1];
		int row = e.getRow();
		int row_end = row + rows.size();
		
		for (; row < row_end ; row ++) {
			String pkLocation = (String) billCardPanel.getBodyValueAt(row, WorkApplyBodyVO.PK_LOCATION + IBillItem.ID_SUFFIX);
			if (pkLocation != null) {
				try {
					UFBoolean EWMG09 = nc.cmp.bill.util.SysInit.getParaBoolean(billForm.getPk_org(), "EWMG09");
					if(UFBoolean.TRUE.equals(EWMG09)){
						Object[] obj_result = sqlExecute("pam_equip","pk_equip", "pk_location", pkLocation);
						if (null != obj_result && obj_result.length >= 0) {
							List list_result = new ArrayList();
							for (Object o : obj_result) {
								list_result.add(o.toString());
							}
							BillCardPanelUtils.setBodyItemValue(billCardPanel, "pk_equip",list_result.get(0), row);
							billCardPanel.getBillModel().loadLoadRelationItemValue(row,"pk_equip");
							billCardPanel.getBillModel().loadEditRelationItemValue(row,WorkApplyBodyVO.PK_EQUIP);
						}
					}
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
		}
	}
	/********************************************************************************************************************/
	
	
	/*     */private void editFailureType2Reason(AMBillForm billForm,
			BillEditEvent e)
	/*     */throws Exception
	/*     */{
		/* 132 */if ((billForm == null) || (e == null)) {
			/* 133 */return;
			/*     */}
		/* 135 */BillItem bodyItem = billForm.getBillCardPanel().getBodyItem(
				"failure_reason_name");
		/* 136 */if (bodyItem == null)
			/* 137 */return;
		/* 138 */BillCardPanel card = billForm.getBillCardPanel();
		/* 139 */UIRefPane refFailTypePane = (UIRefPane) card.getBodyItem(
				"pk_failure_type").getComponent();
		/* 140 */AbstractRefModel model = refFailTypePane.getRefModel();
		/*     */
		/* 142 */if (model == null) {
			/* 143 */return;
			/*     */}
		/* 145 */String idKey = BillCardPanelUtils.getIDColumn(bodyItem);
		/*     */
		/* 147 */String[] keyValues = model.getPkValues();
		/*     */
		/* 149 */Vector selectedData = model.getSelectedData();
		/*     */
		/* 151 */BillModel billModel = billForm.getBillCardPanel()
				.getBillModel();
		/*     */
		/* 153 */int col = billModel.getBodyColByKey("failure_reason_name");
		/* 154 */int column = billModel.getBodyColByKey("pk_failure_reason");
		/* 155 */List rows = RefSingleSelectedUtils.setMultiValue(idKey,
				keyValues, selectedData, billForm, e, null);
		/*     */
		/* 158 */String aValueName = "";
		/* 159 */String aValueId = "";
		/* 160 */if (!(Collections.isEmpty(selectedData)))
		/*     */{
			/* 162 */Vector data = (Vector) selectedData.get(0);
			/*     */
			/* 168 */if ((data.size() == 8) && (null == data.get(7)))
			/*     */{
				/* 170 */aValueName = (String) data.get(1);
				/*     */
				/* 172 */aValueId = (String) data.get(5);
				/*     */}
			/*     */else {
				/* 175 */aValueId = (String) data.get(5);
				/*     */
				/* 177 */if (aValueId != null) {
					/* 178 */FailurereasonVO[] failurereasonVO = ((IFailurereasonService) AMProxy
							.lookup(IFailurereasonService.class))
							.queryFailureReasonVoByPks(new String[]{aValueId});
					/*     */
					/* 181 */String reasonName = MultiLanguageUtil
							.getMultiNameField("reason_name");
					/*     */
					/* 183 */if (null != failurereasonVO) {
						/* 184 */aValueName = (String) failurereasonVO[0]
								.getAttributeValue(reasonName);
						/*     */}
					/*     */}
				/*     */}
			/*     */}
		/*     */
		/* 190 */if (StringUtils.isNotEmpty(aValueId)) {
			/* 191 */billModel.setValueAt(aValueName,
					((Integer) rows.get(0)).intValue(), col);
			/* 192 */billModel.setValueAt(aValueId,
					((Integer) rows.get(0)).intValue(), column);
			/*     */} else {
			/* 194 */billModel.setValueAt(null,
					((Integer) rows.get(0)).intValue(), col);
			/* 195 */billModel.setValueAt(null,
					((Integer) rows.get(0)).intValue(), column);
			/*     */}
		/*     */
		/* 204 */int state = billModel.getRowState(((Integer) rows.get(0))
				.intValue());
		/* 205 */if (state == 1)
			/* 206 */billModel.setRowState(((Integer) rows.get(0)).intValue(),
					1);
		/*     */else
			/* 208 */billModel.setRowState(((Integer) rows.get(0)).intValue(),
					2);
		/*     */}
	/*     */
	/*     */private void afterEditFailureReason(AMBillForm billForm,
			BillEditEvent e)
	/*     */throws Exception
	/*     */{
		/* 223 */if ((billForm == null) || (e == null)) {
			/* 224 */return;
			/*     */}
		/* 226 */BillItem bodyItem = billForm.getBillCardPanel().getBodyItem(
				e.getKey());
		/* 227 */if (bodyItem == null) {
			/* 228 */return;
			/*     */}
		/* 230 */AbstractRefModel refModel = ((UIRefPane) bodyItem
				.getComponent()).getRefModel();
		/*     */
		/* 232 */if (refModel == null) {
			/* 233 */return;
			/*     */}
		/* 235 */String idKey = BillCardPanelUtils.getIDColumn(bodyItem);
		/*     */
		/* 237 */String[] keyValues = refModel.getPkValues();
		/*     */
		/* 239 */Vector selectedData = refModel.getSelectedData();
		/*     */
		/* 241 */BillModel billModel = billForm.getBillCardPanel()
				.getBillModel();
		/*     */
		/* 243 */int col = billModel.getBodyColByKey("failure_reason_name");
		/* 244 */int column = billModel.getBodyColByKey("pk_failure_reason");
		/* 245 */List rows = RefSingleSelectedUtils.setMultiValue(idKey,
				keyValues, selectedData, billForm, e, null);
		/*     */
		/* 248 */String aValueName = "";
		/* 249 */String aValueId = "";
		/* 250 */Vector limitData = new Vector();
		/* 251 */if (!(Collections.isEmpty(selectedData)))
		/*     */{
			/* 253 */for (int i = 0; (i < selectedData.size()) && (i < 8); ++i)
			/*     */{
				/* 255 */Vector s = (Vector) selectedData.get(i);
				/* 256 */limitData.add(s);
				/*     */
				/* 258 */aValueName = aValueName + s.get(1) + ",";
				/*     */
				/* 260 */aValueId = aValueId + s.get(8) + ",";
				/*     */}
			/* 262 */if (aValueName.endsWith(",")) {
				/* 263 */aValueName = aValueName.substring(0,
						aValueName.length() - 1);
				/* 264 */aValueId = aValueId.substring(0,
						aValueId.length() - 1);
				/*     */}
			/*     */}
		/*     */
		/* 268 */if ((CollectionUtils.isNotEmpty(selectedData)) &&
		/* 269 */(selectedData.size() > 8)) {
			/* 270 */ShowStatusBarMsgUtil.showStatusBarMsg(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("failure_0", "04540002-0044"),
					billForm.getContext());
			/* 271 */refModel.setSelectedData(limitData);
			/*     */}
		/*     */
		/* 274 */billModel.setValueAt(aValueName,
				((Integer) rows.get(0)).intValue(), col);
		/* 275 */billModel.setValueAt(aValueId,
				((Integer) rows.get(0)).intValue(), column);
		/*     */
		/* 282 */int state = billModel.getRowState(((Integer) rows.get(0))
				.intValue());
		/* 283 */if (state == 1)
			/* 284 */billModel.setRowState(((Integer) rows.get(0)).intValue(),
					1);
		/*     */else
			/* 286 */billModel.setRowState(((Integer) rows.get(0)).intValue(),
					2);
		/*     */}
	/*     */
	/*     */private void afterEditEquip(AMBillForm billForm, BillEditEvent e)
	/*     */throws Exception
	/*     */{
		/* 298 */billForm.getBillCardPanel().setBodyValueAt(null, e.getRow(),
				"pk_location");
		/* 299 */billForm.getBillCardPanel().getBillModel()
				.loadEditRelationItemValue(e.getRow(), "pk_location");
		/*     */
		/* 301 */List noCheckItems = new ArrayList();
		/* 302 */noCheckItems.add("rowno");
		/* 303 */noCheckItems.add("pk_maintainorg");
		/* 304 */noCheckItems.add("pk_maintainorg_v");
		/* 305 */noCheckItems.add("treat_type");
		/*     */
		/* 307 */List rowList = RefMultiSelectedUtils
				.multiSelectedWithNoCheckItems(billForm, e, noCheckItems);
		/*     */
		/* 313 */if ((rowList != null) && (rowList.size() > 1)) {
			/* 314 */int maxIndex = rowList.size() - 1;
			/* 315 */int maxRow = ((Integer) rowList.get(maxIndex)).intValue();
			/* 316 */rowList.add(Integer.valueOf(maxRow + 1));
			/*     */}
		/*     */
		/* 319 */MaintainOrgUtil.setDefaultMaintainOrgByAssetOrg(billForm, e,
				rowList);
		/*     */}
	/*     */
	/*     */private void afterEditConsignFlag(AMBillForm billForm,
			BillEditEvent e)
	/*     */{
		/* 346 */BillCardPanel card = billForm.getBillCardPanel();
		/* 347 */Boolean isConsign = (Boolean) card.getBodyValueAt(e.getRow(),
				"consign_flag");
		/*     */
		/* 349 */if ((isConsign == null) || (isConsign.booleanValue()))
			/*     */return;
		/* 351 */card.setBodyValueAt(null, e.getRow(), "consign_reason");
		/*     */}
	/*     */
	/*     */private void afterEditFailureType(AMBillForm billForm,
			BillEditEvent e)
	/*     */throws Exception
	/*     */{
		/* 363 */if (e.getSource() instanceof AMBatchAlterAction)
		/*     */{
			/* 365 */return;
			/*     */}
		/* 367 */BillCardPanel card = billForm.getBillCardPanel();
		/*     */
		/* 369 */UIRefPane refFailTypePane = (UIRefPane) card.getBodyItem(
				"pk_failure_type").getComponent();
		/*     */
		/* 371 */Object typeObject = refFailTypePane.getRefModel().getValue(
				"pk_failure_type");
		/*     */
		/* 373 */editFailureType2Reason(billForm, e);
		/*     */
		/* 375 */BillCardPanelUtils.setBodyItemValue(card, "pk_failure_type",
				typeObject, e.getRow());
		/* 376 */card.getBillModel().loadLoadRelationItemValue(e.getRow(),
				"pk_failure_type");
		/*     */}
	/*     */
	/*     */public boolean handleBodyBeforeEditEvent(AMBillForm billForm,
			BillEditEvent e) throws Exception
	/*     */{
		/* 381 */super.handleBodyBeforeEditEvent(billForm, e);
		/* 382 */BillCardPanel card = billForm.getBillCardPanel();
		/*     */
		/* 385 */if ("consign_reason".equals(e.getKey())) {
			/* 386 */Boolean consign_flag = (Boolean) BillCardPanelUtils
					.getBodyValue(card, "consign_flag", e.getRow());
			/* 387 */if ((consign_flag == null)
					|| (!(consign_flag.booleanValue()))) {
				/* 388 */return false;
				/*     */}
			/*     */}
		/*     */
		/* 392 */if ((e.getKey().equals("pk_equip"))
				|| (e.getKey().equals("pk_location")))
		/*     */{

			/* 394 */Object pk_srcBill = card.getBodyValueAt(e.getRow(),
					"src_pk_bill");
			/*     */
			/* 396 */if (pk_srcBill != null)
				/* 397 */return false;
					
			/*     */}
		/* 399 */else if (e.getKey().equals("failure_reason_name")) {
			/* 400 */UIRefPane refPane = (UIRefPane) billForm
					.getBillCardPanel().getBodyItem("failure_reason_name")
					.getComponent();
			/*     */
			/* 402 */refPane.setMultiSelectedEnabled(true);
			/* 403 */String pk_failure_reasons = (String) BillCardPanelUtils
					.getBodyValue(billForm.getBillCardPanel(),
							"pk_failure_reason", e.getRow());
			/*     */
			/* 405 */if (pk_failure_reasons != null) {
				/* 406 */String[] pks = pk_failure_reasons.split(",");
				/* 407 */Vector selectData = refPane.getRefModel()
						.getSelectedData();
				/* 408 */refPane.setPKs(pks);
				/*     */}
			/*     */}
		/* 411 */return true;
		/*     */}
	/*     */
}
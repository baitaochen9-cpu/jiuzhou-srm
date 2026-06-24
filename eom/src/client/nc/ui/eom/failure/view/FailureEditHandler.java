/*
 * @(#)FailureEditHandler.java V60
 *
 * Copyright 2010 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.ui.eom.failure.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.am.pub.IFailurereasonService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.ui.am.editor.AMBillForm;
import nc.ui.am.editor.event.card.WithRefInitCardEditEventHandler;
import nc.ui.am.editor.multiselect.DefaultMultiSelectCallBack;
import nc.ui.am.editor.multiselect.MultiSelectUtils;
import nc.ui.am.ref.util.RefSingleSelectedUtils;
import nc.ui.am.util.BillCardPanelUtils;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.eampub.orgutil.MaintainOrgUtil;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.MultiLanguageUtil;
import nc.vo.am.constant.BillTypeConst_4B;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.failurereason.FailurereasonVO;
import nc.vo.am.proxy.AMProxy;
import nc.vo.eom.failure.FailureBodyVO;
import nc.vo.eom.failure.FailureHeadVO;
import nc.vo.eom.failure.TreatTypeConst;
import nc.vo.ml.LanguageVO;
import nc.vo.ml.MultiLangContext;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.relation.IBusiRoleConst;

import org.apache.commons.lang.StringUtils;
import org.granite.lang.util.Collections;

/**
 * 故障记录事件处理类
 *
 * @author 段云涛
 * @version 6.0
 * @date 2010-4-7
 * modify lissc
 * 增加多故障原因处理
 */
public class FailureEditHandler extends WithRefInitCardEditEventHandler {


	@SuppressWarnings("restriction")
	@Override
	public boolean handleHeadBeforeEditEvent(AMBillForm billForm,BillItemEvent e) throws Exception {
		// 初始化表头参照
		initRef(billForm, e.getItem());
		// 获取主表上的主组织
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		String pk_org = BillCardPanelUtils.getHeadItemRefPk(billCardPanel,
				FailureHeadVO.PK_ORG);
		if (FailureHeadVO.PK_RECORDER.equals(e.getItem().getKey())) {
			BillCardPanelUtils.setOrgForPsnRef(e.getItem(), pk_org, IBusiRoleConst.ASSETORG);
		}
		return true;
	}

	@SuppressWarnings("restriction")
	@Override
	public void handleBodyAfterEditEvent(AMBillForm billForm, BillEditEvent e) throws Exception {
		super.handleBodyAfterEditEvent(billForm, e);

		if (FailureBodyVO.PK_LOCATION.equals(e.getKey())) {
			// 查看位置是否和缓存位置一致
			afterEditLoc(billForm, e);
		} else if (e.getKey().equals(FailureBodyVO.PK_EQUIP)) {
			// 设备编辑后事件
			afterEditEquip(billForm, e);
		} else if (e.getKey().equals(FailureBodyVO.PK_FAILURE_TYPE)) {
			// 故障类别编辑后事件
			afterEditFailureType(billForm, e);
		} else if (e.getKey().equals(FailureBodyVO.FAILURE_TIME) || e.getKey().equals(FailureBodyVO.RESTORE_TIME)) {
			// 校验开始时间是否小于结束时间
			afterEditFailureRestoreDate(billForm, e);
		}else if(e.getKey().equals(FailureBodyVO.FAILURE_REASON_NAME)){
			//多故障原因字段编辑后事件
			afterEditFailureReason(billForm, e);
		} else if (e.getKey().equals(FailureBodyVO.TREAT_TYPE)) {
			// 处理方式字段编辑后事件
			afterEditFailureTreatType(billForm, e);
		}


	}
	
	class  MultiSelectCallBack extends DefaultMultiSelectCallBack{

		@Override
		public void doAfterMultiSelect(AMBillForm billForm, BillEditEvent e,
				List<Integer> rows) {
			// TODO Auto-generated method stub
			super.doAfterMultiSelect(billForm, e, rows);

			// 多选增行处理时补充故障发生日期			
			setFailureTime(billForm, rows);
			
			// 加载位置关联项
			loadRelationItemValue(billForm, rows);
			
		}
	
		
		/*********************************bbt 20240719**************************************************************************************************/
		/**
		 * 
		 * @param tableName 表名
		 * @param columnName 字段名
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
		/***********************************************************************************************************************************/
		
		private void loadRelationItemValue(AMBillForm billForm, List<Integer> rows) {
			BillCardPanel billCardPanel = billForm.getBillCardPanel();
			for (int row : rows) {
				String pkLocation = (String) billCardPanel.getBodyValueAt(row, FailureBodyVO.PK_LOCATION + IBillItem.ID_SUFFIX);
				if (pkLocation != null) {
					billCardPanel.getBillModel().loadLoadRelationItemValue(row, FailureBodyVO.PK_LOCATION);
					
					/***************************bbt 20240722****************************************************/
					try {
						UFBoolean YXM001 = nc.cmp.bill.util.SysInit.getParaBoolean(billForm.getPk_org(), "YXM001");
						if(UFBoolean.TRUE.equals(YXM001)){
							Object[] obj_result = sqlExecute("pam_equip","pk_equip", "pk_location", pkLocation);
							if (null != obj_result && obj_result.length >= 0) {
								List list_result = new ArrayList();
								for (Object o : obj_result) {
									list_result.add(o.toString());
								}
								BillCardPanelUtils.setBodyItemValue(billCardPanel, "pk_equip",list_result.get(0), row);
								billCardPanel.getBillModel().loadLoadRelationItemValue(row,"pk_equip");
							}
						}
					} catch (BusinessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/**************************************************************************************/
				}
				
			}
			
			
		}

		private void setFailureTime(AMBillForm billForm, List<Integer> rows){
			BillCardPanel billCardPanel = billForm.getBillCardPanel();
			for (int row : rows){
				// 故障发生日期
				BillCardPanelUtils.setBodyItemValue(billCardPanel, FailureBodyVO.FAILURE_TIME,
						BizContext.getInstance().getBizDateTime(), row);
			}
			
		}
		
	}
	
	
	
	
	// 处理方式字段编辑后事件
	private void afterEditFailureTreatType(AMBillForm billForm, BillEditEvent e) {
		// 判断该故障记录是否由工单生成
		BillCardPanel billCardPanel = billForm.getBillCardPanel();
		String srcBillType = BillCardPanelUtils.getBodyValue(
				billCardPanel, FailureBodyVO.SRC_BILL_TYPE, e.getRow());
		boolean fromWorkOrder = (BillTypeConst_4B.WORKORDER).equals(srcBillType);
		
		// 判断是否列入维修计划
		Integer treatType = (Integer) e.getValue();
		boolean toMaintainPlan = (TreatTypeConst.trans_to_maintainplan == treatType);
		if (fromWorkOrder && toMaintainPlan) {
			String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0",
					"04540002-0051" /** 该故障记录是由工单生成，不允许再列入维修计划！ **/);
			billForm.showErrorMessage(errMsg);
			BillCardPanelUtils.setBodyItemValue(billForm.getBillCardPanel(), e.getKey(), TreatTypeConst.trans_to_workorder, e.getRow());
		}
		
	}

	/**
	 * 多故障原因字段编辑后事件
	 * @param billForm
	 * @param e
	 * @throws Exception
	 * @author lissc
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
	private void afterEditFailureReason(AMBillForm billForm, BillEditEvent e) throws Exception{
		// 传入变量为空，则直接返回
		if (billForm == null || e == null) {
			return ;
		}
		
		// 获取选择的Item
		BillItem reasonNameItem = billForm.getBillCardPanel().getBodyItem(e.getKey());
		if (reasonNameItem == null) {
			return;
		}
		
		// 获取参照
		AbstractRefModel failureRefModel = ((UIRefPane) reasonNameItem.getComponent()).getRefModel();
		// 若不存在参照，则直接返回
		if (failureRefModel == null) {
			return;
		}
		Vector selectedReasons = failureRefModel.getSelectedData();
		if (Collections.isEmpty(selectedReasons)) {
			return;
		}
		
		int selectedReasonCount = selectedReasons.size();		
		// 故障原因选择不能超过8个
		if(selectedReasonCount > 8){
			ShowStatusBarMsgUtil.showStatusBarMsg
			(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("failure_0","04540002-0044")
					/*@res "故障原因不能超过上限，最多8个"*/, billForm.getContext());			
		}
		
		// 遍历循环组装多故障原因和原因主键
		StringBuilder reasonNames = new StringBuilder();
		StringBuilder reasonIds = new StringBuilder();
		Vector permittedReasons = new Vector();	
		int permittedReasonCount = (selectedReasonCount > 8) ? 8 : selectedReasonCount;
		for (int i = 0; i < permittedReasonCount; i++) {
			Vector aReason = (Vector) selectedReasons.get(i);
			permittedReasons.add(aReason);
			//data中的第二个元素是故障原因名称，该数据从故障树中取
			reasonNames.append(aReason.get(1) + CommonKeyConst.FAILURE_NAME_DIVIDE);
			//data中的第九个元素是故障原因主键，该数据从故障树中取
			reasonIds.append(aReason.get(8) + CommonKeyConst.FAILURE_NAME_DIVIDE);
		}	
		reasonNames.replace(reasonNames.length() - 1, reasonNames.length(), "");
		reasonIds.replace(reasonIds.length() - 1, reasonIds.length(), "");
		failureRefModel.setSelectedData(permittedReasons);	
		
		BillModel billModel = billForm.getBillCardPanel().getBillModel();
		// 获取到需要修改的列
		int nameColumn = billModel.getBodyColByKey(FailureBodyVO.FAILURE_REASON_NAME);
		int idColumn = billModel.getBodyColByKey(FailureBodyVO.PK_FAILURE_REASON);
		// 获取Key在元数据中对应的
		String reasonNameColumnName = BillCardPanelUtils.getIDColumn(reasonNameItem);
		// 获取Key值
		String[] reasonIdArray = failureRefModel.getPkValues();
		List<Integer> rowIndexs = 
				RefSingleSelectedUtils.setMultiValue(reasonNameColumnName, reasonIdArray, selectedReasons, 
													 billForm, e, null);
		billModel.setValueAt(reasonNames.toString(), rowIndexs.get(0), nameColumn);
		billModel.setValueAt(reasonIds.toString(), rowIndexs.get(0), idColumn);
		/**
		 * 设定表体行状态，否则会因为流量优化器会过滤掉，不会修改
		 * TODO (此处暂时有问题，多故障原因修改后，表体行有时候变成修改态，
		 *   有时候不变,暂未找到规律，改成除了增加其余都是修改态，暂时能规避问题，该问题以后再改
		 *   此问题可能是故障原因档案没有关联所致)
		 */
		int state = billModel.getRowState(rowIndexs.get(0));
		if(state == BillModel.ADD){
			billModel.setRowState(rowIndexs.get(0), BillModel.ADD);
		}else{
			billModel.setRowState(rowIndexs.get(0), BillModel.MODIFICATION);
		}
	}

	/**
	 * 选择位置则清空设备
	 */
	private void afterEditLoc(AMBillForm billForm, BillEditEvent e) throws Exception {
		int selectRow = e.getRow();
		BillCardPanel card = billForm.getBillCardPanel();
		// 选择位置则清空设备
		BillCardPanelUtils.setBodyItemValue(card, FailureBodyVO.PK_EQUIP, null, selectRow);
		card.getBillModel().loadEditRelationItemValue(selectRow, FailureBodyVO.PK_EQUIP);

		// 支持多选
		MultiSelectCallBack callBack = new MultiSelectCallBack();
		List<Integer> editRows = MultiSelectUtils.handleMultiSelectedForBodyVOs(billForm,e, callBack);
		
		// 由于清空设备信息时也会清空位置信息，故需要重新设置位置的信息
		if (editRows != null && editRows.size() == 1) {
			card.setBodyValueAt(e.getValue(), e.getRow(), FailureBodyVO.PK_LOCATION);
			card.getBillModel().loadLoadRelationItemValue(e.getRow(), FailureBodyVO.PK_LOCATION);
			/**********************bbt 2024.08.23**************************************************/
			card.getBillModel().loadEditRelationItemValue(e.getRow(), FailureBodyVO.PK_EQUIP);
			/************************************************************************/
		}		
		/*****************************bbt 2024.08.18********************************************************/
		//多选位置后，要逐行匹配位置对应设备的相关信息，如资产使用权
		if(editRows != null && editRows.size() > 1){
			for(int i:editRows)
				card.getBillModel().loadEditRelationItemValue(i, FailureBodyVO.PK_EQUIP);
		}
		/*************************************************************************************/
		
		// 设置默认维修组织
		MaintainOrgUtil.setDefaultMaintainOrgByAssetOrg(billForm, e, editRows);
	}



	/**
	 * 选择设备后的事件处理
	 */
	private void afterEditEquip(AMBillForm billForm, BillEditEvent e) throws Exception {
		// 多选处理
		MultiSelectCallBack callBack = new MultiSelectCallBack();
		List<Integer> rowList = MultiSelectUtils.handleMultiSelectedForBodyVOs(billForm,e,callBack);
		// 执行位置的显示关联项
		// （由于在执行设备关联项时，没有有效的清除位置的关联项信息，因此需要执行位置的显示关联项）
		billForm.getBillCardPanel().getBillModel().loadLoadRelationItemValue(
				e.getRow(), FailureBodyVO.PK_LOCATION);
		// 设置默认维修组织
		MaintainOrgUtil.setDefaultMaintainOrgByAssetOrg(billForm, e, rowList);
	}


	/**
	 * 时间编辑控制，时间的比较
	 */
	private void afterEditFailureRestoreDate(AMBillForm billForm, BillEditEvent e) {
		// 时间的比较
		FailureBodyVO bodyVO = (FailureBodyVO) billForm.getBillCardPanel().getBillModel().getBodyValueRowVO(e.getRow(),
				FailureBodyVO.class.getName());
		UFDateTime restoreTime = bodyVO.getRestore_time();
		UFDateTime failureTime = bodyVO.getFailure_time();
		// 故障发生时间要早于故障结束时间
		if (failureTime != null && restoreTime != null) {
			if (failureTime.compareTo(restoreTime) > 0) {
				// 第{0}行的故障发生时间必须早于故障结束时间
				String errMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("failure_0",
						"04540002-0004", null, new String[] { String.valueOf(e.getRow() + 1) });
				billForm.showErrorMessage(errMsg);
				BillCardPanelUtils.setBodyItemValue(billForm.getBillCardPanel(), e.getKey(), null, e.getRow());
			}
		}

	}

	/**
	 * 选择故障记录后的事件处理
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	private void afterEditFailureType(AMBillForm billForm, BillEditEvent e) throws Exception {
		BillCardPanel card = billForm.getBillCardPanel();
		// 取得故障类别表体的参照
		UIRefPane refFailTypePane = (UIRefPane) card.getBodyItem(FailureBodyVO.PK_FAILURE_TYPE).getComponent();
		// 从故障类别参照中取得故障类别主键
		Object typeObject = refFailTypePane.getRefModel().getValue(FailureBodyVO.PK_FAILURE_TYPE);
		//通过故障类别联查故障原因
		editFailureType2Reason(billForm,e);
		// 将故障类别主键设置到界面，为后续加载名称做准备
		BillCardPanelUtils.setBodyItemValue(card, FailureBodyVO.PK_FAILURE_TYPE, typeObject, e.getRow());
		card.getBillModel().loadLoadRelationItemValue(e.getRow(), FailureBodyVO.PK_FAILURE_TYPE);
	}
	/**
	 * 通过故障类别联查故障原因
	 * @param billForm
	 * @param e
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "restriction" })
	private void editFailureType2Reason(AMBillForm billForm, BillEditEvent e ) throws Exception{
		// 传入变量为空，则直接返回
		if (billForm == null || e == null)
			return ;
		// 获取选择的Item
		BillItem bodyItem = billForm.getBillCardPanel().getBodyItem(FailureBodyVO.FAILURE_REASON_NAME);
		if (bodyItem == null)
			return;
		BillCardPanel card = billForm.getBillCardPanel();
		UIRefPane refFailTypePane = (UIRefPane) card.getBodyItem(FailureBodyVO.PK_FAILURE_TYPE).getComponent();
		nc.ui.bd.ref.AbstractRefModel model = refFailTypePane.getRefModel();
		// 若不存在参照，则直接返回
		if (model == null)
			return;
		// 获取Key在元数据中对应的
		String idKey = BillCardPanelUtils.getIDColumn(bodyItem);
		// 获取Key值
		String[] keyValues = model.getPkValues();

		Vector selectedData = model.getSelectedData();
		
		BillModel billModel = billForm.getBillCardPanel().getBillModel();
		//获取到需要修改的列
		int col = billModel.getBodyColByKey(FailureBodyVO.FAILURE_REASON_NAME);
		int column = billModel.getBodyColByKey(FailureBodyVO.PK_FAILURE_REASON);
		List<Integer> rows = RefSingleSelectedUtils.setMultiValue(idKey, keyValues, selectedData, billForm, e,
				null);
		//如果批改直接浮空，
		//遍历循环组装多故障原因和原因主键
		String aValueName = "";
		String aValueId = "";
		if(!Collections.isEmpty(selectedData)){
			Vector data = (Vector) selectedData.get(0);
			if(data.size() == 8&& null == data.get(7)){
				//data中的第二个元素是故障原因名称，该数据从故障树中取
				aValueName = (String) data.get(1);
				//data中的第六个元素是故障原因主键，该数据从故障树中取
				aValueId = (String) data.get(5);
			}else{
				//data中的第六个元素是故障原因主键，该数据从故障树中取
				aValueId = (String) data.get(5);
				//根据故障原因主键查询数据库，查询出故障原因名称
				if(aValueId != null){
					FailurereasonVO[] failurereasonVO  = AMProxy.lookup(IFailurereasonService.class)
							.queryFailureReasonVoByPks(aValueId);
					//多语处理
					String reasonName = MultiLanguageUtil.getMultiNameField(FailurereasonVO.REASON_NAME);
					//aValueId因为是关联操作，只能是一个故障原因，所以取数组第一个
					if(null != failurereasonVO){
						aValueName = (String) failurereasonVO[0].getAttributeValue(reasonName);
						if(null == aValueName){
							reasonName = getMutiLanSQL(FailurereasonVO.REASON_NAME);
							aValueName = (String) failurereasonVO[0].getAttributeValue(reasonName);
						}
						
					}
					
				}
			}
		}
		if(StringUtils.isNotEmpty(aValueId)){
			billModel.setValueAt(aValueName, rows.get(0), col);
			billModel.setValueAt(aValueId, rows.get(0), column);
		}else{
			billModel.setValueAt(null, rows.get(0), col);
			billModel.setValueAt(null, rows.get(0), column);
		}
		/**
		 * 设定表体行状态，否则会因为流量优化器会过滤掉，不会修改
		 * TODO (此处暂时有问题，多故障原因修改后，表体行有时候变成修改态，
		 *   有时候不变,暂未找到规律，改成除了增加其余都是修改态，暂时能规避问题，该问题以后再改
		 *   此问题可能是故障原因档案没有关联所致)
		 */
		int state = billModel.getRowState(rows.get(0));
		if(state == BillModel.ADD){
			billModel.setRowState(rows.get(0), BillModel.ADD);
		}else{
			billModel.setRowState(rows.get(0), BillModel.MODIFICATION);
		}
	}
	
	/** 
	* 
	* 优选取当前语种，如果当前语种没有值取主语种值 
	* @param mutiLanField 
	* @return string 
	* @author lissc
	*/ 
	public  String getMutiLanSQL(String mutiLanField) {
		// 1.主语种字段名称
		String mainName = mutiLanField;
		// 主语种VO
		LanguageVO mainLanVO = MultiLangContext.getInstance().getMainLangVO();
		// 序列
		Integer manLanSeq = mainLanVO.getLangseq();
		if (manLanSeq > 1) {
			mainName = mutiLanField + manLanSeq;
		}
		return mainName;
	}
	


	@Override
	public boolean handleBodyBeforeEditEvent(AMBillForm billForm, BillEditEvent e) throws Exception {
		super.handleBodyBeforeEditEvent(billForm, e);
 		BillCardPanel card = billForm.getBillCardPanel();

		// 如果存在来源单据，则不能编辑设备和位置单元
		if (e.getKey().equals(FailureBodyVO.PK_EQUIP) || e.getKey().equals(FailureBodyVO.PK_LOCATION)) {
			if (!isEquipOrLocEditable(card, e)) {
				card.getBillModel().setCellEditable(e.getRow(), FailureBodyVO.PK_EQUIP, false);
				card.getBillModel().setCellEditable(e.getRow(), FailureBodyVO.PK_LOCATION, false);
				return false;
			}
		}
		else if(e.getKey().equals(FailureBodyVO.FAILURE_REASON_NAME)){
			   UIRefPane refPane = (UIRefPane) billForm.getBillCardPanel()
				         .getBodyItem(FailureBodyVO.FAILURE_REASON_NAME).getComponent();
			    refPane.setMultiSelectedEnabled(true);
			    String pk_failure_reasons = (String)BillCardPanelUtils
						   .getBodyValue(billForm.getBillCardPanel(), FailureBodyVO.PK_FAILURE_REASON, e.getRow());
			    if(pk_failure_reasons != null){
			    	String pks[]= pk_failure_reasons.split(",");
					Vector selectData = refPane.getRefModel().getSelectedData();
					refPane.setPKs(pks);
				}
		}
		return true;
	}

	/**
	 * 判断设备或位置是否能够编辑 单据存在上游单据则不能编辑设备和位置字段
	 *
	 * @param card
	 * @param e
	 * @return
	 */
	private static boolean isEquipOrLocEditable(BillCardPanel card, BillEditEvent e) {
		// 来源单据类型不为空，则存在上有单据
		String srcBillType = (String) BillCardPanelUtils.getBodyValue(card, FailureBodyVO.SRC_BILL_TYPE, e.getRow());

		if (srcBillType != null) {
			return false;
		}

		return true;
	}
	
	
}
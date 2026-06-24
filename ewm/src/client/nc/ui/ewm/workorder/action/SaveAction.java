/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.ui.ewm.workorder.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/*     */ import java.util.List;
import java.util.Map;

import org.java.plugin.boot.ErrorDialog;

import com.google.gdata.data.DateTime;

/*     */ import nc.ui.am.action.support.AMSaveAction;
/*     */ import nc.ui.am.model.BillManageModel;
/*     */ import nc.ui.am.util.BillCardPanelUtils;
/*     */ import nc.ui.pub.beans.MessageDialog;
/*     */ import nc.ui.pub.bill.BillCardPanel;
/*     */ import nc.ui.pub.bill.BillData;
/*     */ import nc.vo.am.exception.IResumeException;
/*     */ import nc.vo.ewm.workorder.AggWorkOrderVO;
/*     */ import nc.vo.ewm.workorder.WorkOrderHeadVO;
/*     */ import nc.vo.ml.AbstractNCLangRes;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SaveAction extends AMSaveAction
/*     */ {
/*     */   public void doAction(ActionEvent e)
/*     */     throws Exception
/*     */   {
/*  39 */     newBeforeAction();
/*     */ 
/*  41 */     Object obj = getValueFromEditor();
/*     */ 
/*  43 */     beforeSave(obj);
/*     */ 
/*  45 */     validate(obj);
/*     */ 
/*  47 */     Object allValue = getAllValueFromEditor();
/*     */ 
/*  49 */     setDefaultValue(allValue);
/*     */ 
/*  51 */     boolean saveSuccess = tryToSave((AggWorkOrderVO)allValue);
/*  52 */     if (!(saveSuccess))
/*     */       return;
			  /*******************bbt 20230804*************************/
				BillManageModel billManageModel = getModel();
				AggWorkOrderVO billVO = (AggWorkOrderVO)billManageModel.getSelectedData();
				String currOrg = billVO.getParentVO().getPk_org();
				Map<String,Boolean> showWindow = new HashMap<>();
				//苏州工厂要求在工单状态从“进行中”被调整时进行校验
				//表头报告页签的实际结束时间为空，则弹窗提示“存在未出库的计划物料”，message非error
				if (currOrg.equals("0001V11000000000374G") 
						&& billVO.getParent().getAttributeValue("pk_wo_status").equals("0001V110000000002YN9")){
					//actu_end_time  实际结束时间 
						
					if(billVO.getParent().getAttributeValue("actu_end_time") != null 
							|| billVO.getParent().getAttributeValue("actu_end_time") != ""){
						//String actulEndTime = billVO.getParent().getAttributeValue("actu_end_time").toString();
						showWindow.put("2YN9", true);	
					}
				}
				/*2026.03.06 bbt 增加报告页签实际结束时间不能大于当前服务器时间校验*/
				UFDateTime actu_end_time = (UFDateTime) billVO.getParent().getAttributeValue("actu_end_time");
				String aet_s = actu_end_time.toString();
				Date nowTime = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String formatTime = sdf.format(nowTime);
		        int isLaterTime = 0;
		        isLaterTime = aet_s.compareTo(formatTime);
				
				//弹窗信息统一处理
		        if (isLaterTime > 0){
//					ErrorDialog.showError(getModel().getContext().getEntranceUI(),"请注意", "实际结束时间不能大于当前日期；\n请在报告页签下修正");
					throw new BusinessException("实际结束时间不能大于当前日期；\n请在报告页签下修正");
//					return;
				}
				if(showWindow.size() > 1){
					MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "请在确认工单完成后及时将工单状态更新为完成；\n且存在未出库的计划物料");
				}
				else if(showWindow.size() == 1){
					if(showWindow.get("2YNA") != null){
						MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "存在未出库的计划物料"); 
					}
					else if(showWindow.get("2YN9") != null){
						MessageDialog.showHintDlg(getModel().getContext().getEntranceUI(),"请注意", "请在确认工单完成后及时将工单状态更新为完成");
					}
				}
			  /**********************************************************************************/
/*  54 */     afterSave();
/*     */   }
/*     */ 
/*     */ 
/*     */   protected void setDefaultValue(Object obj)
/*     */     throws BusinessException
/*     */   {
/*  61 */     super.setDefaultValue(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void beforeAction()
/*     */   {
/*  89 */     getBillCardPanel().stopEditing();
/*     */ 
/*  91 */     String[] tabCodes = getBillCardPanel().getBillData().getBodyTableCodes();
/*  92 */     if ((tabCodes != null) && (tabCodes.length > 0))
/*  93 */       for (String tabCode : tabCodes)
/*  94 */         if ("wo_plan_inv".equals(tabCode)) {
/*  95 */           List planInvList = new ArrayList();
/*  96 */           planInvList.add("required_date");
/*  97 */           planInvList.add("pk_stockorg_v");
/*  98 */           planInvList.add("pk_stockorg");
/*  99 */           BillCardPanelUtils.deleteEmptyRows(getBillCardPanel(), tabCode, planInvList);
/*     */         } else {
/* 101 */           BillCardPanelUtils.deleteEmptyRows(getBillCardPanel(), tabCode);
/*     */         }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean tryToSave(AggWorkOrderVO workOrderBillVO)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 115 */       save(workOrderBillVO);
/*     */     } catch (BusinessException ex) {
/* 117 */       if (ex instanceof IResumeException) {
/* 118 */         IResumeException resumeException = (IResumeException)ex;
/* 119 */         if ("repairPlanCostCheck".equals(resumeException.getBusiExceptionType())) {
/* 120 */           boolean isResume = isResume(resumeException);
/* 121 */           if (isResume) {
/* 122 */             setabandonCheckFlag(workOrderBillVO);
/* 123 */             tryToSave(workOrderBillVO);
/*     */           } else {
/* 125 */             setSuccessMessage(null);
/* 126 */             return false;
/*     */           }
/*     */         } else {
/* 129 */           throw ex;
/*     */         }
/*     */       } else {
/* 132 */         throw ex;
/*     */       }
/*     */     }
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setabandonCheckFlag(AggWorkOrderVO billVO)
/*     */   {
/* 144 */     billVO.getParentVO().setAttributeValue("check_plan_cost", UFBoolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isResume(IResumeException resumeException)
/*     */   {
/* 156 */     return (MessageDialog.showYesNoDlg(getModel().getContext().getEntranceUI(), null, NCLangRes4VoTransl.getNCLangRes().getStrByID("workorder_0", "04560003-0494", null, new String[] { resumeException.getResumeMessage() })) == 4);
/*     */   }
/*     */ }

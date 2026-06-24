/*    */ package nc.ui.ewm.workorder.tabaction;
/*    */ 
/*    */ import nc.ui.am.action.support.AMInsertLineAction;
/*    */ import nc.ui.am.editor.AMBillForm;
/*    */ import nc.ui.am.model.BillManageModel;
/*    */ import nc.ui.am.util.RownoClientUtils;
/*    */ import nc.ui.ewm.workorder.utils.WorkOrderBodyCostUtils;
/*    */ import nc.ui.ewm.workorder.view.WorkOrderViewUtil;
/*    */ import nc.ui.pub.bill.BillCardPanel;
/*    */ import nc.ui.pub.bill.BillModel;
import nc.ui.trade.business.HYPubBO_Client;
import nc.uif.pub.exception.UifException;
/*    */ import nc.vo.am.common.BizContext;
/*    */ import nc.vo.uif2.LoginContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LineInsertAction
/*    */   extends AMInsertLineAction
/*    */ {
/*    */   private String tabcode;
/*    */   
/*    */   protected void setDefaultValue(int row)
/*    */   {
/* 34 */     super.setDefaultValue(row);
/* 35 */     if (getTabcode() == null) {
/* 36 */       return;
/*    */     }
/* 38 */     if (("wo_taskobj".equals(getTabcode())) || ("wo_task".equals(getTabcode())))
/*    */     {
/*    */ 
/* 41 */       RownoClientUtils.whenAddLineVarStep(getBillForm().getBillCardPanel(), "sequence_num", 1);
/*    */     }
/*    */     
/* 44 */     if ("wo_plan_inv".equals(getTabcode())) {
/* 45 */       Object objRequireDate = WorkOrderViewUtil.getPlanInvDefaultRequireDate(getBillForm());
/*    */       
/* 47 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(objRequireDate, row, "required_date");
/*    */ 
/*    */ 
/*    */     }
/* 51 */     else if ("wo_actual_inv".equals(getTabcode())) {
/* 52 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(BizContext.getInstance().getBizDate(), row, "inout_date");
/*    */     }
/* 54 */     else if ("wo_part".equals(getTabcode())) {
/* 55 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(BizContext.getInstance().getBizDate(), row, "replacedate");
/*    */     }
/* 57 */     else if (getTabcode().equals("wo_log"))
/*    */     {
/* 59 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(getModel().getContext().getPk_loginUser(), row, "pk_recorder_ID");
/*    */       
/* 61 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).loadLoadRelationItemValue(row, "pk_recorder");
/*    */       
/* 63 */       getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(BizContext.getInstance().getServerDateTime(), row, "rec_time");
/*    */     }else if("wo_actual_psn".equals(getTabcode())){
				Object pk_org = getEditedCardPanel().getHeadItem("pk_org").getValueObject();
				Object o = null;
				try {
					o = HYPubBO_Client.findColValue("pub_sysinit", "value",
							" nvl(dr,0)= 0 and pk_org = '" + pk_org + "' and initcode = 'YF603'");
				} catch (UifException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				getBillForm().getBillCardPanel().getBillModel(getTabcode()).setValueAt(o, row, "rate");
			}
/*    */     
/* 66 */     WorkOrderBodyCostUtils.headTotalCostBody(getBillForm().getBillCardPanel());
/*    */   }
/*    */   protected boolean isActionEnable()
/*    */   {
/* 71 */     if (super.isActionEnable()) {
/* 72 */       return !getTabcode().equals("wo_permit");
/*    */     }
/*    */     
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public String getTabcode() {
/* 79 */     if (tabcode == null) {
/* 80 */       return getBillForm().getBillCardPanel().getCurrentBodyTableCode();
/*    */     }
/* 82 */     return tabcode;
/*    */   }
/*    */   
/*    */   public void setTabcode(String tabcode)
/*    */   {
/* 87 */     this.tabcode = tabcode;
/*    */   }
/*    */ }

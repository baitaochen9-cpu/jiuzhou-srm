/*    */ package nc.ui.jzqc.labelcontrol.action.model;
/*    */ 
/*    */ import nc.buzimsg.util.BuzimsgUtil;
/*    */ import nc.buzimsg.view.RcvconfBatchBillTable;
/*    */ import nc.ui.uif2.AppEvent;
/*    */ import nc.ui.uif2.AppEventListener;
/*    */ import nc.ui.uif2.model.HierachicalDataAppModel;
/*    */ import nc.ui.uif2.model.IAppModelDataManagerEx;
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
/*    */ public class BuzimsgMediator
/*    */   implements AppEventListener
/*    */ {
/*    */   private HierachicalDataAppModel treeModel;
/*    */   private IAppModelDataManagerEx msgtempModelDataManager;
/*    */   private IAppModelDataManagerEx receiverModelDataManager;
/*    */   private RcvconfBatchBillTable rcvConfTable;
/*    */   
/*    */   public void setTreeModel(HierachicalDataAppModel treeModel)
/*    */   {
/* 32 */     this.treeModel = treeModel;
/* 33 */     treeModel.addAppEventListener(this);
/*    */   }
/*    */   
/*    */   public void setMsgtempModelDataManager(IAppModelDataManagerEx msgtempModelDataManager)
/*    */   {
/* 38 */     this.msgtempModelDataManager = msgtempModelDataManager;
/*    */   }
/*    */   
/*    */   public void setReceiverModelDataManager(IAppModelDataManagerEx receiverModelDataManager)
/*    */   {
/* 43 */     this.receiverModelDataManager = receiverModelDataManager;
/*    */   }
/*    */   
/*    */   public void setRcvConfTable(RcvconfBatchBillTable rcvConfTable) {
/* 47 */     this.rcvConfTable = rcvConfTable;
/*    */   }
/*    */   
/*    */   public void handleEvent(AppEvent event)
/*    */   {
/* 52 */     if (treeModel.equals(event.getSource())) {
/* 53 */       if ("Model_Initialized".equals(event.getType())) {
/* 55 */         receiverModelDataManager.initModel();
/* 56 */       } else if ("Selection_Changed".equals(event.getType())) {
/* 59 */         receiverModelDataManager.initModelBySqlWhere(getSelMsgresRegVOCode());
/*    */         
/* 61 */         rcvConfTable.displayReceiverColumnValue();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private String getSelMsgresRegVOCode() {
/* 67 */     Object selected = treeModel.getSelectedData();
/* 68 */     return BuzimsgUtil.getMsgresCode(selected);
/*    */   }
/*    */ }
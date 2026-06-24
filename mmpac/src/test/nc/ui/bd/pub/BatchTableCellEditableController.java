/*    */ package nc.ui.bd.pub;
/*    */ 
/*    */ import nc.ui.pub.bill.BillModelCellEditableController;
/*    */ import nc.ui.uif2.model.BatchBillTableModel;
/*    */ import nc.vo.util.ManageModeUtil;
/*    */ import org.apache.commons.lang.StringUtils;
/*    */ 
/*    */ public class BatchTableCellEditableController
/*    */   implements BillModelCellEditableController
/*    */ {
/*    */   private BatchBillTableModel model;
/*    */   
/*    */   public boolean isCellEditable(boolean value, int row, String itemkey)
/*    */   {
/* 15 */     if ((row < 0) || (StringUtils.isEmpty(itemkey)))
/* 16 */       return false;
/* 17 */     Object rowData = getModel().getRow(row);
/* 18 */     return (value) && (ManageModeUtil.manageable(rowData, getModel().getContext()));
/*    */   }
/*    */   
/*    */   public BatchBillTableModel getModel() {
/* 22 */     return model;
/*    */   }
/*    */   
/*    */   public void setModel(BatchBillTableModel model) {
/* 26 */     this.model = model;
/*    */   }
/*    */ }


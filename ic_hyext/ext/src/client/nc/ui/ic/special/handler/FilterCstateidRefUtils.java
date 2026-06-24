package nc.ui.ic.special.handler;
/*    */ 
/*    */ import nc.ui.bd.ref.AbstractRefModel;
/*    */ import nc.ui.pub.beans.UIRefPane;
/*    */ import nc.vo.pub.lang.UFBoolean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilterCstateidRefUtils
/*    */ {
/*    */   private UIRefPane pane;
/*    */ 
/*    */   public FilterCstateidRefUtils(UIRefPane pane)
/*    */   {
/* 18 */     this.pane = pane;
/*    */   }
/*    */ 
/*    
/*    */ 
/*    */   public void filterItemRefByOrg(String pk_org) {
/* 31 */     if ((this.pane == null) || (this.pane.getRefModel() == null)) {
/* 32 */       return;
/*    */     }
/* 34 */     this.pane.getRefModel().setWherePart("pk_org='"+pk_org+"'", true);
/*    */   }
/*    */ }

package nc.ui.pu.m23.editor.card.beforeedit.body;
/*    */ 
/*    */ import nc.ui.bd.ref.AbstractRefModel;
/*    */ import nc.ui.pub.beans.UIRefPane;
/*    */ import nc.vo.pub.lang.UFBoolean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PuFilterCstateidRefUtils
/*    */ {
/*    */   private UIRefPane pane;
/*    */ 
/*    */   public PuFilterCstateidRefUtils(UIRefPane pane)
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

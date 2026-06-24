package nc.ui.bd.material.config.action;
/*    */ 
/*    */ import java.awt.BorderLayout;

/*    */ import javax.swing.BorderFactory;

/*    */ import nc.bs.busilog.vo.BusinessLogVO;
/*    */ import nc.bs.logging.Logger;
/*    */ import nc.ui.pub.beans.UIPanel;
/*    */ import nc.ui.pub.beans.UIScrollPane;
/*    */ import nc.ui.pub.beans.UITextArea;
/*    */ import nc.vo.pub.BusinessException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusiLogDetailPanel
/*    */   extends UIPanel
/*    */ {
/*    */   private static final long serialVersionUID = 2570794765590099359L;
/* 20 */   private UIScrollPane scrollPane = null;
/*    */   
/* 22 */   private UITextArea textArea = null;
/*    */   
/*    */   public BusiLogDetailPanel()
/*    */   {
/* 26 */     initUI();
/*    */   }
/*    */   
/*    */   private void initUI() {
/* 30 */     setLayout(new BorderLayout());
/* 31 */     add(getScrollPane(), "Center");
/*    */   }
/*    */   
/*    */   private UIScrollPane getScrollPane() {
/* 35 */     if (scrollPane == null) {
/* 36 */       scrollPane = new UIScrollPane(getTextArea());
/* 37 */       scrollPane.setBorder(BorderFactory.createEmptyBorder());
/*    */     }
/*    */     
/*    */ 
/* 41 */     return scrollPane;
/*    */   }
/*    */   
/*    */   private UITextArea getTextArea() {
/* 45 */     if (textArea == null) {
/* 46 */       textArea = new UITextArea();
/* 47 */       textArea.setEditable(false);
/* 48 */       textArea.setLineWrap(true);
/* 49 */       textArea.setOpaque(false);
/* 50 */       textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
/*    */     }
/* 52 */     return textArea;
/*    */   }
/*    */   
/*    */   
/*    */   public void setDetail(BusinessLogVO vo) {
/* 70 */     String readableDetail = null;
/*    */     try {
/* 72 */       readableDetail = BusiLogDetailDomParser.getInstance(vo).getFormattedDetail().toString();
/*    */     }
/*    */     catch (BusinessException e) {
/* 75 */       Logger.error("½âÎöxml³ö´í");
/* 76 */       readableDetail = vo.getLogmsg();
/*    */     }
/* 78 */     getTextArea().setText(readableDetail);
/*    */   }
/*    */   
/*    */   public void clearDetail() {
/* 82 */     getTextArea().setText("");
/*    */   }
/*    */ }
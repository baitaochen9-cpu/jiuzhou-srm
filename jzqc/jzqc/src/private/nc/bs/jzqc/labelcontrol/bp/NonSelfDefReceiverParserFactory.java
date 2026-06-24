package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.buzimsg.vo.ReceiverTypeConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NonSelfDefReceiverParserFactory
/*    */   extends ReceiverTypeConstants
/*    */ {
/*    */   public static INonSelfDefReceiverParser getINonSelfDefReceiverParser(int type)
/*    */   {
/* 17 */     switch (type) {
/*    */     case 1: 
/* 19 */       return new UserTypeReceiverParserImpl();
/*    */     case 2: 
/* 21 */       return new UserGroupTypeReceiverParserImpl();
/*    */     case 3: 
/* 23 */       return new RoleTypeReceiverParserImpl();
/*    */     case 4: 
/* 25 */       return new RoleGroupTypeReceiverParserImpl();
/*    */     case 5: 
/* 27 */       return new PersonTypeReceiverParserImpl();
/*    */     case 6: 
/* 29 */       return new ResponsibilityTypeReceiverParserImpl();
/*    */     case 7: 
/* 31 */       return new PostTypeReceiverPaserImpl();
/*    */     }
/* 33 */     return new UserTypeReceiverParserImpl();
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.NonSelfDefReceiverParserFactory
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
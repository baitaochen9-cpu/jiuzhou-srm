package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserTypeReceiverParserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 17 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 18 */       return null;
/*    */     }
/* 20 */     Set<String> set = new HashSet();
/* 21 */     for (String userpk : receiverpks) {
/* 22 */       set.add(userpk);
/*    */     }
/* 24 */     return set;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.UserTypeReceiverParserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
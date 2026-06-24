package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import nc.bs.framework.common.NCLocator;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.itf.uap.rbac.IResponsibilityQueryService;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import nc.vo.sm.UserVO;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResponsibilityTypeReceiverParserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 21 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 22 */       return null;
/*    */     }
/* 24 */     return getUserPksByResPks(receiverpks);
/*    */   }
/*    */   
/*    */   private Set<String> getUserPksByResPks(String[] resPks)
/*    */     throws BusinessException
/*    */   {
/* 30 */     UserVO[] users = getIResponsibilityQueryService().queryUsersOwnedResponsibilitys(resPks);
/* 31 */     if (ArrayUtils.isEmpty(users)) {
/* 32 */       return null;
/*    */     }
/* 34 */     Set<String> userpkSet = new HashSet();
/* 35 */     for (UserVO user : users) {
/* 36 */       userpkSet.add(user.getCuserid());
/*    */     }
/* 38 */     return userpkSet;
/*    */   }
/*    */   
/*    */ 
/* 42 */   private IResponsibilityQueryService query = null;
/*    */   
/* 44 */   private IResponsibilityQueryService getIResponsibilityQueryService() { if (query == null)
/* 45 */       query = ((IResponsibilityQueryService)NCLocator.getInstance().lookup(IResponsibilityQueryService.class));
/* 46 */     return query;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.ResponsibilityTypeReceiverParserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
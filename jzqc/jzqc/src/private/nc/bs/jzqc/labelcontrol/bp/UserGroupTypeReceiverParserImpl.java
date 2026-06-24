package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import nc.bs.framework.common.NCLocator;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.itf.uap.rbac.IUserManageQuery;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import nc.vo.sm.UserVO;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UserGroupTypeReceiverParserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 20 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 21 */       return null;
/*    */     }
/* 23 */     return getUserPksByUserGroupPks(receiverpks);
/*    */   }
/*    */   
/*    */   private Set<String> getUserPksByUserGroupPks(String[] userGroupPks)
/*    */     throws BusinessException
/*    */   {
/* 29 */     UserVO[] users = getIUserManageQuery().queryUsersRelateInUserGroups(userGroupPks, true);
/* 30 */     if (ArrayUtils.isEmpty(users)) {
/* 31 */       return null;
/*    */     }
/* 33 */     Set<String> userpkSet = new HashSet();
/* 34 */     for (UserVO user : users) {
/* 35 */       userpkSet.add(user.getCuserid());
/*    */     }
/* 37 */     return userpkSet;
/*    */   }
/*    */   
/*    */ 
/* 41 */   private IUserManageQuery query = null;
/*    */   
/* 43 */   private IUserManageQuery getIUserManageQuery() { if (query == null)
/* 44 */       query = ((IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class));
/* 45 */     return query;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.UserGroupTypeReceiverParserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
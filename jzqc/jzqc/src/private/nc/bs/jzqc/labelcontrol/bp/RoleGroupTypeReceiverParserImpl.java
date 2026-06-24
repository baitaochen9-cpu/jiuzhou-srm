package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import nc.bs.framework.common.NCLocator;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.itf.uap.rbac.IRoleManageQuery;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import nc.vo.uap.rbac.role.RoleVO;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RoleGroupTypeReceiverParserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 22 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 23 */       return null;
/*    */     }
/* 25 */     return getUserPksByRoleGroupPks(receiverpks);
/*    */   }
/*    */   
/*    */   private Set<String> getUserPksByRoleGroupPks(String[] roleGroupPks)
/*    */     throws BusinessException
/*    */   {
/* 31 */     RoleVO[] roles = getIRoleManageQuery().queryRolesInRoleGroup(roleGroupPks, null);
/* 32 */     if (ArrayUtils.isEmpty(roles)) {
/* 33 */       return null;
/*    */     }
/* 35 */     List<String> rolePkList = new ArrayList();
/* 36 */     for (RoleVO role : roles) {
/* 37 */       rolePkList.add(role.getPk_role());
/*    */     }
/* 39 */     RoleTypeReceiverParserImpl roleParser = new RoleTypeReceiverParserImpl();
/* 40 */     return roleParser.getUserPks((String[])rolePkList.toArray(new String[0]));
/*    */   }
/*    */   
/*    */ 
/* 44 */   private IRoleManageQuery query = null;
/*    */   
/* 46 */   private IRoleManageQuery getIRoleManageQuery() { if (query == null)
/* 47 */       query = ((IRoleManageQuery)NCLocator.getInstance().lookup(IRoleManageQuery.class));
/* 48 */     return query;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.RoleGroupTypeReceiverParserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import nc.bs.framework.common.InvocationInfoProxy;
/*    */ import nc.bs.framework.common.NCLocator;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.pubitf.rbac.IUserPubService;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import nc.vo.sm.UserVO;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ import org.apache.commons.collections.MapUtils;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ public class PersonTypeReceiverParserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   private IUserPubService service;
/*    */   
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 25 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 26 */       return null;
/*    */     }
/* 28 */     return getUserPksByPsndocpks(receiverpks);
/*    */   }
/*    */   
/*    */   private Set<String> getUserPksByPsndocpks(String[] psndocpks)
/*    */     throws BusinessException
/*    */   {
/* 34 */     HashMap<String, UserVO[]> hashMap = getIUserPubService().batchQueryUserVOsByPsnDocID(psndocpks, InvocationInfoProxy.getInstance().getGroupId());
/*    */     
/* 36 */     if (MapUtils.isEmpty(hashMap)) {
/* 37 */       return null;
/*    */     }
/* 39 */     Set<String> userpkSet = new HashSet();
/* 40 */     Iterator<String> it = hashMap.keySet().iterator();
/* 41 */     while (it.hasNext()) {
/* 42 */       String pk_psndoc = (String)it.next();
/* 43 */       UserVO[] users = (UserVO[])hashMap.get(pk_psndoc);
/* 44 */       if (!ArrayUtils.isEmpty(users))
/*    */       {
/* 46 */         for (UserVO user : users)
/* 47 */           userpkSet.add(user.getCuserid());
/*    */       }
/*    */     }
/* 50 */     if (CollectionUtils.isEmpty(userpkSet)) {
/* 51 */       return null;
/*    */     }
/* 53 */     return userpkSet;
/*    */   }
/*    */   
/*    */ 
/*    */   private IUserPubService getIUserPubService()
/*    */   {
/* 59 */     if (service == null)
/* 60 */       service = ((IUserPubService)NCLocator.getInstance().lookup(IUserPubService.class));
/* 61 */     return service;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.PersonTypeReceiverParserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
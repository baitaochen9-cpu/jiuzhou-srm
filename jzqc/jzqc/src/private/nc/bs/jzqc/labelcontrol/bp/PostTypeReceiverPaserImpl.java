package nc.bs.jzqc.labelcontrol.bp;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import nc.bs.framework.common.NCLocator;
/*    */ import nc.buzimsg.itf.INonSelfDefReceiverParser;
/*    */ import nc.pubitf.uapbd.IPsndocPubService;
/*    */ import nc.vo.pub.BusinessException;
/*    */ import nc.vo.sm.UserVO;
/*    */ import org.apache.commons.collections.CollectionUtils;
/*    */ import org.apache.commons.collections.MapUtils;
/*    */ import org.apache.commons.lang.ArrayUtils;
/*    */ 
/*    */ public class PostTypeReceiverPaserImpl
/*    */   implements INonSelfDefReceiverParser
/*    */ {
/*    */   private IPsndocPubService service;
/*    */   
/*    */   public Set<String> getUserPks(String[] receiverpks)
/*    */     throws BusinessException
/*    */   {
/* 24 */     if (ArrayUtils.isEmpty(receiverpks)) {
/* 25 */       return null;
/*    */     }
/* 27 */     return getUserPkSetByPostPks(receiverpks);
/*    */   }
/*    */   
/*    */ 
/*    */   private Set<String> getUserPkSetByPostPks(String[] postpks)
/*    */     throws BusinessException
/*    */   {
/* 34 */     Set<String> userpkSet = new HashSet();
/* 35 */     for (String pk_post : postpks)
/*    */     {
/* 37 */       HashMap<String, UserVO[]> hashMap = getIPsndocPubService().queryUsersByPost(pk_post);
/* 38 */       if (!MapUtils.isEmpty(hashMap))
/*    */       {
/*    */ 
/* 41 */         Iterator<String> it = hashMap.keySet().iterator();
/* 42 */         while (it.hasNext()) {
/* 43 */           String pk_psndoc = (String)it.next();
/* 44 */           UserVO[] users = (UserVO[])hashMap.get(pk_psndoc);
/* 45 */           if (!ArrayUtils.isEmpty(users))
/*    */           {
/* 47 */             for (UserVO user : users)
/* 48 */               userpkSet.add(user.getCuserid());
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 53 */     if (CollectionUtils.isEmpty(userpkSet)) {
/* 54 */       return null;
/*    */     }
/* 56 */     return userpkSet;
/*    */   }
/*    */   
/*    */ 
/*    */   private IPsndocPubService getIPsndocPubService()
/*    */   {
/* 62 */     if (service == null)
/* 63 */       service = ((IPsndocPubService)NCLocator.getInstance().lookup(IPsndocPubService.class));
/* 64 */     return service;
/*    */   }
/*    */ }

/* Location:           D:\pub\home_Test\modules\riart\META-INF\lib\riart_riartmessageLevel-1.jar
 * Qualified Name:     nc.buzimsg.impl.PostTypeReceiverPaserImpl
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
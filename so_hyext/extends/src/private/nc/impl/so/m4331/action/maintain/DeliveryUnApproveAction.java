/*    */ package nc.impl.so.m4331.action.maintain;
/*    */ 
/*    */ import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.pub.action.N_4331_UNAPPROVE;
import nc.bs.so.m4331.maintain.rule.atp.DeliveryVOATPAfterRule;
import nc.bs.so.m4331.maintain.rule.atp.DeliveryVOATPBeforeRule;
import nc.bs.so.m4331.plugin.Action4331PlugInPoint;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.so.m4331.action.maintain.rule.unapprove.CheckEnableUnApproveRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;
import nc.vo.so.pub.enumeration.BillStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeliveryUnApproveAction
/*    */ {
/*    */   public DeliveryVO[] unapprove(N_4331_UNAPPROVE script)
/*    */   {
/* 20 */     DeliveryVO[] retvos = null;
/*    */     try {
/* 22 */       Object[] inCurObjects = script.getPfParameterVO().m_preValueVos;
/* 23 */       DeliveryVO[] inCurVOs = new DeliveryVO[inCurObjects.length];
/* 24 */       int length = inCurObjects.length;
/* 25 */       for (int i = 0; i < length; ++i) {
/* 26 */         inCurVOs[i] = ((DeliveryVO)inCurObjects[i]);
/*    */       }
/*    */ 
/* 29 */       DeliveryHVO vo = inCurVOs[0].getParentVO();
/* 30 */       AroundProcesser processer = new AroundProcesser(Action4331PlugInPoint.UnApproveAction);
/*    */ 
/* 32 */       addBeforRule(processer, vo.getFstatusflag());
/* 33 */       addAfterATPRule(processer, vo.getFstatusflag());
/* 34 */       TimeLog.logStart();
/* 35 */       processer.before(inCurVOs);
/* 36 */       TimeLog.info("调用审批流前执行业务规则");
/* 37 */       TimeLog.logStart();
/* 38 */       script.procUnApproveFlow(script.getPfParameterVO());
/* 39 */       TimeLog.info("走审批工作流处理，此处不允许进行修改");
/* 40 */       TimeLog.logStart();
/* 41 */       TimeLog.info("调用审批流后执行业务规则");
/* 42 */       TimeLog.logStart();
/* 43 */       retvos = queryNewVO(inCurVOs);
/*    */ 
/* 45 */       processer.after(retvos);
/*     */      EventDispatcher.fireEvent(new BusinessEvent("39148475-6fd6-45c3-915d-b57e0f3dce85","1022", retvos));

/* 46 */       TimeLog.info("组织返回值,返回轻量级VO");
/*    */     }
/*    */     catch (Exception e) {
/* 49 */       ExceptionUtils.wrappException(e);
/*    */     }
/* 51 */     return retvos;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void addBeforRule(AroundProcesser<DeliveryVO> processer, Integer statusflag)
/*    */   {
/* 59 */     IRule rule = new CheckEnableUnApproveRule();
/* 60 */     processer.addBeforeRule(rule);
/*    */ 
/* 62 */     if (BillStatus.NOPASS.getIntValue() != statusflag.intValue())
/*    */       return;
/* 64 */     rule = new DeliveryVOATPBeforeRule();
/* 65 */     processer.addBeforeRule(rule);
/*    */   }
/*    */ 
/*    */ 
/*    */   private void addAfterATPRule(AroundProcesser<DeliveryVO> processer, Integer statusflag)
/*    */   {
/* 71 */     if (BillStatus.NOPASS.getIntValue() == statusflag.intValue()) {
/* 72 */       IRule rule = null;
/* 73 */       rule = new DeliveryVOATPAfterRule();
/* 74 */       processer.addAfterRule(rule);
/*    */     }
/*    */   }
/*    */ 
/*    */   private DeliveryVO[] queryNewVO(DeliveryVO[] bills) {
/* 79 */     String[] ids = new String[bills.length];
/* 80 */     int length = bills.length;
/* 81 */     for (int i = 0; i < length; ++i) {
/* 82 */       ids[i] = bills[i].getPrimaryKey();
/*    */     }
/* 84 */     BillQuery query = new BillQuery(DeliveryVO.class);
/* 85 */     return ((DeliveryVO[])query.query(ids));
/*    */   }
/*    */ }

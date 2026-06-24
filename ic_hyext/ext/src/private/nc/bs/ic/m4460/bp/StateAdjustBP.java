/*    */ package nc.bs.ic.m4460.bp;
/*    */ 
/*    */ import nc.bs.ic.m4460.bp.rule.CheckOnhandTSRule;
/*    */ import nc.bs.ic.m4460.bp.rule.InsFlowAccountRule;
import nc.bs.ic.m4460.bp.rule.LablePrintStatusRule;
/*    */ import nc.bs.ic.m4460.bp.rule.StateAdjustDefaultValueRule;
/*    */ import nc.bs.ic.m4460.bp.rule.StateCheckRule;
/*    */ import nc.bs.ic.m4460.bp.rule.UpdateOnhandNumRule;
/*    */ import nc.bs.ic.m4460.bp.rule.UpdatePkOnhanddimAdjRule;
/*    */ import nc.bs.ic.m4460.bp.rule.UpdateReserveRule;
/*    */ import nc.impl.pubapp.pattern.data.vo.VOInsert;
/*    */ import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
/*    */ import nc.vo.ic.m4460.entity.StateAdjustVO;
/*    */ import nc.vo.pubapp.pattern.log.TimeLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StateAdjustBP
/*    */ {
/*    */   public StateAdjustVO[] stateAdjust(StateAdjustVO[] vos)
/*    */   {
/* 25 */     AroundProcesser<StateAdjustVO> processer = new AroundProcesser(null);
/*    */     
/*    */ 
/* 28 */     addBeforeRule(processer);
/*    */     
/* 30 */     addAfterRule(processer);
/*    */     
/* 32 */     TimeLog.logStart();
/* 33 */     processer.before(vos);
/* 34 */     TimeLog.info("调用新增保存BP前执行业务规则");
/*    */     
/*    */ 
/* 37 */     TimeLog.logStart();
/* 38 */     VOInsert<StateAdjustVO> tool = new VOInsert();
/* 39 */     StateAdjustVO[] retVOs = (StateAdjustVO[])tool.insert(vos);
/* 40 */     TimeLog.info("调用新增保存BP，进行保存");
/*    */     
/* 42 */     TimeLog.logStart();
/* 43 */     processer.after(retVOs);
/* 44 */     TimeLog.info("调用新增保存BP后执行业务规则");
/* 45 */     return retVOs;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void addAfterRule(AroundProcesser<StateAdjustVO> processer)
/*    */   {
/* 55 */     processer.addAfterRule(new InsFlowAccountRule(true));
/*    */     
/*    */ 
/* 58 */     processer.addAfterRule(new UpdateOnhandNumRule());
/*    */     
/*    */ 
/* 61 */     processer.addAfterRule(new InsFlowAccountRule(false));
/*    */     
/*    */ 
/* 64 */     processer.addAfterRule(new UpdateReserveRule());
/*    */     
/*    */ 
/* 67 */     processer.addAfterRule(new UpdatePkOnhanddimAdjRule());
/*    */   }
/*    */   
/*    */ 
/*    */   private void addBeforeRule(AroundProcesser<StateAdjustVO> processer)
/*    */   {
/* 73 */     processer.addBeforeRule(new StateAdjustDefaultValueRule());
/*    */     
/* 75 */     processer.addBeforeRule(new StateCheckRule());
/*    */     
/*    */ 
/* 78 */     processer.addBeforeRule(new CheckOnhandTSRule());
			 processer.addBeforeRule(new LablePrintStatusRule());

/*    */   }
/*    */ }

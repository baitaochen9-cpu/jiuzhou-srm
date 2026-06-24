/*     */ package nc.impl.so.m4331.action.maintain;
/*     */ 
/*     */ import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.action.N_4331_APPROVE;
/*     */ import nc.impl.pubapp.pattern.data.bill.BillQuery;
/*     */ import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.impl.so.m4331.action.maintain.rule.approve.CheckBatchCodeRule;
import nc.impl.so.m4331.action.maintain.rule.approve.CheckQualityMarketRule;
import nc.impl.so.m4331.action.maintain.rule.approve.CheckSalePackeListApproveRule;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */ import nc.vo.pubapp.pattern.log.TimeLog;
/*     */ import nc.vo.so.m4331.entity.DeliveryVO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeliveryApproveAction
/*     */ {
/*     */  public Object approve(N_4331_APPROVE script) 
/*     */   {
/*  18 */     Object ret = null;
/*     */     try {
/*  20 */       Object[] inCurObjects = script.getPfParameterVO().m_preValueVos;
/*  21 */       checkDefaultData(inCurObjects);
/*  22 */       DeliveryVO[] inCurVOs = new DeliveryVO[inCurObjects.length];
/*  23 */       int length = inCurObjects.length;
/*  24 */       for (int i = 0; i < length; i++) {
/*  25 */         inCurVOs[i] = ((DeliveryVO)inCurObjects[i]);
/*     */       }
/*  27 */       TimeLog.logStart();
/*     */       
/*     */ 
/*     */ 
/*  31 */       if (inCurVOs.length > 0) {
/*  32 */        script.getPfParameterVO().m_preValueVo = inCurVOs[0];
/*  33 */         script.getPfParameterVO().m_preValueVos = inCurVOs;
/*     */       }
/*     */       
/*  36 */       TimeLog.info(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0067"));
/*     */       
/*  38 */       TimeLog.logStart();
/*  39 */       TimeLog.info(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0068"));
/*     */       
/*     */ 
/*     */ 
/*  43 */       AroundProcesser<DeliveryVO> processer = new AroundProcesser(nc.bs.so.m4331.plugin.Action4331PlugInPoint.ApproveAction);
/*     */       
/*     */ 
/*  46 */       TimeLog.logStart();
/*  47 */       addBeforeRule(processer);
/*  48 */       processer.before(inCurVOs);
/*  49 */       TimeLog.info("调用审批前操作插入点");
/*     */       
/*  51 */       TimeLog.logStart();
/*  52 */       ret = script.procActionFlow(script.getPfParameterVO());
/*  53 */       TimeLog.info(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0069"));
/*     */       
/*  55 */       if (null == ret) {
/*  56 */         TimeLog.logStart();
/*  57 */         TimeLog.info(NCLangResOnserver.getInstance().getStrByID("4006002_0", "04006002-0138"));
/*     */         
/*  59 */         TimeLog.logStart();
/*  60 */         ret = queryNewVO(inCurVOs);
/*     */         addAfterRule(processer);
/*  62 */         processer.after((DeliveryVO[])ret);
/*     */         

/*  64 */         TimeLog.info(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0070"));
/*     */       }
				// 发货单审核推送
				EventDispatcher.fireEvent(new BusinessEvent("39148475-6fd6-45c3-915d-b57e0f3dce85","1020", ret));
/*     */       
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  71 */       ExceptionUtils.wrappException(e);
/*     */     }
/*  73 */     return ret;
/*     */   }
/*     */   
/*     */   private void addBeforeRule(AroundProcesser<DeliveryVO> processer)
/*     */   {
/*  78 */     nc.impl.pubapp.pattern.rule.IRule<DeliveryVO> rule = new nc.impl.so.m4331.action.maintain.rule.approve.CheckSalePackeListApproveRule();
/*  79 */     processer.addBeforeRule(rule);
				// 添加校验批次号校验规则
				rule = new CheckBatchCodeRule();
				processer.addBeforeRule(rule);
				// 添加校验质量市场
				rule = new CheckQualityMarketRule();
				processer.addBeforeRule(rule);
/*     */   }

private void addAfterRule(AroundProcesser<DeliveryVO> processer)
/*     */   {
CheckSalePackeListApproveRule rule1 = new CheckSalePackeListApproveRule();
processer.addAfterRule(rule1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkDefaultData(Object[] inCurObjects)
/*     */   {
/*  87 */     if (nc.vo.trade.checkrule.VOChecker.isEmpty(inCurObjects)) {
/*  88 */       ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0071"));
/*     */     }
/*     */     
/*     */ 
/*  92 */     if (!(inCurObjects instanceof DeliveryVO[])) {
/*  93 */       ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4006002_0", "04006002-0072"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private DeliveryVO[] queryNewVO(DeliveryVO[] bills)
/*     */   {
/* 100 */     String[] ids = new String[bills.length];
/* 101 */     int length = bills.length;
/* 102 */     for (int i = 0; i < length; i++) {
/* 103 */       ids[i] = bills[i].getPrimaryKey();
/*     */     }
/* 105 */     BillQuery<DeliveryVO> query = new BillQuery(DeliveryVO.class);
/* 106 */     return (DeliveryVO[])query.query(ids);
/*     */   }
/*     */ }

/* Location:           D:\zhw\home0816\modules\so\META-INF\lib\so_delivery.jar
 * Qualified Name:     nc.impl.so.m4331.action.maintain.DeliveryApproveAction
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*    */ package nc.bs.pub.action;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;

import nc.bs.dao.BaseDAO;
/*    */ import nc.bs.mmpac.wr.bp.WrFlowAutoFlushBP;
/*    */ import nc.bs.mmpac.wr.plugin.WrPluginPoint;
import nc.bs.pub.action.rule.BeforeApproveProduceRule;
/*    */ import nc.bs.pubapp.pf.action.AbstractPfAction;
/*    */ import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.jdbc.framework.processor.ArrayProcessor;
/*    */ import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.mmpac.wr.entity.WrItemVO;
import nc.vo.mmpac.wr.entity.WrVO;
/*    */ import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
/*    */ import nc.vo.pubapp.pattern.exception.ExceptionUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class N_55A4_APPROVE extends AbstractPfAction<AggWrVO>
/*    */ {
/*    */   protected CompareAroundProcesser<AggWrVO> getCompareAroundProcesserWithRules(Object userObj)
/*    */   {
/* 31 */     CompareAroundProcesser processer = new CompareAroundProcesser(WrPluginPoint.APPROVE);
/*    */ 	//2023-08-30药物科技需求
			//生产报告审批前校验相关材料出库单签字状态
			processer.addBeforeRule(new BeforeApproveProduceRule());
/*    */ 
/*    */ 
/*    */ 
/* 36 */     return processer;
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AggWrVO[] processBP(Object userObj, AggWrVO[] clientFullVOs, AggWrVO[] originBills)
/*    */   {
/*    */     try
/*    */     {		
/* 61 */       AggWrVO[] vo = new WrFlowAutoFlushBP().auditAndBackFlush((AggWrVO[])Arrays.asList(clientFullVOs).toArray(new AggWrVO[0]));
/* 63 */       return vo;
/*    */     }
/*    */     catch (BusinessException e) {
/* 66 */       ExceptionUtils.wrappException(e);
/*    */     }
/* 68 */     return null;
/*    */   }
/*    */ }

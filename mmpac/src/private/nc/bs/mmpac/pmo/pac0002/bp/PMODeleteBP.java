/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.bs.mmpac.pmo.pac0002.bp;
/*     */ 
/*     */ import nc.bs.mmpac.pmo.pac0002.pluginpoint.PMOPluginPoint;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMOATPUpdateWithParaRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMOCffileidDeleteRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMODeleteAutoDeleteMosRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMODeleteAutoDeletePutPlanRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMODeleteBatchCodeRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMODeletePickmRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMODeleteSNRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.PMOMarkWrSNWhenDeleteRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.check.PMOCheckDeleteHaveSubRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.check.PMOCheckDeleteStatusRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.check.PMOCheckSnBindRule;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.check.PMOCheckTurnedDeleteRule;
import nc.bs.mmpac.pmo.pac0002.rule.check.PMOCheckinspcetiontoLims;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4INVP;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4MPS;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4PMO;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4PSCRecive;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4PSM;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4Renovate;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4SFC;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4SO;
/*     */ import nc.bs.mmpac.pmo.pac0002.rule.rewrite.RewriteDeleteNum4TO;
/*     */ import nc.bs.pubapp.pub.rule.ReturnBillCodeRule;
/*     */ import nc.impl.pubapp.pattern.rule.IRule;
/*     */ import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
/*     */ import nc.util.mmf.framework.gc.GCDeleteBPTemplate;
/*     */ import nc.vo.mmpac.pmo.pac0002.entity.PMOAggVO;
/*     */ 
/*     */ 
/*     */ public class PMODeleteBP
/*     */ {
/*     */   public void delete(PMOAggVO[] bills)
/*     */   {
/*  37 */     GCDeleteBPTemplate bp = new GCDeleteBPTemplate(PMOPluginPoint.DELETE);
/*     */ 
/*  39 */     addBeforeRule(bp.getAroundProcesser());
/*     */ 
/*  41 */     addAfterRule(bp.getAroundProcesser());
/*     */ 
/*  43 */     bp.delete(bills);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @SuppressWarnings("unchecked")
			private void addBeforeRule(AroundProcesser<PMOAggVO> processer)
/*     */   {
/*  52 */     IRule statusRule = new PMOCheckDeleteStatusRule();
/*  53 */     processer.addBeforeRule(statusRule);
/*     */ 
/*  55 */     IRule checkturnrule = new PMOCheckTurnedDeleteRule();
/*  56 */     processer.addBeforeRule(checkturnrule);
/*     */ 
/*  58 */     IRule snBindRule = new PMOCheckSnBindRule();
/*  59 */     processer.addBeforeRule(snBindRule);
/*     */ 
/*  61 */     IRule checkFactoryrule = new PMOATPUpdateWithParaRule(true, "55A2");
/*  62 */     processer.addBeforeRule(checkFactoryrule);
/*     */ 
/*     */ 
/*  65 */     IRule rewritePSCRecive = new RewriteDeleteNum4PSCRecive();
/*  66 */     processer.addBeforeRule(rewritePSCRecive);
/*     */ 
/*     */ 
/*  69 */     IRule rewritewr = new RewriteDeleteNum4Renovate();
/*  70 */     processer.addBeforeRule(rewritewr);
/*     */ 
/*  72 */     IRule rewriteso = new RewriteDeleteNum4SO();
/*  73 */     processer.addBeforeRule(rewriteso);
/*     */ 
/*  75 */     IRule rewritepsm = new RewriteDeleteNum4PSM();
/*  76 */     processer.addBeforeRule(rewritepsm);
/*     */ 
/*  78 */     IRule rewritepo = new RewriteDeleteNum4MPS();
/*  79 */     processer.addBeforeRule(rewritepo);
/*     */ 
/*  81 */     IRule rewriteinvp = new RewriteDeleteNum4INVP();
/*  82 */     processer.addBeforeRule(rewriteinvp);
/*     */ 
/*  84 */     IRule rewriteto = new RewriteDeleteNum4TO();
/*  85 */     processer.addBeforeRule(rewriteto);
/*     */ 
/*  87 */     IRule rewritesfc = new RewriteDeleteNum4SFC();
/*  88 */     processer.addBeforeRule(rewritesfc);
				
				/*报检至LIMS后无法删除 @hew 2022-08-18 For Raybow_SZ*/
			  IRule CheckInspection =new PMOCheckinspcetiontoLims();
			  processer.addBeforeRule(CheckInspection);


/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addAfterRule(AroundProcesser<PMOAggVO> processer)
/*     */   {
/* 101 */     IRule checksubbill = new PMOCheckDeleteHaveSubRule();
/* 102 */     processer.addAfterRule(checksubbill);
/*     */ 
/* 104 */     IRule pmorule = new RewriteDeleteNum4PMO();
/* 105 */     processer.addAfterRule(pmorule);
/*     */ 
/* 107 */     IRule deletePickmRule = new PMODeletePickmRule();
/* 108 */     processer.addAfterRule(deletePickmRule);
/*     */ 
/* 110 */     IRule deletebatchbill = new PMODeleteBatchCodeRule();
/* 111 */     processer.addAfterRule(deletebatchbill);
/*     */ 
/* 113 */     IRule autoDeletePutPlan = new PMODeleteAutoDeletePutPlanRule();
/* 114 */     processer.addAfterRule(autoDeletePutPlan);
/*     */ 
/* 116 */     IRule autoDeleteMos = new PMODeleteAutoDeleteMosRule();
/* 117 */     processer.addAfterRule(autoDeleteMos);
/*     */ 
/* 119 */     IRule snDeleteRule = new PMODeleteSNRule();
/* 120 */     processer.addAfterRule(snDeleteRule);
/*     */ 
/* 122 */     IRule cffileidRule = new PMOCffileidDeleteRule();
/* 123 */     processer.addAfterRule(cffileidRule);
/*     */ 
/* 125 */     IRule wrMarkRule = new PMOMarkWrSNWhenDeleteRule();
/* 126 */     processer.addAfterRule(wrMarkRule);
/*     */ 
/*     */ 
/* 129 */     IRule checkFactoryrule = new PMOATPUpdateWithParaRule(false, "55A2");
/* 130 */     processer.addAfterRule(checkFactoryrule);
/*     */ 
/*     */ 
/* 133 */     IRule billcoderule = new ReturnBillCodeRule("55A2", "vbillcode", "pk_group", "pk_org");
/*     */ 
/* 135 */     processer.addAfterRule(billcoderule);
/*     */   }
/*     */ }

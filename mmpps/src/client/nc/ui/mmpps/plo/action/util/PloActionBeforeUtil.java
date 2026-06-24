/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.ui.mmpps.plo.action.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ import nc.ui.mmpps.plo.model.PloManageAppModel;
/*     */ import nc.ui.pub.beans.MessageDialog;
/*     */ import nc.ui.pubapp.pub.power.PowerCheckUtils;
/*     */ import nc.ui.pubapp.uif2app.model.BillManageModel;
/*     */ import nc.util.mmf.framework.base.MMValueCheck;
/*     */ import nc.vo.ml.AbstractNCLangRes;
/*     */ import nc.vo.ml.NCLangRes4VoTransl;
/*     */ import nc.vo.mmpps.mpm.adapter.UAPOrgAdapter;
/*     */ import nc.vo.mmpps.mpm.res.MpmRes;
/*     */ import nc.vo.mmpps.mps0202.AggregatedPoVO;
/*     */ import nc.vo.mmpps.mps0202.PoVO;
/*     */ import nc.vo.uif2.LoginContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PloActionBeforeUtil
/*     */ {
/*     */   public AggregatedPoVO[] doActionBefore(Object[] obj, BillManageModel model)
/*     */   {
/*  34 */     PoVO[] allPloVOs = PoActionsUtil.convertObjects2PloVOs(obj);
/*     */ 
/*  36 */     allPloVOs = PoActionsUtil.getReleasablePloVOs(allPloVOs);
/*     */ 
/*  38 */     if ((allPloVOs == null) || (allPloVOs.length == 0)) {
/*  39 */       MessageDialog.showHintDlg(model.getContext().getEntranceUI(), MpmRes.getPointOutMsg(), NCLangRes4VoTransl.getNCLangRes().getStrByID("50010006_0", "050010006-0044"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */       return null;
/*     */     }
/*     */ 
/*  47 */     AggregatedPoVO[] aggs = AggregatedPoVO.constructAggPoVOs(allPloVOs);
/*  48 */     PowerCheckUtils.checkHasPermission(aggs, "55B4", "release", "vbillcode");
/*     */ 
/*  50 */     return aggs;
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
/*     */ 
/*     */   public AggregatedPoVO[] doBusiActionBefore(Object[] obj, int billStatus, PloManageAppModel model, String message)
/*     */   {
/*  64 */     AggregatedPoVO[] aggs = PoActionsUtil.convertObj2AggVO(obj);
/*     */ 
/*  66 */     aggs = PoActionsUtil.getFilterAggs(aggs, billStatus);
/*     */ 
/*  68 */     if (MMValueCheck.isEmpty(aggs)) {
/*  69 */       MessageDialog.showHintDlg(model.getContext().getEntranceUI(), MpmRes.getPointOutMsg(), message);
/*  70 */       return null;
/*     */     }
/*     */ 
/*  73 */     PowerCheckUtils.checkHasPermission(aggs, "55B4", "release", "vbillcode");
/*     */ 
/*  75 */     return aggs;
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
/*     */ 
/*     */   public AggregatedPoVO[] doEditsActionBefore(Object[] obj, String modifyKey, Object modifyValue)
/*     */   {
/*  89 */     if ((MMValueCheck.isEmpty(obj)) || (MMValueCheck.isEmpty(modifyKey)) || (MMValueCheck.isEmpty(modifyValue))) {
/*  90 */       return null;
/*     */     }
/*     */ 
/*  93 */     PoVO[] pos = PoActionsUtil.convertObjects2PloVOs(obj);
/*  94 */     for (PoVO po : pos) {
/*  95 */       po.setAttributeValue(modifyKey, modifyValue);
/*     */ 
/*  97 */       if (modifyKey.equals("cproddeptvid")) {
/*  98 */         Map dept = UAPOrgAdapter.getLastVIDSByDeptIDS(new String[] { modifyValue.toString() });
/*     */ 
/*     */ 
/* 101 */         if (!(MMValueCheck.isEmpty(dept))) {
/* 102 */           po.setCproddeptvid((String)dept.get(modifyValue.toString()));
					po.setCproddeptid(modifyValue.toString());
/*     */         }
/*     */       }
/*     */     }
/* 106 */     AggregatedPoVO[] aggvos = AggregatedPoVO.constructAggPoVOs(pos);
/*     */ 
/* 108 */     return aggvos;
/*     */   }
/*     */ }

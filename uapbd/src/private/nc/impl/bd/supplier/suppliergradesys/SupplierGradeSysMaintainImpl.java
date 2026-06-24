/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.impl.bd.supplier.suppliergradesys;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import nc.bs.bd.bp.utils.MDQueryUtil;
/*     */ import nc.bs.bd.service.ValueObjWithErrLog;
/*     */ import nc.bs.bd.supplier.suppliergradesys.bp.SupplierGradeSysDeleteBP;
/*     */ import nc.bs.bd.supplier.suppliergradesys.bp.SupplierGradeSysDisableBP;
/*     */ import nc.bs.bd.supplier.suppliergradesys.bp.SupplierGradeSysEnableBP;
/*     */ import nc.bs.bd.supplier.suppliergradesys.bp.SupplierGradeSysInsertBP;
/*     */ import nc.bs.bd.supplier.suppliergradesys.bp.SupplierGradeSysUpdateBP;
/*     */ import nc.itf.bd.supplier.suppliergradesys.ISupplierGradeSysMaintain;
/*     */ import nc.md.persist.framework.IMDPersistenceQueryService;
/*     */ import nc.md.persist.framework.MDPersistenceService;
/*     */ import nc.ui.bd.querytemplate.BDQuerySchemeFactory;
/*     */ import nc.ui.querytemplate.querytree.IQueryScheme;
/*     */ import nc.vo.bd.supplier.suppliergradesys.SupplierGradeSysVO;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pubapp.pattern.exception.ExceptionUtils;
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
/*     */ public class SupplierGradeSysMaintainImpl
/*     */   implements ISupplierGradeSysMaintain
/*     */ {
/*     */   public SupplierGradeSysVO[] insert(SupplierGradeSysVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/*  40 */       SupplierGradeSysInsertBP action = new SupplierGradeSysInsertBP();
/*  41 */       SupplierGradeSysVO[] retvos = action.insert(vos);
/*     */ 
/*  43 */       return retvos;
/*     */     }
/*     */     catch (Exception e) {
/*  46 */       ExceptionUtils.marsh(e);
/*     */     }
/*  48 */     return null;
/*     */   }
/*     */ 
/*     */   public void delete(SupplierGradeSysVO[] vos) throws BusinessException
/*     */   {
/*     */     try {
/*  54 */       SupplierGradeSysDeleteBP deleteBP = new SupplierGradeSysDeleteBP();
/*  55 */       deleteBP.delete(vos);
/*     */     }
/*     */     catch (Exception e) {
/*  58 */       ExceptionUtils.marsh(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SupplierGradeSysVO[] update(SupplierGradeSysVO[] vos) throws BusinessException
/*     */   {
/*     */     try {
/*  65 */       SupplierGradeSysVO[] oldVos = (SupplierGradeSysVO[])(SupplierGradeSysVO[])MDQueryUtil.lockValidateToRetrieveVO(vos);
/*     */ 
/*     */ 
/*  68 */       SupplierGradeSysUpdateBP bp = new SupplierGradeSysUpdateBP();
/*  69 */       SupplierGradeSysVO[] retVos = bp.update(vos, oldVos);
/*  70 */       return retVos;
/*     */     }
/*     */     catch (Exception e) {
/*  73 */       ExceptionUtils.marsh(e);
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public SupplierGradeSysVO[] query(IQueryScheme queryScheme) throws BusinessException
/*     */   {
/*     */     try
/*     */     {
				//20220513 za.y 调整供应商等级体系查询查询条件中默认组织为集团改为可先分子公司 
//				Collection c = MDPersistenceService.lookupPersistenceQueryService().queryBillOfVOByCondWithOrder(SupplierGradeSysVO.class, BDQuerySchemeFactory.getWhereSQL(queryScheme) + getOrgCondition(queryScheme), true, false, new String[] { "code" });
				Collection c = MDPersistenceService.lookupPersistenceQueryService().queryBillOfVOByCondWithOrder(SupplierGradeSysVO.class, BDQuerySchemeFactory.getWhereSQL(queryScheme) , true, false, new String[] { "code" });
				//20220513 za.y 调整供应商等级体系查询查询条件中默认组织为集团改为可先分子公司 end
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */       return ((SupplierGradeSysVO[])c.toArray(new SupplierGradeSysVO[0]));
/*     */     }
/*     */     catch (Exception e) {
/*  92 */       ExceptionUtils.marsh(e);
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   public ValueObjWithErrLog enableSupplierGradeSysVO(SupplierGradeSysVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/* 102 */       SupplierGradeSysVO[] oldVos = (SupplierGradeSysVO[])(SupplierGradeSysVO[])MDQueryUtil.lockValidateToRetrieveVOByFields(vos, new String[] { "enablestate" });
/*     */ 
/*     */ 
/*     */ 
/* 106 */       SupplierGradeSysEnableBP action = new SupplierGradeSysEnableBP();
/* 107 */       return action.enable(vos, oldVos);
/*     */     }
/*     */     catch (Exception e) {
/* 110 */       ExceptionUtils.marsh(e);
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public ValueObjWithErrLog disableSupplierGradeSysVO(SupplierGradeSysVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/* 120 */       SupplierGradeSysDisableBP action = new SupplierGradeSysDisableBP();
/* 121 */       return action.disable(vos);
/*     */     }
/*     */     catch (Exception e) {
/* 124 */       ExceptionUtils.marsh(e);
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   private String getOrgCondition(IQueryScheme queryScheme) {
/* 130 */     return " and pk_org = '" + queryScheme.get("pk_group") + "'";
/*     */   }
/*     */ }

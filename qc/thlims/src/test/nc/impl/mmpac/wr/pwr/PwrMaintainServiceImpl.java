/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.impl.mmpac.wr.pwr;
/*     */ 
/*     */ import nc.bs.mmpac.wr.bp.WrApproveBP;
import nc.bs.mmpac.wr.bp.WrBrBP;
import nc.bs.mmpac.wr.bp.WrDeleteBP;
import nc.bs.mmpac.wr.bp.WrInsertBP;
import nc.bs.mmpac.wr.bp.WrUnApproveBP;
import nc.bs.mmpac.wr.bp.WrUpdateBP;
import nc.bs.mmpac.wr.util.WrExceptionUtil;
import nc.bs.mmpac.wr.util.WrVOStatusUtil;
import nc.itf.mmpac.wr.pwr.IPwrMaintainService;
import nc.util.mmf.framework.gc.GCBillTransferTool;
import nc.vo.mmpac.putplan.entity.PutPlanVO;
import nc.vo.mmpac.wr.entity.AggWrVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PwrMaintainServiceImpl
/*     */   implements IPwrMaintainService
/*     */ {
/*     */   public AggWrVO[] brForWr(PutPlanVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/*  31 */       WrBrBP bp = new WrBrBP();
/*  32 */       return bp.brForWr(vos);
/*     */     }
/*     */     catch (Exception e) {
/*  35 */       ExceptionUtils.marsh(e);
/*     */     }
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */   public AggWrVO[] audit(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/*  45 */       GCBillTransferTool transTool = new GCBillTransferTool(vos);
/*     */ 
/*  47 */       AggWrVO[] fullBills = (AggWrVO[])transTool.getClientFullInfoBill();
/*     */ 
/*  49 */       AggWrVO[] originBills = (AggWrVO[])transTool.getOriginBills();
/*  50 */       WrApproveBP bp = new WrApproveBP();
/*  51 */       return bp.audit(fullBills, originBills);
/*     */     }
/*     */     catch (Exception e) {
/*  54 */       ExceptionUtils.marsh(e);
/*     */     }
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   public AggWrVO[] unAudit(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/*  65 */       new WrVOStatusUtil().setAggWrVOStatus(vos, 0);
/*     */ 
/*  67 */       GCBillTransferTool transTool = new GCBillTransferTool(vos);
/*     */ 
/*  69 */       AggWrVO[] fullBills = (AggWrVO[])transTool.getClientFullInfoBill();
/*     */ 
/*  71 */       AggWrVO[] originBills = (AggWrVO[])transTool.getOriginBills();
/*  72 */       WrUnApproveBP bp = new WrUnApproveBP();
/*     */ 
/*  74 */       return bp.unAudit(fullBills, originBills, 0);
/*     */     }
/*     */     catch (Exception e) {
/*  77 */       ExceptionUtils.marsh(e);
/*     */     }
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AggWrVO[] insert(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/*  90 */        AggWrVO[] fullBills = new WrInsertBP().insert(vos);

				//2023-07-27 liyf 根据生产批次自定义项,更新库存批次自定义项目
				new WrICBatchcodeRule().dealIcBatchcodeUserdef(fullBills);
				
				return fullBills;
/*     */     }
/*     */     catch (Exception e) {
/*  93 */       ExceptionUtils.marsh(e);
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public void delete(AggWrVO[] vos) throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/* 102 */       GCBillTransferTool transferTool = new GCBillTransferTool(vos);
/*     */ 
/* 104 */       AggWrVO[] fullBills = (AggWrVO[])transferTool.getClientFullInfoBill();
/* 105 */       WrDeleteBP bp = new WrDeleteBP();
/* 106 */       bp.delete(fullBills);
/*     */     }
/*     */     catch (Exception e) {
/* 109 */       WrExceptionUtil.marsh(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AggWrVO[] update(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/* 116 */     GCBillTransferTool transTool = new GCBillTransferTool(vos);
/*     */ 
/*     */ 
/* 119 */     AggWrVO[] fullBills = (AggWrVO[])transTool.getClientFullInfoBill();
/*     */ 
/* 121 */     AggWrVO[] originBills = (AggWrVO[])transTool.getOriginBills();
/*     */     try {
/* 123 */       WrUpdateBP bp = new WrUpdateBP();
/* 124 */       return bp.update(fullBills, originBills);
/*     */     }
/*     */     catch (Exception e) {
/* 127 */       WrExceptionUtil.marsh(e);
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public AggWrVO[] insert_RequiresNew(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/* 137 */       GCBillTransferTool transferTool = new GCBillTransferTool(vos);
/*     */ 
/* 139 */       AggWrVO[] mergedVOs = (AggWrVO[])transferTool.getClientFullInfoBill();
/* 140 */       WrInsertBP bp = new WrInsertBP();
/* 141 */        AggWrVO[] fullBills = bp.insert(mergedVOs);
				//2023-07-27 liyf 根据生产批次自定义项,更新库存批次自定义项目
				new WrICBatchcodeRule().dealIcBatchcodeUserdef(fullBills);
				return fullBills;
/*     */     }
/*     */     catch (Exception e) {
/* 144 */       ExceptionUtils.marsh(e);
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */ 
/*     */   public AggWrVO[] update_RequiresNew(AggWrVO[] vos)
/*     */     throws BusinessException
/*     */   {
/* 152 */     GCBillTransferTool transTool = new GCBillTransferTool(vos);
/*     */ 
/* 154 */     AggWrVO[] fullBills = (AggWrVO[])transTool.getClientFullInfoBill();
/*     */ 
/* 156 */     AggWrVO[] originBills = (AggWrVO[])transTool.getOriginBills();
/*     */     try {
/* 158 */       WrUpdateBP bp = new WrUpdateBP();
/* 159 */       fullBills =  bp.update(fullBills, originBills);

				//2023-07-27 liyf 根据生产批次自定义项,更新库存批次自定义项目
				new WrICBatchcodeRule().dealIcBatchcodeUserdef(fullBills);
				return fullBills;
/*     */     }
/*     */     catch (Exception e) {
/* 162 */       WrExceptionUtil.marsh(e);
/*     */     }
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   public AggWrVO[] audit_RequiresNew(AggWrVO[] vos, boolean isAudit)
/*     */     throws BusinessException
/*     */   {
/*     */     try
/*     */     {
/* 172 */       new WrVOStatusUtil().setAggWrVOStatus(vos, 0);
/*     */ 
/* 174 */       GCBillTransferTool transTool = new GCBillTransferTool(vos);
/*     */ 
/* 176 */       AggWrVO[] fullBills = (AggWrVO[])transTool.getClientFullInfoBill();
/*     */ 
/* 178 */       AggWrVO[] originBills = (AggWrVO[])transTool.getOriginBills();
/* 179 */       WrApproveBP bp = new WrApproveBP();
/* 180 */       return bp.audit(fullBills, originBills);
/*     */     }
/*     */     catch (Exception e) {
/* 183 */       ExceptionUtils.marsh(e);
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ }

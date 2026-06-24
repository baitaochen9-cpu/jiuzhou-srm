package nc.ui.jzqc.labelcontrol.action.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;

import nc.buzimsg.model.ReceiverConfModelDataManager;
/*     */ import nc.buzimsg.util.BuzimsgUtil;
/*     */ import nc.buzimsg.util.MsgresRegServiceFetcher;
/*     */ import nc.buzimsg.vo.MsgresRcvConfVO;
/*     */ import nc.desktop.ui.WorkbenchEnvironment;
/*     */ import nc.ui.uif2.model.BatchBillTableModel;
/*     */ import nc.ui.uif2.model.IAppModelDataManagerEx;
/*     */ import nc.vo.pub.BusinessException;

/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReceiverConfModelDataManager1 extends ReceiverConfModelDataManager implements IAppModelDataManagerEx
/*     */ {
/*     */   private BatchBillTableModel receiverConfModel;
/*     */   
/*     */   public void setMsgtempConfModel(BatchBillTableModel receiverConfModel)
/*     */   {
/*  25 */     this.receiverConfModel = receiverConfModel;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initModel()
/*     */   {
/*  31 */    initModelBySqlWhere("labelcontrol");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refresh() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void initModelBySqlWhere(String sqlWhere)
/*     */   {
/*  43 */     MsgresRcvConfVO[] vos = null;
/*     */     
/*  45 */     if (StringUtils.isEmpty(sqlWhere))
/*     */     {
/*  47 */       vos = null;
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/*  53 */         vos = MsgresRegServiceFetcher.getQueryService().queryMsgresRcvConfVOByCode(sqlWhere, WorkbenchEnvironment.getInstance().getGroupVO().getPk_group());
/*  54 */         vos = filterData(vos);
/*     */       }
/*     */       catch (BusinessException e)
/*     */       {
/*  58 */         nc.bs.logging.Logger.error("Attempt to fetch MsgresRcvConfVO failed!", e);
/*     */       }
/*     */     }
/*     */     
/*  62 */     receiverConfModel.initModel(vos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setShowSealDataFlag(boolean showSealDataFlag) {}
/*     */   
/*     */ 
/*     */ 
/*     */   private MsgresRcvConfVO[] filterData(MsgresRcvConfVO[] vos)
/*     */   {
/*  73 */     if ((ArrayUtils.isEmpty(vos)) || (BuzimsgUtil.isLoginUserGroupAdm()))
/*     */     {
/*  75 */       return vos;
/*     */     }
/*     */     
/*  78 */     String[] pkorgs = BuzimsgUtil.getUserPermissionPkorgs();
/*  79 */     if (ArrayUtils.isEmpty(pkorgs))
/*     */     {
/*  81 */       return new MsgresRcvConfVO[0];
/*     */     }
/*     */     
/*  84 */     List<MsgresRcvConfVO> voList = new ArrayList();
/*  85 */     Map<String, String> map = array2Map(pkorgs);
/*     */     
/*  87 */     for (MsgresRcvConfVO confVO : vos)
/*     */     {
/*  89 */       String pk_org = confVO.getPk_org();
/*  90 */       if (map.containsKey(pk_org))
/*     */       {
/*  92 */         voList.add(confVO);
/*     */       }
/*     */     }
/*     */     
/*  96 */     return (MsgresRcvConfVO[])voList.toArray(new MsgresRcvConfVO[0]);
/*     */   }
/*     */   
/*     */   private Map<String, String> array2Map(String[] pkorgs)
/*     */   {
/* 101 */     String pk_group = WorkbenchEnvironment.getInstance().getGroupVO().getPk_group();
/* 102 */     Map<String, String> map = new HashMap();
/* 103 */     for (int i = 0; i < pkorgs.length; i++)
/*     */     {
/* 105 */       map.put(pkorgs[i], pkorgs[i]);
/*     */     }
/* 107 */     map.put(pk_group, pk_group);
/* 108 */     return map;
/*     */   }
/*     */ }

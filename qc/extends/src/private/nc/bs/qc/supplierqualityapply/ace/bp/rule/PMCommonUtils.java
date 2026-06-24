package nc.bs.qc.supplierqualityapply.ace.bp.rule;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import nc.bs.logging.Logger;
/*     */ import nc.impl.am.db.VOPersistUtil;
/*     */ import nc.itf.ewm.pub.IRepairPlanPubService;
/*     */ import nc.itf.ewm.pub.IWorkOrderQueryService;
/*     */ import nc.vo.am.common.util.ArrayConstructor;
/*     */ import nc.vo.am.common.util.ArrayUtils;
/*     */ import nc.vo.am.common.util.BaseVOUtils;
/*     */ import nc.vo.am.proxy.AMProxy;
/*     */ import nc.vo.am.pub.uap.ModuleInfoQuery;
/*     */ import nc.vo.emm.premaintain.PMBillVO;
/*     */ import nc.vo.emm.premaintain.PMHeadVO;
/*     */ import nc.vo.emm.premaintain.PMResultVO;
/*     */ import nc.vo.pub.BusinessException;
/*     */ import nc.vo.pub.SuperVO;
/*     */ import nc.vo.pub.lang.UFDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PMCommonUtils
/*     */ {
/*     */   public static final int DAY = 0;
/*     */   public static final int WEEK = 1;
/*     */   public static final int MONTH = 2;
/*     */   public static final int QUARTER = 3;
/*     */   public static final int YEAR = 4;
/*     */   
/*     */   public static String countNextStdJob(PMBillVO vo, Integer counter)
/*     */   {
/*  75 */     PMHeadVO headVO = vo.getParentVO();
/*  76 */     headVO.setNext_std_job(null);
/*     */     try
/*     */     {
/*  79 */       BaseVOUtils.retrieveBodyVOsByTabs(vo, new String[] { "pm_stdjob" });
/*     */     }
/*     */     catch (BusinessException e) {
/*  82 */       Logger.error(e.getMessage(), e);
/*     */     }
/*     */     
/*  85 */     SuperVO[] stdJobVOs = vo.getTableVO("pm_stdjob");
/*  86 */     if (ArrayUtils.isEmpty(stdJobVOs)) {
/*  87 */       return null;
/*     */     }
/*     */     
/*  90 */     int resultFrequncy = 0;
/*  91 */     String resultPkStdJob = null;
/*  92 */     for (SuperVO stdJobVO : stdJobVOs)
/*     */     {
/*  94 */       boolean deleteStatus = 3 == stdJobVO.getStatus();
/*  95 */       Integer frequency = (Integer)stdJobVO.getAttributeValue("frequence_num");
/*  96 */       boolean nullFrequency = (null == frequency) || (0 == frequency.intValue());
/*  97 */       if ((!deleteStatus) && (!nullFrequency))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 102 */         boolean divided = (counter.intValue() + 1) % frequency.intValue() == 0;
/* 103 */         boolean bigger = frequency.intValue() > resultFrequncy;
/* 104 */         if ((divided) && (bigger)) {
/* 105 */           resultFrequncy = frequency.intValue();
/* 106 */           resultPkStdJob = (String)stdJobVO.getAttributeValue("pk_std_job");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 111 */     headVO.setNext_std_job(resultPkStdJob);
/* 112 */     return resultPkStdJob;
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
/*     */ 
/*     */   private static UFDate addMonths(int i, UFDate ufdate)
/*     */   {
/* 127 */     Date date = ufdate.toDate();
/*     */     
/* 129 */     GregorianCalendar gregoriancalendar = new GregorianCalendar();
/* 130 */     gregoriancalendar.setTime(date);
/*     */     
/* 132 */     int days = gregoriancalendar.get(5);
/*     */     
/* 134 */     gregoriancalendar.set(5, 1);
/*     */     
/* 136 */     gregoriancalendar.add(2, i);
/*     */     
/* 138 */     int months_new = gregoriancalendar.get(2);
/*     */     
/* 140 */     gregoriancalendar.add(5, days - 1);
/*     */     
/* 142 */     while (gregoriancalendar.get(2) != months_new) {
/* 143 */       gregoriancalendar.add(5, -1);
/*     */     }
/*     */     
/* 146 */     return new UFDate(gregoriancalendar.getTime());
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
/*     */   private static UFDate addDays(int i, UFDate ufdate)
/*     */   {
/* 159 */     Date date = ufdate.toDate();
/* 160 */     Calendar calendar = Calendar.getInstance();
/* 161 */     calendar.setTime(date);
/*     */     
/* 163 */     calendar.add(5, i);
/* 164 */     return new UFDate(calendar.getTime());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private static UFDate addYears(int i, UFDate ufdate)
/*     */   {
/* 181 */     Date date = ufdate.toDate();
/* 182 */     GregorianCalendar gregoriancalendar = new GregorianCalendar();
/* 183 */     gregoriancalendar.setTime(date);
/* 184 */     boolean flag = (gregoriancalendar.get(5) == 29) && (gregoriancalendar.get(2) == 2);
/*     */     
/* 186 */     gregoriancalendar.add(1, i);
/* 187 */     if ((flag) && (gregoriancalendar.get(5) != 29))
/* 188 */       gregoriancalendar.add(5, -1);
/* 189 */     return new UFDate(gregoriancalendar.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UFDate getDateByFre(int i, int type, UFDate ufdate)
/*     */   {
/* 201 */     if (type == 0)
/* 202 */       return addDays(i, ufdate);
/* 203 */     if (type == 1)
/* 204 */       return addDays(i * 7, ufdate);
/* 205 */     if (type == 2)
/* 206 */       return addMonths(i, ufdate);
/* 207 */     if (type == 3)
/* 208 */       return addMonths(i * 3, ufdate);
/* 209 */     if (type == 4) {
/* 210 */       return addYears(i, ufdate);
/*     */     }
/* 212 */     return ufdate;
/*     */   }
/*     */ public static UFDate getDateByBefore(int i, int type, UFDate ufdate)
/*     */   {
/* 201 */     if (type == 0)
/* 202 */       return addDays(i*-1, ufdate);
/* 203 */     if (type == 1)
/* 204 */       return addDays(i * 7*-1, ufdate);
/* 205 */     if (type == 2)
/* 206 */       return addMonths(i*-1, ufdate);
/* 207 */     if (type == 3)
/* 208 */       return addMonths(i * 3*-1, ufdate);
/* 209 */     if (type == 4) {
/* 210 */       return addYears(i*-1, ufdate);
/*     */     }
/* 212 */     return ufdate;
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
/*     */ 
/*     */ 
/*     */   public static boolean checkMakeDownriverBill(PMBillVO... billVOs)
/*     */     throws BusinessException
/*     */   {
/* 229 */     if (!ModuleInfoQuery.isEWMEnabled()) {
/* 230 */       return false;
/*     */     }
/* 232 */     ArrayList<String> alPk = new ArrayList();
/* 233 */     for (PMBillVO billVO : billVOs) {
/* 234 */       PMHeadVO headVO = billVO.getParentVO();
/* 235 */       alPk.add(headVO.getPk_pm());
/*     */     }
/* 237 */     String[] pks = (String[])ArrayConstructor.getArray(alPk);
/*     */     
/*     */ 
/* 240 */     SuperVO[] workorderHeadVOs = ((IWorkOrderQueryService)AMProxy.lookup(IWorkOrderQueryService.class)).queryWorkOrderBySrcInfo(pks, billVOs[0].getParentVO().getBill_type());
/*     */     
/*     */ 
/* 243 */     if (ArrayUtils.isNotEmpty(workorderHeadVOs)) {
/* 244 */       return true;
/*     */     }
/*     */     
/* 247 */     String[] rpBodyPks = ((IRepairPlanPubService)AMProxy.lookup(IRepairPlanPubService.class)).queryBodyPksBySrcInfo(billVOs[0].getParentVO().getBill_type(), pks);
/*     */     
/*     */ 
/* 250 */     if (ArrayUtils.isNotEmpty(rpBodyPks)) {
/* 251 */       return true;
/*     */     }
/*     */     
/* 254 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void updatePMVOs(PMBillVO... pmbillVOs)
/*     */   {
/* 263 */     PMHeadVO[] headVOs = new PMHeadVO[pmbillVOs.length];
/* 264 */     ArrayList<PMResultVO> alResultVO = new ArrayList();
/*     */     
/* 266 */     for (int i = 0; i < pmbillVOs.length; i++) {
/* 267 */       headVOs[i] = pmbillVOs[i].getParentVO();
/* 268 */       headVOs[i].setStatus(1);
/* 269 */       SuperVO[] superBodyVOs = (SuperVO[])pmbillVOs[i].getChildren(PMResultVO.class);
/*     */       
/* 271 */       if (ArrayUtils.isNotEmpty(superBodyVOs)) {
/* 272 */         for (SuperVO superbody : superBodyVOs) {
/* 273 */           PMResultVO result = (PMResultVO)superbody;
/* 274 */           result.setStatus(1);
/* 275 */           alResultVO.add(result);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 280 */     VOPersistUtil.update(headVOs);
/* 281 */     if (alResultVO.size() > 0) {
/* 282 */       PMResultVO[] resultVOs = (PMResultVO[])alResultVO.toArray(new PMResultVO[0]);
/*     */       
/* 284 */       VOPersistUtil.update(resultVOs);
/*     */     }
/*     */   }
/*     */ }

/* Location:           D:\zhw\home0816\modules\emm\lib\pubemm_pmbase.jar
 * Qualified Name:     nc.vo.emm.premaintain.utils.PMCommonUtils
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
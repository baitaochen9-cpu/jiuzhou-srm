/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.bs.ia.bill.rule.base.pub;
/*     */ 
/*     */ import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.bs.ia.bill.pub.BillMsgUtil;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ia.bill.entity.base.AbstractBaseBill;
import nc.vo.ia.bill.entity.base.AbstractBaseHeadVO;
import nc.vo.ia.bill.entity.base.AbstractBaseItemVO;
import nc.vo.ia.pub.period.AccountPeriod;
import nc.vo.ia.pub.period.Calendar;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.bill.BillBodyChecker;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.log.TimeLog;
import nc.vo.pubapp.pattern.pub.PubAppTool;
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
/*     */ public class BasePubDataNullCheckRule<E extends AbstractBaseBill>
/*     */   implements IRule<E>
/*     */ {
/*     */   public void process(E[] bills)
/*     */   {
/*  42 */     TimeLog.logStart();
/*  43 */     checkNoChildren(bills);
/*  44 */     TimeLog.info("校验是否存在表体数据");
/*     */ 
/*  46 */     TimeLog.logStart();
/*  47 */     checkBill(bills);
/*  48 */     TimeLog.info("校验单据信息");
/*     */ 
/*  50 */     Map calendarMap = getCalendarMap(bills);
/*     */ 
/*  52 */     TimeLog.logStart();
/*  53 */     checkPeriod(bills, calendarMap);
/*  54 */     TimeLog.info("校验会计期间合法性");
/*     */   }
/*     */ 
/*     */   private void checkPeriod(E[] bills, Map<String, Calendar> calendarMap) {
/*  58 */     for (AbstractBaseBill bill : bills)
/*     */     {
/*  60 */       AbstractBaseHeadVO head = bill.getParentVO();
/*  61 */       String caccountperiod = head.getCaccountperiod();
/*  62 */       String pk_book = head.getPk_book();
/*  63 */       String pk_org = head.getPk_org();
/*     */ 
/*  65 */       String billtype = head.getMetaData().getEntityName();
/*  66 */       UFBoolean bsysflag = head.getBsystemflag();
/*     */ 
/*  68 */       if (billtype.endsWith("i0bill")) continue; if ((bsysflag != null) && (bsysflag.booleanValue())) {
/*     */         continue;
/*     */       }
/*     */ 
/*  72 */       Calendar calendar = (Calendar)calendarMap.get(createKey(pk_org, pk_book));
/*  73 */       AccountPeriod accPeriod = calendar.getPeriod(caccountperiod);
/*  74 */       if (accPeriod.compareTo(calendar.getFirstUnAccountedPeriod()) < 0) {
/*  75 */         String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0114");
/*     */ 
/*     */ 
/*  78 */         ExceptionUtils.wrappBusinessException(message);
/*     */       }
/*  80 */       if (accPeriod.isAccounted().booleanValue()) {
/*  81 */         String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0114");
/*     */ 
/*     */ 
/*  84 */         ExceptionUtils.wrappBusinessException(message);
/*     */       }
/*     */ 
/*  87 */       if (accPeriod.isClosed().booleanValue()) {
	// liyf 2023-8-20 苏州瑞博 入库调整单 调整验证批次 需要在关账后操作
	              if("调整验证批次".equalsIgnoreCase(head.getVnote())){
	            	  
	              }else{
	            	  /*  88 */         String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0115");
	            	  /*     */ 
	            	  /*     */ 
	            	  /*  91 */         ExceptionUtils.wrappBusinessException(message);
	              }

/*     */       }
/*  93 */       for (AbstractBaseItemVO item : bill.getChildrenVO())
/*     */       {
/*  95 */         if ((!(billtype.endsWith("ibbill"))) && (!(billtype.endsWith("ifbill"))) && (!(billtype.endsWith("iebill"))) && (!(billtype.endsWith("adjcalcrange"))))
/*     */ 
/*     */         {
/*  98 */           AccountPeriod bizPeriod = calendar.getPeriod(item.getDaccountdate());
/*     */ 
/* 100 */           if (bizPeriod.toString().compareTo(caccountperiod) > 0) {
/* 101 */             String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0116");
/*     */ 
/*     */ 
/* 104 */             ExceptionUtils.wrappBusinessException(message);
/*     */           }
/*     */         }
/*     */         else {
/* 108 */           AccountPeriod bizPeriod = calendar.getPeriod(item.getDbizdate());
/*     */ 
/* 110 */           if (bizPeriod.toString().compareTo(caccountperiod) > 0) {
/* 111 */             String message = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014002_0", "02014002-0116");
/*     */ 
/*     */ 
/* 114 */             ExceptionUtils.wrappBusinessException(message);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Calendar> getCalendarMap(E[] bills)
/*     */   {
/* 130 */     Map map = new HashMap();
/* 131 */     Set keySet = new HashSet();
/* 132 */     for (AbstractBaseBill bill : bills) {
/* 133 */       AbstractBaseHeadVO head = bill.getParentVO();
/* 134 */       String pk_org = head.getPk_org();
/* 135 */       String pk_book = head.getPk_book();
/*     */ 
/* 137 */       keySet.add(createKey(pk_org, pk_book));
/*     */     }
/* 139 */     String[] keys = (String[])keySet.toArray(new String[0]);
/* 140 */     for (String key : keys) {
/* 141 */       String[] pks = key.split("&");
/* 142 */       Calendar calendar = Calendar.getInstance(pks[0], pks[1]);
/* 143 */       map.put(key, calendar);
/*     */     }
/*     */ 
/* 146 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String createKey(String pk_org, String pk_book)
/*     */   {
/* 157 */     StringBuffer buffer = new StringBuffer();
/* 158 */     buffer.append(pk_org).append("&").append(pk_book);
/* 159 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   private void checkBill(E[] bills)
/*     */   {
/* 164 */     for (AbstractBaseBill bill : bills) {
/* 165 */       AbstractBaseHeadVO head = bill.getParentVO();
/*     */ 
/* 167 */       checkCostRegion(head);
/* 168 */       for (AbstractBaseItemVO item : bill.getChildrenVO())
/*     */       {
/* 170 */         checkInventory(head, item);
/*     */ 
/* 172 */         checkDbizdate(head, item);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkCostRegion(AbstractBaseHeadVO head) {
/* 178 */     String costregion = head.getPk_org();
/* 179 */     if (PubAppTool.isNull(costregion)) {
/* 180 */       String bizmsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014010_0", "02014010-0020");
/*     */ 
/*     */ 
/* 183 */       String billmsg = BillMsgUtil.getBillMessage(head);
/* 184 */       ExceptionUtils.wrappBusinessException(bizmsg + billmsg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkDbizdate(AbstractBaseHeadVO head, AbstractBaseItemVO item) {
/* 189 */     if (item.getDbizdate() == null) {
/* 190 */       String bizmsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014010_0", "02014010-0021");
/*     */ 
/*     */ 
/* 193 */       String billmsg = BillMsgUtil.getBillMessage(head, item);
/* 194 */       ExceptionUtils.wrappBusinessException(bizmsg + billmsg);
/*     */     }
/* 196 */     String billtype = head.getMetaData().getEntityName();
/* 197 */     if ((item.getDaccountdate() != null) || (billtype.endsWith("ibbill")) || (billtype.endsWith("ifbill")) || (billtype.endsWith("iebill")) || (billtype.endsWith("adjcalcrange"))) {
/*     */       return;
/*     */     }
/* 200 */     ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2014010_0", "02014010-0168"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkInventory(AbstractBaseHeadVO head, AbstractBaseItemVO item)
/*     */   {
/* 207 */     if (PubAppTool.isNull(item.getCinventoryid())) {
/* 208 */       String bizmsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("2014010_0", "02014010-0022");
/*     */ 
/*     */ 
/* 211 */       String billmsg = BillMsgUtil.getBillMessage(head, item);
/* 212 */       ExceptionUtils.wrappBusinessException(bizmsg + billmsg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkNoChildren(E[] bills) {
/* 217 */     BillBodyChecker checker = new BillBodyChecker();
/* 218 */     checker.checkNoChildren(bills);
/*     */   }
/*     */ }

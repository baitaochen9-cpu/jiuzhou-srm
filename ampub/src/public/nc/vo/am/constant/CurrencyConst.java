/*     */ package nc.vo.am.constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CurrencyConst
/*     */ {
/*     */   public static final int DEFAULT_DIGIT = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_EQUAL_RATE_DIGIT = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_ILLEGAL_RATE_DIGIT = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_RATE_DIGIT = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String ORG_POSTFIX = "_org";
/*     */   
/*     */ 
/*     */   public static final String GROUP_POSTFIX = "_group";
/*     */   
/*     */ 
/*     */   public static final String GLOBAL_POSTFIX = "_global";
/*     */   
/*     */ 
/*     */   public static final String EXCHANGEMODE_GROUP = "NC001";
/*     */   
/*     */ 
/*     */   public static final String EXCHANGEMODE_GLOBE = "NC002";
/*     */   
/*     */ 
/*     */   public static final String CACBYORG = "基于组织本位币计算";
/*     */   
/*     */ 
/*     */   public static final String CACBYORIGIN = "基于原币计算";
/*     */   
/*     */ 
/*     */   public static final String GROUP_CACCLOSED = "不启用集团本位币";
/*     */   
/*     */ 
/*     */   public static final String GLOBAL_CACCLOSED = "不启用全局本位币";
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getOriginField(String moneyField)
/*     */   {
/*  54 */     if (moneyField.endsWith("_org")) {
/*  55 */       return moneyField.substring(0, moneyField.length() - "_org".length());
/*     */     }
/*  57 */     return moneyField;
/*     */   }
/*     */   
/*     */   public static String getOrgLocalField(String moneyField) {
/*  61 */     if (moneyField.endsWith("_org") || moneyField.startsWith("def")) {
/*  62 */       return moneyField;
/*     */     }
/*  64 */     return moneyField + "_org";
/*     */   }
/*     */   
/*     */   public static String getGroupLocalField(String moneyField) {
/*  68 */     if (moneyField.endsWith("_group")|| moneyField.startsWith("def"))
/*  69 */       return moneyField;
/*  70 */     if (moneyField.endsWith("_org")) {
/*  71 */       return moneyField.substring(0, moneyField.length() - 4) + "_group";
/*     */     }
/*  73 */     return moneyField + "_group";
/*     */   }
/*     */   
/*     */   public static String getGlobalLocalField(String moneyField) {
/*  77 */     if (moneyField.endsWith("_global")|| moneyField.startsWith("def"))
/*  78 */       return moneyField;
/*  79 */     if (moneyField.endsWith("_org")) {
/*  80 */       return moneyField.substring(0, moneyField.length() - 4) + "_global";
/*     */     }
/*  82 */     return moneyField + "_global";
/*     */   }
/*     */   
/*     */   public static String[] getMoneyFieldGroup(String moneyField) {
/*  86 */     String originField = moneyField;
/*     */     
/*  88 */     if (moneyField.endsWith("_org")) {
/*  89 */       originField = moneyField.substring(0, moneyField.length() - "_org".length());
/*  90 */     } else if (moneyField.endsWith("_group")) {
/*  91 */       originField = moneyField.substring(0, moneyField.length() - "_group".length());
/*  92 */     } else if (moneyField.endsWith("_global")) {
/*  93 */       originField = moneyField.substring(0, moneyField.length() - "_global".length());
/*     */     }
/*     */     
/*  96 */     String[] fieldGroup = new String[4];
/*  97 */     fieldGroup[0] = originField;
/*  98 */     fieldGroup[1] = getOrgLocalField(moneyField);
/*  99 */     fieldGroup[2] = getGroupLocalField(moneyField);
/* 100 */     fieldGroup[3] = getGlobalLocalField(moneyField);
/*     */     
/* 102 */     return fieldGroup;
/*     */   }
/*     */ }

/* Location:           E:\kf_zhw\home0816\modules\ampub\lib\pubampub_ampub.jar
 * Qualified Name:     nc.vo.am.constant.CurrencyConst
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
package nc.bs.jzyy.sys;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.sql.SQLException;

/*     */ import nc.vo.pub.BusinessException;
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
/*     */ public class StackPrint
/*     */ {
/*  19 */   private static StackPrint Instance = new StackPrint();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StackPrint getInstance()
/*     */   {
/*  31 */     return Instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String printStack(String message, Throwable ex)
/*     */   {
/*  42 */     StringWriter sw = new StringWriter();
/*  43 */     sw.write(defend(message));
/*  44 */     sw.write("\r\n");
/*  45 */     String stack = filter(sw.toString());
/*  46 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String printStack(Throwable ex)
/*     */   {
/*  56 */     StringWriter sw = new StringWriter();
/*  57 */     String stack = "";
/*  58 */     printStack(ex, sw);
/*  59 */     if ((ex instanceof BusinessException)) {
/*  60 */       stack = filter(sw.toString());
/*     */     } else {
/*  62 */       stack = sw.toString();
/*     */     }
/*  64 */     return stack;
/*     */   }
/*     */   
/*     */   private String defend(String str) {
/*  68 */     String ret = str;
/*  69 */     String para = "ORA-";
/*  70 */     ret = ret.replaceAll(para, "");
/*  71 */     return ret;
/*     */   }
/*     */   
/*     */   private String filter(String stack) {
/*  75 */     String[] strs = stack.split("\r\n\tat");
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     int length = strs.length;
/*  78 */     for (int i = 0; i < length - 1; i++) {
/*  79 */       String str = strs[i];
/*  80 */       if (!inFilterRange(str))
/*     */       {
/*     */ 
/*  83 */         sb.append(defend(strs[i]));
/*  84 */         sb.append("\r\n\tat");
/*     */       } }
/*  86 */     if (!inFilterRange(strs[(length - 1)])) {
/*  87 */       sb.append(strs[(length - 1)]);
/*     */     }
/*  89 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private boolean inFilterRange(String str) {
/*  93 */     String[] conditions = { ".navigation.", ".glue.web.", ".springframework.web.", "javax.servlet.", "nc.lightapp.base.lang.exception.StackPrint"};
/*     */     
/*     */ 
/*     */ 
/*  97 */     boolean flag = false;
/*  98 */     for (String condition : conditions) {
/*  99 */       if (str.indexOf(condition) >= 0) {
/* 100 */         flag = true;
/* 101 */         break;
/*     */       }
/*     */     }
/* 104 */     return flag;
/*     */   }
/*     */   
/*     */   private void printStack(Throwable ex, StringWriter sw) {
/* 108 */     PrintWriter pw = new PrintWriter(sw, true);
/* 109 */     Throwable ex2 = null;
/* 110 */     if ((ex instanceof SQLException)) {
/* 111 */       SQLException e1 = (SQLException)ex;
/* 112 */       ex2 = e1.getNextException();
/*     */     }
/* 114 */     ex.printStackTrace(pw);
/* 115 */     if (ex2 != null) {
/* 116 */       ex2.printStackTrace(pw);
/*     */     }
/*     */   }
/*     */ }

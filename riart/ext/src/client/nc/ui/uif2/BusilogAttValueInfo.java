package nc.ui.uif2;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import nc.md.model.impl.Attribute;
/*    */ 
/*    */ public class BusilogAttValueInfo
/*    */ {
/*    */   private String metaid;
/*    */   private String pk;
/*    */   
/*    */   public String getMetaid()
/*    */   {
/* 14 */     return metaid;
/*    */   }
/*    */   
/* 17 */   public void setMetaid(String metaid) { this.metaid = metaid; }
/*    */   
/*    */   public String getPk() {
/* 20 */     return pk;
/*    */   }
/*    */   
/* 23 */   public void setPk(String pk) { this.pk = pk; }
/*    */   
/*    */   public HashMap<Attribute, String> getMainmap() {
/* 26 */     if (mainmap == null) {
/* 27 */       mainmap = new HashMap();
/*    */     }
/* 29 */     return mainmap;
/*    */   }
/*    */   
/*    */ 
/*    */   public HashMap<String, List<BusilogAttValueInfo>> getSubmainmap()
/*    */   {
/* 35 */     if (submainmap == null)
/*    */     {
/* 37 */       submainmap = new HashMap();
/*    */     }
/* 39 */     return submainmap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */   private HashMap<Attribute, String> mainmap = new HashMap();
/* 47 */   private HashMap<String, List<BusilogAttValueInfo>> submainmap = new HashMap();
/*    */ }

/* Location:           D:\zhw\home0816\modules\baseapp\META-INF\lib\baseapp_applogsLevel-1.jar
 * Qualified Name:     nc.bs.busilog.BusilogAttValueInfo
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
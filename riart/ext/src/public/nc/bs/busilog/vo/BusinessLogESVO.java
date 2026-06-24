/*     */ package nc.bs.busilog.vo;
/*     */ 
/*     */ import nc.vo.pub.SuperVO;
/*     */ import nc.vo.pub.lang.UFDateTime;
/*     */ 
/*     */ 
/*     */ public class BusinessLogESVO
/*     */   extends SuperVO
/*     */ {
/**
	 * 
	 */
	private static final long serialVersionUID = -6665603106878006033L;
/*     */   private String typepk_busiobj;
/*     */   private String pk_busilog;
/*     */   private String busiobjcode;
/*     */   private String busiobjname;
/*     */   private String pk_busiobj;
/*     */   private String client;
/*     */   private UFDateTime logdate;
/*     */   private String logmsg;
/*     */   private String operateresult;
/*     */   private String orgpk_busiobj;
/*     */   private String pk_group;
/*     */   private String pk_operation;
/*     */   private String pk_user;
/*     */   private String tablename;
private String pk_hises;
/*     */   
/*     */   public String getTypepk_busiobj()
/*     */   {
/*  27 */     return typepk_busiobj;
/*     */   }
/*     */   
/*     */   public void setTypepk_busiobj(String typepk_busiobj) {
/*  31 */     this.typepk_busiobj = typepk_busiobj;
/*     */   }
/*     */   
/*     */   public String getPk_busilog() {
/*  35 */     return pk_busilog;
/*     */   }
/*     */   
/*     */   public void setPk_busilog(String pk_busilog) {
/*  39 */     this.pk_busilog = pk_busilog;
/*     */   }
/*     */   
/*     */   public String getBusiobjcode() {
/*  43 */     return busiobjcode;
/*     */   }
/*     */   
/*     */   public void setBusiobjcode(String busiobjcode) {
/*  47 */     this.busiobjcode = busiobjcode;
/*     */   }
/*     */   
/*     */   public String getBusiobjname() {
/*  51 */     return busiobjname;
/*     */   }
/*     */   
/*     */   public void setBusiobjname(String busiobjname) {
/*  55 */     this.busiobjname = busiobjname;
/*     */   }
/*     */   
/*     */   public String getClient() {
/*  59 */     return client;
/*     */   }
/*     */   
/*     */   public void setClient(String ip) {
/*  63 */     client = ip;
/*     */   }
/*     */   
/*     */   public UFDateTime getLogdate() {
/*  67 */     return logdate;
/*     */   }
/*     */   
/*     */   public void setLogdate(UFDateTime logdate) {
/*  71 */     this.logdate = logdate;
/*     */   }
/*     */   
/*     */   public String getLogmsg() {
/*  75 */     return logmsg;
/*     */   }
/*     */   
/*     */   public void setLogmsg(String logmsg) {
/*  79 */     this.logmsg = logmsg;
/*     */   }
/*     */   
/*     */   public String getOperateresult() {
/*  83 */     return operateresult;
/*     */   }
/*     */   
/*     */   public void setOperateresult(String operateresult) {
/*  87 */     this.operateresult = operateresult;
/*     */   }
/*     */   
/*     */   public String getOrgpk_busiobj() {
/*  91 */     return orgpk_busiobj;
/*     */   }
/*     */   
/*     */   public void setOrgpk_busiobj(String orgpk_busiobj) {
/*  95 */     this.orgpk_busiobj = orgpk_busiobj;
/*     */   }
/*     */   
/*     */   public String getPk_group() {
/*  99 */     return pk_group;
/*     */   }
/*     */   
/*     */   public void setPk_group(String pk_group) {
/* 103 */     this.pk_group = pk_group;
/*     */   }
/*     */   
/*     */   public String getPk_operation() {
/* 107 */     return pk_operation;
/*     */   }
/*     */   
/*     */   public void setPk_operation(String pk_operation) {
/* 111 */     this.pk_operation = pk_operation;
/*     */   }
/*     */   
/*     */   public String getPk_user() {
/* 115 */     return pk_user;
/*     */   }
/*     */   
/*     */   public void setPk_user(String pk_user) {
/* 119 */     this.pk_user = pk_user;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTablename(String tablename)
/*     */   {
/* 141 */     this.tablename = tablename;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPKFieldName()
/*     */   {
/* 153 */     return "pk_busilog";
/*     */   }
/*     */   
/*     */   public String getTableName()
/*     */   {
/* 158 */     if (tablename == null) {
/* 159 */       tablename = "sm_busilog_default_es";
/*     */     }
/* 161 */     return tablename;
/*     */   }
/*     */   
/* 164 */   public String getPk_busiobj() { return pk_busiobj; }
/*     */   
/*     */   public void setPk_busiobj(String pk_busiobj)
/*     */   {
/* 168 */     this.pk_busiobj = pk_busiobj;
/*     */   }
public String getPk_hises() {
	return pk_hises;
}
public void setPk_hises(String pk_hises) {
	this.pk_hises = pk_hises;
}

/*     */ }

/* Location:           D:\zhw\home0816\modules\baseapp\lib\pubbaseapp_applogsLevel-1.jar
 * Qualified Name:     nc.bs.busilog.vo.BusinessLogVO
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */ package nc.bs.mmpps.calc.bp.fetch.base;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import nc.vo.pubapp.pattern.exception.ExceptionUtils;
/*     */ public class CommonMapVO
/*     */ {
/*     */   private String where;
/*     */   private String from;
/*     */   private String materialid;
/*     */   private String materialvid;
/*     */   private String free1;
/*     */   private String free10;
/*     */   private String free2;
/*     */   private String free3;
/*     */   private String free4;
/*     */   private String free5;
/*     */   private String free6;
/*     */   private String free7;
/*     */   private String free8;
/*     */   private String free9;
/*     */   private String customerid;
/*     */   private String productorid;
/*     */   private String projectid;
/*     */   private String vendorid;
/*     */   private String cffileid;
/*     */   private String vfirsttype;
/*     */   private String vfirstcode;
/*     */   private String vfirstrowno;
/*     */   private String vfirstid;
/*     */   private String vfirstbid;
/*     */   private String vsrcid;
/*     */   private String vsrctype;
/*     */   private String vsrccode;
/*     */   private String vsrcrowno;
/*     */   private String vsrcbid;
/*     */   private String vbillstatus;
/*     */   private String vbillstatusenumid;
/*     */   private String ccustmaterialid;
/*     */   private String vbatchcode;
/*     */   private String pk_batchcode;
/*     */   private String cunitid;
/*     */   private String nbillnum;
/*     */   private String nbillexenum;
/*     */   private String vtranstype;
/*     */   private String dbilldate;
/*     */   private String vdef1;
/*     */   private String vdef2;
/*     */   private String vdef3;
/*     */   private String vdef4;
/*     */   private String vdef5;
/*     */   private String vdef6;
/*     */   private String vdef7;
/*     */   private String vdef8;
/*     */   private String vdef9;
/*     */   private String vdef10;
/*     */   private String vdef11;
/*     */   private String vdef12;
/*     */   private String vdef13;
/*     */   private String vdef14;
/*     */   private String vdef15;
/*     */   private String vdef16;
/*     */   private String vdef17;
/*     */   private String vdef18;
/*     */   private String vdef19;
/*     */   private String vdef20;
/*     */ 
/*     */   public String getVtranstype()
/*     */   {
/* 115 */     return this.vtranstype;
/*     */   }
/*     */ 
/*     */   public void setVtranstype(String vtranstype) {
/* 119 */     this.vtranstype = vtranstype;
/*     */   }
/*     */ 
/*     */   public String getVbillstatus() {
/* 123 */     return this.vbillstatus;
/*     */   }
/*     */ 
/*     */   public void setVbillstatus(String vbillstatus) {
/* 127 */     this.vbillstatus = vbillstatus;
/*     */   }
/*     */ 
/*     */   public String getVbillstatusenumid() {
/* 131 */     return this.vbillstatusenumid;
/*     */   }
/*     */ 
/*     */   public void setVbillstatusenumid(String vbillstatusenumid) {
/* 135 */     this.vbillstatusenumid = vbillstatusenumid;
/*     */   }
/*     */ 
/*     */   public String getCcustmaterialid() {
/* 139 */     return this.ccustmaterialid;
/*     */   }
/*     */ 
/*     */   public void setCcustmaterialid(String ccustmaterialid) {
/* 143 */     this.ccustmaterialid = ccustmaterialid;
/*     */   }
/*     */ 
/*     */   public String getVbatchcode() {
/* 147 */     return this.vbatchcode;
/*     */   }
/*     */ 
/*     */   public void setVbatchcode(String vbatchcode) {
/* 151 */     this.vbatchcode = vbatchcode;
/*     */   }
/*     */ 
/*     */   public String getPk_batchcode() {
/* 155 */     return this.pk_batchcode;
/*     */   }
/*     */ 
/*     */   public void setPk_batchcode(String pk_batchcode) {
/* 159 */     this.pk_batchcode = pk_batchcode;
/*     */   }
/*     */ 
/*     */   public String getCunitid() {
/* 163 */     return this.cunitid;
/*     */   }
/*     */ 
/*     */   public void setCunitid(String cunitid) {
/* 167 */     this.cunitid = cunitid;
/*     */   }
/*     */ 
/*     */   public String getNbillnum() {
/* 171 */     return this.nbillnum;
/*     */   }
/*     */ 
/*     */   public void setNbillnum(String nbillnum) {
/* 175 */     this.nbillnum = nbillnum;
/*     */   }
/*     */ 
/*     */   public String getNbillexenum() {
/* 179 */     return this.nbillexenum;
/*     */   }
/*     */ 
/*     */   public void setNbillexenum(String nbillexenum) {
/* 183 */     this.nbillexenum = nbillexenum;
/*     */   }
/*     */ 
/*     */   public String getMaterialid() {
/* 187 */     return this.materialid;
/*     */   }
/*     */ 
/*     */   public void setMaterialid(String materialid) {
/* 191 */     this.materialid = materialid;
/*     */   }
/*     */ 
/*     */   public String getMaterialvid() {
/* 195 */     return this.materialvid;
/*     */   }
/*     */ 
/*     */   public void setMaterialvid(String materialvid) {
/* 199 */     this.materialvid = materialvid;
/*     */   }
/*     */ 
/*     */   public String getFree1() {
/* 203 */     return this.free1;
/*     */   }
/*     */ 
/*     */   public void setFree1(String free1) {
/* 207 */     this.free1 = free1;
/*     */   }
/*     */ 
/*     */   public String getFree10() {
/* 211 */     return this.free10;
/*     */   }
/*     */ 
/*     */   public void setFree10(String free10) {
/* 215 */     this.free10 = free10;
/*     */   }
/*     */ 
/*     */   public String getFree2() {
/* 219 */     return this.free2;
/*     */   }
/*     */ 
/*     */   public void setFree2(String free2) {
/* 223 */     this.free2 = free2;
/*     */   }
/*     */ 
/*     */   public String getFree3() {
/* 227 */     return this.free3;
/*     */   }
/*     */ 
/*     */   public void setFree3(String free3) {
/* 231 */     this.free3 = free3;
/*     */   }
/*     */ 
/*     */   public String getFree4() {
/* 235 */     return this.free4;
/*     */   }
/*     */ 
/*     */   public void setFree4(String free4) {
/* 239 */     this.free4 = free4;
/*     */   }
/*     */ 
/*     */   public String getFree5() {
/* 243 */     return this.free5;
/*     */   }
/*     */ 
/*     */   public void setFree5(String free5) {
/* 247 */     this.free5 = free5;
/*     */   }
/*     */ 
/*     */   public String getFree6() {
/* 251 */     return this.free6;
/*     */   }
/*     */ 
/*     */   public void setFree6(String free6) {
/* 255 */     this.free6 = free6;
/*     */   }
/*     */ 
/*     */   public String getFree7() {
/* 259 */     return this.free7;
/*     */   }
/*     */ 
/*     */   public void setFree7(String free7) {
/* 263 */     this.free7 = free7;
/*     */   }
/*     */ 
/*     */   public String getFree8() {
/* 267 */     return this.free8;
/*     */   }
/*     */ 
/*     */   public void setFree8(String free8) {
/* 271 */     this.free8 = free8;
/*     */   }
/*     */ 
/*     */   public String getFree9() {
/* 275 */     return this.free9;
/*     */   }
/*     */ 
/*     */   public void setFree9(String free9) {
/* 279 */     this.free9 = free9;
/*     */   }
/*     */ 
/*     */   public String getCustomerid() {
/* 283 */     return this.customerid;
/*     */   }
/*     */ 
/*     */   public void setCustomerid(String customerid) {
/* 287 */     this.customerid = customerid;
/*     */   }
/*     */ 
/*     */   public String getProductorid() {
/* 291 */     return this.productorid;
/*     */   }
/*     */ 
/*     */   public void setProductorid(String productorid) {
/* 295 */     this.productorid = productorid;
/*     */   }
/*     */ 
/*     */   public String getProjectid() {
/* 299 */     return this.projectid;
/*     */   }
/*     */ 
/*     */   public void setProjectid(String projectid) {
/* 303 */     this.projectid = projectid;
/*     */   }
/*     */ 
/*     */   public String getVendorid() {
/* 307 */     return this.vendorid;
/*     */   }
/*     */ 
/*     */   public void setVendorid(String vendorid) {
/* 311 */     this.vendorid = vendorid;
/*     */   }
/*     */ 
/*     */   public String getVfirsttype() {
/* 315 */     return this.vfirsttype;
/*     */   }
/*     */ 
/*     */   public void setVfirsttype(String vfirsttype) {
/* 319 */     this.vfirsttype = vfirsttype;
/*     */   }
/*     */ 
/*     */   public String getVfirstcode() {
/* 323 */     return this.vfirstcode;
/*     */   }
/*     */ 
/*     */   public void setVfirstcode(String vfirstcode) {
/* 327 */     this.vfirstcode = vfirstcode;
/*     */   }
/*     */ 
/*     */   public String getVfirstrowno() {
/* 331 */     return this.vfirstrowno;
/*     */   }
/*     */ 
/*     */   public void setVfirstrowno(String vfirstrowno) {
/* 335 */     this.vfirstrowno = vfirstrowno;
/*     */   }
/*     */ 
/*     */   public String getVfirstid() {
/* 339 */     return this.vfirstid;
/*     */   }
/*     */ 
/*     */   public void setVfirstid(String vfirstid) {
/* 343 */     this.vfirstid = vfirstid;
/*     */   }
/*     */ 
/*     */   public String getVfirstbid() {
/* 347 */     return this.vfirstbid;
/*     */   }
/*     */ 
/*     */   public void setVfirstbid(String vfirstbid) {
/* 351 */     this.vfirstbid = vfirstbid;
/*     */   }
/*     */ 
/*     */   public String getVsrcid() {
/* 355 */     return this.vsrcid;
/*     */   }
/*     */ 
/*     */   public void setVsrcid(String vsrcid) {
/* 359 */     this.vsrcid = vsrcid;
/*     */   }
/*     */ 
/*     */   public String getVsrctype() {
/* 363 */     return this.vsrctype;
/*     */   }
/*     */ 
/*     */   public void setVsrctype(String vsrctype) {
/* 367 */     this.vsrctype = vsrctype;
/*     */   }
/*     */ 
/*     */   public String getVsrccode() {
/* 371 */     return this.vsrccode;
/*     */   }
/*     */ 
/*     */   public void setVsrccode(String vsrccode) {
/* 375 */     this.vsrccode = vsrccode;
/*     */   }
/*     */ 
/*     */   public String getVsrcrowno() {
/* 379 */     return this.vsrcrowno;
/*     */   }
/*     */ 
/*     */   public void setVsrcrowno(String vsrcrowno) {
/* 383 */     this.vsrcrowno = vsrcrowno;
/*     */   }
/*     */ 
/*     */   public String getVsrcbid() {
/* 387 */     return this.vsrcbid;
/*     */   }
/*     */ 
/*     */   public void setVsrcbid(String vsrcbid) {
/* 391 */     this.vsrcbid = vsrcbid;
/*     */   }
/*     */ 
/*     */   public String getFrom() {
/* 395 */     return this.from;
/*     */   }
/*     */ 
/*     */   public void setFrom(String from) {
/* 399 */     this.from = from;
/*     */   }
/*     */ 
/*     */   public String getWhere() {
/* 403 */     return this.where;
/*     */   }
/*     */ 
/*     */   public void setWhere(String where) {
/* 407 */     this.where = where;
/*     */   }
/*     */ 
/*     */   public String getDbilldate() {
/* 411 */     return this.dbilldate;
/*     */   }
/*     */ 
/*     */   public void setDbilldate(String dbilldate) {
/* 415 */     this.dbilldate = dbilldate;
/*     */   }
/*     */ 
/*     */   public String getCffileid() {
/* 419 */     return this.cffileid;
/*     */   }
/*     */ 
/*     */   public void setCffileid(String cffileid) {
/* 423 */     this.cffileid = cffileid;
/*     */   }
/*     */ 
/*     */   public String getVdef1() {
/* 427 */     return this.vdef1;
/*     */   }
/*     */ 
/*     */   public void setVdef1(String vdef1) {
/* 431 */     this.vdef1 = vdef1;
/*     */   }
/*     */ 
/*     */   public String getVdef2() {
/* 435 */     return this.vdef2;
/*     */   }
/*     */ 
/*     */   public void setVdef2(String vdef2) {
/* 439 */     this.vdef2 = vdef2;
/*     */   }
/*     */ 
/*     */   public String getVdef3() {
/* 443 */     return this.vdef3;
/*     */   }
/*     */ 
/*     */   public void setVdef3(String vdef3) {
/* 447 */     this.vdef3 = vdef3;
/*     */   }
/*     */ 
/*     */   public String getVdef4() {
/* 451 */     return this.vdef4;
/*     */   }
/*     */ 
/*     */   public void setVdef4(String vdef4) {
/* 455 */     this.vdef4 = vdef4;
/*     */   }
/*     */ 
/*     */   public String getVdef5() {
/* 459 */     return this.vdef5;
/*     */   }
/*     */ 
/*     */   public void setVdef5(String vdef5) {
/* 463 */     this.vdef5 = vdef5;
/*     */   }
/*     */ 
/*     */   public String getVdef6() {
/* 467 */     return this.vdef6;
/*     */   }
/*     */ 
/*     */   public void setVdef6(String vdef6) {
/* 471 */     this.vdef6 = vdef6;
/*     */   }
/*     */ 
/*     */   public String getVdef7() {
/* 475 */     return this.vdef7;
/*     */   }
/*     */ 
/*     */   public void setVdef7(String vdef7) {
/* 479 */     this.vdef7 = vdef7;
/*     */   }
/*     */ 
/*     */   public String getVdef8() {
/* 483 */     return this.vdef8;
/*     */   }
/*     */ 
/*     */   public void setVdef8(String vdef8) {
/* 487 */     this.vdef8 = vdef8;
/*     */   }
/*     */ 
/*     */   public String getVdef9() {
/* 491 */     return this.vdef9;
/*     */   }
/*     */ 
/*     */   public void setVdef9(String vdef9) {
/* 495 */     this.vdef9 = vdef9;
/*     */   }
/*     */ 
/*     */   public String getVdef10() {
/* 499 */     return this.vdef10;
/*     */   }
/*     */ 
/*     */   public void setVdef10(String vdef10) {
/* 503 */     this.vdef10 = vdef10;
/*     */   }
/*     */ 
/*     */   public String getVdef11() {
/* 507 */     return this.vdef11;
/*     */   }
/*     */ 
/*     */   public void setVdef11(String vdef11) {
/* 511 */     this.vdef11 = vdef11;
/*     */   }
/*     */ 
/*     */   public String getVdef12() {
/* 515 */     return this.vdef12;
/*     */   }
/*     */ 
/*     */   public void setVdef12(String vdef12) {
/* 519 */     this.vdef12 = vdef12;
/*     */   }
/*     */ 
/*     */   public String getVdef13() {
/* 523 */     return this.vdef13;
/*     */   }
/*     */ 
/*     */   public void setVdef13(String vdef13) {
/* 527 */     this.vdef13 = vdef13;
/*     */   }
/*     */ 
/*     */   public String getVdef14() {
/* 531 */     return this.vdef14;
/*     */   }
/*     */ 
/*     */   public void setVdef14(String vdef14) {
/* 535 */     this.vdef14 = vdef14;
/*     */   }
/*     */ 
/*     */   public String getVdef15() {
/* 539 */     return this.vdef15;
/*     */   }
/*     */ 
/*     */   public void setVdef15(String vdef15) {
/* 543 */     this.vdef15 = vdef15;
/*     */   }
/*     */ 
/*     */   public String getVdef16() {
/* 547 */     return this.vdef16;
/*     */   }
/*     */ 
/*     */   public void setVdef16(String vdef16) {
/* 551 */     this.vdef16 = vdef16;
/*     */   }
/*     */ 
/*     */   public String getVdef17() {
/* 555 */     return this.vdef17;
/*     */   }
/*     */ 
/*     */   public void setVdef17(String vdef17) {
/* 559 */     this.vdef17 = vdef17;
/*     */   }
/*     */ 
/*     */   public String getVdef18() {
/* 563 */     return this.vdef18;
/*     */   }
/*     */ 
/*     */   public void setVdef18(String vdef18) {
/* 567 */     this.vdef18 = vdef18;
/*     */   }
/*     */ 
/*     */   public String getVdef19() {
/* 571 */     return this.vdef19;
/*     */   }
/*     */ 
/*     */   public void setVdef19(String vdef19) {
/* 575 */     this.vdef19 = vdef19;
/*     */   }
/*     */ 
/*     */   public String getVdef20() {
/* 579 */     return this.vdef20;
/*     */   }
/*     */ 
/*     */   public void setVdef20(String vdef20) {
/* 583 */     this.vdef20 = vdef20;
/*     */   }
/*     */ 
/*     */   public void setVdefs(String destPrefix) {
/*     */     try {
/* 588 */       for (int i = 1; i <= 20; ++i)
/* 589 */         super.getClass().getMethod("setVdef" + i, new Class[] { String.class }).invoke(this, new Object[] { destPrefix + i });
/*     */     }
/*     */     catch (Exception ex) {
/* 592 */       ExceptionUtils.wrappException(ex);
/*     */     }
/*     */   }
/*     */ }

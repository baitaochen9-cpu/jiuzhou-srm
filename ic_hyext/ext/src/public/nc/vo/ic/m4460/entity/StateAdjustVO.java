/*     */ package nc.vo.ic.m4460.entity;
/*     */ 
/*     */ import nc.vo.pub.IVOMeta;
/*     */ import nc.vo.pub.SuperVO;
/*     */ import nc.vo.pub.lang.UFDateTime;
/*     */ import nc.vo.pub.lang.UFDouble;
/*     */ import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;
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
/*     */ public class StateAdjustVO
/*     */   extends SuperVO
/*     */ {
/*     */   private static final long serialVersionUID = 1163486349262109507L;
/*     */   public static final String PK_ONHANDDIM_ADJ = "pk_onhanddim_adj";
/*     */   public static final String ONHANDNUMTS = "onhandnumts";
/*     */   public static final String VBILLCODE = "vbillcode";
/*     */   public static final String TABLENAME = "ic_stateadjust";
/*     */   public static final String CSTATEADJUSTID = "cstateadjustid";
/*     */   public static final String PK_GROUP = "pk_group";
/*     */   public static final String PK_ORG = "pk_org";
/*     */   public static final String PK_ONHANDDIM = "pk_onhanddim";
/*     */   public static final String NASSISTNUM = "nassistnum";
/*     */   public static final String NNUM = "nnum";
/*     */   public static final String NGROSSNUM = "ngrossnum";
/*     */   public static final String CADJUSTSTATEID = "cadjuststateid";
/*     */   public static final String NADJUSTASSISTNUM = "nadjustassistnum";
/*     */   public static final String NADJUSTGROSSNUM = "nadjustgrossnum";
/*     */   public static final String NADJUSTNUM = "nadjustnum";
/*     */   public static final String NLOCKNUM = "nlocknum";
/*     */   public static final String NLOCKASSISTNUM = "nlockassistnum";
/*     */   public static final String NRSNUM = "nrsnum";
/*     */   public static final String BILLMAKER = "billmaker";
/*     */   public static final String CREATIONTIME = "creationtime";
/*     */   public static final String TS = "ts";
/*     */   public static final String DR = "dr";
/*     */   public static final String VCHANGERATE = "vchangerate";
/*     */   public static final String CADJUSTBILLTYPE = "cadjustbilltype";
/*     */   public static final String CADJUSTBILLCODE = "cadjustbillcode";
/*     */   public static final String CADJUSTROWNO = "cadjustrowno";
/*     */   public static final String CADJUSTBILLID = "cadjustbillid";
/*     */   public static final String CADJUSTBILLBID = "cadjustbillbid";
/*     */   public static final String CADJUSTTRANSTYPE = "cadjusttranstype";
/*     */   public static final String PK_SERIALCODE = "pk_serialcode";
/*     */   public static final String VSNCODE = "vsncode";
/*     */   public static final String PK_ONHAND_SN = "pk_onhand_sn";
/*     */   
private String vdef1;// 自定义项1
private String vdef2;// 自定义项2
private String vdef3;// 自定义项3
private String vdef4;// 自定义项4
private String vdef5;// 自定义项5
private String vdef6;// 自定义项6
private String vdef7;// 自定义项7
private String vdef8;// 自定义项8
private String vdef9;// 自定义项9
private String vdef10;// 自定义项10
/*     */   public String getPk_onhand_sn()
/*     */   {
/* 119 */     return (String)getAttributeValue("pk_onhand_sn");
/*     */   }
/*     */   
/*     */   public void setPk_onhand_sn(String pk_onhand_sn) {
/* 123 */     setAttributeValue("pk_onhand_sn", pk_onhand_sn);
/*     */   }
/*     */   
/*     */   public String getPk_serialcode() {
/* 127 */     return (String)getAttributeValue("pk_serialcode");
/*     */   }
/*     */   
/*     */   public void setPk_serialcode(String pk_serialcode) {
/* 131 */     setAttributeValue("pk_serialcode", pk_serialcode);
/*     */   }
/*     */   
/*     */   public String getVsncode() {
/* 135 */     return (String)getAttributeValue("vsncode");
/*     */   }
/*     */   
/*     */   public void setVsncode(String vsncode) {
/* 139 */     setAttributeValue("vsncode", vsncode);
/*     */   }
/*     */   
/*     */   public String getCadjusttranstype() {
/* 143 */     return (String)getAttributeValue("cadjusttranstype");
/*     */   }
/*     */   
/*     */   public void setCadjusttranstype(String cadjusttranstype) {
/* 147 */     setAttributeValue("cadjusttranstype", cadjusttranstype);
/*     */   }
/*     */   
/*     */   public String getCadjustbillid()
/*     */   {
/* 152 */     return (String)getAttributeValue("cadjustbillid");
/*     */   }
/*     */   
/*     */   public void setCadjustbillid(String cadjustbillid) {
/* 156 */     setAttributeValue("cadjustbillid", cadjustbillid);
/*     */   }
/*     */   
/*     */   public String getCadjustbillbid() {
/* 160 */     return (String)getAttributeValue("cadjustbillbid");
/*     */   }
/*     */   
/*     */   public void setCadjustbillbid(String cadjustbillbid) {
/* 164 */     setAttributeValue("cadjustbillbid", cadjustbillbid);
/*     */   }
/*     */   
/*     */   public String getCadjustbilltype() {
/* 168 */     return (String)getAttributeValue("cadjustbilltype");
/*     */   }
/*     */   
/*     */   public void setCadjustbilltype(String cadjustbilltype) {
/* 172 */     setAttributeValue("cadjustbilltype", cadjustbilltype);
/*     */   }
/*     */   
/*     */   public String getCadjustbillcode() {
/* 176 */     return (String)getAttributeValue("cadjustbillcode");
/*     */   }
/*     */   
/*     */   public void setCadjustbillcode(String cadjustbillcode) {
/* 180 */     setAttributeValue("cadjustbillcode", cadjustbillcode);
/*     */   }
/*     */   
/*     */   public String getCadjustrowno() {
/* 184 */     return (String)getAttributeValue("cadjustrowno");
/*     */   }
/*     */   
/*     */   public void setCadjustrowno(String cadjustrowno) {
/* 188 */     setAttributeValue("cadjustrowno", cadjustrowno);
/*     */   }
/*     */   
/*     */   public IVOMeta getMetaData()
/*     */   {
/* 193 */     return VOMetaFactory.getInstance().getVOMeta("ic.StateAdjustVO");
/*     */   }
/*     */   
/*     */   public String getTableName()
/*     */   {
/* 198 */     return "ic_stateadjust";
/*     */   }
/*     */   
/*     */   public String getCadjuststateid() {
/* 202 */     return (String)getAttributeValue("cadjuststateid");
/*     */   }
/*     */   
/*     */   public void setCadjuststateid(String cadjuststateid) {
/* 206 */     setAttributeValue("cadjuststateid", cadjuststateid);
/*     */   }
/*     */   
/*     */   public UFDateTime getCreationtime() {
/* 210 */     return (UFDateTime)getAttributeValue("creationtime");
/*     */   }
/*     */   
/*     */   public void setCreationtime(UFDateTime creationtime) {
/* 214 */     setAttributeValue("creationtime", creationtime);
/*     */   }
/*     */   
/*     */   public String getCstateadjustid() {
/* 218 */     return (String)getAttributeValue("cstateadjustid");
/*     */   }
/*     */   
/*     */   public void setCstateadjustid(String cstateadjustid) {
/* 222 */     setAttributeValue("cstateadjustid", cstateadjustid);
/*     */   }
/*     */   
/*     */   public Integer getDr() {
/* 226 */     return (Integer)getAttributeValue("dr");
/*     */   }
/*     */   
/*     */   public void setDr(Integer dr) {
/* 230 */     setAttributeValue("dr", dr);
/*     */   }
/*     */   
/*     */   public UFDouble getNassistnum() {
/* 234 */     return (UFDouble)getAttributeValue("nassistnum");
/*     */   }
/*     */   
/*     */   public void setNassistnum(UFDouble nassistnum) {
/* 238 */     setAttributeValue("nassistnum", nassistnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNgrossnum() {
/* 242 */     return (UFDouble)getAttributeValue("ngrossnum");
/*     */   }
/*     */   
/*     */   public void setNgrossnum(UFDouble ngrossnum) {
/* 246 */     setAttributeValue("ngrossnum", ngrossnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNnum() {
/* 250 */     return (UFDouble)getAttributeValue("nnum");
/*     */   }
/*     */   
/*     */   public void setNnum(UFDouble nnum) {
/* 254 */     setAttributeValue("nnum", nnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNadjustassistnum() {
/* 258 */     return (UFDouble)getAttributeValue("nadjustassistnum");
/*     */   }
/*     */   
/*     */   public void setNadjustassistnum(UFDouble nadjustassistnum) {
/* 262 */     setAttributeValue("nadjustassistnum", nadjustassistnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNadjustgrossnum() {
/* 266 */     return (UFDouble)getAttributeValue("nadjustgrossnum");
/*     */   }
/*     */   
/*     */   public void setNadjustgrossnum(UFDouble nadjustgrossnum) {
/* 270 */     setAttributeValue("nadjustgrossnum", nadjustgrossnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNadjustnum() {
/* 274 */     return (UFDouble)getAttributeValue("nadjustnum");
/*     */   }
/*     */   
/*     */   public void setNadjustnum(UFDouble nadjustnum) {
/* 278 */     setAttributeValue("nadjustnum", nadjustnum);
/*     */   }
/*     */   
/*     */   public UFDouble getNlocknum() {
/* 282 */     return (UFDouble)getAttributeValue("nlocknum");
/*     */   }
/*     */   
/*     */   public void setNlocknum(UFDouble nlocknum) {
/* 286 */     setAttributeValue("nlocknum", nlocknum);
/*     */   }
/*     */   
/*     */   public UFDouble getNrsnum() {
/* 290 */     return (UFDouble)getAttributeValue("nrsnum");
/*     */   }
/*     */   
/*     */   public void setNrsnum(UFDouble nrsnum) {
/* 294 */     setAttributeValue("nrsnum", nrsnum);
/*     */   }
/*     */   
/*     */   public String getBillmaker() {
/* 298 */     return (String)getAttributeValue("billmaker");
/*     */   }
/*     */   
/*     */   public void setBillmaker(String billmaker) {
/* 302 */     setAttributeValue("billmaker", billmaker);
/*     */   }
/*     */   
/*     */   public String getPk_group() {
/* 306 */     return (String)getAttributeValue("pk_group");
/*     */   }
/*     */   
/*     */   public void setPk_group(String pk_group) {
/* 310 */     setAttributeValue("pk_group", pk_group);
/*     */   }
/*     */   
/*     */   public String getPk_onhanddim() {
/* 314 */     return (String)getAttributeValue("pk_onhanddim");
/*     */   }
/*     */   
/*     */   public void setPk_onhanddim(String pk_onhanddim) {
/* 318 */     setAttributeValue("pk_onhanddim", pk_onhanddim);
/*     */   }
/*     */   
/*     */   public UFDateTime getTs() {
/* 322 */     return (UFDateTime)getAttributeValue("ts");
/*     */   }
/*     */   
/*     */   public void setTs(UFDateTime ts) {
/* 326 */     setAttributeValue("ts", ts);
/*     */   }
/*     */   
/*     */   public String getPk_onhanddim_adj() {
/* 330 */     return (String)getAttributeValue("pk_onhanddim_adj");
/*     */   }
/*     */   
/*     */   public void setPk_onhanddim_adj(String pk_onhanddim_adj) {
/* 334 */     setAttributeValue("pk_onhanddim_adj", pk_onhanddim_adj);
/*     */   }
/*     */   
/*     */   public String getVbillcode() {
/* 338 */     return (String)getAttributeValue("vbillcode");
/*     */   }
/*     */   
/*     */   public void setVbillcode(String vbillcode) {
/* 342 */     setAttributeValue("vbillcode", vbillcode);
/*     */   }
/*     */   
/*     */   public String getPk_org() {
/* 346 */     return (String)getAttributeValue("pk_org");
/*     */   }
/*     */   
/*     */   public void setPk_org(String pk_org) {
/* 350 */     setAttributeValue("pk_org", pk_org);
/*     */   }
/*     */   
/*     */   public UFDouble getNlockassistnum() {
/* 354 */     return (UFDouble)getAttributeValue("nlockassistnum");
/*     */   }
/*     */   
/*     */   public void setNlockassistnum(UFDouble nlockassistnum) {
/* 358 */     setAttributeValue("nlockassistnum", nlockassistnum);
/*     */   }
/*     */   
/*     */   public UFDateTime getOnhandnumts() {
/* 362 */     return (UFDateTime)getAttributeValue("onhandnumts");
/*     */   }
/*     */   
/*     */   public void setOnhandnumts(UFDateTime onhandnumts) {
/* 366 */     setAttributeValue("onhandnumts", onhandnumts);
/*     */   }
/*     */   
/*     */   public String getVchangerate() {
/* 370 */     return (String)getAttributeValue("vchangerate");
/*     */   }
/*     */   
/*     */   public void setVchangerate(String vchangerate) {
/* 374 */     setAttributeValue("vchangerate", vchangerate);
/*     */   }
/*     */   
/*     */   public String getCunitid() {
/* 378 */     return (String)getAttributeValue("cunitid");
/*     */   }
/*     */   
/*     */   public void setCunitid(String cunitid) {
/* 382 */     setAttributeValue("cunitid", cunitid);
/*     */   }
/*     */   
/*     */   public String getCastunitid() {
/* 386 */     return (String)getAttributeValue("castunitid");
/*     */   }
/*     */   
/*     */   public void setCastunitid(String castunitid) {
/* 390 */     setAttributeValue("castunitid", castunitid);
/*     */   }
/*     */   
/*     */   public String getVfree1() {
/* 394 */     return (String)getAttributeValue("vfree1");
/*     */   }
/*     */   
/*     */   public String getVfree10() {
/* 398 */     return (String)getAttributeValue("vfree10");
/*     */   }
/*     */   
/*     */   public String getVfree2() {
/* 402 */     return (String)getAttributeValue("vfree2");
/*     */   }
/*     */   
/*     */   public String getVfree3() {
/* 406 */     return (String)getAttributeValue("vfree3");
/*     */   }
/*     */   
/*     */   public String getVfree4() {
/* 410 */     return (String)getAttributeValue("vfree4");
/*     */   }
/*     */   
/*     */   public String getVfree5() {
/* 414 */     return (String)getAttributeValue("vfree5");
/*     */   }
/*     */   
/*     */   public String getVfree6() {
/* 418 */     return (String)getAttributeValue("vfree6");
/*     */   }
/*     */   
/*     */   public String getVfree7() {
/* 422 */     return (String)getAttributeValue("vfree7");
/*     */   }
/*     */   
/*     */   public String getVfree8() {
/* 426 */     return (String)getAttributeValue("vfree8");
/*     */   }
/*     */   
/*     */   public String getVfree9() {
/* 430 */     return (String)getAttributeValue("vfree9");
/*     */   }
/*     */   
/*     */   public void setVfree1(String vfree1) {
/* 434 */     setAttributeValue("vfree1", vfree1);
/*     */   }
/*     */   
/*     */   public void setVfree10(String vfree10) {
/* 438 */     setAttributeValue("vfree10", vfree10);
/*     */   }
/*     */   
/*     */   public void setVfree2(String vfree2) {
/* 442 */     setAttributeValue("vfree2", vfree2);
/*     */   }
/*     */   
/*     */   public void setVfree3(String vfree3) {
/* 446 */     setAttributeValue("vfree3", vfree3);
/*     */   }
/*     */   
/*     */   public void setVfree4(String vfree4) {
/* 450 */     setAttributeValue("vfree4", vfree4);
/*     */   }
/*     */   
/*     */   public void setVfree5(String vfree5) {
/* 454 */     setAttributeValue("vfree5", vfree5);
/*     */   }
/*     */   
/*     */   public void setVfree6(String vfree6) {
/* 458 */     setAttributeValue("vfree6", vfree6);
/*     */   }
/*     */   
/*     */   public void setVfree7(String vfree7) {
/* 462 */     setAttributeValue("vfree7", vfree7);
/*     */   }
/*     */   
/*     */   public void setVfree8(String vfree8) {
/* 466 */     setAttributeValue("vfree8", vfree8);
/*     */   }
/*     */   
/*     */   public void setVfree9(String vfree9) {
/* 470 */     setAttributeValue("vfree9", vfree9);
/*     */   }
/*     */   
/*     */   public void setClocationid(String clocationid) {
/* 474 */     setAttributeValue("clocationid", clocationid);
/*     */   }
/*     */   
/*     */   public String getClocationid() {
/* 478 */     return (String)getAttributeValue("clocationid");
/*     */   }
public String getVdef1() {
	return vdef1;
}
public void setVdef1(String vdef1) {
	this.vdef1 = vdef1;
	 setAttributeValue("vdef1", vdef1);
}
public String getVdef2() {
	return vdef2;
}
public void setVdef2(String vdef2) {
	this.vdef2 = vdef2;
	 setAttributeValue("vdef2", vdef2);
}
public String getVdef3() {
	return vdef3;
}
public void setVdef3(String vdef3) {
	this.vdef3 = vdef3;
	 setAttributeValue("vdef3", vdef3);
}
public String getVdef4() {
	return vdef4;
}
public void setVdef4(String vdef4) {
	this.vdef4 = vdef4;
	 setAttributeValue("vdef4", vdef4);
}
public String getVdef5() {
	return vdef5;
}
public void setVdef5(String vdef5) {
	this.vdef5 = vdef5;
	 setAttributeValue("vdef5", vdef5);
}
public String getVdef6() {
	return vdef6;
}
public void setVdef6(String vdef6) {
	this.vdef6 = vdef6;
	 setAttributeValue("vdef6", vdef6);
}
public String getVdef7() {
	return vdef7;
}
public void setVdef7(String vdef7) {
	this.vdef7 = vdef7;
	 setAttributeValue("vdef7", vdef7);
}
public String getVdef8() {
	return vdef8;
}
public void setVdef8(String vdef8) {
	this.vdef8 = vdef8;
	 setAttributeValue("vdef8", vdef8);
}
public String getVdef9() {
	return vdef9;
}
public void setVdef9(String vdef9) {
	this.vdef9 = vdef9;
	 setAttributeValue("vdef9", vdef9);
}
public String getVdef10() {
	return vdef10;
}
public void setVdef10(String vdef10) {
	this.vdef10 = vdef10;
	 setAttributeValue("vdef10", vdef10);
}


/*     */ }
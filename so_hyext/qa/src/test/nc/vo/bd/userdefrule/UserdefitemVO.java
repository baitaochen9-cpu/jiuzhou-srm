/*     */ package nc.vo.bd.userdefrule;
/*     */ 
/*     */ import nc.vo.pub.SuperVO;
/*     */ import nc.vo.pub.lang.UFBoolean;
/*     */ import nc.vo.pub.lang.UFDateTime;
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
/*     */ public class UserdefitemVO
/*     */   extends SuperVO
/*     */ {
/*     */   public static final String BODYDISPTYPE = "bodydisptype";
/*     */   public static final String CLASSID = "classid";
/*     */   public static final String CREATIONTIME = "creationtime";
/*     */   public static final String CREATOR = "creator";
/*     */   public static final String DATAORIGINFLAG = "dataoriginflag";
/*     */   public static final String DIGITS = "digits";
/*     */   public static final String DISPROPERTY = "disproperty";
/*     */   public static final String ENABLED = "enabled";
/*     */   public static final String INPUTLENGTH = "inputlength";
/*     */   public static final String MEMO = "memo";
/*     */   public static final String MODIFIEDTIME = "modifiedtime";
/*     */   public static final String MODIFIER = "modifier";
/*     */   public static final String PK_GROUP = "pk_group";
/*     */   public static final String PK_ORG = "pk_org";
/*     */   public static final String PK_USERDEFITEM = "pk_userdefitem";
/*     */   public static final String PK_USERDEFRULE = "pk_userdefrule";
/*     */   public static final String PROPINDEX = "propindex";
/*     */   public static final String SHOWNAME = "showname";
/*     */   public static final String SHOWNAME2 = "showname2";
/*     */   public static final String SHOWNAME3 = "showname3";
/*     */   public static final String SHOWNAME4 = "showname4";
/*     */   public static final String SHOWNAME5 = "showname5";
/*     */   public static final String SHOWNAME6 = "showname6";
/*     */   public static final String USUFURCT = "usufruct";
/*     */   public static final String USUFURCTGROUP = "usufructgroup";
/*     */   private static final long serialVersionUID = 8992173339732900823L;
/*  75 */   private UFBoolean usufruct = UFBoolean.FALSE;
/*     */   
/*     */ 
/*     */   private String usufructgroup;
/*     */   
/*     */   private Integer bodydisptype;
/*     */   
/*     */   private String classid;
/*     */   
/*     */   private UFDateTime creationtime;
/*     */   
/*     */   private String creator;
/*     */   
/*     */   private Integer dataoriginflag;
/*     */   
/*     */   private Integer digits;
/*     */   
/*     */   private Integer disproperty;
/*     */   
/*  94 */   private Integer dr = Integer.valueOf(0);
/*     */   
/*  96 */   private UFBoolean enabled = UFBoolean.TRUE;
/*     */   
/*     */ 
/*     */   private Integer inputlength;
/*     */   
/*     */ 
/*     */   private String memo;
/*     */   
/*     */ 
private UFBoolean IsFatherLeafEnable;


public UFBoolean getIsFatherLeafEnable() {
	return IsFatherLeafEnable;
}
public void setIsFatherLeafEnable(UFBoolean isFatherLeafEnable) {
	IsFatherLeafEnable = isFatherLeafEnable;
}
/*     */   private UFDateTime modifiedtime;
/*     */   
/*     */ 
/*     */   private String modifier;
/*     */   
/*     */ 
/*     */   private String pk_group;
/*     */   
/*     */ 
/*     */   private String pk_org;
/*     */   
/*     */ 
/*     */   private String pk_userdefitem;
/*     */   
/*     */ 
/*     */   private String pk_userdefrule;
/*     */   
/*     */ 
/*     */   private Integer propindex;
/*     */   
/*     */ 
/*     */   private String showname;
/*     */   
/*     */ 
/*     */   private String showname2;
/*     */   
/*     */ 
/*     */   private String showname3;
/*     */   
/*     */ 
/*     */   private String showname4;
/*     */   
/*     */ 
/*     */   private String showname5;
/*     */   
/*     */   private String showname6;
/*     */   
/*     */   private UFDateTime ts;
/*     */   
/*     */ 
/*     */   public static String getDefaultTableName()
/*     */   {
/* 147 */     return "bd_userdefitem";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getBodydisptype()
/*     */   {
/* 157 */     return bodydisptype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassid()
/*     */   {
/* 167 */     return classid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UFDateTime getCreationtime()
/*     */   {
/* 177 */     return creationtime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCreator()
/*     */   {
/* 187 */     return creator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getDataoriginflag()
/*     */   {
/* 197 */     return dataoriginflag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getDigits()
/*     */   {
/* 207 */     return digits;
/*     */   }
/*     */   
/*     */   public Integer getDisproperty() {
/* 211 */     return disproperty;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getDr()
/*     */   {
/* 221 */     return dr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UFBoolean getEnabled()
/*     */   {
/* 231 */     return enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getInputlength()
/*     */   {
/* 241 */     return inputlength;
/*     */   }
/*     */   
/*     */   public String getMemo() {
/* 245 */     return memo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UFDateTime getModifiedtime()
/*     */   {
/* 255 */     return modifiedtime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModifier()
/*     */   {
/* 265 */     return modifier;
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
/*     */   public String getParentPKFieldName()
/*     */   {
/* 278 */     return "pk_userdefrule";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPk_group()
/*     */   {
/* 288 */     return pk_group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPk_org()
/*     */   {
/* 298 */     return pk_org;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPk_userdefitem()
/*     */   {
/* 308 */     return pk_userdefitem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPk_userdefrule()
/*     */   {
/* 318 */     return pk_userdefrule;
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
/*     */   public String getPKFieldName()
/*     */   {
/* 331 */     return "pk_userdefitem";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPropindex()
/*     */   {
/* 341 */     return propindex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname()
/*     */   {
/* 351 */     return showname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname2()
/*     */   {
/* 361 */     return showname2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname3()
/*     */   {
/* 371 */     return showname3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname4()
/*     */   {
/* 381 */     return showname4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname5()
/*     */   {
/* 391 */     return showname5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShowname6()
/*     */   {
/* 401 */     return showname6;
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
/*     */   public String getTableName()
/*     */   {
/* 414 */     return "bd_userdefitem";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UFDateTime getTs()
/*     */   {
/* 424 */     return ts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBodydisptype(Integer newBodydisptype)
/*     */   {
/* 434 */     bodydisptype = newBodydisptype;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClassid(String newClassid)
/*     */   {
/* 444 */     classid = newClassid;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCreationtime(UFDateTime newCreationtime)
/*     */   {
/* 454 */     creationtime = newCreationtime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCreator(String newCreator)
/*     */   {
/* 464 */     creator = newCreator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDataoriginflag(Integer newDataoriginflag)
/*     */   {
/* 474 */     dataoriginflag = newDataoriginflag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDigits(Integer newDigits)
/*     */   {
/* 484 */     digits = newDigits;
/*     */   }
/*     */   
/*     */   public void setDisproperty(Integer disproperty) {
/* 488 */     this.disproperty = disproperty;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDr(Integer newDr)
/*     */   {
/* 498 */     dr = newDr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnabled(UFBoolean newEnabled)
/*     */   {
/* 508 */     enabled = newEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInputlength(Integer newInputlength)
/*     */   {
/* 518 */     inputlength = newInputlength;
/*     */   }
/*     */   
/*     */   public void setMemo(String memo) {
/* 522 */     this.memo = memo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModifiedtime(UFDateTime newModifiedtime)
/*     */   {
/* 532 */     modifiedtime = newModifiedtime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModifier(String newModifier)
/*     */   {
/* 542 */     modifier = newModifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPk_group(String newPk_group)
/*     */   {
/* 552 */     pk_group = newPk_group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPk_org(String newPk_org)
/*     */   {
/* 562 */     pk_org = newPk_org;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPk_userdefitem(String newPk_userdefitem)
/*     */   {
/* 572 */     pk_userdefitem = newPk_userdefitem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPk_userdefrule(String newPk_userdefrule)
/*     */   {
/* 582 */     pk_userdefrule = newPk_userdefrule;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropindex(Integer newPropindex)
/*     */   {
/* 592 */     propindex = newPropindex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname(String newShowname)
/*     */   {
/* 602 */     showname = newShowname;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname2(String newShowname2)
/*     */   {
/* 612 */     showname2 = newShowname2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname3(String newShowname3)
/*     */   {
/* 622 */     showname3 = newShowname3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname4(String newShowname4)
/*     */   {
/* 632 */     showname4 = newShowname4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname5(String newShowname5)
/*     */   {
/* 642 */     showname5 = newShowname5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShowname6(String newShowname6)
/*     */   {
/* 652 */     showname6 = newShowname6;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTs(UFDateTime newTs)
/*     */   {
/* 662 */     ts = newTs;
/*     */   }
/*     */   
/*     */   public UFBoolean getUsufruct() {
/* 666 */     return usufruct;
/*     */   }
/*     */   
/*     */   public void setUsufruct(UFBoolean usufruct) {
/* 670 */     this.usufruct = usufruct;
/*     */   }
/*     */   
/*     */   public String getUsufructgroup() {
/* 674 */     return usufructgroup;
/*     */   }
/*     */   
/*     */   public void setUsufructgroup(String usufructgroup) {
/* 678 */     this.usufructgroup = usufructgroup;
/*     */   }
/*     */ }

/* Location:           E:\kf_zhw\home0816\modules\baseapp\lib\pubbaseapp_appuserdefLevel-1.jar
 * Qualified Name:     nc.vo.bd.userdefrule.UserdefitemVO
 * Java Class Version: 7 (51.0)
 * JD-Core Version:    0.7.1
 */
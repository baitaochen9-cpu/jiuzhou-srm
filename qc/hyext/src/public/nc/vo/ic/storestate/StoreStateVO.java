/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*     */package nc.vo.ic.storestate;
/*     */
/*     */import nc.vo.pub.IVOMeta;
/*     */
import nc.vo.pub.SuperVO;
/*     */
import nc.vo.pub.lang.UFBoolean;
/*     */
import nc.vo.pub.lang.UFDateTime;
/*     */
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;
/*     */
/*     */public class StoreStateVO extends SuperVO
/*     */{
	/*     */private static final long serialVersionUID = 1L;
	/*  24 */public static String PK_STORESTATE = "pk_storestate";
	/*     */
	/*  29 */public static String PK_GROUP = "pk_group";
	/*     */
	/*  34 */public static String VCODE = "vcode";
	/*     */
	/*  39 */public static String VNAME = "vname";
	/*     */
	/*  44 */public static String VNAME2 = "vname2";
	/*     */
	/*  49 */public static String VNAME3 = "vname3";
	/*     */
	/*  54 */public static String VNAME4 = "vname4";
	/*     */
	/*  59 */public static String VNAME5 = "vname5";
	/*     */
	/*  64 */public static String VNAME6 = "vname6";
	/*     */
	/*  69 */public static String IUSABILITY = "iusability";
	/*     */
	/*  74 */public static String VMEMO = "vmemo";
	/*     */
	/*  79 */public static String SEALFLAG = "sealflag";
	/*     */
	/*  84 */public static String BISQCDEFAULT = "bisqcdefault";
	public static String PK_ORG = "pk_org";
	/*     */
	/*     */public IVOMeta getMetaData()
	/*     */{
		/*  89 */IVOMeta meta = VOMetaFactory.getInstance().getVOMeta(
				"ic.ic_storestate");
		/*  90 */return meta;
		/*     */}
	/*     */
	/*     */public String getTableName()
	/*     */{
		/*  95 */return "ic_storestate";
		/*     */}
	
	       public void setPk_org(String pk_org) {
		/*  99 */setAttributeValue(PK_ORG, pk_org);
		/*     */}
	/*     */
	/*     */public String getPk_org() {
		/* 103 */return ((String) getAttributeValue(PK_ORG));
		/*     */}
	/*     */
	/*     */public void setPk_storestate(String pk_storestate) {
		/*  99 */setAttributeValue(PK_STORESTATE, pk_storestate);
		/*     */}
	/*     */
	/*     */public String getPk_storestate() {
		/* 103 */return ((String) getAttributeValue(PK_STORESTATE));
		/*     */}
	/*     */
	/*     */public void setPk_group(String pk_group) {
		/* 107 */setAttributeValue(PK_GROUP, pk_group);
		/*     */}
	/*     */
	/*     */public String getPk_group() {
		/* 111 */return ((String) getAttributeValue(PK_GROUP));
		/*     */}
	/*     */
	/*     */public void setVcode(String vcode) {
		/* 115 */setAttributeValue(VCODE, vcode);
		/*     */}
	/*     */
	/*     */public String getVcode() {
		/* 119 */return ((String) getAttributeValue(VCODE));
		/*     */}
	/*     */
	/*     */public void setVname(String vname) {
		/* 123 */setAttributeValue(VNAME, vname);
		/*     */}
	/*     */
	/*     */public String getVname() {
		/* 127 */return ((String) getAttributeValue(VNAME));
		/*     */}
	/*     */
	/*     */public void setVname2(String vname2) {
		/* 131 */setAttributeValue(VNAME2, vname2);
		/*     */}
	/*     */
	/*     */public String getVname2() {
		/* 135 */return ((String) getAttributeValue(VNAME2));
		/*     */}
	/*     */
	/*     */public void setVname3(String vname3) {
		/* 139 */setAttributeValue(VNAME3, vname3);
		/*     */}
	/*     */
	/*     */public String getVname3() {
		/* 143 */return ((String) getAttributeValue(VNAME3));
		/*     */}
	/*     */
	/*     */public void setVname4(String vname4) {
		/* 147 */setAttributeValue(VNAME4, vname4);
		/*     */}
	/*     */
	/*     */public String getVname4() {
		/* 151 */return ((String) getAttributeValue(VNAME4));
		/*     */}
	/*     */
	/*     */public void setVname5(String vname5) {
		/* 155 */setAttributeValue(VNAME5, vname5);
		/*     */}
	/*     */
	/*     */public String getVname5() {
		/* 159 */return ((String) getAttributeValue(VNAME5));
		/*     */}
	/*     */
	/*     */public void setVname6(String vname6) {
		/* 163 */setAttributeValue(VNAME6, vname6);
		/*     */}
	/*     */
	/*     */public String getVname6() {
		/* 167 */return ((String) getAttributeValue(VNAME6));
		/*     */}
	/*     */
	/*     */public void setIusability(Integer iusability) {
		/* 171 */setAttributeValue(IUSABILITY, iusability);
		/*     */}
	/*     */
	/*     */public Integer getIusability() {
		/* 175 */return ((Integer) getAttributeValue(IUSABILITY));
		/*     */}
	/*     */
	/*     */public void setVmemo(String vmemo) {
		/* 179 */setAttributeValue(VMEMO, vmemo);
		/*     */}
	/*     */
	/*     */public String getVmemo() {
		/* 183 */return ((String) getAttributeValue(VMEMO));
		/*     */}
	/*     */
	/*     */public void setSealflag(UFBoolean sealflag) {
		/* 187 */setAttributeValue(SEALFLAG, sealflag);
		/*     */}
	/*     */
	/*     */public UFBoolean getSealflag() {
		/* 191 */return ((UFBoolean) getAttributeValue(SEALFLAG));
		/*     */}
	/*     */
	/*     */public void setBisqcdefault(UFBoolean bisqcdefault) {
		/* 195 */setAttributeValue(BISQCDEFAULT, bisqcdefault);
		/*     */}
	/*     */
	/*     */public UFBoolean getBisqcdefault() {
		/* 199 */return ((UFBoolean) getAttributeValue(BISQCDEFAULT));
		/*     */}
	/*     */
	/*     */public UFDateTime getTs() {
		/* 203 */return ((UFDateTime) getAttributeValue("ts"));
		/*     */}
	/*     */
	/*     */public void setTs(UFDateTime value) {
		/* 207 */setAttributeValue("ts", value);
		/*     */}
	/*     */
	/*     */public Integer getDr() {
		/* 211 */return ((Integer) getAttributeValue("dr"));
		/*     */}
	/*     */
	/*     */public void setDr(Integer value) {
		/* 215 */setAttributeValue("dr", value);
		/*     */}
	/*     */
}
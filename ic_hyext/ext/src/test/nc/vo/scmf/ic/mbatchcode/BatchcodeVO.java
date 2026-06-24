/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*      */package nc.vo.scmf.ic.mbatchcode;
/*      */
/*      */import nc.vo.pub.IVOMeta;
/*      */
import nc.vo.pub.SuperVO;
/*      */
import nc.vo.pub.lang.UFBoolean;
/*      */
import nc.vo.pub.lang.UFDate;
/*      */
import nc.vo.pub.lang.UFDateTime;
/*      */
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;
/*      */
import nc.vo.scmf.ic.hash.entity.ISCMFHashCodeVO;
/*      */
/*      */public class BatchcodeVO extends SuperVO
/*      */implements ISCMFHashCodeVO
/*      */{
	/*   14 */public static final String[] batchAllFields = {
	/*   15 */"pk_batchcode", "cmaterialvid",
	/*   16 */"cmaterialoid", "vbatchcode",
	/*   17 */"vvendbatchcode", "tchecktime",
	/*   18 */"cqualitylevelid", "dproducedate",
	/*   19 */"dvalidate", "bseal", "tbatchtime",
	/*   20 */"vnote", "binqc", "csourcetype",
	/*   21 */"vsourcebillcode", "vsourcerowno",
	/*   22 */"csourcebid", "csourcehid", "version",
	/*   23 */"vdef1", "vdef2", "vdef3", "vdef4",
	/*   24 */"vdef5", "vdef6", "vdef7", "vdef8",
	/*   25 */"vdef9", "vdef10", "vdef11",
	/*   26 */"vdef12", "vdef13", "vdef14",
	/*   27 */"vdef15", "vdef16", "vdef17",
	/*   28 */"vdef18", "vdef19", "vdef20",
	/*   29 */"pk_corp", "pk_group", "ts",
	/*   30 */"vprodbatchcode", "dinbounddate",
	/*   32 */"vdisinfectlot", "pk_supplier",
	/*   33 */"pk_agent", "cemployeeid",
	/*   34 */"nkhchbprc", "pk_batchcodeold_148"};
	/*      */
	/*   39 */public static final String[] batchSelFields = {
	/*   40 */"pk_group", "pk_batchcode", "cmaterialvid",
	/*   41 */"cmaterialoid", "vbatchcode",
	/*   42 */"vvendbatchcode", "tchecktime",
	/*   43 */"cqualitylevelid", "dproducedate",
	/*   44 */"dvalidate", "bseal", "tbatchtime",
	/*   45 */"vnote", "binqc", "version",
	/*   46 */"vdef1", "vdef2", "vdef3", "vdef4",
	/*   47 */"vdef5", "vdef6", "vdef7", "vdef8",
	/*   48 */"vdef9", "vdef10", "vdef11",
	/*   49 */"vdef12", "vdef13", "vdef14",
	/*   50 */"vdef15", "vdef16", "vdef17",
	/*   51 */"vdef18", "vdef19", "vdef20", "ts",
	/*   52 */"vprodbatchcode", "dinbounddate"};
	/*      */public static final String BINQC = "binqc";
	/*      */public static final String BSEAL = "bseal";
	/*      */public static final String CMATERIALOID = "cmaterialoid";
	/*      */public static final String CMATERIALVID = "cmaterialvid";
	/*      */public static final String VDISINFECTLOT = "vdisinfectlot";
	/*      */public static final String PK_SUPPLIER = "pk_supplier";
	/*      */public static final String PK_AGENT = "pk_agent";
	/*      */public static final String CEMPLOYEEID = "cemployeeid";
	/*      */public static final String NKHCHBPRC = "nkhchbprc";
	/*      */public static final String PK_BATCHCODEOLD = "pk_batchcodeold_148";
	/*      */public static final String CQUALITYLEVELID = "cqualitylevelid";
	/*      */public static final String CSOURCEBID = "csourcebid";
	/*      */public static final String CSOURCEHID = "csourcehid";
	/*      */public static final String CSOURCETYPE = "csourcetype";
	/*      */public static final String DPRODUCEDATE = "dproducedate";
	/*      */public static final String DVALIDATE = "dvalidate";
	/*  102 */public static final String[] fieldsChanged = {
	/*  103 */"vbatchcode", "vvendbatchcode",
	/*  104 */"cqualitylevelid", "dproducedate",
	/*  105 */"dvalidate", "vnote", "vdef1",
	/*  106 */"vdef2", "vdef3", "vdef4", "vdef5",
	/*  107 */"vdef6", "vdef7", "vdef8", "vdef9",
	/*  108 */"vdef10", "vdef11", "vdef12",
	/*  109 */"vdef13", "vdef14", "vdef15",
	/*  110 */"vdef16", "vdef17", "vdef18",
	/*  111 */"vdef19", "vdef20", "vprodbatchcode",
	/*  112 */"binqc", "tchecktime", "dinbounddate"};
	
	public static final String[] fieldsFoUpdate = {
	"vbatchcode", "vvendbatchcode",
		"cqualitylevelid",/* "dproducedate",
		"dvalidate", */"vnote", "vdef1",
		"vdef2", "vdef3", "vdef4", "vdef5",
		"vdef6", "vdef7", "vdef8", "vdef9",
		"vdef10", "vdef11", "vdef12",
		"vdef13", "vdef14", "vdef15",
		"vdef16", "vdef17", "vdef18",
		"vdef19", "vdef20", "vprodbatchcode",
		"binqc", "tchecktime", "dinbounddate"};
	/*      */public static final String PK_BATCHCODE = "pk_batchcode";
	/*      */public static final String PK_CORP = "pk_corp";
	/*      */public static final String PK_DEFDOC1 = "pk_defdoc1";
	/*      */public static final String PK_DEFDOC10 = "pk_defdoc10";
	/*      */public static final String PK_DEFDOC11 = "pk_defdoc11";
	/*      */public static final String PK_DEFDOC12 = "pk_defdoc12";
	/*      */public static final String PK_DEFDOC13 = "pk_defdoc13";
	/*      */public static final String PK_DEFDOC14 = "pk_defdoc14";
	/*      */public static final String PK_DEFDOC15 = "pk_defdoc15";
	/*      */public static final String PK_DEFDOC16 = "pk_defdoc16";
	/*      */public static final String PK_DEFDOC17 = "pk_defdoc17";
	/*      */public static final String PK_DEFDOC18 = "pk_defdoc18";
	/*      */public static final String PK_DEFDOC19 = "pk_defdoc19";
	/*      */public static final String PK_DEFDOC2 = "pk_defdoc2";
	/*      */public static final String PK_DEFDOC20 = "pk_defdoc20";
	/*      */public static final String PK_DEFDOC3 = "pk_defdoc3";
	/*      */public static final String PK_DEFDOC4 = "pk_defdoc4";
	/*      */public static final String PK_DEFDOC5 = "pk_defdoc5";
	/*      */public static final String PK_DEFDOC6 = "pk_defdoc6";
	/*      */public static final String PK_DEFDOC7 = "pk_defdoc7";
	/*      */public static final String PK_DEFDOC8 = "pk_defdoc8";
	/*      */public static final String PK_DEFDOC9 = "pk_defdoc9";
	/*      */public static final String PK_GROUP = "pk_group";
	/*  213 */public static final String[] sourcefields = {
	/*  214 */"csourcetype", "vsourcebillcode",
	/*  215 */"vsourcerowno", "csourcebid", "csourcehid"};
	/*      */public static final String TBATCHTIME = "tbatchtime";
	/*      */public static final String TCHECKTIME = "tchecktime";
	/*      */public static final String TS = "ts";
	/*      */public static final String VBATCHCODE = "vbatchcode";
	/*      */public static final String VDEF1 = "vdef1";
	/*      */public static final String VDEF10 = "vdef10";
	/*      */public static final String VDEF11 = "vdef11";
	/*      */public static final String VDEF12 = "vdef12";
	/*      */public static final String VDEF13 = "vdef13";
	/*      */public static final String VDEF14 = "vdef14";
	/*      */public static final String VDEF15 = "vdef15";
	/*      */public static final String VDEF16 = "vdef16";
	/*      */public static final String VDEF17 = "vdef17";
	/*      */public static final String VDEF18 = "vdef18";
	/*      */public static final String VDEF19 = "vdef19";
	/*      */public static final String VDEF2 = "vdef2";
	/*      */public static final String VDEF20 = "vdef20";
	/*      */public static final String VDEF3 = "vdef3";
	/*      */public static final String VDEF4 = "vdef4";
	/*      */public static final String VDEF5 = "vdef5";
	/*      */public static final String VDEF6 = "vdef6";
	/*      */public static final String VDEF7 = "vdef7";
	/*      */public static final String VDEF8 = "vdef8";
	/*      */public static final String VDEF9 = "vdef9";
	/*      */public static final String VERSION = "version";
	/*      */public static final String VHASHCODE = "vhashcode";
	/*      */public static final String VNOTE = "vnote";
	/*      */public static final String VPRODBATCHCODE = "vprodbatchcode";
	/*      */public static final String VSOURCEBILLCODE = "vsourcebillcode";
	/*      */public static final String VSOURCEROWNO = "vsourcerowno";
	/*      */public static final String VVENDBATCHCODE = "vvendbatchcode";
	/*      */public static final String DINBOUNDDATE = "dinbounddate";
	/*      */private static final long serialVersionUID = -6030723983611942470L;
	/*      */public static final String TABLEALIASNAME = "batchcode";
	/*      */
	/*      */public UFBoolean getBinqc()
	/*      */{
		/*  353 */return ((UFBoolean) getAttributeValue("binqc"));
		/*      */}
	/*      */
	/*      */public UFBoolean getBseal()
	/*      */{
		/*  359 */return ((UFBoolean) getAttributeValue("bseal"));
		/*      */}
	/*      */
	/*      */public String getCmaterialoid()
	/*      */{
		/*  365 */return ((String) getAttributeValue("cmaterialoid"));
		/*      */}
	/*      */
	/*      */public String getCmaterialvid()
	/*      */{
		/*  371 */return ((String) getAttributeValue("cmaterialvid"));
		/*      */}
	/*      */
	/*      */public String getCqualitylevelid()
	/*      */{
		/*  377 */return ((String) getAttributeValue("cqualitylevelid"));
		/*      */}
	/*      */
	/*      */public String getCsourcebillbid()
	/*      */{
		/*  382 */return ((String) getAttributeValue("csourcebid"));
		/*      */}
	/*      */
	/*      */public String getCsourcebillhid() {
		/*  386 */return ((String) getAttributeValue("csourcehid"));
		/*      */}
	/*      */
	/*      */public String getCsourcetype() {
		/*  390 */return ((String) getAttributeValue("csourcetype"));
		/*      */}
	/*      */
	/*      */public UFDate getDproducedate()
	/*      */{
		/*  395 */return ((UFDate) getAttributeValue("dproducedate"));
		/*      */}
	/*      */
	/*      */public UFDate getDvalidate()
	/*      */{
		/*  401 */return ((UFDate) getAttributeValue("dvalidate"));
		/*      */}
	/*      */
	/*      */public String getHashCode()
	/*      */{
		/*  407 */return getVhashcode();
		/*      */}
	/*      */
	/*      */public String[] getHashContentFields()
	/*      */{
		/*  412 */return fieldsChanged;
		/*      */}
	/*      */
	/*      */public IVOMeta getMetaData()
	/*      */{
		/*  417 */IVOMeta meta = VOMetaFactory.getInstance().getVOMeta(
				"ic.BatchcodeVO");
		/*  418 */return meta;
		/*      */}
	/*      */
	/*      */public String getPk_batchcode()
	/*      */{
		/*  423 */return ((String) getAttributeValue("pk_batchcode"));
		/*      */}
	/*      */
	/*      */public String getPk_corp()
	/*      */{
		/*  429 */return ((String) getAttributeValue("pk_corp"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc1()
	/*      */{
		/*  435 */return ((String) getAttributeValue("pk_defdoc1"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc10()
	/*      */{
		/*  441 */return ((String) getAttributeValue("pk_defdoc10"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc11()
	/*      */{
		/*  447 */return ((String) getAttributeValue("pk_defdoc11"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc12()
	/*      */{
		/*  453 */return ((String) getAttributeValue("pk_defdoc12"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc13()
	/*      */{
		/*  459 */return ((String) getAttributeValue("pk_defdoc13"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc14()
	/*      */{
		/*  465 */return ((String) getAttributeValue("pk_defdoc14"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc15()
	/*      */{
		/*  471 */return ((String) getAttributeValue("pk_defdoc15"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc16()
	/*      */{
		/*  477 */return ((String) getAttributeValue("pk_defdoc16"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc17()
	/*      */{
		/*  483 */return ((String) getAttributeValue("pk_defdoc17"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc18()
	/*      */{
		/*  489 */return ((String) getAttributeValue("pk_defdoc18"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc19()
	/*      */{
		/*  495 */return ((String) getAttributeValue("pk_defdoc19"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc2()
	/*      */{
		/*  501 */return ((String) getAttributeValue("pk_defdoc2"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc20()
	/*      */{
		/*  507 */return ((String) getAttributeValue("pk_defdoc20"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc3()
	/*      */{
		/*  513 */return ((String) getAttributeValue("pk_defdoc3"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc4()
	/*      */{
		/*  519 */return ((String) getAttributeValue("pk_defdoc4"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc5()
	/*      */{
		/*  525 */return ((String) getAttributeValue("pk_defdoc5"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc6()
	/*      */{
		/*  531 */return ((String) getAttributeValue("pk_defdoc6"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc7()
	/*      */{
		/*  537 */return ((String) getAttributeValue("pk_defdoc7"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc8()
	/*      */{
		/*  543 */return ((String) getAttributeValue("pk_defdoc8"));
		/*      */}
	/*      */
	/*      */public String getPk_defdoc9()
	/*      */{
		/*  549 */return ((String) getAttributeValue("pk_defdoc9"));
		/*      */}
	/*      */
	/*      */public String getPk_group()
	/*      */{
		/*  555 */return ((String) getAttributeValue("pk_group"));
		/*      */}
	/*      */
	/*      */public String getTableName()
	/*      */{
		/*  561 */return "scm_batchcode";
		/*      */}
	/*      */
	/*      */public UFDateTime getTbatchtime()
	/*      */{
		/*  566 */return ((UFDateTime) getAttributeValue("tbatchtime"));
		/*      */}
	/*      */
	/*      */public UFDateTime getTchecktime()
	/*      */{
		/*  572 */return ((UFDateTime) getAttributeValue("tchecktime"));
		/*      */}
	/*      */
	/*      */public UFDateTime getTs()
	/*      */{
		/*  578 */return ((UFDateTime) getAttributeValue("ts"));
		/*      */}
	/*      */
	/*      */public String getVbatchcode()
	/*      */{
		/*  584 */return ((String) getAttributeValue("vbatchcode"));
		/*      */}
	/*      */
	/*      */public String getVdef1()
	/*      */{
		/*  590 */return ((String) getAttributeValue("vdef1"));
		/*      */}
	/*      */
	/*      */public String getVdef10()
	/*      */{
		/*  596 */return ((String) getAttributeValue("vdef10"));
		/*      */}
	/*      */
	/*      */public String getVdef11()
	/*      */{
		/*  602 */return ((String) getAttributeValue("vdef11"));
		/*      */}
	/*      */
	/*      */public String getVdef12()
	/*      */{
		/*  608 */return ((String) getAttributeValue("vdef12"));
		/*      */}
	/*      */
	/*      */public String getVdef13()
	/*      */{
		/*  614 */return ((String) getAttributeValue("vdef13"));
		/*      */}
	/*      */
	/*      */public String getVdef14()
	/*      */{
		/*  620 */return ((String) getAttributeValue("vdef14"));
		/*      */}
	/*      */
	/*      */public String getVdef15()
	/*      */{
		/*  626 */return ((String) getAttributeValue("vdef15"));
		/*      */}
	/*      */
	/*      */public String getVdef16()
	/*      */{
		/*  632 */return ((String) getAttributeValue("vdef16"));
		/*      */}
	/*      */
	/*      */public String getVdef17()
	/*      */{
		/*  638 */return ((String) getAttributeValue("vdef17"));
		/*      */}
	/*      */
	/*      */public String getVdef18()
	/*      */{
		/*  644 */return ((String) getAttributeValue("vdef18"));
		/*      */}
	/*      */
	/*      */public String getVdef19()
	/*      */{
		/*  650 */return ((String) getAttributeValue("vdef19"));
		/*      */}
	/*      */
	/*      */public String getVdef2()
	/*      */{
		/*  656 */return ((String) getAttributeValue("vdef2"));
		/*      */}
	/*      */
	/*      */public String getVdef20()
	/*      */{
		/*  662 */return ((String) getAttributeValue("vdef20"));
		/*      */}
	/*      */
	/*      */public String getVdef3()
	/*      */{
		/*  668 */return ((String) getAttributeValue("vdef3"));
		/*      */}
	/*      */
	/*      */public String getVdef4()
	/*      */{
		/*  674 */return ((String) getAttributeValue("vdef4"));
		/*      */}
	/*      */
	/*      */public String getVdef5()
	/*      */{
		/*  680 */return ((String) getAttributeValue("vdef5"));
		/*      */}
	/*      */
	/*      */public String getVdef6()
	/*      */{
		/*  686 */return ((String) getAttributeValue("vdef6"));
		/*      */}
	/*      */
	/*      */public String getVdef7()
	/*      */{
		/*  692 */return ((String) getAttributeValue("vdef7"));
		/*      */}
	/*      */
	/*      */public String getVdef8()
	/*      */{
		/*  698 */return ((String) getAttributeValue("vdef8"));
		/*      */}
	/*      */
	/*      */public String getVdef9()
	/*      */{
		/*  704 */return ((String) getAttributeValue("vdef9"));
		/*      */}
	/*      */
	/*      */public Integer getVersion()
	/*      */{
		/*  710 */return ((Integer) getAttributeValue("version"));
		/*      */}
	/*      */
	/*      */public String getVhashcode()
	/*      */{
		/*  715 */return ((String) getAttributeValue("vhashcode"));
		/*      */}
	/*      */
	/*      */public String getVnote()
	/*      */{
		/*  720 */return ((String) getAttributeValue("vnote"));
		/*      */}
	/*      */
	/*      */public String getVprodbatchcode()
	/*      */{
		/*  725 */return ((String) getAttributeValue("vprodbatchcode"));
		/*      */}
	/*      */
	/*      */public String getVsourcebillcode() {
		/*  729 */return ((String) getAttributeValue("vsourcebillcode"));
		/*      */}
	/*      */
	/*      */public String getVsourcerowno() {
		/*  733 */return ((String) getAttributeValue("vsourcerowno"));
		/*      */}
	/*      */
	/*      */public String getVvendbatchcode()
	/*      */{
		/*  738 */return ((String) getAttributeValue("vvendbatchcode"));
		/*      */}
	/*      */
	/*      */public UFDate getDinbounddate()
	/*      */{
		/*  743 */return ((UFDate) getAttributeValue("dinbounddate"));
		/*      */}
	/*      */
	/*      */public void setBinqc(UFBoolean binqc)
	/*      */{
		/*  748 */setAttributeValue("binqc", binqc);
		/*      */}
	/*      */
	/*      */public void setBseal(UFBoolean bseal)
	/*      */{
		/*  754 */setAttributeValue("bseal", bseal);
		/*      */}
	/*      */
	/*      */public void setCmaterialoid(String cmaterialoid)
	/*      */{
		/*  760 */setAttributeValue("cmaterialoid", cmaterialoid);
		/*      */}
	/*      */
	/*      */public void setCmaterialvid(String cmaterialvid)
	/*      */{
		/*  779 */setAttributeValue("cmaterialvid", cmaterialvid);
		/*      */}
	/*      */
	/*      */public void setCqualitylevelid(String cqualitylevelid)
	/*      */{
		/*  785 */setAttributeValue("cqualitylevelid", cqualitylevelid);
		/*      */}
	/*      */
	/*      */public void setCsourcebillbid(String bid)
	/*      */{
		/*  790 */setAttributeValue("csourcebid", bid);
		/*      */}
	/*      */
	/*      */public void setCsourcebillhid(String hid) {
		/*  794 */setAttributeValue("csourcehid", hid);
		/*      */}
	/*      */
	/*      */public void setCsourcetype(String csourcetype) {
		/*  798 */setAttributeValue("csourcetype", csourcetype);
		/*      */}
	/*      */
	/*      */public void setDproducedate(UFDate dproducedate)
	/*      */{
		/*  803 */setAttributeValue("dproducedate", dproducedate);
		/*      */}
	/*      */
	/*      */public void setDvalidate(UFDate dvalidate)
	/*      */{
		/*  809 */setAttributeValue("dvalidate", dvalidate);
		/*      */}
	/*      */
	/*      */public void setHashCode(String vcode)
	/*      */{
		/*  815 */setVhashcode(vcode);
		/*      */}
	/*      */
	/*      */public void setPk_batchcode(String pk_batchcode)
	/*      */{
		/*  821 */setAttributeValue("pk_batchcode", pk_batchcode);
		/*      */}
	/*      */
	/*      */public void setPk_corp(String pk_corp)
	/*      */{
		/*  827 */setAttributeValue("pk_corp", pk_corp);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc1(String pk_defdoc1)
	/*      */{
		/*  833 */setAttributeValue("pk_defdoc1", pk_defdoc1);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc10(String pk_defdoc10)
	/*      */{
		/*  839 */setAttributeValue("pk_defdoc10", pk_defdoc10);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc11(String pk_defdoc11)
	/*      */{
		/*  845 */setAttributeValue("pk_defdoc11", pk_defdoc11);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc12(String pk_defdoc12)
	/*      */{
		/*  851 */setAttributeValue("pk_defdoc12", pk_defdoc12);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc13(String pk_defdoc13)
	/*      */{
		/*  857 */setAttributeValue("pk_defdoc13", pk_defdoc13);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc14(String pk_defdoc14)
	/*      */{
		/*  863 */setAttributeValue("pk_defdoc14", pk_defdoc14);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc15(String pk_defdoc15)
	/*      */{
		/*  869 */setAttributeValue("pk_defdoc15", pk_defdoc15);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc16(String pk_defdoc16)
	/*      */{
		/*  875 */setAttributeValue("pk_defdoc16", pk_defdoc16);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc17(String pk_defdoc17)
	/*      */{
		/*  881 */setAttributeValue("pk_defdoc17", pk_defdoc17);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc18(String pk_defdoc18)
	/*      */{
		/*  887 */setAttributeValue("pk_defdoc18", pk_defdoc18);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc19(String pk_defdoc19)
	/*      */{
		/*  893 */setAttributeValue("pk_defdoc19", pk_defdoc19);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc2(String pk_defdoc2)
	/*      */{
		/*  899 */setAttributeValue("pk_defdoc2", pk_defdoc2);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc20(String pk_defdoc20)
	/*      */{
		/*  905 */setAttributeValue("pk_defdoc20", pk_defdoc20);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc3(String pk_defdoc3)
	/*      */{
		/*  911 */setAttributeValue("pk_defdoc3", pk_defdoc3);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc4(String pk_defdoc4)
	/*      */{
		/*  917 */setAttributeValue("pk_defdoc4", pk_defdoc4);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc5(String pk_defdoc5)
	/*      */{
		/*  923 */setAttributeValue("pk_defdoc5", pk_defdoc5);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc6(String pk_defdoc6)
	/*      */{
		/*  929 */setAttributeValue("pk_defdoc6", pk_defdoc6);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc7(String pk_defdoc7)
	/*      */{
		/*  935 */setAttributeValue("pk_defdoc7", pk_defdoc7);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc8(String pk_defdoc8)
	/*      */{
		/*  941 */setAttributeValue("pk_defdoc8", pk_defdoc8);
		/*      */}
	/*      */
	/*      */public void setPk_defdoc9(String pk_defdoc9)
	/*      */{
		/*  947 */setAttributeValue("pk_defdoc9", pk_defdoc9);
		/*      */}
	/*      */
	/*      */public void setPk_group(String pk_group)
	/*      */{
		/*  953 */setAttributeValue("pk_group", pk_group);
		/*      */}
	/*      */
	/*      */public void setTbatchtime(UFDateTime tbatchtime)
	/*      */{
		/*  959 */setAttributeValue("tbatchtime", tbatchtime);
		/*      */}
	/*      */
	/*      */public void setTchecktime(UFDateTime tchecktime)
	/*      */{
		/*  965 */setAttributeValue("tchecktime", tchecktime);
		/*      */}
	/*      */
	/*      */public void setTs(UFDateTime ts)
	/*      */{
		/*  971 */setAttributeValue("ts", ts);
		/*      */}
	/*      */
	/*      */public void setVbatchcode(String vbatchcode)
	/*      */{
		/*  977 */setAttributeValue("vbatchcode", vbatchcode);
		/*      */}
	/*      */
	/*      */public void setVdef1(String vdef1)
	/*      */{
		/*  983 */setAttributeValue("vdef1", vdef1);
		/*      */}
	/*      */
	/*      */public void setVdef10(String vdef10)
	/*      */{
		/*  989 */setAttributeValue("vdef10", vdef10);
		/*      */}
	/*      */
	/*      */public void setVdef11(String vdef11)
	/*      */{
		/*  995 */setAttributeValue("vdef11", vdef11);
		/*      */}
	/*      */
	/*      */public void setVdef12(String vdef12)
	/*      */{
		/* 1001 */setAttributeValue("vdef12", vdef12);
		/*      */}
	/*      */
	/*      */public void setVdef13(String vdef13)
	/*      */{
		/* 1007 */setAttributeValue("vdef13", vdef13);
		/*      */}
	/*      */
	/*      */public void setVdef14(String vdef14)
	/*      */{
		/* 1013 */setAttributeValue("vdef14", vdef14);
		/*      */}
	/*      */
	/*      */public void setVdef15(String vdef15)
	/*      */{
		/* 1019 */setAttributeValue("vdef15", vdef15);
		/*      */}
	/*      */
	/*      */public void setVdef16(String vdef16)
	/*      */{
		/* 1025 */setAttributeValue("vdef16", vdef16);
		/*      */}
	/*      */
	/*      */public void setVdef17(String vdef17)
	/*      */{
		/* 1031 */setAttributeValue("vdef17", vdef17);
		/*      */}
	/*      */
	/*      */public void setVdef18(String vdef18)
	/*      */{
		/* 1037 */setAttributeValue("vdef18", vdef18);
		/*      */}
	/*      */
	/*      */public void setVdef19(String vdef19)
	/*      */{
		/* 1043 */setAttributeValue("vdef19", vdef19);
		/*      */}
	/*      */
	/*      */public void setVdef2(String vdef2)
	/*      */{
		/* 1049 */setAttributeValue("vdef2", vdef2);
		/*      */}
	/*      */
	/*      */public void setVdef20(String vdef20)
	/*      */{
		/* 1055 */setAttributeValue("vdef20", vdef20);
		/*      */}
	/*      */
	/*      */public void setVdef3(String vdef3)
	/*      */{
		/* 1061 */setAttributeValue("vdef3", vdef3);
		/*      */}
	/*      */
	/*      */public void setVdef4(String vdef4)
	/*      */{
		/* 1067 */setAttributeValue("vdef4", vdef4);
		/*      */}
	/*      */
	/*      */public void setVdef5(String vdef5)
	/*      */{
		/* 1073 */setAttributeValue("vdef5", vdef5);
		/*      */}
	/*      */
	/*      */public void setVdef6(String vdef6)
	/*      */{
		/* 1079 */setAttributeValue("vdef6", vdef6);
		/*      */}
	/*      */
	/*      */public void setVdef7(String vdef7)
	/*      */{
		/* 1085 */setAttributeValue("vdef7", vdef7);
		/*      */}
	/*      */
	/*      */public void setVdef8(String vdef8)
	/*      */{
		/* 1091 */setAttributeValue("vdef8", vdef8);
		/*      */}
	/*      */
	/*      */public void setVdef9(String vdef9)
	/*      */{
		/* 1097 */setAttributeValue("vdef9", vdef9);
		/*      */}
	/*      */
	/*      */public void setVersion(Integer version)
	/*      */{
		/* 1103 */setAttributeValue("version", version);
		/*      */}
	/*      */
	/*      */public void setVhashcode(String vhashcode)
	/*      */{
		/* 1108 */setAttributeValue("vhashcode", vhashcode);
		/*      */}
	/*      */
	/*      */public void setVnote(String vnote)
	/*      */{
		/* 1113 */setAttributeValue("vnote", vnote);
		/*      */}
	/*      */
	/*      */public void setVprodbatchcode(String vprodbatchcode)
	/*      */{
		/* 1118 */setAttributeValue("vprodbatchcode", vprodbatchcode);
		/*      */}
	/*      */
	/*      */public void setVsourcebillcode(String billcode) {
		/* 1122 */setAttributeValue("vsourcebillcode", billcode);
		/*      */}
	/*      */
	/*      */public void setVsourcerowno(String value) {
		/* 1126 */setAttributeValue("vsourcerowno", value);
		/*      */}
	/*      */
	/*      */public void setVvendbatchcode(String vvendbatchcode)
	/*      */{
		/* 1131 */setAttributeValue("vvendbatchcode", vvendbatchcode);
		/*      */}
	/*      */
	/*      */public void setDinbounddate(UFDate dinbounddate)
	/*      */{
		/* 1136 */setAttributeValue("dinbounddate", dinbounddate);
		/*      */}
	/*      */
}
/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
/*      */package nc.vo.bd.material;
/*      */
/*      */import java.util.HashMap;
/*      */
import java.util.List;
/*      */
import java.util.Map;
/*      */
import nc.vo.bd.material.cost.MaterialCostVO;
/*      */
import nc.vo.bd.material.fi.MaterialFiVO;
/*      */
import nc.vo.bd.material.plan.MaterialPlanVO;
/*      */
import nc.vo.bd.material.prod.MaterialProdVO;
/*      */
import nc.vo.bd.material.pu.MaterialPuVO;
/*      */
import nc.vo.bd.material.sale.MaterialSaleVO;
/*      */
import nc.vo.bd.material.stock.MaterialStockVO;
/*      */
import nc.vo.pub.IVOMeta;
/*      */
import nc.vo.pub.SuperVO;
/*      */
import nc.vo.pub.lang.UFBoolean;
/*      */
import nc.vo.pub.lang.UFDateTime;
/*      */
import nc.vo.pub.lang.UFDouble;
/*      */
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;
/*      */
/*      */public class MaterialVO extends SuperVO
/*      */{
	/*      */public static final String CODE = "code";
	/*      */public static final String CREATIONTIME = "creationtime";
	/*      */public static final String CREATOR = "creator";
	/*      */public static final String DATAORIGINFLAG = "dataoriginflag";
	/*      */public static final String DEF1 = "def1";
	/*      */public static final String DEF10 = "def10";
	/*      */public static final String DEF11 = "def11";
	/*      */public static final String DEF12 = "def12";
	/*      */public static final String DEF13 = "def13";
	/*      */public static final String DEF14 = "def14";
	/*      */public static final String DEF15 = "def15";
	/*      */public static final String DEF16 = "def16";
	/*      */public static final String DEF17 = "def17";
	/*      */public static final String DEF18 = "def18";
	/*      */public static final String DEF19 = "def19";
	/*      */public static final String DEF2 = "def2";
	/*      */public static final String DEF20 = "def20";
	
	/*      */public static final String DEF21 = "def21";
	/*      */public static final String DEF22 = "def22";
	/*      */public static final String DEF23 = "def23";
	/*      */public static final String DEF24 = "def24";
	/*      */public static final String DEF25 = "def25";
	/*      */public static final String DEF26 = "def26";
	/*      */public static final String DEF27 = "def27";
	/*      */public static final String DEF28 = "def28";
	/*      */public static final String DEF29 = "def29";
	/*      */public static final String DEF30 = "def30";
	
	/*      */public static final String DEF3 = "def3";
	/*      */public static final String DEF4 = "def4";
	/*      */public static final String DEF5 = "def5";
	/*      */public static final String DEF6 = "def6";
	/*      */public static final String DEF7 = "def7";
	/*      */public static final String DEF8 = "def8";
	/*      */public static final String DEF9 = "def9";
	/*      */public static final String DISCOUNTFLAG = "discountflag";
	/*      */public static final String ELECTRONICSALE = "electronicsale";
	/*      */public static final String ENABLESTATE = "enablestate";
	/*      */public static final String FEE = "fee";
	/*      */public static final String GRAPHID = "graphid";
	/*      */public static final String INTOLERANCE = "intolerance";
	/*      */public static final String ISELECTRANS = "iselectrans";
	/*      */public static final String LATEST = "latest";
	/*      */public static final String MATERIALBARCODE = "materialbarcode";
	/*      */public static final String MATERIALMGT = "materialmgt";
	/*      */public static final String MATERIALMNECODE = "materialmnecode";
	/*      */public static final String MATERIALSHORTNAME = "materialshortname";
	/*      */public static final String MATERIALSPEC = "materialspec";
	/*      */public static final String MATERIALTYPE = "materialtype";
	/*      */public static final String MEMO = "memo";
	/*      */public static final String MODIFIEDTIME = "modifiedtime";
	/*      */public static final String MODIFIER = "modifier";
	/*      */public static final String NAME = "name";
	/*      */public static final String NAME2 = "name2";
	/*      */public static final String NAME3 = "name3";
	/*      */public static final String NAME4 = "name4";
	/*      */public static final String NAME5 = "name5";
	/*      */public static final String NAME6 = "name6";
	/*      */public static final String OUTCLOSELOWERLIMIT = "outcloselowerlimit";
	/*      */public static final String OUTTOLERANCE = "outtolerance";
	/*      */public static final String PK_BRAND = "pk_brand";
	/*      */public static final String PK_GOODSCODE = "pk_goodscode";
	/*      */public static final String PK_GROUP = "pk_group";
	/*      */public static final String PK_MARASSTFRAME = "pk_marasstframe";
	/*      */public static final String PK_MARBASCLASS = "pk_marbasclass";
	/*      */public static final String PK_MATERIAL = "pk_material";
	/*      */public static final String PK_MATERIAL_PF = "pk_material_pf";
	/*      */public static final String PK_MATTAXES = "pk_mattaxes";
	/*      */public static final String PK_MEASDOC = "pk_measdoc";
	/*      */public static final String PK_ORG = "pk_org";
	/*      */public static final String PK_PRODLINE = "pk_prodline";
	/*      */public static final String PK_SOURCE = "pk_source";
	/*      */public static final String PRODAREA = "prodarea";
	/*      */public static final String PRODUCTFAMILY = "productfamily";
	/*      */public static final String PROLIFEPERIOD = "prolifeperiod";
	/*      */public static final String RETAIL = "retail";
	/*      */public static final String SETPARTSFLAG = "setpartsflag";
	/*      */public static final String STOREUNITNUM = "storeunitnum";
	/*      */public static final String UNITHEIGHT = "unitheight";
	/*      */public static final String UNITLENGTH = "unitlength";
	/*      */public static final String UNITVOLUME = "unitvolume";
	/*      */public static final String UNITWEIGHT = "unitweight";
	/*      */public static final String UNITWIDTH = "unitwidth";
	/*      */public static final String ENAME = "ename";
	/*      */public static final String VERSION = "version";
	/*      */public static final String DELETESTATE = "deletestate";
	/*      */public static final String DELPERSON = "delperson";
	/*      */public static final String DELTIME = "deltime";
	/*      */public static final String EMATERIALSPEC = "ematerialspec";
	/*      */public static final String GOODSPRTNAME = "goodsprtname";
	/*      */public static final String PICTURE = "picture";
	/*      */public static final String ISHPROITEMS = "ishproitems";
	/*      */public static final String ISFEATURE = "isfeature";
	/*      */public static final String FEATURECLASS = "featureclass";
	/*      */private static final long serialVersionUID = 2841738468098056828L;
	/*      */private String picture;
	/*      */private UFBoolean ishproitems;
	/*      */private UFBoolean isfeature;
	/*      */private String featureclass;
	/*      */private String code;
	/*      */private UFDateTime creationtime;
	/*      */private String creator;
	/*  209 */private Integer dataoriginflag = Integer.valueOf(0);
	/*      */private String def1;
	/*      */private String def10;
	/*      */private String def11;
	/*      */private String def12;
	/*      */private String def13;
	/*      */private String def14;
	/*      */private String def15;
	/*      */private String def16;
	/*      */private String def17;
	/*      */private String def18;
	/*      */private String def19;
	/*      */private String def2;
	/*      */private String def20;
	
	private String def21;
	private String def22;
	private String def23;
	private String def24;
	private String def25;
	private String def26;
	private String def27;
	private String def28;
	private String def29;
	private String def30;
	
	/*      */private String def3;
	/*      */private String def4;
	/*      */private String def5;
	/*      */private String def6;
	/*      */private String def7;
	/*      */private String def8;
	/*      */private String def9;
	/*      */private UFBoolean discountflag;
	/*  253 */private Integer dr = Integer.valueOf(0);
	/*      */private UFBoolean electronicsale;
	/*      */private Integer enablestate;
	/*      */private UFBoolean fee;
	/*      */private String graphid;
	/*      */private UFDouble intolerance;
	/*      */private UFBoolean iselectrans;
	/*      */private UFBoolean latest;
	/*      */private String materialbarcode;
	/*      */private MaterialConvertVO[] materialconvert;
	/*      */private MaterialCostVO[] materialcost;
	/*      */private MaterialFiVO[] materialfi;
	/*      */private Integer materialmgt;
	/*      */private String materialmnecode;
	/*      */private MaterialPlanVO[] materialplan;
	/*      */private MaterialProdVO[] materialprod;
	/*      */private MaterialPuVO[] materialpu;
	/*      */private MaterialSaleVO[] materialsale;
	/*      */private String materialshortname;
	/*      */private String materialspec;
	/*      */private MaterialStockVO[] materialstock;
	/*      */private MaterialTaxTypeVO[] materialtaxtype;
	/*      */private String materialtype;
	/*      */private String memo;
	/*      */private UFDateTime modifiedtime;
	/*      */private String modifier;
	/*      */private String name;
	/*      */private String name2;
	/*      */private String name3;
	/*      */private String name4;
	/*      */private String name5;
	/*      */private String name6;
	/*      */private UFDouble outcloselowerlimit;
	/*      */private UFDouble outtolerance;
	/*      */private String pk_brand;
	/*      */private String pk_goodscode;
	/*      */private String pk_group;
	/*      */private String pk_marasstframe;
	/*      */private String pk_marbasclass;
	/*      */private String pk_material;
	/*      */private String pk_material_pf;
	/*      */private String pk_mattaxes;
	/*      */private String pk_measdoc;
	/*      */private String pk_org;
	/*      */private String pk_prodline;
	/*      */private String pk_source;
	/*      */private String prodarea;
	/*      */private UFBoolean productfamily;
	/*      */private Integer prolifeperiod;
	/*      */private UFBoolean retail;
	/*      */private UFBoolean setpartsflag;
	/*      */private UFDouble storeunitnum;
	/*      */private UFDateTime ts;
	/*      */private String unitheight;
	/*      */private String unitlength;
	/*      */private UFDouble unitvolume;
	/*      */private UFDouble unitweight;
	/*      */private String unitwidth;
	/*      */private String ename;
	/*      */private Integer version;
	/*      */private Integer deletestate;
	/*      */private String delperson;
	/*      */private UFDateTime deltime;
	/*      */private String ematerialspec;
	/*      */private String goodsprtname;
	/*      */private Map<String, List<SuperVO>> exbeanname_tabvo_map;
	/*      */public static final String EXBEANNAME_TABVO_MAP = "exbeanname_tabvo_map";
	/*      */
	/*      */public static String getDefaultTableName()
	/*      */{
		/*  399 */return "bd_material";
		/*      */}
	/*      */
	/*      */public String getCode()
	/*      */{
		/*  408 */return this.code;
		/*      */}
	/*      */
	/*      */public UFDateTime getCreationtime()
	/*      */{
		/*  417 */return this.creationtime;
		/*      */}
	/*      */
	/*      */public String getCreator()
	/*      */{
		/*  426 */return this.creator;
		/*      */}
	/*      */
	/*      */public Integer getDataoriginflag()
	/*      */{
		/*  435 */return this.dataoriginflag;
		/*      */}
	/*      */
	/*      */public String getDef1()
	/*      */{
		/*  444 */return this.def1;
		/*      */}
	/*      */
	/*      */public String getDef10()
	/*      */{
		/*  453 */return this.def10;
		/*      */}
	/*      */
	/*      */public String getDef11()
	/*      */{
		/*  462 */return this.def11;
		/*      */}
	/*      */
	/*      */public String getDef12()
	/*      */{
		/*  471 */return this.def12;
		/*      */}
	/*      */
	/*      */public String getDef13()
	/*      */{
		/*  480 */return this.def13;
		/*      */}
	/*      */
	/*      */public String getDef14()
	/*      */{
		/*  489 */return this.def14;
		/*      */}
	/*      */
	/*      */public String getDef15()
	/*      */{
		/*  498 */return this.def15;
		/*      */}
	/*      */
	/*      */public String getDef16()
	/*      */{
		/*  507 */return this.def16;
		/*      */}
	/*      */
	/*      */public String getDef17()
	/*      */{
		/*  516 */return this.def17;
		/*      */}
	/*      */
	/*      */public String getDef18()
	/*      */{
		/*  525 */return this.def18;
		/*      */}
	/*      */
	/*      */public String getDef19()
	/*      */{
		/*  534 */return this.def19;
		/*      */}
	/*      */
	/*      */public String getDef2()
	/*      */{
		/*  543 */return this.def2;
		/*      */}
	/*      */
	/*      */public String getDef20()
	/*      */{
		/*  552 */return this.def20;
		/*      */}
	/*      */
	/*      */public String getDef3()
	/*      */{
		/*  561 */return this.def3;
		/*      */}
	/*      */
	/*      */public String getDef4()
	/*      */{
		/*  570 */return this.def4;
		/*      */}
	/*      */
	/*      */public String getDef5()
	/*      */{
		/*  579 */return this.def5;
		/*      */}
	/*      */
	/*      */public String getDef6()
	/*      */{
		/*  588 */return this.def6;
		/*      */}
	/*      */
	/*      */public String getDef7()
	/*      */{
		/*  597 */return this.def7;
		/*      */}
	/*      */
	/*      */public String getDef8()
	/*      */{
		/*  606 */return this.def8;
		/*      */}
	/*      */
	/*      */public String getDef9()
	/*      */{
		/*  615 */return this.def9;
		/*      */}
	/*      */
	/*      */public UFBoolean getDiscountflag()
	/*      */{
		/*  624 */return this.discountflag;
		/*      */}
	/*      */
	/*      */public Integer getDr()
	/*      */{
		/*  633 */return this.dr;
		/*      */}
	/*      */
	/*      */public UFBoolean getElectronicsale()
	/*      */{
		/*  642 */return this.electronicsale;
		/*      */}
	/*      */
	/*      */public Integer getEnablestate()
	/*      */{
		/*  651 */return this.enablestate;
		/*      */}
	/*      */
	/*      */public UFBoolean getFee()
	/*      */{
		/*  660 */return this.fee;
		/*      */}
	/*      */
	/*      */public String getGraphid()
	/*      */{
		/*  669 */return this.graphid;
		/*      */}
	/*      */
	/*      */public UFDouble getIntolerance()
	/*      */{
		/*  678 */return this.intolerance;
		/*      */}
	/*      */
	/*      */public UFBoolean getIselectrans()
	/*      */{
		/*  687 */return this.iselectrans;
		/*      */}
	/*      */
	/*      */public UFBoolean getLatest()
	/*      */{
		/*  696 */return this.latest;
		/*      */}
	/*      */
	/*      */public String getMaterialbarcode()
	/*      */{
		/*  705 */return this.materialbarcode;
		/*      */}
	/*      */
	/*      */public MaterialConvertVO[] getMaterialconvert()
	/*      */{
		/*  714 */return this.materialconvert;
		/*      */}
	/*      */
	/*      */public MaterialCostVO[] getMaterialcost()
	/*      */{
		/*  723 */return this.materialcost;
		/*      */}
	/*      */
	/*      */public MaterialFiVO[] getMaterialfi()
	/*      */{
		/*  732 */return this.materialfi;
		/*      */}
	/*      */
	/*      */public Integer getMaterialmgt()
	/*      */{
		/*  741 */return this.materialmgt;
		/*      */}
	/*      */
	/*      */public String getMaterialmnecode()
	/*      */{
		/*  750 */return this.materialmnecode;
		/*      */}
	/*      */
	/*      */public MaterialPlanVO[] getMaterialplan()
	/*      */{
		/*  759 */return this.materialplan;
		/*      */}
	/*      */
	/*      */public MaterialProdVO[] getMaterialprod()
	/*      */{
		/*  768 */return this.materialprod;
		/*      */}
	/*      */
	/*      */public MaterialPuVO[] getMaterialpu()
	/*      */{
		/*  777 */return this.materialpu;
		/*      */}
	/*      */
	/*      */public MaterialSaleVO[] getMaterialsale()
	/*      */{
		/*  786 */return this.materialsale;
		/*      */}
	/*      */
	/*      */public String getMaterialshortname()
	/*      */{
		/*  795 */return this.materialshortname;
		/*      */}
	/*      */
	/*      */public String getMaterialspec()
	/*      */{
		/*  804 */return this.materialspec;
		/*      */}
	/*      */
	/*      */public MaterialStockVO[] getMaterialstock()
	/*      */{
		/*  813 */return this.materialstock;
		/*      */}
	/*      */
	/*      */public MaterialTaxTypeVO[] getMaterialtaxtype()
	/*      */{
		/*  822 */return this.materialtaxtype;
		/*      */}
	/*      */
	/*      */public String getMaterialtype()
	/*      */{
		/*  831 */return this.materialtype;
		/*      */}
	/*      */
	/*      */public String getMemo()
	/*      */{
		/*  840 */return this.memo;
		/*      */}
	/*      */
	/*      */public UFDateTime getModifiedtime()
	/*      */{
		/*  849 */return this.modifiedtime;
		/*      */}
	/*      */
	/*      */public String getModifier()
	/*      */{
		/*  858 */return this.modifier;
		/*      */}
	/*      */
	/*      */public String getName()
	/*      */{
		/*  867 */return this.name;
		/*      */}
	/*      */
	/*      */public String getName2()
	/*      */{
		/*  876 */return this.name2;
		/*      */}
	/*      */
	/*      */public String getName3()
	/*      */{
		/*  885 */return this.name3;
		/*      */}
	/*      */
	/*      */public String getName4()
	/*      */{
		/*  894 */return this.name4;
		/*      */}
	/*      */
	/*      */public String getName5()
	/*      */{
		/*  903 */return this.name5;
		/*      */}
	/*      */
	/*      */public String getName6()
	/*      */{
		/*  912 */return this.name6;
		/*      */}
	/*      */
	/*      */public UFDouble getOutcloselowerlimit()
	/*      */{
		/*  921 */return this.outcloselowerlimit;
		/*      */}
	/*      */
	/*      */public UFDouble getOuttolerance()
	/*      */{
		/*  930 */return this.outtolerance;
		/*      */}
	/*      */
	/*      */public String getParentPKFieldName()
	/*      */{
		/*  943 */return null;
		/*      */}
	/*      */
	/*      */public String getPk_brand()
	/*      */{
		/*  952 */return this.pk_brand;
		/*      */}
	/*      */
	/*      */public String getPk_goodscode()
	/*      */{
		/*  961 */return this.pk_goodscode;
		/*      */}
	/*      */
	/*      */public String getPk_group()
	/*      */{
		/*  970 */return this.pk_group;
		/*      */}
	/*      */
	/*      */public String getPk_marasstframe()
	/*      */{
		/*  979 */return this.pk_marasstframe;
		/*      */}
	/*      */
	/*      */public String getPk_marbasclass()
	/*      */{
		/*  988 */return this.pk_marbasclass;
		/*      */}
	/*      */
	/*      */public String getPk_material()
	/*      */{
		/*  997 */return this.pk_material;
		/*      */}
	/*      */
	/*      */public String getPk_material_pf()
	/*      */{
		/* 1006 */return this.pk_material_pf;
		/*      */}
	/*      */
	/*      */public String getPk_mattaxes()
	/*      */{
		/* 1015 */return this.pk_mattaxes;
		/*      */}
	/*      */
	/*      */public String getPk_measdoc()
	/*      */{
		/* 1024 */return this.pk_measdoc;
		/*      */}
	/*      */
	/*      */public String getPk_org()
	/*      */{
		/* 1033 */return this.pk_org;
		/*      */}
	/*      */
	/*      */public String getPk_prodline()
	/*      */{
		/* 1042 */return this.pk_prodline;
		/*      */}
	/*      */
	/*      */public String getPk_source()
	/*      */{
		/* 1051 */return this.pk_source;
		/*      */}
	/*      */
	/*      */public String getPKFieldName()
	/*      */{
		/* 1064 */return "pk_material";
		/*      */}
	/*      */
	/*      */public String getProdarea()
	/*      */{
		/* 1073 */return this.prodarea;
		/*      */}
	/*      */
	/*      */public UFBoolean getProductfamily()
	/*      */{
		/* 1082 */return this.productfamily;
		/*      */}
	/*      */
	/*      */public Integer getProlifeperiod()
	/*      */{
		/* 1091 */return this.prolifeperiod;
		/*      */}
	/*      */
	/*      */public UFBoolean getRetail()
	/*      */{
		/* 1100 */return this.retail;
		/*      */}
	/*      */
	/*      */public UFBoolean getSetpartsflag()
	/*      */{
		/* 1109 */return this.setpartsflag;
		/*      */}
	/*      */
	/*      */public UFDouble getStoreunitnum()
	/*      */{
		/* 1118 */return this.storeunitnum;
		/*      */}
	/*      */
	/*      */public String getTableName()
	/*      */{
		/* 1131 */return "bd_material";
		/*      */}
	/*      */
	/*      */public UFDateTime getTs()
	/*      */{
		/* 1140 */return this.ts;
		/*      */}
	/*      */
	/*      */public String getUnitheight()
	/*      */{
		/* 1149 */return this.unitheight;
		/*      */}
	/*      */
	/*      */public String getUnitlength()
	/*      */{
		/* 1158 */return this.unitlength;
		/*      */}
	/*      */
	/*      */public UFDouble getUnitvolume()
	/*      */{
		/* 1167 */return this.unitvolume;
		/*      */}
	/*      */
	/*      */public UFDouble getUnitweight()
	/*      */{
		/* 1176 */return this.unitweight;
		/*      */}
	/*      */
	/*      */public String getUnitwidth()
	/*      */{
		/* 1185 */return this.unitwidth;
		/*      */}
	/*      */
	/*      */public Integer getVersion()
	/*      */{
		/* 1194 */return this.version;
		/*      */}
	/*      */
	/*      */public void setCode(String newCode)
	/*      */{
		/* 1204 */this.code = newCode;
		/*      */}
	/*      */
	/*      */public void setCreationtime(UFDateTime newCreationtime)
	/*      */{
		/* 1214 */this.creationtime = newCreationtime;
		/*      */}
	/*      */
	/*      */public void setCreator(String newCreator)
	/*      */{
		/* 1224 */this.creator = newCreator;
		/*      */}
	/*      */
	/*      */public void setDataoriginflag(Integer newDataoriginflag)
	/*      */{
		/* 1234 */this.dataoriginflag = newDataoriginflag;
		/*      */}
	/*      */
	/*      */public void setDef1(String newDef1)
	/*      */{
		/* 1244 */this.def1 = newDef1;
		/*      */}
	/*      */
	/*      */public void setDef10(String newDef10)
	/*      */{
		/* 1254 */this.def10 = newDef10;
		/*      */}
	/*      */
	/*      */public void setDef11(String newDef11)
	/*      */{
		/* 1264 */this.def11 = newDef11;
		/*      */}
	/*      */
	/*      */public void setDef12(String newDef12)
	/*      */{
		/* 1274 */this.def12 = newDef12;
		/*      */}
	/*      */
	/*      */public void setDef13(String newDef13)
	/*      */{
		/* 1284 */this.def13 = newDef13;
		/*      */}
	/*      */
	/*      */public void setDef14(String newDef14)
	/*      */{
		/* 1294 */this.def14 = newDef14;
		/*      */}
	/*      */
	/*      */public void setDef15(String newDef15)
	/*      */{
		/* 1304 */this.def15 = newDef15;
		/*      */}
	/*      */
	/*      */public void setDef16(String newDef16)
	/*      */{
		/* 1314 */this.def16 = newDef16;
		/*      */}
	/*      */
	/*      */public void setDef17(String newDef17)
	/*      */{
		/* 1324 */this.def17 = newDef17;
		/*      */}
	/*      */
	/*      */public void setDef18(String newDef18)
	/*      */{
		/* 1334 */this.def18 = newDef18;
		/*      */}
	/*      */
	/*      */public void setDef19(String newDef19)
	/*      */{
		/* 1344 */this.def19 = newDef19;
		/*      */}
	/*      */
	/*      */public void setDef2(String newDef2)
	/*      */{
		/* 1354 */this.def2 = newDef2;
		/*      */}
	/*      */
	/*      */public void setDef20(String newDef20)
	/*      */{
		/* 1364 */this.def20 = newDef20;
		/*      */}
	/*      */
	/*      */public void setDef3(String newDef3)
	/*      */{
		/* 1374 */this.def3 = newDef3;
		/*      */}
	/*      */
	/*      */public void setDef4(String newDef4)
	/*      */{
		/* 1384 */this.def4 = newDef4;
		/*      */}
	/*      */
	/*      */public void setDef5(String newDef5)
	/*      */{
		/* 1394 */this.def5 = newDef5;
		/*      */}
	/*      */
	/*      */public void setDef6(String newDef6)
	/*      */{
		/* 1404 */this.def6 = newDef6;
		/*      */}
	/*      */
	/*      */public void setDef7(String newDef7)
	/*      */{
		/* 1414 */this.def7 = newDef7;
		/*      */}
	/*      */
	/*      */public void setDef8(String newDef8)
	/*      */{
		/* 1424 */this.def8 = newDef8;
		/*      */}
	/*      */
	/*      */public void setDef9(String newDef9)
	/*      */{
		/* 1434 */this.def9 = newDef9;
		/*      */}
	/*      */
	/*      */public void setDiscountflag(UFBoolean newDiscountflag)
	/*      */{
		/* 1444 */this.discountflag = newDiscountflag;
		/*      */}
	/*      */
	/*      */public void setDr(Integer newDr)
	/*      */{
		/* 1454 */this.dr = newDr;
		/*      */}
	/*      */
	/*      */public void setElectronicsale(UFBoolean newElectronicsale)
	/*      */{
		/* 1464 */this.electronicsale = newElectronicsale;
		/*      */}
	/*      */
	/*      */public void setEnablestate(Integer newEnablestate)
	/*      */{
		/* 1474 */this.enablestate = newEnablestate;
		/*      */}
	/*      */
	/*      */public void setFee(UFBoolean newFee)
	/*      */{
		/* 1484 */this.fee = newFee;
		/*      */}
	/*      */
	/*      */public void setGraphid(String newGraphid)
	/*      */{
		/* 1494 */this.graphid = newGraphid;
		/*      */}
	/*      */
	/*      */public void setIntolerance(UFDouble newIntolerance)
	/*      */{
		/* 1504 */this.intolerance = newIntolerance;
		/*      */}
	/*      */
	/*      */public void setIselectrans(UFBoolean newIselectrans)
	/*      */{
		/* 1514 */this.iselectrans = newIselectrans;
		/*      */}
	/*      */
	/*      */public void setLatest(UFBoolean newLatest)
	/*      */{
		/* 1524 */this.latest = newLatest;
		/*      */}
	/*      */
	/*      */public void setMaterialbarcode(String newMaterialbarcode)
	/*      */{
		/* 1534 */this.materialbarcode = newMaterialbarcode;
		/*      */}
	/*      */
	/*      */public void setMaterialconvert(
			MaterialConvertVO[] newMaterialconvert)
	/*      */{
		/* 1545 */this.materialconvert = newMaterialconvert;
		/*      */}
	/*      */
	/*      */public void setMaterialcost(MaterialCostVO[] newMaterialcost)
	/*      */{
		/* 1556 */this.materialcost = newMaterialcost;
		/*      */}
	/*      */
	/*      */public void setMaterialfi(MaterialFiVO[] newMaterialfi)
	/*      */{
		/* 1566 */this.materialfi = newMaterialfi;
		/*      */}
	/*      */
	/*      */public void setMaterialmgt(Integer newMaterialmgt)
	/*      */{
		/* 1576 */this.materialmgt = newMaterialmgt;
		/*      */}
	/*      */
	/*      */public void setMaterialmnecode(String newMaterialmnecode)
	/*      */{
		/* 1586 */this.materialmnecode = newMaterialmnecode;
		/*      */}
	/*      */
	/*      */public void setMaterialplan(MaterialPlanVO[] newMaterialplan)
	/*      */{
		/* 1597 */this.materialplan = newMaterialplan;
		/*      */}
	/*      */
	/*      */public void setMaterialprod(MaterialProdVO[] newMaterialprod)
	/*      */{
		/* 1608 */this.materialprod = newMaterialprod;
		/*      */}
	/*      */
	/*      */public void setMaterialpu(MaterialPuVO[] newMaterialpu)
	/*      */{
		/* 1618 */this.materialpu = newMaterialpu;
		/*      */}
	/*      */
	/*      */public void setMaterialsale(MaterialSaleVO[] newMaterialsale)
	/*      */{
		/* 1629 */this.materialsale = newMaterialsale;
		/*      */}
	/*      */
	/*      */public void setMaterialshortname(String newMaterialshortname)
	/*      */{
		/* 1639 */this.materialshortname = newMaterialshortname;
		/*      */}
	/*      */
	/*      */public void setMaterialspec(String newMaterialspec)
	/*      */{
		/* 1649 */this.materialspec = newMaterialspec;
		/*      */}
	/*      */
	/*      */public void setMaterialstock(MaterialStockVO[] newMaterialstock)
	/*      */{
		/* 1660 */this.materialstock = newMaterialstock;
		/*      */}
	/*      */
	/*      */public void setMaterialtaxtype(
			MaterialTaxTypeVO[] newMaterialtaxtype)
	/*      */{
		/* 1671 */this.materialtaxtype = newMaterialtaxtype;
		/*      */}
	/*      */
	/*      */public void setMaterialtype(String newMaterialtype)
	/*      */{
		/* 1681 */this.materialtype = newMaterialtype;
		/*      */}
	/*      */
	/*      */public void setMemo(String newMemo)
	/*      */{
		/* 1691 */this.memo = newMemo;
		/*      */}
	/*      */
	/*      */public void setModifiedtime(UFDateTime newModifiedtime)
	/*      */{
		/* 1701 */this.modifiedtime = newModifiedtime;
		/*      */}
	/*      */
	/*      */public void setModifier(String newModifier)
	/*      */{
		/* 1711 */this.modifier = newModifier;
		/*      */}
	/*      */
	/*      */public void setName(String newName)
	/*      */{
		/* 1721 */this.name = newName;
		/*      */}
	/*      */
	/*      */public void setName2(String newName2)
	/*      */{
		/* 1731 */this.name2 = newName2;
		/*      */}
	/*      */
	/*      */public void setName3(String newName3)
	/*      */{
		/* 1741 */this.name3 = newName3;
		/*      */}
	/*      */
	/*      */public void setName4(String newName4)
	/*      */{
		/* 1751 */this.name4 = newName4;
		/*      */}
	/*      */
	/*      */public void setName5(String newName5)
	/*      */{
		/* 1761 */this.name5 = newName5;
		/*      */}
	/*      */
	/*      */public void setName6(String newName6)
	/*      */{
		/* 1771 */this.name6 = newName6;
		/*      */}
	/*      */
	/*      */public void setOutcloselowerlimit(UFDouble newOutcloselowerlimit)
	/*      */{
		/* 1782 */this.outcloselowerlimit = newOutcloselowerlimit;
		/*      */}
	/*      */
	/*      */public void setOuttolerance(UFDouble newOuttolerance)
	/*      */{
		/* 1792 */this.outtolerance = newOuttolerance;
		/*      */}
	/*      */
	/*      */public void setPk_brand(String newPk_brand)
	/*      */{
		/* 1802 */this.pk_brand = newPk_brand;
		/*      */}
	/*      */
	/*      */public void setPk_goodscode(String newPk_goodscode)
	/*      */{
		/* 1812 */this.pk_goodscode = newPk_goodscode;
		/*      */}
	/*      */
	/*      */public void setPk_group(String newPk_group)
	/*      */{
		/* 1822 */this.pk_group = newPk_group;
		/*      */}
	/*      */
	/*      */public void setPk_marasstframe(String newPk_marasstframe)
	/*      */{
		/* 1832 */this.pk_marasstframe = newPk_marasstframe;
		/*      */}
	/*      */
	/*      */public void setPk_marbasclass(String newPk_marbasclass)
	/*      */{
		/* 1842 */this.pk_marbasclass = newPk_marbasclass;
		/*      */}
	/*      */
	/*      */public void setPk_material(String newPk_material)
	/*      */{
		/* 1852 */this.pk_material = newPk_material;
		/*      */}
	/*      */
	/*      */public void setPk_material_pf(String newPk_material_pf)
	/*      */{
		/* 1862 */this.pk_material_pf = newPk_material_pf;
		/*      */}
	/*      */
	/*      */public void setPk_mattaxes(String newPk_mattaxes)
	/*      */{
		/* 1872 */this.pk_mattaxes = newPk_mattaxes;
		/*      */}
	/*      */
	/*      */public void setPk_measdoc(String newPk_measdoc)
	/*      */{
		/* 1882 */this.pk_measdoc = newPk_measdoc;
		/*      */}
	/*      */
	/*      */public void setPk_org(String newPk_org)
	/*      */{
		/* 1892 */this.pk_org = newPk_org;
		/*      */}
	/*      */
	/*      */public void setPk_prodline(String newPk_prodline)
	/*      */{
		/* 1902 */this.pk_prodline = newPk_prodline;
		/*      */}
	/*      */
	/*      */public void setPk_source(String newPk_source)
	/*      */{
		/* 1912 */this.pk_source = newPk_source;
		/*      */}
	/*      */
	/*      */public void setProdarea(String newProdarea)
	/*      */{
		/* 1922 */this.prodarea = newProdarea;
		/*      */}
	/*      */
	/*      */public void setProductfamily(UFBoolean newProductfamily)
	/*      */{
		/* 1932 */this.productfamily = newProductfamily;
		/*      */}
	/*      */
	/*      */public void setProlifeperiod(Integer newProlifeperiod)
	/*      */{
		/* 1942 */this.prolifeperiod = newProlifeperiod;
		/*      */}
	/*      */
	/*      */public void setRetail(UFBoolean newRetail)
	/*      */{
		/* 1952 */this.retail = newRetail;
		/*      */}
	/*      */
	/*      */public void setSetpartsflag(UFBoolean newSetpartsflag)
	/*      */{
		/* 1962 */this.setpartsflag = newSetpartsflag;
		/*      */}
	/*      */
	/*      */public void setStoreunitnum(UFDouble newStoreunitnum)
	/*      */{
		/* 1972 */this.storeunitnum = newStoreunitnum;
		/*      */}
	/*      */
	/*      */public void setTs(UFDateTime newTs)
	/*      */{
		/* 1982 */this.ts = newTs;
		/*      */}
	/*      */
	/*      */public void setUnitheight(String newUnitheight)
	/*      */{
		/* 1992 */this.unitheight = newUnitheight;
		/*      */}
	/*      */
	/*      */public void setUnitlength(String newUnitlength)
	/*      */{
		/* 2002 */this.unitlength = newUnitlength;
		/*      */}
	/*      */
	/*      */public void setUnitvolume(UFDouble newUnitvolume)
	/*      */{
		/* 2012 */this.unitvolume = newUnitvolume;
		/*      */}
	/*      */
	/*      */public void setUnitweight(UFDouble newUnitweight)
	/*      */{
		/* 2022 */this.unitweight = newUnitweight;
		/*      */}
	/*      */
	/*      */public void setUnitwidth(String newUnitwidth)
	/*      */{
		/* 2032 */this.unitwidth = newUnitwidth;
		/*      */}
	/*      */
	/*      */public void setVersion(Integer newVersion)
	/*      */{
		/* 2042 */this.version = newVersion;
		/*      */}
	/*      */
	/*      */public Integer getDeletestate()
	/*      */{
		/* 2051 */return this.deletestate;
		/*      */}
	/*      */
	/*      */public void setDeletestate(Integer newDeletestate)
	/*      */{
		/* 2061 */this.deletestate = newDeletestate;
		/*      */}
	/*      */
	/*      */public String getDelperson()
	/*      */{
		/* 2070 */return this.delperson;
		/*      */}
	/*      */
	/*      */public void setDelperson(String newDelperson)
	/*      */{
		/* 2080 */this.delperson = newDelperson;
		/*      */}
	/*      */
	/*      */public UFDateTime getDeltime()
	/*      */{
		/* 2089 */return this.deltime;
		/*      */}
	/*      */
	/*      */public void setDeltime(UFDateTime newDeltime)
	/*      */{
		/* 2099 */this.deltime = newDeltime;
		/*      */}
	/*      */
	/*      */public void setGoodsprtname(String goodsprtname) {
		/* 2103 */this.goodsprtname = goodsprtname;
		/*      */}
	/*      */
	/*      */public String getGoodsprtname() {
		/* 2107 */return this.goodsprtname;
		/*      */}
	/*      */
	/*      */public void setEmaterialspec(String ematerialspec) {
		/* 2111 */this.ematerialspec = ematerialspec;
		/*      */}
	/*      */
	/*      */public String getEmaterialspec() {
		/* 2115 */return this.ematerialspec;
		/*      */}
	/*      */
	/*      */public void setEname(String ename) {
		/* 2119 */this.ename = ename;
		/*      */}
	/*      */
	/*      */public String getEname() {
		/* 2123 */return this.ename;
		/*      */}
	/*      */
	/*      */public String getPicture() {
		/* 2127 */return this.picture;
		/*      */}
	/*      */
	/*      */public void setPicture(String picture) {
		/* 2131 */this.picture = picture;
		/*      */}
	/*      */
	/*      */public UFBoolean getIshproitems() {
		/* 2135 */return this.ishproitems;
		/*      */}
	/*      */
	/*      */public void setIshproitems(UFBoolean ishproitems) {
		/* 2139 */this.ishproitems = ishproitems;
		/*      */}
	/*      */
	/*      */public UFBoolean getIsfeature() {
		/* 2143 */return this.isfeature;
		/*      */}
	/*      */
	/*      */public void setIsfeature(UFBoolean isfeature) {
		/* 2147 */this.isfeature = isfeature;
		/*      */}
	/*      */
	/*      */public String getFeatureclass() {
		/* 2151 */return this.featureclass;
		/*      */}
	/*      */
	/*      */public void setFeatureclass(String featureclass) {
		/* 2155 */this.featureclass = featureclass;
		/*      */}
	/*      */
	/*      */public Map<String, List<SuperVO>> getExbeanname_tabvo_map()
	/*      */{
		/* 2162 */if (this.exbeanname_tabvo_map == null) {
			/* 2163 */this.exbeanname_tabvo_map = new HashMap();
			/*      */}
		/* 2165 */return this.exbeanname_tabvo_map;
		/*      */}
	/*      */
	/*      */public void setExbeanname_tabvo_map(
			Map<String, List<SuperVO>> exbeanname_tabvo_map)
	/*      */{
		/* 2170 */this.exbeanname_tabvo_map = exbeanname_tabvo_map;
		/*      */}
	/*      */
	/*      */public IVOMeta getMetaData()
	/*      */{
		/* 2175 */return VOMetaFactory.getInstance().getVOMeta("uap.material");
		/*      */}
	/*      */

	public String getDef25() {
		return def25;
	}
	public void setDef25(String def25) {
		this.def25 = def25;
	}
	public String getDef21() {
		return def21;
	}
	public void setDef21(String def21) {
		this.def21 = def21;
	}
	public String getDef22() {
		return def22;
	}
	public void setDef22(String def22) {
		this.def22 = def22;
	}
	public String getDef23() {
		return def23;
	}
	public void setDef23(String def23) {
		this.def23 = def23;
	}
	public String getDef24() {
		return def24;
	}
	public void setDef24(String def24) {
		this.def24 = def24;
	}
	public String getDef26() {
		return def26;
	}
	public void setDef26(String def26) {
		this.def26 = def26;
	}
	public String getDef27() {
		return def27;
	}
	public void setDef27(String def27) {
		this.def27 = def27;
	}
	public String getDef28() {
		return def28;
	}
	public void setDef28(String def28) {
		this.def28 = def28;
	}
	public String getDef29() {
		return def29;
	}
	public void setDef29(String def29) {
		this.def29 = def29;
	}
	public String getDef30() {
		return def30;
	}
	public void setDef30(String def30) {
		this.def30 = def30;
	}
}
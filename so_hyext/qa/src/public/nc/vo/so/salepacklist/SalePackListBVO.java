package nc.vo.so.salepacklist;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2022-5-13
 * @author 
 * @version NCPrj ??
 */
 
public class SalePackListBVO extends SuperVO {
	
/**
*销售包装清单子表主键
*/
public java.lang.String pk_salepacklist_b;
/**
*托盘号
*/
public java.lang.String palleno;
/**
*批次号
*/
public java.lang.String batchcode;
/**
*包装件数
*/
public java.lang.Integer npiece;
/**
*包装规格
*/
public java.lang.String spec;
/**
*托盘规格
*/
public java.lang.String spec_t;
/**
*计量单位
*/
public java.lang.String unit;
/**
*净重
*/
public nc.vo.pub.lang.UFDouble nweight;
/**
*皮重
*/
public nc.vo.pub.lang.UFDouble ntarenum;
/**
*毛重
*/
public nc.vo.pub.lang.UFDouble ngrosswt;
/**
*自定义项1
*/
public java.lang.String def1;
/**
*自定义项2
*/
public java.lang.String def2;
/**
*自定义项3
*/
public java.lang.String def3;
/**
*自定义项4
*/
public java.lang.String def4;
/**
*自定义项5
*/
public java.lang.String def5;
/**
*自定义项6
*/
public java.lang.String def6;
/**
*自定义项7
*/
public java.lang.String def7;
/**
*自定义项8
*/
public java.lang.String def8;
/**
*自定义项9
*/
public java.lang.String def9;
/**
*自定义项10
*/
public java.lang.String def10;
/**
*自定义项11
*/
public java.lang.String def11;
/**
*自定义项12
*/
public java.lang.String def12;
/**
*自定义项13
*/
public java.lang.String def13;
/**
*自定义项14
*/
public java.lang.String def14;
/**
*自定义项15
*/
public java.lang.String def15;
/**
*自定义项16
*/
public java.lang.String def16;
/**
*自定义项17
*/
public java.lang.String def17;
/**
*自定义项18
*/
public java.lang.String def18;
/**
*自定义项19
*/
public java.lang.String def19;
/**
*自定义项20
*/
public java.lang.String def20;
/**
*行号
*/
public java.lang.String rowno;
/**
*批次档案
*/
public java.lang.String pk_batchcode;
/**
*发货单行主键
*/
public java.lang.String pk_sourcebillrowid;
/**
*来源单据类型
*/
public java.lang.String vsrctype;
/**
*来源交易类型
*/
public java.lang.String vsrctrantype;
/**
*来源单据号
*/
public java.lang.String vsrccode;
/**
*来源单据行号
*/
public java.lang.String vsrcrowno;
/**
*来源单据主表
*/
public java.lang.String csrcid;
/**
*来源单据附表
*/
public java.lang.String csrcbid;
/**
*源头单据类型
*/
public java.lang.String vfirsttype;
/**
*源头交易类型
*/
public java.lang.String vfirsttrantype;
/**
*源头单据号
*/
public java.lang.String vfirstcode;
/**
*源头单据主表
*/
public java.lang.String cfirstid;
/**
*源头单据子表
*/
public java.lang.String cfirstbid;
/**
*源头单据行号
*/
public java.lang.String vfirstrowno;
/**
*来源单据表头时间戳
*/
public UFDateTime srcts;
/**
*来源单据表体时间戳
*/
public UFDateTime srcbts;
/**
*上层单据主键
*/
public String pk_salepacklist;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_salepacklist_b的Getter方法.属性名：销售包装清单子表主键
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getPk_salepacklist_b() {
return this.pk_salepacklist_b;
} 

/**
* 属性pk_salepacklist_b的Setter方法.属性名：销售包装清单子表主键
* 创建日期:2022-5-13
* @param newPk_salepacklist_b java.lang.String
*/
public void setPk_salepacklist_b ( java.lang.String pk_salepacklist_b) {
this.pk_salepacklist_b=pk_salepacklist_b;
} 
 
/**
* 属性 palleno的Getter方法.属性名：托盘号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getPalleno() {
return this.palleno;
} 

/**
* 属性palleno的Setter方法.属性名：托盘号
* 创建日期:2022-5-13
* @param newPalleno java.lang.String
*/
public void setPalleno ( java.lang.String palleno) {
this.palleno=palleno;
} 
 
/**
* 属性 batchcode的Getter方法.属性名：批次号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getBatchcode() {
return this.batchcode;
} 

/**
* 属性batchcode的Setter方法.属性名：批次号
* 创建日期:2022-5-13
* @param newBatchcode java.lang.String
*/
public void setBatchcode ( java.lang.String batchcode) {
this.batchcode=batchcode;
} 
 
/**
* 属性 npiece的Getter方法.属性名：包装件数
*  创建日期:2022-5-13
* @return java.lang.Integer
*/
public java.lang.Integer getNpiece() {
return this.npiece;
} 

/**
* 属性npiece的Setter方法.属性名：包装件数
* 创建日期:2022-5-13
* @param newNpiece java.lang.Integer
*/
public void setNpiece ( java.lang.Integer npiece) {
this.npiece=npiece;
} 
 
/**
* 属性 spec的Getter方法.属性名：包装规格
*  创建日期:2022-5-13
* @return nc.vo.bd.defdoc.DefdocVO
*/
public java.lang.String getSpec() {
return this.spec;
} 

/**
* 属性spec的Setter方法.属性名：包装规格
* 创建日期:2022-5-13
* @param newSpec nc.vo.bd.defdoc.DefdocVO
*/
public void setSpec ( java.lang.String spec) {
this.spec=spec;
} 
 
/**
* 属性 spec_t的Getter方法.属性名：托盘规格
*  创建日期:2022-5-13
* @return nc.vo.bd.defdoc.DefdocVO
*/
public java.lang.String getSpec_t() {
return this.spec_t;
} 

/**
* 属性spec_t的Setter方法.属性名：托盘规格
* 创建日期:2022-5-13
* @param newSpec_t nc.vo.bd.defdoc.DefdocVO
*/
public void setSpec_t ( java.lang.String spec_t) {
this.spec_t=spec_t;
} 
 
/**
* 属性 unit的Getter方法.属性名：计量单位
*  创建日期:2022-5-13
* @return nc.vo.bd.material.measdoc.MeasdocVO
*/
public java.lang.String getUnit() {
return this.unit;
} 

/**
* 属性unit的Setter方法.属性名：计量单位
* 创建日期:2022-5-13
* @param newUnit nc.vo.bd.material.measdoc.MeasdocVO
*/
public void setUnit ( java.lang.String unit) {
this.unit=unit;
} 
 
/**
* 属性 nweight的Getter方法.属性名：净重
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNweight() {
return this.nweight;
} 

/**
* 属性nweight的Setter方法.属性名：净重
* 创建日期:2022-5-13
* @param newNweight nc.vo.pub.lang.UFDouble
*/
public void setNweight ( nc.vo.pub.lang.UFDouble nweight) {
this.nweight=nweight;
} 
 
/**
* 属性 ntarenum的Getter方法.属性名：皮重
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNtarenum() {
return this.ntarenum;
} 

/**
* 属性ntarenum的Setter方法.属性名：皮重
* 创建日期:2022-5-13
* @param newNtarenum nc.vo.pub.lang.UFDouble
*/
public void setNtarenum ( nc.vo.pub.lang.UFDouble ntarenum) {
this.ntarenum=ntarenum;
} 
 
/**
* 属性 ngrosswt的Getter方法.属性名：毛重
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNgrosswt() {
return this.ngrosswt;
} 

/**
* 属性ngrosswt的Setter方法.属性名：毛重
* 创建日期:2022-5-13
* @param newNgrosswt nc.vo.pub.lang.UFDouble
*/
public void setNgrosswt ( nc.vo.pub.lang.UFDouble ngrosswt) {
this.ngrosswt=ngrosswt;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2022-5-13
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2022-5-13
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2022-5-13
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2022-5-13
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2022-5-13
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2022-5-13
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2022-5-13
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2022-5-13
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2022-5-13
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2022-5-13
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2022-5-13
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2022-5-13
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2022-5-13
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2022-5-13
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2022-5-13
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2022-5-13
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2022-5-13
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2022-5-13
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2022-5-13
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2022-5-13
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2022-5-13
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 pk_batchcode的Getter方法.属性名：批次档案
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getPk_batchcode() {
return this.pk_batchcode;
} 

/**
* 属性pk_batchcode的Setter方法.属性名：批次档案
* 创建日期:2022-5-13
* @param newPk_batchcode java.lang.String
*/
public void setPk_batchcode ( java.lang.String pk_batchcode) {
this.pk_batchcode=pk_batchcode;
} 
 
/**
* 属性 pk_sourcebillrowid的Getter方法.属性名：发货单行主键
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getPk_sourcebillrowid() {
return this.pk_sourcebillrowid;
} 

/**
* 属性pk_sourcebillrowid的Setter方法.属性名：发货单行主键
* 创建日期:2022-5-13
* @param newPk_sourcebillrowid java.lang.String
*/
public void setPk_sourcebillrowid ( java.lang.String pk_sourcebillrowid) {
this.pk_sourcebillrowid=pk_sourcebillrowid;
} 
 
/**
* 属性 vsrctype的Getter方法.属性名：来源单据类型
*  创建日期:2022-5-13
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getVsrctype() {
return this.vsrctype;
} 

/**
* 属性vsrctype的Setter方法.属性名：来源单据类型
* 创建日期:2022-5-13
* @param newVsrctype nc.vo.pub.billtype.BilltypeVO
*/
public void setVsrctype ( java.lang.String vsrctype) {
this.vsrctype=vsrctype;
} 
 
/**
* 属性 vsrctrantype的Getter方法.属性名：来源交易类型
*  创建日期:2022-5-13
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getVsrctrantype() {
return this.vsrctrantype;
} 

/**
* 属性vsrctrantype的Setter方法.属性名：来源交易类型
* 创建日期:2022-5-13
* @param newVsrctrantype nc.vo.pub.billtype.BilltypeVO
*/
public void setVsrctrantype ( java.lang.String vsrctrantype) {
this.vsrctrantype=vsrctrantype;
} 
 
/**
* 属性 vsrccode的Getter方法.属性名：来源单据号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getVsrccode() {
return this.vsrccode;
} 

/**
* 属性vsrccode的Setter方法.属性名：来源单据号
* 创建日期:2022-5-13
* @param newVsrccode java.lang.String
*/
public void setVsrccode ( java.lang.String vsrccode) {
this.vsrccode=vsrccode;
} 
 
/**
* 属性 vsrcrowno的Getter方法.属性名：来源单据行号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getVsrcrowno() {
return this.vsrcrowno;
} 

/**
* 属性vsrcrowno的Setter方法.属性名：来源单据行号
* 创建日期:2022-5-13
* @param newVsrcrowno java.lang.String
*/
public void setVsrcrowno ( java.lang.String vsrcrowno) {
this.vsrcrowno=vsrcrowno;
} 
 
/**
* 属性 csrcid的Getter方法.属性名：来源单据主表
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getCsrcid() {
return this.csrcid;
} 

/**
* 属性csrcid的Setter方法.属性名：来源单据主表
* 创建日期:2022-5-13
* @param newCsrcid java.lang.String
*/
public void setCsrcid ( java.lang.String csrcid) {
this.csrcid=csrcid;
} 
 
/**
* 属性 csrcbid的Getter方法.属性名：来源单据附表
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getCsrcbid() {
return this.csrcbid;
} 

/**
* 属性csrcbid的Setter方法.属性名：来源单据附表
* 创建日期:2022-5-13
* @param newCsrcbid java.lang.String
*/
public void setCsrcbid ( java.lang.String csrcbid) {
this.csrcbid=csrcbid;
} 
 
/**
* 属性 vfirsttype的Getter方法.属性名：源头单据类型
*  创建日期:2022-5-13
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getVfirsttype() {
return this.vfirsttype;
} 

/**
* 属性vfirsttype的Setter方法.属性名：源头单据类型
* 创建日期:2022-5-13
* @param newVfirsttype nc.vo.pub.billtype.BilltypeVO
*/
public void setVfirsttype ( java.lang.String vfirsttype) {
this.vfirsttype=vfirsttype;
} 
 
/**
* 属性 vfirsttrantype的Getter方法.属性名：源头交易类型
*  创建日期:2022-5-13
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getVfirsttrantype() {
return this.vfirsttrantype;
} 

/**
* 属性vfirsttrantype的Setter方法.属性名：源头交易类型
* 创建日期:2022-5-13
* @param newVfirsttrantype nc.vo.pub.billtype.BilltypeVO
*/
public void setVfirsttrantype ( java.lang.String vfirsttrantype) {
this.vfirsttrantype=vfirsttrantype;
} 
 
/**
* 属性 vfirstcode的Getter方法.属性名：源头单据号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getVfirstcode() {
return this.vfirstcode;
} 

/**
* 属性vfirstcode的Setter方法.属性名：源头单据号
* 创建日期:2022-5-13
* @param newVfirstcode java.lang.String
*/
public void setVfirstcode ( java.lang.String vfirstcode) {
this.vfirstcode=vfirstcode;
} 
 
/**
* 属性 cfirstid的Getter方法.属性名：源头单据主表
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getCfirstid() {
return this.cfirstid;
} 

/**
* 属性cfirstid的Setter方法.属性名：源头单据主表
* 创建日期:2022-5-13
* @param newCfirstid java.lang.String
*/
public void setCfirstid ( java.lang.String cfirstid) {
this.cfirstid=cfirstid;
} 
 
/**
* 属性 cfirstbid的Getter方法.属性名：源头单据子表
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getCfirstbid() {
return this.cfirstbid;
} 

/**
* 属性cfirstbid的Setter方法.属性名：源头单据子表
* 创建日期:2022-5-13
* @param newCfirstbid java.lang.String
*/
public void setCfirstbid ( java.lang.String cfirstbid) {
this.cfirstbid=cfirstbid;
} 
 
/**
* 属性 vfirstrowno的Getter方法.属性名：源头单据行号
*  创建日期:2022-5-13
* @return java.lang.String
*/
public java.lang.String getVfirstrowno() {
return this.vfirstrowno;
} 

/**
* 属性vfirstrowno的Setter方法.属性名：源头单据行号
* 创建日期:2022-5-13
* @param newVfirstrowno java.lang.String
*/
public void setVfirstrowno ( java.lang.String vfirstrowno) {
this.vfirstrowno=vfirstrowno;
} 
 
/**
* 属性 srcts的Getter方法.属性名：来源单据表头时间戳
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSrcts() {
return this.srcts;
} 

/**
* 属性srcts的Setter方法.属性名：来源单据表头时间戳
* 创建日期:2022-5-13
* @param newSrcts nc.vo.pub.lang.UFDateTime
*/
public void setSrcts ( UFDateTime srcts) {
this.srcts=srcts;
} 
 
/**
* 属性 srcbts的Getter方法.属性名：来源单据表体时间戳
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSrcbts() {
return this.srcbts;
} 

/**
* 属性srcbts的Setter方法.属性名：来源单据表体时间戳
* 创建日期:2022-5-13
* @param newSrcbts nc.vo.pub.lang.UFDateTime
*/
public void setSrcbts ( UFDateTime srcbts) {
this.srcbts=srcbts;
} 
 
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2022-5-13
* @return String
*/
public String getPk_salepacklist(){
return this.pk_salepacklist;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2022-5-13
* @param newPk_salepacklist String
*/
public void setPk_salepacklist(String pk_salepacklist){
this.pk_salepacklist=pk_salepacklist;
} 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2022-5-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2022-5-13
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("so.so_salepacklist_b");
    }
   }
    
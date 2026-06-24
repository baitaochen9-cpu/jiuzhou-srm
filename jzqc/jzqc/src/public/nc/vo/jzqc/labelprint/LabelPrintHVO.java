package nc.vo.jzqc.labelprint;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2022-8-27
 * @author yonyouBQ
 * @version NCPrj ??
 */
 
public class LabelPrintHVO extends SuperVO {
	
/**
*集团
*/
public java.lang.String pk_group;
/**
*组织
*/
public java.lang.String pk_org;
/**
*组织版本
*/
public java.lang.String pk_org_v;
/**
*创建人
*/
public java.lang.String creator;
/**
*创建时间
*/
public UFDateTime creationtime;
/**
*修改人
*/
public java.lang.String modifier;
/**
*修改时间
*/
public UFDateTime modifiedtime;
/**
*code
*/
public java.lang.String code;
/**
*name
*/
public java.lang.String name;
/**
*制单时间
*/
public UFDateTime maketime;
/**
*最后修改时间
*/
public UFDateTime lastmaketime;
/**
*单据ID
*/
public java.lang.String billid;
/**
*单据日期
*/
public UFDate billdate;
/**
*单据号
*/
public java.lang.String billno;
/**
*所属组织
*/
public java.lang.String pkorg;
/**
*业务类型
*/
public java.lang.String busitype;
/**
*制单人
*/
public java.lang.String billmaker;
/**
*审批人
*/
public java.lang.String approver;
/**
*审批状态
*/
public java.lang.Integer approvestatus;
/**
*审批批语
*/
public java.lang.String approvenote;
/**
*审批时间
*/
public UFDateTime approvedate;
/**
*交易类型名称
*/
public java.lang.String transtype;
/**
*单据类型
*/
public java.lang.String billtype;
/**
*交易类型
*/
public java.lang.String transtypepk;
/**
*来源单据类型
*/
public java.lang.String srcbilltype;
/**
*来源单据id
*/
public java.lang.String srcbillid;
/**
*来源单据明细id
*/
public java.lang.String srcbillrowid;
/**
*修订枚举
*/
public java.lang.Integer emendenum;
/**
*单据版本pk
*/
public java.lang.String billversionpk;
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
*主键
*/
public java.lang.String pk_labelprint;
/**
*物料版本信息
*/
public java.lang.String pk_material;
/**
*物料信息
*/
public java.lang.String pk_srcmaterial;
/**
*批次号
*/
public java.lang.String vbatchcode;
/**
*供应商批次号
*/
public java.lang.String bc_vvendbatchcode;
/**
*总包装数量
*/
public nc.vo.pub.lang.UFDouble amount;
/**
*标签序号
*/
public java.lang.Integer serial_number;
/**
*包装规格
*/
public java.lang.String couterpackspec;
/**
*批次总数量
*/
public nc.vo.pub.lang.UFDouble num;
/**
*重量
*/
public nc.vo.pub.lang.UFDouble num_b;
/**
*计量单位
*/
public java.lang.String cunitid;
/**
*包装单位
*/
public java.lang.String castunitid;
/**
*打印次数
*/
public java.lang.Integer iprintcount;
/**
*生产日期
*/
public UFDate dproducedate;
/**
*复测日期
*/
public UFDate enddate;
/**
*标签状态
*/
public UFBoolean blabelstatus;
/**
*可打印状态
*/
public UFBoolean bprintstatus;
/**
*备注
*/
public java.lang.String vnote;
/**
*标签总数
*/
public java.lang.Integer serial_total;
/**
*生成批次号
*/
public java.lang.String produceno;
/**
*批次号档案
*/
public java.lang.String pk_batchcode;
/**
*标签序号1
*/
public java.lang.String vserial_number;
/**
*标签总数1
*/
public java.lang.String vserial_total;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2022-8-27
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2022-8-27
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2022-8-27
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2022-8-27
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2022-8-27
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2022-8-27
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2022-8-27
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2022-8-27
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2022-8-27
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2022-8-27
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2022-8-27
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2022-8-27
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 code的Getter方法.属性名：code
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getCode() {
return this.code;
} 

/**
* 属性code的Setter方法.属性名：code
* 创建日期:2022-8-27
* @param newCode java.lang.String
*/
public void setCode ( java.lang.String code) {
this.code=code;
} 
 
/**
* 属性 name的Getter方法.属性名：name
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getName() {
return this.name;
} 

/**
* 属性name的Setter方法.属性名：name
* 创建日期:2022-8-27
* @param newName java.lang.String
*/
public void setName ( java.lang.String name) {
this.name=name;
} 
 
/**
* 属性 maketime的Getter方法.属性名：制单时间
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* 属性maketime的Setter方法.属性名：制单时间
* 创建日期:2022-8-27
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* 属性 lastmaketime的Getter方法.属性名：最后修改时间
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* 属性lastmaketime的Setter方法.属性名：最后修改时间
* 创建日期:2022-8-27
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2022-8-27
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2022-8-27
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2022-8-27
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2022-8-27
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2022-8-27
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2022-8-27
* @return nc.vo.sm.UserVO
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2022-8-27
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2022-8-27
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2022-8-27
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2022-8-27
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2022-8-27
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2022-8-27
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2022-8-27
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型名称
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型名称
* 创建日期:2022-8-27
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2022-8-27
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型
*  创建日期:2022-8-27
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型
* 创建日期:2022-8-27
* @param newTranstypepk nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2022-8-27
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2022-8-27
* @param newSrcbilltype nc.vo.pub.billtype.BilltypeVO
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2022-8-27
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 srcbillrowid的Getter方法.属性名：来源单据明细id
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getSrcbillrowid() {
return this.srcbillrowid;
} 

/**
* 属性srcbillrowid的Setter方法.属性名：来源单据明细id
* 创建日期:2022-8-27
* @param newSrcbillrowid java.lang.String
*/
public void setSrcbillrowid ( java.lang.String srcbillrowid) {
this.srcbillrowid=srcbillrowid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2022-8-27
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2022-8-27
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2022-8-27
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2022-8-27
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2022-8-27
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2022-8-27
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2022-8-27
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2022-8-27
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2022-8-27
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2022-8-27
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2022-8-27
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2022-8-27
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2022-8-27
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2022-8-27
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2022-8-27
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2022-8-27
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2022-8-27
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2022-8-27
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2022-8-27
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2022-8-27
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2022-8-27
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2022-8-27
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2022-8-27
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 pk_labelprint的Getter方法.属性名：主键
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getPk_labelprint() {
return this.pk_labelprint;
} 

/**
* 属性pk_labelprint的Setter方法.属性名：主键
* 创建日期:2022-8-27
* @param newPk_labelprint java.lang.String
*/
public void setPk_labelprint ( java.lang.String pk_labelprint) {
this.pk_labelprint=pk_labelprint;
} 
 
/**
* 属性 pk_material的Getter方法.属性名：物料版本信息
*  创建日期:2022-8-27
* @return nc.vo.bd.material.MaterialVO
*/
public java.lang.String getPk_material() {
return this.pk_material;
} 

/**
* 属性pk_material的Setter方法.属性名：物料版本信息
* 创建日期:2022-8-27
* @param newPk_material nc.vo.bd.material.MaterialVO
*/
public void setPk_material ( java.lang.String pk_material) {
this.pk_material=pk_material;
} 
 
/**
* 属性 pk_srcmaterial的Getter方法.属性名：物料信息
*  创建日期:2022-8-27
* @return nc.vo.bd.material.MaterialVersionVO
*/
public java.lang.String getPk_srcmaterial() {
return this.pk_srcmaterial;
} 

/**
* 属性pk_srcmaterial的Setter方法.属性名：物料信息
* 创建日期:2022-8-27
* @param newPk_srcmaterial nc.vo.bd.material.MaterialVersionVO
*/
public void setPk_srcmaterial ( java.lang.String pk_srcmaterial) {
this.pk_srcmaterial=pk_srcmaterial;
} 
 
/**
* 属性 vbatchcode的Getter方法.属性名：批次号
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getVbatchcode() {
return this.vbatchcode;
} 

/**
* 属性vbatchcode的Setter方法.属性名：批次号
* 创建日期:2022-8-27
* @param newVbatchcode java.lang.String
*/
public void setVbatchcode ( java.lang.String vbatchcode) {
this.vbatchcode=vbatchcode;
} 
 
/**
* 属性 bc_vvendbatchcode的Getter方法.属性名：供应商批次号
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getBc_vvendbatchcode() {
return this.bc_vvendbatchcode;
} 

/**
* 属性bc_vvendbatchcode的Setter方法.属性名：供应商批次号
* 创建日期:2022-8-27
* @param newBc_vvendbatchcode java.lang.String
*/
public void setBc_vvendbatchcode ( java.lang.String bc_vvendbatchcode) {
this.bc_vvendbatchcode=bc_vvendbatchcode;
} 
 
/**
* 属性 amount的Getter方法.属性名：总包装数量
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getAmount() {
return this.amount;
} 

/**
* 属性amount的Setter方法.属性名：总包装数量
* 创建日期:2022-8-27
* @param newAmount nc.vo.pub.lang.UFDouble
*/
public void setAmount ( nc.vo.pub.lang.UFDouble amount) {
this.amount=amount;
} 
 
/**
* 属性 serial_number的Getter方法.属性名：标签序号
*  创建日期:2022-8-27
* @return java.lang.Integer
*/
public java.lang.Integer getSerial_number() {
return this.serial_number;
} 

/**
* 属性serial_number的Setter方法.属性名：标签序号
* 创建日期:2022-8-27
* @param newSerial_number java.lang.Integer
*/
public void setSerial_number ( java.lang.Integer serial_number) {
this.serial_number=serial_number;
} 
 
/**
* 属性 couterpackspec的Getter方法.属性名：包装规格
*  创建日期:2022-8-27
* @return nc.vo.bd.defdoc.DefdocVO
*/
public java.lang.String getCouterpackspec() {
return this.couterpackspec;
} 

/**
* 属性couterpackspec的Setter方法.属性名：包装规格
* 创建日期:2022-8-27
* @param newCouterpackspec nc.vo.bd.defdoc.DefdocVO
*/
public void setCouterpackspec ( java.lang.String couterpackspec) {
this.couterpackspec=couterpackspec;
} 
 
/**
* 属性 num的Getter方法.属性名：批次总数量
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNum() {
return this.num;
} 

/**
* 属性num的Setter方法.属性名：批次总数量
* 创建日期:2022-8-27
* @param newNum nc.vo.pub.lang.UFDouble
*/
public void setNum ( nc.vo.pub.lang.UFDouble num) {
this.num=num;
} 
 
/**
* 属性 num_b的Getter方法.属性名：重量
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNum_b() {
return this.num_b;
} 

/**
* 属性num_b的Setter方法.属性名：重量
* 创建日期:2022-8-27
* @param newNum_b nc.vo.pub.lang.UFDouble
*/
public void setNum_b ( nc.vo.pub.lang.UFDouble num_b) {
this.num_b=num_b;
} 
 
/**
* 属性 cunitid的Getter方法.属性名：计量单位
*  创建日期:2022-8-27
* @return nc.vo.bd.material.measdoc.MeasdocVO
*/
public java.lang.String getCunitid() {
return this.cunitid;
} 

/**
* 属性cunitid的Setter方法.属性名：计量单位
* 创建日期:2022-8-27
* @param newCunitid nc.vo.bd.material.measdoc.MeasdocVO
*/
public void setCunitid ( java.lang.String cunitid) {
this.cunitid=cunitid;
} 
 
/**
* 属性 castunitid的Getter方法.属性名：包装单位
*  创建日期:2022-8-27
* @return nc.vo.bd.material.measdoc.MeasdocVO
*/
public java.lang.String getCastunitid() {
return this.castunitid;
} 

/**
* 属性castunitid的Setter方法.属性名：包装单位
* 创建日期:2022-8-27
* @param newCastunitid nc.vo.bd.material.measdoc.MeasdocVO
*/
public void setCastunitid ( java.lang.String castunitid) {
this.castunitid=castunitid;
} 
 
/**
* 属性 iprintcount的Getter方法.属性名：打印次数
*  创建日期:2022-8-27
* @return java.lang.Integer
*/
public java.lang.Integer getIprintcount() {
return this.iprintcount;
} 

/**
* 属性iprintcount的Setter方法.属性名：打印次数
* 创建日期:2022-8-27
* @param newIprintcount java.lang.Integer
*/
public void setIprintcount ( java.lang.Integer iprintcount) {
this.iprintcount=iprintcount;
} 
 
/**
* 属性 dproducedate的Getter方法.属性名：生产日期
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDproducedate() {
return this.dproducedate;
} 

/**
* 属性dproducedate的Setter方法.属性名：生产日期
* 创建日期:2022-8-27
* @param newDproducedate nc.vo.pub.lang.UFDate
*/
public void setDproducedate ( UFDate dproducedate) {
this.dproducedate=dproducedate;
} 
 
/**
* 属性 enddate的Getter方法.属性名：复测日期
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getEnddate() {
return this.enddate;
} 

/**
* 属性enddate的Setter方法.属性名：复测日期
* 创建日期:2022-8-27
* @param newEnddate nc.vo.pub.lang.UFDate
*/
public void setEnddate ( UFDate enddate) {
this.enddate=enddate;
} 
 
/**
* 属性 blabelstatus的Getter方法.属性名：标签状态
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFUFBoolean
*/
public UFBoolean getBlabelstatus() {
return this.blabelstatus;
} 

/**
* 属性blabelstatus的Setter方法.属性名：标签状态
* 创建日期:2022-8-27
* @param newBlabelstatus nc.vo.pub.lang.UFUFBoolean
*/
public void setBlabelstatus ( UFBoolean blabelstatus) {
this.blabelstatus=blabelstatus;
} 
 
/**
* 属性 bprintstatus的Getter方法.属性名：可打印状态
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFUFBoolean
*/
public UFBoolean getBprintstatus() {
return this.bprintstatus;
} 

/**
* 属性bprintstatus的Setter方法.属性名：可打印状态
* 创建日期:2022-8-27
* @param newBprintstatus nc.vo.pub.lang.UFUFBoolean
*/
public void setBprintstatus ( UFBoolean bprintstatus) {
this.bprintstatus=bprintstatus;
} 
 
/**
* 属性 vnote的Getter方法.属性名：备注
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getVnote() {
return this.vnote;
} 

/**
* 属性vnote的Setter方法.属性名：备注
* 创建日期:2022-8-27
* @param newVnote java.lang.String
*/
public void setVnote ( java.lang.String vnote) {
this.vnote=vnote;
} 
 
/**
* 属性 serial_total的Getter方法.属性名：标签总数
*  创建日期:2022-8-27
* @return java.lang.Integer
*/
public java.lang.Integer getSerial_total() {
return this.serial_total;
} 

/**
* 属性serial_total的Setter方法.属性名：标签总数
* 创建日期:2022-8-27
* @param newSerial_total java.lang.Integer
*/
public void setSerial_total ( java.lang.Integer serial_total) {
this.serial_total=serial_total;
} 
 
/**
* 属性 produceno的Getter方法.属性名：生成批次号
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getProduceno() {
return this.produceno;
} 

/**
* 属性produceno的Setter方法.属性名：生成批次号
* 创建日期:2022-8-27
* @param newProduceno java.lang.String
*/
public void setProduceno ( java.lang.String produceno) {
this.produceno=produceno;
} 
 
/**
* 属性 pk_batchcode的Getter方法.属性名：批次号档案
*  创建日期:2022-8-27
* @return nc.vo.scmf.ic.mbatchcode.BatchcodeVO
*/
public java.lang.String getPk_batchcode() {
return this.pk_batchcode;
} 

/**
* 属性pk_batchcode的Setter方法.属性名：批次号档案
* 创建日期:2022-8-27
* @param newPk_batchcode nc.vo.scmf.ic.mbatchcode.BatchcodeVO
*/
public void setPk_batchcode ( java.lang.String pk_batchcode) {
this.pk_batchcode=pk_batchcode;
} 
 
/**
* 属性 vserial_number的Getter方法.属性名：标签序号1
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getVserial_number() {
return this.vserial_number;
} 

/**
* 属性vserial_number的Setter方法.属性名：标签序号1
* 创建日期:2022-8-27
* @param newVserial_number java.lang.String
*/
public void setVserial_number ( java.lang.String vserial_number) {
this.vserial_number=vserial_number;
} 
 
/**
* 属性 vserial_total的Getter方法.属性名：标签总数1
*  创建日期:2022-8-27
* @return java.lang.String
*/
public java.lang.String getVserial_total() {
return this.vserial_total;
} 

/**
* 属性vserial_total的Setter方法.属性名：标签总数1
* 创建日期:2022-8-27
* @param newVserial_total java.lang.String
*/
public void setVserial_total ( java.lang.String vserial_total) {
this.vserial_total=vserial_total;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2022-8-27
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2022-8-27
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("jzqc.labelprint");
    }
   }
    
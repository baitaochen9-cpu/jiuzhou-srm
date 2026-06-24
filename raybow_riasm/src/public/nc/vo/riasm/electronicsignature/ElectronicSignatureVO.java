package nc.vo.riasm.electronicsignature;

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
 *  创建日期:2021-3-9
 * @author yonyouBQ
 * @version NCPrj ??
 */
 
public class ElectronicSignatureVO extends SuperVO {
	
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
*id
*/
public java.lang.String id;
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
*行号
*/
public java.lang.String rowno;
/**
*单据ID
*/
public java.lang.String billid;
/**
*单据号
*/
public java.lang.String billno;
/**
*单据日期
*/
public UFDate billdate;
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
*交易类型
*/
public java.lang.String transtype;
/**
*单据类型
*/
public java.lang.String billtype;
/**
*交易类型pk
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
public Boolean def11;
/**
*自定义项12
*/
public Boolean def12;
/**
*自定义项13
*/
public Boolean def13;
/**
*自定义项14
*/
public Boolean def14;
/**
*自定义项15
*/
public Boolean def15;
/**
*自定义项16
*/
public Boolean def16;
/**
*自定义项17
*/
public Boolean def17;
/**
*自定义项18
*/
public Boolean def18;
/**
*自定义项19
*/
public Boolean def19;
/**
*自定义项20
*/
public Boolean def20;
/**
*上级对象
*/
public java.lang.String pk_parent;
/**
*提交
*/
public Boolean commits;
/**
*审核
*/
public Boolean approves;
/**
*修改
*/
public Boolean updates;
/**
*停用
*/
public Boolean uneables;
/**
*删除
*/
public Boolean deletes;
/**
*确认
*/
public Boolean confirm;
/**
*保存
*/
public Boolean save;
/**
*领料
*/
public Boolean get;
/**
*备料
*/
public Boolean ready;
/**
*冻结
*/
public Boolean frozen;
/**
*解冻
*/
public Boolean traw;
/**
*生成条码
*/
public Boolean generate;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2021-3-9
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2021-3-9
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2021-3-9
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2021-3-9
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2021-3-9
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2021-3-9
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2021-3-9
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2021-3-9
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2021-3-9
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2021-3-9
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2021-3-9
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2021-3-9
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 id的Getter方法.属性名：id
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getId() {
return this.id;
} 

/**
* 属性id的Setter方法.属性名：id
* 创建日期:2021-3-9
* @param newId java.lang.String
*/
public void setId ( java.lang.String id) {
this.id=id;
} 
 
/**
* 属性 code的Getter方法.属性名：code
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getCode() {
return this.code;
} 

/**
* 属性code的Setter方法.属性名：code
* 创建日期:2021-3-9
* @param newCode java.lang.String
*/
public void setCode ( java.lang.String code) {
this.code=code;
} 
 
/**
* 属性 name的Getter方法.属性名：name
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getName() {
return this.name;
} 

/**
* 属性name的Setter方法.属性名：name
* 创建日期:2021-3-9
* @param newName java.lang.String
*/
public void setName ( java.lang.String name) {
this.name=name;
} 
 
/**
* 属性 maketime的Getter方法.属性名：制单时间
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* 属性maketime的Setter方法.属性名：制单时间
* 创建日期:2021-3-9
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* 属性 lastmaketime的Getter方法.属性名：最后修改时间
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* 属性lastmaketime的Setter方法.属性名：最后修改时间
* 创建日期:2021-3-9
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2021-3-9
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2021-3-9
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2021-3-9
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2021-3-9
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2021-3-9
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2021-3-9
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2021-3-9
* @param newBillmaker java.lang.String
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2021-3-9
* @param newApprover java.lang.String
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2021-3-9
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2021-3-9
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2021-3-9
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2021-3-9
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2021-3-9
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2021-3-9
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2021-3-9
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2021-3-9
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2021-3-9
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2021-3-9
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2021-3-9
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2021-3-9
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2021-3-9
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2021-3-9
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2021-3-9
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2021-3-9
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2021-3-9
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2021-3-9
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2021-3-9
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2021-3-9
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2021-3-9
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2021-3-9
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2021-3-9
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2021-3-9
* @param newDef11 nc.vo.pub.lang.UFBoolean
*/
public void setDef11 ( Boolean def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2021-3-9
* @param newDef12 nc.vo.pub.lang.UFBoolean
*/
public void setDef12 ( Boolean def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2021-3-9
* @param newDef13 nc.vo.pub.lang.UFBoolean
*/
public void setDef13 ( Boolean def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2021-3-9
* @param newDef14 nc.vo.pub.lang.UFBoolean
*/
public void setDef14 ( Boolean def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2021-3-9
* @param newDef15 nc.vo.pub.lang.UFBoolean
*/
public void setDef15 ( Boolean def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2021-3-9
* @param newDef16 nc.vo.pub.lang.UFBoolean
*/
public void setDef16 ( Boolean def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2021-3-9
* @param newDef17 nc.vo.pub.lang.UFBoolean
*/
public void setDef17 ( Boolean def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2021-3-9
* @param newDef18 nc.vo.pub.lang.UFBoolean
*/
public void setDef18 ( Boolean def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2021-3-9
* @param newDef19 nc.vo.pub.lang.UFBoolean
*/
public void setDef19 ( Boolean def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2021-3-9
* @param newDef20 nc.vo.pub.lang.UFBoolean
*/
public void setDef20 ( Boolean def20) {
this.def20=def20;
} 
 
/**
* 属性 pk_parent的Getter方法.属性名：上级对象
*  创建日期:2021-3-9
* @return nc.vo.sm.funcreg.FuncRegisterVO
*/
public java.lang.String getPk_parent() {
return this.pk_parent;
} 

/**
* 属性pk_parent的Setter方法.属性名：上级对象
* 创建日期:2021-3-9
* @param newPk_parent nc.vo.sm.funcreg.FuncRegisterVO
*/
public void setPk_parent ( java.lang.String pk_parent) {
this.pk_parent=pk_parent;
} 
 
/**
* 属性 commits的Getter方法.属性名：提交
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getCommits() {
return this.commits;
} 

/**
* 属性commits的Setter方法.属性名：提交
* 创建日期:2021-3-9
* @param newCommits nc.vo.pub.lang.UFBoolean
*/
public void setCommits ( Boolean commits) {
this.commits=commits;
} 
 
/**
* 属性 approves的Getter方法.属性名：审核
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getApproves() {
return this.approves;
} 

/**
* 属性approves的Setter方法.属性名：审核
* 创建日期:2021-3-9
* @param newApproves nc.vo.pub.lang.UFBoolean
*/
public void setApproves ( Boolean approves) {
this.approves=approves;
} 
 
/**
* 属性 updates的Getter方法.属性名：修改
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getUpdates() {
return this.updates;
} 

/**
* 属性updates的Setter方法.属性名：修改
* 创建日期:2021-3-9
* @param newUpdates nc.vo.pub.lang.UFBoolean
*/
public void setUpdates ( Boolean updates) {
this.updates=updates;
} 
 
/**
* 属性 uneables的Getter方法.属性名：停用
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getUneables() {
return this.uneables;
} 

/**
* 属性uneables的Setter方法.属性名：停用
* 创建日期:2021-3-9
* @param newUneables nc.vo.pub.lang.UFBoolean
*/
public void setUneables ( Boolean uneables) {
this.uneables=uneables;
} 
 
/**
* 属性 deletes的Getter方法.属性名：删除
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getDeletes() {
return this.deletes;
} 

/**
* 属性deletes的Setter方法.属性名：删除
* 创建日期:2021-3-9
* @param newDeletes nc.vo.pub.lang.UFBoolean
*/
public void setDeletes ( Boolean deletes) {
this.deletes=deletes;
} 
 
/**
* 属性 confirm的Getter方法.属性名：确认
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getConfirm() {
return this.confirm;
} 

/**
* 属性confirm的Setter方法.属性名：确认
* 创建日期:2021-3-9
* @param newConfirm nc.vo.pub.lang.UFBoolean
*/
public void setConfirm ( Boolean confirm) {
this.confirm=confirm;
} 
 
/**
* 属性 save的Getter方法.属性名：保存
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getSave() {
return this.save;
} 

/**
* 属性save的Setter方法.属性名：保存
* 创建日期:2021-3-9
* @param newSave nc.vo.pub.lang.UFBoolean
*/
public void setSave ( Boolean save) {
this.save=save;
} 
 
/**
* 属性 get的Getter方法.属性名：领料
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getGet() {
return this.get;
} 

/**
* 属性get的Setter方法.属性名：领料
* 创建日期:2021-3-9
* @param newGet nc.vo.pub.lang.UFBoolean
*/
public void setGet ( Boolean get) {
this.get=get;
} 
 
/**
* 属性 ready的Getter方法.属性名：备料
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getReady() {
return this.ready;
} 

/**
* 属性ready的Setter方法.属性名：备料
* 创建日期:2021-3-9
* @param newReady nc.vo.pub.lang.UFBoolean
*/
public void setReady ( Boolean ready) {
this.ready=ready;
} 
 
/**
* 属性 frozen的Getter方法.属性名：冻结
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getFrozen() {
return this.frozen;
} 

/**
* 属性frozen的Setter方法.属性名：冻结
* 创建日期:2021-3-9
* @param newFrozen nc.vo.pub.lang.UFBoolean
*/
public void setFrozen ( Boolean frozen) {
this.frozen=frozen;
} 
 
/**
* 属性 traw的Getter方法.属性名：解冻
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getTraw() {
return this.traw;
} 

/**
* 属性traw的Setter方法.属性名：解冻
* 创建日期:2021-3-9
* @param newTraw nc.vo.pub.lang.UFBoolean
*/
public void setTraw ( Boolean traw) {
this.traw=traw;
} 
 
/**
* 属性 generate的Getter方法.属性名：生成条码
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFBoolean
*/
public Boolean getGenerate() {
return this.generate;
} 

/**
* 属性generate的Setter方法.属性名：生成条码
* 创建日期:2021-3-9
* @param newGenerate nc.vo.pub.lang.UFBoolean
*/
public void setGenerate ( Boolean generate) {
this.generate=generate;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2021-3-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2021-3-9
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("riasm.electronicsignature");
    }
   }
    
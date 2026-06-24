package nc.bs.jzyy.sys.oa.ic;

import nc.api.rest.ic.utils.JsonParamBDTranslateEnum;

/**
 * 库存单据表头字段映射
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-24 上午11:19:15   
 * @version NCC1909
 */
public enum ICBillFieldsEnum {

  //单据查询模板上的字段对应的值  指向基本档案枚举中的字段
  approver("approver",JsonParamBDTranslateEnum.pk_user.getBdVO()),                 //审批人
  billmaker("billmaker",JsonParamBDTranslateEnum.pk_user.getBdVO()),               //制单人
  cbizid("cbizid",JsonParamBDTranslateEnum.pk_psndoc.getBdVO()),                   //业务员
  ccostdomainid("ccostdomainid",JsonParamBDTranslateEnum.ccostdomainid.getBdVO()), //结算成本域
  ccustomerid("ccustomerid",JsonParamBDTranslateEnum.pk_customer.getBdVO()),       //客户
  cdptid("cdptid",JsonParamBDTranslateEnum.pk_dept.getBdVO()),                     // 部门
  cfanaceorgoid("cfanaceorgoid",JsonParamBDTranslateEnum.pk_financeOrg.getBdVO()), //财务组织
  cmaterialoid("cmaterialoid",JsonParamBDTranslateEnum.pk_material.getBdVO()),     //物料
  corpoid("corpoid",JsonParamBDTranslateEnum.pk_corp.getBdVO()),                   //公司
  cothercalbodyoid("cothercalbodyoid",JsonParamBDTranslateEnum.pk_stockOrg.getBdVO()),//库存组织
  creator("creator",JsonParamBDTranslateEnum.pk_user.getBdVO()),                   //创建人
  crececountryid("crececountryid",JsonParamBDTranslateEnum.pk_country.getBdVO()),  //收货国
  csendcountryid("csendcountryid",JsonParamBDTranslateEnum.pk_country.getBdVO()),  //发货国
  ctaxcountryid("ctaxcountryid",JsonParamBDTranslateEnum.pk_country.getBdVO()),    //报税国
  cvendorid("cvendorid",JsonParamBDTranslateEnum.pk_supplier.getBdVO()),           //供应商
  cvendoridclass("cvendorid.pk_supplierclass",JsonParamBDTranslateEnum.pk_supplierclass.getBdVO()),
  cwarehouseid("cwarehouseid",JsonParamBDTranslateEnum.pk_warehouseid.getBdVO()),  //仓库
  cwhsmanagerid("cwhsmanagerid",JsonParamBDTranslateEnum.pk_psndoc.getBdVO()),     //库管员
  pk_org("pk_org",JsonParamBDTranslateEnum.pk_stockOrg.getBdVO()),                 //库存组织
  pk_group("pk_group",JsonParamBDTranslateEnum.pk_group.getBdVO()),                 //集团 
  
  //表体公共字段
  casscustid("cgeneralbid.casscustid",JsonParamBDTranslateEnum.pk_customer.getBdVO()),
  castunitid("cgeneralbid.castunitid",JsonParamBDTranslateEnum.pk_measdoc.getBdVO()),
  cbodywarehouseid("cgeneralbid.cbodywarehouseid",JsonParamBDTranslateEnum.pk_warehouseid.getBdVO()),
  cliabilityoid("cgeneralbid.cliabilityoid",JsonParamBDTranslateEnum.pk_apliabcenter.getBdVO()),
  clocationid("cgeneralbid.clocationid",JsonParamBDTranslateEnum.pk_clocationid.getBdVO()),
  bodycmaterialoid("cgeneralbid.cmaterialoid",JsonParamBDTranslateEnum.pk_material.getBdVO()),
  marbasclass("cgeneralbid.cmaterialvid.pk_marbasclass",JsonParamBDTranslateEnum.pk_marbasclass.getBdVO()),
  bodycorpoid("cgeneralbid.corpoid",JsonParamBDTranslateEnum.pk_corp.getBdVO()),
  cprojectid("cgeneralbid.cprojectid",JsonParamBDTranslateEnum.cprojectid.getBdVO()),
  csnunitid("cgeneralbid.csnunitid",JsonParamBDTranslateEnum.pk_measdoc.getBdVO()),
  cunitid("cgeneralbid.cunitid",JsonParamBDTranslateEnum.pk_measdoc.getBdVO()),
  bodycvendorid("cgeneralbid.cvendorid",JsonParamBDTranslateEnum.pk_supplier.getBdVO()),
  pk_batchcode("cgeneralbid.pk_batchcode",JsonParamBDTranslateEnum.pk_batchcode.getBdVO()),
  bodypk_org("cgeneralbid.pk_org",JsonParamBDTranslateEnum.pk_stockOrg.getBdVO());
  
  //成员变量
  private String filedName;
  private Object superVO;
  //构造方法
  private ICBillFieldsEnum(String filedName, Object superVO)
  {
      this.filedName = filedName;
      this.superVO = superVO;
  }
  
  public String getFiledName() {
    return filedName;
  }
  public void setFiledName(String filedName) {
    this.filedName = filedName;
  }

  public Object getSuperVO() {
    return superVO;
  }

  public void setSuperVO(Object superVO) {
    this.superVO = superVO;
  }
 
}

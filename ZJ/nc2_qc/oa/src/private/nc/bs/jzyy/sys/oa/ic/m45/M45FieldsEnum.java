package nc.bs.jzyy.sys.oa.ic.m45;

import nc.api.rest.ic.utils.JsonParamBDTranslateEnum;

/**
 * 采购入库单字段映射
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-24 上午11:18:52   
 * @version NCC1909
 */
public enum M45FieldsEnum {
  //单据查询模板上的字段对应的值  指向基本档案枚举中的字段
  cemployeeid("cemployeeid",JsonParamBDTranslateEnum.pk_psndoc.getBdVO()), 
  cotherwhid("cotherwhid",JsonParamBDTranslateEnum.pk_warehouseid.getBdVO()),      
  cpurorgoid("cpurorgoid",JsonParamBDTranslateEnum.pk_purchaseOrg.getBdVO()),
  csendtypeid("csendtypeid",JsonParamBDTranslateEnum.pk_transPortType.getBdVO()),
  ctradewordid("ctradewordid",JsonParamBDTranslateEnum.pk_incoteerm.getBdVO());
  
  //成员变量
  private String filedName;
  private Object superVO;
  //构造方法
  private M45FieldsEnum(String filedName, Object superVO)
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

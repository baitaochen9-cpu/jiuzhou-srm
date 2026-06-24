package nc.pubitf.ic.onhand.mmpps.mpm;

import java.io.Serializable;

import nc.pubitf.mmpub.sdmanage.ISupplyResult;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.pub.define.ICPubMetaNameConst;

/**
 * 查询存量的字段映射，和对应场景的过滤条件
 * 
 * @since 6.0
 * @version 2012-11-5 上午10:34:00
 * @author guofj
 * 
 * @modify zhaofeid 2013-08-16
 */

public class OnhandCalcSupplyMapVO implements ISupplyResult,Serializable{

  private static final long serialVersionUID = -254628918431201951L;
  
  private String from;
  
  private String where;
  
  //yezhian 2023/09/11 增加首次入库日期和后段提前期 
  private String dinbounddate ; //首次入库日期
  

  


  @Override
  public String getFrom() {
    return this.from;
  }

  @Override
  public String getWhere() {
    return this.where;
  }
  
  public void setFrom(String from) {
    this.from = from;
  }
  
  public void setWhere (String where){
    this.where = where;
  }

  @Override
  public String getSupplyTypeCodeValue() {
    return null;
  }

  @Override
  public String getSupplyTypeIdValue() {
    return null;
  }

  @Override
  public String getSupplyid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.PK_ONHANDDIM;
  }

  @Override
  public String getSupplybid() {
    return null;
  }

  @Override
  public String getSupplyRowNo() {
    return null;
  }

  @Override
  public String getSupplyOrgid() {
    return OnhandDimVO.TABLENAME + "." + ICPubMetaNameConst.PK_ORG;
  }

  @Override
  public String getSupplyOrgvid() {
    return OnhandDimVO.TABLENAME + "." + ICPubMetaNameConst.PK_ORG_V;
  }

  @Override
  public String getMaterialid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CMATERIALOID;
  }

  @Override
  public String getMaterialvid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CMATERIALVID;
  }

  @Override
  public String getSupplyCode() {
    return null;
  }

 
  @Override
  public String getSupplyDate() {
	  dinbounddate = "  scm_batchcode.dinbounddate " ;
    return dinbounddate;
  }

  @Override
  public String getNnum() {
    return OnhandNumVO.TABLENAME + "." + OnhandNumVO.NONHANDNUM;
  }

  @Override
  public String getReservatioNnum() {
    return OnhandNumVO.TABLENAME + "." + OnhandNumVO.NRSNUM;
  }

  @Override
  public String getVendorid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CVENDORID;
  }

  @Override
  public String getProductorid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CPRODUCTORID;
  }

  @Override
  public String getPprojectid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CPROJECTID;
  }

  @Override
  public String getCustomerid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CASSCUSTID;
  }

  @Override
  public String getFree1() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE1;
  }

  @Override
  public String getFree2() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE2;
  }

  @Override
  public String getFree3() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE3;
  }

  @Override
  public String getFree4() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE4;
  }

  @Override
  public String getFree5() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE5;
  }

  @Override
  public String getFree6() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE6;
  }

  @Override
  public String getFree7() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE7;
  }

  @Override
  public String getFree8() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE8;
  }

  @Override
  public String getFree9() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE9;
  }

  @Override
  public String getFree10() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VFREE10;
  }

  @Override
  public String getFirstBid() {
    return null;
  }

  @Override
  public String getFirstCode() {
    return null;
  }

  @Override
  public String getFirstId() {
    return null;
  }

  @Override
  public String getFirstRowNo() {
    return null;
  }

  @Override
  public String getFirstType() {
    return null;
  }

  @Override
  public String getVBillStatus() {
    return null;
  }

  @Override
  public String getVBillStatusEnumID() {
    return null;
  }

  @Override
  public String getSrcBid() {
    return null;
  }

  @Override
  public String getSrcCode() {
    return null;
  }

  @Override
  public String getSrcId() {
    return null;
  }

  @Override
  public String getSrcRowNo() {
    return null;
  }

  @Override
  public String getSrcType() {
    return null;
  }

  @Override
  public String getCcustmaterialid() {
    return null;
  }

  @Override
  public String getBatchCode() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VBATCHCODE;
  }

  @Override
  public String getPk_BatchCode() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.PK_BATCHCODE;
  }

  @Override
  public String getBoutendflag() {
    return null;
  }

  @Override
  public String getDbilldate() {
    return null;
  }

  @Override
  public String getCunitid() {
    return null;
  }

  @Override
  public String getNexenum() {
    return null;
  }

  @Override
  public String getVchangerate() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.VCHANGERATE;
  }

  @Override
  public String getCastunitid() {
    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CASTUNITID;
  }

  @Override
  public String getNastnum() {
    return OnhandNumVO.TABLENAME + "." + OnhandNumVO.NONHANDASTNUM;
  }

  @Override
  public String getSupplyNnum() {
    return OnhandNumVO.TABLENAME + "." + OnhandNumVO.NONHANDNUM;
  }

  
  /**
   * 库存状态
   */
  @Override
  public String getTranType() {
//    return OnhandDimVO.TABLENAME + "." + OnhandDimVO.CSTATEID;
	  return " ic_storestate.iusability "; //是否可用
  }

  
  
  @Override
  public String getCffileid() {
    //TODO 特征码
    return null;
  }

}

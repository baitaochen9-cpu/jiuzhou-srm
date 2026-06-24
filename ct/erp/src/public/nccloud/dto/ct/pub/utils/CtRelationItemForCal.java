package nccloud.dto.ct.pub.utils;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.pubapp.calculator.data.RelationItemForCal;

/** 
 * @description 
 * @author xiahui
 * @date 创建时间：2019-1-21 下午5:59:17 
 * @version ncc1.0 
 **/
public class CtRelationItemForCal extends RelationItemForCal {

  /**
   * 合同表体没有 “主单位本币无税净价” 用 “主单位无税单价” 代替
   */
  @Override
  public String getNnetpriceKey() {
    return CtAbstractBVO.NGPRICE;
  }

  // 主单位原币无税净价
  @Override
  public String getNorignetpriceKey() {
    return CtAbstractBVO.NORIGPRICE;
  }

  // 主单位原币含税净价
  @Override
  public String getNorigtaxnetpriceKey() {
    return CtAbstractBVO.NORIGTAXPRICE;
  }

  // “主单位无税单价”
  @Override
  public String getNpriceKey() {
    return CtAbstractBVO.NGPRICE;
  }

  /** 获得报价本币无税净价的itemkey值 */
  @Override
  public String getNqtnetpriceKey() {
    return CtAbstractBVO.NQTPRICE;
  }

  // 因为合同没有“报价单位无税净价” 所以用 “报价单位无税单价”代替
  // 即折扣等于 1
  @Override
  public String getNqtorignetpriceKey() {
    return CtAbstractBVO.NQTORIGPRICE;
  }

  // 因为合同没有“报价单位含税净价” 所以用 “报价单位含税单价”代替
  // 即折扣等于 1
  @Override
  public String getNqtorigtaxnetprcKey() {
    return CtAbstractBVO.NQTORIGTAXPRICE;
  }

  /** 获得报价本币含税净价的itemkey值 */
  @Override
  public String getNqttaxnetpriceKey() {
    return CtAbstractBVO.NQTTAXPRICE;
  }

  /**
   * 主本币含税单价
   */
  @Override
  public String getNtaxnetpriceKey() {
    return CtAbstractBVO.NGTAXPRICE;
  }

  /**
   * 合同表体没有 “主单位本币含税净价” 用 “主单位含税单价” 代替
   */
  @Override
  public String getNtaxpriceKey() {
    return CtAbstractBVO.NGTAXPRICE;
  }

}

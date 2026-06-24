package nc.vo.ia.mi2.vochange;

import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.VOMapInitChgVOAdjust;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.pu.m4201.entity.PurchaseinFIItemVO;
import nc.vo.pu.m4201.entity.PurchaseinFIVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 库存财务与存货采购入库单VO交换处理类
 * 
 * @since 6.0
 * @version 2011-1-27 下午03:15:31
 * @author 皮之兵
 */
public class Chg4201TOI2 extends VOMapInitChgVOAdjust {

  @Override
  protected String getSrcVOKey(CircularlyAccessibleValueObject bvo) {
    String key = "";
    PurchaseinFIItemVO item = (PurchaseinFIItemVO) bvo;
    key = item.getPk_stockps_b();

    return key;
  }

  @Override
  protected void initSrcVOsMap(AggregatedValueObject[] srcVOs) {
    for (Object srcvo : srcVOs) {
      PurchaseinFIVO vo = (PurchaseinFIVO) srcvo;
      for (CircularlyAccessibleValueObject bvo : vo.getChildrenVO()) {
        String srcVOKey = this.getSrcVOKey(bvo);
        this.srcBVOMap.put(srcVOKey, bvo);
        this.srcHVOMap.put(srcVOKey, vo.getParentVO());
      }
    }
  }

  @Override
  protected void setDestVOInfo() {
    for (Entry<String, AbstractRealItemVO> entry : this.iaBVOMap.entrySet()) {
      String csrcbid = entry.getKey();
      AbstractRealItemVO iaitem = entry.getValue();
      PurchaseinFIItemVO poitem =
          (PurchaseinFIItemVO) this.srcBVOMap.get(csrcbid);
      // 计算线索记库存采购入库单行ID
      iaitem.setCcalcthreadid(poitem.getPk_stockps_b());
      
      //****yezhian 重新定义业务日期   2021-03-16***************************************/
      CustomCarriedForwardOrder sevse = NCLocator.getInstance().lookup(nc.vo.ia.util.CustomCarriedForwardOrder.class);
      // 检查物料是否是循环料
      String pk_org = iaHVOMap.get(csrcbid).getPk_org();
      if(null == pk_org || pk_org.isEmpty()){
    	  pk_org = sevse.getPk_CostRegion((String)this.srcHVOMap.get(csrcbid).getAttributeValue("pk_org"),
    			  (String)this.srcHVOMap.get(csrcbid).getAttributeValue("cwarehouseid"));
      }
      if(null ==  pk_org || pk_org.isEmpty()){
    	  //本应报错处理，但这里已经处理不掉了，直接返回不再继续！
    	  continue;
      }
      if(sevse.isSelectMaterial(iaitem.getCinventoryid() , pk_org) ==UFBoolean.TRUE){
    	  String billtype = "I2";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      //****************/
    }
  }
}

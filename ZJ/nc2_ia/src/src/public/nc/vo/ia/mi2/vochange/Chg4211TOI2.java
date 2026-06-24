package nc.vo.ia.mi2.vochange;

import java.util.Map.Entry;

import sun.security.jca.GetInstance;

import nc.bs.framework.common.NCLocator;
import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.OptimizeChgVOAdjust;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pu.m27.entity.SettleBillItemVO;
import nc.vo.pu.m27.entity.SettleBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * 采购结算单（库存采购入）与存货采购入库单VO交换处理类
 * 
 * @since 6.0
 * @version 2011-9-2 下午02:00:51
 * @author 皮之兵
 */
public class Chg4211TOI2 extends OptimizeChgVOAdjust {

  @Override
  public Class<? extends AbstractBill> getICBillClass() {
    return PurchaseInVO.class;
  }

  @Override
  protected String getICBillCode() {
    return SettleBillItemVO.PK_PURCHASEIN;
  }

  @Override
  protected String getSrcVOKey(CircularlyAccessibleValueObject bvo) {
    String key = "";
    SettleBillItemVO item = (SettleBillItemVO) bvo;
    key = item.getPk_settlebill_b();

    return key;
  }

  @Override
  protected void initSrcVOsMap(AggregatedValueObject[] srcVOs) {
    for (Object srcvo : srcVOs) {
      SettleBillVO vo = (SettleBillVO) srcvo;
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
      SettleBillItemVO poitem = (SettleBillItemVO) this.srcBVOMap.get(csrcbid);
      // 计算线索记库存采购入库单行ID
      iaitem.setCcalcthreadid(poitem.getPk_purchasein_b());
      
      //****yezhian 重新定义业务日期   2021-03-16***************************************/
      CustomCarriedForwardOrder sevse = NCLocator.getInstance().lookup(nc.vo.ia.util.CustomCarriedForwardOrder.class);
      // 检查物料是否是循环料
      String pk_org = iaHVOMap.get(csrcbid).getPk_org();
      if(sevse.isSelectMaterial(iaitem.getCinventoryid() , pk_org) ==UFBoolean.TRUE){
    	  String billtype = "I2";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      
      ///*************************************/
    }
  }

}

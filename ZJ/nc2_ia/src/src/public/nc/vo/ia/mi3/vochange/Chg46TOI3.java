package nc.vo.ia.mi3.vochange;

import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.VOMapInitChgVOAdjust;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.ic.m46.entity.FinProdInBodyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

/**
 * 库存其它入库单与存货其它入库单VO交换处理类
 * 
 * @since 6.0
 * @version 2010-12-22 上午09:55:49
 * @author 皮之兵
 */
public class Chg46TOI3 extends VOMapInitChgVOAdjust {

  @Override
  protected void setDestVOInfo() {
    for (Entry<String, AbstractRealItemVO> entry : this.iaBVOMap.entrySet()) {
      String csrcbid = entry.getKey();
      AbstractRealItemVO iaitem = entry.getValue();
      FinProdInBodyVO srcitem = (FinProdInBodyVO) this.srcBVOMap.get(csrcbid);
      // 计算线索记库存产成品入库单行ID
      iaitem.setCcalcthreadid(srcitem.getCgeneralbid());
      
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
    	  String billtype = "I3";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      //**********************************************************************/
    }
  }

}

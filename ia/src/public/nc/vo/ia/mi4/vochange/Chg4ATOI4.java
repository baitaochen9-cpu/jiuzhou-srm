package nc.vo.ia.mi4.vochange;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.bs.framework.common.NCLocator;
import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.KCBYChgVOAdjust;
import nc.vo.ia.enumeration.FCalcBizFlag;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.ic.m4a.entity.GeneralInBodyVO;
import nc.vo.ic.m4a.entity.GeneralInHeadVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.res.billtype.ICBillType;

/**
 * 库存其它入库单与存货其它入库单VO交换处理类
 * 
 * @since 6.0
 * @version 2010-12-22 上午09:55:49
 * @author 皮之兵
 */
public class Chg4ATOI4 extends KCBYChgVOAdjust {

  @Override
  protected void setDestVOInfo() {
    Map<String, FCalcBizFlag> bizflagmap = this.initFCalcBizFlagsMap();

    for (Entry<String, AbstractRealItemVO> entry : this.iaBVOMap.entrySet()) {
      String csrcbid = entry.getKey();
      AbstractRealItemVO iaitem = entry.getValue();
      GeneralInBodyVO icitem = (GeneralInBodyVO) this.srcBVOMap.get(csrcbid);
      String csrctype = icitem.getCsourcetype();
      FCalcBizFlag flag = bizflagmap.get(csrctype);
      if (flag == null) {
        if (ICBillType.TransferWarehouse.getCode().equals(csrctype)) {
          GeneralInHeadVO ichead =
              (GeneralInHeadVO) this.srcHVOMap.get(csrcbid);
          flag = FCalcBizFlag.ZK_TCBY;
          if (super.isKCBY(ichead)) {
            flag = FCalcBizFlag.ZK_KCBY;
          }
        }
      }
      
      
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
    	  String billtype = "I4";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      //**********************************************************************/
      
      if (flag != null && ICBillType.Transform.getCode().equals(csrctype)) {
        iaitem.setFcalcbizflag((Integer) flag.value());
        // 形态转换形成的库存出库单源头行ID记录的是对应形态转换单的行ID，
        // 而形成的库存入库单源头行ID记录的是形成其入库物料的出库单物料行对应的源头行ID，因此我们
        // 把源头行ID作为计算关系
        iaitem.setCcalcid(icitem.getCfirstbillbid());
      }
      else if (flag != null
          && ICBillType.TransferWarehouse.getCode().equals(csrctype)) {
        iaitem.setFcalcbizflag((Integer) flag.value());
        // 把库存出库单来源单据行ID作为计算关系
        iaitem.setCcalcid(icitem.getCsourcebillbid());
      }
      else if (flag != null) {
        iaitem.setFcalcbizflag((Integer) flag.value());
        // 把库存入库单来源单据ID作为计算关系
        iaitem.setCcalcid(icitem.getCsourcebillhid());
      }
      else {
        // 计算线索记库存其他入库单行ID
        iaitem.setCcalcthreadid(icitem.getCgeneralbid());
      }
    }
  }

  private Map<String, FCalcBizFlag> initFCalcBizFlagsMap() {
    Map<String, FCalcBizFlag> map = new HashMap<String, FCalcBizFlag>();
    map.put(ICBillType.Teardown.getCode(), FCalcBizFlag.CX);
    map.put(ICBillType.Transform.getCode(), FCalcBizFlag.XTZH_YWD);
    map.put(ICBillType.Assembly.getCode(), FCalcBizFlag.ZZ);

    return map;
  }

}

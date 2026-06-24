package nc.vo.ia.mi7.vochange;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.KCBYChgVOAdjust;
import nc.vo.ia.enumeration.FCalcBizFlag;
import nc.vo.ia.mi7.entity.I7HeadVO;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.ic.m4i.entity.GeneralOutBodyVO;
import nc.vo.ic.m4i.entity.GeneralOutHeadVO;
import nc.vo.ic.m4l.entity.AssemblyBillHeadVO;
import nc.vo.ic.m4m.entity.TeardownBillHeadVO;
import nc.vo.ic.special.define.SpecialMetaNameConst;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.scmpub.res.billtype.ICBillType;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.data.vo.VOQuery;

/**
 * 库存其它出库单与存货其它出库单VO交换处理类
 * 
 * @since 6.0
 * @version 2010-12-22 上午09:55:49
 * @author 皮之兵
 */
public class Chg4ITOI7 extends KCBYChgVOAdjust {

  @Override
  protected void setDestVOInfo() {
    Map<String, FCalcBizFlag> bizflagmap = this.initFCalcBizFlagsMap();

    Map<String, I7HeadVO> zzMap = new HashMap<String, I7HeadVO>();
    Map<String, I7HeadVO> cxMap = new HashMap<String, I7HeadVO>();
    for (Entry<String, AbstractRealItemVO> entry : this.iaBVOMap.entrySet()) {
      String csrcbid = entry.getKey();
      AbstractRealItemVO iaitem = entry.getValue();
      GeneralOutBodyVO icitem = (GeneralOutBodyVO) this.srcBVOMap.get(csrcbid);
      String csrctype = icitem.getCsourcetype();
      // 填充计算关系
      this.fillCalcFlag(bizflagmap, iaitem, icitem);
      
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
    	  String billtype = "I7";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      //****************/
      

      // 组装需要记录费用
      if (ICBillType.Assembly.getCode().equals(csrctype)) {
        I7HeadVO head = (I7HeadVO) this.iaHVOMap.get(csrcbid);
        zzMap.put(icitem.getCsourcebillhid(), head);
      }
      // 拆卸需要记录费用
      if (ICBillType.Teardown.getCode().equals(csrctype)) {
        I7HeadVO head = (I7HeadVO) this.iaHVOMap.get(csrcbid);
        cxMap.put(icitem.getCsourcebillhid(), head);
      }
    }
    this.fillZZCost(zzMap);
    this.fillCXCost(cxMap);
  }

  private void fillCalcFlag(Map<String, FCalcBizFlag> bizflagmap,
      AbstractRealItemVO iaitem, GeneralOutBodyVO icitem) {
    String csrctype = icitem.getCsourcetype();
    FCalcBizFlag flag = bizflagmap.get(csrctype);
    if (flag == null && ICBillType.TransferWarehouse.getCode().equals(csrctype)) {
      String csrcbid = iaitem.getCsrcbid();
      GeneralOutHeadVO ichead = (GeneralOutHeadVO) this.srcHVOMap.get(csrcbid);
      flag = FCalcBizFlag.ZK_TCBY;
      if (super.isKCBY(ichead)) {
        flag = FCalcBizFlag.ZK_KCBY;
      }
    }

    if (flag != null && ICBillType.Transform.getCode().equals(csrctype)) {
      iaitem.setFcalcthreadbizflag((Integer) flag.value());
      // 形态转换、组装、拆卸形成的库存出库单源头行ID记录的是对应形态转换单、组装单、拆卸单的行ID，
      // 而形成的库存入库单源头行ID记录的是形成其入库物料的出库物料行对应的源头行ID，因此我们
      // 把源头行ID作为计算线索
      iaitem.setCcalcthreadid(icitem.getCfirstbillbid());
    }
    else if (flag != null
        && ICBillType.TransferWarehouse.getCode().equals(csrctype)) {
      iaitem.setFcalcthreadbizflag((Integer) flag.value());
      // 把库存出库单来源单据行ID作为计算线索
      iaitem.setCcalcthreadid(icitem.getCsourcebillbid());
    }
    else if (flag != null) {
      iaitem.setFcalcthreadbizflag((Integer) flag.value());
      // 把库存出库单来源单据ID作为计算线索
      iaitem.setCcalcthreadid(icitem.getCsourcebillhid());
    }
    else {
      // 记录对应库存单据行ID， 便于出库跟踪入库处理
      iaitem.setFcalcthreadbizflag((Integer) FCalcBizFlag.COMMON.value());
      iaitem.setCcalcthreadid(icitem.getCgeneralbid());
    }
  }

  private void fillCXCost(Map<String, I7HeadVO> cxMap) {
    if (cxMap.size() > 0) {
      String[] fields = new String[] {
        SpecialMetaNameConst.CSPECIALHID, TeardownBillHeadVO.TEARDOWNRATE
      };
      VOQuery<TeardownBillHeadVO> bo =
          new VOQuery<TeardownBillHeadVO>(TeardownBillHeadVO.class, fields);
      TeardownBillHeadVO[] icheads =
          bo.query(ToArrayUtil.convert(cxMap.keySet()));
      for (TeardownBillHeadVO ichead : icheads) {
        String key = ichead.getCspecialhid();
        I7HeadVO iahead = cxMap.get(key);
        iahead.setNcost(ichead.getTeardownrate());
      }
    }
  }

  private void fillZZCost(Map<String, I7HeadVO> zzMap) {
    String[] fields = new String[] {
      SpecialMetaNameConst.CSPECIALHID, AssemblyBillHeadVO.ASSEMBLYRATE
    };
    if (zzMap.size() > 0) {
      VOQuery<AssemblyBillHeadVO> bo =
          new VOQuery<AssemblyBillHeadVO>(AssemblyBillHeadVO.class, fields);
      AssemblyBillHeadVO[] icheads =
          bo.query(ToArrayUtil.convert(zzMap.keySet()));
      for (AssemblyBillHeadVO ichead : icheads) {
        String key = ichead.getCspecialhid();
        I7HeadVO iahead = zzMap.get(key);
        iahead.setNcost(ichead.getAssemblyrate());
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

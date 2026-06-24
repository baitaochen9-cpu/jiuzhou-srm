package nc.vo.ia.mi6.vochange;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.vo.bd.material.MaterialVO;
import nc.vo.ia.bill.entity.real.AbstractRealItemVO;
import nc.vo.ia.bill.vochange.VOMapInitChgVOAdjust;
import nc.vo.ia.enumeration.FCalcBizFlag;
import nc.vo.ia.mi6.entity.I6ItemVO;
import nc.vo.ia.pub.util.ToArrayUtil;
import nc.vo.ia.util.CustomCarriedForwardOrder;
import nc.vo.ic.m4d.entity.MaterialOutBodyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.scmpub.res.billtype.ICBillType;
import nc.vo.scmpub.res.billtype.SCBillType;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;

/**
 * 库存材料出库单与存货材料出库单VO交换处理类
 * 
 * @since 6.0
 * @version 2011-1-17 上午09:43:11
 * @author 李瑜
 */
public class Chg4DTOI6 extends VOMapInitChgVOAdjust {

  @Override
  protected void setDestVOInfo() {
    MapList<String, I6ItemVO> map = new MapList<String, I6ItemVO>();
    for (Entry<String, AbstractRealItemVO> entry : this.iaBVOMap.entrySet()) {
      String csrcbid = entry.getKey();
      MaterialOutBodyVO icitem =
          (MaterialOutBodyVO) this.srcBVOMap.get(csrcbid);
      String srcbilltype = icitem.getCsourcetype();
      AbstractRealItemVO iaitem = entry.getValue();
      if (ICBillType.SubContinIn.getCode().equals(srcbilltype)) {
        Integer flag = ValueUtils.getInteger(FCalcBizFlag.WWHX.value());
        iaitem.setFcalcthreadbizflag(flag);
        iaitem.setCcalcthreadid(icitem.getCsourcebillbid());
      }
      // 委外调整单传材料出库成本直接传存货核算材料出成本
      else if (SCBillType.Adjuster.getCode().equals(srcbilltype)) {
        iaitem.setNprice(icitem.getNcostprice());
        iaitem.setNmny(icitem.getNcostmny());
      }
      else {
        // 记录对应库存单据行ID， 便于出库跟踪入库处理
        iaitem.setFcalcthreadbizflag((Integer) FCalcBizFlag.COMMON.value());
        iaitem.setCcalcthreadid(icitem.getCgeneralbid());
      }

      // 将库存材料出自制的单据中成本对象产品vid转换成oid，便于后续成本管理取数结转等操作
      // 这个处理是一个暂时的方案（考虑库存改动较大，因此防止存货转换），到V63需要重新整理需求评估方案。
      if (PubAppTool.isNull(srcbilltype)) {
        I6ItemVO item = (I6ItemVO) iaitem;
        String costobjid = item.getCcostobjid();
        if (!PubAppTool.isNull(costobjid)) {
          map.put(costobjid, item);
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
    	  String billtype = "I6";
          UFDate bizdate = iaitem.getDbizdate();
         
          UFDate bizData = sevse.getBizData(pk_org, bizdate, billtype);
          iaitem.setDbizdate(bizData);
      }
      //**********************************************************************/
      
    }

    this.convertvidTOoid(map);
  }

  private void convertvidTOoid(MapList<String, I6ItemVO> map) {
    if (map.size() > 0) {
      String[] costobjvids = ToArrayUtil.convert(map.keySet());

      String[] fields = new String[] {
        MaterialVO.PK_SOURCE
      };
      Map<String, MaterialVO> materialmap =
          MaterialPubService.queryMaterialBaseInfo(costobjvids, fields);
      for (Entry<String, List<I6ItemVO>> entry : map.entrySet()) {
        String vid = entry.getKey();
        String oid = materialmap.get(vid).getPk_source();
        for (I6ItemVO item : entry.getValue()) {
          item.setCcostobjid(oid);
        }
      }
    }
  }
}

package nc.bs.mmpps.calc.bp.fetch.supply;

import nc.bs.mmpps.calc.bp.utils.OrgVersionCacheManager;
import nc.vo.mmpac.pmo.parameter.PMOSupplyMapVO;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.WorkDateCache;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;

public class SupplyMapVOAdapter
{
  public static SupplyMapVO getPMOSupplyOutPutMapVO(PMOSupplyMapVO pmoMapResult)
  {
    SupplyMapVO mapVO = getPMOSupplyMapVO(pmoMapResult);
    
    String reverseFilter = "- isnull(mm_mo_planoutput.nreservednum, 0)";
    String calcWhere = pmoMapResult.getWhere().replace(reverseFilter, "");
    String reverseFilterOutPut = "- isnull(mm_mo.nholdnum, 0)";
    calcWhere = calcWhere.replace(reverseFilterOutPut, "");
    mapVO.setWhere(calcWhere);
    return mapVO;
  }
  
  public static SupplyMapVO getPMOSupplyMapVO(PMOSupplyMapVO pmoMapResult)
  {
    PMOSupplyMapVO sourceVO = pmoMapResult;
    SupplyMapVO mapVO = new SupplyMapVO();
    mapVO.setFrom(sourceVO.getFrom());
    mapVO.setWhere(pmoMapResult.getWhere());
    
    mapVO.setCustomerid(sourceVO.getCustomerid());
    mapVO.setProductorid(sourceVO.getProductorid());
    mapVO.setProjectid(sourceVO.getProjectid());
    mapVO.setVendorid(sourceVO.getVendorid());
    
    mapVO.setFree1(sourceVO.getFree1());
    mapVO.setFree10(sourceVO.getFree10());
    mapVO.setFree2(sourceVO.getFree2());
    mapVO.setFree3(sourceVO.getFree3());
    mapVO.setFree4(sourceVO.getFree4());
    mapVO.setFree5(sourceVO.getFree5());
    mapVO.setFree6(sourceVO.getFree6());
    mapVO.setFree7(sourceVO.getFree7());
    mapVO.setFree8(sourceVO.getFree8());
    mapVO.setFree9(sourceVO.getFree9());
    
    mapVO.setMaterialid(sourceVO.getMaterialid());
    mapVO.setMaterialvid(sourceVO.getMaterialvid());
    
    mapVO.setSupplyOrgid(sourceVO.getSupplyOrgid());
    mapVO.setSupplyOrgvid(sourceVO.getSupplyOrgvid());
    
    mapVO.setSupplyTypeIdValue(sourceVO.getSupplyTypeIdValue());
    mapVO.setSupplyTypeCodeValue(sourceVO.getSupplyTypeCodeValue());
    mapVO.setSupplyid(sourceVO.getSupplyid());
    mapVO.setSupplybid(sourceVO.getSupplybid());
    mapVO.setSupplyRowNo(sourceVO.getSupplyRowNo());
    mapVO.setSupplyCode(sourceVO.getSupplyCode());
    
    mapVO.setReservatioNnum(sourceVO.getReservatioNnum());
    mapVO.setSupplyDate(sourceVO.getTsupplytime());
    mapVO.setNsupplynum(sourceVO.getNplanoutputrmnum());
    
    mapVO.setVfirsttype(sourceVO.getFirstType());
    mapVO.setVfirstcode(sourceVO.getFirstCode());
    mapVO.setVfirstrowno(sourceVO.getFirstRowNo());
    mapVO.setVfirstid(sourceVO.getFirstId());
    mapVO.setVfirstbid(sourceVO.getFirstBid());
    
    mapVO.setVsrctype(sourceVO.getSrcType());
    mapVO.setVsrccode(sourceVO.getSrcCode());
    mapVO.setVsrcrowno(sourceVO.getSrcRowNo());
    mapVO.setVsrcid(sourceVO.getSrcId());
    mapVO.setVsrcbid(sourceVO.getSrcBid());
    
    mapVO.setCbomid(sourceVO.getVbomversion());
    mapVO.setCpackbomid(sourceVO.getVpackbomversion());
    
    mapVO.setVbillstatus(sourceVO.getvItemStatus());
    mapVO.setVbillstatusenumid(sourceVO.getvItemStatusEnumID());
    mapVO.setCcustmaterialid(sourceVO.getCcustmaterialid());
    return mapVO;
  }
  
  public static SupplyMapVO getOnHandSupplyMapVO(CalculateContext context, SupplyMapVO supplyMapVO)
  {
    String orgVerTable = OrgVersionCacheManager.loadTempTable(context, OrgVersionCacheManager.getOidVidMap(context));
    
    UFDate firstWorkDate = context.getStartWorkDate();
    UFDate firstDate = firstWorkDate;
    if ((context.getWorkDateCache().getWorkDays() != null) && (context.getWorkDateCache().getWorkDays().length > 0)) {
      firstDate = context.getWorkDateCache().getWorkDays()[0];
    }
    String from = supplyMapVO.getFrom();
    supplyMapVO.setFrom(from + " left join " + orgVerTable + " on " + orgVerTable + ".pk_org=" + supplyMapVO.getSupplyOrgid() + " ");
    
    //2023-11-01 liyf 中山瑞华项目处理,如果要新增字段，增加后 也要去增加临时表字段nc.bs.mmpps.calc.bp.fetch.util.SupplyGetterUtil.getTabColMap(CalculateContext context, SupplyMapVO supplyMapVO)
    supplyMapVO.setVdef1(" max(ic_storestate.iusability) ");//库存状态
//	StringBuffer dinbounddate = new StringBuffer();  //如果状态是不可用状态时，供给日期为首次入库日期+后段提前期  ;否则走系统提供的日期
//	dinbounddate.append(" MAX( case when ic_storestate.iusability  = 2 then TO_CHAR(TO_DATE(scm_batchcode.dinbounddate ,  'YYYY-MM-DD hh24:mi:ss') ");
//	dinbounddate.append(" + (select endahead from bd_materialplan where pk_material = ic_onhanddim.cmaterialoid  and pk_org = ic_onhanddim.pk_org) ,  ");
//	
//	dinbounddate.append(" 'YYYY-MM-DD hh24:mi:ss')   else  scm_batchcode.dinbounddate end  )");
//	supplyMapVO.setVdef2(dinbounddate.toString());

    supplyMapVO.setVdef2(" max(scm_batchcode.dinbounddate)  ");//首次入库日期,后续用来比较先入先出
    supplyMapVO.setVdef3(" max(scm_batchcode.dvalidate)  ");//失效日期

    //2023-11-01 liyf 中山瑞华项目处理

    String calcWhere = supplyMapVO.getWhere();
    String reverseNumFilter = "- COALESCE(ic_onhandnum.nrsnum,0.0)";
    calcWhere = calcWhere.replace(reverseNumFilter, "");
    supplyMapVO.setWhere(calcWhere);
    supplyMapVO.setSupplyOrgvid(orgVerTable + ".pk_org_v");
    //库存供给日期,按照库存都可用的思想，供给日期取firstDate
	supplyMapVO.setSupplyDate("'" + firstDate.toString() + "'");

    
///*
// * 这里被重写了生产日期赋值，原来在IC模块实现的映射已失效
// * 从这里进行拦截，通过context获取前台数据，是否考虑提前期，重新构建脚本。
// */
//    SchemaVO parentVO = context.getSchema().getParentVO();
//	UFBoolean vdef1 = new UFBoolean(parentVO.getVdef1()); //是否考虑质检计划周期  ， 如果考虑，待检物料供给日期加上物料后段提前期
//	if(vdef1.equals(UFBoolean.TRUE)){
//		StringBuffer supplydate = new StringBuffer();  //如果状态是不可用状态时，供给日期为首次入库日期+后段提前期  ;否则走系统提供的日期
//		supplydate.append(" MAX( case when ic_storestate.iusability  = 2 then TO_CHAR(TO_DATE(scm_batchcode.dinbounddate ,  'YYYY-MM-DD hh24:mi:ss') ");
//		supplydate.append(" + (select endahead from bd_materialplan where pk_material = ic_onhanddim.cmaterialoid  and pk_org = ic_onhanddim.pk_org) ,  ");
//		supplydate.append(" 'YYYY-MM-DD hh24:mi:ss')   else  '" + firstDate.toString() + "' end  )");
//		supplyMapVO.setSupplyDate(supplydate.toString());
//	}else {
//		supplyMapVO.setSupplyDate("'" + firstDate.toString() + "'");
//
//	}
//	

    String temptableDot = "M.";
    
    supplyMapVO.setProjectid(" case when " + temptableDot + "marcprojectid" + "= 'Y' then " + supplyMapVO.getProjectid() + " else '~' end ");
    

    supplyMapVO.setVendorid(" case when " + temptableDot + "marcvendorid" + "= 'Y' then " + supplyMapVO.getVendorid() + " else '~' end ");
    

    supplyMapVO.setProductorid(" case when " + temptableDot + "marcproductorid" + "= 'Y' then " + supplyMapVO.getProductorid() + " else '~' end ");
    

    supplyMapVO.setCustomerid(" case when " + temptableDot + "marccustomerid" + "= 'Y' then " + supplyMapVO.getCustomerid() + " else '~' end ");
    

    supplyMapVO.setCffileid(" case when " + temptableDot + "marcffileid" + "= 'Y' then " + supplyMapVO.getCffileid() + " else '~' end ");
    

    supplyMapVO.setFree1(" case when " + temptableDot + "marbfree1" + "= 'Y' then " + supplyMapVO.getFree1() + " else '~' end ");
    

    supplyMapVO.setFree2(" case when " + temptableDot + "marbfree2" + "= 'Y' then " + supplyMapVO.getFree2() + " else '~' end ");
    

    supplyMapVO.setFree3(" case when " + temptableDot + "marbfree3" + "= 'Y' then " + supplyMapVO.getFree3() + " else '~' end ");
    

    supplyMapVO.setFree4(" case when " + temptableDot + "marbfree4" + "= 'Y' then " + supplyMapVO.getFree4() + " else '~' end ");
    

    supplyMapVO.setFree5(" case when " + temptableDot + "marbfree5" + "= 'Y' then " + supplyMapVO.getFree5() + " else '~' end ");
    

    supplyMapVO.setFree6(" case when " + temptableDot + "marbfree6" + "= 'Y' then " + supplyMapVO.getFree6() + " else '~' end ");
    

    supplyMapVO.setFree7(" case when " + temptableDot + "marbfree7" + "= 'Y' then " + supplyMapVO.getFree7() + " else '~' end ");
    

    supplyMapVO.setFree8(" case when " + temptableDot + "marbfree8" + "= 'Y' then " + supplyMapVO.getFree8() + " else '~' end ");
    

    supplyMapVO.setFree9(" case when " + temptableDot + "marbfree9" + "= 'Y' then " + supplyMapVO.getFree9() + " else '~' end ");
    

    supplyMapVO.setFree10(" case when " + temptableDot + "marbfree10" + "= 'Y' then " + supplyMapVO.getFree10() + " else '~' end ");
    


    return supplyMapVO;
  }
}

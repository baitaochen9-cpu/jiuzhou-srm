/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.mmpps.calc.bp.utils;
import java.util.HashMap;
import java.util.Map;

import nc.impl.pubapp.pattern.data.bill.BillLazyQuery;
import nc.impl.pubapp.pattern.data.bill.IAttributeOrderConvert;
import nc.vo.mmpps.calc.entity.calculate.AggMaterialVO;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.MatchVO;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SumVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.mmpps.calc.enumeration.DemandType;
import nc.vo.mmpps.calc.enumeration.SupplyType;
import nc.vo.pubapp.util.VOSortUtils;

/**
 * 运算排序处理类
 * 
 * @author dengsw
 */
public class CalcOrderUtil {

    private static Map<Integer, Integer> demandpriorityMap;

    private static Map<Integer, Integer> supplypriorityMap;

    static {

        /** 需求 */
        CalcOrderUtil.demandpriorityMap = new HashMap<Integer, Integer>();
        // 安全库存优先级最高
        CalcOrderUtil.demandpriorityMap.put(DemandType.SAFETY_STOCK.toInteger(), Integer.valueOf(0));
        CalcOrderUtil.demandpriorityMap.put(DemandType.SALES_ORDER.toInteger(), Integer.valueOf(1));
        // 出口合同
        // CalcOrderUtil.demandpriorityMap.put(DemandType.EXPORT_CONSTRACT.toInteger(), Integer.valueOf(2));
        CalcOrderUtil.demandpriorityMap.put(DemandType.PICK_MATERIAL.toInteger(), Integer.valueOf(2));
        CalcOrderUtil.demandpriorityMap.put(DemandType.MANUFACTURE_ORDER.toInteger(), Integer.valueOf(3));
        CalcOrderUtil.demandpriorityMap.put(DemandType.INPUT_DEMAND.toInteger(), Integer.valueOf(4));
        CalcOrderUtil.demandpriorityMap.put(DemandType.TANSFER_OUT.toInteger(), Integer.valueOf(5));
        // 订单中心预订单
        CalcOrderUtil.demandpriorityMap.put(DemandType.EC_PREORDER.toInteger(), Integer.valueOf(6));
        CalcOrderUtil.demandpriorityMap.put(DemandType.TANSFER_OUT_APP.toInteger(), Integer.valueOf(7));
        CalcOrderUtil.demandpriorityMap.put(DemandType.MPS.toInteger(), Integer.valueOf(8));
        CalcOrderUtil.demandpriorityMap.put(DemandType.PLANNED_ORDER.toInteger(), Integer.valueOf(9));
        CalcOrderUtil.demandpriorityMap.put(DemandType.CUSTREQPLAN.toInteger(), Integer.valueOf(10));
        CalcOrderUtil.demandpriorityMap.put(DemandType.PLANNED_INDEPENDENT_DEMAND.toInteger(), Integer.valueOf(11));


        /** 供给 */
        CalcOrderUtil.supplypriorityMap = new HashMap<Integer, Integer>();
        CalcOrderUtil.supplypriorityMap.put(SupplyType.ONHAND_NUM.toInteger(), Integer.valueOf(1));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.DMANUFACTURE_ORDER.toInteger(), Integer.valueOf(2));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.MANUFACTURE_ORDER.toInteger(), Integer.valueOf(3));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.TRANSFER_IN.toInteger(), Integer.valueOf(4));
        // 采购到货计划
        CalcOrderUtil.supplypriorityMap.put(SupplyType.PURCHASE_ORDER_RECEIVEPLAN.toInteger(), Integer.valueOf(5));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.PURCHASE_ORDER.toInteger(), Integer.valueOf(6));
        // 进口合同
        // CalcOrderUtil.supplypriorityMap.put(SupplyType.IMPORT_CONSTRACT.toInteger(), Integer.valueOf(7));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.OUT_SOURCE.toInteger(), Integer.valueOf(8));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.TRANSFER_IN_APP.toInteger(), Integer.valueOf(9));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.PURCHASE_APP.toInteger(), Integer.valueOf(11));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.MPS.toInteger(), Integer.valueOf(12));
        CalcOrderUtil.supplypriorityMap.put(SupplyType.PLANNED_ORDER.toInteger(), Integer.valueOf(13));
    }

    public static Integer getDemandpriority(Integer demandType) {
        return CalcOrderUtil.demandpriorityMap.get(demandType);
    }

    public static Integer getSupplypriority(Integer supplyType) {
        return CalcOrderUtil.supplypriorityMap.get(supplyType);
    }

    /**
     * 需求表的排序OrderBy 不支持别名
     */
    public static String getDemandOrder() {
        String orderby =
                DemandVO.CGROUPKEY + " asc, " + DemandVO.DMATCHDATE + " asc," + DemandVO.DEMANDTIME + " asc, "
                        + DemandVO.DEMANDPRIORITY + " asc, " + DemandVO.NREMAINNUM + " desc, " + DemandVO.PK_DEMANDORG
                        + " asc, " + DemandVO.VDEMANDCODE + " asc, " + DemandVO.VDEMANDROWNO + " asc, "
                        + DemandVO.VSRCCODE + " asc, " + DemandVO.VSRCROWNO + " asc";
        return orderby;

    }
    
    /**
     * 供应表的排序
     * 
     * @return
     */
    public static String getSupplyOrder() {
    	  String orderby =
                  SupplyVO.CGROUPKEY + " asc, " + SupplyVO.DMATCHDATE + " asc," + SupplyVO.DSUPPLYDATE + " asc, "
                          + SupplyVO.SUPPLYPRIORITY + " asc, " + " (" + SupplyVO.NSUPPLYNUM + "-isnull("
                          + SupplyVO.NRESERVATIONNUM + ",0) )" + " desc, " + SupplyVO.PK_SUPPLYORG + " asc, "
                          + SupplyVO.VSUPPLYCODE + " asc, " + SupplyVO.VSUPPLYROWNO + " asc ";
 
        return orderby;
    }

    /**
     * 供应表的排序:只针对MRP运算 增加的按照入库日期进行排序
     * 
     * @return
     */
    public static String getSupplyOrder2() {
  
        //2023-11-06 liyf 按照首次入库日期进行排序+
        String orderby =
                SupplyVO.CGROUPKEY + " asc, " + SupplyVO.DMATCHDATE + " asc," + SupplyVO.DSUPPLYDATE + " asc, "
                        + SupplyVO.SUPPLYPRIORITY + " asc, SUBSTR(vdef2,0,10) asc, SUBSTR(vdef3,0,10) asc , " + " (" + SupplyVO.NSUPPLYNUM + "-isnull("
                        + SupplyVO.NRESERVATIONNUM + ",0) )" + " desc, " + SupplyVO.PK_SUPPLYORG + " asc, "
                        + SupplyVO.VSUPPLYCODE + " asc, " + SupplyVO.VSUPPLYROWNO + " asc ";
                      
		//2023-11-06 liyf 按照首次入库日期进行排序
        return orderby;
    }

    /***
     * 物料范围表排序
     * 
     * @return
     */
    public static String getMaterialOrder() {
        String orderSql =
                MaterialVO.PK_ORG + "," + MaterialVO.CPSID + "," + MaterialVO.CRUNNINGTIME + "," + MaterialVO.LOWCODE;

        return orderSql;
    }

    /***
     * 汇总表排序
     * 
     * @return
     */
    public static String getSumOrder() {
        String orderby = SumVO.CGROUPKEY + " asc, " + SumVO.DEMANDTIME + " asc";
        return orderby;
    }

    /***
     * 匹配明细表排序
     * 
     * @return
     */
    public static String getMatchOrder() {
        String orderby = MatchVO.MATCHROWNO + " asc ";
        return orderby;
    }

    public static void setBillLazilyLoaderOrderMap(BillLazyQuery<AggMaterialVO> billLazyQry) {
        // 汇总表
        billLazyQry.setOrderMap(SumVO.class, SumVO.CPPSSUMID, new IAttributeOrderConvert() {

            @Override
            public String convert(String sqlname) {
                // 按原始需求日期排序，按需求的未满足数量降序
                String orderby = CalcOrderUtil.getSumOrder();
                return orderby;
            }
        });
        // 匹配表
        billLazyQry.setOrderMap(MatchVO.class, MatchVO.CPPSMATCHID, new IAttributeOrderConvert() {

            @Override
            public String convert(String sqlname) {
                // 按原始需求日期排序，按需求的未满足数量降序
                String orderby = CalcOrderUtil.getMatchOrder();
                return orderby;
            }
        });

        // Modified by Goodie
        // setOrderAttribute 与 setOrderMap 不能共用
        // 如果有特殊要求（比如设置升序降序等其他规则）只能用 setOrderMap

        // 需求来源表
        billLazyQry.setOrderMap(DemandVO.class, DemandVO.CPPSDEMANDID, new IAttributeOrderConvert() {

            @Override
            public String convert(String sqlname) {
                // 按原始需求日期排序，按需求的未满足数量降序
                String orderby = CalcOrderUtil.getDemandOrder();

                return orderby;
            }
        });

        // 供给来源表
        billLazyQry.setOrderMap(SupplyVO.class, SupplyVO.CPPSSUPPLYID, new IAttributeOrderConvert() {

            @Override
            public String convert(String sqlname) {

                // 按原始供给日期排序，按供给的未满足数量降序
                String orderby = CalcOrderUtil.getSupplyOrder2();

                return orderby;
            }
        });
    }
}

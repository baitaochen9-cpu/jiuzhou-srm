package nc.bs.mmpps.calc.bp.fetch.util;

import java.util.LinkedHashMap;
import java.util.Map;

import nc.bs.mmpps.calc.bp.fetch.base.DBGetterConst;
import nc.bs.mmpps.calc.bp.fetch.feature.DateScopeFeatureData;
import nc.bs.mmpps.calc.bp.fetch.feature.MaterialScopeFeatureData;
import nc.bs.mmpps.calc.bp.fetch.feature.OrgFeatureData;
import nc.bs.mmpps.calc.bp.fetch.feature.PlanStrategyFeatureData;
import nc.bs.mmpps.calc.bp.fetch.supply.SupplyMapVO;
import nc.bs.mmpps.calc.bp.temptable.MaterialCurrTempTable;
import nc.bs.mmpps.calc.bp.temptable.PPSTempTable;
import nc.bs.mmpps.calc.bp.utils.CalcOrderUtil;
import nc.bs.mmpps.calc.bp.utils.DemandSupplyKeyUtil;
import nc.bs.mmpps.calc.bp.utils.PlanStrategyUtil;
import nc.bs.mmpps.calc.bp.utils.StructVFreeUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.WorkDateCache;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.mmpps.calc.enumeration.SupplyType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class SupplyGetterUtil {

	public static Map<String, String> getReserveTabColMap(
			CalculateContext context, SupplyMapVO supplyMapVO) {
		Map<String, String> map = SupplyGetterUtil.getCommonTabColMap(context,
				supplyMapVO);
		map.put(SupplyVO.VSUPPLYTYPECODE, supplyMapVO.getSupplyTypeIdValue());
		map.put(SupplyVO.BRESERVEONLY, "'" + UFBoolean.TRUE.toString() + "'");
		// 排除存量
		map.put("dr", " case when " + supplyMapVO.getSupplyTypeIdValue()
				+ "='~' then 1 else 0 end ");
		map.put(SupplyVO.CMATERIALID,
				"case when " + supplyMapVO.getSupplyTypeIdValue()
						+ "='~' then '@@@' else " + supplyMapVO.getMaterialid()
						+ " end ");
		map.put(SupplyVO.VSUPPLYBID,
				"case when " + supplyMapVO.getSupplyTypeIdValue()
						+ "='~' then '@@@' else " + supplyMapVO.getSupplybid()
						+ " end ");

		return map;
	}

	public static Map<String, String> getOnhandReserveTabColMap(
			CalculateContext context, SupplyMapVO supplyMapVO) {

		String t = DBGetterConst.MATERIAL_ALIAS;

		Map<String, String> map = SupplyGetterUtil.getCommonTabColMap(context,
				supplyMapVO);
		map.put(SupplyVO.VSUPPLYTYPECODE, supplyMapVO.getSupplyTypeIdValue());
		map.put(SupplyVO.BRESERVEONLY, "'" + UFBoolean.TRUE.toString() + "'");
		map.put(SupplyVO.CPPSSUPPLYID, "max(" + map.get(SupplyVO.CPPSSUPPLYID)
				+ ")");
		map.put(SupplyVO.VSUPPLYID, "max(" + map.get(SupplyVO.VSUPPLYID) + ")");
		map.remove(SupplyVO.VSUPPLYBID);
		map.remove(SupplyVO.VSUPPLYROWNO);
		map.put(SupplyVO.PK_SUPPLYORG_V,
				"max(" + map.get(SupplyVO.PK_SUPPLYORG_V) + ")");
		map.put(SupplyVO.CMATERIALVID, "max(" + map.get(SupplyVO.CMATERIALVID)
				+ ")");
		map.remove(SupplyVO.VSUPPLYCODE);
		map.put(SupplyVO.DSUPPLYDATE, "max(" + map.get(SupplyVO.DSUPPLYDATE)
				+ ")");
		map.put(SupplyVO.CVENDORID, "case when " + t + "."
				+ MaterialVO.MARCVENDORID + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.CVENDORID) + " else '~' end ");
		map.put(SupplyVO.CPRODUCTORID, "case when " + t + "."
				+ MaterialVO.MARCPRODUCTORID + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.CPRODUCTORID) + " else '~' end ");
		map.put(SupplyVO.CPROJECTID, "case when " + t + "."
				+ MaterialVO.MARCPROJECTID + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.CPROJECTID) + " else '~' end ");
		map.put(SupplyVO.CCUSTOMERID, "case when " + t + "."
				+ MaterialVO.MARCCUSTOMERID + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.CCUSTOMERID) + " else '~' end ");
		map.put(SupplyVO.CFFILEID, "case when " + t + "."
				+ MaterialVO.MARCFFILEID + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.CFFILEID) + " else '~' end ");
		map.put(SupplyVO.VFREE1,
				"case when " + t + "." + MaterialVO.MARBFREE1 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE1) + " else '~' end ");
		map.put(SupplyVO.VFREE2,
				"case when " + t + "." + MaterialVO.MARBFREE2 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE2) + " else '~' end ");
		map.put(SupplyVO.VFREE3,
				"case when " + t + "." + MaterialVO.MARBFREE3 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE3) + " else '~' end ");
		map.put(SupplyVO.VFREE4,
				"case when " + t + "." + MaterialVO.MARBFREE4 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE4) + " else '~' end ");
		map.put(SupplyVO.VFREE5,
				"case when " + t + "." + MaterialVO.MARBFREE5 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE5) + " else '~' end ");
		map.put(SupplyVO.VFREE6,
				"case when " + t + "." + MaterialVO.MARBFREE6 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE6) + " else '~' end ");
		map.put(SupplyVO.VFREE7,
				"case when " + t + "." + MaterialVO.MARBFREE7 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE7) + " else '~' end ");
		map.put(SupplyVO.VFREE8,
				"case when " + t + "." + MaterialVO.MARBFREE8 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE8) + " else '~' end ");
		map.put(SupplyVO.VFREE9,
				"case when " + t + "." + MaterialVO.MARBFREE9 + "='"
						+ UFBoolean.TRUE.toString() + "' then "
						+ map.get(SupplyVO.VFREE9) + " else '~' end ");
		map.put(SupplyVO.VFREE10, "case when " + t + "."
				+ MaterialVO.MARBFREE10 + "='" + UFBoolean.TRUE.toString()
				+ "' then " + map.get(SupplyVO.VFREE10) + " else '~' end ");
		map.put(SupplyVO.CGROUPKEY,
				"max(" + OnHandMapUtil.getGroupKeySqlSegment(map, context)
						+ ")");
		map.put(SupplyVO.CCUSTMATERIALID,
				"max(" + map.get(SupplyVO.CCUSTMATERIALID) + ")");
		map.remove(SupplyVO.VSUPPLYTYPECODE);
		map.put(SupplyVO.LEVELCODE, "max(" + map.get(SupplyVO.LEVELCODE) + ")");
		map.put(SupplyVO.NBILLNUM, "sum(" + map.get(SupplyVO.NBILLNUM) + ")");
		map.put(SupplyVO.NSUPPLYNUM, "sum(" + map.get(SupplyVO.NSUPPLYNUM)
				+ ")");
		map.remove(SupplyVO.NBILLEXENUM);
		map.remove(SupplyVO.FBILLSTATUS);
		map.put(SupplyVO.SUPPLYKEY,
				OnHandMapUtil.getSuplyKeySqlSegment(map, context));
		map.put(SupplyVO.PK_BATCHCODE, "max(" + SupplyVO.PK_BATCHCODE + ")");
		map.remove(SupplyVO.NREMAINNUM);
		map.remove(SupplyVO.NRESERVATIONNUM);

		map.remove(SupplyVO.VFIRSTID);
		map.remove(SupplyVO.VFIRSTBID);
		map.remove(SupplyVO.VFIRSTCODE);
		map.remove(SupplyVO.VFIRSTROWNO);
		map.remove(SupplyVO.VFIRSTTYPE);

		map.remove(SupplyVO.VFIRSTDEMANDBID);
		map.remove(SupplyVO.VFIRSTDEMANDID);
		map.remove(SupplyVO.VFIRSTDEMANDCODE);
		map.remove(SupplyVO.VFIRSTDEMANDROWNO);
		map.remove(SupplyVO.VFIRSTDEMANDTYPE);

		map.remove(SupplyVO.VSRCID);
		map.remove(SupplyVO.VSRCBID);
		map.remove(SupplyVO.VSRCCODE);
		map.remove(SupplyVO.VSRCROWNO);
		map.remove(SupplyVO.VSRCTYPE);

		map.put(SupplyVO.VSUPPLYTYPECODE, "'~'");
		map.put(SupplyVO.FBILLSTATUS, "0");
		return map;
	}

	public static DateScopeFeatureData getDateScopeFeatureData(
			CalculateContext calcContext, SupplyMapVO supplyMapVO) {
		WorkDateCache cache = calcContext.getWorkDateCache();
		DateScopeFeatureData para = new DateScopeFeatureData();
		para.setBillDateCol(supplyMapVO.getSupplyDate());
		para.setBillMaxDateValue(cache.getMaterialDateScope().get(
				MaterialVO.DSUPPLYEND));
		para.setBillMinDateValue(cache.getMaterialDateScope().get(
				MaterialVO.DSUPPLYSTART));
		para.setMaterialEndDateCol(DBGetterConst.MATERIAL_ALIAS + "."
				+ MaterialVO.DSUPPLYEND);
		para.setMaterialStartDateCol(DBGetterConst.MATERIAL_ALIAS + "."
				+ MaterialVO.DSUPPLYSTART);
		return para;
	}

	public static OrgFeatureData getOrgFeatureData(SupplyMapVO supplyMapVO) {
		OrgFeatureData para = new OrgFeatureData();
		para.setStockOrgColumnName(supplyMapVO.getSupplyOrgid());
		return para;
	}

	public static PlanStrategyFeatureData getPlanStrategyFeatureData(
			Integer supplyType) {
		PlanStrategyFeatureData para = new PlanStrategyFeatureData();
		para.setPlanStrategyDSCol(PlanStrategyUtil.getSupplyTypeCol(supplyType));
		return para;
	}

	/**
	 * 通用属性映射
	 * @param context
	 * @param supplyMapVO
	 * @return
	 */
	public static Map<String, String> getCommonTabColMap(
			CalculateContext context, SupplyMapVO supplyMapVO) {
		SchemaVO schema = context.getSchema().getParentVO();
		String pkGrp = schema.getPk_group();
		String pkOrg = schema.getPk_org();
		String pkOrgv = schema.getPk_org_v();

		String pk_bill = supplyMapVO.getSupplyid();
		String pk_bill_b = supplyMapVO.getSupplybid();

		Map<String, String> tabColMap = new LinkedHashMap<String, String>();

		tabColMap.put(SupplyVO.CPPSSUPPLYID, "'" + context.getCalculateCode()
				+ "'||" + (pk_bill_b == null ? pk_bill : pk_bill_b));// 1 主键
		tabColMap.put(SupplyVO.CPPSMATERIALID, DBGetterConst.MATERIAL_ALIAS
				+ "." + MaterialVO.CPPSMATERIALID);// 2
													// 物料范围主键
		tabColMap.put(SupplyVO.PK_GROUP, "'" + pkGrp + "'");// 3 集团
		tabColMap.put(SupplyVO.PK_ORG, "'" + pkOrg + "'");// 4 库存组织
		tabColMap.put(SupplyVO.PK_ORG_V, "'" + pkOrgv + "'");// 5 库存组织版本

		tabColMap.put(SupplyVO.VSUPPLYID, supplyMapVO.getSupplyid());// 7 供给表头主键
		tabColMap.put(SupplyVO.VSUPPLYBID, supplyMapVO.getSupplybid());// 8
																		// 供给表体主键
		tabColMap.put(SupplyVO.VSUPPLYROWNO, supplyMapVO.getSupplyRowNo());// 9
																			// 供给单据行号
		tabColMap.put(SupplyVO.PK_SUPPLYORG, supplyMapVO.getSupplyOrgid());// 10
																			// 供给工厂
		tabColMap.put(SupplyVO.PK_SUPPLYORG_V, supplyMapVO.getSupplyOrgvid());// 11
																				// 供给工厂版本
		tabColMap.put(SupplyVO.CMATERIALID, supplyMapVO.getMaterialid());// 12
																			// 物料主键
		tabColMap.put(SupplyVO.CMATERIALVID, supplyMapVO.getMaterialvid());// 13
																			// 物料版本主键

		tabColMap.put(SupplyVO.VSUPPLYCODE, supplyMapVO.getSupplyCode());// 15
																			// 供给单据号
		tabColMap.put(SupplyVO.DSUPPLYDATE,
				"substring(" + supplyMapVO.getSupplyDate()
						+ ",1,10) || ' 00:00:00'");// 16
		// 供给日期
		tabColMap.put(SupplyVO.NSUPPLYNUM, supplyMapVO.getNsupplynum());// 17
																		// 供给数量

		if (supplyMapVO.getReservatioNnum() != null) {
			tabColMap.put(SupplyVO.NRESERVATIONNUM,
					supplyMapVO.getReservatioNnum());// 18
			// 预留数量
			String caseWhen = "case when (" + supplyMapVO.getNsupplynum()
					+ "-isnull(" + supplyMapVO.getReservatioNnum() + ",0))>0"
					+ " then " + supplyMapVO.getNsupplynum() + "-isnull("
					+ supplyMapVO.getReservatioNnum() + ",0) else 0 end";
			tabColMap.put(SupplyVO.NREMAINNUM, caseWhen);// 1801供给数量
		} else {
			// modify by duanxf. tabColMap.put(SupplyVO.NRESERVATIONNUM, "0");
			tabColMap.put(SupplyVO.NRESERVATIONNUM, null);// 18 预留数量
			tabColMap.put(SupplyVO.NREMAINNUM, supplyMapVO.getNsupplynum());//
			// 1801供给数量
		}

		tabColMap.put(SupplyVO.CVENDORID, supplyMapVO.getVendorid());// 19 供应商
		tabColMap.put(SupplyVO.CPRODUCTORID, supplyMapVO.getProductorid());// 20
																			// 生产厂商
		tabColMap.put(SupplyVO.CPROJECTID, supplyMapVO.getProjectid());// 21 项目
		tabColMap.put(SupplyVO.CCUSTOMERID, supplyMapVO.getCustomerid());// 22
																			// 客户
		tabColMap.put(SupplyVO.CFFILEID, supplyMapVO.getCffileid());// 特征码
		tabColMap.put(SupplyVO.VFREE1, supplyMapVO.getFree1());// 23 自由项1
		tabColMap.put(SupplyVO.VFREE2, supplyMapVO.getFree2());// 自由项2
		tabColMap.put(SupplyVO.VFREE3, supplyMapVO.getFree3());// 自由项3
		tabColMap.put(SupplyVO.VFREE4, supplyMapVO.getFree4());// 自由项4
		tabColMap.put(SupplyVO.VFREE5, supplyMapVO.getFree5());// 自由项5
		tabColMap.put(SupplyVO.VFREE6, supplyMapVO.getFree6());// 自由项6
		tabColMap.put(SupplyVO.VFREE7, supplyMapVO.getFree7());// 自由项7
		tabColMap.put(SupplyVO.VFREE8, supplyMapVO.getFree8());// 自由项8
		tabColMap.put(SupplyVO.VFREE9, supplyMapVO.getFree9());// 自由项9
		tabColMap.put(SupplyVO.VFREE10, supplyMapVO.getFree10());// 自由项10

		// 主生产计划、计划订单、离散生产订单、流程生产订单 BOM的主组织和当前运算的组织不同就这么处理
		if (MMValueCheck.isNotEmpty(supplyMapVO.getCbomid())) {
			tabColMap.put(SupplyVO.CPRODBOMID, GetterUtil.getBomDealingSql(
					supplyMapVO.getCbomid(), supplyMapVO.getPk_org(),
					context.getPk_org()));
			tabColMap.put(SupplyVO.CPACKBOMID, GetterUtil.getBomDealingSql(
					supplyMapVO.getCpackbomid(), supplyMapVO.getPk_org(),
					context.getPk_org()));
		}

		// tabColMap.put(SupplyVO.CPRODBOMID, supplyMapVO.getCbomid());//
		// 生产BOM版本
		// tabColMap.put(SupplyVO.CPACKBOMID, supplyMapVO.getCpackbomid());//
		// 包装BOM版本

		// tabColMap.put(SupplyVO.FBILLSTATUS,
		// GetterUtil.null2DefaultStr(supplyMapVO.getVbillstatus()));
		//
		// if (MMValueCheck.isEmpty(supplyMapVO.getVbillstatusenumid())) {
		// tabColMap.put(SupplyVO.FBILLSTATUSMETAID,
		// GetterUtil.null2DefaultStr(supplyMapVO.getVbillstatusenumid()));
		// }
		// else {
		// tabColMap.put(SupplyVO.FBILLSTATUSMETAID,
		// "'" + GetterUtil.null2DefaultStr(supplyMapVO.getVbillstatusenumid())
		// + "'");
		// }

		tabColMap.put(SupplyVO.COMPUTECODE, "'" + context.getCalculateCode()
				+ "'");// 24 运算标识
		tabColMap.put(SupplyVO.CGROUPKEY, StructVFreeUtil.getGroupKeySQL(
				supplyMapVO, DBGetterConst.MATERIAL_ALIAS));// 25
		// 分组key
		tabColMap.put(SupplyVO.LEVELCODE, DBGetterConst.MATERIAL_ALIAS + "."
				+ MaterialVO.LOWCODE);// 26 物料低阶码

		tabColMap.put("dr", "0");// 27 DR
		// 如果单据上没有这个字段则是Null
		if (MMValueCheck.isEmpty(supplyMapVO.getVfirstid())) {

		} else {
			tabColMap.put(SupplyVO.VFIRSTTYPE,
					GetterUtil.null2DefaultStr(supplyMapVO.getVfirsttype()));// 27
																				// 源头单据信息
			tabColMap.put(SupplyVO.VFIRSTCODE,
					GetterUtil.null2DefaultStr(supplyMapVO.getVfirstcode()));// 源头单据号
			tabColMap.put(SupplyVO.VFIRSTROWNO, supplyMapVO.getVfirstrowno());// 源头单据行号
			tabColMap.put(SupplyVO.VFIRSTID,
					GetterUtil.null2DefaultStr(supplyMapVO.getVfirstid()));// 源头单据ID
			tabColMap.put(SupplyVO.VFIRSTBID,
					GetterUtil.null2DefaultStr(supplyMapVO.getVfirstbid()));// 源头单据行ID

			// 原始需求单据信息
			tabColMap.put(
					SupplyVO.VFIRSTDEMANDTYPE,
					GetterUtil.getVfirstDemandCondStr(
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getSupplyTypeCodeValue(),
							supplyMapVO.getSupplyTypeCodeValue()));
			tabColMap.put(
					SupplyVO.VFIRSTDEMANDID,
					GetterUtil.getVfirstDemandCondStr(
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getVfirstid(),
							supplyMapVO.getSupplyTypeCodeValue(),
							supplyMapVO.getSupplyid()));
			tabColMap.put(
					SupplyVO.VFIRSTDEMANDBID,
					GetterUtil.getVfirstDemandCondStr(
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getVfirstbid(),
							supplyMapVO.getSupplyTypeCodeValue(),
							supplyMapVO.getSupplybid()));
			tabColMap.put(
					SupplyVO.VFIRSTDEMANDROWNO,
					GetterUtil.getVfirstDemandCondStr(
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getVfirstrowno(),
							supplyMapVO.getSupplyTypeCodeValue(),
							supplyMapVO.getSupplyRowNo()));
			tabColMap.put(
					SupplyVO.VFIRSTDEMANDCODE,
					GetterUtil.getVfirstDemandCondStr(
							supplyMapVO.getVfirsttype(),
							supplyMapVO.getVfirstcode(),
							supplyMapVO.getSupplyTypeCodeValue(),
							supplyMapVO.getSupplyCode()));

		}
		if (MMValueCheck.isEmpty(supplyMapVO.getVsrcid())) {

		} else {
			// V63 来源单据信息
			tabColMap.put(SupplyVO.VSRCTYPE,
					GetterUtil.null2DefaultStr(supplyMapVO.getVsrctype()));// 27
																			// 来源单据类型
			tabColMap.put(SupplyVO.VSRCCODE,
					GetterUtil.null2DefaultStr(supplyMapVO.getVsrccode()));// 来源单据号
			tabColMap.put(SupplyVO.VSRCROWNO, supplyMapVO.getVsrcrowno());// 来源单据行号
			tabColMap.put(SupplyVO.VSRCID,
					GetterUtil.null2DefaultStr(supplyMapVO.getVsrcid()));// 来源单据ID
			tabColMap.put(SupplyVO.VSRCBID,
					GetterUtil.null2DefaultStr(supplyMapVO.getVsrcbid()));// 来源单据行ID

		}

		tabColMap.put(SupplyVO.CCUSTMATERIALID,
				GetterUtil.null2DefaultStr(supplyMapVO.getCcustmaterialid()));// 客户物料码

		// TODO For DEBUG
		// tabColMap.put(SupplyVO.VDEF20,
		// DemandSupplyKeyUtil.getSupplyKeySQLNoMD5(supplyMapVO, supplyType));

		tabColMap.put(SupplyVO.NBILLNUM, supplyMapVO.getNbillnum());
		tabColMap.put(SupplyVO.NBILLEXENUM, supplyMapVO.getNbillexenum());

		tabColMap.put(SupplyVO.CUNITID, supplyMapVO.getCunitid());
		tabColMap.put(SupplyVO.FBILLSTATUS, supplyMapVO.getVbillstatus());

		tabColMap.put(SupplyVO.ISSUBS, "'" + UFBoolean.FALSE.toString() + "'");
		tabColMap.put(SupplyVO.BCOPRODUCT, "'" + UFBoolean.FALSE.toString()
				+ "'");

		// v65交易类型、委外订单联副供给
		// tabColMap.put(SupplyVO.VSUPPLYTRANSTYPE,
		// supplyMapVO.getVtranstype());
		


		tabColMap.put(SupplyVO.BVISIBLE, "'" + UFBoolean.TRUE.toString() + "'");
		tabColMap.put(SupplyVO.BRESERVEONLY, "'" + UFBoolean.FALSE.toString()
				+ "'");
		tabColMap.put("TS", "'" + new UFDate() + "'");
		return tabColMap;

	}
/**
 * 创建需求映射与表字段对关系
 * @param context
 * @param supplyMapVO
 * @return
 */
	public static Map<String, String> getTabColMap(CalculateContext context,
			SupplyMapVO supplyMapVO) {

		Map<String, String> tabColMap = SupplyGetterUtil.getCommonTabColMap(
				context, supplyMapVO);

		Integer supplyType = supplyMapVO.getSupplyType();
		String billType = supplyMapVO.getSupplyTypeCodeValue();

		tabColMap.put(SupplyVO.FSUPPLYTYPE, supplyType.toString());// 6 供应类型
		tabColMap.put(SupplyVO.VSUPPLYTYPECODE, "'" + billType + "'");// 14//
																		// 供给单据类型
		tabColMap.put(SupplyVO.SUPPLYKEY,
				DemandSupplyKeyUtil.getSupplyKeySQL(supplyMapVO, supplyType));
		// 生产订单不需要取生产批次号，因为供需预定那边显示的库存批次号
		if (!SupplyType.DMANUFACTURE_ORDER.toInteger().equals(supplyType)
				&& !SupplyType.MANUFACTURE_ORDER.toInteger().equals(supplyType)) {
			tabColMap.put(SupplyVO.PK_BATCHCODE, supplyMapVO.getPk_batchcode());
			tabColMap.put(SupplyVO.VBATCHCODE, supplyMapVO.getVbatchcode());
		}
		tabColMap.put(SupplyVO.SUPPLYPRIORITY,
				CalcOrderUtil.getSupplypriority(supplyType).toString());
		
	    //2023-11-01 liyf 中山瑞华项目处理
		if (supplyType == SupplyType.ONHAND_NUM
				.toInt()){	
			//2023-11-06 liyf
		    SchemaVO parentVO =  context.getSchema().getParentVO();
			UFBoolean vdef1 = new UFBoolean(parentVO.getVdef1()); //是否考虑质检计划周期  ， 如果考虑，待检物料供给日期加上物料后段提前期
			if(vdef1.equals(UFBoolean.TRUE)){
				tabColMap.put(SupplyVO.DSUPPLYDATE,	"substring(" + supplyMapVO.getVdef2()+ ",1,10) || ' 00:00:00'");// 16
			}
			tabColMap.put(SupplyVO.VDEF1, supplyMapVO.getVdef1());//库存状态
			tabColMap.put(SupplyVO.VDEF2, supplyMapVO.getVdef2());//首次入库日期
			tabColMap.put(SupplyVO.VDEF3, supplyMapVO.getVdef3());//失效日期
		}
	

	    //2023-11-01 liyf 中山瑞华项目处理

		return tabColMap;

	}

	public static MaterialScopeFeatureData getMaterialScopeFeatureData(
			CalculateContext calcContext, SupplyMapVO demandMapVO) {
		String materialTempTable = PPSTempTable.getInstance(
				MaterialCurrTempTable.class, calcContext).getRealTableName();
		MaterialScopeFeatureData para = new MaterialScopeFeatureData();
		para.setMaterialColumnName(demandMapVO.getMaterialid());
		para.setMaterialTable(materialTempTable);
		return para;
	}
}

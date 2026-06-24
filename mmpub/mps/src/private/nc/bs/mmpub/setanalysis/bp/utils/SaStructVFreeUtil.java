package nc.bs.mmpub.setanalysis.bp.utils;

import java.security.MessageDigest;

import nc.impl.pubapp.pattern.database.DBTool;
import nc.jdbc.framework.util.DBConsts;
import nc.pubitf.ic.onhand.mm.seta.OnhandSetaSupplyMapVO;
import nc.pubitf.mmpac.dmo.mm.DMOSupplyMapVO;
import nc.pubitf.mmpub.sdmanage.ISupplyResult;
import nc.pubitf.pu.m20.mmpub.setanalysis.PrayOrderSaSupplyMapVO;
import nc.pubitf.pu.m21.mmpps.ArrivePlanSupplyMapVO;
import nc.pubitf.pu.m21.mmpub.setanalysis.PurchaseOrderSaSupplyMapVO;
import nc.pubitf.sc.m61.mm.SCOrderSaSupplyMapVO;
import nc.pubitf.to.m5a.mmpub.setanalysis.ITransInAppSaSupplyMap;
import nc.pubitf.to.m5x.mmpub.setanalysis.IM5XResultVOForSa;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmpub.dpub.db.SqlBuilder;
import nc.vo.bd.bom.bom0202.paramvo.mmpps.BomItemView;
import nc.vo.bd.bom.bom0202.paramvo.mmpps.BomOutputView;
import nc.vo.mmmps.mps.paravo.mm.MPSSupplyMapVO;
import nc.vo.mmpac.pmo.parameter.PMOSupplyMapVO;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.mmpps.plo.paravo.mm.PloSupplyMapVO;
import nc.vo.mmpub.setanalysis.entity.SaDemandVO;
import nc.vo.mmpub.setanalysis.entity.SaMatchVO;
import nc.vo.mmpub.setanalysis.entity.SaMaterialVO;
import nc.vo.mmpub.setanalysis.entity.SaSupplyVO;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 自由辅助属性处理类
 * 
 * @since 6.3
 * @version 2012-7-16 下午03:03:12
 * @author guojunf
 */
public class SaStructVFreeUtil {

	public static String getGroupKeySQL(nc.pubitf.scmpub.scmpub.mmpps.calc.IDemandResultForCalc scmDemandResult,
			String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(scmDemandResult.getFree1());
		return SaStructVFreeUtil.getGroupKeySQL(scmDemandResult.getVendorid(), scmDemandResult.getProductorid(),
				scmDemandResult.getProjectid(), scmDemandResult.getCustomerid(), freeprefix, mtrlName);
	}

	/**
	 * 取得调拨订单分组SQL TODO 调拨订单
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySql(IM5XResultVOForSa supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getFfileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得请购单分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(PrayOrderSaSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 采购订单分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(PurchaseOrderSaSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得现存量分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(OnhandSetaSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		// TODO 存量使用辅助属性匹配 v631要改成辅助属性。63支持结构辅助属性
		return SaStructVFreeUtil.getGroupKeySQLForOnhand(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), freeprefix, mtrlName, supplyVO.getFfileid());
	}

	/**
	 * 取得调入申请分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(ITransInAppSaSupplyMap supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getFfileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得流程生产订单分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(PMOSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得计划订单分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(PloSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得离散生产订单分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(DMOSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得到货计划分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(ArrivePlanSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getPprojectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	/**
	 * 取得委外订单的分组SQL
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(SCOrderSaSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	// TODO to change
	// public static String
	// getGroupKeySQL(nc.vo.mmdp.pid.mmpps.calc.DemandResultForCalcVO
	// scmDemandResult, String
	// mtrlName) {
	// String freeprefix =
	// SaStructVFreeUtil.getFreePrefix(scmDemandResult.getVfree1());
	// return SaStructVFreeUtil.getGroupKeySQL(scmDemandResult.getVendorid(),
	// scmDemandResult.getCproductorid(),
	// scmDemandResult.getCprojectid(), scmDemandResult.getCcustomerid(),
	// freeprefix,
	// mtrlName);
	// }

	public static String getGroupKeySQL(nc.pubitf.mmpub.pub.mmpps.calc.IDemandResultForCalc scmDemandResult,
			String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(scmDemandResult.getFree1());
		return SaStructVFreeUtil.getGroupKeySQL(scmDemandResult.getVendorid(), scmDemandResult.getProductorid(),
				scmDemandResult.getProjectid(), scmDemandResult.getCustomerid(), freeprefix, mtrlName);
	}

	public static String getGroupKeySQL(nc.pubitf.scmpub.scmpub.mmpps.calc.ISupplyResultForCalc scmDemandResult,
			String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(scmDemandResult.getFree1());
		return SaStructVFreeUtil.getGroupKeySQL(scmDemandResult.getVendorid(), scmDemandResult.getProductorid(),
				scmDemandResult.getProjectid(), scmDemandResult.getCustomerid(), freeprefix, mtrlName);
	}

	public static String getGroupKeySQL(ISupplyResult supplyResult, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyResult.getFree1());
		return SaStructVFreeUtil.getGroupKeySQL(supplyResult.getVendorid(), supplyResult.getProductorid(),
				supplyResult.getPprojectid(), supplyResult.getCustomerid(), freeprefix, mtrlName);
	}

	/**
	 * 取得分组Key
	 * 
	 * @param supplyVO
	 * @param mtrlName
	 * @return
	 */
	public static String getGroupKeySQL(MPSSupplyMapVO supplyVO, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(supplyVO.getFree1());
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyVO.getVendorid(), supplyVO.getProductorid(),
				supplyVO.getProjectid(), supplyVO.getCustomerid(), supplyVO.getCffileid(), freeprefix, mtrlName);
	}

	public static String getGroupKeySQL(nc.pubitf.mmpub.pub.mmpps.calc.ISupplyResultForCalc scmDemandResult,
			String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(scmDemandResult.getFree1());
		return SaStructVFreeUtil.getGroupKeySQL(scmDemandResult.getVendorid(), scmDemandResult.getProductorid(),
				scmDemandResult.getProjectid(), scmDemandResult.getCustomerid(), freeprefix, mtrlName);
	}

	public static String getDemandGroupKeySQL(String demandTable, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(SaDemandVO.VFREE1);
		String demandTName = demandTable + ".";
		return SaStructVFreeUtil.getGroupKeySQLForSA(demandTName + SaDemandVO.CVENDORID,
				demandTName + SaDemandVO.CPRODUCTORID, demandTName + SaDemandVO.CPROJECTID,
				demandTName + SaDemandVO.CCUSTOMERID, SaDemandVO.CFFILEID, demandTName + freeprefix, mtrlName);
	}

	public static String getSupplyGroupKeySQL(String supplyTable, String mtrlName) {
		String freeprefix = SaStructVFreeUtil.getFreePrefix(SaDemandVO.VFREE1);
		String supplyTName = supplyTable + ".";
		return SaStructVFreeUtil.getGroupKeySQLForSA(supplyTName + SaDemandVO.CVENDORID,
				supplyTName + SaDemandVO.CPRODUCTORID, supplyTName + SaDemandVO.CPROJECTID,
				supplyTName + SaDemandVO.CCUSTOMERID, SaDemandVO.CFFILEID, supplyTName + freeprefix, mtrlName);
	}

	public static String getFreePrefix(String free1name) {
		String freeprefix = "";
		if (MMValueCheck.isNotEmpty(free1name) && free1name.toLowerCase().contains("free1")) {
			freeprefix = free1name.substring(0, free1name.length() - 1);
		} else {
			freeprefix = "";
		}
		return freeprefix;
	}

	public static String getGroupKeySQL(String vendor, String productor, String project, String customer,
			String freeprefix, String materialTable) {
		String mtrlName = materialTable + ".";
		SqlBuilder sql = new SqlBuilder();
		sql.append(mtrlName + SaMaterialVO.CMATERIALID);

//		// 19项目
//		sql.append(" case when " + mtrlName + SaMaterialVO.BPROJECTID + "= 'Y' then isnull(" + project
//				+ ",'~') else '~' end " + " || ");
//		// 20 供应商
//		sql.append(" case when " + mtrlName + SaMaterialVO.BVENDORID + "= 'Y' then isnull(" + vendor
//				+ ",'~') else '~' end " + " || ");
//		// 21 生产厂商
//		sql.append(" case when " + mtrlName + SaMaterialVO.BPRODUCTORID + "= 'Y' then isnull(" + productor
//				+ ",'~') else '~' end " + " || ");
//		// 22 客户
//		sql.append(" case when " + mtrlName + SaMaterialVO.BCUSTOMERID + "= 'Y' then isnull(" + customer
//				+ ",'~') else '~' end " + " || ");
//
//		// 23 自由项1
//		if (MMValueCheck.isNotEmpty(freeprefix)) {
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE1 + "= 'Y' then isnull(" + freeprefix + "1"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE2 + "= 'Y' then isnull(" + freeprefix + "2"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE3 + "= 'Y' then isnull(" + freeprefix + "3"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE4 + "= 'Y' then isnull(" + freeprefix + "4"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE5 + "= 'Y' then isnull(" + freeprefix + "5"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE6 + "= 'Y' then isnull(" + freeprefix + "6"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE7 + "= 'Y' then isnull(" + freeprefix + "7"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE8 + "= 'Y' then isnull(" + freeprefix + "8"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE9 + "= 'Y' then isnull(" + freeprefix + "9"
//					+ ",'~') else '~' end " + " || ");
//			sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE10 + "= 'Y' then isnull(" + freeprefix + "10"
//					+ ",'~') else '~' end ");
//		} else {
//			sql.append(" '~~~~~~~~~~' ");
//		}

		return SaStructVFreeUtil.getMD5Func(sql.toString());
	}

	private static String getGroupKeySQLForSA(String vendor, String productor, String project, String customer,
			String cffileid, String freeprefix, String materialTable) {
		String mtrlName = materialTable + ".";
		SqlBuilder sql = new SqlBuilder();
		sql.append(mtrlName + SaMaterialVO.CMATERIALID);

//        // 19项目
//        sql.append(" case when " + mtrlName + SaMaterialVO.BPROJECTID + "= 'Y' then isnull(" + project
//                + ",'~') else '~' end " + " || ");
//        // 20 供应商
//        sql.append(" case when " + mtrlName + SaMaterialVO.BVENDORID + "= 'Y' then isnull(" + vendor
//                + ",'~') else '~' end " + " || ");
//        // 21 生产厂商
//        sql.append(" case when " + mtrlName + SaMaterialVO.BPRODUCTORID + "= 'Y' then isnull(" + productor
//                + ",'~') else '~' end " + " || ");
//        // 22 客户
//        sql.append(" case when " + mtrlName + SaMaterialVO.BCUSTOMERID + "= 'Y' then isnull(" + customer
//                + ",'~') else '~' end " + " || ");
//        // 2201 特征码
//        sql.append(" case when " + mtrlName + SaMaterialVO.BCFFILEID + "='Y' then isnull(" + cffileid
//                + ",'~') else '~' end " + " || ");
//        // 23 自由项1
//        if (MMValueCheck.isNotEmpty(freeprefix)) {
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE1 + "= 'Y' then isnull(" + freeprefix + "1"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE2 + "= 'Y' then isnull(" + freeprefix + "2"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE3 + "= 'Y' then isnull(" + freeprefix + "3"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE4 + "= 'Y' then isnull(" + freeprefix + "4"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE5 + "= 'Y' then isnull(" + freeprefix + "5"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE6 + "= 'Y' then isnull(" + freeprefix + "6"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE7 + "= 'Y' then isnull(" + freeprefix + "7"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE8 + "= 'Y' then isnull(" + freeprefix + "8"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE9 + "= 'Y' then isnull(" + freeprefix + "9"
//                    + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BVFREE10 + "= 'Y' then isnull(" + freeprefix + "10"
//                    + ",'~') else '~' end ");
//        }
//        else {
//            sql.append(" '~~~~~~~~~~' ");
//        }

		return SaStructVFreeUtil.getMD5Func(sql.toString());
	}

	/**
	 * 库存做供给时,考虑辅助属性,非结构化辅助属性(与运算不同) 2013.3.4 wanghpc,zhaohyc
	 * 
	 * @param vendor
	 * @param productor
	 * @param project
	 * @param customer
	 * @param freeprefix
	 * @param materialTable
	 * @return
	 */
	public static String getGroupKeySQLForOnhand(String vendor, String productor, String project, String customer,
			String freeprefix, String materialTable, String cffileid) {
		String mtrlName = materialTable + ".";
		SqlBuilder sql = new SqlBuilder();
		sql.append(mtrlName + SaMaterialVO.CMATERIALID);
		// 19项目
//        sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOPROJECTID + "= 'Y' then isnull(" + project
//                + ",'~') else '~' end " + " || ");
//        // 20 供应商
//        sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOVENDORID + "= 'Y' then isnull(" + vendor
//                + ",'~') else '~' end " + " || ");
//        // 21 生产厂商
//        sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOPRODUCTORID + "= 'Y' then  isnull(" + productor
//                + ",'~') else '~' end " + " || ");
//        // 22 客户
//        sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOCUSTOMERID + "= 'Y' then  isnull(" + customer
//                + ",'~') else '~' end " + " || ");
//        // 2201 特征码
//        sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOCFFILEID + "= 'Y' then  isnull(" + cffileid
//                + ",'~') else '~' end " + " || ");
//        // 23 自由项1
//        if (MMValueCheck.isNotEmpty(freeprefix)) {
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE1 + "= 'Y' then  isnull(" + freeprefix
//                    + "1" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE2 + "= 'Y' then  isnull(" + freeprefix
//                    + "2" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE3 + "= 'Y' then  isnull(" + freeprefix
//                    + "3" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE4 + "= 'Y' then  isnull(" + freeprefix
//                    + "4" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE5 + "= 'Y' then  isnull(" + freeprefix
//                    + "5" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE6 + "= 'Y' then  isnull(" + freeprefix
//                    + "6" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE7 + "= 'Y' then  isnull(" + freeprefix
//                    + "7" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE8 + "= 'Y' then  isnull(" + freeprefix
//                    + "8" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE9 + "= 'Y' then  isnull(" + freeprefix
//                    + "9" + ",'~') else '~' end " + " || ");
//            sql.append(" case when " + mtrlName + SaMaterialVO.BBASEINFOFREE10 + "= 'Y' then  isnull(" + freeprefix
//                    + "10" + ",'~') else '~' end ");
//        }
//        else {
//            sql.append(" '~~~~~~~~~~' ");
//        }

		return SaStructVFreeUtil.getMD5Func(sql.toString());
	}

	private final static String[] MATCH_KEYS = new String[] { SaDemandVO.CPROJECTID, SaDemandVO.CVENDORID,
			SaDemandVO.CPRODUCTORID, SaDemandVO.CCUSTOMERID, SaDemandVO.VFREE1, SaDemandVO.VFREE2, SaDemandVO.VFREE3,
			SaDemandVO.VFREE4, SaDemandVO.VFREE5, SaDemandVO.VFREE6, SaDemandVO.VFREE7, SaDemandVO.VFREE8,
			SaDemandVO.VFREE9, SaDemandVO.VFREE10 };

	/**
	 * 拷贝结构辅助属性从需求到计划订单
	 * 
	 * @param srcVO
	 * @param tarVO
	 * @param material
	 */
	// TODO unreal
	// public static void copyStuctMatchKeyValues(SaDemandVO srcVO, SumVO tarVO,
	// SaMaterialVO material) {
	// SaStructVFreeUtil.copyStuctMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO,
	// material);
	// }

	public static void copyStuctMatchKeyValues(SaDemandVO srcVO, SaMatchVO tarVO, SaMaterialVO material) {
		SaStructVFreeUtil.copyStuctMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO, material);
	}

	private static void copyStuctMatchKeyValues(ISuperVO srcVO, ISuperVO tarVO, SaMaterialVO material) {
		for (String key : SaStructVFreeUtil.MATCH_KEYS) {

			String value = (String) srcVO.getAttributeValue(key);
			if (value != null) {
				String flagKey = SaStructVFreeUtil.getStructFLagFieldName(key);
				if (((UFBoolean) material.getAttributeValue(flagKey)).booleanValue()) {
					tarVO.setAttributeValue(key, value);
				}
			}
		}
	}

	private static String getStructFLagFieldName(String key) {
		String flagKey = "";
		if (key.equals(SaDemandVO.CPROJECTID) || key.equals(SaDemandVO.CVENDORID) || key.equals(SaDemandVO.CPRODUCTORID)
				|| key.equals(SaDemandVO.CCUSTOMERID)) {
			flagKey = key;
		} else {
			flagKey = key.substring(1);
		}
		return "b" + flagKey;
	}

	/**
	 * 将多产出赋值到供应
	 * 
	 * @param srcVO
	 * @param tarVO
	 */
	public static void copyAllMatchKeyValues(BomOutputView srcVO, SaSupplyVO tarVO) {
		tarVO.setCcustomerid(srcVO.getCcustomerid());
		tarVO.setCproductorid(srcVO.getCproductorid());
		tarVO.setCprojectid(srcVO.getCprojectid());
		tarVO.setCvendorid(srcVO.getCvendorid());
		tarVO.setVfree1(srcVO.getVfree1());
		tarVO.setVfree2(srcVO.getVfree2());
		tarVO.setVfree3(srcVO.getVfree3());
		tarVO.setVfree4(srcVO.getVfree4());
		tarVO.setVfree5(srcVO.getVfree5());
		tarVO.setVfree6(srcVO.getVfree6());
		tarVO.setVfree7(srcVO.getVfree7());
		tarVO.setVfree8(srcVO.getVfree8());
		tarVO.setVfree9(srcVO.getVfree9());
		tarVO.setVfree10(srcVO.getVfree10());
	}

	/**
	 * 将多产出赋值到供应
	 * 
	 * @param srcVO
	 * @param tarVO
	 */
	public static void copyAllMatchKeyValues(BomItemView srcVO, SaDemandVO tarVO) {
		tarVO.setCcustomerid(srcVO.getCcustomerid());
		tarVO.setCproductorid(srcVO.getCproductorid());
		tarVO.setCprojectid(srcVO.getCprojectid());
		tarVO.setCvendorid(srcVO.getCvendorid());
		tarVO.setVfree1(srcVO.getVfree1());
		tarVO.setVfree2(srcVO.getVfree2());
		tarVO.setVfree3(srcVO.getVfree3());
		tarVO.setVfree4(srcVO.getVfree4());
		tarVO.setVfree5(srcVO.getVfree5());
		tarVO.setVfree6(srcVO.getVfree6());
		tarVO.setVfree7(srcVO.getVfree7());
		tarVO.setVfree8(srcVO.getVfree8());
		tarVO.setVfree9(srcVO.getVfree9());
		tarVO.setVfree10(srcVO.getVfree10());
	}

	/**
	 * 将需求的辅助属性拷贝到计划订单
	 * 
	 * @param srcVO
	 * @param tarVO
	 */
	public static void copyAllMatchKeyValues(SaDemandVO srcVO, PoVO tarVO) {
		SaStructVFreeUtil.copyAllMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO);
		// 计划订单客户字段名称和标准名称不同
		// TODO unreal
		// tarVO.setCcustomid(srcVO.getCcustomerid());
	}

	/**
	 * 将计划订单的辅助属性拷贝到供应
	 * 
	 * @param srcVO
	 * @param tarVO
	 */
	/**
	 * @param srcVO
	 * @param tarVO
	 */
	public static void copyAllMatchKeyValues(PoVO srcVO, SaSupplyVO tarVO) {
		SaStructVFreeUtil.copyAllMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO);
		// 计划订单客户字段名称和标准名称不同

		// TODO unreal
		// tarVO.setCcustomerid(srcVO.getcgetCcustomid());
	}

	/**
	 * 将计划订单的辅助属性拷贝到需求
	 * 
	 * @param srcVO
	 * @param tarVO
	 */
	// public static void copyAllMatchKeyValues(PoVO srcVO, SaDemandVO tarVO) {
	// SaStructVFreeUtil.copyAllMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO);
	// // 计划订单客户字段名称和标准名称不同
	// tarVO.setCcustomerid(srcVO.getCcustomid());
	// }

	// public static void copyAllMatchKeyValues(PoVO srcVO, LogItemVO tarVO) {
	// SaStructVFreeUtil.copyAllMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO);
	// // 计划订单客户字段名称和标准名称不同
	// tarVO.setCcustomerid(srcVO.getCcustomid());
	// }

	public static void copyAllMatchKeyValues(SaDemandVO srcVO, SaMatchVO tarVO) {
		SaStructVFreeUtil.copyAllMatchKeyValues((ISuperVO) srcVO, (ISuperVO) tarVO);
	}

	public static void copyAllMatchKeyValues(ISuperVO srcVO, ISuperVO tarVO) {
		for (String key : SaStructVFreeUtil.MATCH_KEYS) {
			String value = (String) srcVO.getAttributeValue(key);
			if (value != null) {
				tarVO.setAttributeValue(key, value);
			}
		}
	}

	public static void setGroupKey(SaSupplyVO SaSupplyVO, SaMaterialVO mtrlVO) {
		SaStructVFreeUtil.setGroupKey((ISuperVO) SaSupplyVO, mtrlVO);
	}

	public static void setGroupKey(SaDemandVO SaDemandVO, SaMaterialVO mtrlVO) {
		SaStructVFreeUtil.setGroupKey((ISuperVO) SaDemandVO, mtrlVO);
	}

	public static String getGroupKey(PoVO plo, SaMaterialVO mtrlVO) {
		return SaStructVFreeUtil.getGroupKeyInternal(plo, mtrlVO);
	}

	private static String getGroupKeyInternal(ISuperVO srcVO, SaMaterialVO mtrlVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(mtrlVO.getCmaterialid());
		for (String key : SaStructVFreeUtil.MATCH_KEYS) {
			String flagKey = SaStructVFreeUtil.getStructFLagFieldName(key);
			if (((UFBoolean) mtrlVO.getAttributeValue(flagKey)).booleanValue()) {
				if (MMValueCheck.isEmpty(srcVO.getAttributeValue(key))) {
					sb.append("~");
				} else {
					sb.append(srcVO.getAttributeValue(key));
				}
			} else {
				sb.append("~");
			}

		}
		return SaStructVFreeUtil.getMD5String(sb.toString());
	}

	/**
	 * @param srcVO
	 * @param mtrlVO
	 */
	private static void setGroupKey(ISuperVO srcVO, SaMaterialVO mtrlVO) {
		// TODO unreal
		// srcVO.setAttributeValue(SumVO.CGROUPKEY,
		// SaStructVFreeUtil.getGroupKeyInternal(srcVO, mtrlVO));
	}

	public static void clearAllFree(SaDemandVO demand) {
		for (String key : SaStructVFreeUtil.MATCH_KEYS) {
			demand.setAttributeValue(key, "~");
		}
	}

	public static String getGroupKeyWithNoFree(String mtrlIDStr) {
		return SaStructVFreeUtil.getMD5String(mtrlIDStr + "~~~~~~~~~~~~~~");
	}

	public static void clearNotStructFree(SaDemandVO demand, SaMaterialVO mtrlVO) {
		for (String key : SaStructVFreeUtil.MATCH_KEYS) {
			String flagKey = SaStructVFreeUtil.getStructFLagFieldName(key);
			if (!((UFBoolean) mtrlVO.getAttributeValue(flagKey)).booleanValue()) {
				demand.setAttributeValue(key, "~");
			}
		}
	}

	/**
	 * 数据库MD5函数
	 * 
	 * @param sql
	 * @return
	 */
	public static String getMD5Func(String sql) {
		DBTool dbtool = new DBTool();
		int dbtype = dbtool.getDBType();
		if (dbtype == DBConsts.ORACLE) {
			return "fn_md5(" + sql + ")";
		} else if (dbtype == DBConsts.SQLSERVER) {
			return "dbo.fn_md5(" + sql + ")";
		}
		return sql;
	}

	/**
	 * Java MD5函数
	 * 
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] b = s.getBytes();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(b);
			byte[] result = digest.digest();
			int j = result.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = result[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toLowerCase();
		} catch (Exception ex) {
			return null;
		}
	}

}

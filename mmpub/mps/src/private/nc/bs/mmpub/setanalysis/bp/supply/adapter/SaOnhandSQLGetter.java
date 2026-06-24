package nc.bs.mmpub.setanalysis.bp.supply.adapter;

import nc.bs.framework.common.NCLocator;
import nc.bs.mmpub.setanalysis.bp.supply.SaSupplyUtil;
import nc.bs.mmpub.setanalysis.bp.utils.SaStructVFreeUtil;
import nc.impl.pubapp.pattern.database.IDQueryBuilder;
import nc.pubitf.ic.onhand.mm.seta.IQryOnhandMapForSeta;
import nc.pubitf.ic.onhand.mm.seta.OnhandSetaSupplyMapVO;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmpub.dpub.db.SqlBuilder;
import nc.vo.mmpub.setanalysis.entity.SaContext;
import nc.vo.mmpub.setanalysis.entity.SaMaterialVO;
import nc.vo.mmpub.setanalysis.utils.SaLangConst;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class SaOnhandSQLGetter {
	/**
	 * @param context
	 * @param type
	 * @param billType
	 * @param materialTempTable
	 * @param materialids
	 * @param firstid
	 * @param isotherfetch
	 * @return modify by malid 增加按物料辅助属性分组
	 */
	public static String getSupplySql(SaContext context, Integer type, String billType, String materialTempTable,
			String[] materialids, String firstid, UFBoolean isotherfetch) {
		IQryOnhandMapForSeta service = NCLocator.getInstance().lookup(IQryOnhandMapForSeta.class);
		OnhandSetaSupplyMapVO supplyVO = null;
		supplyVO = service.getSetaSupplyMapVO();
		// String startDate = context.getStartDate().toString();
		String[] pkOrgs = context.getOrgs().toArray(new String[0]);
		String materialAliase = "m";
		SqlBuilder sql = new SqlBuilder();
		// 取得插入表结构
		sql.append(SaSupplyUtil.getOHSupplyInsertSql());
		sql.select();
		String pk_bill = supplyVO.getSupplyid();
		String pk_bill_b = supplyVO.getSupplybid();
		// 主键Key
		String pk_key = "'" + context.getComputeCode() + "' || " + (pk_bill_b == null ? pk_bill : pk_bill_b);
		if (MMValueCheck.isEmpty(pk_bill)) {
			/*
			 * @res "主键为空！billType="
			 */
			ExceptionUtils.wrappBusinessException(SaLangConst.getKeyIsNull() + billType);
		}
		// 1 主键
		sql.append("max(" + pk_key + "),");
		// 2 物料范围主键
		sql.append("max(" + materialAliase + "." + SaMaterialVO.PK_BR + "),");
		// 3 集团
		sql.append("'" + context.getPk_group() + "',");
		// 4 组织
		sql.append("'" + context.getPk_org() + "',");

		// 5 组织版本
		if (MMValueCheck.isEmpty(context.getPk_org_v())) {
			sql.append("'" + context.getPk_org() + "',");
		} else {
			sql.append("'" + context.getPk_org_v() + "',");
		}

		// 6 供给类型
		sql.append(type + ",");
		// 7 供给表头主键
		sql.append("max(" + supplyVO.getSupplyid() + "),");
		// 8 供给表体主键
		sql.append("max(" + supplyVO.getSupplybid() + "),");
		// 9 供给单据行号
		sql.append("null,");
		// 10 供给工厂
		sql.append("isnull(ic_onhanddim.pk_org,'" + context.getPk_org() + "'),");

		// 11 供给工厂版本
		sql.append("max(case when isnull(ic_onhanddim.pk_org_v,'~')='~' then  '" + context.getPk_org_v()
				+ "' else ic_onhanddim.pk_org_v end),");
		// 12 物料主键
		sql.append("max(" + supplyVO.getMaterialid() + "),");
		// 13 物料版本主键
		sql.append("max(" + supplyVO.getMaterialvid() + "),");
		// 14 供给单据类型
		sql.append("'" + billType + "',");
		// 15 供给单据号
		sql.append("null,");
		// 16 供给日期
		// 是现存量的话则取运算日志时间
		// change by liweiz 供给日期去最小范围的供给日期
		// sql.append("substring('" + startDate + "',1,10) || ' 00:00:00',");
		sql.append("'" + context.getStartDate().toString() + "',");
		// 1601 供给日期
		sql.append("'" + context.getStartDate().toString() + "',");
		// 未执行量
		String unimplementNum = supplyVO.getNnum() + " - isnull(" + supplyVO.getReservationnum() + ",0)";
		// 17 供给数量
		sql.append("sum(" + unimplementNum + "),");
		// 18 预留数量
		sql.append("sum(" + supplyVO.getReservationnum() + "),");

		// 未分配量默认等于未执行量1801
		sql.append("sum(" + unimplementNum + "),");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		sql.append("'~',");
		// 19 供应商
//        sql.append(supplyVO.getVendorid() + ",");
//        // 20 生产厂商
//        sql.append(supplyVO.getProductorid() + ",");
//        // 21 项目
//        sql.append(supplyVO.getProjectid() + ",");
//        // 22 客户
//        sql.append(supplyVO.getCustomerid() + ",");
//        // 2201 特征码
//        sql.append(supplyVO.getFfileid() + ",");
//        // 23 自由项1
//        sql.append(supplyVO.getFree1() + ",");
//        sql.append(supplyVO.getFree2() + ",");
//        sql.append(supplyVO.getFree3() + ",");
//        sql.append(supplyVO.getFree4() + ",");
//        sql.append(supplyVO.getFree5() + ",");
//        sql.append(supplyVO.getFree6() + ",");
//        sql.append(supplyVO.getFree7() + ",");
//        sql.append(supplyVO.getFree8() + ",");
//        sql.append(supplyVO.getFree9() + ",");
//        sql.append(supplyVO.getFree10() + ",");
		// 24 仓库
		sql.append(supplyVO.getStordocid() + ",");
		// 25 分组key
		sql.append("max(" + SaStructVFreeUtil.getGroupKeySQL(supplyVO, materialAliase) + "),");
		// 26 物料低阶码
		sql.append("max(" + materialAliase + "." + SaMaterialVO.LOWLEVELCODE + "),");
		// 27 DR
		sql.append("0,");
		// 28 创建人
		sql.append("'" + context.getUserId() + "',");
		// 29 开始日期
		sql.append("'" + context.getStartDate().toString() + "',");
		// 30 分析号
		sql.append("'" + context.getComputeCode() + "',");
		// 32 未执行量
		sql.append("sum(" + unimplementNum + "),");
		// 3201 执行量 V65设置为空 caijingc 20140310
		sql.append("null,");
		// 33 单据数量
		sql.append("sum(" + supplyVO.getNnum() + "),");
		// 34 订单状态
		sql.append("-1" + ",");
		// 35 主单位
		sql.append("max(" + materialAliase + "." + SaMaterialVO.CMEASDOC + "),");
		// 351 废品系数
		sql.append("max(" + materialAliase + "." + SaMaterialVO.WASTERRATE + "),");
		// 36 源头单据子表ID
		sql.append("'',");
		// 37 源头单据号
		sql.append("'',");
		// 38 源头单据ID
		sql.append("'',");
		// 39 源头单据行号
		sql.append("'',");
		// 40 源头单据类型
		sql.append("'',");
		// 41来源单据子表ID
		sql.append("'',");
		// 42来源单据号
		sql.append("'',");
		// 43来源单据ID
		sql.append("'',");
		// 44来源单据行号
		sql.append("'',");
		// 45来源单据类型
		  sql.append("'',");
        //liyf 202312-18 库存状态,首次入库日期和失效日期
        sql.append(" ic_storestate.vname   as vdef1,");//库存状态
        sql.append("  scm_batchcode.dinbounddate  as vdef2,");//首次入库日期,后续用来比较先入先出
        sql.append("  max(scm_batchcode.dvalidate)  as vdef3");//失效日期5
        
        //liyf 202312-18 库存状态,首次入库日期和失效日期
		SqlBuilder fromSql = new SqlBuilder();
		fromSql.from();
		fromSql.append(supplyVO.getFrom());
		// 物料条件
		fromSql.innerjoin();
		fromSql.append(materialTempTable + " " + materialAliase);
		fromSql.on();
		fromSql.append(materialAliase + "." + SaMaterialVO.CMATERIALID + "=" + supplyVO.getMaterialid());
		
		   //liyf 2023-12-14 增加关联条件,
        fromSql.append(" inner  join  scm_batchcode on ic_onhanddim.pk_batchcode= scm_batchcode.pk_batchcode ");
        fromSql.append(" inner  join  bd_materialplan on ic_onhanddim.cmaterialoid= bd_materialplan.pk_material and bd_materialplan.pk_org = ic_onhanddim.pk_org ");
        //liyf 2023-12-14 增加关联条件,
		fromSql.where();

		if (MMValueCheck.isNotEmpty(supplyVO.getWhere())) {
			fromSql.append(supplyVO.getWhere());
			fromSql.and();
		}
		// 供应组织条件
		IDQueryBuilder builder = new IDQueryBuilder();
		// fromSql.append(builder.buildSQL(supplyVO.getSupplyorgid(), pkOrgs));
		fromSql.append(builder.buildSQL("ic_onhanddim.pk_org", pkOrgs));
		// mapVO.setSupplyOrgid("ic_onhanddim.pk_org");
		// mapVO.setSupplyOrgvid("ic_onhanddim.pk_org_v");
		// 未执行量大于零
		fromSql.and();
		fromSql.append(unimplementNum + ">0 ");
		// 仓库非停用，TODO:修改接口
		fromSql.and();
		fromSql.append("BD_STORDOC.ENABLESTATE <>3");
		fromSql.and();
		fromSql.append(materialAliase + "." + SaMaterialVO.COMPUTECODE + "='" + context.getComputeCode() + "'");
     
    //liyf 如果考虑质检周期 则需要将待检的加进来
//      
      String vdef1 = (String)context.getOption().getVdef1();
		if("Y".equalsIgnoreCase(vdef1)){
		      fromSql.append("and (m.bbaseinfostorestate= 'Y' and ic_storestate.iusability='1' or ( m.bbaseinfostorestate= 'Y' and ic_storestate.vname like '%待检%' )   or nvl(m.bbaseinfostorestate ,'N')='N'  )");//
		}else{
			  SaOnhandWhereService onhand = new SaOnhandWhereService(context);
		      fromSql.append(onhand.getOnhandWhere(materialAliase, supplyVO, context.getOption().getStock(), isotherfetch));// 奥得亮项目补丁
		}
	    //liyf 如果考虑质检周期 则需要将待检的加进来

         // 为解决sql兼容问题，将group by语句调整到having之前 NCdp204846797
		if (MMValueCheck.isNotEmpty(materialids)) {
			// 取得其他供给SQL
			fromSql.and();
			fromSql.append(supplyVO.getMaterialid(), materialids);
			fromSql.append(SaOnhandSQLGetter.getGroupBySql(supplyVO));
			fromSql.append(SaSupplyUtil.getOtherWhereSqlForOnHand(pk_key, context.getComputeCode()));
		} else {
			fromSql.append(SaOnhandSQLGetter.getGroupBySql(supplyVO));
		}
		sql.append(fromSql.toString());
		return sql.toString();
	}

	// add by malid 增加按物料辅助属性分组NCdp204638642,加上按仓库分组
	private static String getGroupBySql(OnhandSetaSupplyMapVO supplyVO) {
		return new StringBuilder(" group by ").append(supplyVO.getSupplyorgid() + ",")
				.append(supplyVO.getMaterialid() + ",")
				//liyf 20230105 增加库存状态的分组
                .append(" ic_storestate.vname ,  scm_batchcode.dinbounddate,")
                 //liyf 20230105 增加库存状态的分组
				.append("'~',").append("'~',").append("'~',").append("'~',")
				.append("'~',")
				// .append(supplyVO.getProjectid() + ",")
				// .append(supplyVO.getProductorid() + ",")
				// .append(supplyVO.getVendorid() + ",")
				// .append(supplyVO.getCustomerid() + ",")
				// .append(supplyVO.getFfileid() + ",")
				.append(supplyVO.getStordocid() + ",").append("'~',").append("'~',").append("'~',").append("'~',")
				.append("'~',").append("'~',").append("'~',").append("'~',").append("'~',").append("'~'")
				// .append(supplyVO.getFree1() + ",")
				// .append(supplyVO.getFree2() + ",")
				// .append(supplyVO.getFree3() + ",")
				// .append(supplyVO.getFree4() + ",")
				// .append(supplyVO.getFree5() + ",")
				// .append(supplyVO.getFree6() + ",")
				// .append(supplyVO.getFree7() + ",")
				// .append(supplyVO.getFree8() + ",")
				// .append(supplyVO.getFree9() + ",")
				// .append(supplyVO.getFree10())
				.append(" ").toString();

	}
}

/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.mmpps.calc.bp.planorder;
import java.util.Map;

import nc.bs.mmpps.calc.bp.utils.CalcHslParseUtil;
import nc.bs.mmpps.calc.bp.utils.EoqUtil;
import nc.bs.mmpps.calc.bp.utils.StructVFreeUtil;
import nc.bs.pubapp.AppBsContext;
import nc.bsutil.mmpub.pub.NaturalCalendarUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.util.mmf.busi.consts.BillTypeConst;
import nc.util.mmf.busi.measure.MeasureHelper;
import nc.util.mmf.busi.measure.MeasureParam;
import nc.util.mmf.framework.base.MMStringUtil;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.MrpConstant;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.mmpps.calc.enumeration.DemandType;
import nc.vo.mmpps.calc.enumeration.PlanPurpose;
import nc.vo.mmpps.calc.enumeration.SchemaType;
import nc.vo.mmpps.mps0202.BillstatusEnum;
import nc.vo.mmpps.mps0202.PoSourceEnum;
import nc.vo.mmpps.mps0202.PoTypeEnum;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.mmpps.plo.util.PloPubilcUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * 计划订单构建类
 * 
 * @since 6.1
 * @version 2012-7-31 下午07:43:26
 * @author chenjij
 */
public final class PloConstruct {

	/**
	 * 创建计划订单
	 * 
	 * @param context
	 * @param demand
	 * @param material
	 * @param depVidMap
	 * @return
	 */
	public static PoVO createPlo(CalculateContext context, DemandVO demand,
			MaterialVO material, Map<String, String> orgVersionMap) {

		PoVO povo = null;
		try {
			povo = PloConstruct.createPlo(context, demand, material);
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}

		if (povo != null) {
			// 设置生产工厂
			PloConstruct.setProdFactory(context, povo, material, demand,
					orgVersionMap);
		}

		return povo;
	}

	private static void setBillInfo(DemandVO demand, MaterialVO material,
			PoVO po) {
		// 需求来源
		po.setFdemandbill(demand.getFdemandtype());
		// 源头信息
		po.setVfirsttype(demand.getVfirstdemandtype());
		po.setVfirstid(demand.getVfirstdemandid());
		po.setVfirstcode(demand.getVfirstdemandcode());
		po.setVfirstbid(demand.getVfirstdemandbid());
		po.setVfirstrowno(demand.getVfirstdemandrowno());

		// 需求合并的
		if (demand.getFdemandtype() == null) {
			po.setVsrctype(demand.getVdemandtypecode());
			po.setVsrcid(demand.getCdemandbillid());
			po.setVsrccode(demand.getVdemandcode());
			po.setVsrcbid(demand.getCdemandbillbid());
			po.setVsrcrowno(demand.getVdemandrowno());

			return;
		}

		// 来源里面加源头单据 不是需求合并 是不是需求接口得加源头单据 13 来源 源头单据信息
		// dengsw 计划独立需求不记录来源单据信息 二维表无法联查
		if (demand.getFdemandtype().intValue() != DemandType.SAFETY_STOCK
				.toInt()
				&& demand.getFdemandtype().intValue() != DemandType.PLANNED_INDEPENDENT_DEMAND
						.toInt()) {
			if (demand.getBrelated() != null
					&& demand.getBrelated().booleanValue()
					&& demand.getFdemandtype() != DemandType.PICK_MATERIAL
							.toInt()) {
				// 来源信息
				po.setVsrctype(demand.getVsrctype());
				po.setVsrcid(demand.getVsrcid());
				po.setVsrccode(demand.getVsrccode());
				po.setVsrcbid(demand.getVsrcbid());
				po.setVsrcrowno(demand.getVsrcrowno());

			} else {
				// 来源信息
				po.setVsrctype(demand.getVdemandtypecode());
				po.setVsrcid(demand.getCdemandbillid());
				po.setVsrccode(demand.getVdemandcode());
				po.setVsrcbid(demand.getCdemandbillbid());
				po.setVsrcrowno(demand.getVdemandrowno());
			}

		}
		//2023-11-07 liyf 整合补丁增加vdef5 订单类型
		VOQuery<PoVO>  qry = new VOQuery<>(PoVO.class);
		PoVO[] srcvo = qry.query(new String[]{po.getVsrcid()});
		if(srcvo!=null && srcvo.length >0){
			po.setVdef5(srcvo[0].getVdef5());
		}
	;

	}

	/**
	 * 创建计划订单
	 * 
	 * @param context
	 * @param demand
	 * @param material
	 * @return
	 * @throws BusinessException
	 */
	private static PoVO createPlo(CalculateContext context, DemandVO demand,
			MaterialVO material) throws BusinessException {
		SchemaVO schema = context.getSchema().getParentVO();

		// Added by Goodie
		// 取组织最新版本
		String pk_org_v = context.getPk_org_v();

		PoVO po = new PoVO();
		// 1 计划订单的类型
		if (SchemaType.MPS.toInt() == schema.getFpstype().intValue()) {
			po.setFpotype(PoTypeEnum.MPS);
		} else {
			// Modified by Goodie
			// 长周期备料时, 应区分MPS件与MRP件
			if (material.getPlanprop().equals(context.getPlanProp_MRP())) {
				po.setFpotype(PoTypeEnum.MRP);
			} else if (material.getPlanprop().equals(context.getPlanProp_MPS())) {
				po.setFpotype(PoTypeEnum.MPS);
			}
		}
		po.setPk_group(schema.getPk_group());
		// 2 计划组织
		po.setPk_org(schema.getPk_org());

		// 如果计划组织为工厂，则将物料计划页签的生产部门带到计划订单
		if (context.getOrgIsFactory()) {
			po.setCproddeptid(material.getPk_prodeptdoc());
		}

		// Modified by Goodie
		po.setPk_org_v(pk_org_v);

		// 3 方案
		po.setCpsid(schema.getCpsid());
		// 4 计划目的
		po.setFplanpurpose(schema.getFplanpurpose());
		// 5 是否为长周期备料
		// po.setIscycle(new UFBoolean(
		// schema.getFplanpurpose().intValue() == PlanPurpose.STOCK
		// .toInt()));
		// 6 运算号
		po.setComputecode(context.getCalculateCode());
		// 7 料品
		po.setCmaterialid(material.getCmaterialid());

		// 如果是替代需求，则不能继续替代
		if (demand.getIssubs() != null && demand.getIssubs().booleanValue()) {
			demand.setCansubs(UFBoolean.FALSE);
		} else {
			// 如果不是需求则直接取Demand上的cansubs来源BOM
			if (demand.getCansubs() == null) {
				demand.setCansubs(UFBoolean.FALSE);
				// demand.setCansubs(UFBoolean.TRUE);
			}
		}

		// 可替代
		po.setBreplaceflag(demand.getCansubs());

		// 如果需求合并，则取最新版本物料
		if (material.getCombineflag() != null
				&& material.getCombineflag().booleanValue()) {
			po.setCmaterialvid(material.getCmaterialvid());
			// 需求合并 但是需求库存组织是相同的也赋值
			if (MMValueCheck.isNotEmpty(demand.getPk_demandorg())) {
				po.setCfactoryid(demand.getPk_demandorg());
				po.setCfactoryvid(demand.getPk_demandorg_v());
			} else {
				// 10 库存组织如果计划组织的库存组织 库存组织才赋值否则不赋值
				if (context.getOrgIsStock()) {
					po.setCfactoryid(schema.getPk_org());

					// Modified by Goodie
					po.setCfactoryvid(pk_org_v);
				}
			}

			// 需求合并的可替代为N
			po.setBreplaceflag(UFBoolean.FALSE);

		} else {
			po.setCmaterialvid(demand.getCmaterialvid());
			// 如果需求不合并，则计划订单的需求库存组织=需求原始单据的需求库存组织
			po.setCfactoryid(demand.getPk_demandorg());
			po.setCfactoryvid(demand.getPk_demandorg_v());

			// 如果需求不合并，且需求类型是销售订单，则将销售订单号赋给计划订单
			if (demand.getFdemandtype().intValue() == DemandType.SALES_ORDER
					.toInt()) {
				po.setVsalebillcode(demand.getVdemandcode());
			}

		}

		if (MMValueCheck.isNotEmpty(context.getEmployId())) {
			po.setCemployeeid(context.getEmployId());
		}
		if (MMValueCheck.isNotEmpty(context.getDepId())) {
			po.setCplandeptid(context.getDepId());
			po.setCplandeptvid(context.getDepVId());
		}
		// 9 单据状态
		po.setFbillstatus(BillstatusEnum.PLAN);
		po.setFposource(PoSourceEnum.COMPUT);

		// 12 数量（MPS汇总表的计划产出量）
		po.setNnumber(demand.getNremainnum());
		// 主单位
		po.setCmeasureid(material.getCmeasdoc());
		// 单位
		po.setCassmeasureid(material.getCprodmeasdoc());
		// 废品率
		po.setWasterrate(material.getWasterrate());
		// 替代类型
		po.setFsubstype(demand.getFsubstype());
		// 物料类型
		po.setFmotype(material.getMartype());
		PloPubilcUtil.setScale(po, PoVO.NNUMBER, null);
		if (po.getNnumber().doubleValue() <= 0) {
			return null;
		}

		/********************************************** 计算计划投入量、计划产出量 *********************************/
		// 计划投入数量：废品系数。
		PloConstruct.updateInputNum(material, po);
		// 计划投入数量进行：调整经济批量。
		PloConstruct.updateEOQ(material, po);
		// 重新根据废品系数计算计划产出数量
		PloConstruct.updateOnputNum(material, po);
		// 设置主辅计量单位及换算率
		PloConstruct.fillMeasureAndAssistNumberByMtrl(material, po);

		/********************************************** 设置相关日期 ***********************************************/
		// 13 需求日期
		po.setDdemandtime(demand.getDemandtime().asLocalEnd());
		po.setDplansupplytime(demand.getDmatchdate().asLocalEnd());
		// 14 处理订单的相关日期
		PloConstruct.setOrderDates(po, material, context);
		// 15 结构辅助属性 4个固定辅助属性没加 需求生成计划订单的时候非结构辅助属性是否赋值
		// 直接赋值就行前面需求合并的时候已经考虑了
		StructVFreeUtil.copyAllMatchKeyValues(demand, po);

		PloConstruct.setBillInfo(demand, material, po);

		// 设置计划订单状态
		OrderStatus orderStatus = new OrderStatus(context, material);
		po = orderStatus.setOrderStatus(po);
		// 12 计量单位信息 保存前统一处理
		// 计划策略
		po.setFplanstrategy(material.getPk_pst());
		// 虚项计划订单 完全配套生产
		if (material.getIsvirtual() != null
				&& material.getIsvirtual().booleanValue()) {
			po.setBcreatemobill(UFBoolean.TRUE); // Modified by Goodie
			po.setIsvirtual(UFBoolean.TRUE);
		} else if (material.getIscreatesonprodorder() != null
				&& material.getIscreatesonprodorder().booleanValue()) {
			// 父项计划订单（55B4）、父项主生产计划（55B5）、父项生产订单（55A2）、备料计划（55A3）、离散生产订单（55C2）
			/**
			 * mps运算创建的计划订单，母子订单的物料应按普通物料一样处理，不敏感母子订单标记，目前给出了提示 &&
			 * material.getPlanprop().equals("MR") MPS件 MRP处理相同
			 */
			if (MMValueCheck.isNotEmpty(po.getVsrctype())
					&& (po.getVsrctype().equals(BillTypeConst.PLO)
							|| po.getVsrctype().equals(BillTypeConst.MPS)
							|| po.getVsrctype().equals(BillTypeConst.DMO)
							|| po.getVsrctype().equals(BillTypeConst.PMO) || po
							.getVsrctype().equals(BillTypeConst.PICKM))) {
				po.setBcreatemobill(UFBoolean.TRUE);
			}

		}

		// 5 是否为长周期备料
		if (schema.getFplanpurpose().intValue() == PlanPurpose.STOCK.toInt()
				&& schema.getFpstype().intValue() == SchemaType.MRP.toInt()) {
			if (material.getLongcycleprep().booleanValue()) {
				po.setIscycle(UFBoolean.TRUE);
			}
		}
		// 制单人 制单日期
		String userID = context.getUserId();
		// po.setBillmaker(AuditInfoUtil.getCurrentUser());
		po.setBillmaker(userID);
		po.setDmakedate(AppBsContext.getInstance().getBusiDate());
		// 创建人
		po.setCreator(context.getCacheValue(MrpConstant.AUDIT_CREATOR)
				.toString());
		// 创建时间
		po.setCreationtime((UFDateTime) context
				.getCacheValue(MrpConstant.AUDIT_CREATIONTIME));

		// 父项相关
		po.setCpmaterialid(demand.getCpmaterialid());
		po.setCpmaterialvid(demand.getCpmaterialvid());
		po.setCpvendorid(demand.getCpvendorid());
		po.setCpproductorid(demand.getCpproductorid());
		po.setCpprojectid(demand.getCpprojectid());
		po.setCpcustomid(demand.getCpcustomerid());
		po.setVpfree1(demand.getVpfree1());
		po.setVpfree2(demand.getVpfree2());
		po.setVpfree3(demand.getVpfree3());
		po.setVpfree4(demand.getVpfree4());
		po.setVpfree5(demand.getVpfree5());
		po.setVpfree6(demand.getVpfree6());
		po.setVpfree7(demand.getVpfree7());
		po.setVpfree8(demand.getVpfree8());
		po.setVpfree9(demand.getVpfree9());
		po.setVpfree10(demand.getVpfree10());

		// V65:增加产品主辅单位
		po.setCpunitid(demand.getCpunitid());
		po.setCpastunitid(demand.getCpastunitid());

		if (MMStringUtil.isNotEmpty(demand.getCpbomid())) {
			po.setCpbomid(demand.getCpbomid());
		} else if (MMStringUtil.isNotEmpty(demand.getCppackbomid())) {
			po.setCpbomid(demand.getCppackbomid());
		}
		po.setCpbomitemid(demand.getCpbomitemid());

		// 客户物料码
		po.setCcustmaterialid(demand.getCcustmaterialid());
		// 生产BOM
		po.setCbomid(demand.getCprodbomid());
		// 包装BOM
		po.setCpackbomid(demand.getCpackbomid());
		// 特征码
		po.setCffileid(demand.getCffileid());
		// 产品特征码
		po.setCpffileid(demand.getCpffileid());
		return po;

	}

	/**
	 * 设置生产工厂
	 * 
	 * @param context
	 * @param po
	 * @param material
	 */
	private static void setProdFactory(CalculateContext context, PoVO po,
			MaterialVO material, DemandVO demand,
			Map<String, String> orgVersionMap) {
		String pk_org = null, pk_org_v = null;
		// 如果计划组织是工厂，则生产工厂为计划组织
		if (context.getOrgIsFactory()) {
			pk_org = context.getPk_org();
			pk_org_v = context.getPk_org_v();
		}
		// 否则，生产工厂为物料计划页签定义的生产工厂
		else if (demand.getFdemandtype() != null
				&& demand.getFdemandtype().intValue() == DemandType.PLANNED_INDEPENDENT_DEMAND
						.toInt()
				&& MMValueCheck.isNotEmpty(demand.getPk_demandorg())) {
			pk_org = demand.getPk_demandorg();
			pk_org_v = demand.getPk_demandorg_v();

		} else {
			// 设置生产工厂VID
			if (orgVersionMap != null) {
				if (MMValueCheck.isNotEmpty(material.getPk_prodorg())
						&& orgVersionMap.containsKey(material.getPk_prodorg())) {
					pk_org = material.getPk_prodorg();
					pk_org_v = orgVersionMap.get(material.getPk_prodorg());
				}
			}
		}

		if (pk_org != null && pk_org_v != null) {
			po.setCstockorgid(pk_org);
			po.setCstockorgvid(pk_org_v);
		}

	}

	/**
	 * 设置订单的相关日期
	 * 
	 * @param po
	 *            计划订单的VO对象
	 */
	private static void setOrderDates(PoVO po, MaterialVO material,
			CalculateContext calcContext) {
		// 收料日期
		PloConstruct.setDrewindTime(po, material, calcContext);
		// 下单日期
		PloConstruct.setBillTime(po, material, calcContext);
		// 计划开工日期
		PloConstruct.setPlanStartTime(po, material, calcContext);
		// 记录异常信息
		// PloConstruct.recordExceptionInformation(po, material, calcContext);
	}

	/**
	 * 确定计划订单收料日期：<br>
	 * 需求日期 － 物料档案.后段提前期
	 * 
	 * @param po
	 *            计划订单
	 */
	private static void setDrewindTime(PoVO po, MaterialVO material,
			CalculateContext calcContext) {
		UFDouble endahead = material.getEndahead();
		endahead = endahead == null ? UFDouble.ZERO_DBL : endahead;
		if (endahead.compareTo(UFDouble.ZERO_DBL) == 0) {
			po.setDrewindtime(po.getDplansupplytime().asLocalEnd());
			if (isPR(material)) {
				po.setDrewindtime(po.getDdemandtime().asLocalEnd());
			}
		} else {
			UFDate drewindtime;
			if (isPR(material)) {
				drewindtime = NaturalCalendarUtil.findDateBefore(
						po.getDdemandtime(), endahead.intValue());
			} else {
				drewindtime = calcContext.getWorkDateCache().getPreWorkDates(
						po.getDplansupplytime(), endahead.intValue());
			}
			po.setDrewindtime(drewindtime.asLocalEnd());
		}
	}

	/**
	 * 确定计划订单下单日期：<br>
	 * 收料日期 － (固定提前期+if(计划数量>提前期批量,(向上取整(计划数量/提前期批量-1)*提前期系数),0)) 收料日期 －
	 * 物料档案.固定提前期
	 * 
	 * @param po
	 *            计划订单
	 */
	private static void setBillTime(PoVO po, MaterialVO material,
			CalculateContext calcContext) {
		// Modified by Goodie
		// 公式调整:
		// 收料日期 － (固定提前期+if(计划数量>提前期批量,(向上取整 ((计划数量/提前期批量-1)*提前期系数)),0))

		// 提前期批量导致的调整
		UFDouble adjust = UFDouble.ZERO_DBL;
		if (material.getAheadbatch() != null
				&& material.getAheadbatch().compareTo(UFDouble.ZERO_DBL) > 0
				&& po.getNaccponum().compareTo(material.getAheadbatch()) > 0) {
			UFDouble aheadcoff = material.getAheadcoff();
			aheadcoff = aheadcoff == null ? UFDouble.ZERO_DBL : aheadcoff;

			// 计划数量/提前期批量-1
			adjust = po.getNaccponum().div(material.getAheadbatch())
					.sub(UFDouble.ONE_DBL);
			// *提前期系数
			adjust = adjust.multiply(aheadcoff);

			adjust = adjust.setScale(0, UFDouble.ROUND_CEILING);
		}

		// 固定提前期
		UFDouble fixedahead = material.getFixedahead();
		fixedahead = fixedahead == null ? UFDouble.ZERO_DBL : fixedahead;
		fixedahead = fixedahead.add(adjust);

		if (fixedahead.doubleValue() == 0) {
			po.setDbilltime(po.getDrewindtime().asLocalBegin());
		} else {
			UFDate dbilltime;
			if (isPR(material)) {
				dbilltime = NaturalCalendarUtil.findDateBefore(
						po.getDrewindtime(), fixedahead.intValue());
			} else {
				dbilltime = calcContext.getWorkDateCache().getPreWorkDates(
						po.getDrewindtime(), fixedahead.intValue());
			}
			po.setDbilltime(dbilltime.asLocalBegin());
		}
	}

	/**
	 * 确定计划订单计划开工日期：<br>
	 * 下单日期－物料档案.前段提前期
	 * 
	 * @param po
	 *            计划订单
	 */
	private static void setPlanStartTime(PoVO po, MaterialVO material,
			CalculateContext calcContext) {
		UFDouble prevahead = material.getPrevahead();
		prevahead = prevahead == null ? UFDouble.ZERO_DBL : prevahead;
		if (prevahead.doubleValue() == 0) {
			po.setDplanstarttime(po.getDbilltime().asLocalBegin());
		} else {
			UFDate dplanstarttime;
			if (isPR(material)) {
				dplanstarttime = NaturalCalendarUtil.findDateBefore(
						po.getDbilltime(), prevahead.intValue());
			} else {
				dplanstarttime = calcContext.getWorkDateCache()
						.getPreWorkDates(po.getDbilltime(),
								prevahead.intValue());
			}
			po.setDplanstarttime(dplanstarttime.asLocalBegin());
		}
	}

	private static void fillMeasureAndAssistNumberByMtrl(MaterialVO materialVO,
			PoVO ploVO) {
		// 设置主辅计量单位和换算率
		ploVO.setCmeasureid(materialVO.getCmeasdoc());
		// 没有默认生产单位
		if (MMValueCheck.isEmpty(materialVO.getCprodmeasdoc())) {
			ploVO.setCassmeasureid(materialVO.getCmeasdoc());
			ploVO.setVchangerate("1/1");
		} else {
			ploVO.setCassmeasureid(materialVO.getCprodmeasdoc());
			ploVO.setVchangerate(materialVO.getMeasrate());
		}
		if (ploVO.getVchangerate().equals("1/1")) {
			// 单位相同 不需要做精度处理
		} else {
			if (ploVO.getVchangerate() != null) {
				String changeRate = new ScaleUtils(materialVO.getPk_group())
						.adjustHslScale(ploVO.getVchangerate());
				changeRate = CalcHslParseUtil.getDefaultHsl(changeRate);
				ploVO.setVchangerate(changeRate);
			}
		}

		// 如果单位与主单位相同，则数量等于主数量
		if (MMStringUtil.isEqual(ploVO.getCassmeasureid(),
				ploVO.getCmeasureid())) {
			ploVO.setNastnum(ploVO.getNnumber());
			ploVO.setNassaccponum(ploVO.getNaccponum());
			return;
		}

		PoVO[] povos = new PoVO[] { ploVO };
		MeasureParam para = new MeasureParam();
		para.setCmaterialvid(PoVO.CMATERIALVID);
		para.setCmeasureid(PoVO.CMEASUREID);
		para.setCassMeasures(PoVO.CASSMEASUREID);
		para.setVchangeRate(PoVO.VCHANGERATE);
		// 计划产出数量
		MeasureHelper.fillAssNumber(povos, para, PoVO.NNUMBER, PoVO.NASTNUM);
		// 计划投入数量
		MeasureHelper.fillAssNumber(povos, para, PoVO.NACCPONUM,
				PoVO.NASSACCPONUM);
		// 精度过滤

	}

	private static void updateEOQ(MaterialVO material, PoVO plo) {
		// 计划投入数量
		UFDouble nnet = plo.getNaccponum();
		UFDouble nplannedOrderReceipts = null;
		// 如果净需求<=0，则计划产出量为0
		if (nnet.compareTo(UFDouble.ZERO_DBL) == 0) {
			nplannedOrderReceipts = UFDouble.ZERO_DBL;
		} else {
			String batchRule = material.getBatchrule();
			// 1、批量规则==“直接批量”，则：计划产出量=净需求
			if (nc.vo.bd.material.IMaterialEnumConst.BATCHRULE_DIRECT
					.equals(batchRule)) {
				nplannedOrderReceipts = nnet;
			}
			// 2、批量规则==“经济批量”，则计划产出量需要根据经济批量和批量增量计算得到：
			else if (nc.vo.bd.material.IMaterialEnumConst.BATCHRULE_ECONOMY
					.equals(batchRule)) {
				nplannedOrderReceipts = EoqUtil.economicBatchCalculate(
						material, nnet);
			}
		}
		plo.setNaccponum(nplannedOrderReceipts);
	}

	private static void updateOnputNum(MaterialVO material, PoVO plo) {
		UFDouble waster = UFDouble.ZERO_DBL;
		if (material.getWasterrate() != null) {
			waster = material.getWasterrate();
		}
		UFDouble coefficient = new UFDouble(1).sub(waster);
		// 计划产出数量/（1-废品系数）
		plo.setNnumber(plo.getNaccponum().multiply(coefficient));
	}

	private static void updateInputNum(MaterialVO material, PoVO plo) {
		UFDouble waster = UFDouble.ZERO_DBL;
		if (material.getWasterrate() != null) {
			waster = material.getWasterrate();
		}
		UFDouble coefficient = new UFDouble(1).sub(waster);
		// 计划投入量/（1-废品系数）
		plo.setNaccponum(plo.getNnumber().div(coefficient));
	}

	private static boolean isPR(MaterialVO materialVO) {
		return materialVO.getMartype() != null
				&& materialVO.getMartype().equals("PR");
	}

}

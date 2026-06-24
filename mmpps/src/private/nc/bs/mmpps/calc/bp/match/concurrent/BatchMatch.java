package nc.bs.mmpps.calc.bp.match.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.framework.common.NCLocator;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.mmpps.calc.bp.BPManager;
import nc.bs.mmpps.calc.bp.CalculateResult;
import nc.bs.mmpps.calc.bp.MatchContainer;
import nc.bs.mmpps.calc.bp.base.BaseMatch;
import nc.bs.mmpps.calc.bp.batch.IMatchFilter;
import nc.bs.mmpps.calc.bp.bom.IBOMQuery;
import nc.bs.mmpps.calc.bp.demand.DemandQuery;
import nc.bs.mmpps.calc.bp.match.FirstMatch;
///{mto_rm}import nc.bs.mmpps.calc.bp.match.LotRegMatch;
import nc.bs.mmpps.calc.bp.match.NormalMatch;
import nc.bs.mmpps.calc.bp.match.ReplaceMatch;
import nc.bs.mmpps.calc.bp.material.MaterialQueryBP;
import nc.bs.mmpps.calc.bp.materialscope.MaterialCacheManager;
import nc.bs.mmpps.calc.bp.rp.RescheduleConstruct;
import nc.bs.mmpps.calc.bp.supply.SupplyQuery;
import nc.bs.mmpps.calc.bp.supply.SupplyUpdateBP;
import nc.bs.mmpps.calc.bp.syslog.SysLogManager;
import nc.bs.mmpps.calc.bp.temptable.BatchMaterialTempTable;
import nc.bs.mmpps.calc.bp.temptable.MaterialAuxTempTable;
import nc.bs.mmpps.calc.bp.temptable.MaterialTempTable;
import nc.bs.mmpps.calc.bp.temptable.PPSTempTable;
import nc.bs.mmpps.calc.bp.temptable.ParentMaterialTempTable;
import nc.bs.mmpps.calc.bp.utils.CalcHslParseUtil;
import nc.bs.mmpps.calc.bp.utils.CalcUFDoubleUtil;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pattern.data.vo.VOUpdate;
import nc.pubitf.mmpub.replace.RealSubsDeleteVO;
import nc.pubitf.mmpub.replace.RealSubsResultVO;
import nc.pubitf.mmpub.replace.mmpps.mpm.IRealSubsMaintainServiceForPPS;
import nc.pubitf.mmpub.replace.mmpps.mpm.RealSubsUpdateVO4MRPSubs;
import nc.util.mmf.busi.measure.MeasureHelper;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.mmbd.pst.enumeration.PlanPurposeEnum;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.mmpps.calc.entity.rp.RescheduleVO;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.mmpps.calc.enumeration.LogItemStatus;
import nc.vo.mmpps.calc.enumeration.MaterialScope;
import nc.vo.mmpps.calc.enumeration.SchemaType;
import nc.vo.mmpps.calc.extinterface.ICalcBatchMatch;
import nc.vo.mmpps.mpm.res.MpmRes;
import nc.vo.mmpps.mps0202.PoVO;
import nc.vo.mmpub.replace.IRealSubsEnumConst;
import nc.vo.mmpub.replace.entity.RealsubsVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 *
 * 分批 匹配
 * 
 * @since 6.5
 * @version 2012-8-10 下午02:55:33
 * @author guojunf
 */

public final class BatchMatch implements ICalcBatchMatch {

	private CalculateContext context;

	private int levelCode;

	/**
	 * 完成数量
	 */
	private int completeCount = 0;

	public int getCompleteCount() {
		return this.completeCount;
	}

	public Set<String> getPoMaterials() {
		return this.poMaterials;
	}

	/**
	 * 生成计划订单物料的数量
	 */
	private Set<String> poMaterials;

	private MaterialQueryBP materialQrybp;

	private IBOMQuery bomQrybp;

	private DemandQuery demandQry;

	private SupplyQuery supplyQry;

	private Collection<MaterialVO> materials;

	// 物料范围ID,物料Entity
	private Map<String, MaterialVO> materialMapping;

	private MatchContainer matchContainer;

	private IRealSubsMaintainServiceForPPS iRealSubsService;

	/**
	 * 计算结果类
	 */
	private CalculateResult calcResult;

	private IMatchFilter batchFilter;

	public BatchMatch(CalculateContext context, int levelCode, IMatchFilter matchFilter, CalculateResult result) {
		this.context = context;
		this.levelCode = levelCode;
		this.batchFilter = matchFilter;
		this.materialQrybp = new MaterialQueryBP(context);
		this.demandQry = new DemandQuery(context);
		this.supplyQry = new SupplyQuery(context);
		this.bomQrybp = new BPManager(this.context).getBOMQueryBP();
		this.matchContainer = new MatchContainer(context, this.bomQrybp);
		this.calcResult = result;
		this.poMaterials = new HashSet<String>();
		this.iRealSubsService = NCLocator.getInstance().lookup(IRealSubsMaintainServiceForPPS.class);

		// 加载数据
		this.loadData();
	}

	/**
	 * 取得合法物料
	 * 
	 * @param allMaterials
	 * @return
	 */
	private Collection<MaterialVO> getLegalMaterials(Collection<MaterialVO> allMaterials) {
		String logMessage1 = NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0273")/* 获取有效物料 */;
		SysLogManager.createStartMessage(this.context.getCalculateCode(), logMessage1);
		// 合法的物料
		Collection<MaterialVO> legalMaterials = new ArrayList<MaterialVO>();
		if (allMaterials == null) {
			return legalMaterials;
		}
		for (MaterialVO material : allMaterials) {
			// 物料检查
			if (this.checkMaterial(material)) {
				continue;
			}
			legalMaterials.add(material);

		}
		SysLogManager.createEndMessage(this.context.getCalculateCode(), logMessage1);
		return legalMaterials;
	}

	@Override
	public void doMatch() {

		PPSTempTable.getInstance(MaterialTempTable.class, this.context).createTable();
		PPSTempTable.getInstance(MaterialAuxTempTable.class, this.context).createTable();
		PPSTempTable.getInstance(ParentMaterialTempTable.class, this.context).createTable();

		String logMessage = NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0274")/* 执行匹配 */;
		SysLogManager.createStartMessage(this.context.getCalculateCode(), logMessage);
		// 合法的物料
		Collection<MaterialVO> legalMaterials = this.getLegalMaterials(this.materials);

		// 如果没有合法物料，则不需要进行匹配
		if (legalMaterials == null || legalMaterials.size() == 0) {
			// 保存一下日志 直接返回
			this.calcResult.saveLastResult();
			return;
		}
		Map<String, String> mtrlMapping = new HashMap<String, String>();
		for (MaterialVO materialVO : legalMaterials) {
			mtrlMapping.put(materialVO.getCmaterialcode(), materialVO.getCmaterialid());
		}
		SysLogManager.createNormalMessage(this.context.getCalculateCode(),
				"当前批物料:"/* -=notranslate=- */);
		SysLogManager.createNormalMessage(this.context.getCalculateCode(), mtrlMapping);

		// 获取所有需求
		List<DemandVO> sumDemands = this.matchContainer.getDemandQry().getAllDemands();
		// 获取所有供给
		List<SupplyVO> sumSupplies = this.matchContainer.getSupplyQry().getAllUsefullSupplies();

		// 过滤无效需求
		this.matchContainer.getDemandQry().doBeforeMatch(legalMaterials);
		this.calcResult.addDS(this.matchContainer.getCurBatchMaterialMap(), sumDemands, sumSupplies);

		List<BaseMatch> batchMatches = new ArrayList<BaseMatch>();

		// TODO:MTO去掉原先的供需预定匹配
		// 供需预留匹配
		// {mto_rm} BaseMatch lotRegMatch = new LotRegMatch(this.matchContainer,
		// {mto_rm} this.calcResult);
		// 预留及源头匹配
		BaseMatch firstMatch = new FirstMatch(this.matchContainer, this.calcResult, null);
		// 无替代匹配
		NormalMatch normalMatch = new NormalMatch(this.matchContainer, this.calcResult, firstMatch);
		// 替代匹配
		ReplaceMatch replaceMatch = new ReplaceMatch(this.matchContainer, this.calcResult, normalMatch);

		// 预留匹配
		// {mto_rm} batchMatches.add(lotRegMatch);
		// 源头匹配
		batchMatches.add(firstMatch);
		// 无替代匹配
		batchMatches.add(normalMatch);
		// 替代匹配
		batchMatches.add(replaceMatch);

		for (BaseMatch bp : batchMatches) {
			bp.setPoMaterials(this.poMaterials);
			bp.doMatch(legalMaterials);
		}

		this.completeCount += legalMaterials.size();

		// 生成取消类型的重排建议
		SchemaVO planSchema = this.context.getSchema().getParentVO();
		// 是否生成重排建议
		boolean isCreateRP = planSchema.getBiscreaterp() != null ? planSchema.getBiscreaterp().booleanValue() : false;
		if (isCreateRP) {
			List<SupplyVO> unMatchedSupplies = this.matchContainer.getSupplyQry().getUnMatchedSupplies();

			List<RescheduleVO> rplist = RescheduleConstruct.createCanceledRPVOs(this.context, legalMaterials,
					unMatchedSupplies);
			this.calcResult.addRps(rplist);
		}
		this.calcResult.addSumVos(legalMaterials);
		this.calcResult.saveLastResult();
		this.updateRemainNum();
		this.updateRealSubs();
		this.calcResult.clearAfterBatch();
		SysLogManager.createEndMessage(this.context.getCalculateCode(), logMessage);
	}

	// 更新实际替代表
	private void updateRealSubs() {
		Map<String, List<RealSubsResultVO>> realSubsMap = this.matchContainer.getRealSubsResultMap();
		if (realSubsMap != null) {
			List<RealSubsUpdateVO4MRPSubs> updateVOs = new ArrayList<RealSubsUpdateVO4MRPSubs>();
			Set<RealSubsResultVO> resultSet = new HashSet<RealSubsResultVO>();
			// 存放计划订单，替代减少量的Map,用来回写计划订单的累计已替代量
			Map<String, UFDouble> reWriteMap = new HashMap<String, UFDouble>();
			for (List<RealSubsResultVO> list : realSubsMap.values()) {
				resultSet.addAll(list);
			}
			// V63 运算更新实际替代关系不进行并发校验
			for (RealSubsResultVO resultVO : resultSet) {
				if (!reWriteMap.containsKey(resultVO.getVsubsid())) {
					reWriteMap.put(resultVO.getVsubsid(), UFDouble.ZERO_DBL);
				}
				// 如果是部分替代
				if (resultVO.getFsubstype().intValue() == IRealSubsEnumConst.SubsTypeEnum_PARTIAL.intValue()
						&& resultVO.getBused() != null && resultVO.getBused().booleanValue()
						&& resultVO.getBmatched().booleanValue()) {
					// 替代主数量 = 运算已分配量+已分配量
					resultVO.setNsnum(CalcUFDoubleUtil.nullToZero(resultVO.getNcalcnum())
							.add(CalcUFDoubleUtil.nullToZero(resultVO.getNsassignednum())));
					// 替代辅数量
					resultVO.setNsastnum(MeasureHelper.getAssNum(resultVO.getNsnum(), resultVO.getVschangerate()));
					// 主料数量 = 替代料数量/替代系数
					UFDouble numCurr = CalcHslParseUtil.calcNumBySubRate(resultVO.getNsnum(), resultVO.getVsubsrate());
					reWriteMap.put(resultVO.getVsubsid(),
							reWriteMap.get(resultVO.getVsubsid()).add(resultVO.getNnum().sub(numCurr)));
					resultVO.setNnum(numCurr);
					// 主料辅数量
					resultVO.setNastnum(MeasureHelper.getAssNum(resultVO.getNnum(), resultVO.getVchangerate()));

					// 构建待更新的VO列表
					updateVOs.add(this.constructUpdateVO(resultVO));
				}
			}

			List<RealSubsDeleteVO> deleteVOs = new ArrayList<RealSubsDeleteVO>();
			for (RealSubsResultVO resultVO : resultSet) {
				// 此处只处理部分替代和全部替代，用户指定替代在其他地方处理
				// 并且没有备料已分配量的
				int susbtype = resultVO.getFsubstype().intValue();
				if (susbtype == IRealSubsEnumConst.SubsTypeEnum_PARTIAL.intValue()
						|| susbtype == IRealSubsEnumConst.SubsTypeEnum_All.intValue()) {
					// 部分替代匹配到了 没有使用
					if ((!resultVO.getBused().booleanValue() || resultVO.getNsnum().compareTo(UFDouble.ZERO_DBL) <= 0)
							&& resultVO.getBmatched().booleanValue()) {
						// 没有备料已分配量直接删除
						if (CalcUFDoubleUtil.nullToZero(resultVO.getNsassignednum())
								.compareTo(UFDouble.ZERO_DBL) <= 0) {

							RealSubsDeleteVO deleteVO = new RealSubsDeleteVO(resultVO.getPk_realsubs(), null);
							deleteVOs.add(deleteVO);
							reWriteMap.put(resultVO.getVsubsid(),
									reWriteMap.get(resultVO.getVsubsid()).add(resultVO.getNnum()));
						} else {
							// 保留备料已分配量
							// 替代主数量 = 运算已分配量+已分配量
							resultVO.setNsnum(resultVO.getNsassignednum());
							// 替代辅数量
							resultVO.setNsastnum(
									MeasureHelper.getAssNum(resultVO.getNsnum(), resultVO.getVschangerate()));
							// 主料数量 = 替代料数量/替代系数
							UFDouble numCurr = CalcHslParseUtil.calcNumBySubRate(resultVO.getNsnum(),
									resultVO.getVsubsrate());

							reWriteMap.put(resultVO.getVsubsid(),
									reWriteMap.get(resultVO.getVsubsid()).add(resultVO.getNnum().sub(numCurr)));

							resultVO.setNnum(numCurr);
							// 主料辅数量
							resultVO.setNastnum(MeasureHelper.getAssNum(resultVO.getNnum(), resultVO.getVchangerate()));

							// 构建待更新的VO列表
							updateVOs.add(this.constructUpdateVO(resultVO));
						}

					}
				}
			}

			// 回写替代量
			if (!updateVOs.isEmpty()) {
				try {
					this.iRealSubsService.updateMRPRealSubs(updateVOs, new String[] { RealsubsVO.NSNUM,
							RealsubsVO.NSASTNUM, RealsubsVO.NNUM, RealsubsVO.NASTNUM });
					updateVOs.clear();
				} catch (BusinessException e) {
					ExceptionUtils.wrappException(e);
				}
			}

			// 删除未使用过的替代关系
			if (!deleteVOs.isEmpty()) {
				try {
					this.iRealSubsService.deleteRealSubs(deleteVOs);
					deleteVOs.clear();
				} catch (BusinessException e) {
					ExceptionUtils.wrappException(e);
				}
			}

			// 更新计划订单
			//this.planOrderUpdate(reWriteMap);

			realSubsMap.clear();
		}

	}

	/**
	 * @param reWriteMap
	 * @return
	 */
	private int planOrderUpdate(Map<String, UFDouble> reWriteMap) {
		/**
		 * Update a Set value = Select b.value - a.value From b Where a.id =
		 * b.id);
		 */
		// String tempTable =
		// PloReplNumTempTable.getInstance(this.context).createTable(reWriteMap);
		// MMSqlBuilder sql = new MMSqlBuilder();
		// sql.append(" Update " + PoVO.tableName);
		// sql.append(" Set " + PoVO.NSUBSEDNUM);
		// sql.append(" = (Select " + PoVO.tableName + "." + PoVO.NSUBSEDNUM +
		// "-" + tempTable + "." + PoVO.NSUBSEDNUM);
		// sql.append(" From " + tempTable);
		// sql.append(" Where ");
		// sql.append(PoVO.tableName + "." + PoVO.CPOID + "=" + tempTable + "."
		// + PoVO.CPOID + ")");

		// DataAccessUtils dbUtil = new DataAccessUtils();
		// int count = dbUtil.update(sql.toString());

		//

		if (reWriteMap == null || reWriteMap.isEmpty()) {
			return 0;
		}
		VOQuery<PoVO> bpQuery = new VOQuery<PoVO>(PoVO.class,
				new String[] { PoVO.CPOID, PoVO.NSUBSEDNUM, PoVO.NSUBSEDASTNUM, PoVO.VCHANGERATE });
		PoVO[] poVOs = bpQuery.query(reWriteMap.keySet().toArray(new String[0]));
		int count = 0;
		if (poVOs != null && poVOs.length > 0) {
			for (PoVO poVO : poVOs) {
				UFDouble value = reWriteMap.get(poVO.getCpoid());
				if (value != null && value.doubleValue() != UFDouble.ZERO_DBL.doubleValue()
						&& poVO.getNsubsednum() != null
						&& poVO.getNsubsednum().doubleValue() != UFDouble.ZERO_DBL.doubleValue()) {
					poVO.setNsubsednum(poVO.getNsubsednum().sub(value));
				}

				if (poVO.getNsubsednum() != null) {
					poVO.setNsubsedastnum(MeasureHelper.getAssNum(poVO.getNsubsednum(), poVO.getVchangerate()));
				} else {
					poVO.setNsubsedastnum(null);
				}

			}

			VOUpdate<PoVO> bpUpdate = new VOUpdate<PoVO>();
			count = bpUpdate.update(poVOs, new String[] { PoVO.NSUBSEDNUM, PoVO.NSUBSEDASTNUM }).length;
		}
		return count;

	}

	private RealSubsUpdateVO4MRPSubs constructUpdateVO(RealSubsResultVO resultVO) {
		RealSubsUpdateVO4MRPSubs updateVO = new RealSubsUpdateVO4MRPSubs();
		// 替代料
		updateVO.setSmatvo(resultVO.getSmatvo());
		updateVO.setCastunitid(resultVO.getCastunitid());
		updateVO.setCsastunitid(resultVO.getCsastunitid());
		updateVO.setCsunitid(resultVO.getCsunitid());
		updateVO.setCunitid(resultVO.getCunitid());
		updateVO.setFsubstype(resultVO.getFsubstype());
		updateVO.setNastnum(resultVO.getNastnum());
		updateVO.setNsastnum(resultVO.getNsastnum());
		updateVO.setNnum(resultVO.getNnum());
		updateVO.setNsnum(resultVO.getNsnum());
		updateVO.setNsubspriority(resultVO.getNsubspriority());
		updateVO.setTs(null);
		updateVO.setPk_realsubs(resultVO.getPk_realsubs());
		updateVO.setVchangerate(resultVO.getVchangerate());
		updateVO.setVschangerate(resultVO.getVschangerate());
		updateVO.setVsubsrate(resultVO.getVsubsrate());
		updateVO.setCsbomid(resultVO.getVsbomid());
		updateVO.setCspackbomid(resultVO.getVspackbomversion());

		return updateVO;
	}

	private void updateRemainNum() {
		SupplyVO[] changeSupplies = this.matchContainer.getSupplyQry().getRemainNumChangeSupplies();
		SupplyUpdateBP supplyupdateBP = new SupplyUpdateBP();
		supplyupdateBP.updateRemainNum(changeSupplies);
	}

	/**
	 * 物料正确性检查
	 * 
	 * @param material
	 * @return
	 */
	private boolean checkMaterial(MaterialVO material) {

		// 时格
		Integer timeslice = material.getTimeslice();

		// Modified by Goodie
		// 时格为空时也抛如下异常信息
		if (timeslice == null || IMaterialEnumConst.TIMESLICE_DAY != timeslice.intValue()) {
			this.logItemInsert(material, LogItemStatus.MTRLTIMEFENCEEX, MpmRes.getTimeFenceExpt());
			return true;
		}
		// 展望期
		Integer planhorizon = material.getIplanhorizon();
		String msg = null;
		if (planhorizon == null) {
			msg = MpmRes.getPlanHorizonNullExpt();
		} else if (planhorizon.intValue() == 0) {
			msg = MpmRes.getPlanHorizonZExpt();
		}
		if (null != msg) {
			this.logItemInsert(material, LogItemStatus.MTRLPLANHOREX, msg + MpmRes.getPlanHorizonExpt());
			return true;
		}

		// 计划属性
		String planProp = material.getPlanprop();

		// 物料范围枚举
		int materialScope = this.context.getSchema().getParentVO().getFmaterielscope().intValue();
		// 是否为选择物料
		boolean isMaterialScope = materialScope == MaterialScope.SPECIFIED_SCOPE.toInt();

		// 需要生成例外报告:
		boolean isCreateReport = isMaterialScope
				&& MaterialCacheManager.getSelected(this.context).contains(material.getCmaterialid());

		// 方案类型
		int pstype = this.context.getSchema().getParentVO().getFpstype().intValue();
		// 如果是MPS运算
		if (pstype == SchemaType.MPS.toInt()) {
			if (planProp == null || !planProp.equals(nc.vo.bd.material.IMaterialEnumConst.PLANPROP_MPS)) {
				if (isCreateReport) {
					this.logItemInsert(material, LogItemStatus.MTRLPLANPROPEX, MpmRes.getMPSCalcExpt());

				}
				return true;
			}
		}

		// Added by Goodie
		// MRP运算
		else if (pstype == SchemaType.MRP.toInt()) {
			// 计划目的
			Integer planpurpose = this.context.getSchema().getParentVO().getFplanpurpose();

			if (planpurpose.equals(PlanPurposeEnum.PRODUCTION.value())) {
				if (planProp == null || !planProp.equals(nc.vo.bd.material.IMaterialEnumConst.PLANPROP_MRP)) {
					if (isCreateReport) {
						this.logItemInsert(material, LogItemStatus.MTRLPLANPROPEX, MpmRes.getMRPCalcExpt());
					}
					return true;
				}
			} else if (planpurpose.equals(PlanPurposeEnum.STOCK.value())) {
				if (planProp == null || !(planProp.equals(nc.vo.bd.material.IMaterialEnumConst.PLANPROP_MPS)
						|| planProp.equals(nc.vo.bd.material.IMaterialEnumConst.PLANPROP_MRP))) {
					if (isCreateReport) {
						this.logItemInsert(material, LogItemStatus.MTRLPLANPROPEX, MpmRes.getNotMPSOMRPCalcExpt());
					}
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param material
	 * @param status
	 * @param msg
	 */
	private void logItemInsert(MaterialVO material, LogItemStatus status, String msg) {
		this.logItemInsert(material, status, msg, 0);
	}

	private void logItemInsert(MaterialVO material, LogItemStatus status, String msg, int dr) {
		this.calcResult.saveLogItem(status, msg, material, dr);
	}

	private void loadData() {
		// Log Start
		SysLogManager.createStartMessage(this.context.getCalculateCode(),
				NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0275")/* 分批匹配数据加载 */);
		SysLogManager.createStartMessage(this.context.getCalculateCode(),
				NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0276", null, new String[] {
						this.batchFilter.getClass().getName() })/* 分批过滤器:{0} */);

		String tempTable = PPSTempTable.getInstance(BatchMaterialTempTable.class, this.context)
				.createTable(this.batchFilter);
		String inSql = " in (Select cppsmaterialid from " + tempTable + ")";

		// 获取当前批物料的需求
		this.demandQry.getBatchDemands(inSql, this.levelCode);
		// 获取当前批物料的供给
		this.supplyQry.getBatchSupplies(inSql, this.levelCode);
		// 获取当前批物料Map
		this.materialMapping = this.materialQrybp.getMaterial(inSql, this.levelCode);

		if (null != this.materialMapping) {
			this.materials = this.materialMapping.values();
		}
		//
		this.matchContainer.setDemandQry(this.demandQry);
		//
		this.matchContainer.setSupplyQry(this.supplyQry);
		//
		this.matchContainer.initData(this.materialMapping);
		// Log End
		SysLogManager.createEndMessage(this.context.getCalculateCode(),
				NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0276", null, new String[] {
						this.batchFilter.getClass().getName() })/* 分批过滤器:{0} */);
		SysLogManager.createEndMessage(this.context.getCalculateCode(),
				NCLangResOnserver.getInstance().getStrByID("50010006_0", "050010006-0275")/* 分批匹配数据加载 */);
	}

	public int getLevelCode() {
		return this.levelCode;
	}

}

package nc.bs.mmpps.calc.bp.supply;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nc.bs.mmpps.calc.bp.base.BaseQuery;
import nc.bs.mmpps.calc.bp.syslog.SysLogManager;
import nc.bs.mmpps.calc.bp.temptable.PPSTempTable;
import nc.bs.mmpps.calc.bp.temptable.SupplyKeyTempTable;
import nc.bs.mmpps.calc.bp.utils.CalcListUtil;
import nc.bs.mmpps.calc.bp.utils.CalcOrderUtil;
import nc.bs.mmpps.calc.bp.utils.DemandSupplyKeyUtil;
import nc.bs.mmpps.calc.bp.utils.SupplyTypeUtil;
import nc.jdbc.framework.SQLParameter;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.util.mmf.framework.db.MMSqlBuilder;
import nc.vo.ic.reserve.pub.ReserveSupplyDetail;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SupplyVO;
import nc.vo.mmpps.calc.entity.schema.SchemaVO;
import nc.vo.mmpps.calc.enumeration.SupplyType;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.util.VOSortUtils;

/**
 * 供给查询类
 * 
 * @since 6.5
 * @version 2012-8-6 上午11:25:33
 * @author chenjij
 */
public final class SupplyQuery extends BaseQuery<SupplyVO> {

	/**
	 * 供给Map Added by Goodie
	 */
	private Map<String, List<SupplyVO>> supplyMap;

	private int lastSupplyIndex;

	// 供需预定供给Map
	private Map<String, SupplyVO> supplyKeyMap;

	/** 供给ID映射,对于存量为MD5 */
	private Map<String, SupplyVO> supplyIDMap = new HashMap<String, SupplyVO>();

	/**
	 * 构造器
	 * 
	 * @param context
	 *            运算的上下文
	 */
	public SupplyQuery(CalculateContext context) {
		super(SupplyVO.class, context);
	}

	/**
	 * 获取所有供给[包括无效的]
	 * 
	 * @return
	 */
	public List<SupplyVO> getAllUsefullSupplies() {
		List<SupplyVO> allSupplies = CalcListUtil.mapListToList(this.supplyMap);
		return allSupplies;
	}

	public SupplyVO[] getSupplies(String condition, SQLParameter para) {
		List<SupplyVO> supplies = this.getEntityQuery().query(condition, null, para);
		if (supplies != null) {
			return supplies.toArray(new SupplyVO[0]);
		}

		return null;
	}

	/**
	 * 根据起始物料范围ID、结束物料范围ID获取供给VO对象
	 * 
	 * @param startId
	 *            起始id
	 * @param endId
	 *            结束id
	 */
	public void getBatchSupplies(String mInSql, int lowLevel) {
		String computeCode = this.getContext().getCalculateCode();
		String logMessage = this.getClass().getName() + ".getBatchSupplies()," + lowLevel + ")";
		SysLogManager.createStartMessage(computeCode, logMessage);

		this.supplyMap = new HashMap<String, List<SupplyVO>>();
		// 供需预定需要的缓存Map
		this.supplyKeyMap = new HashMap<String, SupplyVO>();

		// 不是跟单物料
		MMSqlBuilder sql01 = new MMSqlBuilder();
		sql01.and();
		sql01.append(SupplyVO.CPPSMATERIALID);
		sql01.append(" in ");
		sql01.append(
				"(select cppsmaterialid from mm_calcmaterial where blotregmtrl='N' and cppsmaterialid " + mInSql + ")");
		sql01.and();
		sql01.append(SupplyVO.LEVELCODE, String.valueOf(lowLevel));
		sql01.and();
		sql01.append(SupplyVO.COMPUTECODE, computeCode);
		//liyf 2023-11-27 重写一个排序，增加首次入库日期以及失效日期
//		String normOrder = CalcOrderUtil.getSupplyOrder();
		String normOrder = CalcOrderUtil.getSupplyOrder2();

		SupplyVO[] supplies = this.getVOQuery().query(sql01.toString(), " order by " + normOrder);

		for (SupplyVO supply : supplies) {
			this.addSupplyToMapEnd(this.supplyMap, supply);
			this.supplyKeyMap.put(supply.getSupplykey(), supply);

			// 如果不是存量
			if (supply.getFsupplytype().intValue() != SupplyType.ONHAND_NUM.toInt()) {
				this.supplyIDMap.put(supply.getVsupplybid() != null ? supply.getVsupplybid() : supply.getVsupplyid(),
						supply);
			} else {
				this.supplyIDMap.put(supply.getCgroupkey(), supply);
			}
		}

		// 按单计划
		if (this.getContext().getBLotReg()) {
			// 跟单物料
			MMSqlBuilder sql02 = new MMSqlBuilder();
			sql02.and();
			sql02.append(SupplyVO.CPPSMATERIALID);
			sql02.append(" in ");
			sql02.append("(select cppsmaterialid from mm_calcmaterial where blotregmtrl='Y' and cppsmaterialid "
					+ mInSql + ")");
			sql02.and();
			sql02.append(SupplyVO.LEVELCODE, String.valueOf(lowLevel));
			sql02.and();
			sql02.append(SupplyVO.COMPUTECODE, computeCode);

			// 跟单维度
			@SuppressWarnings("unchecked")
			List<String> fieldCodes = (List<String>) this.getContext().getCacheValue("key#GDWD#STR");
			MMSqlBuilder lotRegOrder = new MMSqlBuilder();
			for (String fieldCode : fieldCodes) {
				if (fieldCode.contains("vfree")) {
					lotRegOrder.append(fieldCode + ",");
				}
			}

			SupplyVO[] supplies02 = this.getVOQuery().query(sql02.toString(),
					" order by " + (lotRegOrder.toString().isEmpty() ? normOrder
							: normOrder.replace(SupplyVO.CGROUPKEY + " asc,", lotRegOrder.toString())));

			// 按单计划把供给放到临时表里面
			PPSTempTable.getInstance(SupplyKeyTempTable.class, this.getContext()).createTableBySupplyVOs(supplies02);
			for (SupplyVO supply : supplies02) {
				this.addSupplyToMapEnd(this.supplyMap, supply);
				this.supplyKeyMap.put(supply.getSupplykey(), supply);

				// 如果不是存量
				if (supply.getFsupplytype().intValue() != SupplyType.ONHAND_NUM.toInt()) {
					this.supplyIDMap.put(
							supply.getVsupplybid() != null ? supply.getVsupplybid() : supply.getVsupplyid(), supply);
				} else {
					this.supplyIDMap.put(supply.getCgroupkey(), supply);
				}
			}

		}
		SysLogManager.createEndMessage(computeCode, logMessage);
	}

	/**
	 * 供需预定使用，根据供给key取得供给
	 * 
	 * @param supplyKey
	 * @return
	 */
	public SupplyVO getSupplyVOByKey(String supplyKey) {
		return this.supplyKeyMap.get(supplyKey);
	}

	public SupplyVO getSupplyVOByRs(ReserveSupplyDetail rs) {
		Integer supplyType = SupplyTypeUtil.getSupplyType(rs.getCsupplytype());

		String key = DemandSupplyKeyUtil.getSupplyKey(rs, supplyType);
		if (this.supplyKeyMap.containsKey(key)) {
			return this.supplyKeyMap.get(key);
		}
		return null;
	}

	private void addSupplyToMapEnd(Map<String, List<SupplyVO>> splMap, SupplyVO supplyVO) {
		String materialID = supplyVO.getCmaterialid();
		if (splMap.containsKey(materialID)) {
			splMap.get(materialID).add(supplyVO);
		} else {
			List<SupplyVO> supplyVOs = new LinkedList<SupplyVO>();
			supplyVOs.add(supplyVO);
			splMap.put(materialID, supplyVOs);
		}
	}

	private void addSupplyToMap(Map<String, List<SupplyVO>> splMap, SupplyVO supplyVO, int index) {
		String materialID = supplyVO.getCmaterialid();
		if (splMap.containsKey(materialID)) {
			if (index < 0) {
				splMap.get(materialID).add(0, supplyVO);
			} else {
				// 安全库存不消耗供给 所以安全库存产生的计划订单 要根据供给的优先级插入单供给明细中
				List<SupplyVO> supplyvos = splMap.get(materialID);
				int lastIndex = index;
				// 再想前寻找合适的位置
				boolean b = true;
				for (; lastIndex >= 0;) {
					SupplyVO frontSupplyVO = supplyvos.get(lastIndex);
					b = true;
					if (supplyVO.getCgroupkey().equals(frontSupplyVO.getCgroupkey())) {
						int a = frontSupplyVO.getDmatchdate().compareTo(supplyVO.getDmatchdate());
						if (a > 0) {
							b = false;
						} else if (a == 0) {
							if (frontSupplyVO.getFsupplytype().intValue() == SupplyType.MPS.toInt()) {
								b = false;
							}
							if (frontSupplyVO.getFsupplytype().intValue() == SupplyType.PLANNED_ORDER.toInt()) {
								if (frontSupplyVO.getNsupplynum().compareTo(supplyVO.getNsupplynum()) < 0) {
									b = false;
								}
							}
						}
						// 其他情况都返回true
					}

					lastIndex--;
					if (b) {
						break;
					}
				}

				// 放在可用供给的后面
				lastIndex = lastIndex + 2;
				int supplySize = supplyvos.size();
				if (lastIndex > supplySize - 1) {
					splMap.get(materialID).add(supplyVO);
				} else {
					splMap.get(materialID).add(lastIndex, supplyVO);
				}

			}
		} else {
			List<SupplyVO> supplyVOs = new LinkedList<SupplyVO>();
			supplyVOs.add(supplyVO);
			splMap.put(materialID, supplyVOs);
		}
	}

	private UFDouble getRemainNum(SupplyVO supplyVO) {
		UFDouble nreservenum = supplyVO.getNreservationnum();
		nreservenum = nreservenum == null ? UFDouble.ZERO_DBL : nreservenum;

		UFDouble nsupplynum = supplyVO.getNsupplynum();
		nsupplynum = nsupplynum == null ? UFDouble.ZERO_DBL : nsupplynum;

		UFDouble remainNum = nsupplynum.sub(nreservenum);
		return remainNum;
	}

	public SupplyVO[] getRemainNumChangeSupplies() {
		List<SupplyVO> needWriteSupplies = new ArrayList<SupplyVO>();
		List<SupplyVO> allSupplies = CalcListUtil.mapListToList(this.supplyMap);
		if (allSupplies != null) {
			for (SupplyVO supplyVO : allSupplies) {
				UFDouble oldRemainNum = this.getRemainNum(supplyVO);
				if (null != oldRemainNum && null != supplyVO.getNremainnum()
						&& oldRemainNum.compareTo(supplyVO.getNremainnum()) != 0) {
					needWriteSupplies.add(supplyVO);
				}

			}
		}
		return needWriteSupplies.toArray(new SupplyVO[0]);

	}

	/**
	 * 同源MatchKey
	 * <p>
	 * 分类标识+源头单据类型+源头单据主键+源头单据表体主键
	 * </p>
	 * 
	 * @param groupKey
	 * @param vfirsttype
	 * @param vfirstid
	 * @param vfirstbid
	 * @return
	 */
	private String getFirstDocMatchKey(String groupKey, String vfirsttype, String vfirstid, String vfirstbid) {
		// 如果没有源头信息，则返回 null
		if (MMValueCheck.isEmpty(vfirsttype)) {
			return null;
		}
		return groupKey + this.emptytoDefault(vfirsttype) + this.emptytoDefault(vfirstid)
				+ this.emptytoDefault(vfirstbid);
	}

	private String emptytoDefault(String key) {
		return MMValueCheck.isEmpty(key) ? "~" : key;
	}

	/**
	 * 获取与需求 相同源头的供给
	 * 
	 * @param demand
	 * @return
	 */
	public List<SupplyVO> getSuppliesEqualFirst(DemandVO demand) {
		List<SupplyVO> matchedSupplies = new ArrayList<SupplyVO>();
		String demandfirstKey = "";
		// 使用原始需求单据信息
		if (!MMValueCheck.isEmpty(demand.getVfirstdemandid())) {
			demandfirstKey = this.getFirstDocMatchKey(demand.getCgroupkey(), demand.getVfirstdemandtype(),
					demand.getVfirstdemandid(), demand.getVfirstdemandbid());
		} else {
			demandfirstKey = this.getFirstDocMatchKey(demand.getCgroupkey(), demand.getVdemandtypecode(),
					demand.getCdemandbillid(), demand.getCdemandbillbid());
		}
		if (null != this.supplyMap && null != this.supplyMap.get(demand.getCmaterialid())) {
			for (SupplyVO supply : this.supplyMap.get(demand.getCmaterialid())) {
				String supplyfirstKey = this.getFirstDocMatchKey(supply.getCgroupkey(), supply.getVfirstdemandtype(),
						supply.getVfirstdemandid(), supply.getVfirstdemandbid());
				if (null != supplyfirstKey && null != demandfirstKey) {
					if (supplyfirstKey.equals(demandfirstKey) && null != supply.getNremainnum()
							&& supply.getNremainnum().compareTo(UFDouble.ZERO_DBL) > 0) {
						matchedSupplies.add(supply);

					}
				}
			}
		}
		return matchedSupplies;
	}

	/**
	 * 获取对应需求的供给
	 * 
	 * @param demand
	 *            某一需求
	 * @param endDate
	 *            结束日期
	 * @return List<SupplyVO>
	 */
	public List<SupplyVO> getSupplies(MaterialVO material, DemandVO demand, UFDate endDate) {
		return this.getSupplies(material, demand, endDate, null);
	}

	public SupplyVO getSupply(String bid) {
		return this.supplyIDMap.get(bid);
	}

	/**
	 * 获取未参与匹配的供应
	 * 
	 * @param cmaterialid
	 * @return
	 */
	public List<SupplyVO> getUnMatchedSupplies(String cmaterialid) {
		List<SupplyVO> supplies = this.supplyMap.get(cmaterialid);
		List<SupplyVO> unMatchedSupplies = new ArrayList<SupplyVO>();
		if (null != supplies) {
			for (SupplyVO supply : supplies) {
				// 如果未满足需求量 !=0
				if (supply.getNremainnum().doubleValue() != 0) {
					unMatchedSupplies.add(supply);
				}
			}
		}
		return unMatchedSupplies;

	}

	/**
	 * 获取未参与匹配的供应
	 * 
	 * @param cmaterialid
	 * @return
	 */
	public List<SupplyVO> getUnMatchedSupplies() {
		Collection<List<SupplyVO>> supplies = this.supplyMap.values();
		List<SupplyVO> unMatchedSupplies = new ArrayList<SupplyVO>();
		if (null != supplies) {
			for (List<SupplyVO> list : supplies) {
				for (SupplyVO supply : list) {
					// 如果未满足需求量 ！=0
					if (null != supply.getNremainnum() && supply.getNremainnum().doubleValue() != 0) {
						unMatchedSupplies.add(supply);
					}
				}
			}
		}
		return unMatchedSupplies;

	}

	/**
	 * 将新supply对象插入到供给列表中 Modified by Goodie
	 * 
	 * @param materialID
	 *            对应物料
	 * @param i
	 *            插入的位置
	 * @param newsupply
	 *            新supply对象
	 */
	public void insert(int index, SupplyVO newSupply) {
		this.addSupplyToMap(this.supplyMap, newSupply, index);
		this.supplyKeyMap.put(newSupply.getSupplykey(), newSupply);
	}

	@SuppressWarnings("unchecked")
	public List<SupplyVO> getSupplies(MaterialVO materialVO, DemandVO demand, UFDate dmatchdate,
			MaterialVO masterMaterialVO) {
		// 考虑跟单物料
		boolean bLotReg = materialVO.getBlotregmtrl() != null && materialVO.getBlotregmtrl().booleanValue();
		List<String> fieldCodes = new ArrayList<String>();
		// 跟单维度包含辅助属性
		boolean bcontainsFree = false;
		if (bLotReg) {
			// 跟单维度
			fieldCodes = (List<String>) this.getContext().getCacheValue("key#GDWD#STR");
			for (String fieldCode : fieldCodes) {
				if (fieldCode.contains("free")) {
					bcontainsFree = true;
				}
			}
		}
		List<SupplyVO> matchedSupplies = new ArrayList<SupplyVO>();
		String demandkey = demand.getCgroupkey();
		UFDate supplyDate = null;
		// 重排期间
		Integer exdays;
		if (masterMaterialVO != null) {
			exdays = masterMaterialVO.getIexsupplydays();
		} else {
			exdays = materialVO.getIexsupplydays();
		}
		// 需求日期+重排期间
		UFDate demandEndDate = this.getContext().getWorkDateCache().getAfterWorkDates(dmatchdate,
				exdays != null ? exdays.intValue() : 0);
		// Modified by Goodie
		List<SupplyVO> supplies = null;
		if (null != this.supplyMap && null != (supplies = this.supplyMap.get(demand.getCmaterialid()))) {
			this.lastSupplyIndex = 0;
		
			for (SupplyVO supply : supplies) {
				// 如果只是预留的供给单据，则不参与供需匹配
				if (supply.getBreserveonly() != null && supply.getBreserveonly().booleanValue()) {
					continue;
				}
				//2023-11-06 liyf
				if(isNotMatched(materialVO,demandEndDate,supply)){
					continue;
				}
			    
				//2023-11-06 liyf 如果库存失效日期>需求日期则不进行匹配

				if (supply.getBmatched2() == null || !supply.getBmatched2().booleanValue()) {
					boolean keyEqual = false;
					// 跟单物料 && 跟单维度中包含辅助属性
					if (bLotReg && bcontainsFree) {
						keyEqual = this.isKeyEqual(materialVO, demand, supply, masterMaterialVO, fieldCodes);
					} else {
						keyEqual = supply.getCgroupkey().equals(demandkey);
					}

					if (!keyEqual) {
						continue;
					}
					if (null != supply.getNremainnum() && supply.getNremainnum().compareTo(UFDouble.ZERO_DBL) > 0) {
						// 根据分类标识相等 && endDate>=供给日期，进行查找。
						supplyDate = supply.getDmatchdate();
						if (supplyDate.beforeDate(demandEndDate) || supplyDate.isSameDate(demandEndDate)) {
							matchedSupplies.add(supply);
						} else {
							break;
						}
					} else {
						break;
					}
				}
			}
		}

		return matchedSupplies;
	}

	/**
	 * 
	 * @param materialVO
	 * @param demandEndDate :需求日期
	 * @param supply：供给明细
	 * @return
	 */
	private boolean isNotMatched(MaterialVO materialVO,UFDate demandEndDate, SupplyVO supply) {
		// TODO Auto-generated method stub
		SchemaVO parentVO =  this.getContext().getSchema().getParentVO();
		 //是否考虑质检计划周期  ， 如果考虑，待检物料供给日期加上物料后段提前期
		if(parentVO.getVdef1() == null){
			return false;
		}
		UFBoolean vdef1 = new UFBoolean(parentVO.getVdef1());
		if(vdef1.equals(UFBoolean.FALSE)){
			return false;
		}
		if (supply.getFsupplytype().intValue() != SupplyType.ONHAND_NUM
				.toInt()){
			return false;
		}
	
		
//		 如果库存失效日期>需求日期则不进行匹配
		if(supply.getVdef3() !=null){
			UFDate dvalidate = new UFDate(supply.getVdef3()).asBegin();
			if (dvalidate.before(demandEndDate)) {
				return true;
			}
		}
		//如果可开始匹配日期<晚于需求日期则不进行匹配:
//		可开始匹配日期，正常库存状态的等于首次入库dinbounddate,
		//待检状态的 制造建 =首次入库日期+后段提前期 工作日历(节假日+休息日)  采购件 = =首次入库日期+后段提前期  工作日历 （自然日）
		if(supply.getVdef2() !=null){
			UFDate dinbounddate = new UFDate(supply.getVdef2()).asEnd();
			//
			UFDouble endahead = materialVO.getEndahead();
			int days =  endahead != null ? endahead.intValue() : 0;
			//DR=分销补货，FR=工厂补货，MR=制造件，PR=采购件，OT=委外件，ET=其他，  
			String martype = materialVO.getMartype();
			if("MR".equalsIgnoreCase(martype)){
				dinbounddate = this.getContext().getWorkDateCache().getAfterWorkDates(dinbounddate,days);
			}else{
				dinbounddate = dinbounddate.getDateAfter(days);
			}
			if (dinbounddate.after(demandEndDate)) {
				return true;
			}
		}
		
	
		return false;
	}

	/**
	 * 是否可以匹配
	 * 
	 * @param materialVO
	 * @param demand
	 * @param supply
	 * @param masterMaterialVO
	 * @return
	 */
	private boolean isKeyEqual(MaterialVO materialVO, DemandVO demand, SupplyVO supply, MaterialVO masterMaterialVO,
			List<String> fieldCodes) {
		// 结构性辅助属性
		StringBuilder d = new StringBuilder();
		StringBuilder s = new StringBuilder();
		StringBuilder nullvalue = new StringBuilder();
		for (int i = 1; i <= 10; i++) {
			String bfree = "bfree" + i;
			String vfree = "vfree" + i;
			UFBoolean freeValue = (UFBoolean) materialVO.getAttributeValue(bfree);
			// 如果是结构性辅助属性
			if (freeValue != null && freeValue.booleanValue()) {
				// 如果跟单维度包含结构性辅助属性
				if (fieldCodes.contains(vfree)) {
					Object dmdValue = demand.getAttributeValue(vfree);
					dmdValue = dmdValue != null ? dmdValue : "~";
					Object splValue = supply.getAttributeValue(vfree);
					splValue = splValue != null ? splValue : "~";
					d.append(dmdValue + "::");
					s.append(splValue + "::");
					nullvalue.append("~::");
				}
			}
		}

		// 结构性辅助属性完全相同
		if (d.toString().equals(s.toString())) {
			return true;
		}

		// 结构性辅助属性全部为空
		//if (s.toString().equals(nullvalue.toString())) {
		//	return true;
		//}

		return false;
	}

	public int getLastSupplyIndex() {
		return this.lastSupplyIndex;
	}
}

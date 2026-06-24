package nc.pubimpl.ic.statusAdjust;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nc.vo.bd.meta.BatchOperateVO;
import org.jfree.util.Log;

import sun.util.logging.resources.logging;

import nc.bs.bd.update63xto636.ClosecheckUpdate.MapListProcessor;
import nc.bs.dao.BaseDAO;
import nc.bs.ecpubapp.report.SqlUtil;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.bs.pub.taskcenter.BgWorkingContext;
import nc.bs.pub.taskcenter.IBackgroundWorkPlugin;
import nc.cmp.bill.util.SQLUtil;
import nc.impl.pubapp.env.BSContext;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.itf.cmp.dmp.IDmpSendMessage;
import nc.itf.ic.m4460.IStateAdjustMaintain;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.md.data.access.service.IMDDataAccessQueryService;
import nc.md.data.criterion.db.MapProcessor;
import nc.pubitf.ic.onhand.IOnhandQry;
import nc.pubitf.uapbd.IMaterialPubService;
import nc.uap.ws.log.NCLogger;
import nc.ui.ic.material.query.InvInfoUIQuery;
import nc.ui.pub.print.IDataSource;
import nc.vo.arap.uforeport.SqlBuffer;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.stock.MaterialStockVO;
import nc.vo.ic.m4460.entity.StateAdjustVO;
import nc.vo.ic.material.define.InvBasVO;
import nc.vo.ic.material.query.InvInfoQuery;
import nc.vo.ic.onhand.entity.OnhandDimVO;
import nc.vo.ic.onhand.entity.OnhandNumVO;
import nc.vo.ic.onhand.entity.OnhandSNVO;
import nc.vo.ic.onhand.entity.OnhandVO;
import nc.vo.ic.onhand.pub.OnhandQryCond;
import nc.vo.ic.onhand.pub.OnhandVOTools;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.NCBaseTypeUtils;
import nc.vo.ic.pub.util.VOEntityUtil;
import nc.vo.ic.pub.util.ValueCheckUtil;
import nc.vo.ic.storestate.StoreStateVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;

/**
 * 失效的批次自动调整库存状态为不合格
 * @author wumink
 * /**
				 * 后台任务  ：
				 * 			改批次号失效日期执行库存状态变更
				 * @author Administrator
				 *
				 *//**
 *
 */
@SuppressWarnings("restriction")
public class AutoAdjustForExpiredBatch implements IBackgroundWorkPlugin {
	
	//是否包含停用的批次
	private static final String INCLUDEBSEAL = "includeBseal";
	//临近失效天数，不可小于0
	private static final String DAYSBEFORE = "daysBefore";
	//待调整库存状态范围
	private static final String INCLUDESTATES = "includeStates";
	//目标库存状态
	private static final String TARGETSTATE = "targetState";
	
//	private static final String BUHEGE = "buhege";
	private static final String DAIJIAN = "daijian";
	//目标质量等级
	private static final String ZLDJ = "after_qualitylevel";

	@Override
	public PreAlertObject executeTask(BgWorkingContext context) throws BusinessException {
		Log.debug("AutoAdjustForExpiredBatch后台任务执行开始");
		//参数处理
		@SuppressWarnings("deprecation")
		String pk_group = context.getGroupId();
		String[] pk_orgs = context.getPk_orgs();
		HashMap<String, Object> params = context.getKeyMap();
		Object includeBseal = params.get(INCLUDEBSEAL);
		Object daysBefore = params.get(DAYSBEFORE);
		Object includeStates = params.get(INCLUDESTATES);
		Object targetState = params.get(TARGETSTATE);
		String zldj = (String) params.get(ZLDJ) ; /*质量等级*/
		UFBoolean includebseal = includeBseal == null ? UFBoolean.FALSE : UFBoolean.valueOf(includeBseal.toString());
		int days = daysBefore == null ? 0 : ValueUtils.getInt(daysBefore);
		if (days < 0) {
			throw new BusinessException("后台任务参数错误：【临近失效日期天数】不可小于0！！！");
		}
		if( StringUtil.isEmpty((String)targetState)){
			throw new BusinessException("后台任务部署参数错误：【TARGETSTATE】目标库存状态不能为空！！！");
		}
		StoreStateVO targetStateVO = getStoreStateByCode((String)targetState, pk_group);
		
		String[] srcStates;
		if (includeStates != null) {
			srcStates = includeStates.toString().trim().split(",");
		} else {
			throw new BusinessException("后台任务参数错误：【待调整库存状态范围】不可为空！！！");
		}
		
		//批次号查询存量的时候，数据量过大，20220524 zhian.ye 使用分页处理，否则容易超出sql限制,so 决定每次只处理50个批次号；
		BatchcodeVO[] batchcodes = getExpiredBatchcodeVO(includebseal, days,srcStates);
		List<String> pk_onhanddims  = new ArrayList<String>();
		
		while (batchcodes.length > 0) {
			String sql = getOnhandWhereSql(batchcodes, pk_group, pk_orgs, srcStates);
			OnhandVO[] onhands = getOnhandVOs(sql);
			if (onhands == null || onhands.length <= 0) {
				return getReturnObject(null);
			}
			StateAdjustVO[] stateAdjustVOs = processOnhandVO(onhands);
			if (stateAdjustVOs == null || stateAdjustVOs.length <= 0) {
				return getReturnObject(null);
			}
			
			try {
				pk_onhanddims.addAll(doStateAdjust(stateAdjustVOs, targetStateVO)) ;
				/*20200707 yezhian TO RayBow 修改：执行完成后反查批次号，
				 * 将对应批次号的质量等级全改为【过期】,支持参数设置
				 */
				if(zldj != null && !zldj.isEmpty())
				this.executeTaskAfutToBatchcode(batchcodes, zldj);
				
			} catch (Exception e) {
			throw new	BusinessException("后台任务错误：批次号变更失败，请联系技术人员！！！");
			}
			batchcodes = getExpiredBatchcodeVO(includebseal, days,srcStates);
		}
		
		
		
		
	
		return getReturnObject(pk_onhanddims.toArray(new String[0]));
		
	}
	/**
	 * 
	 * @param onhanddims  维度主键
	 * @param zldj 调整后的质量等级
	 * @return
	 * @throws BusinessException 
	 */
	private BatchOperateVO executeTaskAfutToBatchcode(BatchcodeVO[] batchcodes, String zldj) throws BusinessException{
		
		if(null == batchcodes || batchcodes.length == 0){
			return null;  //无参数接收直接返回
		}

		//设置批次号内的质量等级
		for(BatchcodeVO batchcode : batchcodes){
			batchcode.setCqualitylevelid(zldj);
		}
		BatchOperateVO batchOperate = new BatchOperateVO();
		batchOperate.setUpdObjs(batchcodes);
		SmartServiceImpl serve = new SmartServiceImpl();
		BatchOperateVO batchSave = serve.batchSave(batchOperate);
//		batchOperate.set
		return batchSave;
		
	}
	
	protected PreAlertObject getReturnObject(String[] pk_onhanddims) throws BusinessException {
		PreAlertObject resultObject = new PreAlertObject();
		AutoAdjustForExpiredBatchDataSource datasource = new AutoAdjustForExpiredBatchDataSource();
		resultObject.setReturnObj(datasource);
		resultObject.setReturnType(PreAlertReturnType.RETURNDATASOURCE);
		if (pk_onhanddims == null || pk_onhanddims.length <= 0) {
			return resultObject;
		} 
		IOnhandQry onhandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		OnhandDimVO[] onhandDims = onhandquery.queryOnhandDim(pk_onhanddims);
		if (onhandDims == null || onhandDims.length <= 0) {
			return resultObject;
		}
		Set<String> pk_materials = new HashSet<>();
		Set<String> pk_batchcodes = new HashSet<>();
		Map<String, OnhandDimVO> onhandMap = new HashMap<>();
		for (int i = 0; i < onhandDims.length; i++) {
			pk_materials.add(onhandDims[i].getCmaterialvid());
			pk_batchcodes.add(onhandDims[i].getPk_batchcode());
			onhandMap.put(onhandDims[i].getPk_onhanddim(), onhandDims[i]);
		}
		IMaterialPubService service = NCLocator.getInstance().lookup(IMaterialPubService.class);
		Map<String, MaterialVO> materialMap = service.queryMaterialBaseInfoByPks(pk_materials.toArray(new String[0]), 
				new String[]{MaterialVO.CODE, MaterialVO.NAME});
		VOQuery<BatchcodeVO> query = new VOQuery<>(BatchcodeVO.class);
		BatchcodeVO[] batchcodeVOs = pk_batchcodes == null || pk_batchcodes.size() <= 0 ? null : query.query(pk_batchcodes.toArray(new String[0]));
		Map<String, BatchcodeVO> batchcodeMap = new HashMap<>();
		int len = batchcodeVOs == null ? 0 : batchcodeVOs.length;
		for (int i = 0; i < len; i++) {
			batchcodeMap.put(batchcodeVOs[i].getPk_batchcode(), batchcodeVOs[i]);
		}
		Set<Entry<String, OnhandDimVO>> onhandEntrys = onhandMap.entrySet();
		List<String> materialpks = new ArrayList<>();
		List<String> materialcodes = new ArrayList<>();
		List<String> materialnames = new ArrayList<>();
		List<String> batchcodepks = new ArrayList<>();
		List<String> batchcodes = new ArrayList<>();
		List<String> expireddates = new ArrayList<>();
		for (Entry<String, OnhandDimVO> entry : onhandEntrys) {
			String pk_material = entry.getValue().getCmaterialvid();
			String pk_batchcode = entry.getValue().getPk_batchcode();
			materialpks.add(pk_material);
			materialcodes.add(materialMap.get(pk_material).getCode());
			materialnames.add(materialMap.get(pk_material).getName());
			batchcodepks.add(pk_batchcode);
			batchcodes.add(batchcodeMap.get(pk_batchcode).getVbatchcode());
			expireddates.add(batchcodeMap.get(pk_batchcode).getDvalidate().toStdString());
		}
		datasource.setMaterialCode(materialcodes.toArray(new String[0]));
		datasource.setMaterialName(materialnames.toArray(new String[0]));
		datasource.setBatchcode(batchcodes.toArray(new String[0]));
		datasource.setPKMaterialCode(materialpks.toArray(new String[0]));
		datasource.setPKBatchcode(batchcodepks.toArray(new String[0]));
		datasource.setExpiredDate(expireddates.toArray(new String[0]));
		
		return resultObject;
	}

	/**
	 * 获取效期失效的批次号档案
	 * @param srcStates 
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private BatchcodeVO[] getExpiredBatchcodeVO(UFBoolean includeBseal, int daysBefore, String[] srcStates) throws BusinessException {
		UFDate criticalDate = BSContext.getInstance().getDate().getDateBefore(daysBefore);
		StringBuilder sql = new StringBuilder();
		sql.append(" and exists (");
		sql.append("    select 1 from bd_material_v material  ");
		sql.append(" 	where material.pk_material = cmaterialoid ");
		sql.append(" 	and isnull(material.dr, 0) = 0 ");
		sql.append(" 	and isnull(material.enablestate, 2) = 2 ");
		sql.append(" )");
		sql.append(" and EXISTS (select 1 ");
		sql.append(" from IC_ONHANDDIM  ");
		sql.append(" left join IC_ONHANDNUM on IC_ONHANDDIM.PK_ONHANDDIM = IC_ONHANDNUM.PK_ONHANDDIM ");
		sql.append(" where IC_ONHANDDIM.PK_BATCHCODE =  scm_batchcode.PK_BATCHCODE and (IC_ONHANDNUM.nonhandnum > 0 or IC_ONHANDNUM.nonhandastnum > 0) ");
		sql.append(" and "+nc.vo.pf.pub.util.SQLUtil.buildSqlForIn("IC_ONHANDDIM.cstateid", srcStates)+" ");
		sql.append(" and nvl(IC_ONHANDDIM.dr,0) = 0 and nvl(IC_ONHANDNUM.dr,0)=0   ) " );
		sql.append(" and substr(dvalidate, 0, 10) < '").append(criticalDate).append("'");
		sql.append(" and dvalidate is not null");
		if (!includeBseal.booleanValue()) {
			sql.append(" and bseal = 'N'");
		}
		//sql.append("and ROWNUM < 50");
		
		VOQuery<BatchcodeVO> query = new VOQuery<>(BatchcodeVO.class);
		BatchcodeVO[] batchcodes = query.query(sql.toString(), null);
		return batchcodes;
	}

	/**
	 * 进行库存状态调整
	 * @param bills 库存状态调整记录
	 * @param targetStateVO 目标库存状态
	 * @return String[] 已调整的存量维度pk
	 * @throws BusinessException
	 */
	private Set<String> doStateAdjust(StateAdjustVO[] bills, StoreStateVO targetStateVO) throws BusinessException {
		if(bills == null || bills.length <= 0){
			return null;
		}
		for (StateAdjustVO stateAdjust : bills) {
			setDefaultValue(stateAdjust, targetStateVO);
		}
		Set<String> onhanddims = new HashSet<>();
		IStateAdjustMaintain stateAdjustService = NCLocator.getInstance().lookup(IStateAdjustMaintain.class);
		for (StateAdjustVO stateAdjust : bills) {
			try {
				stateAdjustService.stateAdjust(new StateAdjustVO[] { stateAdjust });
				if (!onhanddims.contains(stateAdjust.getPk_onhanddim())) {
					onhanddims.add(stateAdjust.getPk_onhanddim());
				}
			} catch (Exception e) {
				continue;
			}
		}
		return onhanddims;
	}

	/**
	 * 设置库存状态调整记录的默认值
	 * @param stateAdjustVO 库存状态调整记录
	 * @param targetStateVO 调整后库存状态
	 * @throws BusinessException
	 */
	public void setDefaultValue(StateAdjustVO stateAdjustVO, StoreStateVO targetStateVO) throws BusinessException {
		// 库存现存量
		UFDouble nnum = stateAdjustVO.getNnum() != null ? stateAdjustVO.getNnum() : UFDouble.ZERO_DBL;
		UFDouble nassistnum = stateAdjustVO.getNassistnum() != null ? stateAdjustVO.getNassistnum() : UFDouble.ZERO_DBL;
		UFDouble nlocknum = stateAdjustVO.getNlocknum() != null ? stateAdjustVO.getNlocknum() : UFDouble.ZERO_DBL;
		UFDouble nlockassistnum = stateAdjustVO.getNlockassistnum() != null ? stateAdjustVO.getNlockassistnum() : UFDouble.ZERO_DBL;
		// 设置调整数量现存量
		stateAdjustVO.setNadjustassistnum(nassistnum.sub(nlockassistnum));
		stateAdjustVO.setNadjustnum(nnum.sub(nlocknum));
		// 设置调整人
		stateAdjustVO.setBillmaker("NC_USER0000000000000");
		// 设置调整状态
		stateAdjustVO.setCadjuststateid(targetStateVO.getPk_storestate());
		// 设置调整时间
		stateAdjustVO.setCreationtime(AppContext.getInstance().getServerTime());
	}

	/**
	 * 获取查询物料结存记录的sql
	 * @param batchcodes 批次号数组
	 * @param pk_group
	 * @param pk_orgs
	 * @param includeStates 被调整的库存状态
	 * @return
	 */
	public String getOnhandWhereSql(BatchcodeVO[] batchcodes, String pk_group, String[] pk_orgs, String[] includeStates) {
		SqlBuffer sql = new SqlBuffer();
		sql.append(" 1=1 ");
		if (pk_orgs != null && pk_orgs.length > 0) {
			sql.append(" and ");
			sql.append(ICPubMetaNameConst.PK_ORG, pk_orgs);
		}
		if (batchcodes == null || batchcodes.length <= 0) {
			return sql.toString();
		} 
		List<String> pk_batchcodes = new ArrayList<>();
		for (int i = 0; i < batchcodes.length; i++) {
			pk_batchcodes.add(batchcodes[i].getPk_batchcode());
		}
		if (pk_batchcodes != null && pk_batchcodes.size() > 0) {
			sql.append(" and ");
			sql.append(ICPubMetaNameConst.PK_BATCHCODE, pk_batchcodes.toArray(new String[0]));
		}
		if (includeStates != null && includeStates.length > 0) {
			sql.append(" and ");
			sql.append(ICPubMetaNameConst.CSTATEID, includeStates);
		}
				//sql.append(" and vbatchcode ='CNT302' ");
		return sql.toString();
	}

	/**
	 * 方法功能描述：根据编码,查询待检库存状态vo
	 * @throws BusinessException 
	 * 
	 */
	private StoreStateVO getStoreStateByCode(String code, String pk_group) throws BusinessException {
		VOQuery<StoreStateVO> query = new VOQuery<>(StoreStateVO.class);
		StringBuilder sql = new StringBuilder();
		sql.append("  and " + StoreStateVO.PK_GROUP + " = '" + pk_group + "'");
		sql.append(" and ic_storestate." + StoreStateVO.PK_STORESTATE + " = '" + code + "'");
		StoreStateVO[] states = query.query(sql.toString(), null);
		if (states == null || states.length <= 0) {
			throw new BusinessException("无法查询到编码为【" + code + "】的库存状态！！！");
		}
		return states[0];
	}

	/**
	 * 根据条件获取物料结存记录
	 * @throws BusinessException 
	 */
	private OnhandVO[] getOnhandVOs(String where) throws BusinessException {
		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		// 通过查询条件，返回现存量
		OnhandQryCond cond = new OnhandQryCond();
		cond.setISSum(true);
		cond.addSelectFields(CollectionUtils.combineArrs(
				OnhandDimVO.getDimContentFields(),
				new String[] { OnhandDimVO.PK_ONHANDDIM }));// 加入现存量维度及现存量维度pk
		cond.setWhere(where.toString());
		OnhandVO[] onhands = onbandquery.queryOnhand(cond);/*yezhian*/
		return onhands;
	}

	

	/**
	 * 处理查询出来的物料结存记录
	 * 
	 * @param onhandvos
	 * @throws BusinessException
	 */
	private StateAdjustVO[] processOnhandVO(OnhandVO[] onhandvos) throws BusinessException {
		// 根据单品信息过滤掉现存量记录，V60暂不支持库存状态调整
		OnhandVO[] rets = this.filterOnHandBySN(onhandvos);
		StateAdjustVO[] bills = this.translateResult(rets);
		return bills;
	}
	
	/**
	 * 根据单品信息过滤掉现存量记录
	 * @param resultVOs
	 * @return
	 * @throws BusinessException
	 */
	BaseDAO dao = new BaseDAO();
	private OnhandVO[] filterOnHandBySN(OnhandVO[] resultVOs) throws BusinessException {
		String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs, OnhandDimVO.PK_ONHANDDIM);
		OnhandSNVO[] vos = NCLocator.getInstance().lookup(IOnhandQry.class).queryOnhandSNVOByDimPK(pk_dims);
		if (ValueCheckUtil.isNullORZeroLength(vos)) {
			return resultVOs;
		}
		Set<String> dims = VOEntityUtil.getVOsValueSet(vos, OnhandDimVO.PK_ONHANDDIM);
		List<OnhandVO> ret = new ArrayList<OnhandVO>();
		for (OnhandVO vo : resultVOs) {
			String cmaterialvid = vo.getCmaterialvid();
			
			String sql = " select 1 from bd_materialstock where pk_material = '"+cmaterialvid+"' and pk_org = '"+vo.getPk_org()+"'" +
					" and serialmanaflag = 'Y'";
			Object executeQuery = dao.executeQuery(sql, new ArrayProcessor());
			if (!dims.contains(vo.getPk_onhanddim()) && executeQuery != null
					) {
				continue;
			}
			ret.add(vo);
		
		}
		return CollectionUtils.listToArray(ret);
	}
	

	/**
	 * 将现存量记录转化为库存状态调整单
	 * 
	 * @param resultVOs
	 * @return
	 * @throws BusinessException
	 */
	private StateAdjustVO[] translateResult(OnhandVO[] resultVOs)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}
		Map<String, InvBasVO> map = this.getInvBasMap(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(map)) {
			return null;
		}
		Map<String, UFDateTime> tsMap = this.getOnhandnumTS(resultVOs);
		if (ValueCheckUtil.isNullORZeroLength(tsMap)) {
			return null;
		}
		InvBasVO inv = null;
		List<StateAdjustVO> billList = new ArrayList<StateAdjustVO>();
		for (OnhandVO onhandVO : resultVOs) {
			inv = map.get(onhandVO.getCmaterialvid());
			// 物料管理属性：库存状态管理；
			if (inv == null || !inv.getFix1().booleanValue()) {
				continue;
			}
			// 结存主数量-冻结数量>0；预留主数量，冻结数量满足至少有一个为空或零
			if (tsMap.get(onhandVO.getPk_onhanddim()) == null || !canStateAdjust(onhandVO)) {
				continue;
			}

			StateAdjustVO vo = new StateAdjustVO();
			vo.setPk_onhanddim(onhandVO.getPk_onhanddim());
			vo.setPk_group(onhandVO.getPk_group());
			vo.setPk_org(onhandVO.getPk_org());// 库存组织，方便生成单据号
			vo.setNnum(onhandVO.getNonhandnum());// 结存主数量
			vo.setNassistnum(onhandVO.getNonhandastnum());// 结存数量
			vo.setNgrossnum(onhandVO.getNgrossnum());// 毛重
			vo.setNrsnum(onhandVO.getNrsnum());// 预留主数量
			vo.setNlocknum(onhandVO.getNlocknum());// 冻结主数量
			vo.setNlockassistnum(onhandVO.getNlockastnum());// 冻结数量
			vo.setOnhandnumts(tsMap.get(onhandVO.getPk_onhanddim()));// 现存量时间戳
			vo.setVchangerate(onhandVO.getVchangerate());// 换算率；

			for (int i = 1; i < 11; i++) {
				vo.setAttributeValue("vfree" + i, onhandVO.getAttributeValue("vfree" + i));
			}

			vo.setCunitid(inv.getPk_measdoc());// 主单位，方便处理精度
			vo.setCastunitid(onhandVO.getCastunitid());// 单位，方便处理精度
			billList.add(vo);
		}
		return billList.toArray(new StateAdjustVO[0]);
	}

	/**
	 * 现存量数量方面是否符合状态调整
	 * 
	 * @param onhandVO
	 * @return
	 */
	private boolean canStateAdjust(OnhandVO onhandVO) {
		UFDouble nlocknum = onhandVO.getNlocknum() == null ? UFDouble.ZERO_DBL : onhandVO.getNlocknum();
		UFDouble nonhandnum = onhandVO.getNonhandnum() == null ? UFDouble.ZERO_DBL : onhandVO.getNonhandnum();
		//------
		if(onhandVO.getAttributeValue("vbatchcode").equals("CNT302")){
			Log.debug(onhandVO.getAttributeValue("vbatchcode"));
		}
		if (nonhandnum.compareTo(nlocknum) <= 0) {
			return false;
		}
		if (NCBaseTypeUtils.isGtZero(onhandVO.getNlocknum())
				&& NCBaseTypeUtils.isGtZero(onhandVO.getNrsnum())) {
			return false;
		}
		if (NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandnum())
				&& NCBaseTypeUtils.isNullOrZero(onhandVO.getNonhandastnum())
				&& NCBaseTypeUtils.isNullOrZero(onhandVO.getNgrossnum())) {
			return false;
		}
		return true;
	}

	/**
	 * 物料是否库存状态管理，现存量换算率处理
	 * 
	 * @param resultVOs
	 * @return
	 */
	private Map<String, InvBasVO> getInvBasMap(OnhandVO[] resultVOs) {
		String[] cmaterialvids = VOEntityUtil.getVOsValues(resultVOs,
				ICPubMetaNameConst.CMATERIALVID, String.class);
		InvInfoQuery invInfoQuery = InvInfoUIQuery.getInstance().getInvInfoQuery();
		InvBasVO[] invvos = invInfoQuery.getInvBasVO(cmaterialvids);
		if (ValueCheckUtil.isNullORZeroLength(invvos)) {
			return null;
		}
		// 现存量换算率处理
		OnhandVOTools.fillOnhandVChangerate(resultVOs, invInfoQuery);

		Map<String, InvBasVO> map = new HashMap<String, InvBasVO>();
		for (InvBasVO vo : invvos) {
			map.put(vo.getPk_material(), vo);
		}
		return map;
	}

	/**
	 * 现存量的ts
	 * 
	 * @param pk_dims
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, UFDateTime> getOnhandnumTS(OnhandVO[] resultVOs)
			throws BusinessException {
		if (ValueCheckUtil.isNullORZeroLength(resultVOs)) {
			return null;
		}

		String[] pk_dims = VOEntityUtil.getVOsNotRepeatValue(resultVOs, OnhandDimVO.PK_ONHANDDIM);

		// 现存量查询接口
		IOnhandQry onbandquery = NCLocator.getInstance().lookup(IOnhandQry.class);
		OnhandNumVO[] numvos = onbandquery.queryOnhandNumByDim(pk_dims);
		if (ValueCheckUtil.isNullORZeroLength(numvos)) {
			return null;
		}
		Map<String, UFDateTime> map = new HashMap<String, UFDateTime>();
		for (OnhandNumVO vo : numvos) {
			map.put(vo.getPk_onhanddim(), vo.getTs());
		}
		return map;
	}
	
	public class AutoAdjustForExpiredBatchDataSource implements IDataSource {

		private static final long serialVersionUID = 8543547808997920885L;
		
		private static final String PK_MATERIAL = "pk_material";
		private static final String MATERIALCODE = "materialcode";
		private static final String MATERIALNAME = "materialname";
		private static final String PK_BATCHCODE = "pk_batchcode";
		private static final String BATCHCODE = "batchcode";
		private static final String EXPIREDDATE = "expireddate";
		
		private HashMap<String, String[]> dataMap = new HashMap<>();
		
		public void setPKMaterialCode(String[] values) {
			this.dataMap.put(PK_MATERIAL, values);
		}
		
		public void setMaterialCode(String[] values) {
			this.dataMap.put(MATERIALCODE, values);
		}
		
		public void setMaterialName(String[] values) {
			this.dataMap.put(MATERIALNAME, values);
		}
		
		public void setPKBatchcode(String[] values) {
			this.dataMap.put(PK_BATCHCODE, values);
		}
		
		public void setBatchcode(String[] values) {
			this.dataMap.put(BATCHCODE, values);
		}
		
		public void setExpiredDate(String[] values) {
			this.dataMap.put(EXPIREDDATE, values);
		}

		@Override
		public String[] getItemValuesByExpress(String itemExpress) {
			return this.dataMap.get(itemExpress);
		}

		@Override
		public boolean isNumber(String itemExpress) {
			return false;
		}

		@Override
		public String[] getDependentItemExpressByExpress(String itemExpress) {
			return null;
		}

		@Override
		public String[] getAllDataItemExpress() {
			return new String[] {
					PK_MATERIAL, MATERIALCODE, MATERIALNAME, PK_BATCHCODE, BATCHCODE, EXPIREDDATE 
			};
		}

		@Override
		public String[] getAllDataItemNames() {
			return null;
		}

		@Override
		public String getModuleName() {
			return null;
		}
		
	}

}

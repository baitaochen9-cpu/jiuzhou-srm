/*
 * @(#)PointCheck4OutService.java V60
 *
 * Copyright 2010 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.impl.emm.dailypc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.am.framework.action.IActionTemplate;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.uif2.validation.Validator;
import nc.impl.am.bill.BillBaseDAO;
import nc.impl.am.bill.rule.ValidateServiceRule;
import nc.impl.am.common.InSqlManager;
import nc.impl.am.db.QueryUtil;
import nc.impl.am.db.VOPersistUtil;
import nc.impl.eampub.validator.EquipValidator;
import nc.impl.emm.dailypc.rule.WriteBackSrcBill;
import nc.itf.aim.pub.IEquipService;
import nc.itf.emm.prv.IDailyPC;
import nc.itf.emm.pub.IPCOutService;
import nc.itf.emm.pub.IPCPubService;
import nc.itf.emm.pub.ParameterVO;
import nc.itf.org.IOrgConst;
import nc.itf.uap.pf.IPFBillItfDef;
import nc.vo.aim.equip.EquipHeadVO;
import nc.vo.am.common.BaseLockData;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.AssertUtils;
import nc.vo.am.common.util.MapUtils;
import nc.vo.am.common.util.OrgUtils;
import nc.vo.am.common.util.StringUtils;
import nc.vo.am.constant.BillStatusConst;
import nc.vo.am.constant.BillTypeConst_4B;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.constant.CommonKeyConst4EAM;
import nc.vo.am.manager.LockManager;
import nc.vo.am.proxy.AMProxy;
import nc.vo.emm.dailypc.AggDailyPCVO;
import nc.vo.emm.dailypc.DailyPCBodyVO;
import nc.vo.emm.dailypc.DailyPCHeadVO;
import nc.vo.emm.dailypc.DisposeResult;
import nc.vo.emm.pcstd.PCStdBodyVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.BillItfDefVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.util.AuditInfoUtil;

/**
 * 点检记录对外服务实现类
 * @author 
 *
 */
public class PointCheck4OutService extends BillBaseDAO<AggDailyPCVO> implements IPCOutService, IPCPubService {

//	private final static String Default_RelationOrg_Msg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0030")/*@res "没能获取有权限的资产组织，不能进行设备有效性检验,请检查业务委托关系 "*/;
	/**
	 * 交易类型的连接符，是指transi_type和PK_transitype的连接符
	 */
	public final static String SEPERTOR = "#";

	/**
	 * 推式生成点检的规则校验
	 */
	private ValidateServiceRule<AggDailyPCVO> validationRule = null;

	@Override
	public void closePCbodyVO(AggDailyPCVO... billVOs) throws BusinessException {
		if (ArrayUtils.isEmpty(billVOs)) {
			return;
		}
		// 加锁
		lockDatas(billVOs);
		// 检查表体是否有在未处理状态

		if (!checkPCbodyVOS(DisposeResult.UNSETTLED, billVOs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0031")/*@res "单据无法关闭，因为单据表体的处理结果不是未处理"*/);
		}
		// 更新表体为己经关闭状态
		updateDisposeResult(DisposeResult.CLOSED, billVOs);
	}

	@Override
	public AggDailyPCVO[] openPCbodyVO(AggDailyPCVO... billVOs) throws BusinessException {
		if (ArrayUtils.isEmpty(billVOs)) {
			return null;
		}
		// 加锁
		lockDatas(billVOs);
		// 检查表体是否有己经关闭的单据
		if (!checkPCbodyVOS(DisposeResult.CLOSED, billVOs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0032")/*@res "单据无法打开，因为单据表体的处理结果不是关闭"*/);
		}
		// 更新表体为未处理状态
		return updateDisposeResult(DisposeResult.UNSETTLED, billVOs);
	}

	/**
	 * 检查点检表体是否符合所要处理的状态
	 *
	 * @param disp_result
	 * @param billVOs
	 * @return
	 * @throws BusinessException
	 */
	private boolean checkPCbodyVOS(final Integer disp_result, AggDailyPCVO... billVOs) throws BusinessException {
		// 最后验证是否通过
		for (AggDailyPCVO aggVo : billVOs) {
			SuperVO[] bodyVOS = aggVo.getAllChildrenVO();
			if (ArrayUtils.isNotEmpty(bodyVOS)) {
				for (SuperVO bodyVo : bodyVOS) {
					// 验证处理结果
					Integer db_disp = (Integer) bodyVo.getAttributeValue(DailyPCBodyVO.DISP_RESULT);
					if (!disp_result.equals(db_disp)) {
						return false;
					}
				}
			} else {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0033")/*@res "单据的表体为空"*/);
			}

		}
		return true;
	}

	/**
	 * 加锁
	 *
	 * @param BillVOs
	 * @throws BusinessException
	 */
	private void lockDatas(AggDailyPCVO... BillVOs) throws BusinessException {
		BaseLockData<AggDailyPCVO> toLockData = new BaseLockData<AggDailyPCVO>(BillVOs);
		LockManager.lock(toLockData, new String[] { CommonKeyConst.ts }, null);
	}

	/**
	 * 更新到数据库
	 *
	 * @param billVOs
	 * @throws BusinessException
	 */
	private AggDailyPCVO[] updateAggVOS(AggDailyPCVO... billVOs) throws BusinessException {
		List<ISuperVO> headList = new ArrayList<ISuperVO>();
		List<ISuperVO> bodyList = new ArrayList<ISuperVO>();
		for (AggDailyPCVO tempAGG : billVOs) {
			headList.add(tempAGG.getParent());
			bodyList.addAll(Arrays.asList(tempAGG.getAllChildrenVO()));
		}
		BaseDAO perUtil = new BaseDAO();
		// 更新表头的TS
		perUtil.updateVOArray(headList.toArray(new SuperVO[0]), new String[] { DailyPCHeadVO.PK_POINTCHECK });
		// 更新表体
		perUtil.updateVOArray(bodyList.toArray(new SuperVO[0]), new String[] { DailyPCBodyVO.DISP_RESULT,
				DailyPCBodyVO.PK_DISPOSER, DailyPCBodyVO.DISP_TIME, DailyPCBodyVO.DEST_BILL_TYPE,
				DailyPCBodyVO.DEST_PK_BILL, DailyPCBodyVO.DEST_TRANSITYPE });
		return null;
	}

	/**
	 *新增保存到数据库
	 *
	 * @param billVOs
	 * @throws BusinessException
	 */
	private AggDailyPCVO[] insertAggVOS(AggDailyPCVO... billVOs) throws BusinessException {
		// 更新设备上的位置
		updateEquip(billVOs);
		// 生成校验的规则
		genValidationRule(billVOs);
		return pushSaveBillVO(billVOs);
	}

	/**
	 * 注册推式保存前后的扩展动作
	 *
	 * @param insertAction
	 */
	@Override
	protected void initPushSaveActionRules(IActionTemplate<AggDailyPCVO> template) {
		super.initPushSaveActionRules(template);
		//设备状态处理规则放在设备校验规则前
//		template.addBeforeRule(new EquipStatusProcessRule());
		// 如果规则不为空再去校验
		if (validationRule != null) {
			template.addBeforeRule(validationRule);
		}
		template.addAfterRule(new WriteBackSrcBill());
	}

	/**
	 * 更新处理结果
	 *
	 * @param isClosed
	 * @param billVOs
	 * @throws BusinessException
	 */
	private AggDailyPCVO[] updateDisposeResult(final Integer disp_result, AggDailyPCVO... billVOs)
			throws BusinessException {
		for (AggDailyPCVO aggVo : billVOs) {
			SuperVO[] bodyVOS = aggVo.getAllChildrenVO();
			if (bodyVOS != null && bodyVOS.length > 0) {
				for (SuperVO bodyVo : bodyVOS) {
					bodyVo.setStatus(VOStatus.UPDATED);
					// 更新处理结果，处理人，处理时间
					bodyVo.setAttributeValue(DailyPCBodyVO.DISP_RESULT, disp_result);
					bodyVo.setAttributeValue(DailyPCBodyVO.PK_DISPOSER, AuditInfoUtil.getCurrentUser());
					bodyVo.setAttributeValue(DailyPCBodyVO.DISP_TIME, getBuziTime());
				}
			}
			aggVo.getParent().setStatus(VOStatus.UPDATED);
		}
		// 更新到数据库
		return updateAggVOS(billVOs);
	}

	@Override
	public AggDailyPCVO[] repeatCheckPoint(String pk_org, AggDailyPCVO... billVOs) throws BusinessException {

		if (ArrayUtils.isEmpty(billVOs)) {
			return null;
		}
		// 加锁
		lockDatas(billVOs);
		// 检查表体是否是未处理的单据
		if (!checkPCbodyVOS(DisposeResult.UNSETTLED, billVOs)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0034")/*@res "单据无法复检，因为单据表体的处理结果不是未处理"*/);
		}
		// 生成复检
		AggDailyPCVO[] destAggVOs = generateRepeateCheckVo(pk_org, billVOs);

		writeDestInfoToSrcBills(destAggVOs, billVOs);
		// 更新表体为复检状态
		return updateDisposeResult(DisposeResult.REPEAT_POINTCHECK, billVOs);
	}

	/**
	 * 将目的单据的信息写到来源单据中
	 *
	 * @param destAggVOs
	 * @param srcBillVOs
	 */
	private void writeDestInfoToSrcBills(AggDailyPCVO[] destAggVOs, AggDailyPCVO... srcBillVOs) {
		// key为上游单据表头PK，Value为下游单据表头PK
		Map<Object, Object> pks = new HashMap<Object, Object>();
		// key为上游单据表头PK，Value为下游单据交易类型
		Map<Object, Object> transi_map = new HashMap<Object,Object>();
		for(AggDailyPCVO destAggVO : destAggVOs){
			SuperVO[] bodyVOS = destAggVO.getChildrenVO();
			Object transitype = destAggVO.getParent().getAttributeValue(DailyPCHeadVO.TRANSI_TYPE);
			for (SuperVO tempVO : bodyVOS) {
				pks.put(tempVO.getAttributeValue(DailyPCBodyVO.SRC_PK_BILL), tempVO
						.getAttributeValue(DailyPCBodyVO.PK_POINTCHECK));
				transi_map.put(tempVO.getAttributeValue(DailyPCBodyVO.SRC_PK_BILL), transitype);
			}
		}
		
		// 将目的单据信息写入来源单据
		Object dest_bill_type = destAggVOs[0].getParent().getAttributeValue(DailyPCHeadVO.BILL_TYPE);
		for (AggDailyPCVO aggVO : srcBillVOs) {
			String pk_h = aggVO.getPrimaryKey();
			if (pks.keySet().contains(pk_h)) {
				SuperVO[] srcbodyVOS = aggVO.getAllChildrenVO();
				for (SuperVO tempBodyVO : srcbodyVOS) {
					tempBodyVO.setAttributeValue(DailyPCBodyVO.DEST_BILL_TYPE, dest_bill_type);
					tempBodyVO.setAttributeValue(DailyPCBodyVO.DEST_PK_BILL, pks.get(pk_h));
					tempBodyVO.setAttributeValue(DailyPCBodyVO.DEST_TRANSITYPE, transi_map.get(pk_h));
				}
			}
		}
	}

	/**
	 * 生成复检记录
	 *
	 * @param pk_org
	 * @param billVOs
	 * @throws BusinessException
	 *             <p>
	 *             该方法需要注意：
	 *             <p>
	 *             1、日常点检到日常点检下游单据直接复制上游的交易类型
	 *             <p>
	 *             2、专业点检到专业点检下游单据直接复制上游的交易类型
	 *             <p>
	 *             3、日常点检到专业点检下游单据的交易类型需要根据单据接口关系确定
	 *             <p>
	 */
	private AggDailyPCVO[] generateRepeateCheckVo(String pk_org, AggDailyPCVO... billVOs) throws BusinessException {
		// 这种克隆方式得到的结果，好像还不是深度克隆后的聚合VO，对表体部分的修改还是会影响到源VO
		// AggDailyPCVO[] cloneVOS = (AggDailyPCVO[])
		// CloneUtil.deepClone(billVOs);
		// 获取目的单据类型、主组织以及集团，用于批量生产单据号
		String bill_type;
		String pk_org_dest;
		String pk_group;
		Map<String, String> src2Dest = null;

		
		pk_group = (String) billVOs[0].getParentVO().getAttributeValue(CommonKeyConst4EAM.pk_group);
		List<AggDailyPCVO> cloneVOs = new ArrayList<AggDailyPCVO>();
		
		for (AggDailyPCVO aggVO : billVOs) {
			AggDailyPCVO tempvo = (AggDailyPCVO) aggVO.clone();
			DailyPCHeadVO headVO = (DailyPCHeadVO) tempvo.getParent();
			SuperVO[] bodyVOS = tempvo.getAllChildrenVO();
			for (SuperVO bodyVO : bodyVOS) {
				// 设置来源信息
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_PK_BILL, headVO.getPrimaryKey());
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_PK_BILL_B, bodyVO.getPrimaryKey());
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_BILL_TYPE, headVO.getBill_type());
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_PK_GROUP, headVO.getPk_group());
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_PK_ORG, headVO.getPk_org());
				bodyVO.setAttributeValue(DailyPCBodyVO.SRC_TRANSITYPE, headVO.getTransi_type());
				// 初始化信息
				bodyVO.setAttributeValue(DailyPCBodyVO.DISP_RESULT, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.PK_DISPOSER, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.DISP_TIME, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.EXCEPTION_FLAG, UFBoolean.FALSE);
				bodyVO.setAttributeValue(DailyPCBodyVO.PK_PCRESULT, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.PCMEAS_RESULT, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.PK_PCUSER, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.PC_TIME, null);
				bodyVO.setAttributeValue(DailyPCBodyVO.MEMO, null);
				bodyVO.setPrimaryKey(null);
				// 如果日常点检复检为专业点检且日常点检中引用了组织级的点检标准，则复检生成的专业点检的点检标准字段清空
				String pk_pcstd_b = (String) bodyVO.getAttributeValue(DailyPCBodyVO.PK_PCSTD_B);
				if(StringUtils.isNotEmpty(pk_pcstd_b) && pk_pcstd_b != null && BillTypeConst_4B.DAILYPC.equals(headVO.getBill_type()) 
						&& !StringUtil.isEmptyWithTrim(pk_org)) {
					PCStdBodyVO[] pcstdBodyVO = QueryUtil.querySuperVOByPks(PCStdBodyVO.class, 
							new String[]{"pk_org"}, new String[]{pk_pcstd_b});
					// 检查引用的点检标准是否为组织级，是则清空点检标准字段
					if (OrgUtils.checkOrgUnitType(pcstdBodyVO[0].getPk_org(), IOrgConst.ASSETORGTYPE)) {
						bodyVO.setAttributeValue(DailyPCBodyVO.PK_PCSTD_B, null);
					}
				}
			}
			if (!BillTypeConst_4B.SPECPC.equals((String)aggVO.getParentVO().getAttributeValue(CommonKeyConst4EAM.bill_type))&&!StringUtil.isEmptyWithTrim(pk_org)) {
				// 单据类型为专业点检时直接设置下游交易类型，如果传入组织不为空，表示需要生成专业点检
				bill_type = BillTypeConst_4B.SPECPC;
				src2Dest = getDestTransiType((DailyPCHeadVO) aggVO.getParentVO(), bill_type);
				pk_org_dest = pk_org;

				if (src2Dest == null) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0035")/*@res "没有找到目的交易类型"*/);
				}
				// 如果pk_org不为空，则需要重新设备交易规则
				// 根据来源交易类型取目的交易类型
				String destTransiType = src2Dest.get(headVO.getTransi_type());
				if (destTransiType == null) {
					// 如果没有指定的交易类型，则取默认的交易类型
					destTransiType = src2Dest.get(SEPERTOR);
					if (destTransiType == null) {
						throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0035")/*@res "没有找到目的交易类型"*/);
					}
				}
				headVO.setTransi_type(destTransiType.split(SEPERTOR)[0]);
				headVO.setPk_transitype(destTransiType.split(SEPERTOR)[1]);
			}else {
				bill_type = (String) billVOs[0].getParentVO().getAttributeValue(CommonKeyConst4EAM.bill_type);
				pk_org_dest = StringUtils.isEmpty(pk_org) ? ((String) billVOs[0].getParentVO().getAttributeValue(CommonKeyConst4EAM.pk_org)) : pk_org;
			}
			String pk_org_v = OrgUtils.getCurVidByPkOrg(pk_org_dest);


			// 设置表头信息
			headVO.setPk_group(pk_group);
			headVO.setPk_org(pk_org_dest);
			headVO.setPk_org_v(pk_org_v);
			headVO.setBill_type(bill_type);
			headVO.setBill_status(BillStatusConst.free_check);
			headVO.setAttributeValue(DailyPCHeadVO.BILLMAKER, InvocationInfoProxy.getInstance().getUserId());
			headVO.setAttributeValue(DailyPCHeadVO.BILLMAKETIME, getBuziTime());
			headVO.setBusi_type(null);
			
			// 初始信息
			headVO.setDisbug_status(null);
			headVO.setPc_opinion(null);
			headVO.setPc_circs(null);
			headVO.setMemo(null);

			headVO.setModifier(null);
			headVO.setModifiedtime(null);
			headVO.setAuditor(null);
			headVO.setAudittime(null);
			headVO.setCheck_opinion(null);
			headVO.setPk_duty(null);

			headVO.setPrimaryKey(null);
			headVO.setStatus(VOStatus.NEW);
			cloneVOs.add(tempvo);
		}
		return insertAggVOS(mergeBillsByPk_transitype(cloneVOs.toArray(new AggDailyPCVO[0])));
	}

	/**
	 * 并单操作，根据交易类型并单，相同交易类型单据进行并单
	 *
	 * @param billVOs
	 * @return
	 */
	private AggDailyPCVO[] mergeBillsByPk_transitype(AggDailyPCVO... billVOs) {

		if (ArrayUtils.isEmpty(billVOs))
			return null;
		
		Map<String,AggDailyPCVO> map = new HashMap<String,AggDailyPCVO>();
		
		for (AggDailyPCVO aggVO : billVOs) {
			// 获取交易类型
			String pk_tran = (String) aggVO.getParent().getAttributeValue(DailyPCHeadVO.PK_TRANSITYPE);
			AggDailyPCVO vo  = map.get(pk_tran);
			if(vo == null){
				// 不同交易类型放入MAP
				map.put(pk_tran, aggVO);
			}else{
				// 相同交易类型合单
				SuperVO[] rebodyVOs = vo.getAllChildrenVO();
				SuperVO[] bodyVOs = aggVO.getAllChildrenVO();
				SuperVO[] newBodyVOs = ArrayUtils.addElement(rebodyVOs, bodyVOs);
				int num = 0;
				for (SuperVO tempVO : newBodyVOs) {
					// 设置序号
					tempVO.setAttributeValue(DailyPCBodyVO.SEQUENCE_NUM, ++num);
				}
				vo.setChildren(DailyPCBodyVO.class, newBodyVOs);
				map.put(pk_tran, vo);
			}
		}
		
		return map.values().toArray(new AggDailyPCVO[0]);
		
	}

	/**
	 * 通过来源交易类型取得目的交易类型，含pk_transitype
	 *
	 * @param headVO
	 * @param destBillType
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDestTransiType(DailyPCHeadVO headVO, String destBillType) throws BusinessException {
		BillItfDefVO defVO = new BillItfDefVO();
		defVO.setSrc_billtype(headVO.getBill_type());
		defVO.setDest_billtype(destBillType);
		defVO.setPk_group(headVO.getPk_group());
		defVO.setSrc_transtype(headVO.getTransi_type());
		BillItfDefVO[] defVOs = AMProxy.lookup(IPFBillItfDef.class).getBillItfDef(defVO);
		Map<String, String> src2Dest = null;
		if (!ArrayUtils.isEmpty(defVOs)) {
			src2Dest = new HashMap<String, String>();
			for (BillItfDefVO defvo : defVOs) {
				BilltypeVO billTypeVO = PfDataCache.getBillTypeInfo(defvo.getDest_transtype());
				src2Dest.put(defvo.getSrc_transtype() == null ? SEPERTOR : defvo.getSrc_transtype(), defvo
						.getDest_transtype()
						+ SEPERTOR + billTypeVO.getPk_billtypeid());
			}
		}
		return src2Dest;
	}

	@Override
	public void updateDisposeRusltForNewRepairPlan(ParameterVO paraVO) throws BusinessException {
		// 为新增的维修计划更新处理状态
		updateDisposeRusltForNewBill(paraVO, DisposeResult.GENERATE_MT_PLAN);
	}

	@Override
	public void updateDisposeRusltForNewWO(ParameterVO paraVO) throws BusinessException {
		// 为新增的工单更新处理状态
		updateDisposeRusltForNewBill(paraVO, DisposeResult.GENERATE_WO);
	}

	/**
	 * 为新增的下游单据，更新处理状态
	 *
	 * @param pkMap
	 * @param disp_result
	 *            处理状态
	 * @throws BusinessException
	 */
	private void updateDisposeRusltForNewBill(ParameterVO paraVO, Integer disp_result) throws BusinessException {
		if (MapUtils.isEmpty(paraVO.getPk_pc_bAdest_bill())) {
			// 参数不能为空
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0036")/*@res "参数据不能为空"*/);
		}
		// 查询表体
		DailyPCBodyVO[] bodyOVS = QueryUtil.querySuperVOByPks(DailyPCBodyVO.class, new String[] {
				DailyPCBodyVO.PK_POINTCHECK, DailyPCBodyVO.PK_POINTCHECK_B, DailyPCBodyVO.DISP_RESULT }, paraVO
				.getPk_pc_bAdest_bill().keySet().toArray(new String[0]));
		if (ArrayUtils.isEmpty(bodyOVS)) {
			// 点检单据表体为空
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0037")/*@res "点检单据表体为空"*/);
		}
		// 存放点检计录表头PK
		Set<String> headSet = new HashSet<String>();
		// 校验表体
		for (DailyPCBodyVO bodyVO : bodyOVS) {
			if (DisposeResult.UNSETTLED.intValue() != bodyVO.getDisp_result().intValue()) {
				// 点检单据表体处理结果必须是未处理
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0038")/*@res "点检单据表体处理结果必须是未处理"*/);
			} else if(paraVO.getPk_pc_bAdest_bill().get(bodyVO.getPrimaryKey())!=null){
				// 未生成下游单据的不做处理
				// 处理结果、处理时间、处理人
				bodyVO.setDisp_result(disp_result);
				bodyVO.setDisp_time(getBuziTime());
				bodyVO.setPk_disposer(AuditInfoUtil.getCurrentUser());
				// 存储下游的单据类型、目的单据表头PK、目的交易类型
				bodyVO.setDest_bill_type(paraVO.getDest_bill_type());
				bodyVO.setDest_pk_bill(paraVO.getPk_pc_bAdest_bill().get(bodyVO.getPrimaryKey()));
				bodyVO.setDest_transitype(paraVO.getPk_pcAdest_transitype().get(bodyVO.getPk_pointcheck()));
				// 如果目的单据为维修计划，则需要存放维修计划的表体PK
				if (BillTypeConst_4B.REPAIR_PLAN.equals(paraVO.getDest_bill_type())) {
					bodyVO.setDest_pk_bill_b(paraVO.getPk_pc_bAdest_bill_b().get(bodyVO.getPrimaryKey()));
				}
				headSet.add(bodyVO.getPk_pointcheck());
			}
		}
		// 组装表头
		DailyPCHeadVO[] headVOS = new DailyPCHeadVO[headSet.size()];
		int i = 0;
		for (String pk_h : headSet) {
			DailyPCHeadVO headVo = new DailyPCHeadVO();
			headVo.setPk_pointcheck(pk_h);
			headVo.setBill_status(BillStatusConst.check_pass);
			headVOS[i++] = headVo;
		}
		// 先加锁表头,并校验
		LockManager.lockBaseVOs(headVOS, new String[] { DailyPCHeadVO.BILL_STATUS });

		// 更新表头
		if(headVOS!=null&&headVOS.length!=0){
			VOPersistUtil.update(new String[] { DailyPCHeadVO.PK_POINTCHECK }, headVOS);
		}
		// 更新表体
		if(bodyOVS!=null&&bodyOVS.length!=0){
			VOPersistUtil.update(new String[] { DailyPCBodyVO.DISP_RESULT, DailyPCBodyVO.DISP_TIME,
					DailyPCBodyVO.PK_DISPOSER, DailyPCBodyVO.DEST_BILL_TYPE, DailyPCBodyVO.DEST_PK_BILL,
					DailyPCBodyVO.DEST_PK_BILL_B, DailyPCBodyVO.DEST_TRANSITYPE }, bodyOVS);
		}

	}

	/**
	 * 初始化表头公共的字段
	 *
	 * @param headVO
	 */
	private void initHeadVOCommonFields(DailyPCHeadVO headVO) {
		headVO.setBill_type(BillTypeConst_4B.SPECPC);
		headVO.setBill_status(BillStatusConst.free_check);
	}

	@Override
	public AggDailyPCVO[] pushSavePCVO(AggDailyPCVO... billVOs) throws BusinessException {
		AssertUtils.notNull(billVOs, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0039")/*@res "保存的参照不可为空"*/);
		for (AggDailyPCVO tempAggVO : billVOs) {
			DailyPCHeadVO headVO = (DailyPCHeadVO) tempAggVO.getParent();
			// 初始化表头公共字段
			initHeadVOCommonFields(headVO);
			//设置制单人，制单时间
			headVO.setAttributeValue(DailyPCHeadVO.BILLMAKER, InvocationInfoProxy.getInstance().getUserId());
			headVO.setAttributeValue(DailyPCHeadVO.BILLMAKETIME, getBuziTime());
			SuperVO[] bodyVOS = tempAggVO.getAllChildrenVO();
			for (int i = 0; i < bodyVOS.length; i++) {
				// 设置序号
				if(bodyVOS[i].getAttributeValue(DailyPCBodyVO.SEQUENCE_NUM) == null ||
						((String)bodyVOS[i].getAttributeValue(DailyPCBodyVO.SEQUENCE_NUM)).isEmpty()){
					bodyVOS[i].setAttributeValue(DailyPCBodyVO.SEQUENCE_NUM, i + 1);
				}
				bodyVOS[i].setAttributeValue(DailyPCBodyVO.DISP_RESULT, DisposeResult.UNSETTLED.intValue());
			}
		}
		return insertAggVOS(billVOs);
	}

	@Override
	public void updateDisposeRusltWhenDelRepairPlan(List<String> pk_repairplan_bs) throws BusinessException {
		StringBuilder whereSQL = new StringBuilder();
		whereSQL.append(" dest_bill_type='").append(BillTypeConst_4B.REPAIR_PLAN).append("' and dest_pk_bill_b in ");
		whereSQL.append(InSqlManager.getInSQLValue(pk_repairplan_bs));
		// 查询来源单据
		String[] pk_bodys = QueryUtil.queryPksByWhereSql(DailyPCBodyVO.class, whereSQL.toString());
		// 更新处理结果
		updateDisposeRusltWhenDel(pk_bodys);
	}

	@Override
	public void updateDisposeRusltWhenDelWO(List<String> pk_wos) throws BusinessException {
		StringBuilder whereSQL = new StringBuilder();
		whereSQL.append(" dest_bill_type='").append(BillTypeConst_4B.WORKORDER).append("' and dest_pk_bill in ");
		whereSQL.append(InSqlManager.getInSQLValue(pk_wos));
		// 查询来源单据
		String[] pk_bodys = QueryUtil.queryPksByWhereSql(DailyPCBodyVO.class, whereSQL.toString());
		// 更新处理结果
		updateDisposeRusltWhenDel(pk_bodys);
	}

	
	/**
	 * 当删除上游单据的时候更新处理结果
	 *
	 * @param pk_bodys
	 * @throws BusinessException
	 */
	private void updateDisposeRusltWhenDel(String[] pk_bodys) throws BusinessException {
		if (ArrayUtils.isEmpty(pk_bodys)) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pointcheck_0","04550003-0029")/*@res "参数不可为空"*/);
		}
		int index = 0;
		DailyPCBodyVO[] bodyVOs = new DailyPCBodyVO[pk_bodys.length];
		for (String pk_b : pk_bodys) {
			bodyVOs[index] = new DailyPCBodyVO();
			bodyVOs[index].setPk_pointcheck_b(pk_b);
			//处理结果更新为未处理
			bodyVOs[index].setDisp_result(DisposeResult.UNSETTLED);
			//清除处理人、时间、下游单据信息
			bodyVOs[index].setDisp_time(null);
			bodyVOs[index].setPk_disposer(null);
			bodyVOs[index].setDest_bill_type(null);
			bodyVOs[index].setDest_pk_bill(null);
			bodyVOs[index].setDest_pk_bill_b(null);
			bodyVOs[index].setStatus(VOStatus.UPDATED);
			index++;
		}
		//将更改字段更新到数据库
		VOPersistUtil.update(new String[] { DailyPCBodyVO.DISP_RESULT, DailyPCBodyVO.DISP_TIME,
				DailyPCBodyVO.PK_DISPOSER, DailyPCBodyVO.DEST_BILL_TYPE, DailyPCBodyVO.DEST_PK_BILL,
				DailyPCBodyVO.DEST_PK_BILL_B }, bodyVOs);

	}
	
	/**
	 * 更新设备信息
	 *
	 * @param billVOs
	 * @throws BusinessException
	 */
	protected void updateEquip(AggDailyPCVO... billVOs) throws BusinessException {
		// 获取设备主键
		Set<String> pk_set = new HashSet<String>();
		for (AggDailyPCVO tempAggVO : billVOs) {
			SuperVO[] bodyVOS = tempAggVO.getAllChildrenVO();
			for (SuperVO bodyvo : bodyVOS) {
				String pk_equip = ((DailyPCBodyVO) bodyvo).getPk_equip();
				if (StringUtils.isNotEmpty(pk_equip)) {
					pk_set.add(pk_equip);
				}
			}
		}
		if (pk_set.size() < 1)
			return;
		// 获取设备VO
		Map<String, EquipHeadVO> equip_map = AMProxy.lookup(IEquipService.class).queryEquipHeadVOs(
				pk_set.toArray(new String[0]));
		if (MapUtils.isEmpty(equip_map)) {
			return;
		}
		// 更新AggDailyPCVO
		for (AggDailyPCVO tempAggVO : billVOs) {
			SuperVO[] bodyVOS = tempAggVO.getAllChildrenVO();
			for (SuperVO bodyvo : bodyVOS) {
				DailyPCBodyVO vo = (DailyPCBodyVO) bodyvo;
				String pk_equip = vo.getPk_equip();
				if (StringUtils.isEmpty(pk_equip))
					break;
				EquipHeadVO equip_vo = equip_map.get(pk_equip);
				if (equip_vo != null) {
					vo.setPk_location(equip_vo.getPk_location());
				}
			}
		}
	}

	/**
	 * 生成校验规则,用于推式生成点检的校验
	 *
	 * @param billVOs
	 * @throws BusinessException
	 */
	private void genValidationRule(AggDailyPCVO... billVOs) throws BusinessException {
		List<Validator> validators = new ArrayList<Validator>();
		// 设备校验(业务校验)
		EquipValidator equipValidator = EquipValidator.getValidator();
		equipValidator.setUseRedun(false);
		equipValidator.setOwnerOrg(true);
		DailyPCHeadVO tempVO = (DailyPCHeadVO) billVOs[0].getParentVO();
		if(BillTypeConst_4B.DAILYPC.equals(tempVO.getBill_type())){
			// 注入资产组织
			equipValidator.setPk_orgs(new String[]{tempVO.getPk_org()});
		}else{
			// 注入维修组织
			equipValidator.setMaintainPk_org(tempVO.getPk_org());
		}
		validators.add(equipValidator);
		validationRule = new ValidateServiceRule<AggDailyPCVO>(validators);
	}
	
	
	/**
	 * 获取前台业务时间
	 * @return
	 */
	private UFDateTime getBuziTime(){
		
		return BizContext.getInstance().getBizDateTime();
	}
//	/**
//	 * 取得需要去设备那里校验的组织
//	 *
//	 * @param headVO
//	 * @return
//	 */
//	private String[] getValidateOrgs(DailyPCHeadVO headVO) throws BusinessException {
//		List<String> orgs = new ArrayList<String>();
//		// 如果生成的日常点检则直接取表头的组织
//		if (BillTypeConst_4B.DAILYPC.equals(headVO.getBill_type())) {
//			orgs.add(headVO.getPk_org());
//		} else {
//			// 如果是专业点检则取表头的维修组织对应的资产组织，根据业务委托关系
//			Map<String, String[]> maintainOrg = EAMPubProxy.getAssetOrgIDsByMaintainIDs(new String[] { headVO
//					.getPk_org() });
//			if (MapUtils.isNotEmpty(maintainOrg)) {
//				String[] assetOrgs = maintainOrg.values().iterator().next();
//				if (ArrayUtils.isNotEmpty(assetOrgs)) {
//					orgs.addAll(Arrays.asList(assetOrgs));
//				}
//			}
//		}
//		return orgs.toArray(new String[0]);
//	}

	/**
	 * 点检记录表体生成故障记录后，回写其故障标识
	 * @param targetFailureBodyVOs 故障记录表体
	 * @param failureFlag
	 * @throws BusinessException
	 */
	@Override
	public void writeBackBodyVOFailureFlag(SuperVO[] targetFailureBodyVOs,
			UFBoolean failureFlag) throws BusinessException {
		AMProxy.lookup(IDailyPC.class).writeBackBodyVOFailureFlag(targetFailureBodyVOs, failureFlag);
		
	}

	/**
	 * 根据表体主键查询点检记录表体
	 * @param bodyPks
	 * @return
	 */
	@Override
	public SuperVO[] queryBodyVOsBy(String[] bodyPks, String[] fieldNames)
			throws BusinessException {		
		return AMProxy.lookup(IDailyPC.class).queryBodyVOsBy(bodyPks, fieldNames);
	}
}

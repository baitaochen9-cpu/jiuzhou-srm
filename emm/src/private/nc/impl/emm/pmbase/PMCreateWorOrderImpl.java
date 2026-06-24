package nc.impl.emm.pmbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.impl.am.common.PfUtilToolsAM;
import nc.itf.emm.prv.IPMCreateBill;
import nc.itf.ewm.pub.IPushSaveWorkOrderService;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.am.common.BaseLockData;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.ArrayConstructor;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.AssertUtils;
import nc.vo.am.constant.BillTypeConst_4B;
import nc.vo.am.constant.CommonKeyConst;
import nc.vo.am.constant.IWorkOrderFields;
import nc.vo.am.manager.LockManager;
import nc.vo.am.proxy.AMProxy;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.PMResultVO;
import nc.vo.emm.premaintain.utils.PMArithTools;
import nc.vo.emm.premaintain.utils.PMCommonUtils;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

import org.apache.commons.lang.StringUtils;

/**
 * 预防性维护生成工单实现类
 * 
 * @author duanyt
 * @date 2012-5-9
 */
public class PMCreateWorOrderImpl implements IPMCreateBill {

	/**
	 * 方法功能:预防性维护生成工单 流量工具不支持某页签增行的情况，因此返回全部数据
	 */
	public Object makeWorkOrder(PMBillVO billVO, Object userObj)
			throws BusinessException {

		UFBoolean bUseFreqRule = (UFBoolean) userObj;
		// 加锁
		BaseLockData<PMBillVO> lockdata = new BaseLockData<PMBillVO>(billVO);
		LockManager.lock(lockdata, new String[] { CommonKeyConst.ts }, null);
		// 补充数据
		PMBillVO billFullVO = new PMBaseImpl().queryPMBillVO(
				billVO.getPrimaryKey(), true, false);
		// 查询并刷新测量点最新读数
		PMArithTools.setMeasureResultLatest(new PMBillVO[] { billFullVO });
		// 校验封存及频率规则
		checkBeforeMakeWorkOrder(billFullVO, bUseFreqRule, true);
		// 设置上一工单目标日期
		PMArithTools.setPreTargStartDateBeforeMakeWO(billFullVO.getParentVO(),
				getCurrDate(), bUseFreqRule.booleanValue());
		// 根据接口关系执行相应的VO交换
		AggregatedValueObject[] workOrderVOs = PfUtilToolsAM
				.runChangeDataAryWithItfDef(BillTypeConst_4B.WORKORDER, billFullVO);
		// 生成工单
		workOrderVOs = AMProxy.lookup(IPushSaveWorkOrderService.class)
				.pushSaveWOFromOuter(workOrderVOs);
		// 生成工单后设置预防性维护信息
		setPMBillInfoAfterMakeWO(billFullVO, bUseFreqRule.booleanValue());
		// 更新记录的最新工单主键
		setLatestWorkOrder(billFullVO, workOrderVOs[0]);
		// 更新预防性维护
		PMCommonUtils.updatePMVOs(billFullVO);
		// 查询
		PMBillVO billFullVONew = new PMBaseImpl().queryPMBillVO(
				billVO.getPrimaryKey(), true, true);

		return billFullVONew;
	}

	
	public String pluginErroMsg;
	
	public String getPluginErroMsg() {
		return pluginErroMsg;
	}

	public void setPluginErroMsg(String pluginErroMsg) {
		this.pluginErroMsg = pluginErroMsg;
	}

	
	public PMBillVO[] makeWorkOrderForPlugin(PMBillVO[] billVO, Object userObj)
			throws BusinessException {

		// 查询并刷新测量点最新读数
		PMArithTools.setMeasureResultLatest(billVO);
		Map<String,PMBillVO> pmMap = new HashMap<String,PMBillVO>();
		// 设置上一工单目标日期
		for(PMBillVO pmvo : billVO){
			boolean ispass = checkBeforeMakeWorkOrder(pmvo,
					UFBoolean.FALSE, false);
			if (ispass) {
				PMArithTools.setPreTargStartDateBeforeMakeWO(
						pmvo.getParentVO(), getCurrDate(), true);
				pmMap.put(pmvo.getPrimaryKey(), pmvo);
			}
		}
		
		// 根据接口关系执行相应的VO交换
		AggregatedValueObject[] workOrderVOs = PfUtilToolsAM.runChangeDataAryWithItfDef(BillTypeConst_4B.WORKORDER, billVO);
		
		// 生成工单后根据参数补充工单的制单人
		backTaskSetWorkOrderInfo((HashMap<String, Object>) userObj,workOrderVOs);
		
		// 生成工单
		workOrderVOs = AMProxy.lookup(IPushSaveWorkOrderService.class)
				.pushSaveWOFromOuter(workOrderVOs);
		
		for(AggregatedValueObject workOrde :workOrderVOs){
			String src_pk_bill = (String) workOrde.getParentVO().getAttributeValue("src_pk_bill");//取来源单据ID
			// 生成工单后设置预防性维护信息
			
			setPMBillInfoAfterMakeWO(pmMap.get(src_pk_bill), true);
			// 更新记录的最新工单主键
			setLatestWorkOrder(pmMap.get(src_pk_bill), workOrde);
			
		}
		
		 PMBillVO[] pmvos = pmMap.values().toArray(new PMBillVO[0]);
		// 更新预防性维护
		PMCommonUtils.updatePMVOs(pmvos);

		return pmvos;
	}
	
	/**
	 * 方法功能:预防性维护生成工单 流量工具不支持某页签增行的情况，因此返回全部数据
	 */
	@SuppressWarnings("unchecked")
	public void makeWorkOrder(PMBillVO[] billVOs, Object userObj)
			throws BusinessException {
		AssertUtils.isTrue(userObj instanceof HashMap);
		// 查询并刷新测量点最新读数
		PMArithTools.setMeasureResultLatest(billVOs);
		ArrayList<PMBillVO> alBillVO = new ArrayList<PMBillVO>();
		for (int i = 0; i < billVOs.length; i++) {
			boolean ispass = checkBeforeMakeWorkOrder(billVOs[i],
					UFBoolean.TRUE, false);
			if (ispass) {
				alBillVO.add(billVOs[i]);
				PMArithTools.setPreTargStartDateBeforeMakeWO(
						billVOs[i].getParentVO(), getCurrDate(), true);
			}
		}
		if (alBillVO.size() == 0) {
			return;
		}
		PMBillVO[] rightBillVOs = alBillVO.toArray(new PMBillVO[0]);
		// 根据接口关系执行相应的VO交换
		AggregatedValueObject[] workOrderVOs = PfUtilToolsAM
				.runChangeDataAryWithItfDef(BillTypeConst_4B.WORKORDER,
						rightBillVOs);
		// 生成工单后根据参数补充工单的制单人
		backTaskSetWorkOrderInfo((HashMap<String, Object>) userObj,
				workOrderVOs);
		ArrayList<AggregatedValueObject> alCreated = new ArrayList<AggregatedValueObject>();
		for (AggregatedValueObject workOrderBill : workOrderVOs) {
			try {
				AggregatedValueObject[] workOrderCreatedVOs = AMProxy.lookup(
						IPushSaveWorkOrderService.class).pushSaveWOFromOuter(
						(AggregatedValueObject[]) ArrayConstructor
								.getArray(workOrderBill));
				alCreated.add(workOrderCreatedVOs[0]);
			} catch (BusinessException e) {
				// 后台任务，有异常不需要抛出，跳过即可
				Logger.error(e.getMessage(), e);
			}
		}
		ArrayList<PMBillVO> allSuccCreatePMBills = new ArrayList<PMBillVO>();
		for (int i = 0; i < rightBillVOs.length; i++) {
			// 生成工单后设置预防性维护信息
			AggregatedValueObject workOrderBill = getWorkOrderFromPM(
					rightBillVOs[i], alCreated);
			if (workOrderBill != null) {
				setPMBillInfoAfterMakeWO(rightBillVOs[i], true);
				// 更新记录的最新工单主键
				setLatestWorkOrder(rightBillVOs[i], workOrderBill);
				allSuccCreatePMBills.add(rightBillVOs[i]);
			}

		}
		if (allSuccCreatePMBills.size() > 0) {
			PMBillVO[] pmBillSucc = allSuccCreatePMBills
					.toArray(new PMBillVO[0]);
			// 更新预防性维护
			PMCommonUtils.updatePMVOs(pmBillSucc);
		}

	}
	
	

	/**
	 * 获取有改预防性维护生成的工单
	 * 
	 * @param billVO
	 * @param alCreated
	 * @return
	 */
	private AggregatedValueObject getWorkOrderFromPM(PMBillVO billVO,
			ArrayList<AggregatedValueObject> alCreated) {
		for (AggregatedValueObject workOrder : alCreated) {
			String billid = (String) workOrder.getParentVO().getAttributeValue(
					IWorkOrderFields.SRC_PK_BILL);
			if (billVO.getParentVO().getPk_pm().equals(billid)) {
				return workOrder;
			}
		}
		return null;
	}

	/**
	 * 是否满足绩效频率规则
	 * 
	 * 
	 * @param pmvo
	 * @return
	 */
	private boolean isCheckPassByResultRule(PMBillVO pmvo) {
		SuperVO[] resultVOs = (SuperVO[]) pmvo.getChildren(PMResultVO.class);

		for (SuperVO superBodyVO : resultVOs) {
			PMResultVO resultVO = (PMResultVO) superBodyVO;
			UFDouble measureResult = new UFDouble(resultVO.getMeasure_result());
			UFDouble ahead_num = resultVO.getAhead_num();
			if (ahead_num == null) {
				ahead_num = UFDouble.ZERO_DBL;
			}
			UFDouble nextNum = resultVO.getNext_num();
			if (measureResult != null && nextNum != null) {
				// 测量结构大于下一工单读数-提前量
				if (measureResult.compareTo(nextNum.sub(ahead_num)) >= 0) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 是否满足绩效频率规则
	 * 
	 * 
	 * @param pmvo
	 * @return
	 */
	private boolean isCheckPassByTimeRule(PMBillVO billVO) {
		PMHeadVO headVO = billVO.getParentVO();
		PMArithTools.setNextCreateWODate(headVO);
		// 取应当生成工单的日期
		UFDate makeWODate = headVO.getNext_create_date();
		if (makeWODate != null && getCurrTime().compareTo(makeWODate) >= 0) {
			// 如果当前日期在下一工单生成之后，可以生成工单
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 设置绩效频率信息
	 * 
	 * @param listVOs
	 * @param bUseFreqRule
	 */
	private void setResultInfoAfterMakeWO(PMBillVO billVO, boolean bUseFreqRule) {
		SuperVO[] resultVOs = (SuperVO[]) billVO.getChildren(PMResultVO.class);
		if (ArrayUtils.isEmpty(resultVOs)) {
			return;
		}
		for (SuperVO superBodyVO : resultVOs) {
			PMResultVO resultvo = (PMResultVO) superBodyVO;
			// 设置工单读数
			PMArithTools.setResultNumsAfterMakeWO(resultvo, bUseFreqRule);
			resultvo.setPk_adjuster(null);
			resultvo.setAdjust_date(null);
			resultvo.setPersist_flag(UFBoolean.FALSE);
		}

	}

	/**
	 *   生成工单设置预防性维护信息
	 * 清空上一工单目标结束日期，设置下一工单目标开始日期，计数器、下一工作包等信息
	 *   
	 * @param listVOs
	 * @param bUseFreqRule
	 */
	private void setPMBillInfoAfterMakeWO(PMBillVO billVO, boolean bUseFreqRule) {

		PMHeadVO headVO = billVO.getParentVO();
		// 特种设备预防性维护：保存实际完成时间（setTimeFreqAfterMakeWO会清空它）
		UFDate preEndDate = headVO.getPre_end_date();
		// 设置下一工单目标开始、结束日期等
		PMArithTools
				.setTimeFreqAfterMakeWO(headVO, getCurrDate(), bUseFreqRule);
		/**********20200628  yezhian*********************
		 * 覆盖计数器计算逻辑
		 * 选择重算下个工单开始时间和结束时间
		 * ********/
		// 特种设备预防性维护：恢复pre_end_date用于按实际完成时间计算
		if (isSpecialEquipmentPM(headVO) && preEndDate != null) {
			headVO.setPre_end_date(preEndDate);
		}
		new PMPubServiceImpl().rayBowUpdateDates(headVO);
		/************************************************************/
		// 设置绩效频率信息
		setResultInfoAfterMakeWO(billVO, bUseFreqRule);
		// 计数器
		Integer counter = headVO.getCounter();
		headVO.setCounter(counter + 1);
		String next_std_job = PMCommonUtils.countNextStdJob(billVO,
				(counter + 1));
		headVO.setNext_std_job(next_std_job);

	}

	private boolean checkBeforeMakeWorkOrder(PMBillVO billVO,
			UFBoolean bUseFreqRule, boolean isThrowExc)
			throws BusinessException {
		PMHeadVO headVO = billVO.getParentVO();
		if (headVO.getEnablestate() == IPubEnumConst.ENABLESTATE_DISABLE) {
			if (isThrowExc) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes().getStrByID("pmbase_0", "04550002-0060")/*
																				 * @
																				 * res
																				 * "预防性维护已停用，不能生成工单"
																				 */);
			} else {
				return false;
			}
		}
		if (bUseFreqRule.booleanValue()) {
			// 特种设备预防性维护且首次生成（counter=0）：跳过频率检查，允许立即生成
			if (isSpecialEquipmentPM(headVO) && (headVO.getCounter() == null || headVO.getCounter() == 0)) {
				return true;
			}
			boolean isResultPass = isCheckPassByResultRule(billVO);
			boolean isTimePass = isCheckPassByTimeRule(billVO);
			if (!isResultPass && !isTimePass) {
				if (isThrowExc) {
					throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("pmbase_0",
									"04550002-0061")/* @res "不满足频率规则，不能生成工单" */);
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 方法功能:后台任务预防性维护生成工单补充工单的制单人。
	 * 
	 * @param mbtype
	 * @param mber
	 * @param woMakeVOs
	 * @param workOrderVOs
	 * @throws BusinessException
	 */
	private void backTaskSetWorkOrderInfo(HashMap<String, Object> hmParam,
			AggregatedValueObject[] workOrderVOs) throws BusinessException {
		if (hmParam != null && !hmParam.isEmpty()) {
			String mode = (String) hmParam.get(WoMakePlugin.CreatorMode);
			String creator = (String) hmParam.get(WoMakePlugin.CREATOR);
			String srcField = null;
			if (mode.equals(WoMakePlugin.CreatorMode_Value_Exec)) {
				srcField = IWorkOrderFields.PK_EXECUTOR;
			} else if (mode.equals(WoMakePlugin.CreatorMode_Value_Director)) {
				srcField = IWorkOrderFields.PK_DIRECTOR;
			}
			if (srcField != null) {
				// 设置工单制单人
				for (int i = 0; i < workOrderVOs.length; i++) {
					String billMaker = getUserPk((String) workOrderVOs[i]
							.getParentVO().getAttributeValue(srcField));
					// 若没有对应的用户则取工单制单人参数
					if (StringUtils.isBlank(billMaker)) {
						billMaker = creator;
					}
					// 设置制单人
					workOrderVOs[i].getParentVO().setAttributeValue(
							CommonKeyConst.BILLMAKER, billMaker);
					// 设置创建人
					workOrderVOs[i].getParentVO().setAttributeValue(
							CommonKeyConst.creator, billMaker);
				}
			} else {
				for (int i = 0; i < workOrderVOs.length; i++) {
					// 设置制单人
					workOrderVOs[i].getParentVO().setAttributeValue(
							CommonKeyConst.BILLMAKER, creator);
					// 设置制单人
					workOrderVOs[i].getParentVO().setAttributeValue(
							CommonKeyConst.creator, creator);
				}
			}
		}
	}

	/**
	 * 根据人员主键取用户主键
	 * 
	 * @param customerPk
	 * @return
	 * @throws BusinessException
	 */
	private String getUserPk(String customerPk) throws BusinessException {
		String userPk = null;

		if (StringUtils.isNotBlank(customerPk)) {
			UserVO userVO = AMProxy.lookup(IUserPubService.class)
					.queryUserVOByPsnDocID(customerPk);
			if (userVO != null) {
				userPk = userVO.getCuserid();
			}
		}

		return userPk;
	}

	/**
	 * 更新记录的最新工单主键 @
	 * 
	 * @param woMakeVOs
	 * @param workOrderVOs
	 */
	private void setLatestWorkOrder(PMBillVO billVO,
			AggregatedValueObject workOrderVO) {
		PMHeadVO headVO = billVO.getParentVO();
		SuperVO woHeadVO = (SuperVO) workOrderVO.getParentVO();
		headVO.setPk_pre_bill(woHeadVO.getPrimaryKey());

	}

	/**
	 * 当前日期
	 * 
	 * @return
	 */
	public static UFDate getCurrDate() {
		return BizContext.getInstance().getBizDate().asBegin();
	}

	/**
	 * 当前日期
	 * 
	 * @return
	 */
	public static UFDate getCurrTime() {
		return BizContext.getInstance().getBizDate();
	}

	/**
	 * 判断是否为特种设备预防性维护（交易类型4B72-Cxx-02）
	 */
	private boolean isSpecialEquipmentPM(PMHeadVO headVO) {
		if (headVO == null || headVO.getTransi_type() == null) {
			return false;
		}
		return "4B72-Cxx-02".equals(headVO.getTransi_type().trim());
	}
}
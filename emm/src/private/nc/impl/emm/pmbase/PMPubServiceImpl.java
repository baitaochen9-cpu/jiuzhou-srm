package nc.impl.emm.pmbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.impl.am.bill.BillBaseDAO;
import nc.impl.am.common.DataServiceImpl;
import nc.impl.am.db.QueryUtil;
import nc.impl.am.db.VOPersistUtil;
import nc.impl.emm.pmresult.PMResultImpl;
import nc.itf.am.prv.IDataService;
import nc.itf.emm.pub.IPMPubService;
import nc.itf.emm.pub.IPMQueryService;
import nc.itf.ewm.pub.IRepairPlanPubService;
import nc.itf.fi.pub.SysInit;
import nc.vo.am.common.BizContext;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.SqlBuilder;
import nc.vo.am.constant.BillTypeConst_4B;
import nc.vo.am.constant.CommonKeyConst4EAM;
import nc.vo.am.constant.IWorkOrderFields;
import nc.vo.am.proxy.AMProxy;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.PMResultVO;
import nc.vo.emm.premaintain.utils.PMArithTools;
import nc.vo.emm.premaintain.utils.PMCommonUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

/**
 * <p>
 * 预防性维护对外提供的服务类。
 * </p>
 * 
 * @author cuikai
 * @version 6.0
 */
public class PMPubServiceImpl extends BillBaseDAO<PMBillVO> implements
		IPMPubService, IPMQueryService {

	private static String WO_CANCELED = "cancel";

	private static String WO_FINISHED = "finish";

	/**
	 * 工单更改为作废类别回写预防性维护。
	 * 
	 * @param woHeadVO
	 * @throws BusinessException
	 */
	@Override
	public void woWriteBackPM4Canceled(SuperVO woHeadVO)
			throws BusinessException {
		writeBackPMBill(woHeadVO, WO_CANCELED);
	}

	/**
	 * 工单更改为完成类别回写预防性维护。
	 * 
	 * @param woHeadVO
	 * @throws BusinessException
	 */
	@Override
	public void woWriteBackPM4Finish(SuperVO woHeadVO) throws BusinessException {
		writeBackPMBill(woHeadVO, WO_FINISHED);
	}

	/**
	 * 维修计划保存时回写预防性维护
	 * 
	 * @param drBodyVOs
	 *            下游单据表体VO
	 */
	@Override
	public void rpWriteBackPM(SuperVO[] drBodyVOs,
			Map<String, UFDate> maxStartDateMap,
			Map<String, String> srcPkBodyPkMap,
			Map<String, Integer> maxCounterMap ) throws BusinessException {
		List<String> billVOPKs = new ArrayList<String>();
		// 取来源单据主键
		for (SuperVO bodyVO : drBodyVOs) {
			String srcPk = (String) bodyVO
					.getAttributeValue(CommonKeyConst4EAM.SRC_PK_BILL);
			if (!billVOPKs.contains(srcPk)) {
				billVOPKs.add(srcPk);
			}
		}
		// 查询预防性维护VO
		PMBillVO[] billVOs = QueryUtil.queryBillVOByPks(PMBillVO.class,
				billVOPKs.toArray(new String[0]), true);
		// 设置时间型预防性维护回写日期
		setTimePMNextCreateDate(billVOs, maxStartDateMap);
		// 绩效型和条件型预防性维护刷新测量点最新读数
		PMArithTools.setMeasureResultLatest(billVOs);
		// 回写预防性维护下一信息
		rewritePMBackInfo(billVOs, maxCounterMap);
		// 设置预防性维护生成的最新下游单据（维修计划、工单）
		setDownriverBillPk(billVOs, srcPkBodyPkMap);
		// 更新预防性维护到数据库
		if (billVOs != null && billVOs.length != 0) {
			PMCommonUtils.updatePMVOs(billVOs);
		}
	}

	/**
	 * 设置时间型预防性维护最新时间
	 * 
	 * @param billVOs
	 * @param drBodyVOs
	 */
	private void setTimePMNextCreateDate(PMBillVO[] billVOs,
			Map<String, UFDate> maxStartDateMap) {
		// 取最大的计划开始日期
		for (PMBillVO billVO : billVOs) {
			PMHeadVO pmHeadVO = (PMHeadVO) billVO.getParent();
			pmHeadVO.setNext_start_date(maxStartDateMap.get(billVO
					.getPrimaryKey()));
		}
	}

	/**
	 * 设置生成最新下游单据的主键
	 * 
	 * @param billVOs
	 * @param drBodyVOs
	 * @param maxStartDateMap
	 */
	private void setDownriverBillPk(PMBillVO[] billVOs,
			Map<String, String> srcPkBodyPkMap) {

		for (PMBillVO billVO : billVOs) {
			PMHeadVO pmHeadVO = (PMHeadVO) billVO.getParent();
			pmHeadVO.setPk_pre_bill(srcPkBodyPkMap.get(billVO.getPrimaryKey()));
		}	
	}

	/**
	 * 重写预防性维护回写信息： 时间型回写日期信息 绩效型回写绩效页签数据
	 * 
	 * @param billVOs
	 * @param maxCounterMap 
	 */
	private void rewritePMBackInfo(PMBillVO[] billVOs, Map<String, Integer> maxCounterMap) {
		for (PMBillVO billVO : billVOs) {
			PMHeadVO headVO = billVO.getParentVO();
			// 设置上一工单目标日期
			PMArithTools.setPreTargStartDateBeforeMakeWO(headVO, BizContext
					.getInstance().getBizDate().asBegin(), true);
			// 特种设备预防性维护：保存实际完成时间（setTimeFreqAfterMakeWO会清空它）
			UFDate savedPreEndDate = headVO.getPre_end_date();
			// 设置下一工单目标开始、结束日期等
			PMArithTools.setTimeFreqAfterMakeWO(headVO, BizContext
					.getInstance().getBizDate().asBegin(), true);
			// 特种设备预防性维护：覆盖为按实际完成时间计算
			if (isSpecialEquipmentPM(headVO) && savedPreEndDate != null) {
				headVO.setNext_start_date(PMCommonUtils.getDateByFre(
						headVO.getPeriods(), headVO.getPeriods_unit(), savedPreEndDate));
				updateDatesForNextCreat(headVO);
			}
			// 设置绩效页签数据
			SuperVO[] resultVOs = (SuperVO[]) billVO
					.getChildren(PMResultVO.class);
			if (ArrayUtils.isNotEmpty(resultVOs)) {
				for (SuperVO resultVO : resultVOs) {
					PMResultVO resultvo = (PMResultVO) resultVO;
					// 设置工单读数
					PMArithTools.setResultNumsAfterMakeWO(resultvo, true);
					// 设置调整人为空
					resultvo.setPk_adjuster(null);
					// 设置调整日期为空
					resultvo.setAdjust_date(null);
					// 设置永久调整字段为false
					resultvo.setPersist_flag(UFBoolean.FALSE);
				}
			}
			// 计数器
			Integer counter = maxCounterMap.get(headVO.getPk_pm());
			headVO.setCounter(counter + 1);
			String next_std_job = PMCommonUtils.countNextStdJob(billVO,
					(counter + 1));
			headVO.setNext_std_job(next_std_job);
		}
		// 更新预防性维护到数据库
		if (billVOs != null && billVOs.length != 0) {
			PMCommonUtils.updatePMVOs(billVOs);
		}
	}

	/**
	 * 工单作废时设置预防性维护表头时间信息
	 * 
	 * @param pmHeadVO
	 */
	private void setPMHeadWhenWOCancel(PMHeadVO pmHeadVO) {
		// 上一工单完成日期 ＝ 上一工单开始日期
		pmHeadVO.setPre_end_date(pmHeadVO.getPre_start_date());
		// 可变计划的算法：下一工单开始日期为空，下一工单开始日期＝上一工单开始日期＋生成频率
		UFDate next_start_date = pmHeadVO.getNext_start_date();
		if (pmHeadVO.getAlter_flag().booleanValue()) {
			// CHECKED_CUIKAI 没有判空,由于有bug,cuikai修改
			if (next_start_date != null) {
				// 如果下一工单目标日期等于首次开始日期则不回写
				if (pmHeadVO.getInitial_date().compareTo(next_start_date) != 0) {
					next_start_date = PMCommonUtils.getDateByFre(
							pmHeadVO.getPeriods(), pmHeadVO.getPeriods_unit(),
							pmHeadVO.getPre_start_date());
					pmHeadVO.setNext_start_date(next_start_date.asBegin());
					// 设置下一工单建立日期
					pmHeadVO.setNext_create_date(next_start_date.getDateBefore(
							pmHeadVO.getAhead_days() == null ? 0 : pmHeadVO
									.getAhead_days()).asBegin());
				}
			} else {
				next_start_date = PMCommonUtils.getDateByFre(
						pmHeadVO.getPeriods(), pmHeadVO.getPeriods_unit(),
						pmHeadVO.getPre_start_date());
				pmHeadVO.setNext_start_date(next_start_date.asBegin());
				
				// 设置下一工单建立日期
				pmHeadVO.setNext_create_date(next_start_date.getDateBefore(
						pmHeadVO.getAhead_days() == null ? 0 : pmHeadVO
								.getAhead_days()).asBegin());
			}
		}
	}

	/**
	 * 工单完成时设置预防性维护表头
	 * 
	 * @param pmHeadVO
	 * @param woHeadVO
	 */
	private void setPMHeadWhenWOFinish(PMHeadVO pmHeadVO, SuperVO woHeadVO) {
		// 符合时间型的回写时间型
		UFDateTime actuEndTime = (UFDateTime) woHeadVO
				.getAttributeValue(IWorkOrderFields.ACTU_END_TIME);
		pmHeadVO.setPre_end_date(actuEndTime.getBeginDate());
		// 特种设备预防性维护：永远按实际完成时间计算
		if (isSpecialEquipmentPM(pmHeadVO)) {
			UFDate actuEndDate = actuEndTime.getDate();
			Integer periods = pmHeadVO.getPeriods();
			Integer periodsUnit = pmHeadVO.getPeriods_unit();
			if (periods != null && periodsUnit != null && actuEndDate != null) {
				UFDate nextStartDate = PMCommonUtils.getDateByFre(periods, periodsUnit, actuEndDate);
				pmHeadVO.setNext_start_date(nextStartDate.asBegin());
				pmHeadVO.setNext_create_date(nextStartDate.getDateBefore(
						pmHeadVO.getAhead_days() == null ? 0 : pmHeadVO.getAhead_days()).asBegin());
			}
		} else if (pmHeadVO.getAlter_flag().booleanValue()) {
			UFDate next_start_date = pmHeadVO.getNext_start_date();
			// CHECKED_CUIKAI 没有判空,由于有bug,cuikai修改
			if (next_start_date != null) {
				if (pmHeadVO.getInitial_date().compareTo(next_start_date) != 0) {
					next_start_date = PMCommonUtils
							.getDateByFre(
									pmHeadVO.getPeriods(),
									pmHeadVO.getPeriods_unit(),
									((UFDateTime) woHeadVO
											.getAttributeValue(IWorkOrderFields.ACTU_END_TIME))
											.getDate());
					pmHeadVO.setNext_start_date(next_start_date.asBegin());
					// 设置下一工单建立日期
					pmHeadVO.setNext_create_date(next_start_date.getDateBefore(
							pmHeadVO.getAhead_days() == null ? 0 : pmHeadVO
									.getAhead_days()).asBegin());
				}
			} else {
				next_start_date = PMCommonUtils
						.getDateByFre(
								pmHeadVO.getPeriods(),
								pmHeadVO.getPeriods_unit(),
								((UFDateTime) woHeadVO
										.getAttributeValue(IWorkOrderFields.ACTU_END_TIME))
										.getDate());
				// 设置下一工单目标日期
				pmHeadVO.setNext_start_date(next_start_date.asBegin());
				// 设置下一工单建立日期
				pmHeadVO.setNext_create_date(next_start_date.getDateBefore(
						pmHeadVO.getAhead_days() == null ? 0 : pmHeadVO
								.getAhead_days()).asBegin());
			}
		}
		
		
//	
		
	}

	/**
	 * 回写绩效页签
	 * 
	 * @param pmHeadVO
	 * @throws BusinessException
	 */
	private PMResultVO[] setPMResultWhenCanceled(PMHeadVO pmHeadVO)
			throws BusinessException {

		List<String> alPK = new ArrayList<String>();
		alPK.add(pmHeadVO.getPk_pm());
		// 查询出带有最新测量读数的绩效页签的数据
		Collection<PMResultVO> resultVOList = new PMResultImpl()
				.queryMeasureResult(alPK);
		if (resultVOList == null || resultVOList.isEmpty()) {
			return null;
		}
		PMResultVO[] resultVOs = resultVOList.toArray(new PMResultVO[0]);
		List<PMResultVO> alResultVOList = new ArrayList<PMResultVO>();
		// 绩效页签的回写算法
		for (PMResultVO resultVO : resultVOs) {
			// 上一工单开始读数为空不做任何处理
			if (resultVO.getPre_start_num() != null) {
				resultVO.setPre_end_num(resultVO.getPre_start_num());
				// 可变计划：下一工单读数＝上一工单开始读数＋生成频率
				if (resultVO.getAlter_flag().booleanValue()) {
					if (resultVO.getNext_num() == null) {
						resultVO.setNext_num(resultVO.getPre_start_num().add(
								new UFDouble(resultVO.getGen_rate())));
					}
				}
				resultVO.setAdjust_date(null);
				resultVO.setPk_adjuster(null);
				resultVO.setPersist_flag(UFBoolean.FALSE);
				alResultVOList.add(resultVO);
			}
		}
		PMResultVO[] pmResultVOs = null;
		if (alResultVOList.size() > 0) {
			pmResultVOs = alResultVOList.toArray(new PMResultVO[0]);
		}
		return pmResultVOs;

	}

	/**
	 * 工单完成时回写绩效信息
	 * 
	 * @param pmHeadVO
	 * @return
	 * @throws BusinessException
	 */
	private PMResultVO[] setPMResultWhenFinished(PMHeadVO pmHeadVO)
			throws BusinessException {
		List<String> pmPK = new ArrayList<String>();
		pmPK.add(pmHeadVO.getPk_pm());

		// 查询出带有最新测量读数的绩效页签的数据
		Collection<PMResultVO> resultVOList = new PMResultImpl()
				.queryMeasureResult(pmPK);
		if (resultVOList == null || resultVOList.isEmpty()) {
			return null;
		}
		PMResultVO[] resultVOs = resultVOList.toArray(new PMResultVO[0]);
		List<PMResultVO> alResultVOList = new ArrayList<PMResultVO>();
		// 绩效页签的回写算法
		for (PMResultVO resultVO : resultVOs) {
			// 如果测量点读数为空，不做处理
			if (resultVO.getMeasure_result() != null) {
				resultVO.setPre_end_num(new UFDouble(resultVO.getMeasure_result()));
				if (resultVO.getAlter_flag().booleanValue()) {
					resultVO.setNext_num(resultVO.getPre_end_num().add(
							new UFDouble(resultVO.getGen_rate())));
				}
				resultVO.setAdjust_date(null);
				resultVO.setPk_adjuster(null);
				resultVO.setPersist_flag(UFBoolean.FALSE);
				alResultVOList.add(resultVO);
			}
		}
		PMResultVO[] pmResultVOs = null;
		if (alResultVOList.size() > 0) {
			pmResultVOs = alResultVOList.toArray(new PMResultVO[0]);
		}
		return pmResultVOs;
	}

	/**
	 * 回写预防性维护
	 * 
	 * @param woHeadVO
	 * @param woOperaCode
	 * @throws BusinessException
	 * 
	 * update 20200628 yezhian 
	 */
	private void writeBackPMBill(SuperVO woHeadVO, String woOperaCode)
			throws BusinessException {
		PMHeadVO pmHeadVO = queryPMBillNeedReWrite(woHeadVO);
		if (pmHeadVO == null) {
			return;
		}
		String billtype = pmHeadVO.getBill_type();

		PMResultVO[] pmResultVOs = null;
		if (WO_CANCELED.equals(woOperaCode)) {
			setPMHeadWhenWOCancel(pmHeadVO);
			// 非时间型时，回写绩效页签
			if (!BillTypeConst_4B.PMTIME.equals(billtype)) {
				pmResultVOs = setPMResultWhenCanceled(pmHeadVO);
			}
		} else if (WO_FINISHED.equals(woOperaCode)) {
			setPMHeadWhenWOFinish(pmHeadVO, woHeadVO);
			
			/****20200628 yezhian*******b********************/
			this.rayBowUpdateDates_WO_FINISHED(pmHeadVO);
			/***************************e****/
			
			// 非时间型时，回写绩效页签
			if (!BillTypeConst_4B.PMTIME.equals(billtype)) {
				pmResultVOs = setPMResultWhenFinished(pmHeadVO);
			}
		}
		// 回写时间型预防性维护表头
		VOPersistUtil
				.update(new String[] { PMHeadVO.PRE_END_DATE,
						PMHeadVO.NEXT_START_DATE, PMHeadVO.NEXT_CREATE_DATE ,
						PMHeadVO.DEF2},
						pmHeadVO);
		if (pmResultVOs != null) {
			// 回写绩效型预防性维护表体
			VOPersistUtil.update(new String[] { PMResultVO.ADJUST_DATE,
					PMResultVO.PK_ADJUSTER, PMResultVO.NEXT_NUM,
					PMResultVO.PRE_END_NUM }, pmResultVOs);
		}
	}
	/**
	 * yezhian 20200730 工单完成后回写预防计划表头时，校正下次生成时间
	 * @param pmHeadVO
	 * @param wO_FINISHED2
	 */
	private void rayBowUpdateDates_WO_FINISHED(PMHeadVO pmHeadVO) {
		UFDate pre_end_date = pmHeadVO.getPre_end_date();//上一个工单完成时间，完成时这个值已被填充
		Integer periods = pmHeadVO.getPeriods();//周期
		Integer periods_unit = pmHeadVO.getPeriods_unit();//周期单位
		// 特种设备预防性维护：跳过允差判断，永远按实际完成时间计算
		if (isSpecialEquipmentPM(pmHeadVO)) {
			pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre(
					periods, periods_unit, pre_end_date));
			updateDatesForNextCreat(pmHeadVO);
			return;
		}
		Integer def1 = pmHeadVO.getDef1() == null ? 0 : new Integer(pmHeadVO.getDef1());//允差
		UFDate pre_start_date = pmHeadVO.getPre_start_date(); //上一个工单目标时间
		
		 boolean before = pre_end_date.before(PMCommonUtils.getDateByFre( //结束日期早于目标日期-允差
					def1 * -1 , 
				    0,
				  pre_start_date/*目标日期*/) );
		 boolean after = pre_end_date.after(PMCommonUtils.getDateByFre( //结束日期晚于目标日期+允差
					def1  , 
				    0,
				  pre_start_date/*目标日期*/) );
		 /*
		  * 如果超出允差，看组织参数进行值
		  */
		 String pk_org = pmHeadVO.getPk_org();
		
			String itm = this.getStartItiem(pk_org);
			
		 if(before || after){ //超出允差
			 pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre( 
						periods   , 
						periods_unit,
						(UFDate)pmHeadVO.getAttributeValue(itm) /*上一工单实际完成日期*/));
		 }else{  //未超出允差
			 pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre( 
						periods   , 
						periods_unit,
						pre_start_date /*上一工单目标开始日期*/));
		 }
		
		 updateDatesForNextCreat(pmHeadVO);
	}

	/**
	 * 业务参数组织
	 *  EMM04 超出允差重复取值
	 * @param pk_org
	 * @return
	 */
	private String getStartItiem(String pk_org) {
		 String emm04 = "下一工单计划开始时间";
		 try {
			 emm04 = SysInit.getParaString(pk_org, "EMM04");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		 if ("上一工单实际完成时间".equals(emm04)){
			 return PMHeadVO.PRE_END_DATE;
		 } 	
			 return PMHeadVO.PRE_START_DATE;
	}

	/***
	 * 工单目标结束时间/工单目标生成时间
	 * @param pmHeadVO
	 */
	private void updateDatesForNextCreat(PMHeadVO pmHeadVO) {
		// TODO Auto-generated method stub
		Integer ahead_days = pmHeadVO.getAhead_days(); // 提前天数
		Integer def1 = pmHeadVO.getDef1() == null ? 0 : new Integer(pmHeadVO.getDef1());//允差
		//下一工单不管怎么处理，结束时间和生成时间都基于计划开始时间去进行修改
		//永远等于下一个工单目标时期+允差 == 下一工单计划结束时间
		pmHeadVO.setDef2(PMCommonUtils.getDateByFre( 
				def1   , 
				0,
				pmHeadVO.getNext_start_date() ).toString());
		//下一个工单计划生成时间 == 下一个工单目标时间 + 提前天数
		pmHeadVO.setNext_create_date(PMCommonUtils.getDateByFre( 
				ahead_days*-1   , 
				0,
				pmHeadVO.getNext_start_date()));
	}

	/**
	 * yezhian 20200730 创建工单重置表头时间
	 * 								工单生成时只要管下一个工单生成时间，下一个工单目标时间，下一个工单目标结束时间
	 * 								都是一个预计值，会不会产生变化要看工单完成时会不会回写新的值进来
	 * @param pmHeadVO
	 */
	public void rayBowUpdateDates(PMHeadVO pmHeadVO){
		Integer periods = pmHeadVO.getPeriods();//周期
		Integer periods_unit = pmHeadVO.getPeriods_unit();//周期单位
		// 特种设备预防性维护：有完成记录才按pre_end_date+周期计算，无完成记录不推进
		if (isSpecialEquipmentPM(pmHeadVO)) {
			if (pmHeadVO.getPre_end_date() != null) {
				pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre(
						periods, periods_unit,
						pmHeadVO.getPre_end_date() /*上一工单实际完成日期*/));
			}
			// 无论有无完成记录，都重新计算 next_create_date 和 def2
			updateDatesForNextCreat(pmHeadVO);
			return;
		}
		Integer counter = pmHeadVO.getCounter();//计数器
		UFDate initial_date = pmHeadVO.getInitial_date();//开始日期
		UFDate pre_start_date = pmHeadVO.getPre_start_date(); //上一个工单目标时间
		// periods/periods_unit already declared above
		UFDate adjust_start_date = pmHeadVO.getAdjust_start_date(); //调整后的日期
		
		
//
//		// 1.检查计数器 
//		if(counter == 0){//第一次生成
//			//开始时间+同期 == 下一次工单目标日期
//			pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre( 
//					periods   , 
//					periods_unit,
//					adjust_start_date == null ?  pre_start_date : pre_start_date));  //如果第一次就被调整，就以调整后的值做为起算日期
//		}else{
			pmHeadVO.setNext_start_date(PMCommonUtils.getDateByFre( 
					periods   , 
					periods_unit,
					pre_start_date /*上一个工单目标日期*/)
					);
			
//		}
		updateDatesForNextCreat(pmHeadVO);
		
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

	/**
	 * 查询需要更新的预防性维护
	 * 
	 * @param woHeadVO
	 * @return
	 * @throws BusinessException
	 */
	private PMHeadVO queryPMBillNeedReWrite(SuperVO woHeadVO)
			throws BusinessException {
		SqlBuilder sqlCond = new SqlBuilder();
		// 预防性维护——>工单 
		sqlCond.append(" ( ");
		sqlCond.append(PMHeadVO.PK_PM, (String) woHeadVO
				.getAttributeValue(IWorkOrderFields.SRC_PK_BILL));
		sqlCond.append("	AND ");
		sqlCond.append(PMHeadVO.PK_PRE_BILL,
				(String) woHeadVO.getAttributeValue(IWorkOrderFields.PK_WO));
		sqlCond.append(" ) ");
		// 预防性维护——>维修计划——>工单
		String rpBodyPk = (String) woHeadVO.getAttributeValue(IWorkOrderFields.SRC_PK_BILL_B);
		String pmPk = AMProxy.lookup(IRepairPlanPubService.class).querySrcPkByBodyPk(rpBodyPk);
		if(rpBodyPk != null && pmPk != null){
			sqlCond.append(" OR (");
			sqlCond.append(PMHeadVO.PK_PM, pmPk);
			sqlCond.append("	AND ");
			sqlCond.append(PMHeadVO.PK_PRE_BILL, rpBodyPk);
			sqlCond.append(" ) ");
		}
		// 根据查询条件查询出对应的主表VO
		PMHeadVO[] headVOs = new DataServiceImpl().retrieveSuperVOs(
				PMHeadVO.class, sqlCond.toString());
		if (ArrayUtils.isEmpty(headVOs)) {
			return null;
		}
		return headVOs[0];

	}

	@Override
	public String[] queryPMBillPKsByEquip(String pk_equip)
			throws BusinessException {
		String[] billhids = AMProxy.lookup(IDataService.class)
				.queryPksByWhereSql(PMHeadVO.class,
						PMHeadVO.PK_EQUIP + " = '" + pk_equip + "'");

		return billhids;
	}

	@Override
	public String[] queryPMBillPKsByLocation(String pk_location)
			throws BusinessException {
		String[] billhids = AMProxy.lookup(IDataService.class)
				.queryPksByWhereSql(PMHeadVO.class,
						PMHeadVO.PK_LOCATION + " = '" + pk_location + "'");

		return billhids;
	}

	@Override
	public void rpWriteBackPMWhenMakeWO(String[] headPKs)
			throws BusinessException {
		PMHeadVO[] pmheadVOs = QueryUtil.querySuperVOByPks(PMHeadVO.class,
				headPKs);

		for (PMHeadVO headVO : pmheadVOs) {
			// 设置上一工单完成日期为空
			headVO.setPre_end_date(null);
			// 设置上一工单目标日期
			PMArithTools.setPreTargStartDateBeforeMakeWO(headVO, BizContext
					.getInstance().getBizDate().asBegin(), false);
		}

		// 更新到数据库中
		VOPersistUtil.update(pmheadVOs);
	}

}

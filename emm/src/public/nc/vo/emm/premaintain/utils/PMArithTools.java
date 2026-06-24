package nc.vo.emm.premaintain.utils;

import java.util.ArrayList;
import java.util.Map;

import nc.itf.am.pub.IMeaspointPubService;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.measurepoint.MeaspointBodyVO;
import nc.vo.am.proxy.AMProxy;
import nc.vo.emm.premaintain.PMBillVO;
import nc.vo.emm.premaintain.PMHeadVO;
import nc.vo.emm.premaintain.PMResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 预防性维护生成工单的频率算法
 * 
 * @author zhengss
 * 
 */
public class PMArithTools {
	/**
	 * 取最大日期，不考虑时间的比较
	 * 
	 * @param dates
	 * @return
	 */
	private static UFDate getMaxUFDate(UFDate[] dates) {
		if (ArrayUtils.isEmpty(dates)) {
			return null;
		}
		UFDate date = dates[0];
		for (int i = 1; i < dates.length; i++) {
			if (date == null || dates[i] != null && date.beforeDate(dates[i])) {
				date = dates[i];
			}
		}
		return date;
	}

	private static UFDouble getMaxDouble(UFDouble[] doubleNums) {

		UFDouble maxNum = UFDouble.ZERO_DBL;
		for (UFDouble doubleNum : doubleNums) {
			if (doubleNum != null && doubleNum.compareTo(maxNum) >= 0) {
				maxNum = doubleNum;
			}
		}
		return maxNum;
	}

	private static UFDouble getPreStartNum(UFDouble measureResultLatest,
			UFDouble nextNum, UFDouble ahead_num, boolean bUseFreqRule) {
		// 默认取当前读数
		UFDouble PreStartNum = measureResultLatest;
		if (ahead_num == null) {
			ahead_num = UFDouble.ZERO_DBL;
		}
		// 如果根据规则计算，并且符合绩效频率规则，则取当前读数和下一个读数的最大值
		if (bUseFreqRule && measureResultLatest != null && nextNum != null
				&& measureResultLatest.compareTo(nextNum.sub(ahead_num)) >= 0) {
			PreStartNum = getMaxDouble(new UFDouble[] { measureResultLatest,
					nextNum });
		}
		return PreStartNum;
	}

	/**
	 * 设置上一工单读数
	 * 
	 * @param resultvo
	 *            绩效页签VO
	 * @param bUseFreqRule
	 *            是否使用频率规则
	 */
	private static void setPreStartNum(PMResultVO resultvo, boolean bUseFreqRule) {
		UFDouble measureResult = new UFDouble(resultvo.getMeasure_result());
		UFDouble nextNum = resultvo.getNext_num();
		UFDouble preStartNum = getPreStartNum(measureResult, nextNum,
				resultvo.getAhead_num(), bUseFreqRule);
		resultvo.setPre_start_num(preStartNum);
	}

	/**
	 * 生成工单后设置工单读数
	 * 
	 * @param resultvo
	 *            绩效页签VO
	 * @param bUseFreqRule
	 *            是否使用频率规则
	 */
	public static void setResultNumsAfterMakeWO(PMResultVO resultvo,
			boolean bUseFreqRule) {
		// 设置上一工单开始读数
		setPreStartNum(resultvo, bUseFreqRule);
		// 清空上一工单结束读数
		resultvo.setPre_end_num(null);
		// 可变计划：下一工单读数为空
		if (resultvo.getAlter_flag() != null
				&& resultvo.getAlter_flag().booleanValue()) {
			resultvo.setNext_num(null);
		} else {
			// 固定计划：下一工单读数为上一工单＋频率
			if (resultvo.getPre_start_num() != null) {
				UFDouble next_num = resultvo.getPre_start_num().add(
						new UFDouble(resultvo.getGen_rate()));
				resultvo.setNext_num(next_num);
			}
		}
	}

	/**
	 * 计算应该生成工单的日期
	 * 
	 * @param headVO
	 * @return
	 */
	public static void setNextCreateWODate(PMHeadVO headVO) {
		UFDate adjustDate = headVO.getAdjust_start_date();
		UFDate nextStartDate = headVO.getNext_start_date();
		Integer aheadDays = headVO.getAhead_days();
		UFDate makeWODate = getNextCreateWODate(adjustDate, nextStartDate,
				aheadDays);
		headVO.setNext_create_date(makeWODate);
	}

	/**
	 * 根据时间频率计算下一工单生成日期
	 * 
	 * @param adjustDate
	 * @param nextStartDate
	 * @param aheadDays
	 * @return
	 */
	public static UFDate getNextCreateWODate(UFDate adjustDate,
			UFDate nextStartDate, Integer aheadDays) {
		// 取应当生成工单的日期，优先取调整后目标开始日期
		UFDate createDate = null;
		if (adjustDate == null) {
			createDate = nextStartDate;
		} else {
			createDate = adjustDate;
		}
		if (createDate != null && aheadDays != null) {
			createDate = createDate.getDateBefore(aheadDays);
		}
		return createDate;
	}

	/**
	 * 编辑周期和可变计划字段时，计算下一工单开始读数
	 * 
	 * @param resultVO
	 * @return
	 */
	public static UFDouble getNextStartNum(PMResultVO resultVO) {
		UFDouble preStartNum = resultVO.getPre_start_num();
		UFDouble preEndNum = resultVO.getPre_end_num();
		UFDouble initialNum = resultVO.getInitial_num();
		UFDouble genRate = resultVO.getGen_rate();
		UFDouble nextNum = null;
		// 初始值或周期为空
		if (genRate == null || initialNum == null) {
			return null;
		}
		// 是否可变计划
		if (resultVO.getAlter_flag().booleanValue()) {
			if (preEndNum != null) {
				// 上一工单结束读数为空（表示工单还没有完成）,下一工单读数 ＝ 上一工单结束读数 ＋ 新的频率
				nextNum = preEndNum.add(genRate);
			} else if (preStartNum != null) {
				nextNum = null;
			} else  {
				// 如果上一工单开始读数为空(表示没有生成过工单)
				nextNum = initialNum.add(genRate);
			}
		} else {
			// 固定计划
			if (preStartNum == null) {
				// 下一工单读数 ＝ 起始工单读数 ＋ 新的频率
				if (initialNum != null) {
					nextNum = initialNum.add(genRate);
				}
			} else {
				// 下一工单读数 ＝ 上一工单开始读数 ＋ 新的频率
				nextNum = preStartNum.add(genRate);

			}
		}
		return nextNum;
	}

	/**
	 * 设置上一工单目标日期的时间型的算法，需要生成工单之前计算 计算上一工单目标开始日期
	 * 
	 * @param resultvo
	 *            绩效页签VO
	 * @param bUseFreqRule
	 *            是否使用频率规则
	 */
	public static void setPreTargStartDateBeforeMakeWO(PMHeadVO headVO,
			UFDate currDate, boolean bUseFreqRule) {
		// 使用频率规则的上一工单目标日期的算法
		if (bUseFreqRule) {
			UFDate adjustStartDate = headVO.getAdjust_start_date();
			// 下一工单目标开始日期
			UFDate nextStartDate = headVO.getNext_start_date();
			UFDate preStartDate = null;
			if (adjustStartDate == null) {
				preStartDate = getMaxUFDate(new UFDate[] { nextStartDate,
						currDate });
			} else {
				preStartDate = getMaxUFDate(new UFDate[] { adjustStartDate,
						currDate });
			}
			headVO.setPre_start_date(preStartDate);
		} else {
			// 上一工单目标日期等于当前日期
			headVO.setPre_start_date(currDate);
		}
	}

	/**
	 * 下一工单目标开始日期基线的计算方法，
	 * 
	 * @param nextStartDate
	 *            下一工单目标开始日期
	 * @param adjustStartDate
	 *            调整后目标开始日期
	 * @param bAdjustPersist
	 *            是否永久调整
	 * @param bUseFreqRule
	 *            是否使用频率规则
	 * @return
	 */
	private static UFDate getNextTargStartDateByTimeRule(
			UFDate adjustStartDate, UFDate nextStartDate, UFDate currDate,
			boolean bAdjustPersist, boolean bUseFreqRule) {
		// 如果时永久调整，则取调整日期作为计算依据
		UFDate baseTargStartDate = null;
		if (!bUseFreqRule) {
			// 如果不使用规则
			baseTargStartDate = currDate;
		} else if (adjustStartDate == null) {
			// 如果调整日期为空
			baseTargStartDate = getMaxUFDate(new UFDate[] { currDate,
					nextStartDate });
		} else if (bAdjustPersist) {
			// 永久调整
			baseTargStartDate = getMaxUFDate(new UFDate[] { currDate,
					adjustStartDate });
		} else {
			// 临时调整
			baseTargStartDate = nextStartDate;
		}
		return baseTargStartDate;
	}

	/**
	 * 生成工单后计算: 上一工单完成日期清空 下一工单目标开始日期和结束日期 下一工单生成日期
	 * 
	 * @param headVO
	 *            表头VO
	 * @param currDate
	 *            但前日期
	 * @param bUseFreqRule
	 *            是否使用频率规则
	 */
	public static void setTimeFreqAfterMakeWO(PMHeadVO headVO, UFDate currDate,
			boolean bUseFreqRule) {
		// 设置上一工单完成日期为空
		headVO.setPre_end_date(null);

		if (headVO.getAlter_flag().booleanValue()) {
			// 可变计划，下一工单开始日期为空,下一工单生成日期为空
			headVO.setNext_start_date(null);
		} else {
			UFDate adjustStartDate = headVO.getAdjust_start_date();
			UFDate nextStartDate = headVO.getNext_start_date();
			boolean bAdjustPersist = headVO.getPersist_flag().booleanValue();
			Integer periods = headVO.getPeriods();
			Integer periods_unit = headVO.getPeriods_unit();
			if (periods != null && periods_unit != null) {
				// 取基准日期
				UFDate baseTargStartDate = getNextTargStartDateByTimeRule(
						adjustStartDate, nextStartDate, currDate,
						bAdjustPersist, bUseFreqRule);
				UFDate nextTargStartDate = PMCommonUtils.getDateByFre(periods,
						periods_unit, baseTargStartDate);
				headVO.setNext_start_date(nextTargStartDate);
			}

		}
		// 清空调整信息
		if (headVO.getAdjust_start_date() != null) {
			headVO.setPersist_flag(UFBoolean.FALSE);
			headVO.setPk_adjuster(null);
			headVO.setAdjust_date(null);
			headVO.setAdjust_start_date(null);
		}
		// 设置下一工单生成日期
		setNextCreateWODate(headVO);
	}

	/**
	 * 编辑周期和可变计划时获取下一工单开始日期
	 * 
	 * @param periods
	 * @param periodsUnit
	 * @param preStartDate
	 * @param preEndDate
	 * @param alterFlag
	 * @return
	 */
	public static UFDate getNextTargStartDate(Integer periods,
			Integer periodsUnit, UFDate preStartDate, UFDate preEndDate,
			UFDate initialDate, UFBoolean alterFlag) {
		UFDate next_start_date = null;
		if (periods != null && periodsUnit != null && preStartDate != null) {
			if (alterFlag.booleanValue()) {
				if (preEndDate != null) {
					// 下一工单到期日期等于上一工单完成日期 ＋ 新的频率
					next_start_date = PMCommonUtils.getDateByFre(periods,
							periodsUnit, preEndDate);
				}
			} else {
				// 下一工单到期日期等于上一工单开始日期 ＋ 新的频率
				next_start_date = PMCommonUtils.getDateByFre(periods,
						periodsUnit, preStartDate);

			}
		} else if (initialDate != null) {
			next_start_date = initialDate;
		}
		return next_start_date;

	}

	/**
	 * 刷新测量点最新读数
	 * 
	 * @param billVOs
	 * @throws BusinessException
	 */
	public static void setMeasureResultLatest(PMBillVO[] billVOs)
			throws BusinessException {
		ArrayList<String> alMeasurebid = new ArrayList<String>();
		// 获取测量点表体主键
		for (PMBillVO billVO : billVOs) {
			// 绩效页签VO
			SuperVO[] resultVOs = (SuperVO[]) billVO
					.getChildren(PMResultVO.class);
			// 获取测量点表体主键
			if (ArrayUtils.isNotEmpty(resultVOs)) {
				for (SuperVO superBodyVO : resultVOs) {
					// 测量点表体主键
					String pk_measure_point_b = ((PMResultVO) superBodyVO)
							.getPk_measure_point_b();
					if (pk_measure_point_b != null) {
						alMeasurebid.add(pk_measure_point_b);
					}
				}
			}
		}

		if (alMeasurebid.size() <= 0) {
			return;
		}
		// 查询测量点表体VO
		Map<String, MeaspointBodyVO> hmBodyVO = AMProxy.lookup(
				IMeaspointPubService.class).queryMeasPointBodyVosMap(
				alMeasurebid.toArray(new String[0]));

		// 将测量点的读数设置到表体VO上
		for (PMBillVO billVO : billVOs) {
			SuperVO[] resultVOs = (SuperVO[]) billVO
					.getChildren(PMResultVO.class);
			if (ArrayUtils.isNotEmpty(resultVOs)) {
				// 设置每一行的测量点读数
				for (SuperVO superBodyVO : resultVOs) {
					PMResultVO resultVO = (PMResultVO) superBodyVO;
					String pk_measure_point_b = resultVO
							.getPk_measure_point_b();
					if (pk_measure_point_b != null) {
						// 设置测量点最新读数
						resultVO.setMeasure_result(hmBodyVO.get(
								pk_measure_point_b).getLast_meas_result());
					}
				}
			}
		}
	}

}

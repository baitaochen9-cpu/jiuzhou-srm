package nc.bs.mmpps.calc.bp.sum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.mmpps.calc.bp.result.SumResult;
import nc.bs.mmpps.calc.bp.utils.CalcUFDoubleUtil;
import nc.bs.mmpps.calc.bp.utils.StructVFreeUtil;
import nc.vo.mmpps.calc.entity.CalculateContext;
import nc.vo.mmpps.calc.entity.calculate.DemandVO;
import nc.vo.mmpps.calc.entity.calculate.MaterialVO;
import nc.vo.mmpps.calc.entity.calculate.SumVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 汇总表构建类
 * 
 * @since 6.3
 * @version 2012-8-6 上午09:46:12
 * @author dengsw
 */
public class SumConstruct {

	public static String[] SUMNUMFIELDS = new String[] { SumVO.NBEGINPABNUM,
			SumVO.NENDPABNUM, SumVO.NFACTDEMANDNUM, SumVO.NGROSSDEMANDNUM,
			SumVO.NNETDEMANDNUM, SumVO.NPLANACCEPTNUM, SumVO.NPLANINPUTNUM,
			SumVO.NPLANOUTPUTNUM, SumVO.NPREDEMANDNUM, SumVO.NRESERVATIONNUM,
			SumVO.NSDEMANDNUM/* 替代需求 */, SumVO.NSSUPPLYNUM/* 替代供给 */,
			SumVO.NEXPLANACCEPTNUM /* 超期计划接收量 */,
			SumVO.NSUPPLYASSIGNNUM/* 供给已分配量 */, SumVO.NRELDEMANDNUM
	/* 相关需求量 */
	};

	public static SumVO createSumVO(CalculateContext context,
			DemandVO demandVO, MaterialVO materialVO) {
		SumVO sumVO = new SumVO();
		// 集团
		sumVO.setPk_group(demandVO.getPk_group());
		// 组织
		sumVO.setPk_org(demandVO.getPk_org());
		// 组织版本
		sumVO.setPk_org_v(demandVO.getPk_org_v());
		// 物料oid
		sumVO.setCmaterialid(demandVO.getCmaterialid());
		// 物料范围ID
		sumVO.setCppsmaterialid(demandVO.getCppsmaterialid());
		// 分类标识
		sumVO.setCgroupkey(demandVO.getCgroupkey());
		// 运算标识
		sumVO.setComputecode(context.getCalculateCode());
		StructVFreeUtil.copyStuctMatchKeyValues(demandVO, sumVO, materialVO);

		return sumVO;
	}

	/**
	 * 根据物料范围和数量缓存保存汇总表 创建汇总表VO实时加入SumResult防止太多的东西放在内存里面导致溢出
	 * 
	 * @param materialVOs
	 */
	public static List<SumVO> createSumVOs(CalculateContext context,
			Collection<MaterialVO> materialVOs, SumTempResult sumTempResult,
			SumResult sumResult) {

		List<SumVO> sumVOs = new ArrayList<SumVO>();
		Map<String, SumNumVO> sumNumMap = sumTempResult.getSumNumVOMap();
		Map<String, SumVO> sumVOMap = sumTempResult.getSumVOMap();
		Map<String, Set<String>> grpKeyMap = sumTempResult.getGroupKeyMap();
		if (materialVOs == null) {
			return sumVOs;
		}
		// 物料
		for (MaterialVO materialVO : materialVOs) {
			if (null == grpKeyMap) {
				continue;
			}
			// 汇总表是开始工作日-供应截止日期自然日减1【需求供应开始日期不满足需要】
			List<UFDate> days = context.getWorkDateCache().getWorkDaysBetween(
					context.getStartWorkDate(),
					materialVO.getDsupplyend().getDateBefore(1));
			Set<String> groupKeys = grpKeyMap.get(materialVO.getCmaterialid());
			if (null == groupKeys) {
				continue;
			}
			// 汇总维度
			for (String grpKey : groupKeys) {
				SumVO sumVO = sumVOMap.get(grpKey);
				boolean isPAB = true;
				// 每一个工作日
				UFDouble startPab = UFDouble.ZERO_DBL;
				UFDouble endPab = UFDouble.ZERO_DBL;
				
				for (UFDate workday : days) {
					SumNumVO sumNumVO = sumNumMap.get(grpKey
							+ workday.toString());

					if (sumNumVO != null) {
						sumNumVO.nullToZero();
						if (isPAB) {
							startPab = sumNumVO.getPabNum();
						} else {
							//2023-10-20 liyf 后续的供应如果库存，前面的应该也要体现出来
							startPab = endPab;
//							startPab = endPab.add(sumNumVO.getPabNum()) ;
						}
					} else {
						startPab = endPab;
					}

					SumVO daySumVO = SumConstruct.createEachWorkDaySum(sumVO,
							sumNumVO, isPAB, materialVO, workday, startPab);

					endPab = daySumVO.getNendpabnum();
					// 只有第一笔为true
					if (isPAB) {
						isPAB = false;
					}
					sumResult.add(daySumVO);// 及时插入防止内存溢出
				}
			}
		}

		return sumVOs;
	}

	/**
	 * 根据汇总信息创建每天汇总表信息
	 * 
	 * @param srcSumVO
	 * @param sumNumVO
	 * @param isPAB
	 * @param materialVO
	 * @param workday
	 * @return
	 */
	private static SumVO createEachWorkDaySum(SumVO srcSumVO,
			SumNumVO sumNumVO, boolean isPAB, MaterialVO materialVO,
			UFDate workday, UFDouble startpab) {
		UFDouble endpab = UFDouble.ZERO_DBL;
		SumVO daysumVO = (SumVO) srcSumVO.clone();
		CalcUFDoubleUtil.nullToZero(daysumVO, SumConstruct.SUMNUMFIELDS);
		daysumVO.setDemandtime(workday);
		// 有数量记录 表示有需求或者供应
		if (sumNumVO != null) {
			sumNumVO.nullToZero();
			daysumVO.setNbeginpabnum(startpab);
			SumConstruct.setSumQty(daysumVO, sumNumVO,
					materialVO.getSafetystocknum());
			endpab = SumConstruct.calculateEndPab(daysumVO);

		} else {
			daysumVO.setNbeginpabnum(startpab);
			endpab = startpab;
		}

		daysumVO.setNendpabnum(endpab);
		return daysumVO;
	}

	/**
	 * 设置 计划产出量、计划接收量、毛需求、预留量、净需求、计划投入量
	 * 
	 * @param sumVO
	 * @param sumNumVO
	 */
	private static void setSumQty(SumVO sumVO, SumNumVO sumNumVO,
			UFDouble safetystockNum) {

		// 计划独立需求量
		sumVO.setNpredemandnum(sumNumVO.getNpredemandnum());
		// 实际需求量
		sumVO.setNfactdemandnum(sumNumVO.getNfactdemandnum());
		// 相关需求量
		UFDouble nreldemandnum = sumNumVO.getNreldemandnum();
		sumVO.setNreldemandnum(nreldemandnum);
		// 替代需求
		UFDouble subsDemandnum = sumNumVO.getNsubsDemandNum();
		sumVO.setNsdemandnum(subsDemandnum);
		// 毛需求
		UFDouble grossdemandNum = sumNumVO.getGrossDemandNum();
		// 毛需求 = 计划独立需求量 + 实际需求量 ［包括预留数量］+ 相关需求量 +　替代需求
		sumVO.setNgrossdemandnum(grossdemandNum);

		// 预留量
		UFDouble reservationNum = sumNumVO.getReservationNum();
		sumVO.setNreservationnum(reservationNum);
		// 期初PAB
		UFDouble beginpabNum = sumVO.getNbeginpabnum();
		// 计划接收量
		UFDouble planacceptNum = sumNumVO.getPlanAcceptNum();
		sumVO.setNplanacceptnum(planacceptNum);
		// 超期计划接收量
		UFDouble nexplanacceptnum = sumNumVO.getNexplanacceptnum();
		sumVO.setNexplanacceptnum(nexplanacceptnum);
		// 供给已分配量
		UFDouble nsupassignednum = sumNumVO.getNsupassignednum();
		sumVO.setNsupplyassignnum(nsupassignednum);
		// 替代供给
		UFDouble subsSupplynum = sumNumVO.getNsubsSupplyNum();
		sumVO.setNssupplynum(subsSupplynum);

		// 计划投入量
		sumVO.setNplaninputnum(sumNumVO.getPlanInputNum());
		// 计划产出量
		UFDouble planoutputNum = sumNumVO.getPlanOutputNum();
		sumVO.setNplanoutputnum(planoutputNum);

		// BUGV61 不能使用供需匹配中的需求缺口数量作为净需求
		// 比如 D1 3.5 10 D2 3.6 10 经济批量是15
		// D1 10 PLO1 15 D2的缺口是5 净需求变成 15，其实净需求应该是20

		// 净需求 =（当期毛需求-已分配量）＋安全库存（如果方案定义考虑）－（期初PAB＋当期计划接收量）
		UFDouble netdemandNum = sumVO.getNgrossdemandnum().sub(reservationNum)
				.add(CalcUFDoubleUtil.nullToZero(safetystockNum))
				.sub(beginpabNum.add(planacceptNum));
		if (netdemandNum.compareTo(UFDouble.ZERO_DBL) < 0) {
			// 处理负值
			netdemandNum = UFDouble.ZERO_DBL;
		}

		sumVO.setNnetdemandnum(netdemandNum);
	}

	/**
	 * 计算期末PAB
	 * 
	 * @param sum
	 * @return
	 */
	private static UFDouble calculateEndPab(SumVO sum) {
		UFDouble npab = sum.getNbeginpabnum() == null ? UFDouble.ZERO_DBL : sum
				.getNbeginpabnum();
		UFDouble nplannedOrderReceipts = sum.getNplanoutputnum();
		UFDouble nplanaccept = sum.getNplanacceptnum();
		UFDouble ngross = sum.getNgrossdemandnum();
		UFDouble nreserved = sum.getNreservationnum();
		// 替代供给
		UFDouble subsSupplynum = sum.getNssupplynum();

		// 期末PAB=期初PAB+当期计划接收量+替代供给量+当期计划产出量-（当期毛需求-已分配量）
		UFDouble nendpab = npab.add(nplanaccept).add(subsSupplynum)
				.add(nplannedOrderReceipts).sub(ngross.sub(nreserved));

		return nendpab;
	}

}

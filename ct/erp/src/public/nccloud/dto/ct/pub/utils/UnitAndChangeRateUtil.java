package nccloud.dto.ct.pub.utils;

import java.util.ArrayList;
import java.util.List;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.uitl.ArrayUtil;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-22 上午9:55:17
 * @version ncc1.0
 **/
public class UnitAndChangeRateUtil {
	/**
	 * 查询换算率
	 */
	public static String getChangeRate(int row, ExtBillUtil util, String feild) {
		String unit = util.getBodyStringValue(row, CtAbstractBVO.CUNITID);
		String astunit = util.getBodyStringValue(row, feild);
		String changeRate = "1/1";
		if (unit.equals(astunit)) {
			return changeRate;
		}
		String pk_material = util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL);

		changeRate = MaterialPubService.queryMainMeasRateByMaterialAndMeasdoc(pk_material, astunit);

		// 查不到返回默认？
		if (changeRate == null) {
			changeRate = "1/1";
		}
		return changeRate;
	}

	public static boolean isFixedChangeRate(String pk_material, String astUnit) {

		return MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(pk_material, astUnit);

	}

	/**
	 * 查询是否固定换算率
	 */
	public static boolean isFixUnitRate(String material, String cunitid, String castunitid) {
		boolean flag = true;
		if (material == null || cunitid == null || castunitid == null) {
			return flag;
		}

		flag = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(material, cunitid, castunitid);

		return flag;
	}

	/**
	 * 设置换算率
	 */
	public static void setChangeRate(ExtBillUtil util, int[] rows) {
		if (ArrayUtil.isEmpty(rows)) {
			return;
		}
		String oldChangeRate = null;
		String newChangeRate = null;
		List<Integer> newRateRows = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			oldChangeRate = util.getBodyStringValue(rows[i], CtAbstractBVO.VCHANGERATE);
			newChangeRate = UnitAndChangeRateUtil.getChangeRate(rows[i], util, CtAbstractBVO.CASTUNITID);
			if (newChangeRate.equals(oldChangeRate)) {
				return;
			}
			util.setBodyValue(rows[i], CtAbstractBVO.VCHANGERATE, newChangeRate);
			newRateRows.add(Integer.valueOf(rows[i]));
		}
		// 换算率联动其他
		if (newRateRows.size() > 0) {
			new RelationCalculate().calculate(util, rows, CtAbstractBVO.VCHANGERATE);
		}
	}

	/**
	 * 方法功能描述：设置报价单位换算率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param helper
	 * @param rows
	 *          <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-12 下午01:39:06
	 */
	public static void setCqtChangeRate(ExtBillUtil util, int[] rows) {
		if (ArrayUtil.isEmpty(rows)) {
			return;
		}
		String oldChangeRate = null;
		String newChangeRate = null;
		List<Integer> newRateRows = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			oldChangeRate = util.getBodyStringValue(rows[i], CtAbstractBVO.VQTUNITRATE);
			newChangeRate = UnitAndChangeRateUtil.getChangeRate(rows[i], util, CtAbstractBVO.CQTUNITID);
			if (newChangeRate.equals(oldChangeRate)) {
				return;
			}
			util.setBodyValue(rows[i], CtAbstractBVO.VQTUNITRATE, newChangeRate);
			newRateRows.add(Integer.valueOf(rows[i]));
		}
		// 换算率联动其他
		if (newRateRows.size() > 0) {
			new RelationCalculate().calculate(util, rows, CtAbstractBVO.VQTUNITRATE);
		}
	}
}

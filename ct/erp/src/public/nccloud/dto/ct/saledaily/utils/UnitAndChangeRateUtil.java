package nccloud.dto.ct.saledaily.utils;

import java.util.ArrayList;
import java.util.List;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.uitl.ArrayUtil;
import nc.vo.scmf.pub.keyvalue.IKeyValue;
import nc.vo.scmf.pub.keyvalue.VOKeyValue;

/**
 * @description 单位换算率
 * @author wangshrc
 * @date 2019年2月15日 下午6:58:01
 * @version ncc1.0
 */
public class UnitAndChangeRateUtil {

	/**
	 * 方法功能描述： 查询换算率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param row
	 * @param util
	 * @param feild
	 *            报价单位字段或者 单位字段
	 * @return <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-12 下午01:49:46
	 */
	public static String getChangeRate(int row, IKeyValue keyValue, String feild) {
		String unit = keyValue.getBodyStringValue(row, CtAbstractBVO.CUNITID);
		String astunit = keyValue.getBodyStringValue(row, feild);
		String changeRate = "1/1";
		if (unit.equals(astunit)) {
			return changeRate;
		}
		String pk_material = keyValue.getBodyStringValue(row,
				CtAbstractBVO.PK_MATERIAL);

		changeRate = MaterialPubService.queryMainMeasRateByMaterialAndMeasdoc(
				pk_material, astunit);

		// 查不到返回默认？
		if (changeRate == null) {
			changeRate = "1/1";
		}
		return changeRate;
	}

	public static boolean isFixedChangeRate(String pk_material, String astUnit) {

		return MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(
				pk_material, astUnit);

	}

	/**
	 * 方法功能描述：查询是否固定换算率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param panel
	 * @param row
	 * @return <p>
	 * @since 6.0
	 * @author lizhengb
	 * @time 2010-4-1 下午01:16:33
	 */
	public static boolean isFixUnitRate(String material, String cunitid,
			String castunitid) {
		boolean flag = true;
		if (material == null || cunitid == null || castunitid == null) {
			return flag;
		}

		flag = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(
				material, cunitid, castunitid);

		return flag;
	}

	/**
	 * ' 方法功能描述：设置换算率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param helper
	 * @param rows
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-12 下午01:40:50
	 */
	public static void setChangeRate(AggCtSaleVO billvo, int[] rows) {
		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		if (ArrayUtil.isEmpty(rows)) {
			return;
		}
		String oldChangeRate = null;
		String newChangeRate = null;
		List<Integer> newRateRows = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			oldChangeRate = keyValue.getBodyStringValue(rows[i],
					CtAbstractBVO.VCHANGERATE);
			newChangeRate = UnitAndChangeRateUtil.getChangeRate(rows[i],
					keyValue, CtAbstractBVO.CASTUNITID);
			if (newChangeRate.equals(oldChangeRate)) {
				return;
			}
			keyValue.setBodyValue(rows[i], CtAbstractBVO.VCHANGERATE,
					newChangeRate);
			newRateRows.add(Integer.valueOf(rows[i]));
		}
		// 换算率联动其他
		if (newRateRows.size() > 0) {
			SaleRelationCalculate cal = new SaleRelationCalculate();
			cal.calculate(billvo, CtAbstractBVO.VCHANGERATE);
		}
	}

	/**
	 * 方法功能描述：设置报价单位换算率
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param helper
	 * @param rows
	 *            <p>
	 * @since 6.0
	 * @author liuchx
	 * @time 2010-6-12 下午01:39:06
	 */
	public static void setCqtChangeRate(AggCtSaleVO billvo, int[] rows) {
		IKeyValue keyValue = new VOKeyValue<AggCtSaleVO>(billvo);
		if (ArrayUtil.isEmpty(rows)) {
			return;
		}
		String oldChangeRate = null;
		String newChangeRate = null;
		List<Integer> newRateRows = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			oldChangeRate = keyValue.getBodyStringValue(rows[i],
					CtAbstractBVO.VQTUNITRATE);
			newChangeRate = UnitAndChangeRateUtil.getChangeRate(rows[i],
					keyValue, CtAbstractBVO.CQTUNITID);
			if (newChangeRate.equals(oldChangeRate)) {
				return;
			}
			keyValue.setBodyValue(rows[i], CtAbstractBVO.VQTUNITRATE,
					newChangeRate);
			newRateRows.add(Integer.valueOf(rows[i]));
		}
		// 换算率联动其他
		if (newRateRows.size() > 0) {
			SaleRelationCalculate cal = new SaleRelationCalculate();
			cal.calculate(billvo, CtAbstractBVO.VQTUNITRATE);
		}
	}

}

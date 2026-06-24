package nccloud.dto.ct.pub.utils;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.util.RelationCalculateUtil;
import nc.vo.pubapp.calculator.Calculator;
import nc.vo.pubapp.calculator.Condition;
import nc.vo.pubapp.calculator.data.IDataSetForCal;
import nc.vo.pubapp.calculator.data.IRelationForItems;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.vo.pubapp.scale.ScaleUtils;

/**
 * @description 单价金额联动
 * @author xiahui
 * @date 创建时间：2019-1-22 上午9:00:27
 * @version ncc1.0
 **/
public class RelationCalculate {
	public void calculate(ExtBillUtil util, int[] rows, String itemKey) {

		IRelationForItems item = new CtRelationItemForCal();
		item.setNnetpriceKey(CtAbstractBVO.NGPRICE);
		item.setNtaxnetpriceKey(CtAbstractBVO.NGTAXPRICE);
		ScaleUtils scale = ScaleUtils.getScaleUtilAtBS();

		String pk_group = util.getHeadTailStringValue(CtAbstractVO.PK_GROUP);
		boolean isTaxPrior = RelationCalculateUtil.isTaxPrior(pk_group);
		boolean isGlobalEnable = RelationCalculateUtil.isGlobalEnable();
		boolean isGroupEnable = RelationCalculateUtil.isGroupEnable(pk_group);
		boolean isOrigCurToGroupMoney = RelationCalculateUtil.isOrigCurToGroupMoney(pk_group);
		boolean isOrigCurToGlobalMoney = RelationCalculateUtil.isOrigCurToGlobalMoney();

		boolean isInternational = false;
		boolean isFbuysellflagHead = false;

		Integer fbuysellflag = ValueUtils.getInteger(util.getHeadValue(CTVatNameConst.FBUYSELLFLAG));
		if (null != fbuysellflag) {
			isInternational = RelationCalculateUtil.isInternational(fbuysellflag);
			isFbuysellflagHead = true;
		}

		Integer matCtl = CtTransBusitypesUtil.getNinvctlstyle(util);

		for (int row : rows) {
			// 创建数据集实例 初始化数据关系计算用的数据集
			IDataSetForCal data = new CtVODataSet(util, row, item, matCtl);
			Calculator tool = new Calculator(data, scale);//
			// 创建参数实例，在计算的时候用来获得参数条件：是否含税优先等
			Condition cond = new Condition();// 创建参数实例
			// 设置是否进行本币换算
			cond.setIsCalLocalCurr(true);
			// 设置调单价方式调折扣
			cond.setIsChgPriceOrDiscount(false);
			String pk_material = util.getBodyStringValue(row, CtAbstractBVO.PK_MATERIAL);
			String cunitid = util.getBodyStringValue(row, CtAbstractBVO.CUNITID);
			String castunitid = util.getBodyStringValue(row, CtAbstractBVO.CASTUNITID);
			String cqtunitid = util.getBodyStringValue(row, CtAbstractBVO.CQTUNITID);
			// 设置是否固定单位换算率
			cond.setIsFixNchangerate(UnitAndChangeRateUtil.isFixUnitRate(pk_material, cunitid, castunitid));
			// 是否固定报价单位换算率
			cond.setIsFixNqtunitrate(UnitAndChangeRateUtil.isFixUnitRate(pk_material, cunitid, cqtunitid));

			// 设置含税优先还是无税优先
			cond.setIsTaxOrNet(isTaxPrior);
			// 设置全局本币是否启用
			cond.setGlobalLocalCurrencyEnable(isGlobalEnable);
			// 设置集团本币是否启用
			cond.setGroupLocalCurrencyEnable(isGroupEnable);
			// 用原币折算全局本位币金额（否则用组织本位币）
			cond.setOrigCurToGlobalMoney(isOrigCurToGlobalMoney);
			// 用原币折算集团本位币金额（否则用组织本位币）
			cond.setOrigCurToGroupMoney(isOrigCurToGroupMoney);

			if (!isFbuysellflagHead) {
				isInternational = RelationCalculateUtil.isInternational((Integer) util.getBodyValue(row,
						CTVatNameConst.FBUYSELLFLAG));
			}
			// 是否跨国
			cond.setInternational(isInternational);

			// 不可抵扣税率联动计算时设置为FALSE 否则会进行联动计算将主本币无税单价设置为空
			if (CTVatNameConst.NNOSUBTAXRATE.equals(itemKey) || CTVatNameConst.NCALTAXMNY.equals(itemKey)
					|| CtAbstractBVO.NTAX.equals(itemKey)) {
				cond.setCalOrigCurr(false);
			}

			// 两个参数 cond 为计算时的参数条件
			tool.calculate(cond, itemKey);
		}
	}

}

package nccloud.dto.ct.pub.utils;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.util.RelationCalculateUtil;

/**
 * @description 根据含税或无税优先参数，确定联动计算的单价
 * @author xiahui
 * @date 创建时间：2019-1-21 下午4:57:19
 * @version ncc1.0
 **/
public class ReCalculateByPriceUtil {

	public void reCalculate(ExtBillUtil util, int[] rows) {
		boolean isTaxPrior = RelationCalculateUtil.isTaxPrior(util.getHeadTailStringValue(CtAbstractVO.PK_GROUP));
		RelationCalculate calcul = new RelationCalculate();
		if (isTaxPrior) {
			calcul.calculate(util, rows, CtAbstractBVO.NQTORIGTAXPRICE);
		} else {
			calcul.calculate(util, rows, CtAbstractBVO.NQTORIGPRICE);
		}
	}
}

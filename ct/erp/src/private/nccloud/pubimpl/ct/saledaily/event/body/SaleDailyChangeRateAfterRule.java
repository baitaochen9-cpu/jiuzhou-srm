package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.rule.SaleRelationCalculate;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.pub.lang.UFDouble;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同换算率
 * @author wangshrc
 * @date 2019年2月14日 下午2:15:48
 * @version ncc1.0
 */
public class SaleDailyChangeRateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		if (event.getChangrows().length <= 0)
			return billvo;
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		// 当单位与报价单位相同 并且 换算率不相同时，设置换算率也要相等。
		String castUnitID = bvos[event.getRow()].getCastunitid();
		String cqtUnitID = bvos[event.getRow()].getCqtunitid();
		String keyValue = event.getChangrows()[0].getNewvalue().toString();
		// 换算率发动联动计算
		SaleRelationCalculate cal = new SaleRelationCalculate();
		cal.calculate(billvo, event.getChangeKey());
		if (StringUtils.equals(castUnitID, cqtUnitID)) {
			// 当key为换算率
			if (StringUtils.equals(event.getChangeKey(),
					CtAbstractBVO.VCHANGERATE)) {
				UFDouble nastNum = bvos[event.getRow()].getNastnum();
				bvos[event.getRow()].setNqtunitnum(nastNum);
			} else {
				// 当key 为报价换算率
				UFDouble nastNum = bvos[event.getRow()].getNastnum();
				bvos[event.getRow()].setNastnum(nastNum);
			}

			bvos[event.getRow()].setAttributeValue(event.getChangeKey(),
					keyValue);

		}
		return billvo;
	}

}

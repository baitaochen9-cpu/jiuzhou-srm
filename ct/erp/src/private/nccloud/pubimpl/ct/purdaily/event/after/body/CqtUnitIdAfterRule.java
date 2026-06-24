package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.UnitAndChangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 报价单位改变 首先改变报价换算率 启动报价单位换算率的联动计算
 * @author xiahui
 * @date 创建时间：2019-1-22 上午11:41:25
 * @version ncc1.0
 **/
public class CqtUnitIdAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		String astUnit = util.getBodyStringValue(row, CtAbstractBVO.CQTUNITID);
		if (!StringUtil.isEmptyTrimSpace(astUnit)) {
			UnitAndChangeRateUtil.setCqtChangeRate(util, new int[] { row });
		}

		return billvo;
	}

}

package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.uitl.StringUtil;
import nccloud.dto.ct.saledaily.utils.UnitAndChangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 报价单位编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午6:42:04
 * @version ncc1.0
 */
public class SaleDailyCqtUnitAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		if (event.getChangrows().length <= 0)
			return billvo;
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		String astUnit = bvos[event.getRow()].getCqtunitid();
		if (StringUtil.isEmptyTrimSpace(astUnit)) {
			return billvo;
		}
		UnitAndChangeRateUtil.setCqtChangeRate(billvo,
				new int[] { event.getRow() });
		return billvo;
	}

}

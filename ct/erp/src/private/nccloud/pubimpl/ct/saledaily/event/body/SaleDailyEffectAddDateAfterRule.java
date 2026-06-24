package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 延期天数
 * @author wangshrc
 * @date 2019年2月15日 上午11:21:23
 * @version ncc1.0
 */
public class SaleDailyEffectAddDateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		SalePayTermUtil.setPlanEndDate(billvo, event.getRow());
		return billvo;
	}

}

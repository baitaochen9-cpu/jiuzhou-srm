package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 账期天数编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午10:35:59
 * @version ncc1.0
 */
public class SaleDailyPayMentDayAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		if (bvos[row].getPaymentday() != null) {
			bvos[row].setCheckdata(null);
			bvos[row].setEffectmonth(null);
			bvos[row].setEffectaddmonth(null);
			bvos[row].setAccountday(null);
		}
		SalePayTermUtil.setPlanEndDate(billvo, event.getRow());
		return billvo;
	}

}

package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 付款协议表体页签附加月编辑后事件：编辑附加月后，判断附加月是否为空
 *              ，如果附加月为空设置默认值为0；其次判断账期天数是否为空，不为空则清空账期天数。
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:39:47
 * @version ncc1.0
 **/
public class CTEffectAddMonthAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 附加月
		Object effectaddmonth = util.getBodyValue(row, CtPaymentVO.EFFECTADDMONTH);
		if (ValueUtil.isEmpty(effectaddmonth)) {
			// 设置附加月默认值为0
			util.setBodyValue(row, CtPaymentVO.EFFECTADDMONTH, 0);
		}
		if (!ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.PAYMENTDAY))) {
			// 清空账期天数
			util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, null);
		}

		return billvo;
	}

}

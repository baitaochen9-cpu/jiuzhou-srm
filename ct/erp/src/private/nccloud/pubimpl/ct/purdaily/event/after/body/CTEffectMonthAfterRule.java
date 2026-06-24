package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.enumeration.EnumCtEffectMonth;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 付款协议表体页签生效月编辑后事件：编辑生效月后，判断生效月是否为空
 *              ，如果生效月为空设置默认值为当月生效；判断账期天数是否为空，不为空则清空账期天数。
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:39:40
 * @version ncc1.0
 **/
public class CTEffectMonthAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 生效月
		Object effectmonth = util.getBodyValue(row, CtPaymentVO.EFFECTMONTH);
		if (ValueUtil.isEmpty(effectmonth)) {
			// 设置生效月默认值为当月生效
			util.setBodyValue(row, CtPaymentVO.EFFECTMONTH, EnumCtEffectMonth.CURMONTHEFFECT.toIntValue());
		}
		if (!ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.PAYMENTDAY))) {
			// 清空账期天数
			util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, null);
		}

		return billvo;
	}

}

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
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:39:32
 * @version ncc1.0
 **/
public class CTCheckDateAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 固定结账日
		Object checkdata = util.getBodyValue(row, CtPaymentVO.CHECKDATA);
		if (!ValueUtil.isEmpty(checkdata)) {
			// 如果固定结账日不为空，则生效月、附加月不能为空
			if (ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.EFFECTMONTH))) {
				util.setBodyValue(row, CtPaymentVO.EFFECTMONTH, EnumCtEffectMonth.CURMONTHEFFECT.toIntValue());
			}
			if (ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.EFFECTADDMONTH))) {
				util.setBodyValue(row, CtPaymentVO.EFFECTADDMONTH, 0);
			}
			// 清空账期天数
			util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, null);
		} else {
			util.clearRowValueByItemKeys(row, new String[] { CtPaymentVO.EFFECTMONTH, CtPaymentVO.EFFECTADDMONTH });
			if (ValueUtil.isEmpty(util.getBodyValue(event.getRow(), CtPaymentVO.OUTACCOUNTDATE))) {
				if (ValueUtil.isEmpty(util.getBodyValue(event.getRow(), CtPaymentVO.PAYMENTDAY))) {
					// 设置账期天数默认值
					util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, 0);
				}
			}
		}

		return billvo;
	}

}

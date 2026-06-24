package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 付款协议表体页签固定结账日编辑后事件: 编辑出账日后，出账日为空，并且出账日为空,如果账期天数也为空则设置账期天数默认值为0。
 *              否则清空账期天数。
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:38:34
 * @version ncc1.0
 **/
public class CTOutAccountDateAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 出账日
		Object outaccountdate = util.getBodyValue(row, CtPaymentVO.OUTACCOUNTDATE);
		if (!ValueUtil.isEmpty(outaccountdate)) {
			// 清空账期天数
			util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, null);
		} else {
			if (ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.CHECKDATA))
					&& ValueUtil.isEmpty(util.getBodyValue(row, CtPaymentVO.PAYMENTDAY))) {
				// 设置账期天数默认值
				util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, 0);
			}
		}

		return billvo;
	}

}

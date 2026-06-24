package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPaymentVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 
 *              付款协议表体页签账期天数编辑后事件：编辑账期天数后，如果账期天数不为空，则清空固定结账日、生效月、附加月。账期天数为空设置默认值0
 *              。
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:39:18
 * @version ncc1.0
 **/
public class CTPaymentDayAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 账期天数
		Object paymentday = util.getBodyValue(row, CtPaymentVO.PAYMENTDAY);
		if (!ValueUtil.isEmpty(paymentday)) {
			// 账期天数不为空，清空出账日、固定结账日、生效月、附加月
			util.clearRowValueByItemKeys(row, new String[] { CtPaymentVO.OUTACCOUNTDATE, CtPaymentVO.CHECKDATA,
					CtPaymentVO.EFFECTMONTH, CtPaymentVO.EFFECTADDMONTH });
		} else {
			// 账期天数为空设置默认值0
			util.setBodyValue(row, CtPaymentVO.PAYMENTDAY, 0);
		}

		return billvo;
	}

}

package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ct.uitl.ValueUtil;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 结账日编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午10:15:22
 * @version ncc1.0
 */
public class SaleDailyCheckDataAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		// 如果填写了固定结账日，需要清空账期天数
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		if (!ValueUtil.isEmpty(bvos[row].getCheckdata())) {
			bvos[row].setPaymentday(null);
			// 设置生效月,附加月默认值
			if (ValueUtil.isEmpty(bvos[row].getEffectmonth())) {
				bvos[row].setEffectmonth("0");

			}
			// 附加月默认值
			if (ValueUtil.isEmpty(bvos[row].getEffectaddmonth())) {
				bvos[row].setEffectaddmonth(Integer.valueOf(0));
			}
		} else {
			bvos[row].setEffectmonth(null);
			bvos[row].setEffectaddmonth(null);
			bvos[row].setAccountday(null);
		}
		SalePayTermUtil.setPlanEndDate(billvo, event.getRow());
		return billvo;
	}

}

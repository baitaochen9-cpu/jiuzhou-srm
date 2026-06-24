package nccloud.web.ct.saledaily.event;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.scmpub.pub.event.ExtAbstractBodyAfterAction;

/**
 * @description 销售合同表体编辑后
 * @author wangshrc
 * @date 2019年2月13日 上午11:13:57
 * @version ncc1.0
 */
public class SaleDailyBodyAfterEvents extends
		ExtAbstractBodyAfterAction<AggCtSaleVO> {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyBodyAfterEventHandler";
	}
	
	@Override
	protected ExtBillCard doAfterForExtBillCard(ExtBillCard extbillcard, Map<String, Object> userObject) {
		SaleDailyPrecisionUtil.dealPrecision(extbillcard);
		return super.doAfterForExtBillCard(extbillcard, userObject);
	}

}

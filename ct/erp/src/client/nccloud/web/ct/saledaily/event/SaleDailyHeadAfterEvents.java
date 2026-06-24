package nccloud.web.ct.saledaily.event;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.scmpub.pub.event.ExtAbstractHeadAfterAction;

/**
 * @description 销售合同表头编辑后
 * @author wangshrc
 * @date 2019年2月13日 上午10:59:19
 * @version ncc1.0
 */
public class SaleDailyHeadAfterEvents extends
		ExtAbstractHeadAfterAction<AggCtSaleVO> {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyHeadAfterEventHandler";
	}
	@Override
	protected ExtBillCard doAfterForExtBillCard(ExtBillCard extbillcard, Map<String, Object> userObject) {
		SaleDailyPrecisionUtil.dealPrecision(extbillcard);
		return super.doAfterForExtBillCard(extbillcard, userObject);
	}
	

}

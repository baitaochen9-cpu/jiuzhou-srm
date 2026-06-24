package nccloud.web.ct.saledaily.action;

import nc.itf.ct.saledaily.ISaledailyApprove;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.BusinessException;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.web.ct.saledaily.utils.SaleDailyCompareUtil;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;

/**
 * @description 销售合同卡片变更删除
 * @author wangshrc
 * @date 2019年2月13日 上午9:34:24
 * @version ncc1.0
 */
public class SaleDailyCardModiDeleteAction extends SaleDailyCardCommonAction {
	@Override
	protected Object action(AggCtSaleVO[] vos) {
		SCMExtBillCardOperator operator = SaleDailyCompareUtil
				.getBillCardOperator();
		ISaledailyApprove service = ServiceLocator
				.find(ISaledailyApprove.class);
		AggCtSaleVO[] results = null;
		try {
			results = service.modiDelete(vos);
			ExtBillCard billcard = operator.toCard(results[0]);
			SaleDailyPrecisionUtil.dealPrecision(billcard);
			return billcard;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@Override
	protected String getPFActionName() {
		return null;
	}
}

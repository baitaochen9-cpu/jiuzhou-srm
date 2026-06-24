package nccloud.web.ct.saledaily.action;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.util.GridCompareUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.pubitf.ct.saledaily.service.ISaleDailyQueryForNCCloudService;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

/**
 * @description 销售合同增行
 * @author wangshrc
 * @date 2019年3月1日 下午2:37:05
 * @version ncc1.0
 */
public class SaleDailyCardAddLineAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		ExtBillCardOperator operator = new ExtBillCardOperator("400600200_card");
		AggCtSaleVO vo = (AggCtSaleVO) operator.toBill(request);
		CtSaleBVO bvo = new CtSaleBVO();
		vo.setCtSaleBVO(new CtSaleBVO[] { bvo });
		ISaleDailyQueryForNCCloudService service = ServiceLocator
				.find(ISaleDailyQueryForNCCloudService.class);
		try {
			service.setAddLineInfo(vo);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		ExtBillCard card = operator.toCard(vo);
		GridCompareUtils.compareExtBillCardGrid(operator.getOriginBillcard(),
				card);
		SaleDailyPrecisionUtil.dealPrecision(card);
		return card;
	}

}

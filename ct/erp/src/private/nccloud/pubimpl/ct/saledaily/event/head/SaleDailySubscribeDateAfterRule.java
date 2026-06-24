package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.saledaily.utils.ExchangeRateUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同签订日期编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午4:00:46
 * @version ncc1.0
 */
public class SaleDailySubscribeDateAfterRule implements
		IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		UFDate subDate = (UFDate) util.getHeadValue(CtAbstractVO.SUBSCRIBEDATE); // 签订日期
		UFDate valDate = (UFDate) util.getHeadValue(CtAbstractVO.VALDATE); // 生效日期

		if (ValueUtil.isEmpty(subDate)) {
			util.setHeadValue(CtAbstractVO.SUBSCRIBEDATE, event.getOldValue());
			ExceptionUtils
					.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("4020003_0",
									"04020003-0034")/*
													 * @res "合同签订日期不能为空！"
													 */);
		}
		// 636 签订日期改变重询汇率
		// 1. 币种改变汇率 2.汇率变动处理
		ExchangeRateUtil.changeSellExchangeRate(billvo);

		if (ValueUtil.isEmpty(valDate)) {
			return billvo;
		} else if (subDate.after(valDate)
				&& !subDate.isSameDate(valDate, Calendars.getGMTDefault())) {
			util.setHeadValue(CtAbstractVO.SUBSCRIBEDATE, event.getOldValue());
			userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0049")/*
															 * @res
															 * "签订日期不能在计划生效日期之后！"
															 */);
		}
		return billvo;
	}

}

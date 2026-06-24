package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.pub.utils.ExchangeRateUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 合同签订日期 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午9:44:18
 * @version ncc1.0
 **/
public class SubscribeDateAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		UFDate subDate = (UFDate) util.getHeadValue(CtAbstractVO.SUBSCRIBEDATE); // 签订日期
		UFDate valDate = (UFDate) util.getHeadValue(CtAbstractVO.VALDATE); // 生效日期

		if (ValueUtil.isEmpty(subDate)) {
			util.setHeadValue(CtAbstractVO.SUBSCRIBEDATE, event.getOldValue());
			userobject
					.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0034")); // 合同签订日期不能为空！
			return billvo;
		}

		// 636 签订日期改变重询汇率
		// 1. 币种改变汇率 2.汇率变动处理
		ExchangeRateUtil.changeSellExchangeRate(util);

		if (ValueUtil.isEmpty(valDate)) {
			return billvo;
		} else if (subDate.after(valDate) && !subDate.isSameDate(valDate, Calendars.getGMTDefault())) {
			util.setHeadValue(CtAbstractVO.SUBSCRIBEDATE, event.getOldValue());
			ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0049")/*
													 * @res "签订日期不能在计划生效日期之后！"
													 */);
		}

		return billvo;
	}

}

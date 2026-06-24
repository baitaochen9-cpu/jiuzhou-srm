package nccloud.web.ct.price.action;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.web.scmpub.pub.event.AbstractBodyAfterAction;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;
/**
 * ±íÌå±à¼­ºó
 * @author zhaoypm
 *
 */
public class CardBodyAfterEditAction extends AbstractBodyAfterAction<AggCtPriceVO> {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.price.event.after.handler.CardBodyAfterEventHandler";
	}

	@Override
	protected BillCard doAfterForBillCard(BillCard billCard, Map<String, Object> userObject) {
		this.processScale(billCard);
		return billCard;
	}
	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body, CtPriceHeaderVO.CORIGCURRENCYID,
				PositionType.Head);
		operator.processPrecision();
	}
}

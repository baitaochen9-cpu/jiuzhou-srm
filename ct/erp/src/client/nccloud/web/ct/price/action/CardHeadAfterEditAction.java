package nccloud.web.ct.price.action;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.web.scmpub.pub.event.AbstractHeadAfterAction;
import nccloud.web.scmpub.pub.event.head.CardHeadAfterEditEventSCM;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;
/**
 * ±íÍ·±à¼­ºó
 * @author zhaoypm
 *
 */
public class CardHeadAfterEditAction extends AbstractHeadAfterAction<AggCtPriceVO> {

	@Override
	public String getClassName() {
		return "nccloud.pubimpl.ct.price.event.after.handler.CardHeadAfterEventHandler";

	}

	@Override
	protected BillCard afterProcess(CardHeadAfterEditEventSCM webevent, PageTemplet templet,
			Map<String, Object> userObject, AggCtPriceVO billvo) {
//		EditRelationItemLoader loader = new EditRelationItemLoader(templet);
		BillCard billcard = super.afterProcess(webevent, templet, userObject, billvo);
//		loader.loadEditRelationItem(webevent);
		this.processScale(billcard);
		return billcard;
	}

	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body,
				CtPriceHeaderVO.CORIGCURRENCYID, PositionType.Head);
		operator.processPrecision();
	}

}

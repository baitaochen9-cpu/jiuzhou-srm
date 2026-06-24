package nccloud.pubimpl.ct.price.event.after.handler;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBodyAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.pubimpl.ct.price.event.after.rule.CardBodyPuorgAfterRule;

public class CardBodyAfterEventHandler extends AbstractBodyAfterHandler{

	@Override
	protected IBodyAfterRule<AggCtPriceVO> getAfterRule(String changeKey) {
		if(CtPriceBodyVO.PK_PUORG.equals(changeKey)){
			return new CardBodyPuorgAfterRule();
		}
		return null;
	}

}

package nccloud.pubimpl.ct.price.event.before.handler;

import nc.vo.ct.price.entity.CtPriceBodyVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.ct.price.event.before.rule.CardBodyOrgregionBeforeRule;

public class CardBeforeEditBeforeHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		if(CtPriceBodyVO.ORGREGION.equals(key)){
			return new CardBodyOrgregionBeforeRule();
		}
		return null;
	}

}

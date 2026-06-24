package nccloud.pubimpl.ct.price.event.after.handler;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractHeadAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.pubimpl.ct.price.event.after.rule.CardHeadCorigcurrencyidAfterRule;
import nccloud.pubimpl.ct.price.event.after.rule.CardHeadCvendoridAfterEventRule;
import nccloud.pubimpl.ct.price.event.after.rule.CardHeadMarbasclassAfterRule;
import nccloud.pubimpl.ct.price.event.after.rule.CardHeadMaterialAfterRule;

public class CardHeadAfterEventHandler extends AbstractHeadAfterHandler  {

	@Override
	protected IHeadAfterRule<AggCtPriceVO> getAfterRule(String changeKey) {
		if(CtPriceHeaderVO.CVENDORID.equals(changeKey)){
			return new CardHeadCvendoridAfterEventRule();
		}
		if(CtPriceHeaderVO.PK_MATERIAL.equals(changeKey)){
			return new CardHeadMaterialAfterRule();
		}
		if(CtPriceHeaderVO.PK_MARBASCLASS.equals(changeKey)){
			return new CardHeadMarbasclassAfterRule();
		}
		if(CtPriceHeaderVO.CORIGCURRENCYID.equals(changeKey)){
			return new CardHeadCorigcurrencyidAfterRule();
		}
		return null;
	}

}

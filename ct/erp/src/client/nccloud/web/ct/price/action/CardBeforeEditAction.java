package nccloud.web.ct.price.action;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;
/**
 * ±íÌå±à¼­Ç°
 * @author zhaoypm
 *
 */
public class CardBeforeEditAction extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.price.event.before.handler.CardBeforeEditBeforeHandler";
	}

}

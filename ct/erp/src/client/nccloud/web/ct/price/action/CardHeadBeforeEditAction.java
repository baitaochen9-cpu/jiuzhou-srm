package nccloud.web.ct.price.action;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;
/**
 * 
 * @description 价格信息表表头编辑前 
 * @author zhaoypm
 * @time 2019年6月20日 上午9:54:51
 * @since ncc1.0
 */
public class CardHeadBeforeEditAction extends AbstractBeforeAction{

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.price.event.before.handler.CardHeadBeforeEditHandler";
	}

}

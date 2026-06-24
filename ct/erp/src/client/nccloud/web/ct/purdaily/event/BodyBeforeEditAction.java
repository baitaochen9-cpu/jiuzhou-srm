package nccloud.web.ct.purdaily.event;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;

/** 
 * @description 表体编辑前Action
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:16:09 
 * @version ncc1.0 
 **/
public class BodyBeforeEditAction extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.purdaily.event.BodyBeforeEventHandler";
	}

}

package nccloud.web.ct.purdaily.event;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;

/** 
 * @description 表头编辑前事件
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:09:17 
 * @version ncc1.0 
 **/
public class HeadBeforeEditAction extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.purdaily.event.HeadBeforeEventHandler";
	}

}

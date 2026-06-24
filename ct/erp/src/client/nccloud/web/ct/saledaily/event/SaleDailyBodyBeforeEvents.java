package nccloud.web.ct.saledaily.event;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;

/**
 * @description 销售合同表体编辑后
 * @author wangshrc
 * @date 2019年2月19日 上午9:19:01
 * @version ncc1.0
 */
public class SaleDailyBodyBeforeEvents extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyBodyBeforeEventHandler";
	}

}

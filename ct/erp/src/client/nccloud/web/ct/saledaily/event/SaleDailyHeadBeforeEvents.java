package nccloud.web.ct.saledaily.event;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;

/**
 * @description 销售合同表头编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午9:13:15
 * @version ncc1.0
 */
public class SaleDailyHeadBeforeEvents extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyHeadBeforeEventHandler";
	}

}

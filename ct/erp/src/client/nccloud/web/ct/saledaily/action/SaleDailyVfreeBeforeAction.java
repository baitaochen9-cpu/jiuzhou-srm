package nccloud.web.ct.saledaily.action;

import nccloud.web.scmpub.pub.event.AbstractBeforeAction;

/**
 * @description 销售合同自由辅助属性编辑前
 * @author wangshrc
 * @date 2019年3月11日 上午9:28:28
 * @version ncc1.0
 */
public class SaleDailyVfreeBeforeAction extends AbstractBeforeAction {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.saledaily.operator.SaleDailyVfreeEditHandler";
	}

}

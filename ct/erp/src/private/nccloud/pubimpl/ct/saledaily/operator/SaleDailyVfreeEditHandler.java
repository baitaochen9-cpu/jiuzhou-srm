package nccloud.pubimpl.ct.saledaily.operator;

import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.body.SaleDailyVfreeBeforeRule;

/**
 * @description 销售合同自由辅助属性编辑前
 * @author wangshrc
 * @date 2019年3月11日 上午9:30:10
 * @version ncc1.0
 */
public class SaleDailyVfreeEditHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		IBeforeRule rule = null;
		if (key.startsWith("vfree")) {
			rule = new SaleDailyVfreeBeforeRule();
		}
		return rule;
	}

}

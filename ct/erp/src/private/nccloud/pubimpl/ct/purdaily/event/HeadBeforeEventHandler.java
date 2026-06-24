package nccloud.pubimpl.ct.purdaily.event;

import nc.vo.ct.entity.CtAbstractVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.head.CTNoriprepaylimitBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.head.GlobalExChangeRateBeforeRule;
import nccloud.pubimpl.ct.purdaily.event.before.head.GroupExChangeRateBeforeRule;

/**
 * @description 表头编辑前
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:20:13
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.before.PuHeadTailBeforeEventHandler
 **/
public class HeadBeforeEventHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		IBeforeRule rule = null;

		// 集团本位币汇率
		if (CtAbstractVO.NGROUPEXCHGRATE.equals(key)) {
			rule = new GroupExChangeRateBeforeRule();
		}
		// 全局本位币汇率
		else if (CtAbstractVO.NGROUPEXCHGRATE.equals(key)) {
			rule = new GlobalExChangeRateBeforeRule();
		}
		// 预付款限额
		else if (CtAbstractVO.NORIPREPAYLIMITMNY.equals(key)) {
			rule = new CTNoriprepaylimitBeforeRule();
		}

		return rule;
	}

}

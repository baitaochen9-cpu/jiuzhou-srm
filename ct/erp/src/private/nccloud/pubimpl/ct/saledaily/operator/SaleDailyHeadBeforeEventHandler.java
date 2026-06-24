package nccloud.pubimpl.ct.saledaily.operator;

import nc.vo.ct.entity.CtAbstractVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyGlobalExChangeBeforeRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyGroupExChangeBeforeRule;
import nccloud.pubimpl.scmpub.event.beforerule.VBillcodeBeforeEditRule;

/**
 * @description 销售合同表头编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午9:30:11
 * @version ncc1.0
 */
public class SaleDailyHeadBeforeEventHandler extends AbstractBeforeHandler {

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		IBeforeRule rule = null;
		if (CtAbstractVO.NGROUPEXCHGRATE.equals(key)
				 ) {
			// 集团本位币汇率编辑前
			rule = new SaleDailyGroupExChangeBeforeRule();
		}
		if(CtAbstractVO.NGLOBALEXCHGRATE.equals(key)) {
			// 全局本位币汇率编辑前
			rule = new SaleDailyGlobalExChangeBeforeRule();
		}
		if (CtAbstractVO.VBILLCODE.equals(key)) {
			return new VBillcodeBeforeEditRule();
		}
		return rule;
	}

}

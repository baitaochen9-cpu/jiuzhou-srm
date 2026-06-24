package nccloud.pubimpl.ct.price.event.before.handler;

import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractBeforeHandler;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.scmpub.event.beforerule.VBillcodeBeforeEditRule;
/**
 * 
 * @description 价格信息表表头编辑前 
 * @author zhaoypm
 * @time 2019年6月20日 上午9:55:08
 * @since ncc1.0
 */
public class CardHeadBeforeEditHandler extends AbstractBeforeHandler{

	@Override
	protected IBeforeRule getBeforeRule(String key) {
		if (key.equals(CtPriceHeaderVO.VCODE)) {
			return new VBillcodeBeforeEditRule();
		} 
		return null;
	}

}

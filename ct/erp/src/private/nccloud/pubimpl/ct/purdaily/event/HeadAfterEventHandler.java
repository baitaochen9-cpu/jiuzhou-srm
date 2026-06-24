package nccloud.pubimpl.ct.purdaily.event;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractHeadAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.ChangerateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.CtTypeAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.InvalliDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.OrgChangeAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.PayTermAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.PersonAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.PuCorigcurrencyidAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.SubscribeDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.ValDateAfterRule;
import nccloud.pubimpl.ct.purdaily.event.after.head.VendorAfterRule;

/**
 * @description 表头编辑后
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:20:02
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.after.PuHeadTailAfterEventHandler
 **/
public class HeadAfterEventHandler extends AbstractHeadAfterHandler {

	@Override
	protected IHeadAfterRule<AggCtPuVO> getAfterRule(String key) {
		IHeadAfterRule<AggCtPuVO> rule = null;

		// 主组织
		if (CtPuVO.PK_ORG_V.equals(key)) {
			rule = new OrgChangeAfterRule();
		}
		// 供应商
		else if (CtPuVO.CVENDORID.equals(key)) {
			rule = new VendorAfterRule();
		}
		// 合同签订日期
		else if (CtAbstractVO.SUBSCRIBEDATE.equals(key)) {
			rule = new SubscribeDateAfterRule();
		}
		// 计划生效日期
		else if (CtAbstractVO.VALDATE.equals(key)) {
			rule = new ValDateAfterRule();
		}
		// 计划终止日期
		else if (CtAbstractVO.INVALLIDATE.equals(key)) {
			rule = new InvalliDateAfterRule();
		}
		// 交易类型
		else if (CtAbstractVO.CTRANTYPEID.equals(key)) {
			rule = new CtTypeAfterRule();
		}
		// 折本汇率|全局本位币汇率|集团本位币汇率
		else if (CtAbstractVO.NEXCHANGERATE.equals(key) || CtAbstractVO.NGLOBALEXCHGRATE.equals(key)
				|| CtAbstractVO.NGROUPEXCHGRATE.equals(key)) {
			rule = new ChangerateAfterRule();
		}
		// 币种
		else if (CtAbstractVO.CORIGCURRENCYID.equals(key)) {
			rule = new PuCorigcurrencyidAfterRule();
		}
		// 付款协议
		else if (CtAbstractVO.PK_PAYTERM.equals(key)) {
			rule = new PayTermAfterRule();
		}
		
		// 人员
		else if (CtAbstractVO.PERSONNELID.equals(key)) {
			rule = new PersonAfterRule();
		}

		return rule;
	}

}

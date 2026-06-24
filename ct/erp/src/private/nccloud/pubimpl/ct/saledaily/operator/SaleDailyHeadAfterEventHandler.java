package nccloud.pubimpl.ct.saledaily.operator;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nccloud.dto.scmpub.pub.event.rule.AbstractHeadAfterHandler;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyChangeRateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyCorigcurrencyAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyCustomerAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyInvalliDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyOrgAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyPayTermAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyPersonAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailySubscribeDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyValDateAfterRule;
import nccloud.pubimpl.ct.saledaily.event.head.SaleDailyVtrantypeAfterRule;

/**
 * @description 销售合同表头编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午1:33:26
 * @version ncc1.0
 */
public class SaleDailyHeadAfterEventHandler extends AbstractHeadAfterHandler {

	@Override
	protected IHeadAfterRule<AggCtSaleVO> getAfterRule(String changeKey) {
		IHeadAfterRule<AggCtSaleVO> rule = null;
		if (CtSaleVO.PK_ORG_V.equals(changeKey)) {
			rule = new SaleDailyOrgAfterRule();
		} else if (CtSaleVO.PK_CUSTOMER.equals(changeKey)) {
			// 销售合同表头客户编辑后
			rule = new SaleDailyCustomerAfterRule();
		} else if (CtAbstractVO.PERSONNELID.equals(changeKey)) {
			// 销售合同人员编辑后
			rule = new SaleDailyPersonAfterRule();
		} else if (CtAbstractVO.SUBSCRIBEDATE.equals(changeKey)) {
			// 销售合同签订日期编辑后
			rule = new SaleDailySubscribeDateAfterRule();
		} else if (CtAbstractVO.VALDATE.equals(changeKey)) {
			// 销售合同生效日期编辑后
			rule = new SaleDailyValDateAfterRule();
		} else if (CtAbstractVO.INVALLIDATE.equals(changeKey)) {
			// 销售合同计划终止日期编辑后
			rule = new SaleDailyInvalliDateAfterRule();
		} else if (CtAbstractVO.CTRANTYPEID.equals(changeKey)) {
			// 销售合同交易类型编辑后
			rule = new SaleDailyVtrantypeAfterRule();
		} else if (CtAbstractVO.NEXCHANGERATE.equals(changeKey)
				|| CtAbstractVO.NGLOBALEXCHGRATE.equals(changeKey)
				|| CtAbstractVO.NGROUPEXCHGRATE.equals(changeKey)) {
			// 销售合同表头汇率编辑后
			rule = new SaleDailyChangeRateAfterRule();
		} else if (CtAbstractVO.CORIGCURRENCYID.equals(changeKey)) {
			// 销售合同币种编辑后
			rule = new SaleDailyCorigcurrencyAfterRule();
		} else if (CtAbstractVO.PK_PAYTERM.equals(changeKey)) {
			// 销售合同付款协议编辑后
			rule = new SaleDailyPayTermAfterRule();
		}
		return rule;
	}

}

package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.util.CTVatUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同财务组织编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午9:50:20
 * @version ncc1.0
 */
public class SaleDailyFinanceorgAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		String forg = (String) event.getChangrows()[0].getNewvalue();
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		int row = event.getRow();
		if (null != forg) {
			String ctaxcountryid = CTVatUtil.getTaxCountry(bvos[row]
					.getPk_financeorg());
			if (StringUtils.isBlank(ctaxcountryid)) {
				return billvo;
			}
			if (bvos[row].getCtaxcountryid() != null
					&& bvos[row].getCtaxcountryid().equals(ctaxcountryid)) {
				return billvo;
			}
			bvos[row].setCtaxcountryid(ctaxcountryid);
			new SaleDailyCountryAfterRule()
					.afterEdit(billvo, event, userobject);
		}
		return billvo;
	}

}

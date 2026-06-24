package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.ct.util.CTVatUtil;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 财务组织编辑后
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:37:15
 * @version ncc1.0
 **/
public class CTPuFinanceorgAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();
		String country = (String) event.getChangrows()[0].getNewvalue();
		if (StringUtil.isEmptyTrimSpace(country)) {
			return billvo;
		}

		String taxcountry = CTVatUtil.getTaxCountry(util.getBodyStringValue(row, CtAbstractBVO.PK_FINANCEORG));
		String oldtaxcountry = util.getBodyStringValue(row, CTVatNameConst.CTAXCOUNTRYID);

		if (null != taxcountry && !taxcountry.equals(oldtaxcountry)) {
			util.setBodyValue(row, CTVatNameConst.CTAXCOUNTRYID, taxcountry);
			new CTPuCountryAfterRule(CTVatNameConst.CTAXCOUNTRYID).afterEdit(billvo, event, userobject);
		}

		return billvo;
	}

}

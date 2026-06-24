package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.itf.scmpub.reference.uap.org.StockOrgPubService;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description
 * @author xiahui
 * @date 创建时间：2019-1-22 下午1:37:57
 * @version ncc1.0
 **/
public class CTPuArrvstockAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		String pk_arrvstock = util.getBodyStringValue(row, CtPuBVO.PK_ARRVSTOCK);
		if (null == pk_arrvstock) {
			return billvo;
		}
		Map<String, String> stockcountrys = StockOrgPubService.queryCountryByStockOrg(new String[] { pk_arrvstock });
		util.setBodyValue(row, CTVatNameConst.CRECECOUNTRYID, stockcountrys.get(pk_arrvstock));

		return billvo;
	}

}

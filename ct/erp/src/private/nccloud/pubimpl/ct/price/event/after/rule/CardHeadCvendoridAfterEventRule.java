package nccloud.pubimpl.ct.price.event.after.rule;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.supplier.SupplierPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
/**
 * 
 * @description 表头上供应商编辑后 
 * @author zhaoypm
 * @time 2019-4-2 下午3:50:53
 * @since ncc1.0
 */
public class CardHeadCvendoridAfterEventRule implements
		IHeadAfterRule<AggCtPriceVO> {

	@Override
	public AggCtPriceVO afterEdit(AggCtPriceVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		CtPriceHeaderVO head = billvo.getParentVO();
		String pk_org = head.getPk_org();
		String pk_supplier = head.getCvendorid();
		String currtype = SupplierPubService.getDefaultCurrtype(pk_supplier,
				pk_org);
		if (null == currtype) {
			currtype = OrgUnitPubService.getOrgCurr(pk_org);
		}
		head.setCorigcurrencyid(currtype);
		return billvo;
	}
}

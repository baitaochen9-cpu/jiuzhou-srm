package nccloud.pubimpl.ct.price.event.after.rule;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;
import nccloud.dto.scmpub.pub.utils.SCMMultSelectedUtil;
import nccloud.pubimpl.ct.price.event.after.util.RegionUtil;

/**
 * 
 * @description 表体多选适用采购组织后增行
 * @author zhaoypm
 * @time 2019-4-9 下午3:23:12
 * @since ncc1.0
 */
public class CardBodyPuorgAfterRule implements IBodyAfterRule<AggCtPriceVO> {

	@Override
	public AggCtPriceVO afterEdit(AggCtPriceVO billvo, BillCardBodyEditEvent event, Map userobject) {
		int[] rows = new SCMMultSelectedUtil().handleMultSelected(billvo, event, userobject);
		new RegionUtil().setRegionInfo(billvo);
		return billvo;
	}

}

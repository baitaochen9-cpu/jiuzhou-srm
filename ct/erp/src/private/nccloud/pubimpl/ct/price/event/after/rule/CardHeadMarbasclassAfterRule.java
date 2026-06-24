package nccloud.pubimpl.ct.price.event.after.rule;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.pubimpl.ct.price.event.after.util.RegionUtil;
/**
 * 
 * @description 物料基本分类编辑后
 * <ol>
 * <li>清除物料编码，名称，规格，型号（js完成）</li>
 * <li>清除报价单位，设置为不可编辑（js完成）</li>
 * <li>设置区域信息</li>
 * </ol> 
 * @author zhaoypm
 * @time 2019-4-9 上午9:12:56
 * @since ncc1.0
 */
public class CardHeadMarbasclassAfterRule implements
		IHeadAfterRule<AggCtPriceVO> {

	@Override
	public AggCtPriceVO afterEdit(AggCtPriceVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		// 设置区域信息
		new RegionUtil().setRegionInfo(billvo);
		return billvo;
	}

}

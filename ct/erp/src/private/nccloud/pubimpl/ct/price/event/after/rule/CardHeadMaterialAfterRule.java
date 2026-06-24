package nccloud.pubimpl.ct.price.event.after.rule;

import java.util.Map;
import java.util.Map.Entry;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.pubimpl.ct.price.event.after.util.RegionUtil;

/**
 * 
 * @description 物料编辑后规则
 *              <ol>
 *              <li>设置业务单位参照按照物料主键过滤</li>
 *              <li></li>
 *              </ol>
 * @author zhaoypm
 * @time 2019-4-8 下午2:06:00
 * @since ncc1.0
 */
public class CardHeadMaterialAfterRule implements IHeadAfterRule<AggCtPriceVO> {

	@Override
	public AggCtPriceVO afterEdit(AggCtPriceVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		// 设置参照过滤和设置业务单位默认值
		initRefFilter(billvo, event);
		// 清除物料分类
		clearMatClass(billvo);
		// 设置区域信息
		new RegionUtil().setRegionInfo(billvo);
		return billvo;
	}

	/**
	 * 清空物料分了信息，需要到前台更新数据 <b>并且同时要在前台使业务单位字段可用</b>
	 * 
	 * @param billvo
	 */
	private void clearMatClass(AggCtPriceVO billvo) {
		CtPriceHeaderVO head = billvo.getParentVO();
		head.setPk_marbasclass(null);
	}

	/**
	 * 给默认采购单位（业务单位）设置默认值，需要到前台更新数据 <b>并且同时还要在前台设置业务单位参照按照物料主键过滤</b>
	 * 
	 * @param billvo
	 * @param event
	 */
	private void initRefFilter(AggCtPriceVO billvo, BillCardHeadEditEvent event) {
		CtPriceHeaderVO head = billvo.getParentVO();
		String pk_material = head.getPk_material();
		Map<String, String> measdocMap = MaterialPubService
				.queryPuMeasdocIDByPks(new String[] { pk_material });
		for (Entry<String, String> entry : measdocMap.entrySet()) {
			// 物料档案的默认采购单位(主单位或业务单位)
			head.setCastunitid(entry.getValue());
		}

	}

}

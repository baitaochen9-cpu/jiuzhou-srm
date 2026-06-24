package nccloud.pubimpl.ct.price.event.after.rule;

import java.util.Map;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pubapp.scale.BillVOScaleProcessor;
import nc.vo.pubapp.scale.PosEnum;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * 
 * @description 价格模板表头币种编辑后
 *              <ol>
 *              <li>设置合同价格信息的精度</li>
 *              <li>清空价格项的值</li>
 *              <ol>
 * @author zhaoypm
 * @time 2019-4-9 上午10:52:46
 * @since ncc1.0
 */
public class CardHeadCorigcurrencyidAfterRule implements IHeadAfterRule<AggCtPriceVO> {
	// 价格
	private static String[] pricekeys = { CtPriceBodyVO.NPRICEITEM1, CtPriceBodyVO.NPRICEITEM2,
			CtPriceBodyVO.NPRICEITEM3, CtPriceBodyVO.NPRICEITEM4, CtPriceBodyVO.NPRICEITEM5, CtPriceBodyVO.NPRICEITEM6,
			CtPriceBodyVO.NPRICEITEM7, CtPriceBodyVO.NPRICEITEM8, CtPriceBodyVO.NPRICEITEM9,
			CtPriceBodyVO.NPRICEITEM10, CtPriceBodyVO.NPRICEITEM11, CtPriceBodyVO.NPRICEITEM12,
			CtPriceBodyVO.NPRICEITEM13, CtPriceBodyVO.NPRICEITEM14, CtPriceBodyVO.NPRICEITEM15,
			CtPriceBodyVO.NPRICEITEM16, CtPriceBodyVO.NPRICEITEM17, CtPriceBodyVO.NPRICEITEM18,
			CtPriceBodyVO.NPRICEITEM19, CtPriceBodyVO.NPRICEITEM20, CtPriceBodyVO.NPRICEITEM21,
			CtPriceBodyVO.NPRICEITEM22, CtPriceBodyVO.NPRICEITEM23, CtPriceBodyVO.NPRICEITEM24,
			CtPriceBodyVO.NPRICEITEM25, CtPriceBodyVO.NPRICEITEM26, CtPriceBodyVO.NPRICEITEM27,
			CtPriceBodyVO.NPRICEITEM28, CtPriceBodyVO.NPRICEITEM29, CtPriceBodyVO.NPRICEITEM30,
			CtPriceBodyVO.BASEPRICE, CtPriceBodyVO.TOTALPRICE };

	@Override
	public AggCtPriceVO afterEdit(AggCtPriceVO billvo, BillCardHeadEditEvent event, Map userobject) {
		CtPriceHeaderVO head = billvo.getParentVO();
		CtPriceBodyVO[] body = billvo.getChildrenVO();
		String corigcurrencyid = head.getCorigcurrencyid();
		if (!isEmpty(corigcurrencyid)) {
			String pk_group = head.getPk_group();
			BillVOScaleProcessor scale = new BillVOScaleProcessor(pk_group, new AggCtPriceVO[] { billvo });
			// 单价精度
			scale.setPriceCtlInfo(CardHeadCorigcurrencyidAfterRule.pricekeys, PosEnum.body, null,
					CtPriceHeaderVO.CORIGCURRENCYID, PosEnum.head, null);
			// 进行计算
			scale.process();
			clearPriceItemsValue(body);
		}
		return billvo;
	}

	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	/**
	 * 清空价格项的值
	 * 
	 * @param body
	 */
	private void clearPriceItemsValue(CtPriceBodyVO[] body) {
		for (CtPriceBodyVO vo : body) {
			for (String priceItem : pricekeys) {
				vo.setAttributeValue(priceItem, null);
			}
		}
	}
}

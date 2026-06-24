package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 采购合同“辅单位”编辑后事件
 * @author xiahui
 * @date 创建时间：2019-1-22 上午11:38:10
 * @version ncc1.0
 **/
public class PuCastUnitIdAfterRule implements IBodyAfterRule<AggCtPuVO> {
	private static final String PK_CT_PRICE_NAME = "pk_ct_price.vname";

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		// 清空合同价格信息表主键
		util.setBodyValue(row, CtPuBVO.PK_CT_PRICE, null);
		// 清空合同价格信息表名称
		util.setBodyValue(row, PuCastUnitIdAfterRule.PK_CT_PRICE_NAME, null);

		return billvo;
	}

}

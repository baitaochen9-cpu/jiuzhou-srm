package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.pub.CTVatNameConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 单价金额联动
 * @author xiahui
 * @date 创建时间：2019-2-15 下午4:29:48
 * @version ncc1.0
 **/
public class RelationCalculateAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		RelationCalculate calcul = new RelationCalculate();
		calcul.calculate(util, new int[] { event.getRow() }, event.getChangeKey());
		
		return billvo;
	}

}

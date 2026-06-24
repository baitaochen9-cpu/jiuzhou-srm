package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 联动计算
 * @author wangshrc
 * @date 2019年3月3日 下午8:59:50
 * @version ncc1.0
 */
public class SaleDailyRelationCalculateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		RelationCalculate calcul = new RelationCalculate();
		calcul.calculate(util, new int[] { event.getRow() },
				event.getChangeKey());

		return billvo;
	}

}

package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 销售合同质保金编辑后
 * @author wangshrc
 * @date 2019年2月15日 上午10:39:56
 * @version ncc1.0
 */
public class SaleDailyDepositAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		int count = bvos.length;
		// 20、 质保金：质量保证金额，只有一行可以设置该属性，允许所有行都为非质保金行
		for (int i = 0; i < count; i++) {
			if (i != row && bvos[i].getIsdeposit() != null) {
				bvos[i].setIsdeposit(null);
			}
		}
		return billvo;
	}

}

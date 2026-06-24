package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 销售合同物料基本分类编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午2:05:40
 * @version ncc1.0
 */
public class SaleDailyMarBaseClassAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		final String NCHANGERAtE = "1.0000/1.0000";
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		for (int i = 0; i < bvos.length; i++) {
			bvos[i].setVchangerate(NCHANGERAtE);
			bvos[i].setVqtunitrate(NCHANGERAtE);
		}
		return billvo;
	}

}

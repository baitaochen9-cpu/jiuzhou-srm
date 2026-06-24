package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDouble;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 收款比例
 * @author wangshrc
 * @date 2019年2月15日 上午10:44:42
 * @version ncc1.0
 */
public class SaleDailyAccrateAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		UFDouble rate = bvos[row].getAccrate();
		if (rate == null || UFDouble.ZERO_DBL.compareTo(rate) >= 0
				|| rate.toDouble() > 100) {
			bvos[row].setAccrate(null);
			userobject.put("errMsg", NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0392")/*
															 * 收款比例必须大于0小于等于100
															 */);
		}
		return billvo;
	}

}

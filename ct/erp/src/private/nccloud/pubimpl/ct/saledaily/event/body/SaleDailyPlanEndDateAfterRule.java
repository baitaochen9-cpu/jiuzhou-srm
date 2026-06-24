package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 计划到期日
 * @author wangshrc
 * @date 2019年2月15日 上午11:00:16
 * @version ncc1.0
 */
public class SaleDailyPlanEndDateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		UFDate enddate = bvos[row].getDplanenddate();
		UFDate effectdate = bvos[row].getDplaneffectdate();

		if (effectdate != null && enddate != null && enddate.before(effectdate)) {
			bvos[row].setDplanenddate(null);
			userobject.put("errMsg", NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0393")/*
															 * 计划到期日必须大于计划起效日
															 */
			);
		}
		return billvo;
	}
}

package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 实际生效日
 * @author wangshrc
 * @date 2019年2月15日 上午11:03:17
 * @version ncc1.0
 */
public class SaleDailyRealEffectDateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		UFDate enddate = bvos[row].getDrealenddate();
		UFDate effectdate = bvos[row].getDrealeffectdate();

		if (effectdate == null) {
			bvos[row].setDrealenddate(null);
		} else if (effectdate != null) {
			SalePayTermUtil.setRealEndDate(billvo, row);
		}
		if (enddate != null && effectdate != null && effectdate.after(enddate)) {
			userobject.put("errMsg", NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0393")/*
															 * 实际到期日必须大于等于实际起效日
															 */
			);
		}
		return billvo;
	}

}

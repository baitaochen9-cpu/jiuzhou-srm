package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSalePayTermVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.saledaily.utils.SalePayTermUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 计划生效日期
 * @author wangshrc
 * @date 2019年2月15日 上午10:50:24
 * @version ncc1.0
 */
public class SaleDailyPlanEffectDateAfterRule implements
		IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings("rawtypes")
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		// 输入起效日期延期天数，账期天数，固定结账日，生效月，附加月，计划起效日期时，根据计划起效日期与账期系统自动计算计划到期日，计划到期日可手工调整并大于等于计划起效日
		int row = event.getRow();
		CtSalePayTermVO[] bvos = billvo.getCtSalePayTermVO();
		UFDate enddate = bvos[row].getDplanenddate();
		UFDate effectdate = bvos[row].getDplaneffectdate();

		if (effectdate != null && enddate == null) {
			SalePayTermUtil.setPlanEndDate(billvo, event.getRow());
		} else if (effectdate == null) {
			bvos[row].setDplanenddate(null);
		} else if (enddate != null && effectdate != null
				&& effectdate.after(enddate)) {
			ExceptionUtils.wrappBusinessException(NCLangRes4VoTransl
					.getNCLangRes().getStrByID("4020003_0", "04020003-0393")/*
																			 * 计划到期日必须大于等于计划起效日
																			 */
			);
		}
		return billvo;
	}

}

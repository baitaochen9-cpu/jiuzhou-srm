package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同计划终止日期编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午5:04:28
 * @version ncc1.0
 */
public class SaleDailyInvalliDateAfterRule implements
		IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		// 终止日期
		UFDate invalDate = billvo.getParentVO().getInvallidate();
		// 生效日期
		UFDate valDate = billvo.getParentVO().getValdate();
		if (ValueUtil.isEmpty(valDate) || ValueUtil.isEmpty(invalDate)) {
			return billvo;
		} else if (invalDate.before(valDate)
				|| ValueUtil.equals(invalDate, valDate)) {
			billvo.getParentVO().setInvallidate(null);
			userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("4020003_0", "04020003-0048")/*
															 * @res
															 * "计划终止日期应大于计划生效日期！"
															 */);
		}
		return billvo;
	}

}

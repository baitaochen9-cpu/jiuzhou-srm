package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;

/**
 * @description 销售合同生效日期编辑后
 * @author wangshrc
 * @date 2019年2月13日 下午4:49:53
 * @version ncc1.0
 */
public class SaleDailyValDateAfterRule implements IHeadAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardHeadEditEvent event, Map userobject) {
		CtSaleVO hvo = billvo.getParentVO();
		// 计划生效日期
		UFDate valDate = hvo.getValdate();
		if (!ValueUtil.isEmpty(valDate)) {
			// 签订日期
			UFDate subDate = hvo.getSubscribedate();
			// 终止日期
			UFDate invalDate = hvo.getInvallidate();
			if (!ValueUtil.isEmpty(subDate)) {
				if (valDate.before(subDate)
						&& !valDate.isSameDate(subDate,
								Calendars.getGMTDefault())) {
					hvo.setValdate(null);
					userobject
							.put("errMsg",
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("4020003_0",
													"04020003-0050")/*
																	 * @res
																	 * "计划生效日期不能在合同签订日期之前！"
																	 */);
					return billvo;
				}
			}
			if (!ValueUtil.isEmpty(invalDate)) {
				if (valDate.after(invalDate)
						|| ValueUtil.equals(valDate, invalDate)) {
					hvo.setValdate(null);
					userobject
							.put("errMsg",
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("4020003_0",
													"04020003-0051")/*
																	 * @res
																	 * "计划生效日期应小于计划终止日期！"
																	 */);
				}
			}
		}
		return billvo;
	}

}

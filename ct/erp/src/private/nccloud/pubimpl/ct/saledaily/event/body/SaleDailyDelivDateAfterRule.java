package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.Map;

import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 计划收发货日期编辑后
 * @author wangshrc
 * @date 2019年2月14日 下午1:27:27
 * @version ncc1.0
 */
public class SaleDailyDelivDateAfterRule implements IBodyAfterRule<AggCtSaleVO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public AggCtSaleVO afterEdit(AggCtSaleVO billvo,
			BillCardBodyEditEvent event, Map userobject) {
		if (event.getChangrows().length <= 0)
			return billvo;
		CtSaleBVO[] bvos = billvo.getCtSaleBVO();
		Object oDelivdate = event.getChangrows()[0].getNewvalue();
		if (!ValueUtil.isEmpty(oDelivdate)) {
			Object oSubDate = billvo.getParentVO().getSubscribedate();
			Object oInvlliDate = billvo.getParentVO().getInvallidate();
			// 如果合同签订日期为空
			if (ValueUtil.isEmpty(oSubDate)) {
				bvos[event.getRow()].setDelivdate(null);
				userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4020003_0", "04020003-0034")/*
																 * @res
																 * "合同签订日期不能为空。"
																 */);
				return billvo;
			} else if (ValueUtil.isEmpty(oInvlliDate)) {
				bvos[event.getRow()].setDelivdate(null);
				userobject.put("errMsg", nc.vo.ml.NCLangRes4VoTransl
						.getNCLangRes()
						.getStrByID("4020003_0", "04020003-0035")/*
																 * @res
																 * "合同终止日期不能为空。"
																 */);
				return billvo;
			} else {
				UFDate dDelivdate = new UFDate(oDelivdate.toString());
				UFDate dSubDate = new UFDate(oSubDate.toString());
				UFDate dInvlliDate = new UFDate(oInvlliDate.toString());
				if (dDelivdate.before(dSubDate)
						|| dDelivdate.after(dInvlliDate)) {
					bvos[event.getRow()].setDelivdate(null);
					userobject
							.put("errMsg",
									nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
											.getStrByID("4020003_0",
													"04020003-0036")/*
																	 * @res
																	 * "计划收发货日期应大于合同签订日期并且小于合同计划终止日期。"
																	 */);
				}
			}
		}
		return billvo;
	}

}

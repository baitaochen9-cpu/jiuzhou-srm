package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.Calendars;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 计划生效日期 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午9:50:08
 * @version ncc1.0
 **/
public class ValDateAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		UFDate valDate = (UFDate) util.getHeadValue(CtAbstractVO.VALDATE); // 计划生效日期
		if (!ValueUtil.isEmpty(valDate)) {
			UFDate subDate = (UFDate) util.getHeadValue(CtAbstractVO.SUBSCRIBEDATE); // 签订日期
			UFDate invalDate = (UFDate) util.getHeadValue(CtAbstractVO.INVALLIDATE); // 终止日期

			if (!ValueUtil.isEmpty(subDate)) {
				if (valDate.before(subDate) && !valDate.isSameDate(subDate, Calendars.getGMTDefault())) {
					util.setHeadValue(CtAbstractVO.VALDATE, null);
					userobject.put(MsgFlag.ERROR,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0050")); // 计划生效日期不能在合同签订日期之前！
					return billvo;
				}
			}
			if (!ValueUtil.isEmpty(invalDate)) {
				if (valDate.after(invalDate) || ValueUtil.equals(valDate, invalDate)) {
					util.setHeadValue(CtAbstractVO.VALDATE, null);
					userobject.put(MsgFlag.ERROR,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0051")); // 计划生效日期应小于计划终止日期！
					return billvo;
				}
			}
		}

		return billvo;
	}

}

package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFDate;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 计划终止日期 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 下午2:25:25
 * @version ncc1.0
 **/
public class InvalliDateAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);

		UFDate invalDate = (UFDate) util.getHeadValue(CtAbstractVO.INVALLIDATE); // 终止日期
		UFDate valDate = (UFDate) util.getHeadValue(CtAbstractVO.VALDATE); // 生效日期
		if (ValueUtil.isEmpty(valDate) || ValueUtil.isEmpty(invalDate)) {
			return billvo;
		} else if (invalDate.before(valDate) || ValueUtil.equals(invalDate, valDate)) {
			util.setHeadValue(CtAbstractVO.INVALLIDATE, null);
			userobject
					.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0048")); // 计划终止日期应大于计划生效日期！
			return billvo;
		}
		return billvo;
	}

}

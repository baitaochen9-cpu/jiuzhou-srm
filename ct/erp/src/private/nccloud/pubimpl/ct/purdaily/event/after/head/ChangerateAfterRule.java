package nccloud.pubimpl.ct.purdaily.event.after.head;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFDouble;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.ct.pub.utils.RelationCalculate;
import nccloud.dto.scmpub.pub.context.BillCardHeadEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IHeadAfterRule;
import nccloud.dto.to.pub.constance.MsgFlag;

/**
 * @description 折本汇率 编辑后事件
 * @author xiahui
 * @date 创建时间：2019-2-14 上午10:24:56
 * @version ncc1.0
 **/
public class ChangerateAfterRule implements IHeadAfterRule<AggCtPuVO> {

	@SuppressWarnings("unchecked")
	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardHeadEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		String rateItem = event.getChangeKey();
		if (StringUtil.isEmptyWithTrim(rateItem)) {
			return billvo;
		}

		UFDouble oldRate = (UFDouble) event.getOldValue();
		UFDouble obj = (UFDouble) util.getHeadValue(rateItem);
		if (ValueUtil.isEmpty(obj) || UFDouble.ZERO_DBL.compareTo(obj) > 0 || ValueUtil.equals(obj, UFDouble.ZERO_DBL)) {
			if (!ValueUtil.isEmpty(oldRate)) {
				util.setHeadValue(rateItem, oldRate);
			}
			userobject
					.put(MsgFlag.ERROR, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0042")); // 汇率不能为空，也不能小于等于0！
			return billvo;
		}

		// 联动计算表体单价金额字段
		int[] rows = util.getRows(util.getBodyRowCount());
		new RelationCalculate().calculate(util, rows, rateItem);

		return billvo;
	}

}

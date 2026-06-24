package nccloud.pubimpl.ct.purdaily.event.after.body;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.ct.pub.utils.ExtBillUtil;
import nccloud.dto.scmpub.pub.context.BillCardBodyEditEvent;
import nccloud.dto.scmpub.pub.event.rule.IBodyAfterRule;

/**
 * @description 计划收发货日期编辑后事件类
 * @author xiahui
 * @date 创建时间：2019-1-21 下午1:55:12
 * @version ncc1.0
 * @ref nc.ui.ct.editor.after.body.DelivDate
 **/
public class DelivDateAfterRule implements IBodyAfterRule<AggCtPuVO> {

	@Override
	public AggCtPuVO afterEdit(AggCtPuVO billvo, BillCardBodyEditEvent event, @SuppressWarnings("rawtypes") Map userobject) {
		ExtBillUtil util = new ExtBillUtil(billvo);
		int row = event.getRow();

		UFDate delivDate = (UFDate) event.getChangrows()[0].getNewvalue();
		if (!ValueUtil.isEmpty(delivDate)) {
			UFDate subScribeDate = util.getHeadTailUFDateValue(CtAbstractVO.SUBSCRIBEDATE);
			UFDate invalliDate = util.getHeadTailUFDateValue(CtAbstractVO.INVALLIDATE);
			// 如果合同签订日期为空
			if (ValueUtil.isEmpty(subScribeDate)) {
				util.clearRowValueByItemKeys(row, new String[] { CtAbstractBVO.DELIVDATE });
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
						"04020003-0034")/* @res "合同签订日期不能为空。" */);
			}
			// 如果计划终止日期为空
			else if (ValueUtil.isEmpty(invalliDate)) {
				util.clearRowValueByItemKeys(row, new String[] { CtAbstractBVO.DELIVDATE });
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
						"04020003-0035")/* @res "计划终止日期不能为空。" */);
			}
			// 计划收发货日期小于合同签订日期或大于合同计划终止日期
			else if (delivDate.before(subScribeDate) || delivDate.after(invalliDate)) {
				util.clearRowValueByItemKeys(row, new String[] { CtAbstractBVO.DELIVDATE });
				ExceptionUtils.wrappBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
						"04020003-0036")/* @res "计划收发货日期应大于合同签订日期并且小于合同计划终止日期。" */);
			}

		}

		return billvo;
	}

}

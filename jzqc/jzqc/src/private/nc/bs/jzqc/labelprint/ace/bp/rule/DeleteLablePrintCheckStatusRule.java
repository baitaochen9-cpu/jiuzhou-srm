package nc.bs.jzqc.labelprint.ace.bp.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;

public class DeleteLablePrintCheckStatusRule implements IRule<AggLabelPrintHVO> {
	@Override
	public void process(AggLabelPrintHVO[] vos) {
		if (ArrayUtils.isEmpty(vos))
			return;
		for (AggLabelPrintHVO clientBill : vos) {
			if (clientBill.getParentVO() != null
					&& clientBill.getParentVO().getIprintcount() != null
					&& clientBill.getParentVO().getIprintcount().intValue() > 0) {
				ExceptionUtils
						.asBusinessRuntimeException(new BusinessException(
								"打印次数大于零的数据不能删除！"));
			}
		}
	}
}
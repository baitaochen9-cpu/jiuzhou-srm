package nc.bs.jzqc.labelprintapply.ace.bp.rule;

import nc.bs.jzqc.labelprint.ace.bp.rule.UpdateLablePrintStatusRule;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprint.LabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.jzqc.labelprintapply.LabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scmpub.util.VOEntityUtil;

public class UpdateLablePrintStatusFalseRule implements
		IRule<AggLabelprintapplyHVO> {
	@Override
	public void process(AggLabelprintapplyHVO[] vos) {

		if (ArrayUtils.isEmpty(vos))
			return;
		String[] pks = VOEntityUtil.getVOsNotRepeatValue(
				VOEntityUtil.getHeadVOs(vos), "srcbillid");
		SCMBillQuery<AggLabelPrintHVO> query = new SCMBillQuery<AggLabelPrintHVO>(
				AggLabelPrintHVO.class);

		AggLabelPrintHVO[] vos1;
		try {
			vos1 = query.queryVOByIDs(pks);
			checkPrintCount(vos, vos1);

			UpdateLablePrintStatusRule rule = new UpdateLablePrintStatusRule();
			rule.process(vos1, UFBoolean.FALSE);
		} catch (BusinessException ex) {
			ExceptionUtils.asBusinessRuntimeException(ex);
		}
	}

	private void checkPrintCount(AggLabelprintapplyHVO[] vos,
			AggLabelPrintHVO[] vos1) throws BusinessException {
		if (ArrayUtils.isEmpty(vos) || ArrayUtils.isEmpty(vos1))
			return;

		int len = vos.length;
		for (int i = 0; i < len; i++) {
			LabelprintapplyHVO hvo = vos[i].getParentVO();
			LabelPrintHVO hvo1 = vos1[i].getParentVO();
			if (hvo.getIprintcount().intValue() != hvo1.getIprintcount()
					.intValue()) {
				throw new BusinessException("打印次数发生变化，不能弃审！");
			}

		}
	}
}
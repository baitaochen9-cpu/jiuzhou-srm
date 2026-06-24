package nc.bs.jzqc.labelprintapply.ace.bp.rule;

import nc.bs.jzqc.labelprint.ace.bp.rule.UpdateLablePrintStatusRule;
import nc.bs.scmpub.query.SCMBillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.am.common.util.ArrayUtils;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scmpub.util.VOEntityUtil;

public class UpdateLablePrintStatusTrueRule implements
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
			UpdateLablePrintStatusRule rule = new UpdateLablePrintStatusRule();
			rule.process(vos1, UFBoolean.TRUE);
		} catch (BusinessException ex) {
			ExceptionUtils.asBusinessRuntimeException(ex);
		}

	}
}
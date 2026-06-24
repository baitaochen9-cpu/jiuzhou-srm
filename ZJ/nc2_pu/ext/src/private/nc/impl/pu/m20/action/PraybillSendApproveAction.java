package nc.impl.pu.m20.action;

import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.scmpub.pf.PfParameterUtil;
import nc.impl.pu.m20.action.approve.PushOaRule;
import nc.impl.pu.m20.rule.ApproveFlowCheckRule;
import nc.impl.pu.m20.rule.PraybillStateChgRule;
import nc.impl.pubapp.pattern.data.bill.BillUpdate;
import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.pub.rule.pf.SendApproveStatusChkRule;

public class PraybillSendApproveAction {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PraybillVO[] sendapprove(PraybillVO[] vos, AbstractCompiler2 script) {
		PfParameterUtil util = new PfParameterUtil((script == null)
				? null
				: script.getPfParameterVO(), vos);

		PraybillVO[] sendVos = (PraybillVO[]) util.getClientFullInfoBill();
		PraybillVO[] orgVos = (PraybillVO[]) util.getOrginBills();

		AroundProcesser processer = new AroundProcesser(null);

		addRule(processer);
		processer.before(sendVos);
		PraybillVO[] updatedVos = (PraybillVO[]) new BillUpdate().update(
				sendVos, orgVos);
		new PushOaRule().process(updatedVos);
		return updatedVos;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addRule(AroundProcesser<PraybillVO> processer) {
		processer.addBeforeFinalRule(new ApproveFlowCheckRule());
		processer.addBeforeFinalRule(new SendApproveStatusChkRule());
		processer.addBeforeFinalRule(new PraybillStateChgRule());
	}
}
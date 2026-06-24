package nc.impl.pp.m28.action.approve;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.pp.m28.entity.PriceAuditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class PushOaRule implements  IRule<PriceAuditVO> {
	public void process(PriceAuditVO[] vos) {

		ISysDispatcher sys = NCLocator.getInstance().lookup(
				ISysDispatcher.class);
		for (PriceAuditVO vo : vos) {
			try {
				sys.dispatch(vo, "OA_PriceAudit_APPROVE", null);
			} catch (BusinessException e) {
				 ExceptionUtils.wrappException(e);
			}
		}
	}

}

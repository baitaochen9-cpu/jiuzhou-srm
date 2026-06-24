package nc.impl.pu.m20.action.approve;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class PushOaRule implements  IRule<PraybillVO> {
	public void process(PraybillVO[] vos) {

		ISysDispatcher sys = NCLocator.getInstance().lookup(
				ISysDispatcher.class);
		for (PraybillVO vo : vos) {
			try {
				sys.dispatch(vo, "OA_Praybill_APPROVE", null);
			} catch (BusinessException e) {
				 ExceptionUtils.wrappException(e);
			}
		}
	}

}

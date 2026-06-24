package nc.impl.so.m30.action.main.approve;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.so.m30.entity.SaleOrderVO;

public class PushOaRule implements  IRule<SaleOrderVO> {
	public void process(SaleOrderVO[] vos) {

		ISysDispatcher sys = NCLocator.getInstance().lookup(
				ISysDispatcher.class);
		for (SaleOrderVO vo : vos) {
			try {
				sys.dispatch(vo, "OA_SaleOrder_APPROVE", null);
			} catch (BusinessException e) {
				 ExceptionUtils.wrappException(e);
			}
		}
	}

}

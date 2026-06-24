package nc.vo.ct.rule;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

public class PushOaRule implements  IRule<AggCtSaleVO> {
	public void process(AggCtSaleVO[] vos) {

		ISysDispatcher sys = NCLocator.getInstance().lookup(
				ISysDispatcher.class);
		for (AggCtSaleVO vo : vos) {
			try {
				sys.dispatch(vo, "OA_SaleCt", null);
			} catch (BusinessException e) {
				 ExceptionUtils.wrappException(e);
			}
		}
	}

}

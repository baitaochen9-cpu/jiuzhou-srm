package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.api.srm.ISysDispatcher;

public class PushSrmCancelRule implements IRule<PurchaseInVO> {
	public void process(PurchaseInVO[] vos) {
		
		for (PurchaseInVO vo : vos) {
			try {
				PushSrmRule srm = new PushSrmRule();
				boolean srm2 = srm.isSrm(vo);
				boolean isToSrm = srm.IsToSrm(vo.getHead().getPk_org());
				if (srm2 && isToSrm) {
					ISysDispatcher sys = NCLocator.getInstance().lookup(
							ISysDispatcher.class);
					// 采购入库取消签字推srm
					sys.dispatch(vo, "srm_m45_cancelSign", null);
				}
			} catch (Exception e) {
				ExceptionUtils.wrappException(e);
			}
		}
	}

}

package nccloud.pubimpl.scmpub.ssc.service;

import nc.bs.scmpub.ssc.util.PfServiceScmForSSC;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.pubitf.scmpub.ssc.service.ISSCService;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * nccloud¹²Ïí·þÎñ
 *
 * @author cuijun
 *
 */
public class SSCServiceImpl implements ISSCService {

	@Override
	public String[] isStartSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) throws BusinessException {
		return PfServiceScmForSSC.getFlowStartActionNames(vos, module);
	}

	@Override
	public String[] isEndSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) throws BusinessException {
		return PfServiceScmForSSC.getFlowEndActionNames(vos, module);
	}
}

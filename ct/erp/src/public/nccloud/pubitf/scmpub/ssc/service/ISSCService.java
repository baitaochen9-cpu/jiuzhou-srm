package nccloud.pubitf.scmpub.ssc.service;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * nccloud共享服务
 * 
 * @author cuijun
 *
 */
public interface ISSCService {
	/**
	 * 获取提交流程的动作编码，是启用审批流还是工作流
	 * 
	 * @return
	 */
	public String[] isStartSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) throws BusinessException;

	/**
	 * 获取收回流程的动作编码，是启用审批流还是工作流
	 * 
	 * @return
	 */
	public String[] isEndSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) throws BusinessException;

}

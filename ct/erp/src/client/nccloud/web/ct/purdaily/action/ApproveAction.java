package nccloud.web.ct.purdaily.action;

import itf.approvecenter.util.DataExchangeBean;
import nccloud.itf.uap.pf.IApproveBusiHandler;
import nccloud.web.scmpub.pub.resexp.PfResumeExceptionNccUtils;
import nccloud.web.workflow.approvalcenter.action.NCCFlowInteractiveConstant;

/** 
 * @description 采购合同审批服务类
 * @author xiahui
 * @date 创建时间：2019-2-19 上午9:33:36 
 * @version ncc1.0 
 * @ref nccloud.web.to.m5xmaintain.action.ApproveAction
 **/
public class ApproveAction implements IApproveBusiHandler  {

	@Override
	public DataExchangeBean checkBeforeApprove(String billtype, String billno, Object billVO) throws Exception {
		// 先默认不进行检查
		DataExchangeBean bean = new DataExchangeBean();
		bean.setCode(NCCFlowInteractiveConstant.SUCCESS);
		return bean;
	}

	@Override
	public DataExchangeBean handleApproveBusiException(String billtype, String billno, Object billVO, Exception ex)
			throws Exception {
		return PfResumeExceptionNccUtils.handleApproveResumeException(ex);
	}

}

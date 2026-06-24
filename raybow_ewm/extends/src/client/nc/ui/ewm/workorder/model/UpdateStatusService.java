package nc.ui.ewm.workorder.model;

import nc.itf.ewm.prv.IWorkOrderService;
import nc.ui.am.action.taskself.IBillOperateService;
import nc.ui.am.action.taskself.SFContext;
import nc.vo.am.proxy.AMProxy;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WOHisVO;
import nc.vo.pub.BusinessException;

public class UpdateStatusService implements IBillOperateService {
	public Object operateBill(Object bill, SFContext context) throws Exception {
		IWorkOrderService workOrderService = (IWorkOrderService) AMProxy
				.lookup(IWorkOrderService.class);
		return workOrderService.updateWorkOrderStatus((AggWorkOrderVO) bill,
				(WOHisVO) context.getUserObj(), true);
	}
	
	
	public AggWorkOrderVO updateWorkOrderHeadVO(AggWorkOrderVO[] paramAggWorkOrderVOs) throws BusinessException{
		IWorkOrderService workOrderService = (IWorkOrderService) AMProxy
				.lookup(IWorkOrderService.class);
		return workOrderService.updateWorkOrderHeadVO(paramAggWorkOrderVOs);
	}
}

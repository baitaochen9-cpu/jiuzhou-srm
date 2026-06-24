package nc.impl.ewm.wouseouter;

import nc.itf.emm.pub.IPMPubService;
import nc.itf.ewm.pub.IRepairPlanPubService;
import nc.vo.am.constant.BillTypeConst_4B;
import nc.vo.am.proxy.AMProxy;
import nc.vo.am.pub.uap.ModuleInfoQuery;
import nc.vo.ewm.workorder.AggWorkOrderVO;
import nc.vo.ewm.workorder.WorkOrderHeadVO;
import nc.vo.pub.BusinessException;

/**
 * 
 * 概要功能:工单对维修维护单据操作的实现类，调用维修维护模块的服务完成。
 * 
 * @author:cuikai
 * @date:2010-11-24
 * @version:6.0
 * @update:
 */
public class WorkOrderUseEMMImpl {
	private static IPMPubService getWriteBackPMBaseService() {
		return (AMProxy.lookup(IPMPubService.class));
	}

	/**
	 * 工单完成时回写预防性维护
	 * @param headVOs
	 * @param srcBilltype
	 * @throws BusinessException
	 */
	public static void woWriteBackPM4Finish(WorkOrderHeadVO headVOs, String srcBilltype) throws BusinessException {
		if (ModuleInfoQuery.isEMMEnabled()) {
			IPMPubService pmService = getWriteBackPMBaseService();
			// 维修计划若由预防性维护生成则也需要回写
			if (srcBilltype.equals(BillTypeConst_4B.PMCONDITION)
					||srcBilltype.equals(BillTypeConst_4B.PMRESULT)
					||srcBilltype.equals(BillTypeConst_4B.PMTIME)
					||srcBilltype.equals(BillTypeConst_4B.REPAIR_PLAN)) {		
				pmService.woWriteBackPM4Finish(headVOs);
			}
		}
	}

	/**
	 * 工单作废时回写预防性维护
	 * 
	 * @param headVOs
	 * @param srcBilltype
	 * @throws BusinessException
	 */
	public static void woWriteBackPM4Cancel(WorkOrderHeadVO headVOs, String srcBilltype) throws BusinessException {
		if (ModuleInfoQuery.isEMMEnabled()) {
			IPMPubService pmService = getWriteBackPMBaseService();
			if (srcBilltype.equals(BillTypeConst_4B.PMCONDITION)||srcBilltype.equals(BillTypeConst_4B.PMRESULT)||srcBilltype.equals(BillTypeConst_4B.PMTIME)) {
				pmService.woWriteBackPM4Canceled(headVOs);			
			}
		}
	}

	/**
	 * 工单新增时回写维修计划
	 */
	public static void writeBackRepairPlanWhenAdd(AggWorkOrderVO[] workOrderVOs) throws BusinessException {
		(AMProxy.lookup(IRepairPlanPubService.class)).reWriteRepairPlanWhenAdd(workOrderVOs);

	}

	/**
	 * 工单删除时回写维修计划
	 * 
	 * @param workOrderVOs
	 * @throws BusinessException
	 */
	public static void writeBackRepairPlanWhenDelete(AggWorkOrderVO[] workOrderVOs) throws BusinessException {
		(AMProxy.lookup(IRepairPlanPubService.class)).reWriteRepairPlanWhenDel(workOrderVOs);

	}
}

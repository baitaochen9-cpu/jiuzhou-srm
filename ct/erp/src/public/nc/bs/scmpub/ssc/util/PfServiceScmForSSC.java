package nc.bs.scmpub.ssc.util;

import nc.bs.framework.common.NCLocator;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.itf.uap.pf.IPFWorkflowQry;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.vo.bd.meta.BDObjectAdpaterFactory;
import nc.vo.bd.meta.IBDObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.IWorkFlowStatus;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmpub.util.ValueCheckUtil;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * 
 * @description
 * 接入共享的服务
 * @scene
 * 
 * @param
 * 
 *
 * @since NCC 1909
 * @version 2019年3月21日上午9:48:43
 * @author wangceb
 */
public class PfServiceScmForSSC {

	/**
	 * 判断共享是否启用
	 * @param vo
	 * @param module
	 * @return
	 */
	public static boolean isStartSSCWorkFlow(AbstractBill vo, BusiUnitTypeEnum module) {
		// 判断共享是否启用
		if (!SysInitGroupQuery.isSSCTPEnabled()) {
			return false;
		}
		try {
			NCObject ncObj = NCObject.newInstance(vo);

			BDObjectAdpaterFactory bdObjectFactory = new BDObjectAdpaterFactory();
			IBDObject bdObject = bdObjectFactory.createBDObject(vo.getParentVO());
			// 判断是否当前组织接入了共享
			UFBoolean workFlowFlag = NCLocator
					.getInstance()
					.lookup(ISSClientageMatchService.class)
					.queryUnitsByBusiUnitAndOrgPk(
							(String) bdObject.getPk_group(),
							(String) bdObject.getPk_org(),module);
			if (ValueCheckUtil.isTrue(workFlowFlag)) {
				IFlowBizItf flowBizItf = ncObj.getBizInterface(IFlowBizItf.class);
				// 获取制单人
				String billMaker = flowBizItf.getBillMaker();
				if (billMaker == null) {
					billMaker = AppContext.getInstance().getPkUser();
				}
				// 判断当前组织是否存在工作流
				boolean isHaveWork = NCLocator
						.getInstance()
						.lookup(IPFWorkflowQry.class)
						.isExistWorkflowDefinitionWithEmend(
								flowBizItf.getTranstype(),
								(String) bdObject.getPk_org(), billMaker, -1,
								WorkflowTypeEnum.Workflow.getIntValue());
				if(isHaveWork) {
					return isHaveWork;
				}else {
					ExceptionUtils.wrappBusinessException("没有匹配到可用的工作流");
				}
			}else {
				return false;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return false;
	}
	
	/**
	 * 判断共享是否启用
	 * @param vo
	 * @param module
	 * @return
	 */
	public static boolean isEndSSCWorkFlow(AbstractBill vo, BusiUnitTypeEnum module) {
		// 判断共享是否启用
		if (!SysInitGroupQuery.isSSCTPEnabled()) {
			return false;
		}
		try {
			NCObject ncObj = NCObject.newInstance(vo);

			BDObjectAdpaterFactory bdObjectFactory = new BDObjectAdpaterFactory();
			IBDObject bdObject = bdObjectFactory.createBDObject(vo.getParentVO());
			// 判断是否当前组织接入了共享
			UFBoolean workFlowFlag = NCLocator
					.getInstance()
					.lookup(ISSClientageMatchService.class)
					.queryUnitsByBusiUnitAndOrgPk(
							(String) bdObject.getPk_group(),
							(String) bdObject.getPk_org(),module);
			if (ValueCheckUtil.isTrue(workFlowFlag)) {
				IFlowBizItf flowBizItf = ncObj.getBizInterface(IFlowBizItf.class);
				// 获取制单人
				String billMaker = flowBizItf.getBillMaker();
				if (billMaker == null) {
					billMaker = AppContext.getInstance().getPkUser();
				}
				
				IPFWorkflowQry service = NCLocator.getInstance().lookup(IPFWorkflowQry.class);
				
				// 判断当前组织是否存在工作流
				boolean isHaveWork = service.isExistWorkflowDefinitionWithEmend(
								flowBizItf.getTranstype(),
								(String) bdObject.getPk_org(), billMaker, -1,
								WorkflowTypeEnum.Workflow.getIntValue());
				if(isHaveWork) {
					return isHaveWork;
				}else {
					int flow = service.queryFlowStatus(flowBizItf.getTranstype(), (String) bdObject.getId(), WorkflowTypeEnum.Workflow.getIntValue());
					if(flow == IWorkFlowStatus.BILL_NOT_IN_WORKFLOW||flow == IWorkFlowStatus.BILLTYPE_NO_WORKFLOW) return false;
					ExceptionUtils.wrappBusinessException("没有匹配到可用的工作流");
				}
			}else {
				return false;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappException(e);
		}
		return false;
	}

	/**
	 * 判断共享是否启用
	 * @param vos
	 * @param module
	 * @return
	 */
	public static boolean[] isStartSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) {
		boolean[] rets = new boolean[vos.length];
		int index = 0;
		for (AbstractBill vo : vos) {
			rets[index] = isStartSSCWorkFlow(vo,module);
			index++;
		}
		return rets;
	}
	
	/**
	 * 判断共享是否启用
	 * @param vos
	 * @param module
	 * @return
	 */
	public static boolean[] isEndSSCWorkFlow(AbstractBill[] vos, BusiUnitTypeEnum module) {
		boolean[] rets = new boolean[vos.length];
		int index = 0;
		for (AbstractBill vo : vos) {
			rets[index] = isEndSSCWorkFlow(vo,module);
			index++;
		}
		return rets;
	}
	
	/**
	 * 判断是否重走流程
	 * @param vos
	 * @param module
	 * @return
	 */
	public static boolean[] isSignalSSCWorkFlow(AbstractBill[] vos,boolean[] isHaveWorks) {
		boolean[] rets = new boolean[vos.length];
		int index = 0;
		//判断共享服务中心驳回类型
		IWorkflowMachine wfmacine=NCLocator.getInstance().lookup(IWorkflowMachine.class);
		try {
			for (AbstractBill vo : vos) {
				if(isHaveWorks[index]) {
					NCObject ncObj = NCObject.newInstance(vo);
					IFlowBizItf flowBizItf = ncObj.getBizInterface(IFlowBizItf.class);
					WorkflownoteVO noteVO = null;
						noteVO = wfmacine.checkWorkFlow("SIGNAL",
								flowBizItf.getTranstype(), vo, null);
					
					if (noteVO!=null) {//共享服务中心驳回不重走
						rets[index] = true;
					} else{
						rets[index] = false;
					}
				}
				index++;
			}
		} catch (Exception e) {
			//此次异常不处理
			//ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return rets;
	}
	
	/**
	 * 获取提交流程的动作编码，是启用审批流还是工作流
	 * @param vos
	 * @param module
	 * @return
	 */
	public static String[] getFlowStartActionNames(AbstractBill[] vos, BusiUnitTypeEnum module) {
		boolean[] isHaveWorks = isStartSSCWorkFlow(vos, module);
		boolean[] isSignalWorks = isSignalSSCWorkFlow(vos,isHaveWorks);
		String[] actionNames = new String[vos.length];
		for (int i = 0; i < actionNames.length; i++) {
			actionNames[i] = isHaveWorks[i] ? "START" : "SAVE";
			if(isSignalWorks[i]) {
				actionNames[i] = "SIGNAL";
			}
		}
		
		return actionNames;
	}
	
	
	/**
	 * 获取收回流程的动作编码，是启用审批流还是工作流
	 * @param vos
	 * @param module
	 * @return
	 */
	public static String[] getFlowEndActionNames(AbstractBill[] vos, BusiUnitTypeEnum module) {
		boolean[] isHaveWorks = isEndSSCWorkFlow(vos, module);
		String[] actionNames = new String[vos.length];
		for (int i = 0; i < actionNames.length; i++) {
			actionNames[i] = isHaveWorks[i] ? "RECALL" : "UNSAVE";
		}
		
		return actionNames;
	}

}

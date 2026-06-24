package nccloud.web.ct.purdaily.action;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.pub.action.DeleteScriptAction;

/** 
 * @description 采购合同维护删除
 * @author xiahui
 * @date 创建时间：2019-1-15 上午9:56:18 
 * @version ncc1.0 
 **/
public class DeleteAction extends DeleteScriptAction {
	
	@Override
	public String getPermissioncode() {
		return CTBillType.PurDaily.getCode();
	}
	
	@Override
	public String getActioncode() {
		return PowerActionEnum.DELETE.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtPuVO.VBILLCODE;
	}
	
	@Override
	public SCMScriptResultDTO execScript(AbstractBill[] bills) {
		CloudPFlowContext context = new CloudPFlowContext();
		context.setActionName("DELETE");
		context.setBillType("Z2");
		context.setBillVos(bills);
		// 执行提交动作脚本
		SCMScriptResultDTO result = ServiceLocator.find(IBatchRunScriptService.class).runBacth(context, AggCtPuVO.class);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getClazz() {
		return AggCtPuVO.class;
	}

}

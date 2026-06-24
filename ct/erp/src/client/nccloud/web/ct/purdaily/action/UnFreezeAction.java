package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.pub.action.BaseScriptAction;
import nccloud.web.ct.purdaily.utils.ResultUtil;
import nccloud.web.ct.purdaily.utils.ScriptActionUtil;

/**
 * @description 解冻
 * @author xiahui
 * @date 创建时间：2019-1-18 上午8:46:43
 * @version ncc1.0
 **/
public class UnFreezeAction extends BaseScriptAction {

	@Override
	public Map<String, Object> processSuccessResult(AbstractBill[] bills, AbstractBill[] orginalBills) {
		return ResultUtil.processScriptResult(bills, orginalBills);
	}

	@Override
	public void beforeProcess(Object[] objs, Map<String, Object> userObj) {
		String reason = (String) userObj.get("reason");
		ScriptActionUtil.addNewExecVO((AbstractBill[]) objs, CtFlowEnum.FROZEN.toIntValue(), reason,
				ScriptActionUtil.getUNFROZEN());
	}

	@Override
	public SCMScriptResultDTO execScript(AbstractBill[] bills) {
		CloudPFlowContext context = new CloudPFlowContext();
		context.setActionName("UNFREEZE");
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

	@Override
	protected Boolean isHandleResumeException() {
		return Boolean.TRUE;
	}

}

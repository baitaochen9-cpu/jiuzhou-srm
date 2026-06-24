package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchOprContext;
import nccloud.web.ct.purdaily.utils.ResultUtil;
import nccloud.web.ct.pub.action.BaseInterfaceAction;

/**
 * @description 变更删除
 * @author xiahui
 * @date 创建时间：2019-1-21 上午9:27:04
 * @version ncc1.0
 **/
public class ModifyDeleteAction extends BaseInterfaceAction {

	@Override
	public Map<String, Object> processSuccessResult(Object[] bills, Object[] orginalBills) {
		return ResultUtil.processScriptResult(bills, orginalBills);
	}

	@Override
	public SCMBatchOprContext createContext(Object[] objs) {
		SCMBatchOprContext context = new SCMBatchOprContext();
		context.setInterfaceName("nc.itf.ct.purdaily.IPurdailyApprove");
		context.setMethodName("modiDelete");
		context.setParams(new Object[] { objs });
		context.setParamTypes(new Class[] { AggCtPuVO[].class });
		context.setBillPos(0);
		return context;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getClazz() {
		return AggCtPuVO.class;
	}
}

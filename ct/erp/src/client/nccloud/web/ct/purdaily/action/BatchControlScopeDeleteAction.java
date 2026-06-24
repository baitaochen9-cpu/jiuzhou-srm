package nccloud.web.ct.purdaily.action;

import java.util.HashMap;
import java.util.Map;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.purdaily.service.IControlScope;
import nccloud.web.ct.purdaily.info.ControlScopeInfo;

/**
 * @description 合同控制范围批量调整,删除
 * @author xiahui
 * @date 创建时间：2019-2-26 上午9:40:07
 * @version ncc1.0
 **/
public class BatchControlScopeDeleteAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		ControlScopeInfo info = json.fromJson(str, ControlScopeInfo.class);

		try {
			// 查询原始数据
			AggCtPuVO[] bills = ServiceLocator.find(IPurdailyMaintain.class).queryCtPuVoByIds(info.getPks());
			OperateExceptionUtils.checkVo(bills, null); // 检验单据删除

			Map<String, String> puorgmap = new HashMap<String, String>(); // map<合同id,主组织>
			for (AggCtPuVO bill : bills) {
				puorgmap.put(bill.getParentVO().getPk_ct_pu(), bill.getParentVO().getPk_org());
			}

			ServiceLocator.find(IControlScope.class).deleteBatchCtrlScope(info.getPks(), info.getsCtrlScopeNew(), puorgmap);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

}

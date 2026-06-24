package nccloud.web.ct.purdaily.action;

import nc.vo.pub.BusinessException;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.purdaily.service.IControlScope;
import nccloud.web.ct.purdaily.info.ControlScopeInfo;

/**
 * @description 合同控制范围更新
 * @author xiahui
 * @date 创建时间：2019-2-26 上午9:07:12
 * @version ncc1.0
 **/
public class ControlScopeUpdateAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		ControlScopeInfo info = json.fromJson(str, ControlScopeInfo.class);
		try {
			ServiceLocator.find(IControlScope.class).updateCtrlScope(info.getPks()[0], info.getsCtrlScopeNew());
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

}

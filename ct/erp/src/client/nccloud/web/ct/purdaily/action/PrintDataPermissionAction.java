package nccloud.web.ct.purdaily.action;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.scmpub.pub.action.DataPermissionAction;

/** 
 * @description 打印数据权限控制
 * @author xiahui
 * @date 创建时间：2019-2-13 下午5:51:27 
 * @version ncc1.0 
 **/
public class PrintDataPermissionAction extends DataPermissionAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			String str = request.read();
			IJson json = JsonFactory.create();
			String[] ids = json.fromJson(str, String[].class);
			IPurdailyMaintain service = ServiceLocator.find(IPurdailyMaintain.class);
			// 查询表头信息
			AggCtPuVO [] vos = service.queryCtPuVoByIds(ids);
			this.checkPermission(vos);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.PurDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.PRINT.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtPuVO.VBILLCODE;
	}

}

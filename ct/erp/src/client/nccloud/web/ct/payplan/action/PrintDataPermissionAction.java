package nccloud.web.ct.payplan.action;

import nc.itf.ct.purdaily.ICtPayPlanQuery;
import nc.vo.ct.purdaily.entity.AggPayPlanVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.purdaily.entity.PayPlanVO;
import nc.vo.ct.purdaily.entity.PayPlanViewVO;
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
 * @date 创建时间：2019-3-5 下午7:15:55
 * @version ncc1.0
 **/
public class PrintDataPermissionAction extends DataPermissionAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			String str = request.read();
			IJson json = JsonFactory.create();
			String[] ids = json.fromJson(str, String[].class);
			PayPlanViewVO[] vos = ServiceLocator.find(ICtPayPlanQuery.class).queryPayPlanViews(ids);
			this.checkPermission(this.getAggVOFromViewVO(vos));
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	public AggPayPlanVO[] getAggVOFromViewVO(PayPlanViewVO[] viewVOs) {
		AggPayPlanVO[] aggVOs = new AggPayPlanVO[viewVOs.length];
		for (int i = 0; i < viewVOs.length; ++i) {
			CtPuVO headerVO = (CtPuVO) viewVOs[i].getVO(CtPuVO.class);
			PayPlanVO payplanVO = (PayPlanVO) viewVOs[i].getVO(PayPlanVO.class);
			aggVOs[i] = new AggPayPlanVO();
			aggVOs[i].setParent(headerVO);
			aggVOs[i].setChildrenVO(new PayPlanVO[] { payplanVO });
		}
		return aggVOs;
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

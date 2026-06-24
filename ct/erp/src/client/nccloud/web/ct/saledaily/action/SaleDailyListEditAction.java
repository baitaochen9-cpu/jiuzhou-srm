package nccloud.web.ct.saledaily.action;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.scmpub.pub.action.DataPermissionAction;

/**
 * @description 销售合同列表修改
 * @author wangshrc
 * @date 2019年1月21日 上午9:22:19
 * @version ncc1.0
 */
public class SaleDailyListEditAction extends DataPermissionAction implements
		ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo info = json.fromJson(str, SaleDailyQueryInfo.class);
		AggCtSaleVO[] aggvos = null;
		ISaledailyMaintainApp service = ServiceLocator
				.find(ISaledailyMaintainApp.class);
		try {
			aggvos = service.queryMZ3App(new String[] { info.getPk() });
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		if (null == aggvos || aggvos.length == 0) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0006")/*@res "该数据已被删除，请重新查询"*/);
		}
		this.checkPermission(aggvos);
		return null;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.SaleDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.EDIT.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtSaleVO.VBILLCODE;
	}

}
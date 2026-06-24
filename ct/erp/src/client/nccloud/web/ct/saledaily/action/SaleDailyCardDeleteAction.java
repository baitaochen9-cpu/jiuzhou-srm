package nccloud.web.ct.saledaily.action;

import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.riart.pflow.CloudPFlowContext;
import nccloud.pubitf.riart.pflow.ICloudScriptPFlowService;
import nccloud.web.scmpub.pub.action.DataPermissionAction;

/**
 * @description 销售合同卡片删除
 * @author wangshrc
 * @date 2019年1月18日 下午1:59:42
 * @version ncc1.0
 */
public class SaleDailyCardDeleteAction extends DataPermissionAction implements
		ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo info = json.fromJson(read, SaleDailyQueryInfo.class);
		AggCtSaleVO[] aggvos = null;
		ISaledailyMaintainApp service = ServiceLocator
				.find(ISaledailyMaintainApp.class);
		try {
			aggvos = service.queryMZ3App(new String[] { info.getPk() });
			if (null == aggvos || aggvos.length == 0) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0006")/*@res "该数据已被删除，请重新查询"*/);
			}
			aggvos[0].getParentVO().setTs(info.getTs());
			CloudPFlowContext context = new CloudPFlowContext();
			context.setActionName(IPFActionName.DEL_DELETE);
			context.setTrantype(aggvos[0].getParentVO().getVtrantypecode());
			context.setBillType(CTBillType.SaleDaily.getCode());
			context.setBillVos(aggvos);
			ICloudScriptPFlowService scriptService = ServiceLocator
					.find(ICloudScriptPFlowService.class);
			this.checkPermission(aggvos);
			scriptService.exeScriptPFlow(context);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.SaleDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.DELETE.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtSaleVO.VBILLCODE;
	}

}
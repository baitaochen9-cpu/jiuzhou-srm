package nccloud.web.ct.saledaily.action;

import java.util.Map;

import nc.itf.ct.saledaily.ISaledailyMaintain;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.IVOMeta;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;
import nc.vo.pubapp.pattern.tool.performance.DeepCloneTool;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.entity.SaleDailyReasonInfo;
import nccloud.dto.scmpub.pflow.SCMCloudPFlowContext;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.pubitf.scmpub.commit.service.IBatchRunScriptService;
import nccloud.web.ct.pub.action.AbstractGridAction;
import nccloud.web.ct.saledaily.utils.SaleDailyCompareUtil;
import nccloud.web.scmpub.pub.operator.SCMExtBillCardOperator;

/**
 * @description 销售合同卡片通用action
 * @author wangshrc
 * @date 2019年1月23日 下午4:02:08
 * @version ncc1.0
 */
public abstract class SaleDailyCardCommonAction extends AbstractGridAction<AggCtSaleVO> {
	protected String reason = null;

	protected abstract String getPFActionName();

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		SaleDailyReasonInfo info = json.fromJson(read, SaleDailyReasonInfo.class);
		this.reason = info.getReason();
		return super.doAction(request);
	}

	@Override
	protected AggCtSaleVO[] queryVos(String[] ids) {
		ISaledailyMaintain service = ServiceLocator.find(ISaledailyMaintain.class);
		try {
			AggCtSaleVO[] vos = service.queryCtApVoByIds(ids);
			return vos;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@Override
	protected Object action(AggCtSaleVO[] vos) {
		DeepCloneTool tool = new DeepCloneTool();
		AggCtSaleVO origvo = (AggCtSaleVO)tool.deepClone(vos[0]);
		this.beforeGetVos(vos);
		// 权限校验
		if (this.getActioncode() != null) {			
			this.checkPermission(vos);
		}
		// 转换为前台结构
		SCMExtBillCardOperator operator = SaleDailyCompareUtil.getBillCardOperator();
		appendPseudoColumn(vos);
		SCMCloudPFlowContext context = new SCMCloudPFlowContext();
		context.setBillVos(vos);
		context.setTrantype(vos[0].getParentVO().getVtrantypecode());
		context.setBillType(CTBillType.SaleDaily.getCode());
		context.setActionName(this.getPFActionName());
		this.proContext(context);
		SCMScriptResultDTO dto = ServiceLocator.find(IBatchRunScriptService.class).runBacth(context, AggCtSaleVO.class,
				new String[] { IPFActionName.SAVE, IPFActionName.APPROVE, "UNSAVEBILL", IPFActionName.UNAPPROVE,
						"TERMINATE", "UNTERMINATE", "FREEZE", "UNFREEZE", "UNVALIDATE", "VALIDATE" });
		Object obj = dto.getData();
		if (obj != null && obj instanceof Map) {
			return dto.getData();
		}
		ExtBillCard billcard = SaleDailyCompareUtil.operator(operator, dto.getSucessVOs()[0],origvo);
		return billcard;
	}
	
	protected void proContext(SCMCloudPFlowContext context) {
		
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.SaleDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return null;
	}

	@Override
	public String getBillCodeField() {
		return CtSaleVO.VBILLCODE;
	}

	/**
	 * 
	 * 主要用于处理有理由的按钮动作，增加一行exec
	 * 
	 * @param vos
	 *
	 */
	protected void beforeGetVos(AggCtSaleVO[] vos) {
	}
	/**
	 * 增加伪列
	 * @param bills
	 */
	private void appendPseudoColumn(AggCtSaleVO[] bills) {
		if (bills != null && bills.length > 0) {
			for (AggCtSaleVO bill : bills) {
					IBillMeta billMeta = bill.getMetaData();
					IVOMeta[] childMetas = billMeta.getChildren();
				    for (IVOMeta childMeta : childMetas) {
				    	ISuperVO[] clientVOs = bill.getChildren(childMeta);
				    	for (int i = 0; i < clientVOs.length; i++) {
				    		clientVOs[i].setAttributeValue("pseudocolumn", i);
						}
				    }				
			}
		}
	}
}

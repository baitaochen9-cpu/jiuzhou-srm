package nccloud.web.ct.saledaily.action;

import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nccloud.dto.scmpub.pflow.SCMCloudPFlowContext;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.scmpub.ssc.service.ISSCService;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * @description 销售合同卡片提交
 * @author wangshrc
 * @date 2019年1月23日 上午11:15:37
 * @version ncc1.0
 */
public class SaleDailyCardCommitAction extends SaleDailyCardCommonAction {
	@Override
	public String getPFActionName() {
		return IPFActionName.SAVE;
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.COMMIT.getActioncode();
	}
	
	@Override
	protected void proContext(SCMCloudPFlowContext context) {
		super.proContext(context);
		ISSCService service = ServiceLocator.find(ISSCService.class);
		String[] actionNames;
		try {
			//校验影像权限
//			service.commitCheckWithImage((AbstractBill[])context.getBillVos());
			actionNames = service.isStartSSCWorkFlow((AbstractBill[])context.getBillVos(),BusiUnitTypeEnum.SO);
			context.setActionNames(actionNames);
			context.setActionName(actionNames[0]);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
	}

}

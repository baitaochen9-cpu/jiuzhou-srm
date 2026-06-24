package nccloud.web.ct.saledaily.action;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nccloud.dto.scmpub.pflow.SCMCloudPFlowContext;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.scmpub.ssc.service.ISSCService;
import nccloud.pubitf.ssctp.sscbd.lientage.ISSClientageMatchService.BusiUnitTypeEnum;

/**
 * @description 销售合同卡片收回
 * @author wangshrc
 * @date 2019年1月23日 上午11:15:47
 * @version ncc1.0
 */
public class SaleDailyCardUnCommitAction extends SaleDailyCardCommonAction {
	@Override
	public String getPFActionName() {
		return "UNSAVEBILL";
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.UNCOMMIT.getActioncode();
	}
	
	@Override
	protected void proContext(SCMCloudPFlowContext context) {
		super.proContext(context);
		ISSCService service = ServiceLocator.find(ISSCService.class);
		String[] actionNames;
		try {
			actionNames = service.isEndSSCWorkFlow((AbstractBill[])context.getBillVos(),BusiUnitTypeEnum.SO);
			for(int i = 0;i<actionNames.length;i++) {
				if(actionNames[i].equals("UNSAVE")) {
					actionNames[i] = "UNSAVEBILL";
				}
			}
			context.setActionNames(actionNames);
			context.setActionName(actionNames[0]);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
	}

}

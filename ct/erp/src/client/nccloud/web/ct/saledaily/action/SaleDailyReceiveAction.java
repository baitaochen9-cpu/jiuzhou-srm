package nccloud.web.ct.saledaily.action;

import java.util.List;

import nc.bs.pub.relatedApp.RelatedAppVO;
import nc.itf.ct.saledaily.ISaledailyMaintain;
import nc.itf.scmpub.reference.uap.group.SysInitGroupQuery;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.scmpub.relateapp.service.IBillRelateAppService;

/**
 * 
 * @description 销售合同收款
 * @author cuijun
 * @date 2019-3-4 下午3:13:05
 * @version ncc1.0
 */
public class SaleDailyReceiveAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		try {
		      if (!SysInitGroupQuery.isAREnabled()) {
		    	  ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl
		              .getNCLangRes().getStrByID("4020003_0", "04020003-0075")/*
		                                                                       * @res
		                                                                       * "应收模块未启用!"
		                                                                       */);
		        }
			String read = request.read();
			IJson json = JsonFactory.create();
			SaleDailyQueryInfo info = json.fromJson(read, SaleDailyQueryInfo.class);
			// 校验是否满足转单,将不满足错误信息提示在合同页面
			ISaledailyMaintain saleDailyService = ServiceLocator.find(ISaledailyMaintain.class);
			saleDailyService.makePaybillByCtID(info.getPks());
			// 查询下游单据信息
			IBillRelateAppService billRelateService = ServiceLocator.find(IBillRelateAppService.class);
			List<RelatedAppVO> result = billRelateService.getRelatedAppVos(CTBillType.SaleDaily.getCode(),
					info.getCtrantypeid(), info.getDestbillType(), 1);
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return false;
	}
}

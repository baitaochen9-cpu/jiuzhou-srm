package nccloud.report.ct.purpayexec.adjuster;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ic.icreport.pubconst.ICReportTransMap;
import nc.vo.uif2.LoginContext;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;
/**
 * 采购合同付款执行情况查询条件处理器
 * @author yangls7
 * @date 2019-3-11 
 * @version ncc1909
 */
public class PurPaymentExecConditionProcessor extends DefaultConditionProcessor{

	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		IQueryCondition condition = super.doQueryByWebScheme(context,
				reportModel, queryScheme);
		ICReportTransMap tranMap = new ICReportTransMap();
		
		tranMap.put("scheme", queryScheme);
		tranMap.put("loginContext", getLoginContext(context));
		BaseQueryCondition result = (BaseQueryCondition) condition;
		result.setUserObject(tranMap);
		result.setDescriptors(null);
		return result;
	}
	/**
	 * 构建一个loginContext，后续要用到
	 * @param context
	 * @return
	 */
	private LoginContext getLoginContext(IContext context){

			LoginContext loginContext = new LoginContext();
			loginContext.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
			loginContext.setPk_loginUser(InvocationInfoProxy.getInstance().getUserId());
			return loginContext;
	}

}

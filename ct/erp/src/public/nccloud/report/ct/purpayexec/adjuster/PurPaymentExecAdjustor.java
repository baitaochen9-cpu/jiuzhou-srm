package nccloud.report.ct.purpayexec.adjuster;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.model.AbsAnaReportModel;
import com.ufida.report.anareport.model.AnaReportModel;

import nc.itf.iufo.freereport.extend.IAreaCondition;
import nc.itf.iufo.freereport.extend.IQueryConditionProcessor;
import nc.itf.iufo.freereport.extend.IReportAdjustorForWeb;

/**
 * 采购合同付款计划执行情况查询格式调整器
 * @author yangls7
 * @date 2019-3-8 
 * @version ncc1909
 */
public class PurPaymentExecAdjustor implements IReportAdjustorForWeb {

	@Override
	public void doAreaAdjust(IContext context, String areaPK,
			IAreaCondition areaCond, AbsAnaReportModel reportModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doReportAdjust(IContext context, AnaReportModel reportModel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IQueryConditionProcessor getConditionProcessor(IContext context,
			AnaReportModel reportModel) {
		
		return new PurPaymentExecConditionProcessor();
	}

}

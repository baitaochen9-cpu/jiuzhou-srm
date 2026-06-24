package nccloud.report.ct.purexecution.adjuster;

import nc.bs.ic.icreport.pub.ICReportConditionUtils;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.AggrDescriptor;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.report.queryinfo.CtQueryInfoPara;
import nc.vo.ct.report.queryinfo.purdaily.CtPurExecQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.ct.report.util.CtReportAdjustor;
import nc.vo.ic.icreport.pubconst.ICReportTransMap;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nccloud.commons.lang.ArrayUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * @description TODO
 * @author zhengxinm
 * @date 2019-1-9 上午11:28:11
 * @version ncc1.0
 */
public class PurExecutionConditionProcessor extends DefaultConditionProcessor {
	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		IQueryCondition condition = super.doQueryByWebScheme(context,
		reportModel, queryScheme);
		ICReportTransMap tranMap = new ICReportTransMap();
		// 设置查询变量
	    ICReportConditionUtils.setDescription(context, queryScheme);
	    ConditionVO[] queryconds =
		        new ICReportConditionUtils()
		            .getGeneralConditionVOFromIQueryScheme(queryScheme);
	    BaseQueryCondition result = (BaseQueryCondition) condition;
	    // 获取查询模板条件
	 
	    AggrDescriptor desc = new AggrDescriptor();
	    
	    CtPurExecQryInfoPara para = new CtPurExecQryInfoPara();
	    ConditionVO[] conds = (ConditionVO[]) queryScheme
				.get("logicalcondition");

	    if (ArrayUtils.isEmpty(conds) || null == conds[0]
	        || StringUtil.isEmptyWithTrim(conds[0].getValue())) {
	      // 明细查询
	      para.setGroupcondtion(null);
	      result.setDescriptors(new Descriptor[] {
	        desc
	      });
	    }
	    else {
	      // 汇总查询
	      para.setGroupcondtion(conds[0].getValue());
	      result.setDescriptors(new Descriptor[] {
	        desc, new CtAggrDescriptor<CtPurExecQryInfoPara>(para)
	      });
	    }
	    tranMap.put(CtQueryInfoPara.QUERY_CONDS, queryconds);
	    result.setUserObject(tranMap);
	    context.setAttribute(CtQueryInfoPara.QUERY_PARA, para);
	    result.setRoportAdjustor(new CtReportAdjustor());
	    return result;

	}
}

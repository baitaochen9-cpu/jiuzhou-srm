package nccloud.report.ct.saledaily;

import nccloud.pubitf.ic.pub.utils.ICReportConditionUtils;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.descriptor.FilterDescriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.report.queryinfo.saledaily.CtSaleExecQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nccloud.commons.lang.ArrayUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * @description 销售合同执行情况查询报表特殊处理
 * @author cuijun
 * @date 2019-01-08 上午10:10:43
 * @version ncc1.0
 */
public class SaleReportExecQryProcessor extends DefaultConditionProcessor {
	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		if (queryScheme == null) {
			return new BaseQueryCondition(false);
		}
		IQueryCondition condition = super.doQueryByWebScheme(context,
				reportModel, queryScheme);
		return this.doBusinessProcess(condition, context, queryScheme);
	}

	/**
	 * 特殊业务处理
	 * 
	 * @param condition
	 * @param context
	 * @param queryScheme
	 * @return
	 */
	private IQueryCondition doBusinessProcess(IQueryCondition condition,
			IContext context, IQueryScheme queryScheme) {
		// 设置查询变量
		ICReportConditionUtils.setDescription(context, queryScheme);
		if (condition == null || !(condition instanceof BaseQueryCondition)) {
			return condition;
		}
		BaseQueryCondition result = (BaseQueryCondition) condition;
		// 获取查询模板条件
		String whereSql = queryScheme.getWhereSQLOnly();
		// where条件直接处理成筛选描述器
		FilterItem item = new FilterItem();
		item.setExpression(whereSql);
		item.setManualExpression(true);
		FilterDescriptor filter = new FilterDescriptor();
		filter.addFilter(item);
		CtSaleExecQryInfoPara para = new CtSaleExecQryInfoPara();
		ConditionVO[] conds = (ConditionVO[]) queryScheme
				.get("logicalcondition");
		if (ArrayUtils.isEmpty(conds) || null == conds[0]
				|| StringUtil.isEmptyWithTrim(conds[0].getValue())) {
			// 明细查询
			para.setGroupcondtion(null);
			result.setDescriptors(new Descriptor[] { filter });
		} else {
			// 汇总查询
			para.setGroupcondtion(conds[0].getValue());
			result.setDescriptors(new Descriptor[] { filter,
					new CtAggrDescriptor<CtSaleExecQryInfoPara>(para) });
		}
		return result;
	}
}

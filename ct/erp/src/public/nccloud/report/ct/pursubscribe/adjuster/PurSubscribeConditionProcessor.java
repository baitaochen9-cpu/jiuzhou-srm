package nccloud.report.ct.pursubscribe.adjuster;

import nc.bs.ic.icreport.pub.ICReportConditionUtils;
import nc.bs.pubapp.report.ReportPermissionUtils;
import nc.bs.scmpub.report.ReportQueryCondition;
import nc.impl.pubapp.env.BSContext;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.report.queryinfo.CtQueryInfoPara;
import nc.vo.ct.report.queryinfo.purdaily.CtPurSubQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.ic.icreport.pubconst.ICReportTransMap;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.ic.pub.util.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nccloud.commons.lang.ArrayUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * 采购合同汇总表条件处理器
 * 
 * @author yangls7
 * @date 2019-3-4
 * @version ncc1909
 */
public class PurSubscribeConditionProcessor extends DefaultConditionProcessor {

	// 库存报表后台传递对象
	private transient ICReportTransMap tranMap = null;

	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		// 设置权限
		ReportPermissionUtils utils = new ReportPermissionUtils(context);
		utils.setMainBeanClass(CtPuVO.class);

		BaseQueryCondition condition = this.createQueryCondition(context);
		this.buildTranMap(queryScheme);
		ICReportConditionUtils.setDescription(this.tranMap, queryScheme);
		// 处理特别条件
		this.dealConditionVO();
		this.doBusinessProcess(condition, context);
		// 放到UserObject，带到后台
		condition.setUserObject(this.tranMap);
		// 不需要处理双引擎
		this.getTranMap().put(CtQueryInfoPara.QUERY_CONDS,
				this.getTranMap().getConditionVOs());
		this.getTranMap().put(CtQueryInfoPara.QUERY_FLAG, Boolean.TRUE);
		return condition;
	}

	protected void doBusinessProcess(IQueryCondition condition, IContext context) {
		FilterItem item = new FilterItem();
		item.setExpression(this.getTranMap().getWherePart());
		BaseQueryCondition result = (BaseQueryCondition) condition;
		CtPurSubQryInfoPara para = new CtPurSubQryInfoPara();
		ConditionVO[] conds = this.getTranMap().getSelectConditionVOs();
		if (ArrayUtils.isEmpty(conds) || null == conds[0]
				|| StringUtil.isSEmptyOrNull(conds[0].getValue())) {
			// 默认汇总口径为物料+供应商
			para.setGroupcondtion(null);
			result.setDescriptors(new Descriptor[] {

			});
		} else {
			para.setGroupcondtion(conds[0].getValue());
			result
					.setDescriptors(new Descriptor[] { new CtAggrDescriptor<CtPurSubQryInfoPara>(
							para) });
		}
		this.getTranMap().put(CtQueryInfoPara.QUERY_PARA, para);
		return;
	}

	/**
	 * 创建基础条件
	 * 
	 * @param context
	 * @return
	 */
	protected BaseQueryCondition createQueryCondition(IContext context) {
		ReportQueryCondition condition = new ReportQueryCondition(true);
		return condition;
	}

	/**
	 * 构建传递缓存类
	 * 
	 * @param where
	 */
	private void buildTranMap(IQueryScheme queryScheme) {
		// 构建返回对象
		this.tranMap = new ICReportTransMap();
		this.tranMap.setNewQuery(true);
		ICReportConditionUtils.buildConditionVO(queryScheme, this.tranMap);
		this.tranMap.setGeneralConditionVOs(CollectionUtils.combineArrs(
				this.tranMap.getGeneralConditionVOs(),
				new ConditionVO[] { this.groupCondition() }));
		this.tranMap.setQueryScheme(queryScheme);
		this.tranMap.setWherePart(new ConditionVO().getSQLStr(this.tranMap
				.getGeneralConditionVOs()));
	}

	public ICReportTransMap getTranMap() {
		return this.tranMap;
	}

	/**
	 * 构造集团条件
	 * 
	 * @return
	 */
	private ConditionVO groupCondition() {
		ConditionVO cond = new ConditionVO();
		cond.setDataType(5);
		cond.setFieldCode(ICPubMetaNameConst.PK_GROUP);
		cond.setOperaCode("=");
		cond.setValue(BSContext.getInstance().getGroupID());
		return cond;
	}

	/**
	 * 处理左包函和单选的操作符
	 */
	protected void dealConditionVO() {
		ConditionVO[] conds = this.tranMap.getGeneralConditionVOs();
		for (ConditionVO cond : conds) {
			// 日期型不再处理
			// this.dealUFDate(cond);
			if (cond.getOperaCode().equalsIgnoreCase("left like")) {
				cond.setOperaCode("like");
				String value = cond.getValue();
				if (!StringUtil.isSEmptyOrNull(value) && !value.endsWith("%")) {
					cond.setValue(cond.getValue() + "%");
				}
				continue;
			}
			if (cond.getOperaCode().equalsIgnoreCase("==")) {
				cond.setOperaCode("=");
				continue;
			}
		}
	}
}

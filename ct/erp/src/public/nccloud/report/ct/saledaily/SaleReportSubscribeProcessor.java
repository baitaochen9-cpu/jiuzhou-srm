package nccloud.report.ct.saledaily;

import nc.bs.pubapp.report.ReportPermissionUtils;
import nc.impl.pubapp.env.BSContext;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.scmmm.vo.scmpub.report.entity.SCMReportTransMap;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.report.queryinfo.CtQueryInfoPara;
import nc.vo.ct.report.queryinfo.saledaily.CtSaleSubQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.ic.icreport.pubconst.ICReportTransMap;
import nc.vo.ic.pub.define.ICPubMetaNameConst;
import nc.vo.ic.pub.util.CollectionUtils;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nccloud.commons.lang.ArrayUtils;
import nccloud.pubitf.ic.pub.utils.ICReportConditionUtils;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * @description 销售合同汇总查询报表特殊处理
 * @author cuijun
 * @date 2019-01-08 上午10:10:43
 * @version ncc1.0
 */
public class SaleReportSubscribeProcessor extends DefaultConditionProcessor {
	// 报表后台传递对象
	private ICReportTransMap tranMap = null;

	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		// 构造查询条件
		BaseQueryCondition condition = (BaseQueryCondition) super
				.doQueryByWebScheme(context, reportModel, queryScheme);
		if (queryScheme == null) {
			return null;
		}
		// 设置权限
		ReportPermissionUtils utils = new ReportPermissionUtils(context);
		utils.setMainBeanClass(CtSaleVO.class);

		// 构造传递对象
		this.buildTranMap(queryScheme);
		// 处理业务组自己的实现
		this.doBusinessProcess(condition, context);
		this.getTranMap().put(CtQueryInfoPara.QUERY_CONDS,
				this.getTranMap().getConditionVOs());
		this.getTranMap().put(CtQueryInfoPara.QUERY_FLAG, Boolean.TRUE);
		// 放到UserObject，带到后台
		condition.setUserObject(this.getTranMap());
		return condition;
	}

	private void doBusinessProcess(BaseQueryCondition condition,
			IContext context) {
		BaseQueryCondition result = (BaseQueryCondition) condition;
		CtSaleSubQryInfoPara para = new CtSaleSubQryInfoPara();
		ConditionVO[] conds = this.getTranMap().getSelectConditionVOs();
		if (ArrayUtils.isEmpty(conds) || null == conds[0]
				|| StringUtil.isEmptyWithTrim(conds[0].getValue())) {
			// 默认汇总口径为物料+供应商
			para.setGroupcondtion(null);
			result.setDescriptors(new Descriptor[] {

			});
		} else {
			para.setGroupcondtion(conds[0].getValue());
			result.setDescriptors(new Descriptor[] { new CtAggrDescriptor<CtSaleSubQryInfoPara>(
					para) });
		}
		this.getTranMap().put(CtQueryInfoPara.QUERY_PARA, para);
		return;
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

	public SCMReportTransMap getTranMap() {
		return this.tranMap;
	}
}

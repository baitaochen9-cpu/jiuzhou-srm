package nccloud.report.ct.saledaily;

import nccloud.pubitf.ic.pub.utils.ICReportConditionUtils;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.descriptor.FilterDescriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.enumeration.MarClassBoundEnum;
import nc.vo.ct.report.queryinfo.saledaily.CtSaleQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;

/**
 * @description 销售合同查询报表特殊处理
 * @author cuijun
 * @date 2019-01-08 上午10:10:43
 * @version ncc1.0
 */
public class SaledailyReportQueryProcessor extends DefaultConditionProcessor {
	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		if (queryScheme == null) {
			return new BaseQueryCondition(false);
		}
		// 构造查询条件
		BaseQueryCondition condition = (BaseQueryCondition) super
				.doQueryByWebScheme(context, reportModel, queryScheme);
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
		ConditionVO[] conds = (ConditionVO[]) queryScheme
				.get("logicalcondition");
		BaseQueryCondition result = (BaseQueryCondition) condition;
		// 获取查询模板条件
		String whereSql = queryScheme.getWhereSQLOnly();
		// 查询条件增加 物料分类范围和物料基本分类(物料分类编码)的组合条件
		whereSql += this.getMarbasclass(conds);

		// where条件直接处理成筛选描述器
		FilterItem item = new FilterItem();
		item.setExpression(whereSql);
		item.setManualExpression(true);
		FilterDescriptor filter = new FilterDescriptor();
		filter.addFilter(item);
		CtSaleQryInfoPara para = new CtSaleQryInfoPara();

		ConditionVO groupCond = null;
		for (ConditionVO cond : conds) {
			if ("groupcondition".equals(cond.getFieldCode())) {
				groupCond = cond;
				break;
			}
		}
		if (null == groupCond
				|| StringUtil.isEmptyWithTrim(groupCond.getValue())) {
			// 明细查询
			para.setGroupcondtion(null);
			result.setDescriptors(new Descriptor[] { filter });
		} else {
			// 汇总查询
			para.setGroupcondtion(groupCond.getValue());
			result.setDescriptors(new Descriptor[] { filter,
					new CtAggrDescriptor<CtSaleQryInfoPara>(para) });
		}
		return result;
	}

	/**
	 * 添加 物料基本分类 为 固定条件
	 * 
	 * @param qrySchemeProcessor
	 */
	private String getMarbasclass(ConditionVO[] conds) {
		SqlBuilder inCondition = new SqlBuilder();
		SqlBuilder inConditionNew = new SqlBuilder();
		ConditionVO condMarClassBound = null;
		ConditionVO condMarBasClass = null;
		for (ConditionVO cond : conds) {
			// 物料分类范围
			if ("marbasclassbound".equals(cond.getFieldCode())) {
				condMarClassBound = cond;
			}
			// 物料基本分类
			else if ("ct_sale_b.pk_srcmaterial.pk_marbasclass".equals(cond
					.getFieldCode())) {
				condMarBasClass = cond;
			}
		}

		if (null != condMarClassBound && null != condMarBasClass
				&& null != condMarBasClass.getValue()
				&& null != condMarClassBound.getValue()) {
			String[] bounds = this
					.formatCondition(condMarClassBound.getValue()).substring(1)
					.split(",");
			for (String bound : bounds) {
				if (!"".equals(inCondition.toString())) {
					inCondition.append(" or ");
				}

				// 物料分类范围 为 物料基本分类
				if (MarClassBoundEnum.MARBASCLASS.getEnumValue().getValue()
						.equals(String.valueOf(bound.charAt(1)))) {
					// 查询模板中物料基本分类是否包含下级:
					// // 1.不包含下级时
					// inCondition.append("ct_sale_b.pk_marbasclass in ");
					// inCondition.append(this.formatCondition(condMarBasClass.getValue()));
					// 2.包含下级时
					if (condMarBasClass.getValue().contains(",")) {
						inCondition.append("ct_sale_b.pk_marbasclass in ");
						inCondition.append(condMarBasClass.getValue());
					} else {
						inCondition.append("ct_sale_b.pk_marbasclass",
								condMarBasClass.getValue());
					}

				}
				// 物料分类范围 为 物料
				else if (MarClassBoundEnum.MATERIAL.getEnumValue().getValue()
						.equals(String.valueOf(bound.charAt(1)))) {
					inCondition.append("ct_sale_b.pk_material in (select ");
					inCondition.append(CtAbstractBVO.PK_MATERIAL);
					inCondition.append(" from bd_material where ");
					
					// 查询模板中物料基本分类是否包含下级:
					// // 1.不包含下级时
					// inCondition.append(" in ");
					// inCondition.append(this.formatCondition(condMarBasClass.getValue()));
					// inCondition.append(") ");
					// 2.包含下级时
					if (condMarBasClass.getValue().contains(",")) {
						inCondition.append(CtAbstractBVO.PK_MARBASCLASS);
						inCondition.append(" in ");
						inCondition.append(condMarBasClass.getValue());
						inCondition.append(") ");
					}else {
						inCondition.append(CtAbstractBVO.PK_MARBASCLASS,
								condMarBasClass.getValue());
						inCondition.append(") ");
						
					}
					
				}
			}
			inConditionNew.append(" and (");
			inConditionNew.append(inCondition.toString());
			inConditionNew.append(") ");
		}
		return inConditionNew.toString();
	}

	private String formatCondition(String condValue) {
		String[] conds = condValue.split(",");
		if (conds.length == 1) {
			return "('" + condValue + "')";
		}
		return condValue;
	}
}

package nccloud.report.ct.purdaily.adjuster;

import nc.bs.pubapp.report.ReportPermissionUtils;
import nc.itf.iufo.freereport.extend.IQueryCondition;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.descriptor.FilterDescriptor;
import nc.pub.smart.model.descriptor.FilterItem;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.enumeration.MarClassBoundEnum;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.ct.report.queryinfo.CtQueryInfoPara;
import nc.vo.ct.report.queryinfo.purdaily.CtPurQryInfoPara;
import nc.vo.ct.report.util.CtAggrDescriptor;
import nc.vo.ct.report.util.CtReportAdjustor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

import com.ufida.dataset.IContext;
import com.ufida.report.anareport.base.BaseQueryCondition;
import com.ufida.report.anareport.base.DefaultConditionProcessor;
import com.ufida.report.anareport.model.AbsAnaReportModel;
/**
 * 
 * @description 采购合同查询
 * @author zhengxinm 
 * @date 2019-1-18 上午8:51:28 
 * @version ncc1.0
 */
public class PurdailyConditionProcessor  extends DefaultConditionProcessor{
	@Override
	public IQueryCondition doQueryByWebScheme(IContext context,
			AbsAnaReportModel reportModel, IQueryScheme queryScheme) {
		IQueryCondition condition = super.doQueryByWebScheme(context, reportModel, queryScheme);
		
		
		    BaseQueryCondition result = (BaseQueryCondition) condition;
		    // 获取查询模板条件
		    ConditionVO[] conds = (ConditionVO[]) queryScheme
					.get("logicalcondition");
		    // 查询条件增加 物料分类范围和物料基本分类(物料分类编码)的组合条件
		    String whereSql = queryScheme.getWhereSQLOnly();
		    whereSql += this.getMarbasclass(conds);

		    // where条件直接处理成筛选描述器
		    FilterItem item = new FilterItem();
		    item.setExpression(whereSql);
		    item.setManualExpression(true);
		    FilterDescriptor filter = new FilterDescriptor();
		    filter.addFilter(item);
		    CtPurQryInfoPara para = new CtPurQryInfoPara();   
		    ConditionVO groupCond = null;
		    for (ConditionVO cond : conds) {
		      if ("groupcondition".equals(cond.getFieldCode())) {
		        groupCond = cond;
		        break;
		      }
		    }
		    if (null == groupCond || StringUtil.isEmptyWithTrim(groupCond.getValue())) {
		      // 明细查询
		      para.setGroupcondtion(null);
		      result.setDescriptors(new Descriptor[] {
		        filter
		      });
		    }
		    else {
		      // 汇总查询
		      para.setGroupcondtion(groupCond.getValue());
		      result.setDescriptors(new Descriptor[] {
		        filter, new CtAggrDescriptor<CtPurQryInfoPara>(para)
		      });
		    }
		    context.setAttribute(CtQueryInfoPara.QUERY_PARA, para);
		    result.setRoportAdjustor(new CtReportAdjustor());
		    // 设置权限
		    ReportPermissionUtils utils = new ReportPermissionUtils(context);
		    utils.setMainBeanClass(CtPuVO.class);
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
	      else if ("ct_pu_b.pk_srcmaterial.pk_marbasclass".equals(cond
	          .getFieldCode())) {
	        condMarBasClass = cond;
	      }
	    }

	    if (null != condMarClassBound && null != condMarBasClass
	        && null != condMarBasClass.getValue()
	        && null != condMarClassBound.getValue()) {
	      String[] bounds =
	          this.formatCondition(condMarClassBound.getValue()).substring(1)
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
	          // inCondition.append("ct_pu_b.pk_marbasclass in ");
	          // inCondition.append(this.formatCondition(condMarBasClass.getValue()));
	          // 2.包含下级时
	          inCondition.append("ct_pu_b.pk_marbasclass in (");
	          inCondition.append(condMarBasClass.getValue());
	          inCondition.append(")");
	        }
	        // 物料分类范围 为 物料
	        else if (MarClassBoundEnum.MATERIAL.getEnumValue().getValue()
	            .equals(String.valueOf(bound.charAt(1)))) {
	          inCondition.append("ct_pu_b.pk_material in (select ");
	          inCondition.append(CtAbstractBVO.PK_MATERIAL);
	          inCondition.append(" from bd_material where ");
	          inCondition.append(CtAbstractBVO.PK_MARBASCLASS);
	          // 查询模板中物料基本分类是否包含下级:
	          // // 1.不包含下级时
	          // inCondition.append(" in ");
	          // inCondition.append(this.formatCondition(condMarBasClass.getValue()));
	          // inCondition.append(") ");
	          // 2.包含下级时
	          inCondition.append(" in (");
	          inCondition.append(condMarBasClass.getValue());
	          inCondition.append(")) ");
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

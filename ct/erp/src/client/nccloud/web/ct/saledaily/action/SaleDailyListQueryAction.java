package nccloud.web.ct.saledaily.action;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.enumeration.MarClassBoundEnum;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nccloud.dto.scmpub.page.entity.SCMQueryTreeFormatVO;
import nccloud.dto.so.delivery.enumeration.TabCodeEnum;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;
import nccloud.web.scmpub.page.action.AbstractPageQueryAction;

/**
 * @description 销售合同列表查询
 * @author wangshrc
 * @date 2019年1月14日 下午5:01:52
 * @version ncc1.0
 */
public class SaleDailyListQueryAction extends AbstractPageQueryAction<AggCtSaleVO> {

	@Override
	public String getTabConditionClazz() {
		return "nccloud.pubimpl.ct.saledaily.service.SaleDailyTabCondition";
	}

	@Override
	protected void modifyQuerySchme(SCMQueryTreeFormatVO queryInfo, IQueryScheme queryScheme) {
		QuerySchemeProcessor qrySchemeProcessor = new QuerySchemeProcessor(queryScheme);
		String headTableName = qrySchemeProcessor.getMainTableAlias();
		qrySchemeProcessor.appendWhere(" and " + headTableName + ".bshowlatest= 'Y' ");
		//处理 物料基本分类 固定条件
		this.putMarbasclassIntoCondition(qrySchemeProcessor);
	}

	private void putMarbasclassIntoCondition(QuerySchemeProcessor qrySchemeProcessor) {

	    QueryCondition conditionMarbasclass =
	        qrySchemeProcessor.getQueryCondition("pk_ct_sale_b.pk_marbasclass");
	    QueryCondition conditionMaterialBound =
	        qrySchemeProcessor.getQueryCondition("marbasclassbound");
	    if (null != conditionMarbasclass
	        && conditionMarbasclass.getValues().length > 0) {
	      String bodyTableName =
	          qrySchemeProcessor
	              .getTableAliasOfAttribute("pk_ct_sale_b.pk_marbasclass");
	      Object[] values = conditionMarbasclass.getValues();
	      SqlBuilder inConditionNew = new SqlBuilder();
	      // 查询模板中物料基本分类是否包含下级:
	      // // 1.不包含下级时
	      // SqlBuilder inCondition = new SqlBuilder();
	      // for (Object value : values) {
	      // inCondition.append("'" + value.toString() + "',");
	      // }
	      // // 取得 物料基本分类 列表
	      // inConditionNew.append(inCondition.toString().substring(0,
	      // inCondition.toString().length() - 1));

	      // 2.包含下级时
	    for (Object value : values) {
	    	inConditionNew.append("'" + value.toString() + "',");
	       }
//	      inConditionNew.append(values[0]);
	      Object[] boundValues = conditionMaterialBound.getValues();
	      SqlBuilder boundCondition = new SqlBuilder();
	      for (Object boundValue : boundValues) {
	        if (boundCondition.toString().length() != 0) {
	          boundCondition.append(" or ");
	        }
	        // 物料分类范围 为 物料
	        if (MarClassBoundEnum.MATERIAL.getEnumValue().getValue()
	            .equals(boundValue)) {
	          boundCondition.append(bodyTableName);
	          boundCondition.append(".");
	          boundCondition.append(CtAbstractBVO.PK_MATERIAL);
	          boundCondition.append(" in (select ");
	          boundCondition.append(CtAbstractBVO.PK_MATERIAL);
	          boundCondition.append(" from bd_material where ");
	          boundCondition.append(CtAbstractBVO.PK_MARBASCLASS);
	          boundCondition.append(" in (");
	          boundCondition.append(inConditionNew.toString().substring(0, inConditionNew.toString().length()-1));
	          boundCondition.append("))");
	        }
	        // 物料分类范围 为 物料基本分类
	        else if (MarClassBoundEnum.MARBASCLASS.getEnumValue().getValue()
	            .equals(boundValue)) {
	          boundCondition.append(bodyTableName);
	          boundCondition.append(".");
	          boundCondition.append(CtAbstractBVO.PK_MARBASCLASS);
	          boundCondition.append(" in (");
	          boundCondition.append(inConditionNew.toString().substring(0, inConditionNew.toString().length()-1));
	          boundCondition.append(")");
	        }
	      }
	      qrySchemeProcessor
	          .appendWhere(" and (" + boundCondition.toString() + ")");
	    }
	  
		
	}

	@Override
	public String[] getHeadOrderFields() {
		return new String[] { CtAbstractVO.VBILLCODE };
	}

	@Override
	public String[] getAllTabs() {
		return TabCodeEnum.AllEnums;
	}

	@Override
	public String getAllTabCode() {
		return TabCodeEnum.All.value();
	}

	@Override
	protected void afterProcess(Grid grid) {
		SaleDailyPrecisionUtil.dealPrecision(grid);
	}
}

package nc.impl.ct.purdaily;

import nc.bs.scmpub.page.ApproveBillFilter;
import nc.bs.scmpub.page.BillPageLazyQuery;
import nc.bs.scmpub.page.IBillFilter;
import nc.impl.pubapp.pattern.data.bill.EfficientBillQuery;
import nc.itf.ct.purdaily.IPurdailyMaintainApp;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.enumeration.MarClassBoundEnum;
import nc.vo.ct.pub.CTQueryConst;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.pubapp.query2.sql.process.QueryCondition;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.scmpub.page.PageQueryVO;
import nc.vo.scmpub.res.billtype.CTBillType;

/**
 * 单据维护应用接口实现
 * 
 * @since 6.36
 * @version 2015-04-07 13:51:17
 */
public class PurdailyMaintainAppImpl implements IPurdailyMaintainApp {

  @Override
  public PageQueryVO queryMZ2App(IQueryScheme queryScheme)
      throws BusinessException {
    PageQueryVO page = null;
    try {
      // modify by liangchen1 港华合同变更生效以及重走审批流需求
      // 查询出的应该是最新的合同，用新增的bshowlatest字段
      QuerySchemeProcessor qrySchemeProcessor =
          new QuerySchemeProcessor(queryScheme);
      String headTableName = qrySchemeProcessor.getMainTableAlias();
      qrySchemeProcessor.appendWhere(" and " + headTableName
          + ".bshowlatest= 'Y' ");
      // 处理待审批固定条件
      QueryCondition condition =
          qrySchemeProcessor.getQueryCondition(CTQueryConst.BISAPPROVING);
      IBillFilter filter = null;
      if (null != condition) {
        Object[] values = condition.getValues();
        if (UFBoolean.valueOf(values[0].toString()).booleanValue()) {
          // 自由，审批中，提交
          qrySchemeProcessor.appendWhere(" and " + headTableName
              + ".fstatusflag in (0,2,7) ");
        }
        // 增加待审批单据的过滤
        filter = new ApproveBillFilter(AggCtPuVO.class, CTBillType.PurDaily);
      }

      QueryCondition orgcondition =
          qrySchemeProcessor.getQueryCondition(CTQueryConst.PK_SCOPEORG);
      if (null != orgcondition && null != orgcondition.getValues()
          && orgcondition.getValues().length > 0) {
        String[] orgs = orgcondition.getValues();
        StringBuffer orgsql = new StringBuffer("'" + orgs[0] + "'");
        for (int i = 1; i < orgs.length; i++) {
          orgsql.append(" ,'" + orgs[i] + "'");
        }
        qrySchemeProcessor
            .appendWhere(" and "
                + headTableName
                + ".pk_ct_pu in ( select pk_ct_pu from ct_scope where dr=0 and pk_scopeorg in ( "
                + orgsql.toString() + " )) ");

      }
      if (queryScheme.get("funcode") != null
          && queryScheme.get("funcode").equals("40203001")) {
        qrySchemeProcessor.appendWhere(this.getBbracketorderSql(headTableName));
      }

      // 处理 物料基本分类 固定条件
      this.putMarbasclassIntoCondition(qrySchemeProcessor);

      // 当前集团
      qrySchemeProcessor.appendCurrentGroup();
      // 当前有权限的组织
      qrySchemeProcessor.appendFuncPermissionOrgSql();
      // 排序
      // SqlBuilder order = new SqlBuilder();
      // order.append("order by ");
      // order.append(headTableName);
      // order.append(".vbillcode");
      BillPageLazyQuery<AggCtPuVO> query =
          new BillPageLazyQuery<AggCtPuVO>(AggCtPuVO.class, filter);
      query.addHeadOrder(CtAbstractVO.VBILLCODE);
      page = query.query(queryScheme);
    }
    catch (Exception e) {
      ExceptionUtils.marsh(e);
    }
    return page;
  }

  @Override
  public AggCtPuVO[] queryMZ2App(String[] ids) throws BusinessException {
    BillPageLazyQuery<AggCtPuVO> query =
        new BillPageLazyQuery<AggCtPuVO>(AggCtPuVO.class);
    AggCtPuVO[] bills = null;
    try {
      bills = query.queryPageBills(ids);
    }
    catch (Exception ex) {
      ExceptionUtils.marsh(ex);
    }
    return bills;
  }

  private String getBbracketorderSql(String headTableName) {
    SqlBuilder sql = new SqlBuilder();
    sql.append(" and ");
    sql.append(headTableName + "." + BusinessSetVO.BBRACKETORDER,
        UFBoolean.FALSE);
    return sql.toString();
  }

  /**
   * 添加 物料基本分类 为 固定条件
   * 
   * @param qrySchemeProcessor
   */
  private void putMarbasclassIntoCondition(
      QuerySchemeProcessor qrySchemeProcessor) {
    // String bodyTableName =
    // qrySchemeProcessor
    // .getTableAliasOfAttribute("pk_ct_pu_b.pk_marbasclass");
    QueryCondition conditionMarbasclass =
        qrySchemeProcessor.getQueryCondition("pk_ct_pu_b.pk_marbasclass");
    QueryCondition conditionMaterialBound =
        qrySchemeProcessor.getQueryCondition("marbasclassbound");
    if (null != conditionMarbasclass
        && conditionMarbasclass.getValues().length > 0) {
      String bodyTableName =
          qrySchemeProcessor
              .getTableAliasOfAttribute("pk_ct_pu_b.pk_marbasclass");
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
      inConditionNew.append(values[0]);

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
          boundCondition.append(inConditionNew.toString());
          boundCondition.append("))");
        }
        // 物料分类范围 为 物料基本分类
        else if (MarClassBoundEnum.MARBASCLASS.getEnumValue().getValue()
            .equals(boundValue)) {
          boundCondition.append(bodyTableName);
          boundCondition.append(".");
          boundCondition.append(CtAbstractBVO.PK_MARBASCLASS);
          boundCondition.append(" in (");
          boundCondition.append(inConditionNew.toString());
          boundCondition.append(")");
        }
      }
      qrySchemeProcessor
          .appendWhere(" and (" + boundCondition.toString() + ")");
    }
  }

@Override
public AggCtPuVO queryMZ3AppByVbillcode(String billid) throws BusinessException {
	// 根据物料 合同id 以及合同明细id 查询采购订单表体数据
	try {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" from ct_pu ");
		sql.append(" where ");
		sql.append(CtPuVO.PK_CT_PU, billid);
		AggCtPuVO[] vos = new EfficientBillQuery<AggCtPuVO>(AggCtPuVO.class).query(sql.toString());
		String vbillcode =  vos[0].getParentVO().getVbillcode();
		//变更后合同 生效后主键变为原始版本，此处通过报账传递的pk查询vbillcode再查出最新版本有效数据
		SqlBuilder sql2 = new SqlBuilder();
		sql2.append(" from ct_pu ");
		sql2.append(" where ");
		sql2.append(CtPuVO.VBILLCODE, vbillcode);
		sql2.append(" and "); 
		sql2.append(CtPuVO.BSHOWLATEST, UFBoolean.TRUE);
		sql2.append(" and ");
		sql2.append(CtPuVO.DR, 0);
		return new EfficientBillQuery<AggCtPuVO>(AggCtPuVO.class).query(sql2.toString())[0];
	} catch (Exception ex) {
		ExceptionUtils.marsh(ex);
		return null;
	}
}

}

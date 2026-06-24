package nccloud.web.ct.purdaily.ref;

import java.util.Map;

import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.business.enumeration.FpricePattern;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.res.billtype.CTBillType;
import nc.vo.scmpub.res.billtype.ECBillType;
import nc.vo.scmpub.util.StringUtil;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.processor.IRefSqlBuilder;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.pubitf.platform.db.SqlParameterCollection;

/**
 * @description 交易类型过滤参照
 * @author xiahui
 * @date 创建时间：2019-2-13 下午2:12:24
 * @version ncc1.0
 * @ref nc.ui.ct.purdaily.editor.before.head.PurdailyType
 **/
public class Z2BillTypeRefSql implements IRefSqlBuilder {

	private static final String VSRCTYPE = "VSRCTYPE";
	private static final String CECTYPECODE = "CECTYPECODE";
	private static final String PK_CT_PRICE = "PK_CT_PRICE";

	@Override
	public String getExtraSql(RefQueryInfo para, RefMeta meta) {
		Map<String, String> conditions = para.getQueryCondition();
		if (conditions != null) {
			String vsrctype = conditions.get(VSRCTYPE);
			if (vsrctype == null) {
				return this.getNoSrcWhereSql(CTBillType.PurDaily.getCode());
			} else {
				// 是否是来源EC
				String cectypecode = conditions.get(CECTYPECODE);
				String pk_ct_price = conditions.get(PK_CT_PRICE);
				if (ECBillType.EC47.getCode().equals(cectypecode) && !StringUtil.isEmptyTrimSpace(pk_ct_price)) {
					return this.getEC47WhereSql(CTBillType.PurDaily.getCode());
				} else {
					return this.getIsPriceOrEcWhereSql(CTBillType.PurDaily.getCode());
				}
			}
		}
		return null;
	}

	@Override
	public SqlParameterCollection getExtraSqlParameter(RefQueryInfo para, RefMeta meta) {
		return null;
	}

	@Override
	public String getOrderSql(RefQueryInfo para, RefMeta meta) {
		return null;
	}

	/**
	 * 获取是否是来自价格审批单或者EC sql
	 * 
	 * @param billType
	 * @return
	 */
	private String getIsPriceOrEcWhereSql(String billType) {
		try {
			SqlBuilder sql = new SqlBuilder();
			IBusinessTypeService service = ServiceLocator.find(IBusinessTypeService.class);
			String trans = service.queryTypeCodesForFilt(billType, true);
			sql.append(" pk_billtypecode in " + trans);
			sql.append(" and pk_group = '" + SessionContext.getInstance().getClientInfo().getPk_group() + "'");
			return sql.toString();
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 获取是否来源EC47 SQL
	 * 
	 * @param billType
	 * @return
	 */
	private String getEC47WhereSql(String billType) {
		SqlBuilder ecSql = new SqlBuilder();
		ecSql.append(" pk_billtypecode in ");
		ecSql.append(" (select vtrantypecode ");
		ecSql.append(" from ct_business ");
		ecSql.append(" where ");
		ecSql.append(" vtrantypecode in (select pk_billtypecode from bd_billtype ");
		ecSql.append(" where parentbilltype ='" + billType + "' ) ");
		ecSql.append(" and ");
		ecSql.append(BusinessSetVO.BBRACKETORDER, UFBoolean.FALSE);
		ecSql.append(" and ");
		ecSql.append(BusinessSetVO.FPRICEPATTERN, FpricePattern.MULTIPRICE);
		ecSql.append(")");
		ecSql.append(" and pk_group = '" + SessionContext.getInstance().getClientInfo().getPk_group() + "'");
		ecSql.append(" and dr=0");
		return ecSql.toString();
	}

	/**
	 * 获取没有来源的whereSql
	 * 
	 * @param billType
	 * @return
	 */
	private String getNoSrcWhereSql(String billType) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(" pk_billtypecode in ");
		sql.append(" (select vtrantypecode ");
		sql.append(" from ct_business ");
		sql.append(" where ");
		sql.append("  ");
		sql.append(" vtrantypecode in (select pk_billtypecode from bd_billtype ");
		sql.append(" where parentbilltype ='" + billType + "' ) ");
		sql.append(" and ");
		sql.append(BusinessSetVO.BBRACKETORDER, UFBoolean.FALSE);
		sql.append(")");
		sql.append(" and pk_group = '" + SessionContext.getInstance().getClientInfo().getPk_group() + "'");
		return sql.toString();
	}

}

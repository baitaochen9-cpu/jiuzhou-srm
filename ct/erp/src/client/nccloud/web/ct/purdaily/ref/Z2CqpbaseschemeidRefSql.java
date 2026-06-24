package nccloud.web.ct.purdaily.ref;

import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.scmpub.util.StringUtil;
import nccloud.framework.web.processor.IRefSqlBuilder;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.pubitf.platform.db.SqlParameterCollection;

/**
 * @description 优质优价方案过滤参照
 * @author xiahui
 * @date 创建时间：2019-2-15 下午3:04:36
 * @version ncc1.0
 * @ref nc.ui.ct.util.FilterPriceSchemeUtils
 **/
public class Z2CqpbaseschemeidRefSql implements IRefSqlBuilder {

	@Override
	public String getExtraSql(RefQueryInfo para, RefMeta meta) {
		Map<String, String> conditions = para.getQueryCondition();
		if (conditions != null) {
			String pk_material = conditions.get(CtAbstractBVO.PK_MATERIAL);
			String pk_org = conditions.get(CtAbstractBVO.PK_ORG);
			String pk_group = conditions.get(CtAbstractBVO.PK_GROUP);
			if (!StringUtil.isEmptyTrimSpace(pk_group) && !StringUtil.isEmptyTrimSpace(pk_org)
					&& !StringUtil.isEmptyTrimSpace(pk_material)) {
				SqlBuilder sql = new SqlBuilder();
				// by diaoxy for 合同优质优价方案 参照过滤只能过滤到本组织过滤不到集团级的 on 20130428
				sql.append("(");
				sql.append("(");
				sql.append("pk_group", pk_group);
				sql.append(" and NORGTYPE=0 ");
				sql.append(")");
				sql.append(" or ");
				sql.append("(");
				sql.append("pk_org", pk_org);
				sql.append(" and NORGTYPE=1");
				sql.append(")");
				sql.append(")");
				sql.append(" and ");
				sql.append("pk_material", pk_material);
				return sql.toString();
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

}

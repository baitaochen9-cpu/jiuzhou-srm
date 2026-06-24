package nccloud.web.ct.price.ref;

import java.util.Map;

import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nccloud.framework.web.processor.IRefSqlBuilder;
import nccloud.framework.web.processor.refgrid.RefQueryInfo;
import nccloud.framework.web.ui.meta.RefMeta;
import nccloud.pubitf.platform.db.SqlParameterCollection;
/**
 * 
 * @description 区域字段参照过滤扩展sql 
 * @author zhaoypm
 * @time 2019-4-8 上午10:41:49
 * @since ncc1.0
 */
public class RegionRefFilter implements IRefSqlBuilder {
	private static final String PK_ORG = "pk_org";
	private static final String PK_MATERIAL = "pk_material";
	private static final String PK_MARBASCLASS = "pk_marclass";

	@Override
	public String getExtraSql(RefQueryInfo para, RefMeta meta) {
		Map<String, String> condition = para.getQueryCondition();
		String pk_org = condition.get(PK_ORG);
		String pk_material = condition.get(PK_MATERIAL);
		String pk_marclass = condition.get(PK_MARBASCLASS);
		String extraSql = createExtraSql(pk_org, pk_material, pk_marclass);
		return extraSql;
	}
	/**
	 * 根据参数创建sql
	 * @param pk_org
	 * @param pk_material
	 * @param pk_marclass
	 * @return
	 */
	private String createExtraSql(String pk_org, String pk_material,
			String pk_marclass) {
		SqlBuilder sql = new SqlBuilder();
		sql.append(
				" pk_supatra=(select pk_supatra from ec_adaptmarclass where pk_org",
				pk_org);
		if (!isStringEmpty(pk_material)) {
			sql.append(
					" and pk_marclass=(select pk_marbasclass from bd_material_v where pk_source",
					pk_material);
			sql.append(")");
		} else if (!isStringEmpty(pk_marclass)) {
			sql.append(" and pk_marclass", pk_marclass);
		}
		sql.append(" and dr=0)");
		return sql.toString();
	}

	private boolean isStringEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	@Override
	public SqlParameterCollection getExtraSqlParameter(RefQueryInfo para,
			RefMeta meta) {
		return null;
	}

	@Override
	public String getOrderSql(RefQueryInfo para, RefMeta meta) {
		return null;
	}

}

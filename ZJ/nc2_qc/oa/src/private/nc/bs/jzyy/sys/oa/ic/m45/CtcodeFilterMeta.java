package nc.bs.jzyy.sys.oa.ic.m45;

import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.querytemplate.md.MDFilterMeta;

public class CtcodeFilterMeta extends MDFilterMeta {
	private static final long serialVersionUID = 3947056499017341809L;

	public CtcodeFilterMeta(String beanid, String attributePath) {
		super(beanid, attributePath);
		setDataType(5);
	}

	public String getInstrumentedSql(String basicSql) {
		SqlBuilder where = new SqlBuilder();
		String keyValue = null;
		where.append(" in (select b.pk_order_b from po_order_b b where b.ccontractid in ( select u.pk_ct_pu from ct_pu u where u.vbillcode ='"
				+ keyValue + "'))");
		return where.toString();
	}

	// public String getInstrumentedSql(String basicSql) {
	// String sql = super.getInstrumentedSql(basicSql);
	//
	// IBean cardBean = getBean(CardVO.class);
	// IBean cardHistBean = getBean(CardhistoryVO.class);
	//
	// Set<String> cardBeanAttrNames = MDUtils.getAttributeNames(cardBean);
	// Set<String> cardHistBeanAttrNames = MDUtils
	// .getAttributeNames(cardHistBean);
	//
	// String[] splitSqls = sql.split("\\s+", 2);
	// String fieldCode = splitSqls[0].trim();
	//
	// if (fieldCode.equals("pk_usedept")) {
	// sql = "fa_deptscale.pk_dept " + splitSqls[1];
	// } else if (cardBeanAttrNames.contains(fieldCode)) {
	// sql = cardBean.getTable().getName() + "." + sql.trim();
	// } else if (cardHistBeanAttrNames.contains(fieldCode)) {
	// sql = cardHistBean.getTable().getName() + "." + sql.trim();
	// }
	//
	// return sql;
	// }

}
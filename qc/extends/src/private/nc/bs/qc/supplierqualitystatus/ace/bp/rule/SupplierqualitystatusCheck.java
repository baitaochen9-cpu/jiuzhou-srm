package nc.bs.qc.supplierqualitystatus.ace.bp.rule;

import nc.bs.pu.pub.VOQryUtil;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class SupplierqualitystatusCheck {

	public void process(String pk_group, String pk_org, String pk_supplier,
			String pk_billtype) {

		SqlBuilder builder = new SqlBuilder();
		builder.append(" dr=0 and ");

		builder.append("pk_group", pk_group);
		builder.append("pk_org", pk_org);
		builder.append("pk_supplier", pk_supplier);
		SupplierqualityHVO[] vos = (SupplierqualityHVO[]) new VOQryUtil(
				SupplierqualityHVO.class).qryByCondition(builder.toString(),
				new String[] { "pk_supplierquality", "pk_supplier",
						"qualitylevel" });
	}
}
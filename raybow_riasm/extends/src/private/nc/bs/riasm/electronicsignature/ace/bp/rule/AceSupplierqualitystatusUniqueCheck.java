package nc.bs.riasm.electronicsignature.ace.bp.rule;

import nc.cmp.utils.StringUtil;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;

public class AceSupplierqualitystatusUniqueCheck implements IRule {

	@Override
	public void process(Object[] vos) {
		// 唯一性校驗規則
		if (vos == null || vos.length == 0)
			return;
		AggElectronicSignatureVO[] bills = (AggElectronicSignatureVO[]) vos;
		processBill(bills);
	}

	public void processBill(AggElectronicSignatureVO[] bills) {
		for (AggElectronicSignatureVO bill : bills) {
			SqlBuilder builder = new SqlBuilder();
			builder.append("select count(pk_supplierquality) from qc_supplierquality where  nvl(dr,0)=0 ");

			builder.append(" and pk_group", bill.getParentVO().getPk_group());
			builder.append(" and pk_org", bill.getParentVO().getPk_org());
			builder.append(" and pk_parent", bill.getParentVO().getPk_parent());

			if (!StringUtil.isEmptyWithTrim(bill.getParentVO().getPrimaryKey())) {
				builder.append(" and pk_supplierquality <> '"
						+ bill.getParentVO().getPrimaryKey() + "'");
			}
			builder.append(" group by pk_supplier");
			builder.append(" having count(pk_supplierquality)>1");

			DataAccessUtils util = new DataAccessUtils();
			IRowSet rows = util.query(builder.toString());
			if (rows.size() > 0) {
				ExceptionUtils.wrappBusinessException("相同供应商下物料+生产厂商不能重复！！");
			}
		}
	}
}
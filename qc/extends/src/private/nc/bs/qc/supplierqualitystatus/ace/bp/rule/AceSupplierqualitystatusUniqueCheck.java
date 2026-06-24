package nc.bs.qc.supplierqualitystatus.ace.bp.rule;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.cmp.utils.StringUtil;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class AceSupplierqualitystatusUniqueCheck implements IRule {

	@Override
	public void process(Object[] vos) {
		// 唯一性校驗規則
		// 1.供應商唯一性存在
		// 2.物料+生產商唯一性存在
		if (vos == null || vos.length == 0)
			return;
		AggSupplierqualityHVO[] bills = (AggSupplierqualityHVO[]) vos;
		processBill(bills);
	}

	public void processBill(AggSupplierqualityHVO[] bills) {
		BaseDAO dao = new BaseDAO();
		for (AggSupplierqualityHVO bill : bills) {
			SqlBuilder builder = new SqlBuilder();
			builder.append("select count(pk_supplierquality) from qc_supplierquality where  nvl(dr,0)=0 ");

			builder.append(" and pk_group", bill.getParentVO().getPk_group());
			builder.append(" and pk_org", bill.getParentVO().getPk_org());
			builder.append(" and pk_supplier", bill.getParentVO()
					.getPk_supplier());
			builder.append(" and pk_material", bill.getParentVO()
					.getPk_material());
			builder.append(" and pk_vendor", bill.getParentVO().getPk_vendor());

			if (!StringUtil.isEmptyWithTrim(bill.getParentVO().getPrimaryKey())) {
				builder.append(" and pk_supplierquality <> '"
						+ bill.getParentVO().getPrimaryKey() + "'");
			}
			builder.append(" group by pk_supplier,pk_vendor,pk_material");
			builder.append(" having count(pk_supplierquality)>=1");

			Integer i = 0;
			try {
				i = (Integer) dao.executeQuery(builder.toString(),
						new ColumnProcessor());
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (i != null && i.intValue() > 0) {
				ExceptionUtils.wrappBusinessException("相同供应商下物料+生产厂商不能重复！！");
			}
		}
	}
}
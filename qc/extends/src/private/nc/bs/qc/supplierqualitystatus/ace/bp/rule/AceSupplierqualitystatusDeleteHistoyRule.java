package nc.bs.qc.supplierqualitystatus.ace.bp.rule;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Log;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.am.common.util.ExceptionUtils;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;

public class AceSupplierqualitystatusDeleteHistoyRule implements IRule {

	@Override
	public void process(Object[] vos) {

		if (vos == null || vos.length == 0)
			return;
		try {
			AggSupplierqualityHVO[] bills = (AggSupplierqualityHVO[]) vos;

			List<String> list = new ArrayList<>();
			BaseDAO dao = new BaseDAO();
			for (AggSupplierqualityHVO bill : bills) {
				list.add(bill.getPrimaryKey());
			}

			String inwhere = SQLUtil.buildSqlForIn("y.pk_supplierquality",
					list.toArray(new String[list.size()]));
			StringBuffer strb = new StringBuffer();
			strb.append(" update pu_supplierhistory y  set y.dr = 1 ");
			strb.append(" where nvl(y.dr,0) = 0 and ");
			strb.append(inwhere);

			dao.executeUpdate(strb.toString());
		} catch (DAOException e) {
			Log.getInstance(this.getClass()).error(e);
			ExceptionUtils.asBusinessRuntimeException(e);
		}
	}
}
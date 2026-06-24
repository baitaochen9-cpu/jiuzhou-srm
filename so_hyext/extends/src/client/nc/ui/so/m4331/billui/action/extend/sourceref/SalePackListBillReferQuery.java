package nc.ui.so.m4331.billui.action.extend.sourceref;

import java.awt.Container;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.ISalepacklistMaintain;
import nc.ui.ic.pub.sourceref.BillReferQuery;
import nc.vo.querytemplate.TemplateInfo;

public class SalePackListBillReferQuery extends BillReferQuery {
	public SalePackListBillReferQuery(Container c, TemplateInfo info) {
		super(c, info);
	}

	public Object[] queryByWhereSql(String where) throws Exception {
		ISalepacklistMaintain queryservice = NCLocator.getInstance().lookup(ISalepacklistMaintain.class);
		return queryservice.queryRefBills(where);
	}
}
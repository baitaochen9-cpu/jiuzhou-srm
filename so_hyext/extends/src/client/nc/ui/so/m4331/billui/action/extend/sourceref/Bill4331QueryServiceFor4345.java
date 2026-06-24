package nc.ui.so.m4331.billui.action.extend.sourceref;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.ISalepacklistMaintain;
import nc.ui.pubapp.uif2app.query2.model.IRefQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

@SuppressWarnings("all")
public class Bill4331QueryServiceFor4345 implements IRefQueryService{

	@Override
	public Object[] queryByWhereSql(String paramString) throws Exception {
		ISalepacklistMaintain icq = NCLocator.getInstance().lookup(ISalepacklistMaintain.class);
		return icq.queryRefBills(paramString);
	}

	@Override
	public Object[] queryByQueryScheme(IQueryScheme iqueryscheme) throws Exception {
		ISalepacklistMaintain icq = NCLocator.getInstance().lookup(ISalepacklistMaintain.class);
		return icq.queryRefBills(iqueryscheme);
	}

}

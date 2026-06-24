package nc.ui.qc.supplierqualityapply.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.qc.ISupplierqualityapplyMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceSupplierqualityapplyMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ISupplierqualityapplyMaintain query = NCLocator.getInstance().lookup(
				ISupplierqualityapplyMaintain.class);
		return query.query(queryScheme);
	}

}
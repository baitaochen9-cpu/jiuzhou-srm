package nc.ui.qc.supplierqualitystatus.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceSupplierqualitystatusMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ISupplierqualitystatusMaintain query = NCLocator.getInstance().lookup(
				ISupplierqualitystatusMaintain.class);
		return query.query(queryScheme);
	}

}
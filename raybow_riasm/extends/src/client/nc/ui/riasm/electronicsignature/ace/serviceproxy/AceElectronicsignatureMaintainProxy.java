package nc.ui.riasm.electronicsignature.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.riasm.IElectronicsignatureMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceElectronicsignatureMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IElectronicsignatureMaintain query = NCLocator.getInstance().lookup(
				IElectronicsignatureMaintain.class);
		return query.query(queryScheme);
	}

}
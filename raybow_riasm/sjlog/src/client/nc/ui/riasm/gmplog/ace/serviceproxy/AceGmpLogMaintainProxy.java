package nc.ui.riasm.gmplog.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.riasm.IGmpLogMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceGmpLogMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IGmpLogMaintain query = NCLocator.getInstance().lookup(
				IGmpLogMaintain.class);
		return query.query(queryScheme);
	}

}
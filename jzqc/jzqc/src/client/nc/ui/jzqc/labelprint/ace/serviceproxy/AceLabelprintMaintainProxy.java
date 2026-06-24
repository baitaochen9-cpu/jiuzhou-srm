package nc.ui.jzqc.labelprint.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceLabelprintMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ILabelprintMaintain query = NCLocator.getInstance().lookup(
				ILabelprintMaintain.class);
		return query.query(queryScheme);
	}

}
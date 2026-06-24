package nc.ui.so.salepacklist.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.so.ISalepacklistMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceSalepacklistMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ISalepacklistMaintain query = NCLocator.getInstance().lookup(
				ISalepacklistMaintain.class);
		return query.query(queryScheme);
	}

}
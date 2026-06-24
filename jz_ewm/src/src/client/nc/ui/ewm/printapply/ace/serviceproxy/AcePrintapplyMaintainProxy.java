package nc.ui.ewm.printapply.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.ewm.IPrintapplyMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AcePrintapplyMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		IPrintapplyMaintain query = NCLocator.getInstance().lookup(
				IPrintapplyMaintain.class);
		return query.query(queryScheme);
	}

}
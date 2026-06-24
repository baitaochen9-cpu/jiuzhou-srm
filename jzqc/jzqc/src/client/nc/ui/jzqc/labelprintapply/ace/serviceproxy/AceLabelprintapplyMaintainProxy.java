package nc.ui.jzqc.labelprintapply.ace.serviceproxy;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelprintapplyMaintain;
import nc.ui.pubapp.uif2app.query2.model.IQueryService;
import nc.ui.querytemplate.querytree.IQueryScheme;

/**
 * 示例单据的操作代理
 * 
 * @author author
 * @version tempProject version
 */
public class AceLabelprintapplyMaintainProxy implements IQueryService {
	@Override
	public Object[] queryByQueryScheme(IQueryScheme queryScheme)
			throws Exception {
		ILabelprintapplyMaintain query = NCLocator.getInstance().lookup(
				ILabelprintapplyMaintain.class);
		return query.query(queryScheme);
	}

}
package nc.bs.jzqc.labelstatus.ace.bp;

import nc.impl.pubapp.pattern.data.vo.SchemeVOQuery;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pubapp.query2.sql.process.QuerySchemeProcessor;
import nc.vo.jzqc.labelstatus.LabelStatusVO;

public class AceLabelstatusBP {

	public LabelStatusVO[] queryByQueryScheme(IQueryScheme querySheme) {
		QuerySchemeProcessor p = new QuerySchemeProcessor(querySheme);
		p.appendFuncPermissionOrgSql();
		return new SchemeVOQuery<LabelStatusVO>(LabelStatusVO.class).query(querySheme,
				null);
	}
}

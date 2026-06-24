package nc.impl.pub.ace;
import nc.bs.jzqc.labelstatus.ace.bp.AceLabelstatusBP;
import nc.impl.pubapp.pub.smart.SmartServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.ISuperVO;
import nc.vo.jzqc.labelstatus.LabelStatusVO;
import nc.vo.uif2.LoginContext;

public abstract class AceLabelstatusPubServiceImpl extends SmartServiceImpl {
	public LabelStatusVO[] pubquerybasedoc(IQueryScheme querySheme)
			throws nc.vo.pub.BusinessException {
		return new AceLabelstatusBP().queryByQueryScheme(querySheme);
	}
}
package nc.itf.riasm;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.vo.pub.BusinessException;

public interface IGmpLogMaintain {

	public void delete(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] insert(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] update(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggGmpLogConfigHvo[] save(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] unsave(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] approve(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;

	public AggGmpLogConfigHvo[] unapprove(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException;
}

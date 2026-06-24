package nc.impl.riasm;

import nc.impl.pub.ace.AceGmpLogPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.riasm.gmplog.AggGmpLogConfigHvo;
import nc.itf.riasm.IGmpLogMaintain;
import nc.vo.pub.BusinessException;

public class GmpLogMaintainImpl extends AceGmpLogPubServiceImpl
		implements IGmpLogMaintain {

	@Override
	public void delete(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] insert(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] update(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggGmpLogConfigHvo[] save(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] unsave(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] approve(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggGmpLogConfigHvo[] unapprove(AggGmpLogConfigHvo[] clientFullVOs,
			AggGmpLogConfigHvo[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}

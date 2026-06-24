package nc.impl.jzqc;

import nc.impl.pub.ace.AceLabelprintapplyPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.itf.jzqc.ILabelprintapplyMaintain;
import nc.vo.pub.BusinessException;

public class LabelprintapplyMaintainImpl extends AceLabelprintapplyPubServiceImpl
		implements ILabelprintapplyMaintain {

	@Override
	public void delete(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] insert(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] update(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}
	public AggLabelprintapplyHVO[] pubquerybills(String where)
			throws BusinessException {
		return super.pubquerybills(where);
	}

	@Override
	public AggLabelprintapplyHVO[] save(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] unsave(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] approve(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggLabelprintapplyHVO[] unapprove(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}

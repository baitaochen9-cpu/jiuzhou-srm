package nc.impl.ewm;

import nc.impl.pub.ace.AcePrintapplyPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.itf.ewm.IPrintapplyMaintain;
import nc.vo.pub.BusinessException;

public class PrintapplyMaintainImpl extends AcePrintapplyPubServiceImpl
		implements IPrintapplyMaintain {

	@Override
	public void delete(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] insert(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] update(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggPrintapply[] save(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] unsave(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] approve(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggPrintapply[] unapprove(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}

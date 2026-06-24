package nc.itf.ewm;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.ewm.printapply.AggPrintapply;
import nc.vo.pub.BusinessException;

public interface IPrintapplyMaintain {

	public void delete(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] insert(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] update(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggPrintapply[] save(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] unsave(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] approve(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;

	public AggPrintapply[] unapprove(AggPrintapply[] clientFullVOs,
			AggPrintapply[] originBills) throws BusinessException;
}

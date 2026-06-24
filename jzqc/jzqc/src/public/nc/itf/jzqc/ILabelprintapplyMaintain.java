package nc.itf.jzqc;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.jzqc.labelprintapply.AggLabelprintapplyHVO;
import nc.vo.pub.BusinessException;

public interface ILabelprintapplyMaintain {

	public void delete(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] insert(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] update(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;
	
	public AggLabelprintapplyHVO[] pubquerybills(String where)
			throws BusinessException;

	public AggLabelprintapplyHVO[] save(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] unsave(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] approve(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;

	public AggLabelprintapplyHVO[] unapprove(AggLabelprintapplyHVO[] clientFullVOs,
			AggLabelprintapplyHVO[] originBills) throws BusinessException;
}

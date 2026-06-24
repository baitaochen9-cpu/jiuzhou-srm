package nc.itf.so;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.so.salepacklist.AggSalePackListHVO;

public interface ISalepacklistMaintain {

	public void delete(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] insert(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] update(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSalePackListHVO[] save(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] unsave(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] approve(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;

	public AggSalePackListHVO[] unapprove(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException;
	
	public abstract AggSalePackListHVO[] queryRefBills(String paramString)
			throws BusinessException;
	public abstract AggSalePackListHVO[] queryRefBills(IQueryScheme paramIQueryScheme)
			throws BusinessException;
}

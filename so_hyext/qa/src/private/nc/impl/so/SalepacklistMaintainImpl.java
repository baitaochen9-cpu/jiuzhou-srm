package nc.impl.so;

import nc.impl.pub.ace.AceSalepacklistPubServiceImpl;
import nc.itf.so.ISalepacklistMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.so.salepacklist.AggSalePackListHVO;

public class SalepacklistMaintainImpl extends AceSalepacklistPubServiceImpl
		implements ISalepacklistMaintain {

	@Override
	public void delete(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] insert(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] update(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggSalePackListHVO[] save(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] unsave(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] approve(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] unapprove(AggSalePackListHVO[] clientFullVOs,
			AggSalePackListHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSalePackListHVO[] queryRefBills(String where)
			throws BusinessException {
		return queryAllBillDatas(where);
	}

	@Override
	public AggSalePackListHVO[] queryRefBills(IQueryScheme paramIQueryScheme)
			throws BusinessException {

		return queryAllBillDatas(paramIQueryScheme.getWhereSQLOnly());
	}

}

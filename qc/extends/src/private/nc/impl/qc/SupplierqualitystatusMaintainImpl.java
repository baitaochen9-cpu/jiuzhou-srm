package nc.impl.qc;

import java.util.Map;
import java.util.Set;

import nc.impl.pub.ace.AceSupplierqualitystatusPubServiceImpl;
import nc.itf.qc.ISupplierqualitystatusMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.BusinessException;

public class SupplierqualitystatusMaintainImpl extends AceSupplierqualitystatusPubServiceImpl
		implements ISupplierqualitystatusMaintain {

	@Override
	public void delete(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] insert(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] update(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggSupplierqualityHVO[] save(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] unsave(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] approve(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggSupplierqualityHVO[] unapprove(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

	@Override
	public void checkSupplierStatus(Map<String, String> pk_material_vs,String pk_group, String pk_org,
			String pk_supplier, String pk_billtype,String supgradestatus) throws BusinessException {
		super.checkSupplierStatus(pk_material_vs, pk_group, pk_org, pk_supplier, pk_billtype, supgradestatus);
		
	}

	@Override
	public BatchOperateVO batchSaveVO(BatchOperateVO vo)
			throws BusinessException {
		return super.batchSaveVO(vo);
	}

}

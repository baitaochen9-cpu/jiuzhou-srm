package nc.itf.qc;

import java.util.Map;
import java.util.Set;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.bd.meta.BatchOperateVO;
import nc.vo.pu.supqualistatus.AggSupplierqualityHVO;
import nc.vo.pub.BusinessException;

public interface ISupplierqualitystatusMaintain {

	public void delete(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] insert(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] update(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggSupplierqualityHVO[] save(AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] unsave(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] approve(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public AggSupplierqualityHVO[] unapprove(
			AggSupplierqualityHVO[] clientFullVOs,
			AggSupplierqualityHVO[] originBills) throws BusinessException;

	public void checkSupplierStatus(Map<String, String> pk_material_vs, String pk_group,
			String pk_org, String pk_supplier, String pk_billtype,
			String supgradestatus) throws BusinessException;

	public BatchOperateVO batchSaveVO(BatchOperateVO vo)
			throws BusinessException;
}

package nc.itf.riasm;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.sm.UserVO;

public interface IElectronicsignatureMaintain {

	public void delete(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] insert(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] update(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggElectronicSignatureVO[] save(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] unsave(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] approve(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;

	public AggElectronicSignatureVO[] unapprove(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException;
	
	 UserVO findUserByCode(String userCode) throws BusinessException;
}

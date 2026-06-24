package nc.impl.riasm;

import java.util.Collection;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.impl.pub.ace.AceElectronicsignaturePubServiceImpl;
import nc.itf.riasm.IElectronicsignatureMaintain;
import nc.jdbc.framework.SQLParameter;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.sm.UserExVOUtil;
import nc.vo.sm.UserVO;

public class ElectronicsignatureMaintainImpl extends
		AceElectronicsignaturePubServiceImpl implements
		IElectronicsignatureMaintain {

	@Override
	public void delete(AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] insert(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] update(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggElectronicSignatureVO[] save(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] unsave(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] approve(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggElectronicSignatureVO[] unapprove(
			AggElectronicSignatureVO[] clientFullVOs,
			AggElectronicSignatureVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

	public UserVO findUserByCode(String userCode) throws BusinessException
	/*     */{
		/* 97 */BaseDAO dao = new BaseDAO();
		/* 98 */SQLParameter param = new SQLParameter();
		/*     */
		/* 100 */if (userCode != null)
			/* 101 */userCode = userCode.toUpperCase();
		/* 102 */param.addParam(userCode);
		/*     */
		/* 107 */Collection c = dao.retrieveByClause(UserVO.class,
				"user_code_q=?", param);
		/*     */
		/* 109 */if ((c == null) || (c.isEmpty())) {
			/* 110 */return null;
			/*     */}
		/* 112 */UserVO user = (UserVO) c.iterator().next();
		/*     */
		/* 115 */String originDataSource = InvocationInfoProxy.getInstance()
				.getUserDataSource();
		/*     */
		/* 118 */UserVO resultUser = UserExVOUtil.setUserLockedState(user);
		/*     */
		/* 120 */InvocationInfoProxy.getInstance().setUserDataSource(
				originDataSource);
		/*     */
		/* 122 */return resultUser;
		/*     */}
}

package nc.impl.bd.account;


import java.util.Map;
import nc.bs.bd.service.ValueObjWithErrLog;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.impl.bd.account.assign.AccountOptAssign;
import nc.impl.bd.account.assign.AccountOptBatchAssign;
import nc.impl.bd.account.batchupdate.AccountOptBatchUpdate;
import nc.impl.bd.account.busilog.AccBizLogWriter;
import nc.impl.bd.account.cancleassign.AccountOptBatchCancleAssign;
import nc.impl.bd.account.cancleassign.AccountOptCancleAssign;
import nc.impl.bd.account.distribute.AccDistEditStatuService;
import nc.impl.bd.account.distribute.AccDistEditableChecker;
import nc.impl.bd.account.middlelev.AccCodeDefaultAdjuster;
import nc.impl.bd.account.middlelev.AccountOptInsertMiddleLev;
import nc.impl.bd.account.middlelev.IAccCodeAdjuster;
import nc.impl.bd.account.switcher.AccountOptSwitch;
import nc.impl.bd.account.updateoperate.AccountOptBatchUpadateAss;
import nc.impl.bd.account.updateoperate.AccountOptUpdate;
import nc.impl.bd.account.util.AccountBGUtil;
import nc.itf.bd.account.IAccountService;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.bd.accchart.AccChartVO;
import nc.vo.bd.accctrlrule.AccCtrlRuleVO;
import nc.vo.bd.account.AccAssVO;
import nc.vo.bd.account.AccountVO;
import nc.vo.bd.errorlog.ErrLogReturnValue;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.para.SysInitVO;

import org.apache.commons.lang.ArrayUtils;


/**
 * 会计科目档案的保存类,uapbd模块
 * 补丁需要部署在uapbd模块下，WEB-INF下
 */

public class AccountServiceImpl implements IAccountService {
	private AccountQryServiceImpl accountQry;

	IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
	IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();

	public AccountVO delete(AccountVO vo) throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { "DELETE",
				vo });
		
		AccountVO[] result = new AccountVO[]{vo};
		try {
			masterDataDisposeProcess.accchartDispose(result, Operations.DELETE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("同步主数据基础数据【会计科目】出错:" + e.getMessage());
		}

		AccountVO resultvo = (new AccountOptDelete(vo))
				.deleteAccountWithHistory();
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		return resultvo;
	}

	public AccountVO[] insert(AccountVO vo) throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { vo });
		AccountVO[] result = (new AccountOptInsert(vo)).insertAccountHistory();
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		try {
			masterDataDisposeProcess.accchartDispose(result, Operations.CREATE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据基础数据【会计科目】出错:" + e.getMessage());
		}
		return result;
	}

	public AccountVO[] update(AccountVO vo) throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { vo });
		AccountVO[] result = (new AccountOptUpdate(vo)).updateAccountHistory();
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		try {
			masterDataDisposeProcess.accchartDispose(result, Operations.UPDATE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据基础数据【会计科目】出错:" + e.getMessage());
		}
		return result;
	}

	public AccountVO[] switchFlag(AccountVO vo, String attr)
			throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { vo });
		AccountVO[] result = (new AccountOptSwitch(vo)).switchFlag(attr);
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		return result;
	}

	public AccountQryServiceImpl getAccountQry() {
		if (this.accountQry == null)
			this.accountQry = new AccountQryServiceImpl();
		return this.accountQry;
	}

	public AccountVO[] enableAccount(AccountVO vo, boolean isLower)
			throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { vo });
		AccountVO[] result = (new AccountOptSwitch(vo))
				.enableOrDisableAccount(isLower);
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		try {
			masterDataDisposeProcess.accchartDispose(result, Operations.UPDATE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据基础数据【会计科目】出错:" + e.getMessage());
		}
		return result;
	}

	public AccountVO[] disableAccount(AccountVO vo, boolean isLower)
			throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, new Object[] { vo });
		AccountVO[] result = (new AccountOptSwitch(vo))
				.enableOrDisableAccount(isLower);
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, new Object[] { vo });
		try {
			masterDataDisposeProcess.accchartDispose(result, Operations.UPDATE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据基础数据【会计科目】出错:" + e.getMessage());
		}
		return result;
	}

	public AccountVO[] batchUpdateAccAss(String pk_accchart,
			String[] pk_accasoas, AccAssVO[] assVOs) throws BusinessException {
		AccDistEditableChecker.check(AccChartVO.class,
				new Object[] { pk_accchart });
		AccountVO[] result = (new AccountOptBatchUpadateAss())
				.batchUpdateAccAss(pk_accchart, pk_accasoas, assVOs);
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccChartVO.class,
				new Object[] { pk_accchart });
		return result;
	}

	public ErrLogReturnValue assignAccount(String pk_accctrlrule,
			String[] pk_accounts) throws BusinessException {
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrule, false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		ErrLogReturnValue result = (new AccountOptAssign(pk_accctrlrule))
				.assign(pk_accounts, (parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		return result;
	}

	public ErrLogReturnValue assignAccountWithHistory(String pk_accchart,
			String pk_accctrlrule, String[] pk_accounts)
			throws BusinessException {
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrule, false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		ErrLogReturnValue result = (new AccountOptAssign(pk_accctrlrule))
				.assignWithHistory(pk_accchart, pk_accounts,
						(parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		return result;
	}

	public ErrLogReturnValue batchAssignAccount(String pk_accchart,
			String[] pk_accctrlrules, String[] pk_accounts)
			throws BusinessException {
		if (ArrayUtils.isEmpty((Object[]) pk_accctrlrules))
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("10140accb", "010140accb0429"));
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrules[0], false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class,
				(Object[]) pk_accctrlrules);
		ErrLogReturnValue result = (new AccountOptBatchAssign(pk_accctrlrules,
				pk_accchart)).assignWithHistory(pk_accounts,
				(parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				(Object[]) pk_accctrlrules);
		return result;
	}

	public ErrLogReturnValue cancleAssignAccount(String pk_accctrlrule,
			String[] pk_accounts) throws BusinessException {
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrule, false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class, new Object[] {
				"DELETE", pk_accctrlrule });
		ErrLogReturnValue result = (new AccountOptCancleAssign(pk_accctrlrule))
				.cancleAssign(pk_accounts, (parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		return result;
	}

	public ErrLogReturnValue cancleAssignAccountWithHistory(String pk_accchart,
			String pk_accctrlrule, String[] pk_accounts)
			throws BusinessException {
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrule, false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class, new Object[] {
				"DELETE", pk_accctrlrule });
		ErrLogReturnValue result = (new AccountOptCancleAssign(pk_accctrlrule))
				.cancleAssignWithHistory(pk_accchart, pk_accounts,
						(parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				new Object[] { pk_accctrlrule });
		return result;
	}

	public ErrLogReturnValue batchCancleAssignAccount(String pk_accchart,
			String[] pk_accctrlrules, String[] pk_accounts)
			throws BusinessException {
		if (ArrayUtils.isEmpty((Object[]) pk_accctrlrules))
			throw new BusinessException(NCLangResOnserver.getInstance()
					.getStrByID("10140accb", "010140accb0429"));
		int parentChartCtrlLevel = AccountBGUtil.getParentChartCtrlLevelByPk(
				pk_accctrlrules[0], false);
		AccDistEditableChecker.check(AccCtrlRuleVO.class,
				(Object[]) pk_accctrlrules);
		ErrLogReturnValue result = (new AccountOptBatchCancleAssign(
				pk_accctrlrules, pk_accchart)).cancleAssignWithHistory(
				pk_accounts, (parentChartCtrlLevel == -1));
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccCtrlRuleVO.class,
				(Object[]) pk_accctrlrules);
		return result;
	}

	public AccountVO[] insertMiddleLev(String pk_accasoa, String pk_accchart,
			String[] pk_accounts, String code, String[] names,
			boolean allowRepeat) throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, (Object[]) pk_accounts);
		AccountBGUtil.checkVersionAndLock(pk_accchart);
		AccountVO parentVO = getAccountQry().getAccountVOByPK(pk_accasoa);
		if (parentVO == null)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("10140accb", "010140accb0216"));
		AccountVO[] result = (new AccountOptInsertMiddleLev(parentVO,
				(IAccCodeAdjuster) new AccCodeDefaultAdjuster(), allowRepeat))
				.insertMiddleLev(pk_accounts, code, names);
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, (Object[]) pk_accounts);
		return result;
	}

	public ErrLogReturnValue batchUpdate(String attr,
			Map<String, Object> attr_valueMap, String[] permissionOrgs,
			String[] selectedOrgs, String[] selectedPKs, boolean isNeedReturnVOs)
			throws BusinessException {
		AccDistEditableChecker.check(AccountVO.class, (Object[]) selectedPKs);
		ErrLogReturnValue returnValue = (new AccountOptBatchUpdate(
				selectedOrgs, selectedPKs, attr, attr_valueMap.get(attr)))
				.batchUpdate();
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccountVO.class, (Object[]) selectedPKs);
		return returnValue;
	}

	public ErrLogReturnValue batchUpdate(String attr,
			Map<String, Object> attr_valueMap, String[] permissionOrgs,
			String[] selectedOrgs, String condition) throws BusinessException {
		AccDistEditableChecker.check(AccChartVO.class, (Object[]) selectedOrgs);
		ErrLogReturnValue returnValue = (new AccountOptBatchUpdate(
				selectedOrgs, condition, attr, attr_valueMap.get(attr)))
				.batchUpdate();
		AccBizLogWriter.getInstance().flush();
		AccDistEditStatuService.update(AccChartVO.class,
				(Object[]) selectedOrgs);
		return returnValue;
	}

	public ValueObjWithErrLog enableAccounts(AccountVO[] accvos, boolean isLower)
			throws BusinessException {
		ValueObjWithErrLog log = new ValueObjWithErrLog();
		AccountVO[] returnVos = new AccountVO[0];
		for (AccountVO accountVO : accvos) {
			try {
				returnVos = (AccountVO[]) ArrayUtils.addAll(
						(Object[]) returnVos,
						(Object[]) enableAccount(accountVO, isLower));
			} catch (Exception e) {
				log.addErrLogMessage((SuperVO) accountVO, e.getMessage());
			}
		}
		log.setVos((SuperVO[]) returnVos);
		return log;
	}

	public ValueObjWithErrLog disableAccounts(AccountVO[] accvos,
			boolean isLower) throws BusinessException {
		ValueObjWithErrLog log = new ValueObjWithErrLog();
		AccountVO[] returnVos = new AccountVO[0];
		for (AccountVO accountVO : accvos) {
			try {
				returnVos = (AccountVO[]) ArrayUtils.addAll(
						(Object[]) returnVos,
						(Object[]) disableAccount(accountVO, isLower));
			} catch (Exception e) {
				log.addErrLogMessage((SuperVO) accountVO, e.getMessage());
			}
		}
		log.setVos((SuperVO[]) returnVos);
		return log;
	}

}

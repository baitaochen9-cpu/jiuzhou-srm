package nc.impl.bd.bankacc.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import nc.vo.pubapp.pattern.data.ValueUtils;
import nc.bs.bd.bankacc.cust.CustSupBankaccGlobeConst;
import nc.bs.bd.bankacc.validator.BankaccIbanValidator;
import nc.bs.bd.bankacc.validator.CustSupBankaccAutoEnableValidation;
import nc.bs.bd.bankacc.validator.PsnAndCustSubaccCurrtypeUniqueValidation;
import nc.bs.bd.baseservice.BaseService;
import nc.bs.bd.baseservice.busilog.IBusiOperateConst;
import nc.bs.bd.baseservice.md.VOArrayUtil;
import nc.bs.bd.cache.CacheProxy;
import nc.bs.businessevent.EventDispatcher;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.businessevent.bd.BDCommonEventUtil;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.ncc.mdm.common.Operations;
import nc.bs.ncc.mdm.masterdata.IMasterDataTranslateProcess;
import nc.bs.ncc.mdm.masterdata.IMsaterDataDisposeProcess;
import nc.bs.ncc.mdm.util.MasterDataDisposeProcessImpl;
import nc.bs.ncc.mdm.util.MasterDataTranslateProcessImpl;
import nc.bs.ncc.mdm.util.MdmStringUtils;
import nc.bs.ncc.mdm.vo.MdmCustomerSubVO;
import nc.bs.ncc.mdm.vo.MdmCustomerVO;
import nc.bs.ncc.mdm.vo.MdmSupplierSubVO;
import nc.bs.ncc.mdm.vo.MdmSupplierVO;
import nc.bs.sec.esapi.NCESAPI;
import nc.bs.uif2.validation.IValidationService;
import nc.bs.uif2.validation.ValidationException;
import nc.bs.uif2.validation.ValidationFailure;
import nc.bs.uif2.validation.ValidationFrameworkUtil;
import nc.bs.uif2.validation.Validator;
import nc.impl.bd.bankacc.base.BankAccBaseInfoQueryServiceImpl;
import nc.impl.pubapp.pattern.data.vo.VOQuery;
import nc.itf.bd.bankacc.cust.ICustBankaccService;
import nc.itf.bd.bankacc.cust.ICustSupBankaccShareService;
import nc.itf.bd.bankdoc.IBankdocService;
import nc.itf.bd.cust.baseinfo.ICustSupQueryService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.uap.busibean.ISysInitQry;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.MDBaseQueryFacade;
import nc.md.model.IBean;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.md.persist.framework.MDPersistenceService;
import nc.vo.bd.address.AddressVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccUseVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankaccount.IBankAccConstant;
import nc.vo.bd.bankaccount.cust.CustBankaccUnionVO;
import nc.vo.bd.bankdoc.RandomGenerator;
import nc.vo.bd.banktype.BankTypeVO;
import nc.vo.bd.cust.CustSupplierVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.pub.DistributedSubEntiryValidator;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.pub.SingleDistributedDeleteValidator;
import nc.vo.bd.pub.SingleDistributedUpdateValidator;
import nc.vo.bd.pub.sqlutil.BDSqlInUtil;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.para.SysInitVO;
import nc.vo.trade.sqlutil.IInSqlBatchCallBack;
import nc.vo.trade.sqlutil.InSqlBatchCaller;
import nc.vo.trade.voutils.VOUtil;
import nc.vo.util.AuditInfoUtil;
import nc.vo.util.BDAuditInfoUtil;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.BDReferenceChecker;
import nc.vo.util.BDUniqueRuleValidate;
import nc.vo.util.BDVersionValidationUtil;
import nc.vo.util.bizlock.BizlockDataUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nc.bs.ncc.mdm.common.EventOperationEnum;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.yonyou.apache.http.HttpEntity;
import com.yonyou.apache.http.client.config.RequestConfig;
import com.yonyou.apache.http.client.methods.CloseableHttpResponse;
import com.yonyou.apache.http.client.methods.HttpGet;
import com.yonyou.apache.http.client.methods.HttpPost;
import com.yonyou.apache.http.entity.ContentType;
import com.yonyou.apache.http.entity.StringEntity;
import com.yonyou.apache.http.impl.client.CloseableHttpClient;
import com.yonyou.apache.http.impl.client.HttpClients;
import com.yonyou.apache.http.util.EntityUtils;

/**
 *uapbd 模块，处理课上银行账户子表启用停用同步主数据 
 *补丁需要部署在upabd模块下web-inf/
 */

public class CustBankaccServiceImpl extends BaseService<CustbankVO> implements ICustBankaccService {

  public CustBankaccServiceImpl() {
    super(IBDMetaDataIDConst.CUSTBANKACCOUNT);
  }

  private BaseDAO dao = new BaseDAO();

  private IBankdocService bankdocService = null;

  private ICustSupQueryService custSupQryService = null;
    
  IMasterDataTranslateProcess masterDataTranslateProcess = MasterDataTranslateProcessImpl.getInstance();
  IMsaterDataDisposeProcess masterDataDisposeProcess = MasterDataDisposeProcessImpl.getInstance();
  
  @Override
  public void deleteCustBankacc(CustBankaccUnionVO deleteVO) throws BusinessException {
    CustbankVO custbankVO = deleteVO.getCustbankVO();
    BankAccbasVO bankaccbasVO = deleteVO.getBankaccbasVO();
    // 主键锁
    BDPKLockUtil.lockString(custbankVO.PK_CUST);
    BDPKLockUtil.lockSuperVO(bankaccbasVO);
    // 版本校验
    BDVersionValidationUtil.validateSuperVO(custbankVO);
    BDVersionValidationUtil.validateSuperVO(bankaccbasVO);
    checkCustBankForDist(deleteVO);
    // 后台逻辑校验(引用校验)
    Validator refCheckValidator = BDReferenceChecker.getInstance();
    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(refCheckValidator);
    vService.validate(bankaccbasVO.getBankaccsub());
    vService =
        ValidationFrameworkUtil.createValidationService(new SingleDistributedDeleteValidator());
    vService.validate(deleteVO.getCustbankVO());

    // 删除前事件
    fireBeforeDeleteEvent(deleteVO);

    // 库操作
    String where =
        CustbankVO.PK_CUST + " = '" + NCESAPI.sqlEncode(custbankVO.getPk_cust()) + "' and "
            + CustbankVO.PK_BANKACCBAS + " = '" + NCESAPI.sqlEncode(custbankVO.getPk_bankaccbas())
            + "'";
    dao.deleteByClause(CustbankVO.class, where);

    bankaccbasVO.setStatus(VOStatus.DELETED);
    getMDService().deleteBillFromDB(bankaccbasVO);
    // 缓存通知
    CacheProxy.fireDataDeleted(custbankVO.getTableName(), custbankVO.getPrimaryKey());
    CacheProxy.fireDataDeleted(bankaccbasVO.getTableName(), bankaccbasVO.getPrimaryKey());
    BankAccSubVO[] bankaccsubs = bankaccbasVO.getBankaccsub();
    List<String> pk_bankaccsub = VOUtil.extractFieldValues(bankaccsubs, "pk_bankaccsub", null);

    CacheProxy.fireDataDeletedBatch(BankAccSubVO.getDefaultTableName(),
        pk_bankaccsub.toArray(new String[0]));
    for (BankAccSubVO subvo : bankaccsubs) {
      BankAccUseVO[] usevo = subvo.getSubaccuse();
      if (usevo == null || usevo.length == 0)
        continue;
      List<String> pk_bankaccuse = VOUtil.extractFieldValues(usevo, "pk_bankaccuse", null);
      CacheProxy.fireDataDeletedBatch(BankAccSubVO.getDefaultTableName(),
          pk_bankaccuse.toArray(new String[0]));

    }

    // 增加业务日志
    CustbankaccLogUtil util = new CustbankaccLogUtil(IBDMetaDataIDConst.CUSTBANKACC, custbankVO);
    util.writeModefyBusiLog(IBusiOperateConst.DELETE, bankaccbasVO, bankaccbasVO);

    fireAfterDeleteEvent(deleteVO);
  }

  @Override
  public void deleteCustBankacc(CustBankaccUnionVO[] deleteVO) throws BusinessException {
    if (ArrayUtils.isEmpty(deleteVO))
      return;
    for (CustBankaccUnionVO unionVO : deleteVO) {
      deleteCustBankacc(unionVO);
    }
  }

  private void checkCustBankForDist(CustBankaccUnionVO vo) throws BusinessException {
    CustbankVO custbankvo = vo.getCustbankVO();
    if (1 == custbankvo.getAccclass()) {
      DistributedSubEntiryValidator validator = new DistributedSubEntiryValidator<CustomerVO>();
      String pk_customer = custbankvo.getPk_cust();
      validator.distributedEditCheck(IBDMetaDataIDConst.CUSTOMER,
          IBDMetaDataIDConst.CUSTBANKACCOUNT, pk_customer);
    }
    else {
      DistributedSubEntiryValidator validator = new DistributedSubEntiryValidator<SupplierVO>();
      String pk_supplier = custbankvo.getPk_cust();
      validator.distributedEditCheck(IBDMetaDataIDConst.SUPPLIER,
          IBDMetaDataIDConst.SUPPLIERBANKACCOUNT, pk_supplier);
    }
  }

  private void checkCustbankNum(BankAccbasVO vo, BankAccbasVO oldvo) throws BusinessException {
    CustBankNumEditValidation validator = new CustBankNumEditValidation();
    validator.validate(vo, oldvo);
  }

  @Override
  public CustBankaccUnionVO insertCustBankacc(CustBankaccUnionVO insertVO) throws BusinessException {
    CustbankVO custbankVO = insertVO.getCustbankVO();
    BankAccbasVO bankaccbasVO = insertVO.getBankaccbasVO();
    // 加锁
    IBean bean =
        MDBaseQueryFacade.getInstance().getBeanByFullClassName(bankaccbasVO.getClass().getName());
    BizlockDataUtil.lockDataByBizlock(
        new CustBankAccBizLockDataProvider(bean, custbankVO.getPk_cust()), bankaccbasVO);

    // 处理银行档案,欠银行档案的编码的生成
    if (bankaccbasVO != null && !StringUtils.isBlank(bankaccbasVO.getPk_banktype())
        && !StringUtils.isBlank(bankaccbasVO.getName())
        && StringUtils.isBlank(bankaccbasVO.getPk_bankdoc())) {

      // 根据pk_cust查出对应pk_org
      CustSupplierVO tempCustSupVO = queryCustSupByPk(custbankVO.getPk_cust());
      String pk_org = tempCustSupVO.getPk_org();
      String pk_group = tempCustSupVO.getPk_group();

      String pk_bankdoc = processBankdoc(bankaccbasVO, pk_org, pk_group);
      bankaccbasVO.setPk_bankdoc(pk_bankdoc);
    }
    checkCustBankForDist(insertVO);
    insertValidator(insertVO);
    // 如果为客商，进行客商范围内银行账户唯一性校验
    if (isCustSupplier(custbankVO.getPk_cust(), custbankVO.getAccclass())) {
      custSupBankAccUniqueValidator(insertVO);
    }
    // 审计信息
    BDAuditInfoUtil.addData(bankaccbasVO);
    // 库操作

    // 处理银行账户
    bankaccbasVO.setStatus(VOStatus.NEW);
    bankaccbasVO.setEnablestate(IPubEnumConst.ENABLESTATE_INIT);
    // 处理银行账户子户
    BankAccSubVO[] bankaccsubvos = bankaccbasVO.getBankaccsub();
    String accnum = bankaccbasVO.getAccnum();
    String accname = bankaccbasVO.getAccname();
    bankaccbasVO.setName(accname);
    boolean isHasDefault = false;
    String pk_currtype = null;
    for (BankAccSubVO vo : bankaccsubvos) {
      vo.setAccnum(accnum);
      vo.setAccname(accname);
      if (vo.getIsdefault() != null && vo.getIsdefault().booleanValue()) {
        isHasDefault = true;
        pk_currtype = vo.getPk_currtype();
      }
    }
    String pk_bankaccbas = getMDService().saveBill(bankaccbasVO);
    BankAccbasVO returnBankaccbasVO =
        getMDQryService().queryBillOfVOByPK(BankAccbasVO.class, pk_bankaccbas, false);

    custbankVO.setPk_bankaccbas(pk_bankaccbas);
    // String pk_custbank = dao.insertVO(custbankVO);
    // CustbankVO returnCustbankVO =
    // (CustbankVO)dao.retrieveByPK(CustbankVO.class, pk_custbank);
    BankAccSubVO[] accsubVOs = returnBankaccbasVO.getBankaccsub();
    List<CustbankVO> list = new ArrayList<CustbankVO>();
    if (accsubVOs != null) {
      for (BankAccSubVO subVO : accsubVOs) {
        CustbankVO cloneVO = (CustbankVO) custbankVO.clone();
        cloneVO.setPrimaryKey(null);
        cloneVO.setPk_bankaccsub(subVO.getPrimaryKey());
        cloneVO.setPk_oldcustbank(subVO.getPrimaryKey());
        cloneVO.setStatus(VOStatus.NEW);
        list.add(cloneVO);
      }
    }
    String[] pks = dao.insertVOArray(list.toArray(new CustbankVO[0]));
    // 处理客商银行账户从申请单审批时子表是否默认保存不上的问题
    if (isHasDefault) {
      if (!StringUtils.isEmpty(pk_currtype)) {
        BankAccSubVO[] newBankaccsubVos = returnBankaccbasVO.getBankaccsub();
        for (BankAccSubVO vo : newBankaccsubVos) {
          if (vo.getPk_currtype().equals(pk_currtype)) {
            String sql =
                "update " + CustbankVO.getDefaultTableName() + " set " + CustbankVO.ISDEFAULT
                    + "='Y',pk_oldcustbank='" + vo.getPk_bankaccsub() + "' where "
                    + CustbankVO.PK_CUST + "='" + insertVO.getPk_cust() + "' and "
                    + CustbankVO.PK_BANKACCSUB + "='" + vo.getPk_bankaccsub() + "'";
            dao.executeUpdate(sql);
            break;
          }
        }
      }
    }

    custbankVO = (CustbankVO) dao.retrieveByPK(CustbankVO.class, pks[0]);
    custbankVO.setPk_bankaccsub(null);

    // 缓存通知
    CacheProxy.fireDataInserted(bankaccbasVO.getTableName(), pk_bankaccbas);
    CacheProxy.fireDataInserted(BankAccSubVO.getDefaultTableName(), null);
    CacheProxy.fireDataInserted(custbankVO.getTableName(), null);
    // 构造返回值(返回保存的时候需要的值，设置默认子户的值)

    String pk_custbank = custbankVO.getPk_custbank();
    String pk_cust = insertVO.getPk_cust();
    CustBankaccUnionVO returnVO = new CustBankaccUnionVO();
    returnVO = queryCustBankaccUnionVO(pk_custbank, pk_bankaccbas, pk_cust);
    if (isCustSupplier(custbankVO.getPk_cust(), custbankVO.getAccclass())) {
      getCustSupBankaccShareService().shareInsertCustSupbankaccs(returnVO);
    }
    // 增加业务日志
    String metaId = IBDMetaDataIDConst.CUSTBANKACC;
    CustbankaccLogUtil util = new CustbankaccLogUtil(metaId, returnVO.getCustbankVO());
    util.writeBusiLog(IBusiOperateConst.ADD, null, returnVO.getBankaccbasVO());

    fireAfterInsertEvent(returnVO);
    return returnVO;
  }

  /**
   * <p>
   * 说明：判断客户是否客商
   * <li></li>
   * </p>
   * 
   * @param custbankVO
   * @return
   * @throws BusinessException
   * @date 2014年10月16日 下午8:22:18
   * @since NC6.36
   */
  private boolean isCustSupplier(String pk_cust, int accclass) throws BusinessException {
    boolean iscustsup = false;

    // 如果为客户，那么查询是否为供应商
    if (accclass == IBankAccConstant.ACCCLASS_CUST) {
      SupplierVO supplier = (SupplierVO) dao.retrieveByPK(SupplierVO.class, pk_cust);
      if (supplier != null) {
        if (supplier.getIscustomer() == null) {
          iscustsup = false;
        }
        else {
          iscustsup = supplier.getIscustomer().booleanValue();
        }
      }
    }
    if (accclass == IBankAccConstant.ACCCLASS_SUPPLIER) {
      // 如果为供应商，查询是否为客户
      CustomerVO customer = (CustomerVO) dao.retrieveByPK(CustomerVO.class, pk_cust);
      if (customer != null)
        if (customer.getIssupplier() == null) {
          iscustsup = false;
        }
        else {
          iscustsup = customer.getIssupplier().booleanValue();
        }
    }
    return iscustsup;
  }

  private CustBankaccUnionVO queryCustBankaccUnionVO(String pk_custbank, String pk_bankaccbas,
      String pk_cust) throws BusinessException {
    if (StringUtil.isEmptyWithTrim(pk_bankaccbas) || StringUtil.isEmptyWithTrim(pk_custbank))
      return null;
    BankAccbasVO bankAccbasVO =
        getMDQryService().queryBillOfVOByPK(BankAccbasVO.class, NCESAPI.sqlEncode(pk_bankaccbas),
            false);
    if (bankAccbasVO == null)
      return null;
    Map<String, UFBoolean> isDefaultMap = new HashMap<String, UFBoolean>();
    CustbankVO custBankVO =
        (CustbankVO) dao.retrieveByPK(CustbankVO.class, NCESAPI.sqlEncode(pk_custbank));
    Collection<CustbankVO> col =
        dao.retrieveByClause(CustbankVO.class,
            CustbankVO.PK_CUST + " = '" + NCESAPI.sqlEncode(pk_cust) + "'");
    if (col != null && col.size() > 0) {
      for (CustbankVO custbankvo : col) {
        isDefaultMap.put(custbankvo.getPk_bankaccsub(), custbankvo.getIsdefault());

      }
    }
    BankAccSubVO[] bankaccsubvos = bankAccbasVO.getBankaccsub();
    for (BankAccSubVO subvo : bankaccsubvos) {
      subvo.setIsdefault(isDefaultMap.get(subvo.getPk_bankaccsub()));

    }
    bankAccbasVO.setBankaccsub(bankaccsubvos);
    CustBankaccUnionVO unionVO = new CustBankaccUnionVO();
    unionVO.setBankaccbasVO(bankAccbasVO);
    unionVO.setCustbankVO(custBankVO);
    return unionVO;
  }

  protected void fireAfterInsertEvent(CustBankaccUnionVO vo) throws BusinessException {
    BDCommonEventUtil custbankacc = new BDCommonEventUtil(IBDMetaDataIDConst.CUSTBANKACC);
    custbankacc.dispatchInsertAfterEvent(vo);
  }

  private CustSupplierVO queryCustSupByPk(String pk_cust) throws BusinessException {
    return getCustSupQueryService().queryCustSupVOByPk(pk_cust);
  }

  /**
   * <p>
   * 说明：客商范围内银行账号是否唯一检验
   * <li></li>
   * </p>
   * 
   * @return
   * @throws BusinessException
   * @date 2014年10月16日 下午8:50:54
   * @since NC6.5
   */
  private void custSupBankAccUniqueValidator(CustBankaccUnionVO insertVO) throws BusinessException {
    CustSupBankAccUniqueValidation custsupUniqueValidator = new CustSupBankAccUniqueValidation();
    ValidationFailure faliure = custsupUniqueValidator.validate(insertVO);
    if (faliure != null) {
      throw new BusinessException(faliure.getMessage());
      /** 银行账户在客商范围内重复 **/
    }
  }

  private void insertValidator(CustBankaccUnionVO insertVO) throws ValidationException {
    // 后台逻辑校验(非空校验、唯一性校验)
    CustBankRefPkExsitValidation refValidator = new CustBankRefPkExsitValidation();
    ValidationFailure faliure =
        refValidator.validate(insertVO.getCustbankVO(), insertVO.getCustbankVO().getAccclass());
    List<ValidationFailure> failurelist = new ArrayList<ValidationFailure>();
    if (faliure != null) {
      failurelist.add(faliure);
      throw new nc.bs.uif2.validation.ValidationException(failurelist);
    }

    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(getInsertValidator());
    vService.validate(insertVO);

    IValidationService vaService = ValidationFrameworkUtil.createValidationService(new Validator[] {
        new BankaccIbanValidator()
    });
    vaService.validate(insertVO.getBankaccbasVO());
  }

  @Override
  protected Validator[] getInsertValidator() {
    CustBankAccountNullValidation nullValidation = new CustBankAccountNullValidation();
    CustBankaccsubValidation accsubValidation = new CustBankaccsubValidation();
    CustBankAccountUniqueValidation uniqueValidation = new CustBankAccountUniqueValidation();
    PsnAndCustSubaccCurrtypeUniqueValidation accsubUniqueValidation =
        new PsnAndCustSubaccCurrtypeUniqueValidation();
    CustSupIsInnerAccountValidation innerCustsupValidation = new CustSupIsInnerAccountValidation();
    return new Validator[] {
        nullValidation, accsubValidation, uniqueValidation, accsubUniqueValidation,
        innerCustsupValidation
    };
  }

  private Validator[] getEnableValidator() {
    // SingleDistributedUpdateValidator distributeValidator=new
    // SingleDistributedUpdateValidator();
    CustBankAduitValidation auditValidation = new CustBankAduitValidation();
    CustSupBankaccAutoEnableValidation autoEnableValidation =
        new CustSupBankaccAutoEnableValidation();
    return new Validator[] {
        auditValidation, autoEnableValidation
    };
  }

  private String processBankdoc(BankAccbasVO bankaccbasvo, String pk_org, String pk_group)
      throws BusinessException {

    String pk_bankdoc = bankaccbasvo.getPk_bankdoc();
    String bankdocname = bankaccbasvo.getName();
    boolean inputDir = CustSupBankaccGlobeConst.isCustSupBankdocInputDir();

    if (StringUtils.isBlank(pk_bankdoc)) {
      if (inputDir) {
        pk_bankdoc =
            getBankdocService().addNewBankdocWithPkOrg(bankaccbasvo.getPk_banktype(), bankdocname,
                pk_org, pk_group);
      }
      else {
        throw new BusinessException(NCLangResOnserver.getInstance().getStrByID("10140cba",
            "110140cba0045", null, new String[] {
                bankaccbasvo.getAccnum(), bankaccbasvo.getAccname()
            })/*
               * "客商银行帐户 账号:[" + bankaccbasvo .getAccnum()] + "  户名：[" +
               * bankaccbasvo .getAccname() + " ]\n开户银行不存在银行档案，请核对后再进行数据导入"
               */);

      }
    }
    return pk_bankdoc;

  }

  private String getBankdocCode(String pk_banktype) throws BusinessException {
    BankTypeVO banktypevo =
        (BankTypeVO) dao.retrieveByPK(BankTypeVO.class, NCESAPI.sqlEncode(pk_banktype));
    String banktypeCode = banktypevo.getCode();
    StringBuilder bankdocCode = new StringBuilder();
    if (banktypeCode != null) {
      // 银行类别编码前4位
      if (banktypeCode.trim().length() >= 4) {
        bankdocCode.append(banktypeCode.substring(1, 4));
      }
      else {
        int len = banktypeCode.trim().length();
        bankdocCode.append(banktypeCode.trim());
        for (int i = len + 1; i < 5; i++) {
          bankdocCode.append("0");
        }
      }
      // 中间4为为"auto"
      bankdocCode.append("auto");
      // 自动生成8位随机串
      boolean hasValidCode = false;
      while (!hasValidCode) {
        String[] randomStrs = RandomGenerator.generateRandomStrings();
        for (String randomStr : randomStrs) {
          if (isRepeat(randomStr)) {
            continue;
          }
          else {
            hasValidCode = true;
            bankdocCode.append(randomStr);
            return bankdocCode.toString();
          }
        }
      }
    }
    return null;

  }

  private boolean isRepeat(String randomStr) {
    String where =
        "select count(*) from bd_bankdoc where code='" + NCESAPI.sqlEncode(randomStr) + "'";
    try {
      Integer num = (Integer) dao.executeQuery(where, new ColumnProcessor());
      if (num == 0) {
        return false;
      }
      else {
        return true;
      }
    }
    catch (BusinessException e) {
      Logger.error(e.getMessage());
    }
    return false;
  }

  @Override
  public CustBankaccUnionVO[] insertCustBankaccs(CustBankaccUnionVO[] insertVOs)
      throws BusinessException {
    // TODO 暂时先这样实现，需要改成批量实现
    if (ArrayUtils.isEmpty(insertVOs))
      return null;
    List<CustBankaccUnionVO> list = new ArrayList<CustBankaccUnionVO>();
    CustBankaccUnionVO newVO = null;
    for (CustBankaccUnionVO vo : insertVOs) {
      newVO = insertCustBankacc(vo);
      list.add(newVO);
    }
    return list.toArray(new CustBankaccUnionVO[0]);
  }

  @Override
  public CustBankaccUnionVO[] updateCustBankaccs(CustBankaccUnionVO[] updateVOs)
      throws BusinessException {
    // TODO 暂时实现方式，可改成批量实现
    if (ArrayUtils.isEmpty(updateVOs))
      return null;
    List<CustBankaccUnionVO> list = new ArrayList<CustBankaccUnionVO>();
    CustBankaccUnionVO updateVO = null;
    for (CustBankaccUnionVO vo : updateVOs) {
      updateVO = updateCustBankacc(vo);
      list.add(updateVO);
    }
    return list.toArray(new CustBankaccUnionVO[0]);
  }

  private BaseDAO getBaseDao() {
    return new BaseDAO();
  }

  @SuppressWarnings("unchecked")
  @Override
  public CustBankaccUnionVO updateCustBankacc(CustBankaccUnionVO updateVO) throws BusinessException {
    CustbankVO custbankVO = updateVO.getCustbankVO();
    BankAccbasVO bankaccbasVO = updateVO.getBankaccbasVO();

    // 提供修改默认币种事件通知
    String pk_cust = NCESAPI.sqlEncode(custbankVO.getPk_cust());
    Collection<CustbankVO> oldDefaultBankCol =
        getBaseDao().retrieveByClause(CustbankVO.class,
            " pk_cust='" + pk_cust + "' and isdefault='Y'");
    CustbankVO oldDefaultBankvo = null;
    if (oldDefaultBankCol != null && oldDefaultBankCol.size() > 0) {
      oldDefaultBankvo = new CustbankVO();
      oldDefaultBankvo = oldDefaultBankCol.toArray(new CustbankVO[0])[0];
    }

    // 主键锁
    BDPKLockUtil.lockString(custbankVO.getPk_cust());
    BDPKLockUtil.lockSuperVO(bankaccbasVO);
    // 版本校验
    BDVersionValidationUtil.validateSuperVO(bankaccbasVO);
    checkCustBankForDist(updateVO);
    // 如果为客商，进行客商范围内银行账户唯一性校验
    if (isCustSupplier(custbankVO.getPk_cust(), custbankVO.getAccclass().intValue())) {
      custSupBankAccUniqueValidator(updateVO);
    }
    // 处理银行档案信息
    if (bankaccbasVO != null && !StringUtils.isBlank(bankaccbasVO.getPk_banktype())
        && !StringUtils.isBlank(bankaccbasVO.getName())) {
      // 根据pk_cust查出对应pk_org
      CustSupplierVO tempCustSupVO = queryCustSupByPk(custbankVO.getPk_cust());
      String pk_org = tempCustSupVO.getPk_org();
      String pk_group = tempCustSupVO.getPk_group();

      String pk_bankdoc = processBankdoc(bankaccbasVO, pk_org, pk_group);

      bankaccbasVO.setPk_bankdoc(pk_bankdoc);
    }
    // 处理银行账户上面的账号和账户和子户上面的账号和账户同步的问题
    BankAccbasVO oldVO =
        getMDQryService()
            .queryBillOfVOByPK(BankAccbasVO.class, bankaccbasVO.getPrimaryKey(), false);
    updateValidator(updateVO);
    checkCustbankNum(updateVO.getBankaccbasVO(), oldVO);
    // 审计信息
    BDAuditInfoUtil.updateData(bankaccbasVO);
    // 库操作

    // 处理银行账户信息
    bankaccbasVO.setName("");
    bankaccbasVO.setStatus(VOStatus.UPDATED);

    CustBankaccUnionVO oldvo = new CustBankaccUnionVO();
    CustbankVO oldCustbankvo =
        getMDQryService().queryBillOfVOByPK(CustbankVO.class, custbankVO.getPk_custbank(), false);
    oldvo.setBankaccbasVO(oldVO);
    oldvo.setCustbankVO(oldCustbankvo);
    String accnum_old = oldVO.getAccnum();
    String accname_old = oldVO.getAccname();
    String accnum_new = bankaccbasVO.getAccnum();
    String accname_new = bankaccbasVO.getAccname();
    if (!accnum_old.equals(accnum_new) || !accname_old.equals(accname_new)) {
      BankAccSubVO[] accsubvos_new = bankaccbasVO.getBankaccsub();
      BankAccSubVO[] accsubvos_old = oldVO.getBankaccsub();
      List<BankAccSubVO> accsubvos_final = new ArrayList<BankAccSubVO>();
      if (accsubvos_new != null && accname_new.length() > 0) {
        for (int i = 0; i < accsubvos_new.length; i++) {
          if (accsubvos_new[i].getStatus() != VOStatus.DELETED) {
            accsubvos_new[i].setAccnum(accnum_new);
            accsubvos_new[i].setAccname(accname_new);
            accsubvos_final.add(accsubvos_new[i]);

          }

        }
      }
      List<String> pk_bankaccsubs_new = new ArrayList<String>();
      if (accsubvos_new != null && accsubvos_new.length > 0)
        pk_bankaccsubs_new =
            VOUtil.extractFieldValues(accsubvos_new, BankAccSubVO.PK_BANKACCSUB, null);
      for (int i = 0; i < accsubvos_old.length; i++) {
        if (!pk_bankaccsubs_new.contains(accsubvos_old[i].getPk_bankaccsub())) {
          accsubvos_old[i].setAccnum(accnum_new);
          accsubvos_old[i].setAccname(accname_new);
          accsubvos_old[i].setStatus(VOStatus.UPDATED);
          accsubvos_final.add(accsubvos_old[i]);

        }
      }
      bankaccbasVO.setBankaccsub(accsubvos_final.toArray(new BankAccSubVO[0]));
    }
    else {
      BankAccSubVO[] accsubvos_new = bankaccbasVO.getBankaccsub();
      if (accsubvos_new != null && accsubvos_new.length > 0) {
        for (BankAccSubVO vo : accsubvos_new) {
          if (vo.getStatus() != VOStatus.DELETED) {
            vo.setAccnum(accnum_old);
            vo.setAccname(accname_old);

          }

        }
      }

    }
    // 存储银行账户的信息
    getMDService().saveBillWithRealDelete(bankaccbasVO);
    BankAccbasVO returnBankaccbasVO =
        getMDQryService()
            .queryBillOfVOByPK(BankAccbasVO.class, bankaccbasVO.getPrimaryKey(), false);
    custbankVO.setPk_bankaccbas(returnBankaccbasVO.getPk_bankaccbas());
    // 同步关联表中数据
    String where =
        CustbankVO.PK_CUST + " = '" + NCESAPI.sqlEncode(custbankVO.getPk_cust()) + "' and "
            + CustbankVO.PK_BANKACCBAS + " = '"
            + NCESAPI.sqlEncode(returnBankaccbasVO.getPk_bankaccbas()) + "' and "
            + CustbankVO.PK_BANKACCSUB + " !='~' and " + CustbankVO.ACCCLASS + "="
            + custbankVO.getAccclass();

    Collection<CustbankVO> col = dao.retrieveByClause(CustbankVO.class, where);
    List<String> old_accsubPkList =
        VOUtil.extractFieldValues(col.toArray(new CustbankVO[0]), CustbankVO.PK_BANKACCSUB, null);
    BankAccSubVO[] accsubVOs = returnBankaccbasVO.getBankaccsub();
    List<String> new_accsubPkList =
        VOUtil.extractFieldValues(accsubVOs, BankAccSubVO.PK_BANKACCSUB, null);
    List<CustbankVO> addVOList = new ArrayList<CustbankVO>();
    List<CustbankVO> updateVOList = new ArrayList<CustbankVO>();
    List<CustbankVO> delVOList = new ArrayList<CustbankVO>();
    for (CustbankVO vo : col) {
      if (new_accsubPkList.contains(vo.getPk_bankaccsub())) {
        // vo.setIsdefault(custbankVO.getIsdefault());
        vo.setStatus(VOStatus.UPDATED);
        BDAuditInfoUtil.updateData(vo);
        updateVOList.add(vo);
      }
      else {
        vo.setStatus(VOStatus.DELETED);
        delVOList.add(vo);
      }
    }
    new_accsubPkList.removeAll(old_accsubPkList);
    for (String pk_bankaccsub : new_accsubPkList) {
      CustbankVO cloneVO = (CustbankVO) custbankVO.clone();
      cloneVO.setPrimaryKey(null);
      cloneVO.setIsdefault(null);
      cloneVO.setPk_bankaccsub(pk_bankaccsub);
      cloneVO.setStatus(VOStatus.NEW);
      AuditInfoUtil.addData(cloneVO);
      addVOList.add(cloneVO);
    }
    if (addVOList.size() > 0) {
      dao.insertVOList(addVOList);
      CacheProxy.fireDataInserted(CustbankVO.getDefaultTableName());
    }
    if (updateVOList.size() > 0) {
      dao.updateVOList(updateVOList);
      CacheProxy.fireDataUpdated(CustbankVO.getDefaultTableName());
    }
    if (delVOList.size() > 0) {
      dao.deleteVOList(delVOList);
      CacheProxy.fireDataDeleted(CustbankVO.getDefaultTableName(), null);
    }
    // 缓存通知
    CacheProxy.fireDataUpdated(BankAccbasVO.getDefaultTableName(), bankaccbasVO.getPrimaryKey());
    CacheProxy.fireDataUpdated(CustbankVO.getDefaultTableName(), custbankVO.getPrimaryKey());
    CacheProxy.fireDataUpdated(BankAccSubVO.getDefaultTableName(), null);
    // 构造返回值
    CustBankaccUnionVO returnVO = new CustBankaccUnionVO();
    returnVO.setBankaccbasVO(returnBankaccbasVO);
    CustbankVO returnCustbankVO =
        getMDQryService().queryBillOfVOByPK(CustbankVO.class, custbankVO.getPrimaryKey(), false);
    if (returnCustbankVO == null) {
      List<CustbankVO> returnList =
          (List<CustbankVO>) dao.retrieveByClause(CustbankVO.class, where);
      returnCustbankVO = returnList.get(0);
    }
    returnVO.setCustbankVO(returnCustbankVO);

    fireAfterUpdateEvent(returnVO, oldvo);
    // 客商银行账户子户更新
    if (isCustSupplier(custbankVO.getPk_cust(), custbankVO.getAccclass())
        && !bankaccbasVO.getSharetag().booleanValue()) {
      getCustSupBankaccShareService().shareUpdateCustSupbankaccs(returnVO);
    }
    // 增加业务日志
    CustbankaccLogUtil util =
        new CustbankaccLogUtil(IBDMetaDataIDConst.CUSTBANKACC, returnVO.getCustbankVO());
    util.writeModefyBusiLog(IBusiOperateConst.EDIT, returnVO.getBankaccbasVO(),
        oldvo.getBankaccbasVO());
    // 提供修改默认币种时发事件通知构造账户的unionbankaccvo
    Collection<CustbankVO> newDefaultBankcol =
        getBaseDao().retrieveByClause(CustbankVO.class,
            "pk_cust='" + pk_cust + "' and isdefault='Y'");
    CustbankVO newDefaultBankvo = null;
    if (newDefaultBankcol != null && newDefaultBankcol.size() > 0) {
      newDefaultBankvo = newDefaultBankcol.toArray(new CustbankVO[0])[0];

    }
    if (isDispatch(oldDefaultBankvo, newDefaultBankvo)) {

      CustBankaccUnionVO oldunionvo = new CustBankaccUnionVO();
      oldunionvo.setCustbankVO(oldCustbankvo);
      CustBankaccUnionVO newunionvo = new CustBankaccUnionVO();
      newunionvo.setCustbankVO(newDefaultBankvo);
      fireSetDefaultEvent(newunionvo, oldunionvo);

    }

    return returnVO;
  }

  private boolean isDispatch(CustbankVO oldDefaultBankvo, CustbankVO newDefaultBankvo) {
    if (newDefaultBankvo != null && oldDefaultBankvo != null
        && !newDefaultBankvo.getPk_bankaccsub().equals(oldDefaultBankvo.getPk_bankaccsub())) {
      return true;

    }
    else if (newDefaultBankvo == null && oldDefaultBankvo != null) {
      return true;

    }
    else if (oldDefaultBankvo == null && newDefaultBankvo != null) {
      return true;

    }
    return false;

  }

  private void fireSetDefaultEvent(CustBankaccUnionVO vo, CustBankaccUnionVO oldvo)
      throws BusinessException {
    EventDispatcher.fireEvent(new BDCommonEvent(IBDMetaDataIDConst.CUSTBANKACC, "1090",
        new Object[] {
            oldvo
        }, vo));

  }

  protected void fireAfterUpdateEvent(CustBankaccUnionVO vo, CustBankaccUnionVO oldvo)
      throws BusinessException {

    BDCommonEventUtil psnbankaccbas = new BDCommonEventUtil(IBDMetaDataIDConst.CUSTBANKACC, oldvo);
    psnbankaccbas.dispatchUpdateAfterEvent(vo);
    // EventDispatcher.fireEvent(new
    // UsePermChangeEvent(IBDMetaDataIDConst.PSNBANKACCBAS,
    // IEventType.TYPE_UPDATE_AFTER));
  }

  protected void fireAfterDeleteEvent(CustBankaccUnionVO vo) throws BusinessException {
    BDCommonEventUtil psnbankaccbas = new BDCommonEventUtil(IBDMetaDataIDConst.CUSTBANKACC);
    psnbankaccbas.dispatchDeleteAfterEvent(vo);
    // EventDispatcher.fireEvent(new
    // UsePermChangeEvent(IBDMetaDataIDConst.PSNBANKACCBAS,
    // IEventType.TYPE_DELETE_AFTER));
  }

  protected void fireBeforeDeleteEvent(CustBankaccUnionVO vo) throws BusinessException {
    BDCommonEventUtil psnbankaccbas = new BDCommonEventUtil(IBDMetaDataIDConst.CUSTBANKACC);
    psnbankaccbas.dispatchDeleteBeforeEvent(vo);
  }

  private void updateValidator(CustBankaccUnionVO updateVO) throws ValidationException {
    // 后台逻辑校验
    CustBankRefPkExsitValidation refValidator = new CustBankRefPkExsitValidation();
    ValidationFailure faliure =
        refValidator.validate(updateVO.getCustbankVO(), updateVO.getCustbankVO().getAccclass());
    List<ValidationFailure> failurelist = new ArrayList<ValidationFailure>();
    if (faliure != null) {
      failurelist.add(faliure);
      throw new nc.bs.uif2.validation.ValidationException(failurelist);
    }
    CustBankAccountNullValidation nullValidation = new CustBankAccountNullValidation();
    CustBankaccsubValidation accsubValidation = new CustBankaccsubValidation();
    CustBankAccountUniqueValidation uniqueValidation = new CustBankAccountUniqueValidation();
    PsnAndCustSubaccCurrtypeUniqueValidation accsubUniqueValidation =
        new PsnAndCustSubaccCurrtypeUniqueValidation();
    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(nullValidation, accsubValidation,
            uniqueValidation, accsubUniqueValidation);
    vService.validate(updateVO);
    vService =
        ValidationFrameworkUtil.createValidationService(new SingleDistributedUpdateValidator());
    vService.validate(updateVO.getCustbankVO());
    IValidationService vaService = ValidationFrameworkUtil.createValidationService(new Validator[] {
        new BankaccIbanValidator()
    });
    vaService.validate(updateVO.getBankaccbasVO());
  }

  // /*
  // * 需要触发update后事件
  // */
  // @Override
  // public CustBankaccUnionVO[] processIsDefaultOperation(
  // CustBankaccUnionVO updateVO,String pk_bankaccsub) throws
  // BusinessException {
  //
  // String pk_cust = updateVO.getCustbankVO().getPk_cust();
  // Integer accclass = updateVO.getCustbankVO().getAccclass();
  //
  // BankAccSubVO unionVO = new CustBankaccQueryServiceImpl()
  // .queryDefaultCustBankacc(pk_cust, accclass);
  //
  // if (unionVO != null) {
  // return updateCustBank(pk_cust,accclass, unionVO,pk_bankaccsub);
  // } else {
  // return updateCustBank(pk_cust,accclass,pk_bankaccsub);
  // }
  // }
  /*
   * 需要触发update后事件
   */
  @Override
  public CustBankaccUnionVO[] processIsDefaultOperation(CustBankaccUnionVO updateVO,
      String pk_bankaccsub) throws BusinessException {

    String pk_cust = updateVO.getCustbankVO().getPk_cust();
    BDPKLockUtil.lockString(pk_cust);
    Integer accclass = updateVO.getCustbankVO().getAccclass();

    CustBankaccUnionVO unionVO =
        new CustBankaccQueryServiceImpl().queryDefaultCustBankacc(pk_cust, accclass);

    if (unionVO != null) {
      return updateCustBank(updateVO, unionVO, pk_bankaccsub);
    }
    else {
      return updateCustBank(updateVO, pk_bankaccsub);
    }
  }

  private CustBankaccUnionVO[] updateCustBank(CustBankaccUnionVO updateVONew,
      CustBankaccUnionVO updateVOOld, String pk_bankaccsub) throws BusinessException {
    CustSupBankAccSubUpdateHandler subBankaccDefaulthandler = new CustSupBankAccSubUpdateHandler();
    CustbankVO custbankVONew = updateVONew.getCustbankVO();
    CustbankVO custbankVOOld = updateVOOld.getCustbankVO();
    String pk_cust = NCESAPI.sqlEncode(custbankVONew.getPk_cust());
    String old_pk_bankaccsub = NCESAPI.sqlEncode(custbankVOOld.getPk_bankaccsub());
    Integer enableState = updateVONew.getBankaccbasVO().getEnablestate();
    // String old_pk_bankaccbas = custbankVOOld.getPk_bankaccbas();
    // String new_pk_bankaccbas = custbankVONew.getPk_bankaccbas();
    Integer accclass = custbankVOOld.getAccclass();
    // 主键锁
    BDPKLockUtil.lockSuperVO(new CustbankVO[] {
        custbankVONew, custbankVOOld
    });

    String oldVO_sql =
        "update bd_custbank set isdefault='N' where pk_cust = '" + pk_cust
            + "' and pk_bankaccsub = '" + old_pk_bankaccsub + "' and accclass = "
            + accclass.intValue();
    String newVO_sql =
        "update bd_custbank set isdefault='Y' where pk_cust = '" + pk_cust
            + "' and pk_bankaccsub = '" + NCESAPI.sqlEncode(pk_bankaccsub) + "' and accclass = "
            + accclass.intValue();
    dao.executeUpdate(oldVO_sql);
    dao.executeUpdate(newVO_sql);
    // 更新默认子户
    if (isCustSupplier(pk_cust, accclass))
      subBankaccDefaulthandler.processIsCustBankDefault(pk_cust, pk_bankaccsub, old_pk_bankaccsub,
          accclass);
    CacheProxy.fireDataUpdated("bd_custbank");
    updateVONew.getCustbankVO().setPk_bankaccsub(pk_bankaccsub);
    updateVOOld.getCustbankVO().setPk_bankaccsub(old_pk_bankaccsub);
    fireSetDefaultEvent(updateVONew, updateVOOld);
    boolean isShowSeal = IPubEnumConst.ENABLESTATE_DISABLE == enableState;
    // 构造返回值
    return new CustBankaccQueryServiceImpl().queryCustBankaccUnionVOsByCust(
        NCESAPI.sqlEncode(custbankVONew.getPk_cust()), accclass, isShowSeal);

  }

  private CustBankaccUnionVO[] updateCustBank(CustBankaccUnionVO updateVONew, String pk_bankaccsub)
      throws BusinessException {
    CustSupBankAccSubUpdateHandler subBankaccDefaulthandler = new CustSupBankAccSubUpdateHandler();
    CustBankaccUnionVO oldvo = updateVONew;
    CustbankVO custbankVONew = updateVONew.getCustbankVO();
    String pk_cust = NCESAPI.sqlEncode(updateVONew.getCustbankVO().getPk_cust());
    String pk_bankaccbas = NCESAPI.sqlEncode(updateVONew.getCustbankVO().getPk_bankaccbas());
    Integer accclass = custbankVONew.getAccclass();
    Integer enableState = updateVONew.getBankaccbasVO().getEnablestate();

    custbankVONew.setIsdefault(UFBoolean.TRUE);
    // 主键锁
    BDPKLockUtil.lockSuperVO(custbankVONew);

    String sql =
        "update bd_custbank set isdefault='Y' where pk_cust = '" + pk_cust
            + "' and pk_bankaccbas = '" + pk_bankaccbas + "' and pk_bankaccsub='" + pk_bankaccsub
            + "' and accclass = " + accclass.intValue();
    CacheProxy.fireDataUpdated("bd_custbank");
    dao.executeUpdate(sql);
    // 更新默认子户
    if (isCustSupplier(pk_cust, accclass))
      subBankaccDefaulthandler.processCustbankDefault(pk_cust, pk_bankaccbas, pk_bankaccsub,
          accclass);
    updateVONew.getCustbankVO().setPk_bankaccsub(pk_bankaccsub);
    fireSetDefaultEvent(updateVONew, oldvo);

    boolean isShowSeal = IPubEnumConst.ENABLESTATE_DISABLE == enableState;
    // 构造返回值
    return new CustBankaccQueryServiceImpl().queryCustBankaccUnionVOsByCust(
        custbankVONew.getPk_cust(), accclass, isShowSeal);

  }

  private CustBankaccUnionVO[] updateCustBank(String pk_cust, Integer accclass,
      BankAccSubVO updateVOOld, String pk_bankaccsub) throws BusinessException {

    // // 主键锁
    // BDPKLockUtil.lockSuperVO(new CustbankVO[] { custbankVONew,
    // custbankVOOld });
    String old_pk_bankaccsub = NCESAPI.sqlEncode(updateVOOld.getPk_bankaccsub());

    String oldBankaccsub_sql =
        "update bd_bankaccsub set isdefault='N' where pk_bankaccsub='" + old_pk_bankaccsub + "'";
    String newBankaccsub_sql =
        "update bd_bankaccsub set isdefault='Y' where pk_bankaccsub='"
            + NCESAPI.sqlEncode(pk_bankaccsub) + "'";

    dao.executeUpdate(oldBankaccsub_sql);
    dao.executeUpdate(newBankaccsub_sql);
    // CacheProxy.fireDataUpdated("bd_custbank");

    // 构造返回值
    return new CustBankaccQueryServiceImpl()
        .queryCustBankaccUnionVOsByCust(pk_cust, accclass, true);

  }

  private CustBankaccUnionVO[] updateCustBank(String pk_cust, Integer accclass, String pk_bankaccsub)
      throws BusinessException {
    String bankaccsubsql =
        "update bd_bankaccsub set isdefault='Y' where pk_bankaccsub='"
            + NCESAPI.sqlEncode(pk_bankaccsub) + "'";
    dao.executeUpdate(bankaccsubsql);
    // 构造返回值
    return new CustBankaccQueryServiceImpl()
        .queryCustBankaccUnionVOsByCust(pk_cust, accclass, true);

  }

  @Override
  public CustBankaccUnionVO disableCustBankacc(CustBankaccUnionVO vo) throws BusinessException {
    CustbankVO custbankVO = vo.getCustbankVO();
    BankAccbasVO bankaccbasVO = vo.getBankaccbasVO();

    // 主键锁
    BDPKLockUtil.lockString(custbankVO.getPk_cust());
    BDPKLockUtil.lockSuperVO(bankaccbasVO);
    // 版本校验
    BDVersionValidationUtil.validateSuperVO(bankaccbasVO);
    checkCustBankForDist(vo);
    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(new SingleDistributedUpdateValidator());
    vService.validate(vo.getCustbankVO());
    // 库操作
    BDAuditInfoUtil.updateData(bankaccbasVO);
    bankaccbasVO.setEnablestate(IPubEnumConst.ENABLESTATE_DISABLE);
    // 设定停用时间
    bankaccbasVO.setDisabletime(new UFDateTime());

    bankaccbasVO.setStatus(VOStatus.UPDATED);
    getMDService().saveBill(bankaccbasVO);
    // //增加数据权限的事件通知
    // EventDispatcher.fireEvent(new
    // UsePermChangeEvent(IBDMetaDataIDConst.CUSTBANKACC,
    // IEventType.TYPE_ENABLE_DISABLE_AFTER));
    // 缓存通知
    CacheProxy.fireDataUpdated(bankaccbasVO.getTableName(), bankaccbasVO.getPrimaryKey());
    // 构造返回值
    CustBankaccUnionVO returnVO = new CustBankaccUnionVO();
    BankAccbasVO returnBankaccbasVO =
        getMDQryService()
            .queryBillOfVOByPK(BankAccbasVO.class, bankaccbasVO.getPrimaryKey(), false);
    returnVO.setBankaccbasVO(returnBankaccbasVO);
    returnVO.setCustbankVO(custbankVO);

    // 增加业务日志
    CustbankaccLogUtil util =
        new CustbankaccLogUtil(IBDMetaDataIDConst.CUSTBANKACC, returnVO.getCustbankVO());
    BankAccbasVO oldVO = (BankAccbasVO) returnVO.getBankaccbasVO().clone();
    oldVO.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
    util.writeModefyBusiLog(IBusiOperateConst.DISABLE, returnVO.getBankaccbasVO(), oldVO);

    //同步主数据
    if(vo!=null){
        List<CustBankaccUnionVO> vos = new ArrayList<CustBankaccUnionVO>();
        vos.add(vo);
        CustBankaccUnionVO[] custBankaccUnionVOS = new CustBankaccUnionVO[vos.size()];
		for(int i=0;i<vos.size();i++){
			custBankaccUnionVOS[i] = vos.get(i);
		}

		try {
			masterDataDisposeProcess.bankAccSubDispose(custBankaccUnionVOS, masterDataTranslateProcess, Operations.ENABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据客商档案【客商银行账户】出错:" + e.getMessage());
		}
	
    }
    
    return returnVO;
  }

  public CustBankaccUnionVO enableCustBankacc(CustBankaccUnionVO vo) throws BusinessException {
    CustbankVO custbankVO = vo.getCustbankVO();
    BankAccbasVO bankaccbasVO = vo.getBankaccbasVO();

    // 主键锁
    BDPKLockUtil.lockString(custbankVO.getPk_cust());
    BDPKLockUtil.lockSuperVO(bankaccbasVO);
    // 版本校验
    BDVersionValidationUtil.validateSuperVO(custbankVO);
    BDVersionValidationUtil.validateSuperVO(bankaccbasVO);
    checkCustBankForDist(vo);
    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(getEnableValidator());
    vService.validate(vo);
    // 库操作
    BDAuditInfoUtil.updateData(bankaccbasVO);
    bankaccbasVO.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
    // 设定启用时间
    bankaccbasVO.setEnabletime(new UFDateTime());
    bankaccbasVO.setStatus(VOStatus.UPDATED);
    getMDService().saveBill(bankaccbasVO);
    // //增加数据权限的事件通知
    // EventDispatcher.fireEvent(new
    // UsePermChangeEvent(IBDMetaDataIDConst.CUSTBANKACC,
    // IEventType.TYPE_DISABLE_ENABLE_AFTER));
    // 缓存通知
    CacheProxy.fireDataUpdated(bankaccbasVO.getTableName(), bankaccbasVO.getPrimaryKey());
    // 构造返回值
    CustBankaccUnionVO returnVO = new CustBankaccUnionVO();
    BankAccbasVO returnBankaccbasVO =
        getMDQryService()
            .queryBillOfVOByPK(BankAccbasVO.class, bankaccbasVO.getPrimaryKey(), false);
    returnVO.setBankaccbasVO(returnBankaccbasVO);
    returnVO.setCustbankVO(custbankVO);

    // 增加业务日志
    CustbankaccLogUtil util =
        new CustbankaccLogUtil(IBDMetaDataIDConst.CUSTBANKACC, returnVO.getCustbankVO());
    BankAccbasVO oldVO = (BankAccbasVO) returnVO.getBankaccbasVO().clone();
    oldVO.setEnablestate(IPubEnumConst.ENABLESTATE_DISABLE);
    util.writeModefyBusiLog(IBusiOperateConst.ENABLE, returnVO.getBankaccbasVO(), oldVO);

    //同步主数据
    if(vo!=null){
        List<CustBankaccUnionVO> vos = new ArrayList<CustBankaccUnionVO>();
        vos.add(vo);
        CustBankaccUnionVO[] custBankaccUnionVOS = new CustBankaccUnionVO[vos.size()];
		for(int i=0;i<vos.size();i++){
			custBankaccUnionVOS[i] = vos.get(i);
		}

		try {
			masterDataDisposeProcess.bankAccSubDispose(custBankaccUnionVOS, masterDataTranslateProcess, Operations.ENABLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.error("同步主数据客商档案【客商银行账户】出错:" + e.getMessage());
		}
	
    }
    
    return returnVO;
  }

  @SuppressWarnings("unchecked")
  public void deleteCustBankaccByCustIDAndType(String pk_cust, final Integer accclass)
      throws BusinessException {
    if (pk_cust == null) {
      return;
    }
    String where = CustbankVO.PK_CUST + " = '" + NCESAPI.sqlEncode(pk_cust) + "'";
    if (accclass != null) {
      where += " and " + CustbankVO.ACCCLASS + " = " + accclass.intValue();
    }
    dao.deleteByClause(CustbankVO.class, where);
    CacheProxy.fireDataDeleted("bd_custbank", null);
  }

  @SuppressWarnings("unchecked")
  public void deleteCustBankaccByCustIDsAndAccID(String[] pk_custs, final String accID)
      throws BusinessException {
    if (pk_custs == null || pk_custs.length == 0 || accID == null) {
      return;
    }
    InSqlBatchCaller caller = new InSqlBatchCaller(pk_custs);
    List<CustbankVO> voList = null;
    try {
      voList = (List<CustbankVO>) caller.execute(new IInSqlBatchCallBack() {

        List<CustbankVO> voList = new ArrayList<CustbankVO>();

        @SuppressWarnings("unchecked")
        public Object doWithInSql(String inSql) throws DAOException {
          String where =
              CustbankVO.PK_BANKACCSUB + " = '" + NCESAPI.sqlEncode(accID) + "' and "
                  + CustbankVO.PK_CUST + " in " + inSql;
          Collection<CustbankVO> col = dao.retrieveByClause(CustbankVO.class, where);
          voList.addAll(col);
          return voList;
        }

      });
    }
    catch (SQLException e) {
      Logger.error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }
    if (voList == null || voList.size() == 0) {
      return;
    }
    deleteCustBankVOs(voList.toArray(new CustbankVO[0]));
    // final String pk_bankaccbas = voList.get(0).getPk_bankaccbas();
    // //查询客商银行账户表里该子户对应的账户是否还有其他子户，如果没有，则需要删除银行账户的记录
    // InSqlBatchCaller custaccCaller = new InSqlBatchCaller(pk_custs);
    // List<CustbankVO> custaccVOList = null;
    // try {
    // custaccVOList = (List<CustbankVO>) custaccCaller.execute(new
    // IInSqlBatchCallBack(){
    //
    // List<CustbankVO> custaccVOList = new ArrayList<CustbankVO>();
    //
    // public Object doWithInSql(String inSql) throws DAOException{
    // String where = CustbankVO.PK_BANKACCSUB + " is not null and " +
    // CustbankVO.PK_BANKACCBAS + " = '" + pk_bankaccbas + "' and " +
    // CustbankVO.PK_CUST + " in " + inSql;
    // Collection<CustbankVO> col= dao.retrieveByClause(CustbankVO.class,
    // where);
    // custaccVOList.addAll(col);
    // return custaccVOList;
    // }
    //
    // });
    // } catch (SQLException e) {
    // Logger.error(e.getMessage(),e);
    // throw new BusinessException(e.getMessage());
    // }
    // List<String> list = new ArrayList<String>();
    // list.addAll(Arrays.asList(pk_custs));
    // if(custaccVOList!=null && custaccVOList.size()>0){
    // List<String> custIDList =
    // VOUtil.extractFieldValues(custaccVOList.toArray(new CustbankVO[0]),
    // CustbankVO.PK_CUST, null);
    // for(String pk_cust : pk_custs){
    // if(custIDList.contains(pk_cust)){
    // list.remove(pk_cust);
    // }
    // }
    // }
    // if(list.size()>0){
    // InSqlBatchCaller accCaller = new InSqlBatchCaller(list.toArray(new
    // String[0]));
    // try {
    // accCaller.execute(new IInSqlBatchCallBack(){
    //
    // public Object doWithInSql(String inSql) throws BusinessException,
    // SQLException {
    // String where = CustbankVO.PK_BANKACCBAS + " ='" + pk_bankaccbas +
    // "' and " + CustbankVO.PK_BANKACCSUB + " is null and " +
    // CustbankVO.PK_CUST + " in " + inSql;
    // dao.deleteByClause(CustbankVO.class, where);
    // return null;
    // }
    //
    // });
    // } catch (SQLException e) {
    // Logger.error(e.getMessage(),e);
    // throw new BusinessException(e.getMessage());
    // }
    // }
  }

  @SuppressWarnings("unchecked")
  public void deleteCustBankaccByCustIDAndAccIDs(final String pk_cust, String[] accIDs)
      throws BusinessException {
    if (pk_cust == null || accIDs == null || accIDs.length == 0) {
      return;
    }
    InSqlBatchCaller caller = new InSqlBatchCaller(accIDs);
    List<CustbankVO> voList = null;
    try {
      voList = (List<CustbankVO>) caller.execute(new IInSqlBatchCallBack() {

        List<CustbankVO> voList = new ArrayList<CustbankVO>();

        @SuppressWarnings("unchecked")
        public Object doWithInSql(String inSql) throws BusinessException, SQLException {
          String where =
              CustbankVO.PK_CUST + " = '" + NCESAPI.sqlEncode(pk_cust) + "' and "
                  + CustbankVO.PK_BANKACCSUB + " in " + inSql;
          Collection<CustbankVO> col = dao.retrieveByClause(CustbankVO.class, where);
          voList.addAll(col);
          return voList;
        }

      });
    }
    catch (SQLException e) {
      Logger.error(e.getMessage(), e);
      throw new BusinessException(e.getMessage());
    }
    if (voList == null || voList.size() == 0) {
      return;
    }
    deleteCustBankVOs(voList.toArray(new CustbankVO[0]));
  }

  @SuppressWarnings("unchecked")
  public void insertCustBankaccByAccIDsAndCustID(String[] accIDs, String pk_cust, Integer accclass)
      throws BusinessException {
    if (accIDs == null || accIDs.length == 0 || pk_cust == null || accclass == null) {
      return;
    }
    // 查询子户对应的银行账户的pk
    Map<String, String> map = new BankAccBaseInfoQueryServiceImpl().querySubVOsBySubPks(accIDs);
    if (map != null) {
      CustbankVO[] custbankVOs = new CustbankVO[accIDs.length];
      for (int i = 0; i < custbankVOs.length; i++) {
        custbankVOs[i] = new CustbankVO();
        custbankVOs[i].setPk_bankaccsub(accIDs[i]);
        custbankVOs[i].setPk_bankaccbas(map.get(accIDs[i]));
        custbankVOs[i].setPk_cust(pk_cust);
        custbankVOs[i].setAccclass(accclass);
        custbankVOs[i].setIsdefault(UFBoolean.FALSE);
      }
      insertCustBankVOs(custbankVOs);
    }
  }

  public void insertCustBankaccByCustIDsAndAccID(String[] pk_custs, String accID, Integer accclass)
      throws BusinessException {
    if (pk_custs == null || pk_custs.length == 0 || accID == null || accclass == null) {
      return;
    }
    BankAccSubVO subVO = (BankAccSubVO) dao.retrieveByPK(BankAccSubVO.class, accID);
    String pk_bankaccbas = subVO.getPk_bankaccbas();
    CustbankVO[] custbankVOs = new CustbankVO[pk_custs.length];
    for (int i = 0; i < pk_custs.length; i++) {
      custbankVOs[i] = new CustbankVO();
      custbankVOs[i].setPk_bankaccbas(pk_bankaccbas);
      custbankVOs[i].setPk_bankaccsub(accID);
      custbankVOs[i].setPk_cust(pk_custs[i]);
      custbankVOs[i].setAccclass(accclass);
      custbankVOs[i].setIsdefault(UFBoolean.FALSE);
    }
    insertCustBankVOs(custbankVOs);
  }

  public void deleteCustBankVOs(CustbankVO[] custbankVOs) throws BusinessException {
    if (custbankVOs == null || custbankVOs.length == 0)
      return;

    // 删除时的加锁操作
    BDPKLockUtil.lockString(getCustIDS(custbankVOs));

    // 校验版本
    BDVersionValidationUtil.validateSuperVO(custbankVOs);

    // 删除前引用对象校验
    deleteValidateVOs(custbankVOs);

    // 删除前事件处理
    fireBeforeDeleteEvent(custbankVOs);

    // 缓存通知
    CacheProxy.fireDataDeleted(CustbankVO.getDefaultTableName(), null);

    // 库操作
    dbDeleteVOs(custbankVOs);

    // 删除后事件通知
    fireAfterDeleteEvent(custbankVOs);
    // //增加数据权限的事件通知
    // EventDispatcher.fireEvent(new
    // UsePermChangeEvent(IBDMetaDataIDConst.CUSTBANKACC,
    // IEventType.TYPE_DELETE_AFTER));

    // 业务日志
    writeDeletedBusiLog(custbankVOs);
  }

  public CustbankVO[] insertCustBankVOs(CustbankVO[] custbankVOs) throws BusinessException {
    if (custbankVOs == null || custbankVOs.length == 0)
      return null;

    // 新增时的加锁操作
    BDPKLockUtil.lockString(getCustIDS(custbankVOs));
    // 逻辑校验
    IValidationService vService =
        ValidationFrameworkUtil.createValidationService(getCustBankInsertValidator());
    vService.validate(custbankVOs);

    // 插入前事件通知
    fireBeforeInsertEvent(custbankVOs);

    // 库操作
    String[] pks = dbInsertVOs(custbankVOs);

    // 通知更新缓存
    CacheProxy.fireDataInserted(CustbankVO.getDefaultTableName(), null);

    // 重新检索出插入的VO
    custbankVOs = retrieveVOs(pks, null, custbankVOs);

    // 插入事件后通知
    fireAfterInsertEvent(custbankVOs);

    // 业务日志
    writeInsertBusiLog(custbankVOs);

    return custbankVOs;
  }

  protected Validator[] getCustBankInsertValidator() {
    Validator[] validators = new Validator[] {
        new BDUniqueRuleValidate()
    };
    return validators;

  }

  public void deleteCustBankaccByAccID(String accID) throws BusinessException {
    if (StringUtils.isBlank(accID)) {
      return;
    }
    String where = CustbankVO.PK_BANKACCSUB + " = '" + NCESAPI.sqlEncode(accID) + "'";
    dao.deleteByClause(CustbankVO.class, where);
    CacheProxy.fireDataDeleted(CustbankVO.getDefaultTableName(), null);
  }

  /**
   * 返回元数据持久化服务对象
   */
  private IMDPersistenceService getMDService() {
    return MDPersistenceService.lookupPersistenceService();
  }

  private IMDPersistenceQueryService getMDQryService() {
    return MDPersistenceService.lookupPersistenceQueryService();
  }

  private IBankdocService getBankdocService() {
    if (bankdocService == null) {
      bankdocService = NCLocator.getInstance().lookup(IBankdocService.class);
    }
    return bankdocService;

  }

  private ICustSupQueryService getCustSupQueryService() {
    if (custSupQryService == null) {
      custSupQryService = NCLocator.getInstance().lookup(ICustSupQueryService.class);
    }
    return custSupQryService;
  }

  private String[] getCustIDS(CustbankVO[] custbankVOs) {
    if (custbankVOs == null || custbankVOs.length == 0)
      return null;
    ArrayList<String> custIDList = new ArrayList<String>();
    for (CustbankVO custbankVO : custbankVOs) {
      custIDList.add(custbankVO.getPk_cust());
    }
    return custIDList.toArray(new String[0]);
  }

  @Override
  public CustbankVO[] updateCustbankForDist(CustbankVO[] custbankvos) throws BusinessException {
    if (custbankvos == null || custbankvos.length == 0)
      return null;
    String[] pks = VOArrayUtil.getPrimaryKeyArray(custbankvos);
    // 查询所有已存在PK
    String sql =
        "select pk_custbank from " + CustbankVO.getDefaultTableName() + " where "
            + BDSqlInUtil.formInSQLWithoutAnd(CustbankVO.PK_CUSTBANK, pks, false);
    List<String> pkList = (List<String>) dao.executeQuery(sql, new ColumnListProcessor());
    List<CustbankVO> addList = new ArrayList<CustbankVO>();
    List<CustbankVO> upList = new ArrayList<CustbankVO>();
    for (CustbankVO vo : custbankvos) {
      if (pkList.contains(vo.getPrimaryKey())) {
        vo.setStatus(VOStatus.UPDATED);
        upList.add(vo);
      }
      else {
        vo.setStatus(VOStatus.NEW);
        addList.add(vo);
      }
    }
    if (addList.size() > 0) {
      dao.insertVOArrayWithPK(addList.toArray(new CustbankVO[0]));
      CacheProxy.fireDataInserted(CustbankVO.getDefaultTableName());
    }
    if (upList.size() > 0) {
      dao.updateVOArray(upList.toArray(new CustbankVO[0]));
      CacheProxy.fireDataUpdated(CustbankVO.getDefaultTableName());
    }

    return null;
  }

  @Override
  public CustBankaccUnionVO[] batchDisableCustBankacc(CustBankaccUnionVO[] vos)
      throws BusinessException {
    List<CustBankaccUnionVO> tmpList = new ArrayList<CustBankaccUnionVO>();

    for (CustBankaccUnionVO idx : vos) {
      tmpList.add(disableCustBankacc(idx));
    }

    return tmpList.toArray(new CustBankaccUnionVO[0]);
  }

  @Override
  public CustBankaccUnionVO[] batchEnableCustBankacc(CustBankaccUnionVO[] vos)
      throws BusinessException {
    List<CustBankaccUnionVO> tmpList = new ArrayList<CustBankaccUnionVO>();

    for (CustBankaccUnionVO idx : vos) {
      tmpList.add(enableCustBankacc(idx));
    }
    return tmpList.toArray(new CustBankaccUnionVO[0]);
  }

  private ICustSupBankaccShareService getCustSupBankaccShareService() {
    return NCLocator.getInstance().lookup(ICustSupBankaccShareService.class);
  }

  @Override
  public String insertBankDocWithInputDir(BankAccbasVO bankaccbasvo, String pk_org, String pk_group)
      throws BusinessException {
    return processBankdoc(bankaccbasvo, pk_org, pk_group);
  }
	
}

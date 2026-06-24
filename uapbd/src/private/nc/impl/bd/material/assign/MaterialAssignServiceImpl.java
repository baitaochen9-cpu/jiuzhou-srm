package nc.impl.bd.material.assign;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nc.bs.bd.assignservice.AssignBaseService;
import nc.bs.bd.assignservice.multiorg.MultiOrgReturnValueCombUtil;
import nc.bs.bd.material.marorg.IMarOrgService;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.component.RemoteProcessComponetFactory;
import nc.bs.framework.execute.ThreadFactoryManager;
import nc.bs.logging.Logger;
import nc.itf.bd.material.assign.IMaterialAssignService;
import nc.itf.bd.pub.IBDMetaDataIDConst;
import nc.itf.bd.pub.assign.AssignStatus;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.bd.assign.AssignStatusVO;
import nc.vo.bd.errorlog.ErrLogResult;
import nc.vo.bd.errorlog.ErrLogReturnValue;
import nc.vo.bd.errorlog.ErrorLogUtil;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.pub.sqlutil.BDSqlInUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.util.BDPKLockUtil;
import nc.vo.util.SqlWhereUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

/**
 * 物料分配实现
 * 
 * @author jiangjuna
 */
public class MaterialAssignServiceImpl extends AssignBaseService implements IMaterialAssignService {

  private BaseDAO baseDAO = null;

  private static final int MAX_NUM_MAR = 300000;

  private static final int MAX_NUM_ORG = 100;

  private IMarOrgService marOrgService;

  public MaterialAssignServiceImpl() {
    super(IBDMetaDataIDConst.MATERIAL, MaterialVO.PK_MARBASCLASS);
  }

  @Override
  public ErrLogReturnValue assignMaterialByCondition(final String[] funcPermissionOrgIDs,
      final String whereCondition, final AssignStatus assignStatus, final String[] targets)
      throws BusinessException {
    final String wherePart =
        this.appendFuncPermissionCondition(whereCondition, funcPermissionOrgIDs);
    final InvocationInfo info = getInvocationInfo();
    final String logUtilPK = generateNewPk();
    // 启用两上线程分别处理分配信息分配和其它页签信息分配
    ExecutorService executorService =
        Executors.newFixedThreadPool(2, ThreadFactoryManager.newThreadFactory());
    CompletionService<ErrLogReturnValue> service =
        new ExecutorCompletionService<ErrLogReturnValue>(executorService);
    // 线程一：分配记录关系
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }

          ErrLogReturnValue multiorgValue =
              marOrgAssignByCondition(funcPermissionOrgIDs, wherePart, assignStatus, targets);
          // 调强分配接口，记录分配关系；
          // ErrLogReturnValue multiorgValue =
          // NCLocator
          // .getInstance()
          // .lookup(IMarOrgService.class)
          // .assignMaterialByCondition(funcPermissionOrgIDs, wherePart, assignStatus, targets);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    // 线程二：分配页签信息
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          ErrLogReturnValue multiorgValue =
              NCLocator.getInstance().lookup(IMaterialAssignService.class)
                  .assignByCondition(wherePart, assignStatus, targets);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    int taskCount = 0;// 执行完的任务计数器
    Future<ErrLogReturnValue> f = null;
    List<ErrLogReturnValue> list = new ArrayList<ErrLogReturnValue>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    while (taskCount < 2) {
      try {
        f = service.take();
        Object obj = f.get();
        if (obj instanceof ErrLogReturnValue) {
          list.add((ErrLogReturnValue) obj);
        }
      } catch (Exception e) {
        Logger.error(e);
        if (f != null) {
          f.cancel(true);
        }
        exceptionList.add(e);
      }
      taskCount++;
    }
    executorService.shutdownNow();
    if (!CollectionUtils.isEmpty(exceptionList)) {
      Exception exception = exceptionList.get(0);
      BusinessException e = new BusinessException(exception.getMessage());
      e.setStackTrace(exception.getStackTrace());
      RemoteProcessComponetFactory factory =
          (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
              "RemoteProcessComponetFactory");
      if (factory != null) {
        factory.postErrorProcess(exception);
        factory.clearThreadScopePostProcess();
      }
      throw e;
    }
    return MultiOrgReturnValueCombUtil.combineReturnValue(list.get(0), list.get(1), logUtilPK);
  }

  private ErrLogReturnValue marOrgAssignByCondition(final String[] funcPermissionOrgIDs,
      final String whereCondition, final AssignStatus assignStatus, final String[] targets)
      throws BusinessException {
    String countSql =
        "select  count(1) from " + MaterialVO.getDefaultTableName() + " where " + whereCondition;
    Integer marterialCount = (Integer) getBaseDAO().executeQuery(countSql, new ColumnProcessor());
    if (marterialCount <= MAX_NUM_MAR && targets.length <= MAX_NUM_ORG) {
      return getMarOrgService().assignMaterialByCondition(funcPermissionOrgIDs, whereCondition,
          assignStatus, targets);
    }
    List<ErrLogReturnValue> totalRrrLogReturnValues = new ArrayList<ErrLogReturnValue>();
    int count = targets.length / MAX_NUM_ORG;
    int remain = targets.length % MAX_NUM_ORG;
    for (int i = 0; i < count; i++) {
      int start = i * MAX_NUM_ORG;
      int end = (i + 1) * MAX_NUM_ORG;
      String[] subtargets = getPKOrgsByIndex(start, end, targets);
      ErrLogReturnValue errLogReturnValue =
          getMarOrgService().assignMaterialByCondition_RequiresNew(funcPermissionOrgIDs,
              whereCondition, assignStatus, subtargets);
      totalRrrLogReturnValues.add(errLogReturnValue);
    }
    if (remain != 0) {
      int start = count * MAX_NUM_ORG;
      int end = targets.length;
      String[] subtargets = getPKOrgsByIndex(start, end, targets);
      ErrLogReturnValue errLogReturnValue =
          getMarOrgService().assignMaterialByCondition_RequiresNew(funcPermissionOrgIDs,
              whereCondition, assignStatus, subtargets);
      totalRrrLogReturnValues.add(errLogReturnValue);
    }
    return MultiOrgReturnValueCombUtil.combineListReturnValue(totalRrrLogReturnValues);
  }


  @Override
  public ErrLogReturnValue assignMaterialByPks(final String[] pks, final String[] targets,
      final String[] funcPermissionOrgIDs) throws BusinessException {
    final ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0000")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "分配"
                                                                                                   */,
            true);
    String[] filterOrgPks = null;
    if (ArrayUtils.isEmpty(funcPermissionOrgIDs)) {
      filterOrgPks = pks;
    } else {
      filterOrgPks = this.filterFuncPermission(pks, funcPermissionOrgIDs, util);
    }
    final InvocationInfo info = getInvocationInfo();
    final String[] filterPks = filterOrgPks;
    final String logUtilPK = generateNewPk();
    // 启用两上线程分别处理分配信息分配和其它页签信息分配
    ExecutorService executorService =
        Executors.newFixedThreadPool(2, ThreadFactoryManager.newThreadFactory());
    CompletionService<ErrLogReturnValue> service =
        new ExecutorCompletionService<ErrLogReturnValue>(executorService);
    // 线程一：分配记录关系
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          ErrLogReturnValue multiorgValue =
              marOrgAssignByPks(filterPks, targets, funcPermissionOrgIDs);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    // 线程二：分配页签信息
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          ErrLogReturnValue multiorgValue =
              NCLocator.getInstance().lookup(IMaterialAssignService.class)
                  .assignByPks(filterPks, targets, true);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    int taskCount = 0;// 执行完的任务计数器
    Future<ErrLogReturnValue> f = null;
    List<ErrLogReturnValue> list = new ArrayList<ErrLogReturnValue>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    while (taskCount < 2) {
      try {
        f = service.take();
        Object obj = f.get();
        if (obj instanceof ErrLogReturnValue) {
          list.add((ErrLogReturnValue) obj);
        }
      } catch (Exception e) {
        Logger.error(e);
        if (f != null) {
          f.cancel(true);
        }
        exceptionList.add(e);
      }
      taskCount++;
    }
    executorService.shutdownNow();
    if (!CollectionUtils.isEmpty(exceptionList)) {
      Exception exception = exceptionList.get(0);
      BusinessException e = new BusinessException(exception.getMessage());
      e.setStackTrace(exception.getStackTrace());
      RemoteProcessComponetFactory factory =
          (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
              "RemoteProcessComponetFactory");
      if (factory != null) {
        factory.postErrorProcess(exception);
        factory.clearThreadScopePostProcess();
      }
      throw e;
    }
    return MultiOrgReturnValueCombUtil.combineReturnValue(list.get(0), list.get(1), logUtilPK);
  }


  private ErrLogReturnValue marOrgAssignByPks(final String[] filterPks, final String[] targets,
      final String[] funcPermissionOrgIDs) throws BusinessException {
    if (filterPks.length <= MAX_NUM_MAR && targets.length <= MAX_NUM_ORG) {
      return getMarOrgService().assignMaterialByPks(filterPks, targets, funcPermissionOrgIDs);
    }
    List<ErrLogReturnValue> totalRrrLogReturnValues = new ArrayList<ErrLogReturnValue>();
    int count = targets.length / MAX_NUM_ORG;
    int remain = targets.length % MAX_NUM_ORG;
    for (int i = 0; i < count; i++) {
      int start = i * MAX_NUM_ORG;
      int end = (i + 1) * MAX_NUM_ORG;
      String[] subtargets = getPKOrgsByIndex(start, end, targets);
      ErrLogReturnValue errLogReturnValue =
          getMarOrgService().assignMaterialByPks_RequiresNew(filterPks, subtargets,
              funcPermissionOrgIDs);
      totalRrrLogReturnValues.add(errLogReturnValue);
    }
    if (remain != 0) {
      int start = count * MAX_NUM_ORG;
      int end = targets.length;
      String[] subtargets = getPKOrgsByIndex(start, end, targets);
      ErrLogReturnValue errLogReturnValue =
          getMarOrgService().assignMaterialByPks_RequiresNew(filterPks, subtargets,
              funcPermissionOrgIDs);
      totalRrrLogReturnValues.add(errLogReturnValue);
    }
    return MultiOrgReturnValueCombUtil.combineListReturnValue(totalRrrLogReturnValues);
  }


  private String[] getPKOrgsByIndex(int start, int end, String[] pk_orgs) {
    List<String> list = new ArrayList<String>();
    for (int i = start; i < end; i++) {
      list.add(pk_orgs[i]);
    }
    return list.toArray(new String[0]);
  }

  @Override
  public void assignMaterialToSelfOrg(MaterialVO vo) throws BusinessException {
    // 调强分配接口，记录分配关系；
    NCLocator.getInstance().lookup(IMarOrgService.class).assignMaterialToSelfOrg(vo);

    ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0000")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "分配"
                                                                                                   */,
            false);
    this.assignByPks(new String[] {vo.getPrimaryKey()},
        this.groupTargetPks(new String[] {vo.getPk_org()}, false), util);
    ErrLogResult result = util.getErrLogResult();
    if (result.getErrorMessagegNum() > 0) {
      throw new BusinessException(result.toString());
    }
  }


  @Override
  public void assignMaterialForPf(String pk_material, String pk_org, String[] funcPermissionOrgIDs)
      throws BusinessException {
    // 调强分配接口，记录分配关系；
    NCLocator
        .getInstance()
        .lookup(IMarOrgService.class)
        .assignMaterialByPks(new String[] {pk_material}, new String[] {pk_org},
            funcPermissionOrgIDs);
    ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0000")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "分配"
                                                                                                   */,
            false);
    this.assignByPks(new String[] {pk_material}, this.groupTargetPks(new String[] {pk_org}, false),
        util);
    ErrLogResult result = util.getErrLogResult();
    if (result.getErrorMessagegNum() > 0) {
      throw new BusinessException(result.toString());
    }
  }

  @Override
  public void cancelAssignMaterial(MaterialVO vo) throws BusinessException {
    // 调强分配接口，记录分配关系；
    NCLocator.getInstance().lookup(IMarOrgService.class).cancelAssignMaterial(vo);
    ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0003")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "取消分配"
                                                                                                   */,
            true);
    this.cancelAssignByPks(new String[] {vo.getPrimaryKey()}, this.groupTargetPks(null, false),
        util);
    ErrLogResult result = util.getErrLogResult();
    if (result.getErrorMessagegNum() > 0) {
      throw new BusinessException(result.toString());
    }
  }

  @Override
  public ErrLogReturnValue cancelAssignMaterialByCondition(final String[] funcPermissionOrgIDs,
      String whereCondition, final AssignStatus assignStatus, final String[] targets)
      throws BusinessException {
    final String wherePart =
        this.appendFuncPermissionCondition(whereCondition, funcPermissionOrgIDs);
    final InvocationInfo info = getInvocationInfo();
    final String logUtilPK = generateNewPk();
    // 启用两上线程分别处理分配信息分配和其它页签信息分配
    ExecutorService executorService =
        Executors.newFixedThreadPool(2, ThreadFactoryManager.newThreadFactory());
    CompletionService<ErrLogReturnValue> service =
        new ExecutorCompletionService<ErrLogReturnValue>(executorService);
    // 线程一：取消分配记录关系
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");

        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          // 调强分配接口，记录分配关系；
          ErrLogReturnValue multiorgValue =
              NCLocator
                  .getInstance()
                  .lookup(IMarOrgService.class)
                  .cancelAssignMaterialByCondition(funcPermissionOrgIDs, wherePart, assignStatus,
                      targets);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    // 线程二：取消分配页签信息
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          ErrLogReturnValue returnValue =
              NCLocator.getInstance().lookup(IMaterialAssignService.class)
                  .cancelAssignByCondition(wherePart, assignStatus, targets);
          if (factory != null) {
            factory.postProcess();
          }
          return returnValue;

        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            newFactory.clearThreadScopePostProcess();
        }
      }
    });
    int taskCount = 0;// 执行完的任务计数器
    Future<ErrLogReturnValue> f = null;
    List<ErrLogReturnValue> list = new ArrayList<ErrLogReturnValue>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    while (taskCount < 2) {
      try {
        f = service.take();
        Object obj = f.get();
        if (obj instanceof ErrLogReturnValue) {
          list.add((ErrLogReturnValue) obj);
        }
      } catch (Exception e) {
        Logger.error(e);
        if (f != null) {
          f.cancel(true);
        }
        exceptionList.add(e);
      }
      taskCount++;
    }
    executorService.shutdownNow();
    if (!CollectionUtils.isEmpty(exceptionList)) {
      Exception exception = exceptionList.get(0);
      BusinessException e = new BusinessException(exception.getMessage());
      e.setStackTrace(exception.getStackTrace());
      RemoteProcessComponetFactory factory =
          (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
              "RemoteProcessComponetFactory");
      if (factory != null) {
        factory.postErrorProcess(exception);
        factory.clearThreadScopePostProcess();
      }
      throw e;
    }
    return MultiOrgReturnValueCombUtil.combineReturnValue(list.get(0), list.get(1), logUtilPK);
  }

  @Override
  public ErrLogReturnValue cancelAssignMaterialByPks(final String[] pks, final String[] targets,
      final String[] funcPermissionOrgIDs) throws BusinessException {
    final ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0000")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "分配"
                                                                                                   */,
            true);
    String[] filterOrgPks = null;
    if (ArrayUtils.isEmpty(funcPermissionOrgIDs)) {
      filterOrgPks = pks;
    } else {
      filterOrgPks = this.filterFuncPermission(pks, funcPermissionOrgIDs, util);
    }
    final InvocationInfo info = getInvocationInfo();
    final String[] filterPks = filterOrgPks;
    final String logUtilPK = generateNewPk();
    // 启用两上线程分别处理分配信息分配和其它页签信息分配
    ExecutorService executorService =
        Executors.newFixedThreadPool(2, ThreadFactoryManager.newThreadFactory());
    CompletionService<ErrLogReturnValue> service =
        new ExecutorCompletionService<ErrLogReturnValue>(executorService);
    // 线程一：取消分配记录关系
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          // 调强分配接口，记录分配关系；
          ErrLogReturnValue multiorgValue =
              NCLocator.getInstance().lookup(IMarOrgService.class)
                  .cancelAssignMaterialByPks(filterPks, targets, funcPermissionOrgIDs);
          if (factory != null) {
            factory.postProcess();
          }
          return multiorgValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            factory.clearThreadScopePostProcess();
        }
      }
    });
    // 线程二：取消分配页签信息
    service.submit(new Callable<ErrLogReturnValue>() {
      @Override
      public ErrLogReturnValue call() throws Exception {
        RemoteProcessComponetFactory factory =
            (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                "RemoteProcessComponetFactory");
        try {
          setInvocationInfoWithLogUtilPK(info, logUtilPK);
          if (factory != null) {
            factory.preProcess();
          }
          ErrLogReturnValue returnValue =
              NCLocator.getInstance().lookup(IMaterialAssignService.class)
                  .cancelAssignByPks(filterPks, targets, true);
          if (factory != null) {
            factory.postProcess();
          }
          return returnValue;
        } catch (Exception ex) {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null) {
            newFactory.postErrorProcess(ex);
          }
          Logger.error(ex.getMessage(), ex);
          throw ex;
        } finally {
          RemoteProcessComponetFactory newFactory =
              (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
                  "RemoteProcessComponetFactory");
          if (newFactory != null)
            factory.clearThreadScopePostProcess();
        }
      }
    });
    int taskCount = 0;// 执行完的任务计数器
    Future<ErrLogReturnValue> f = null;
    List<ErrLogReturnValue> list = new ArrayList<ErrLogReturnValue>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    while (taskCount < 2) {
      try {
        f = service.take();
        Object obj = f.get();
        if (obj instanceof ErrLogReturnValue) {
          list.add((ErrLogReturnValue) obj);
        }
      } catch (Exception e) {
        Logger.error(e);
        if (f != null) {
          f.cancel(true);
        }
        exceptionList.add(e);
      }
      taskCount++;
    }
    executorService.shutdownNow();
    if (!CollectionUtils.isEmpty(exceptionList)) {
      Exception exception = exceptionList.get(0);
      BusinessException e = new BusinessException(exception.getMessage());
      e.setStackTrace(exception.getStackTrace());
      RemoteProcessComponetFactory factory =
          (RemoteProcessComponetFactory) NCLocator.getInstance().lookup(
              "RemoteProcessComponetFactory");
      if (factory != null) {
        factory.postErrorProcess(exception);
        factory.clearThreadScopePostProcess();
      }
      throw e;
    }
    return MultiOrgReturnValueCombUtil.combineReturnValue(list.get(0), list.get(1), logUtilPK);
  }

  @Override
  public ErrLogReturnValue copyAssignMaterialByPk(MaterialVO vo, String pk_source,
      boolean assignToSelf) throws BusinessException {
    // 调强分配接口，记录分配关系；
    NCLocator.getInstance().lookup(IMarOrgService.class)
        .copyAssignMaterialByPk(vo, pk_source, assignToSelf);
    BDPKLockUtil.lockString(vo.getPrimaryKey(), pk_source);
    ErrorLogUtil util =
        new ErrorLogUtil(
            this.getBaseBean().getID(),
            InvocationInfoProxy.getInstance().getUserId(),
            nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10180advcg", "010180advcg0037")/*
                                                                                                   * @
                                                                                                   * res
                                                                                                   * "复制分配数据"
                                                                                                   */,
            false);
    this.copyAssignByPk(vo, pk_source, util);
    if (assignToSelf) {
      this.assignByPks(new String[] {vo.getPrimaryKey()},
          this.groupTargetPks(new String[] {vo.getPk_org()}, false), util);
    }
    return util.getErrLogReturnValue(vo, -1);
  }

  @Override
  public AssignStatusVO queryAssignStatusVO(String[] pk_relations) throws BusinessException {
    return this.queryAssignStatus(pk_relations);
  }

  @Override
  public String queryAssignVOByOrg(String[] pk_orgs, String tablename) throws BusinessException {
    return this.queryAssignDataByOrg(pk_orgs, tablename);
  }

  @Override
  public String[] queryMaterialPksByCondition(String[] funcPermissionOrgIDs, String whereCondition,
      AssignStatus assignStatus, String[] targets) throws BusinessException {
    String wherePart = this.appendFuncPermissionCondition(whereCondition, funcPermissionOrgIDs);
    return this.queryPksByCondition(wherePart, assignStatus, targets);
  }

  private String appendFuncPermissionCondition(String condition, String[] funcPermissionOrgIDs) {
    String visibleCondtion =
        BDSqlInUtil.formInSQLWithoutAnd(MaterialVO.PK_ORG, funcPermissionOrgIDs, false);
    return new SqlWhereUtil(condition).and(visibleCondtion).getSQLWhere();
  }

  private String[] filterFuncPermission(String[] pks, String[] funcPermissionOrgIDs,
      final ErrorLogUtil util) throws BusinessException {
    String cond = BDSqlInUtil.formInSQLWithoutAnd(MaterialVO.PK_MATERIAL, pks, false);
    cond += " " + BDSqlInUtil.formInSQL(MaterialVO.PK_ORG, funcPermissionOrgIDs, false);
    String sql =
        "select " + MaterialVO.PK_MATERIAL + " from " + MaterialVO.getDefaultTableName()
            + " where " + cond;
    List<String> col = (List<String>) getBaseDAO().executeQuery(sql, new ColumnListProcessor());
    return col.toArray(new String[0]);
  }

  private BaseDAO getBaseDAO() {
    if (this.baseDAO == null) {
      this.baseDAO = new BaseDAO();
    }
    return this.baseDAO;
  }

  private String getFuncPermissionErrorMsg(MaterialVO vo) {
    return nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("10140mag", "010140mag0125", null,
        new String[] {vo.getCode()})
    /* @res "编码[{0}]的物料不是当前节点有权限组织的数据。" */;
  }

  public IMarOrgService getMarOrgService() {
    if (this.marOrgService == null) {
      this.marOrgService = NCLocator.getInstance().lookup(IMarOrgService.class);
    }
    return this.marOrgService;
  }

}

package nc.impl.cm.allocate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.db.CMSqlBuilder;
import nc.bs.cm.allocate.cancel.AllocateCancel;
import nc.bs.cm.allocate.execute.executor.AllocateExecutorFactory;
import nc.bs.cm.allocate.execute.executor.IAllocateExecute;
import nc.bs.cm.allocate.persistent.AllocateStatusPersistent;
import nc.bs.cm.allocate.util.AllocateScaleUtil;
import nc.bs.cm.allocate.util.OtherStatusCheckUtil;
import nc.bs.cm.pub.framework.CMAlgorithmBaseFramework;
import nc.bs.framework.common.IAttributeManager;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.framework.reqattribute.ReqAttributeManager;
import nc.impl.pubapp.pattern.data.vo.VOInsert;
import nc.impl.pubapp.pattern.database.DataAccessUtils;
import nc.itf.cm.allocate.IAllocateExecuteService;
import nc.itf.cm.allocdef.IAllocdefPubQueryService;
import nc.itf.framework.ejb.CMTProxy;
import nc.pub.billcode.BillCodeReturnExecutor;
import nc.pubitf.cm.costdataclose.cm.allocate.IAppronumRewriteForAllocateService;
import nc.pubitf.cm.costinglevel.ICostingLevelForPubService;
import nc.vo.cm.allocate.entity.AllocateCalcParamBean;
import nc.vo.cm.allocate.entity.AllocateLangConst;
import nc.vo.cm.allocate.entity.AllocateStatusEnum;
import nc.vo.cm.allocate.entity.AllocateStatusVO;
import nc.vo.cm.allocdef.entity.AllocdefAggVO;
import nc.vo.cm.allocdef.entity.AllocdefHeadVO;
import nc.vo.cm.autocostcalculation.enumeration.TaskItemEnum;
import nc.vo.cmpub.framework.scale.CMScaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.data.IRowSet;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 事务执行的实现类
 *
 * @author liyjf
 */
public class AllocateExecuteServiceImpl extends CMAlgorithmBaseFramework implements IAllocateExecuteService {

    @Override
    protected int getTaskItem() {
        return TaskItemEnum.ALLOCATE.toIntValue();
    }

    @Override
    public void executeAllocate(AllocateStatusVO[] allocstatusVOs) throws BusinessException {
        String pkOrg = allocstatusVOs[0].getPk_org();
        String period = allocstatusVOs[0].getCperiod();

        Class<?>[] parameterTypes = new Class<?>[1];
        parameterTypes[0] = AllocateStatusVO[].class;

        Object[] param = new Object[1];
        param[0] = allocstatusVOs;

        this.doExecute(pkOrg, period, "doExecuteAllocate", parameterTypes, param);
    }

    public void doExecuteAllocate(AllocateStatusVO[] allocstatusVOs) throws BusinessException {
        try {
            if (CMArrayUtil.isEmpty(allocstatusVOs)) {
                return;
            }
            String pk_group = allocstatusVOs[0].getPk_group();
            String pk_org = allocstatusVOs[0].getPk_org();
            String period = allocstatusVOs[0].getCperiod();

            // 获取业务日期(期间最后一天)
            UFDate endOfPeriod = BDAdapter.getEndDateByPeriod(pk_org, period);
            // 获取精度
            CMScaleVO scaleVO = new AllocateScaleUtil().queryCurrdigit(pk_org);
            Integer currdigit = Integer.valueOf(scaleVO.getMoneyScale());
            int currRoundType = BDAdapter.getCurrencyRoundType(pk_org);

            // 分配执行前检查
//            this.checkBeforeExecuteAllocateRule(allocstatusVOs);
            // 获取定义实例
            Map<String, AllocdefAggVO> statusRelationAllocDefMap = this.getAllocateDef(allocstatusVOs);
            // 分配工厂
            AllocateExecutorFactory fatory = new AllocateExecutorFactory();

            // 删除结转层次
            this.deleteLevelRequires(pk_group, pk_org, period);

            IAttributeManager reqAttributeManager = new ReqAttributeManager().getBillNOReqAttributeManager();
            for (AllocateStatusVO allocstatusVO : allocstatusVOs) {
                AllocdefAggVO aggVo = statusRelationAllocDefMap.get(allocstatusVO.getCallocdefid());
                if (aggVo == null) {
                    ExceptionUtils.wrappBusinessException(AllocateLangConst.getERR_NO_ALLOCDEF());
                }

                AllocateCalcParamBean calcBean = new AllocateCalcParamBean();
                calcBean.setPkOrg(pk_org);
                calcBean.setCperiod(period);
                calcBean.setBusinessDate(endOfPeriod);
                calcBean.setAggvo(aggVo);
                calcBean.setStatusvo(allocstatusVO);
                calcBean.setCurrdigit(currdigit);
                calcBean.setCurrRoundType(currRoundType);

                Method method = this.getMethod(this);
                Object[] param = new Object[2];
                param[0] = calcBean;
                param[1] = fatory;
                NCLocator.getInstance().lookup(CMTProxy.class).delegate_RequiresNew(this, method, param);
                reqAttributeManager.setAttribute(BillCodeReturnExecutor.REQ_REGION_BILLCODE, null);
            }
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    private void checkBeforeExecuteAllocateRule(AllocateStatusVO[] allocstatusVOS) throws BusinessException {
        String pkGroup = allocstatusVOS[0].getPk_group();
        String pkOrg = allocstatusVOS[0].getPk_org();
        String period = allocstatusVOS[0].getCperiod();
        OtherStatusCheckUtil checkUtil = new OtherStatusCheckUtil();
        // 检查关帐
        if (!checkUtil.isCloseAccount(pkGroup, pkOrg, period)) {
            throw new BusinessException(AllocateLangConst.getERR_NO_DATACLOSE());
        }
        // 检查结转
        if (checkUtil.isCostTran(pkGroup, pkOrg, period)) {
            throw new BusinessException(AllocateLangConst.getERR_IS_TRAN());
        }
        // 检查结账
        if (checkUtil.isAccountSettled(pkGroup, pkOrg, period)) {
            throw new BusinessException(AllocateLangConst.getERR_ACCOUNT_SETTLED());
        }
    }

    private Method getMethod(Object target) {
        Class<?>[] parameterTypes = new Class<?>[2];
        parameterTypes[0] = AllocateCalcParamBean.class;
        parameterTypes[1] = AllocateExecutorFactory.class;
        Method method = null;
        try {
            method = target.getClass().getDeclaredMethod("allocateExecute", parameterTypes);
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return method;
    }

    public void allocateExecute(AllocateCalcParamBean calcBean, AllocateExecutorFactory fatory)
            throws BusinessException {
        // 检查执行状态（&初始化）
        if (!this.sendCheckAllocateStatusRequire(calcBean.getStatusvo())) {
            return;
        }

        // 根据聚合vo的表头的分配方法，创建不同的分配实例
        IAllocateExecute allocateExecute = fatory.createAllocateExecutor(calcBean.getAggvo());

        // 执行分配
        allocateExecute.doAllocateExecute(calcBean);

        // 保存分配状态
        this.saveAllocateStatus(new AllocateStatusVO[] {
            calcBean.getStatusvo()
        });
    }

    private boolean sendCheckAllocateStatusRequire(AllocateStatusVO statusVO) throws BusinessException {
        Class<?>[] parameterTypes = new Class<?>[1];
        parameterTypes[0] = AllocateStatusVO.class;
        Method method = null;
        Object result = null;
        try {
            method = this.getClass().getMethod("checkAllocateStatus", parameterTypes);

            Object[] param = new Object[1];
            param[0] = statusVO;
            result = NCLocator.getInstance().lookup(CMTProxy.class).delegate_RequiresNew(this, method, param);
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return (boolean) result;
    }

    public boolean checkAllocateStatus(AllocateStatusVO statusVO) throws BusinessException {
        CMSqlBuilder sql = new CMSqlBuilder();
        sql.select();
        sql.append(AllocateStatusVO.FSTATUS);
        sql.from(AllocateStatusVO.getDefaultTableName());
        sql.where();
        sql.append(AllocateStatusVO.PK_ORG, statusVO.getPk_org());
        sql.and();
        sql.append(AllocateStatusVO.CPERIOD, statusVO.getCperiod());
        sql.and();
        sql.append(AllocateStatusVO.CALLOCDEFID, statusVO.getCallocdefid());

        DataAccessUtils util = new DataAccessUtils();
        IRowSet rows = util.query(sql.toString());

        if (rows.next()) {
            if (AllocateStatusEnum.ALLOCATED_INT == rows.getInt(0)) {
                return false;
            }
        }
        else {
            statusVO.setFstatus(AllocateStatusEnum.UNALLOCATE_INT);
            new VOInsert<AllocateStatusVO>().insert(new AllocateStatusVO[] {
                    statusVO
            });
        }
        return true;
    }

    public void deleteLevelRequires(String pkGroup, String pkOrg, String cperiod) throws BusinessException {
        Class<?>[] parameterTypes = new Class<?>[3];
        parameterTypes[0] = String.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = String.class;
        Method method = null;
        try {
            method = this.getClass().getMethod("deleteLevel", parameterTypes);

            Object[] param = new Object[3];
            param[0] = pkGroup;
            param[1] = pkOrg;
            param[2] = cperiod;
            NCLocator.getInstance().lookup(CMTProxy.class).delegate_RequiresNew(this, method, param);
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
    }

    public void deleteLevel(String pkGroup, String pkOrg, String cperiod) throws BusinessException {
        NCLocator.getInstance().lookup(ICostingLevelForPubService.class).deleteLevels(pkGroup, pkOrg, cperiod);
    }

    /**
     * 保存事务执行状态
     *
     * @param allocStatus
     *            事务执行状态
     */
    private void saveAllocateStatus(AllocateStatusVO[] allocStatusVOs) {
        AllocateStatusPersistent tsp = new AllocateStatusPersistent();
        tsp.saveStatus(allocStatusVOs, AllocateStatusEnum.ALLOCATED.toIntValue());
    }

    /**
     * 获得事务分配定义的Map
     *
     * @param allocstatusVOS
     *            分配状态VO数组
     * @return 事务分配定义的Map
     */
    private Map<String, AllocdefAggVO> getAllocateDef(AllocateStatusVO[] allocstatusVOS) throws BusinessException {
        String[] allocteDefIDs = new String[allocstatusVOS.length];
        for (int i = 0; i < allocstatusVOS.length; i++) {
            allocteDefIDs[i] = allocstatusVOS[i].getCallocdefid();
        }

        IAllocdefPubQueryService queryService = NCLocator.getInstance().lookup(IAllocdefPubQueryService.class);

        AllocdefAggVO[] allocDefAggVos = queryService.queryTransactionDef(allocteDefIDs);
        Map<String, AllocdefAggVO> defMap = new HashMap<String, AllocdefAggVO>();
        if (CMArrayUtil.isEmpty(allocDefAggVos)) {
            return defMap;
        }
        for (AllocdefAggVO allocdefAggVO : allocDefAggVos) {
            defMap.put(((AllocdefHeadVO) allocdefAggVO.getParentVO()).getCallocdefid(), allocdefAggVO);
        }
        return defMap;
    }

    @Override
    public void cancelAllocate(AllocateStatusVO[] allocstatusVOs) throws BusinessException {
        String pkOrg = allocstatusVOs[0].getPk_org();
        String period = allocstatusVOs[0].getCperiod();

        Class<?>[] parameterTypes = new Class<?>[1];
        parameterTypes[0] = AllocateStatusVO[].class;

        Object[] param = new Object[1];
        param[0] = allocstatusVOs;

        this.doCancelExecute(pkOrg, period, "doCancelAllocate", parameterTypes, param);
    }

    public void doCancelAllocate(AllocateStatusVO[] allocstatusVOs) throws BusinessException {
        try {
            // 按照倒序进行取消
            this.invertedOrder(allocstatusVOs);

            String pk_group = allocstatusVOs[0].getPk_group();
            String pk_org = allocstatusVOs[0].getPk_org();
            String period = allocstatusVOs[0].getCperiod();

            // 取消分配前检查
//            this.checkBeforeCancelAllocateRule(allocstatusVOs);
            AllocateCancel cancel = new AllocateCancel();

            List<String> allocDefIdList = new ArrayList<String>();
            for (AllocateStatusVO status : allocstatusVOs) {
                allocDefIdList.add(status.getCallocdefid());
            }
            cancel.cancelAllocate(allocstatusVOs);

            // 删除约当产量表数据
            NCLocator.getInstance().lookup(IAppronumRewriteForAllocateService.class)
                    .deleteAppronumByUnAlloc(pk_org, period, allocDefIdList.toArray(new String[allocDefIdList.size()]));

            // 删除结转层次
            NCLocator.getInstance().lookup(ICostingLevelForPubService.class).deleteLevels(pk_group, pk_org, period);
        }
        catch (Exception e) {
            ExceptionUtils.marsh(e);
        }
    }

    private void checkBeforeCancelAllocateRule(AllocateStatusVO[] allocstatusVOS) throws BusinessException {
        String pkGroup = allocstatusVOS[0].getPk_group();
        String pkOrg = allocstatusVOS[0].getPk_org();
        String period = allocstatusVOS[0].getCperiod();
        OtherStatusCheckUtil checkUtil = new OtherStatusCheckUtil();
        // 检查结转
        if (checkUtil.isCostTran(pkGroup, pkOrg, period)) {
            throw new BusinessException(AllocateLangConst.getERR_IS_TRAN());
        }
        // 检查结账
        if (checkUtil.isAccountSettled(pkGroup, pkOrg, period)) {
            throw new BusinessException(AllocateLangConst.getERR_ACCOUNT_SETTLED());
        }
    }

    /**
     * 倒序
     *
     * @param allocstatusVOS
     *            AllocStatus
     */
    private void invertedOrder(AllocateStatusVO[] allocstatusVOS) {
        Arrays.sort(allocstatusVOS, new Comparator<AllocateStatusVO>() {

            @Override
            public int compare(AllocateStatusVO o1, AllocateStatusVO o2) {
                return o2.getNseq().compareTo(o1.getNseq());
            }
        });
    }
}

package nc.bs.cm.allocate.execute.executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMMapUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.base.CMValueCheck;
import nc.bs.cm.allocate.execute.calculate.DataAllocateComponent;
import nc.bs.cm.allocate.execute.data.output.StuffOutputVO;
import nc.bs.cm.allocate.execute.executor.distributed.DefaultAllocDistributedExecStrategy;
import nc.bs.cm.allocate.execute.strategy.AbstractAllocateStrategy;
import nc.bs.cm.allocate.execute.strategy.CTOAllocateStrategy;
import nc.bs.cm.allocate.execute.strategy.IAllocateStrategy;
import nc.bs.cm.allocate.persistent.AllocateDatePersistent;
import nc.bs.cm.allocate.persistent.AllocateRecordPersistent;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.CMSysInitAdapter;
import nc.cmpub.business.adapter.FIAdapter;
import nc.cmpub.business.cons.CMSysInitParamConst;
import nc.cmpub.business.enumeration.CMAllocStatusEnum;
import nc.cmpub.business.enumeration.CMSourceTypeEnum;
import nc.cmpub.framework.batchlimit.MABatchLimitEnum;
import nc.cmpub.framework.batchlimit.MABatchLimitUtil;
import nc.cmpub.framework.distributed.DistributedExcuteProcessor;
import nc.cmpub.framework.exception.CMExceptionUtil;
import nc.pubitf.bd.bdactivity.pub.IBDActivityPubQuery;
import nc.pubitf.cm.costdataclose.cm.allocate.IAppronumRewriteForAllocateService;
import nc.pubitf.cm.costobject.cm.pub.ICostObjectQueryForPub;
import nc.pubitf.cm.stuff.cm.allocate.IStuffQueryForAllocate;
import nc.pubitf.cm.stuff.cm.allocate.IStuffRewriteForAllocate;
import nc.pubitf.uapbd.MeasdocUtil;
import nc.vo.bd.bdactivity.entity.BDActivityVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.cm.allocate.entity.AllocateCalcParamBean;
import nc.vo.cm.allocate.entity.AllocateLangConst;
import nc.vo.cm.allocate.entity.IAllocateTargetVO;
import nc.vo.cm.allocate.entity.activitycost.ActivityCostAggVO;
import nc.vo.cm.allocate.entity.activitycost.ActivityCostItemVO;
import nc.vo.cm.allocate.entity.record.AllocateRecordAgg;
import nc.vo.cm.allocate.entity.record.AllocateRecordCollectVO;
import nc.vo.cm.allocate.entity.record.AllocateRecordHeadVO;
import nc.vo.cm.allocate.entity.record.AllocateRecordItemVO;
import nc.vo.cm.allocdef.entity.AllocdefAggVO;
import nc.vo.cm.allocdef.entity.AllocdefHeadVO;
import nc.vo.cm.allocdef.entity.CelementItemVO;
import nc.vo.cm.costdataclose.entity.CDClose4AllocParamVO;
import nc.vo.cm.costobject.entity.CostObjectConst;
import nc.vo.cm.costobject.entity.CostObjectVO;
import nc.vo.cm.stuff.entity.StuffAggVO;
import nc.vo.cm.stuff.entity.StuffHeadVO;
import nc.vo.cm.stuff.entity.StuffItemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapList;
import nc.vo.resa.factor.FactorVO;

/**
 * 分配的基类
 *
 * @author liyjf
 */
public abstract class AbstractAllocateExecute implements IAllocateExecute {
    private String[] centerids;

    private String[] elementids;

    private String appronumParam = null;

    public String getAppronumParam(String pk_org) throws BusinessException {
        if (CMStringUtil.isEmpty(this.appronumParam)) {
            this.appronumParam =
                    CMSysInitAdapter.sysInitQry().getParaString(pk_org, CMSysInitParamConst.CALCULATE_APPRONUM);
        }
        return this.appronumParam;
    }

    @Override
    public void doAllocateExecute(AllocateCalcParamBean calcBean) throws BusinessException {
        // 构建分配策略类
        IAllocateStrategy strategy = this.builderAllocateStrategy(calcBean.getAggvo(), calcBean.getCperiod());
        AllocdefHeadVO defHeadVO = (AllocdefHeadVO) calcBean.getAggvo().getParentVO();
        ((AbstractAllocateStrategy) strategy).setCostType(defHeadVO.getCcosttype());
        ((AbstractAllocateStrategy) strategy).setEndOfPeriod(calcBean.getBusinessDate());

        String[] centerIds = this.getSendCenterids(defHeadVO);

        // 排除CTO不分配最终成本对象范围的影响
        boolean isCTOAlloc = false;
        if (strategy instanceof CTOAllocateStrategy) {
            isCTOAlloc = true;
        }
        // 检查辅助服务状态(如果存在非仓储转移单的话,必须先进行辅助服务分配)
        this.checkHasStuffShift(calcBean.getCperiod(), centerIds,
                this.getElementids(calcBean.getAggvo(), calcBean.getBusinessDate()), calcBean.getPkOrg(), isCTOAlloc);

        // 获得待分配对象 在具体的分配类实现
        AllocateRecordHeadVO[] beAllocateVOs =
                this.getBeAllocateDatas(calcBean.getAggvo(), calcBean.getCperiod(), calcBean.getBusinessDate(),
                        centerIds, false);

        if (CMArrayUtil.isEmpty(beAllocateVOs)) {
            return;
        }
        // 设置默认值。
        this.setAllocateRecordDefaultValue(calcBean.getAggvo(), calcBean.getCperiod(), beAllocateVOs,
                calcBean.getCurrdigit(), calcBean.getCurrRoundType());

        List<AllocateRecordHeadVO> groupList = new ArrayList<AllocateRecordHeadVO>();
        int limit = MABatchLimitUtil.getInstance().getBatchLimit(MABatchLimitEnum.ALLOCATE_EXECUTE_LIMIT, -1);
        if (limit == -1) {
            for (AllocateRecordHeadVO beAllocateVO : beAllocateVOs) {
                groupList.add(beAllocateVO);
            }
            // 分配数据
            this.allocateData(calcBean, groupList, strategy);
        }
        else {
            calcBean.setAllocateType(this.getAllocateType());
            calcBean.setCsendcenterids(this.centerids);
            calcBean.setCsendelementids(this.elementids);
            List<List<AllocateRecordHeadVO>> dataList = new ArrayList<List<AllocateRecordHeadVO>>();
            for (AllocateRecordHeadVO beAllocateVO : beAllocateVOs) {
                groupList.add(beAllocateVO);
                if (groupList.size() == limit) {
                    dataList.add(groupList);
                    groupList = new ArrayList<AllocateRecordHeadVO>();
                }
            }
            if (groupList.size() > 0) {
                dataList.add(groupList);
            }

            new DistributedExcuteProcessor<List<AllocateRecordHeadVO>>(new DefaultAllocDistributedExecStrategy())
            .batchExecute(calcBean, dataList);
        }
    }

    /**
     * 设置默认值
     *
     * @param aggvo
     *            事务定义VO
     * @param period
     *            会计期间
     * @param beAllocateDataAggs
     *            待分配VO
     */
    protected void setAllocateRecordDefaultValue(AllocdefAggVO aggvo, String period,
            AllocateRecordHeadVO[] beAllocateDatas, Integer currdigit, int currRoundType) throws BusinessException {
        AllocdefHeadVO defHead = (AllocdefHeadVO) aggvo.getParentVO();
        for (AllocateRecordHeadVO headVo : beAllocateDatas) {
            // 分配事务id
            headVo.setCallocdefid(defHead.getCallocdefid());
            // 会计期间
            headVo.setCperiod(period);

            // 获取单位、本位币精度信息
            headVo.setMoneyscale(currdigit.intValue());
            headVo.setMoneyroundtype(currRoundType);

            Integer[] numscales = MeasdocUtil.getInstance().getPrecisionByPks(new String[] {
                    headVo.getCmeasdocid()
            });
            if (CMArrayUtil.isNotEmpty(numscales) && numscales[0] != null) {
                headVo.setNumscale(numscales[0].intValue());
            }
        }

    }

    /**
     * 设置来源单据状态
     *
     * @param beAllocateDatas
     *            汇总后的待分配对象
     */
    protected void setSourceStatus(List<AllocateRecordAgg> beAllocateDatas) throws BusinessException {
        Set<String> itemIdSet = new HashSet<String>();
        AllocateRecordHeadVO defheadvo = (AllocateRecordHeadVO) beAllocateDatas.get(0).getParentVO();
        IStuffQueryForAllocate stuffQuery = NCLocator.getInstance().lookup(IStuffQueryForAllocate.class);
        // 修改消耗单 来源ID确认算法
        MapList<String, String[]> collectctIdMap =
                stuffQuery.queryStuffCollectIds(defheadvo.getPk_org(), defheadvo.getCperiod(), this.centerids,
                        this.elementids);
        for (AllocateRecordAgg beAllocateData : beAllocateDatas) {
            List<AllocateRecordCollectVO> collectList = new ArrayList<AllocateRecordCollectVO>();
            // 获取关联ID 生成CollectVO
            AllocateRecordHeadVO headvo = (AllocateRecordHeadVO) beAllocateData.getParentVO();
            if (CMStringUtil.isNotEmpty(headvo.getCsourcebill_bid())) {
                itemIdSet.add(headvo.getCsourcebill_bid());
                AllocateRecordCollectVO collectVo = new AllocateRecordCollectVO();
                collectVo.setPk_group(headvo.getPk_group());
                collectVo.setPk_org(headvo.getPk_org());
                collectVo.setPk_org_v(headvo.getPk_org_v());
                collectVo.setCsourcebillid(headvo.getCsourcebillid());
                collectVo.setCsourcebill_bid(headvo.getCsourcebill_bid());
                collectVo.setIsourcetype(Integer.valueOf(CMSourceTypeEnum.STUFF.getEnumValue().getValue().toString()));
                collectList.add(collectVo);
            }
            else {
                List<String> sourcebidList = new ArrayList<String>();
                AllocateRecordItemVO[] children =
                        (AllocateRecordItemVO[]) beAllocateData.getChildren(AllocateRecordItemVO.class);
                for (AllocateRecordItemVO child : children) {
                    sourcebidList.add(child.getCsourcebill_bid());
                }

                String key =
                        headvo.getCcostcenterid() + headvo.getCcostobjectid() + headvo.getCsrccostdomainid()
                        + headvo.getCelementid() + headvo.getCmaterialid() + headvo.getCcustomerid()
                        + headvo.getCvendorid() + headvo.getCproductorid() + headvo.getCprojectid()
                        + headvo.getVbfree1() + headvo.getVbfree2() + headvo.getVbfree3() + headvo.getVbfree4()
                        + headvo.getVbfree5() + headvo.getVbfree6() + headvo.getVbfree7() + headvo.getVbfree8()
                        + headvo.getVbfree9() + headvo.getVbfree10() + headvo.getBfromcostcenter();
                if (null == headvo.getNalloctmoney()) {
                    key = key + "Null";
                }
                List<String[]> itemIdList = collectctIdMap.get(key);
                if (CMCollectionUtil.isEmpty(itemIdList)) {
                    throw new BusinessException(AllocateLangConst.getERR_NO_SOURCE());
                }

                for (String[] itemId : itemIdList) {
                    if (!sourcebidList.contains(itemId[1])) {
                        itemIdSet.add(itemId[1]);
                        AllocateRecordCollectVO collectVo = new AllocateRecordCollectVO();
                        collectVo.setPk_group(headvo.getPk_group());
                        collectVo.setPk_org(headvo.getPk_org());
                        collectVo.setPk_org_v(headvo.getPk_org_v());
                        collectVo.setCsourcebillid(itemId[0]);
                        collectVo.setCsourcebill_bid(itemId[1]);
                        collectVo.setIsourcetype(Integer.valueOf(CMSourceTypeEnum.STUFF.getEnumValue().getValue()
                                .toString()));
                        collectList.add(collectVo);
                    }
                }

                if (CMCollectionUtil.isEmpty(itemIdSet)) {
                    throw new BusinessException(AllocateLangConst.getERR_NO_SOURCE());
                }
            }
            beAllocateData.setChildren(AllocateRecordCollectVO.class,
                    collectList.toArray(new AllocateRecordCollectVO[0]));
        }
        // 设置消耗单状态
        if (CMCollectionUtil.isNotEmpty(itemIdSet)) {
            this.setStuffStatus(itemIdSet.toArray(new String[0]), CMAllocStatusEnum.ALLOCATE);
        }
    }

    private void setStuffStatus(String[] itemids, CMAllocStatusEnum allocState) throws BusinessException {
        IStuffRewriteForAllocate service = NCLocator.getInstance().lookup(IStuffRewriteForAllocate.class);

        service.setBodyAllocStatus(itemids, allocState);
    }

    /**
     * 保存待分配对象
     *
     * @param beAllocateDatas
     *            汇总后的待分配对象
     */
    protected void saveAllocateSourceRelation(List<AllocateRecordAgg> beAllocateDatas) {
        // 保存数据 ，没有其他的业务处理
        AllocateRecordPersistent recordPersistent = new AllocateRecordPersistent();
        recordPersistent.save(beAllocateDatas);
    }

    /**
     * 驱动计算约当产量表
     *
     * @param beAllocateDatas
     *            汇总后的待分配对象
     */
    protected void insertAppronum(String pk_org, List<AllocateRecordAgg> beAllocateDatas) throws BusinessException {

        if (!this.needInsertAppronum(pk_org)) {
            return;
        }

        // 构造参数
        Map<String, CDClose4AllocParamVO> paramvomap = this.createAppronumParam(beAllocateDatas);
        // 计算约当产量表
        NCLocator.getInstance().lookup(IAppronumRewriteForAllocateService.class)
        .insertAppronumByAlloc(paramvomap.values().toArray(new CDClose4AllocParamVO[0]));
    }

    protected boolean needInsertAppronum(String pk_org) throws BusinessException {
        return CMSysInitParamConst.CALCULATE_APPRONUM_WORTH.equals(this.getAppronumParam(pk_org));
    }

    private Map<String, CDClose4AllocParamVO> createAppronumParam(List<AllocateRecordAgg> beAllocateDatas) {
        Map<String, CDClose4AllocParamVO> paramvomap = new HashMap<String, CDClose4AllocParamVO>();

        for (AllocateRecordAgg beAllocateData : beAllocateDatas) {
            AllocateRecordHeadVO headvo = (AllocateRecordHeadVO) beAllocateData.getParentVO();
            String pk_org = headvo.getPk_org();
            String period = headvo.getCperiod();
            String defId = headvo.getCallocdefid();

            ISuperVO[] vos = beAllocateData.getChildren(AllocateRecordItemVO.class);
            for (ISuperVO vo : vos) {
                if (((AllocateRecordItemVO) vo).getTargetVO() instanceof StuffOutputVO) {
                    AllocateRecordItemVO itemvo = (AllocateRecordItemVO) vo;
                    String key = itemvo.getItemcostcenter() + itemvo.getItemcostobjid() + itemvo.getItemelementsid();
                    if (!paramvomap.containsKey(key)) {
                        CDClose4AllocParamVO paramvo = new CDClose4AllocParamVO();
                        paramvo.setPk_org(pk_org);
                        paramvo.setCperiod(period);
                        paramvo.setCcostcenterid(itemvo.getItemcostcenter());
                        paramvo.setCcostobjectid(itemvo.getItemcostobjid());
                        paramvo.setCelementid(itemvo.getItemelementsid());
                        paramvo.setCallocdefid(defId);
                        paramvomap.put(key, paramvo);
                    }
                }
            }
        }
        return paramvomap;
    }

    /**
     * 构建分配策略类
     *
     * @param aggvo
     *            分配定义VO
     * @param period
     *            会计期间
     * @return 分配策略类
     */
    protected abstract IAllocateStrategy builderAllocateStrategy(AllocdefAggVO aggvo, String period)
            throws BusinessException;

    /**
     * 分配数据
     *
     * @param strategy
     *            分配策略类
     * @param beAllocateDataAggs
     *            待分配对象
     * @param businessDate
     *            业务日期
     */
    public void allocateData(AllocateCalcParamBean calcBean, List<AllocateRecordHeadVO> beAllocateVOs,
            IAllocateStrategy strategy) throws BusinessException {
        DataAllocateComponent dataAllcator = new DataAllocateComponent();

        List<Object> allocate2AllList = new ArrayList<Object>();
        List<AllocateRecordAgg> aggList = new ArrayList<AllocateRecordAgg>();
        String message = "";
        for (AllocateRecordHeadVO beAllocateHeadVO : beAllocateVOs) {
            try {
//            	//liyf 测试  celementid  核算要素   :50010571  物料搬运
//            	if(!"1001V110000000000WTO".equalsIgnoreCase(beAllocateHeadVO.getCelementid())){
//            		continue;
//            	}
                AllocateRecordAgg beAllocateDataAgg = new AllocateRecordAgg();
                beAllocateDataAgg.setParent(beAllocateHeadVO);
                aggList.add(beAllocateDataAgg);

                AllocateRecordItemVO[] allocate2Infos =
                        strategy.getReceiveInfos(beAllocateHeadVO, calcBean.getBusinessDate());
                if (CMArrayUtil.isEmpty(allocate2Infos)) {
                    throw new BusinessException(AllocateLangConst.getERR_NO_RECEIVEINFO1()
                            + ((AllocdefHeadVO) strategy.getAggvo().getParent()).getVcode()
                            + AllocateLangConst.getERR_NO_RECEIVEINFO2());
                }

                // 用于过滤分配结果为0的数据
                List<AllocateRecordItemVO> allocate2InfoList = new ArrayList<AllocateRecordItemVO>();
                for (AllocateRecordItemVO allocate2Info : allocate2Infos) {
                    allocate2InfoList.add(allocate2Info);
                }
                // 分配数据
                List<Object> allocate2List = dataAllcator.allcateData(beAllocateHeadVO, allocate2InfoList, strategy);
                beAllocateDataAgg.setChildren(AllocateRecordItemVO.class,
                        allocate2InfoList.toArray(new AllocateRecordItemVO[0]));

                allocate2AllList.addAll(allocate2List);
            }
            catch (Exception ex) {
                int debugMode = MABatchLimitUtil.getInstance().getBatchLimit(MABatchLimitEnum.CM_DEBUG_MODE, 0);
                if (debugMode == 0) {
                    message =
                            message
                            + this.getErrInfo(beAllocateHeadVO, CMExceptionUtil.getBusinessExceptionMessage(ex))
                            + "\r\n";
                }
                else {
                    ExceptionUtils.wrappException(ex);
                }
            }
        }
        if (message != "") {
            ExceptionUtils.wrappBusinessException(message);
        }

        // 合并单据(消耗单)
        List<Object> allocateBillList = this.combinBill(allocate2AllList);

        // 设置来源类型、及计算单价(消耗单)
        this.setResourceType(allocateBillList, strategy);

        // 保存分配对象
        this.saveAllocate2Object(allocateBillList);

        // 设置分配结果表体数据
        this.setAllocateItemData(aggList);

        // 设置来源单据状态
        this.setSourceStatus(aggList);

        // 保存待分配对象
        this.saveAllocateSourceRelation(aggList);

        // 驱动计算约当产量表
        this.insertAppronum(calcBean.getPkOrg(), aggList);
    }

    private void setResourceType(List<Object> allocate2AllList, IAllocateStrategy strategy) {
        if (allocate2AllList.isEmpty()) {
            return;
        }
        // 未定义类型的事务分配，不计来源(CTC完工)
        if (strategy.getResourceType() == null) {
            return;
        }
        // 消耗单处理
        if (allocate2AllList.get(0).getClass().equals(StuffAggVO.class)) {
            for (Object stuffaggvo : allocate2AllList) {
                StuffHeadVO head = ((StuffAggVO) stuffaggvo).getParentVO();
                head.setIsourcetype(strategy.getResourceType());
                // head.setVnote(strategy.getResourceTypeShow());

                // 计算单价
                StuffItemVO[] items = (StuffItemVO[]) ((StuffAggVO) stuffaggvo).getChildrenVO();
                for (StuffItemVO item : items) {
                    if (item.getNmoney() == null || item.getNnum() == null || item.getNnum().equals(UFDouble.ZERO_DBL)) {
                        continue;
                    }
                    item.setNprice(item.getNmoney().div(item.getNnum()));
                }
            }
        }
        else if (allocate2AllList.get(0).getClass().equals(ActivityCostAggVO.class)) {
            for (Object activitycostaggvo : allocate2AllList) {
                // 计算单价
                ActivityCostItemVO[] items =
                        (ActivityCostItemVO[]) ((ActivityCostAggVO) activitycostaggvo).getChildrenVO();
                for (ActivityCostItemVO item : items) {
                    if (item.getNmoney() == null || item.getNnum() == null || item.getNnum().equals(UFDouble.ZERO_DBL)) {
                        continue;
                    }
                    item.setNprice(item.getNmoney().div(item.getNnum()));
                }
            }
        }
    }

    protected void setAllocateItemData(List<AllocateRecordAgg> aggList) {
        for (AllocateRecordAgg beAllocateData : aggList) {
            AllocateRecordItemVO[] items =
                    (AllocateRecordItemVO[]) beAllocateData.getChildren(AllocateRecordItemVO.class);

            for (AllocateRecordItemVO item : items) {
                IAllocateTargetVO<?> allcateTargetvo = item.getTargetVO();
                // 来源设置
                item.setIsourcetype((Integer) allcateTargetvo.getSourceType().value());

                // 来源ID
                item.setCsourcebillid(allcateTargetvo.getSourceID());
                item.setCsourcebill_bid(allcateTargetvo.getSourceBID());

                // 数量，金额
                item.setNallocnum(allcateTargetvo.getNnum());
                item.setNallocmoney(allcateTargetvo.getNmoney());
                item.setNnum(allcateTargetvo.getNnum());
                item.setNmoney(allcateTargetvo.getNmoney());

                item.setTargetVO(null);
            }
        }
    }

    /**
     * 合并单据(消耗单)
     *
     * @param allocate2AllList
     * @return
     */
    protected List<Object> combinBill(List<Object> allocate2AllList) {
        if (allocate2AllList.isEmpty()) {
            return allocate2AllList;
        }
        List<Object> allocateBillList = new ArrayList<Object>();
        Map<String, StuffAggVO> allocateBillMap = new HashMap<String, StuffAggVO>();
        for (Object vo : allocate2AllList) {
            // 只支持消耗单单据合并(表头合并)
            if (!vo.getClass().equals(StuffAggVO.class)) {
                allocateBillList.add(vo);
                continue;
            }
            StuffHeadVO head = ((StuffAggVO) vo).getParentVO();
            String key = this.getUnionKey(head);
            StuffAggVO oldStuffAggVO = allocateBillMap.get(key);
            if (oldStuffAggVO == null) {
                allocateBillMap.put(key, (StuffAggVO) vo);
            }
            else {
                List<StuffItemVO> stuffItemList = new ArrayList<StuffItemVO>();
                for (ISuperVO stuffItemVO : oldStuffAggVO.getChildren(StuffItemVO.class)) {
                    stuffItemList.add((StuffItemVO) stuffItemVO);
                }
                for (ISuperVO stuffItemVO : ((StuffAggVO) vo).getChildren(StuffItemVO.class)) {
                    StuffItemVO item = (StuffItemVO) stuffItemVO;
                    stuffItemList.add(item);
                }

                oldStuffAggVO.setChildren(StuffItemVO.class,
                        stuffItemList.toArray(new StuffItemVO[stuffItemList.size()]));
            }
        }
        for (Entry<String, StuffAggVO> entry : allocateBillMap.entrySet()) {
            allocateBillList.add(entry.getValue());
        }
        return allocateBillList;
    }

    private String getUnionKey(StuffHeadVO headInfo) {
        return headInfo.getPk_org() + headInfo.getCcostcenterid() + headInfo.getCcostobjectid()
                + headInfo.getIoriginalsourcetype();
    }

    /**
     * 保存分配的对象
     *
     * @param allocate2AllList
     *            分配对象的List
     */
    private void saveAllocate2Object(List<Object> allocate2AllList) throws BusinessException {
        new AllocateDatePersistent().saveAllocateVOs(allocate2AllList);
    }

    /**
     * 获得待分配数据
     *
     * @param aggvo
     *            分配定义VO
     * @param period
     *            会计期间
     * @param businessDate
     *            业务日期
     * @return 待分配数据
     */
    @Override
    public AllocateRecordHeadVO[] getBeAllocateDatasForClient(String pkOrg, String period, AllocdefAggVO aggvo)
            throws BusinessException {
        // 获取业务日期(期间最后一天)
        UFDate endOfPeriod = BDAdapter.getEndDateByPeriod(pkOrg, period);
        return this.getBeAllocateDatas(aggvo, period, endOfPeriod,
                this.getSendCenterids((AllocdefHeadVO) aggvo.getParentVO()), true);
    }

    protected void checkHasStuffShift(String period, String[] ccentrids, String[] pkFactorasoas, String pk_org,
            boolean isCTOAlloc) throws BusinessException {
        IStuffQueryForAllocate stuffserv = NCLocator.getInstance().lookup(IStuffQueryForAllocate.class);

        if (stuffserv.checkHasStuffShift(period, ccentrids, pkFactorasoas, pk_org, isCTOAlloc)) {
            // 如果存在来源于辅助服务的消耗单，且实际金额为空，说明需要进行辅助服务分配。
            throw new BusinessException(AllocateLangConst.getERR_HAS_UNALLOCSHIFT());
        }
    }

    /**
     * 获得待分配数据
     *
     * @param aggvo
     *            分配定义VO
     * @param period
     *            会计期间
     * @param businessDate
     *            业务日期
     * @param centerIds
     *            成本中心ID数组
     * @param forShow
     *            前台显示
     * @return 待分配数据
     */
    protected abstract AllocateRecordHeadVO[] getBeAllocateDatas(AllocdefAggVO aggvo, String period,
            UFDate businessDate, String[] centerIds, boolean forShow) throws BusinessException;

    protected abstract int getAllocateType();

    /**
     * 查询策略类
     *
     * @author liyjf
     * @param <T>
     *            类型
     */
    protected interface IMMDataQuery<T> {

        /**
         * 查询方法
         *
         * @param headvo
         *            AllocdefHeadVO
         * @param period
         *            会计期间
         * @param ccentrids
         *            发送成本中心数组（合并了发送成本中心组和发送成本中心）
         * @param pkFactorasoas
         *            核算要素组（表体的核算要素组）
         * @param pk_group
         *            集团
         * @param pk_org
         *            组织
         * @return 符合条件的查询方法
         */
        List<T> queryVOByCondition(AllocdefHeadVO headvo, String period, String[] ccentrids, String[] pkFactorasoas,
                boolean forCollect) throws BusinessException;
    }

    /**
     * 查询VO
     *
     * @param <T>
     *            类型
     * @param period
     *            会计期间
     * @param aggvo
     *            事务VO
     * @param mmquery
     *            查询策略
     * @param businessDate
     *            业务日期
     * @param centerIds
     *            成本中心ID数组
     * @return
     * @throws BusinessException
     */
    protected <T> List<T> queryBeAllocateDatas(String period, AllocdefAggVO aggvo, IMMDataQuery<T> mmquery,
            UFDate businessDate, String[] centerIds, boolean forCollect) throws BusinessException {
        AllocdefHeadVO headvo = (AllocdefHeadVO) aggvo.getParentVO();
        return mmquery.queryVOByCondition(headvo, period, centerIds, this.getElementids(aggvo, businessDate),
                forCollect);
    }

    /**
     * 获得待分配的核算要素
     *
     * @param aggvo
     *            分配事务定义VO
     * @param businessDate
     *            业务日期
     * @return 待分配的核算要素
     */
    protected String[] getElementids(AllocdefAggVO aggvo, UFDate businessDate) {
        if (this.elementids == null) {
            Set<String> allElementSet = new HashSet<String>();
            String[] elementIDs = this.getElementVOFileds(aggvo, CelementItemVO.CELEMENTID);
            if (!CMArrayUtil.isEmpty(elementIDs)) {
                allElementSet.addAll(Arrays.asList(elementIDs));
            }

            String[] elementGroupIDs = this.getElementVOFileds(aggvo, CelementItemVO.CELEMENTGROUPID);

            elementIDs = FIAdapter.getFactorasoaPksByClass(elementGroupIDs, businessDate);

            if (!CMArrayUtil.isEmpty(elementIDs)) {
                allElementSet.addAll(Arrays.asList(elementIDs));
            }
            this.elementids = allElementSet.toArray(new String[0]);
        }
        return this.elementids;
    }

    public void setElementids(String[] elementids) {
        this.elementids = elementids;
    }

    /**
     * 获得分配事务定义核算要素子表部分字段
     *
     * @param aggvo
     *            分配事务定义VO
     * @param fields
     *            字段
     * @return 分配事务定义核算要素子表部分字段
     */
    protected String[] getElementVOFileds(AllocdefAggVO aggvo, String fields) {

        CelementItemVO[] items = (CelementItemVO[]) aggvo.getChildren(CelementItemVO.class);
        Set<String> fieldValueSet = new HashSet<String>();
        if (items != null) {

            for (CelementItemVO item : items) {
                if (CMValueCheck.isNotEmpty(item.getAttributeValue(fields))) {
                    fieldValueSet.add((String) item.getAttributeValue(fields));
                }
            }
        }

        return fieldValueSet.toArray(new String[0]);
    }

    /**
     * 获得发送成本中心
     *
     * @param headvo
     *            分配事务定义表头VO
     * @return 发送成本中心数组
     */
    protected String[] getSendCenterids(AllocdefHeadVO headvo) throws BusinessException {
        String[] ccentrids = this.centerids;
        if (ccentrids == null) {
            // 发送成本中心组ID
            String ccenttrclsid = headvo.getCctcgroupid();
            if (ccenttrclsid != null) {

                ccentrids = FIAdapter.getCostCentersByClassID(ccenttrclsid);
            }
            else {
                ccentrids = new String[] {
                        headvo.getCcostcenterid()
                };
            }
            this.centerids = ccentrids;
        }
        return ccentrids;
    }

    public void setSendCenterids(String[] centerids) {
        this.centerids = centerids;
    }

    protected String getErrInfo(AllocateRecordHeadVO beAllocateVO, String errInfo) {
        String result = "";
        try {
            if (CMStringUtil.isNotEmpty(beAllocateVO.getCcostobjectid())) {
                Map<String, CostObjectVO> cobjMap =
                        NCLocator.getInstance().lookup(ICostObjectQueryForPub.class)
                        .queryCostObjectInfo(null, null, new String[] {
                                beAllocateVO.getCcostobjectid()
                        }, new String[] {
                                CostObjectVO.VCOSTOBJCODE
                        }, CostObjectConst.LEGAL_TYPE_ALL);
                if (CMMapUtil.isNotEmpty(cobjMap)) {
                    result +=
                            AllocateLangConst.getINFO_PRODUCT() + "【"
                                    + cobjMap.get(beAllocateVO.getCcostobjectid()).getVcostobjcode() + "】";
                }
            }
            if (CMStringUtil.isNotEmpty(beAllocateVO.getCmaterialid())) {
                String cmaterialvid = BDAdapter.convertMaterialid2Vid(beAllocateVO.getCmaterialid());

                Map<String, MaterialVO> materialMap =
                        BDAdapter.queryMaterialBaseInfoByPks(new String[] {
                                cmaterialvid
                        }, new String[] {
                                MaterialVO.PK_SOURCE, MaterialVO.CODE, MaterialVO.NAME, MaterialVO.NAME2, MaterialVO.NAME3,
                                MaterialVO.NAME4, MaterialVO.NAME5, MaterialVO.NAME6, MaterialVO.ENABLESTATE
                        });
                MaterialVO materialVO = materialMap.get(cmaterialvid);
                if (materialVO != null && CMStringUtil.isNotEmpty(materialVO.getCode())) {
                    result += AllocateLangConst.getINFO_STUFF() + "【" + materialVO.getCode() + "】";
                }
            }
            else if (CMStringUtil.isNotEmpty(beAllocateVO.getCelementid())) {
                String celementid = beAllocateVO.getCelementid();
                FactorVO factorVO = FIAdapter.queryFactorVOByAsoaPK(celementid);
                if (factorVO != null && CMStringUtil.isNotEmpty(factorVO.getFactorcode())) {
                    result += AllocateLangConst.getINFO_ELEMENT() + "【" + factorVO.getFactorcode() + "】";
                }
            }
            if (CMStringUtil.isNotEmpty(beAllocateVO.getCactivityid())) {
                List<String> activityPKs = new ArrayList<String>();
                activityPKs.add(beAllocateVO.getCactivityid());
                BDActivityVO[] vos =
                        NCLocator.getInstance().lookup(IBDActivityPubQuery.class).queryBDActivityVOByPKs(activityPKs);
                if (CMArrayUtil.isNotEmpty(vos) && CMStringUtil.isNotEmpty(vos[0].getVactivitycode())) {
                    result += AllocateLangConst.getINFO_ACTIVITY() + "【" + vos[0].getVactivitycode() + "】";
                }
            }
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        result += errInfo;
        return result;
    }
}

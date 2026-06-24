package nc.bs.cm.costrevert.revert.batchrevert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMArrayUtil;
import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMNumberUtil;
import nc.bd.framework.base.CMNumberUtil2;
import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.base.CMValueCheck;
import nc.bs.cm.costrevert.CtRevertUtil;
import nc.bs.cm.costrevert.maintain.CostRevertDBMaintian;
import nc.bs.cm.costrevert.revert.numcalculate.BgRevertNumCal;
import nc.bs.cm.costrevert.revert.numcalculate.CtRevertRatioNumCal;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.adapter.CMSysInitAdapter;
import nc.cmpub.business.adapter.IAAdapter;
import nc.cmpub.business.adapter.InventoryBalanceInfo;
import nc.cmpub.business.cons.CMSysInitParamConst;
import nc.cmpub.framework.batchlimit.MABatchLimitEnum;
import nc.cmpub.framework.batchlimit.MABatchLimitUtil;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.pub.billcode.vo.EntityValueVO;
import nc.pubitf.cm.iastuff.cm.costrevert.IIastuffRewriteByCostrevert;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.bd.material.cost.MaterialCostmodeVO;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cm.costrevert.AggCtRevertVO;
import nc.vo.cm.costrevert.CMMLangConstCostRevert;
import nc.vo.cm.costrevert.CtRevertContainer;
import nc.vo.cm.costrevert.CtRevertHeadVO;
import nc.vo.cm.costrevert.CtRevertItemVO;
import nc.vo.cm.costrevert.CtRevertParamVO;
import nc.vo.cm.costrevert.CtRevertStatusEnum;
import nc.vo.cm.pc.pc0410.CMTProxySrv;
import nc.vo.cm.revertbg.entity.RevertBgAggVO;
import nc.vo.cm.revertbg.entity.RevertBgHeadVO;
import nc.vo.cm.revertbg.entity.RevertBgItemVO;
import nc.vo.cm.revertbg.enumeration.RevertSourceTypeEnum;
import nc.vo.ia.generalnab.para.cm.QueryInventoryParmVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 实际成本还原
 *
 * @since 6.1
 * @version 2011-11-22 下午03:38:08
 * @author xuyanga
 * @modify liwzh
 * @modify sz-6.31
 */
public class ActualCostRevert extends AbstractCostRevert {

    public ActualCostRevert(CtRevertParamVO paramVo) {
        super(paramVo);
    }

    /**
     * 还原入口函数
     *
     * @return
     * @throws BusinessException
     */
    @Override
    public void doRevertCost() throws BusinessException {
        // 根据成本分类查询所有的物料
        this.costClassMaterialMap =
                BDAdapter.getAllMaterialIDsByCostClassIDs(this.paramVo.getPk_group(), this.paramVo.getPk_org(),
                        this.paramVo.getCostclassid().toArray(new String[0]));
        if (this.costClassMaterialMap == null) {
            this.costClassMaterialMap = new HashMap<String, List<String>>();
        }
        // 如果当前会计期间为期初期间，需要将当前期初成本还原数据生成比例保存到数据库
        this.dealBeginAccountCtRevertRatio(this.paramVo);
        // 调用其他入库消耗单的接口，重新从存货核算获取单价金额
        CMTProxySrv.delegate_RequiresNew(this, this.getMethodIaStuff(this), null);
        // this.updateIastuffVO();
        // 获取分批计算的数量
        int batchSize = MABatchLimitUtil.getInstance().getBatchLimit(MABatchLimitEnum.COST_REVERT_LIMIT, 2000);
        // 按照层次根据期初和本期产品消耗计算还原后的数据
        int lower = this.paramVo.getLower();
        for (int i = lower; i > 0; i--) {
            if (this.paramVo.getLevelMsg().get(Integer.valueOf(i)) == null) {
                continue;
            }
            List<String> matList = this.paramVo.getLevelMsg().get(Integer.valueOf(i));
            Method method = this.getMethod(this);
            Object[] paras = new Object[2];
            paras[0] = matList;
            paras[1] = batchSize;
            CMTProxySrv.delegate_RequiresNew(this, method, paras);
            Logger.error("成本还原第" + i + "层第批执行完毕！");
        }
    }

    public void revertDelegateNew(List<String> matList, int batchSize) throws BusinessException {
        // 删除数据
        this.doDeleteRevertDatas(matList);
        // 记录当次需要的物料集合
        List<String> batchMtrPKList = new ArrayList<String>();
        for (String materialPK : matList) {
            if (batchMtrPKList.size() == batchSize) { // 分批计算
                this.costRevertByBatch(batchMtrPKList);
                batchMtrPKList.clear();
            }
            batchMtrPKList.add(materialPK);
        }
        if (batchMtrPKList.size() > 0) {
            this.costRevertByBatch(batchMtrPKList);
        }
        // 更新还原层次状态到“已还原”
        CostRevertDBMaintian.updateCtRvertLevelStatus(matList.toArray(new String[matList.size()]),
                this.paramVo.getCperiod(), CtRevertStatusEnum.REVERTED.toIntValue());
    }

    private Method getMethodIaStuff(Object target) {
        Method method = null;
        try {
            method = target.getClass().getDeclaredMethod("updateIastuffVO");
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return method;
    }

    private Method getMethod(Object target) {
        Class<?>[] parameterTypes = new Class<?>[2];
        parameterTypes[0] = List.class;
        parameterTypes[1] = int.class;
        Method method = null;
        try {
            method = target.getClass().getDeclaredMethod("revertDelegateNew", parameterTypes);
        }
        catch (Exception e) {
            ExceptionUtils.wrappException(e);
        }
        return method;
    }

    public void costRevertByBatch(List<String> batchMtrPKList) throws BusinessException {
        CtRevertContainer container = new CtRevertContainer();
        // 获得完工与期初数据
        this.getPrepareDatas(batchMtrPKList, container);
        // 获得当前层的所有产品，包括成本分类对应的产品，用Set避免重复
        this.getThisLevelProductIdSet(batchMtrPKList, container);
        // 获得其他出入库消耗单与产成品其他入库分项成本数据
        this.getIaStuffAndSubitem(container);
        // 汇总完工成本、期初成本还原数据、其他出入库消耗单和产成品其它入库分项成本数据
        this.getAllAggVOList(container);
        // 根据物料得到是否还原为是的数据
        // this.getIsCostrestoreMaterials(container);
        // 得到物料的生产信息与成本信息与要素信息
        this.getMaterialProdVOOidMap(container);
        // 获得所有半成品集合
        this.getHalfProductList(container);
        // 本期还原比率与上期还原比率
        this.getCtRevertRatio(container);
        // 添加比率集合，为了获取精度
        this.addAllAggVOListRatio(container);
        // 处理精度
        this.getMeasureMoneyPrecision(container.getAllAggVOList());
        // 获得还原结果
        this.getResultDatas(container);
        // 处理还原比率
        this.dealCtRevertRatio(container);
        // 保存还原结果
        this.saveCtRevertDatas(container);
        // 处理期末结存
        this.dealBalanceDatas(container);
    }

    public void addAllAggVOListRatio(CtRevertContainer container) {
        container.getAllAggVOList().addAll(container.getCtRevertRatioMap().values());
        container.getAllAggVOList().addAll(container.getCtRevertRatioPreMap().values());
    }

    // 获得还原结果
    public void getResultDatas(CtRevertContainer container) throws BusinessException {
        // 1.记录自制已经还原过的数据
        Map<String, AggCtRevertVO> revertMap =
                this.dealProductRevertCost(container.getActualProdCostMap(), container.getMaterialProdVOs(),
                        container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
                        container.getCtRevertRatioPreMap());
        container.setRevertMap(revertMap);
        // 合并带成本中心已还原结果
        Map<String, AggCtRevertVO> sumRevertMap = this.getSumCtRevertByCostcenterMap(revertMap, false);
        // 将比率放到本期还原比率中待用
        container.getCtRevertRatioMap().putAll(sumRevertMap);
        // // 2.记录自制、联副已经还原过的数据
        // Map<String, AggCtRevertVO> revertLfMap =
        // this.dealProductRevertCost(container.getActualProdCostLfMap(), container.getMaterialProdVOs(),
        // container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
        // container.getCtRevertRatioPreMap());
        // container.setRevertLfMap(revertLfMap);
        // // 合并带成本中心已还原结果
        // Map<String, AggCtRevertVO> sumRevertLfMap = this.getSumCtRevertByCostcenterMap(revertLfMap);
        // // 将比率放到本期还原比率中待用
        // container.getCtRevertRatioMap().putAll(sumRevertLfMap);
        // 3.记录返工、主产品已经还原过的数据
        Map<String, AggCtRevertVO> revertReworkMap =
                this.dealProductRevertCost(container.getActualProdCostReworkMap(), container.getMaterialProdVOs(),
                        container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
                        container.getCtRevertRatioPreMap());
        container.setRevertReworkMap(revertReworkMap);
        // // 4.记录返工、联副已经还原过的数据
        // Map<String, AggCtRevertVO> revertReworkLfMap =
        // this.dealProductRevertCost(container.getActualProdCostReworkLfMap(), container.getMaterialProdVOs(),
        // container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
        // container.getCtRevertRatioPreMap());
        // container.setRevertReworkLfMap(revertReworkLfMap);
        // 5.记录委外、主产品已经还原过的数据
        Map<String, AggCtRevertVO> revertOutMap =
                this.dealProductRevertCost(container.getActualProdCostOutMap(), container.getMaterialProdVOs(),
                        container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
                        container.getCtRevertRatioPreMap());
        container.setRevertOutMap(revertOutMap);
        // 合并带成本中心已还原结果
        Map<String, AggCtRevertVO> sumRevertOutReWorkMap = this.getSumCtRevertByCostcenterMap(revertOutMap, true);
        // 将比率放到本期还原比率中待用
        container.getCtRevertRatioMap().putAll(sumRevertOutReWorkMap);
        // 记录委外返工已经还原过的数据
        Map<String, AggCtRevertVO> revertOutReWorkMap =
                this.dealProductRevertCost(container.getActualProdCostOutReWorkMap(), container.getMaterialProdVOs(),
                        container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
                        container.getCtRevertRatioPreMap());
        container.setRevertOutReWorkMap(revertOutReWorkMap);
        // // 6.记录委外、联副已经还原过的数据
        // Map<String, AggCtRevertVO> revertOutLfMap =
        // this.dealProductRevertCost(container.getActualProdCostOutLfMap(), container.getMaterialProdVOs(),
        // container.getMaterialCostmodeVOs(), container.getCtRevertRatioMap(),
        // container.getCtRevertRatioPreMap());
        // container.setRevertOutLfMap(revertOutLfMap);
        // 分单保存形态转换数据的还原结果
        Map<String, List<AggCtRevertVO>> iaTransformPerOrderRevertMap =
                this.dealLastIaTransformPerOrderRevert(container.getIaStuffPerOrderSumAggMap(),
                        container.getMaterialProdVOs(), container.getMaterialCostmodeVOs(),
                        container.getCtRevertRatioMap(), container.getCtRevertRatioPreMap());
        container.setIaTransformPerOrderRevertMap(iaTransformPerOrderRevertMap);
    }

    // 本期还原比率与上期还原比率
    public void getCtRevertRatio(CtRevertContainer container) throws BusinessException {
        // 获取分批计算的数量
        int batchSize = 100;
        // 记录当次需要的物料集合
        Set<String> batchMtrPKSet = new HashSet<String>();
        for (String materialPK : container.getProductIdSet()) {
            if (batchMtrPKSet.size() == batchSize) { // 分批计算
                Map<String, AggCtRevertVO> revertRatio = CtRevertRatioNumCal.calNum(this.paramVo, batchMtrPKSet, false);
                // 取到比率的物料从元集合中删除
                this.getPreMtrPKSet(revertRatio, batchMtrPKSet);
                // 本期还原比率
                container.getCtRevertRatioMap().putAll(revertRatio);
                // 上期还原比率
                if (CMValueCheck.isNotEmpty(batchMtrPKSet)) {
                    container.getCtRevertRatioPreMap().putAll(
                            CtRevertRatioNumCal.calNum(this.paramVo, batchMtrPKSet, true));
                }
                batchMtrPKSet.clear();
            }
            batchMtrPKSet.add(materialPK);
        }
        if (batchMtrPKSet.size() > 0) {
            Map<String, AggCtRevertVO> revertRatio = CtRevertRatioNumCal.calNum(this.paramVo, batchMtrPKSet, false);
            // 没有取到本期比率的物料放在集合中
            this.getPreMtrPKSet(revertRatio, batchMtrPKSet);
            // 本期还原比率
            container.getCtRevertRatioMap().putAll(revertRatio);
            // 上期还原比率
            if (CMValueCheck.isNotEmpty(batchMtrPKSet)) {
                container.getCtRevertRatioPreMap()
                        .putAll(CtRevertRatioNumCal.calNum(this.paramVo, batchMtrPKSet, true));
            }
        }
    }

    /**
     * 取到比率的物料从元集合中删除
     *
     * @param revertRatio
     * @param batchMtrPKSet
     * @return
     */
    private void getPreMtrPKSet(Map<String, AggCtRevertVO> revertRatio, Set<String> batchMtrPKSet) {
        Set<String> matAndCostClass = new HashSet<String>();
        for (AggCtRevertVO aggvo : revertRatio.values()) {
            CtRevertHeadVO headvo = (CtRevertHeadVO) aggvo.getParentVO();
            if (CMValueCheck.isNotEmpty(headvo.getCmaterialid())) {
                matAndCostClass.add(headvo.getCmaterialid());
            }
            else if (CMValueCheck.isNotEmpty(headvo.getCmarcostclassid())) {
                matAndCostClass.add(headvo.getCmarcostclassid());
            }
        }
        batchMtrPKSet.removeAll(matAndCostClass);
    }

    /**
     * 如果当前会计期间为期初期间，需要将当前期初成本还原数据生成比例保存到数据库
     *
     * @param paramVO
     * @throws BusinessException
     */
    public void dealBeginAccountCtRevertRatio(CtRevertParamVO paramVO) throws BusinessException {
        String beginPeriod = BDAdapter.getBeginAccount(paramVO.getPk_org());
        if (paramVO.getCperiod().compareTo(beginPeriod) <= 0) {
            List<String> batchProdCostClass = new ArrayList<String>();
            batchProdCostClass.addAll(this.paramVo.getBatchProdCostClassIDs());
            // 获得当前期间为期初期间的初成本还原数据
            Map<String, AggCtRevertVO> revertBGAggMap = BgRevertNumCal.calNum(this.paramVo, batchProdCostClass);
            if (revertBGAggMap == null || revertBGAggMap.size() == 0) {
                return;
            }
            // 业务日期
            UFDate businessDate = BDAdapter.getBeginDateByPeriod(paramVO.getPk_org(), beginPeriod);
            businessDate = businessDate.getDateBefore(1);
            this.setBusinessDatePre(businessDate);
            Map<String, AggCtRevertVO> revertBGAggCloneMap = new HashMap<String, AggCtRevertVO>();
            for (Entry<String, AggCtRevertVO> ratiovo : revertBGAggMap.entrySet()) {
                AggCtRevertVO ctRevertVOClone = (AggCtRevertVO) ratiovo.getValue().clone();
                CtRevertHeadVO ratioHeadVO = (CtRevertHeadVO) ctRevertVOClone.getParent();
                ratioHeadVO.setCperiod(beginPeriod.split("-")[0] + "-00");
                revertBGAggCloneMap.put(ratiovo.getKey(), ctRevertVOClone);
            }
            CtRevertContainer container = new CtRevertContainer();
            container.setRevertBGAggMap(revertBGAggCloneMap);
            // 处理还原比率
            this.dealCtRevertRatio(container);
            // 保存还原结果
            this.saveCtRevertDatas(container);
            this.setBusinessDatePre(null);
        }
    }

    /**
     * 删除数据
     *
     * @throws BusinessException
     */
    @Override
    public void doDeleteRevertDatas(List<String> matList) throws BusinessException {
        this.deleteRevertDatas(matList);
    }

    /**
     * 获取计量单位金额的精度
     *
     * @param actualProdCostMap 完工成本数据
     * @param revertBGAggMap 期初数据
     * @throws BusinessException
     */
    private void getMeasureMoneyPrecision(List<AggCtRevertVO> allAggVOList) throws BusinessException {
        Set<String> measdocSet = new HashSet<String>();
        // 计量单位
        // 金额信息
        measdocSet.addAll(this.getMeasureIds(allAggVOList));
        this.scaleUtil.getScale(this.paramVo.getPk_group(), this.paramVo.getPk_org());
        // 数量信息
        this.scaleUtil.getNumScaleMap(measdocSet);
    }

    /**
     * 获取计量单位
     *
     * @param paramBgBanlanceAggMap
     * @return
     */
    private Set<String> getMeasureIds(List<AggCtRevertVO> allAggVOList) {
        Set<String> measdocSet = new HashSet<String>();
        if (CMValueCheck.isNotEmpty(allAggVOList)) {
            AggCtRevertVO[] aggArr = allAggVOList.toArray(new AggCtRevertVO[allAggVOList.size()]);
            for (AggCtRevertVO aggvo : aggArr) {
                CtRevertHeadVO headvo = (CtRevertHeadVO) aggvo.getParentVO();
                if (headvo.getCmeasureid() != null) {
                    measdocSet.add(headvo.getCmeasureid());
                }
                for (CircularlyAccessibleValueObject vo : aggvo.getChildrenVO()) {
                    CtRevertItemVO itemvo = (CtRevertItemVO) vo;
                    if (CMStringUtil.isNotEmpty(itemvo.getCmeasureid())) {
                        measdocSet.add(itemvo.getCmeasureid());
                    }
                }
            }
        }
        return measdocSet;
    }

    /**
     * 产品树还原
     *
     * @param actualProdCostMap 完工成本
     * @param materialProdVOs 物料的生产信息
     * @param materialCostmodeVOs 物料成本信息
     * @param ctRevertRatioMap 还原比率
     * @param ctRevertRatioPreMap 还原前比率
     * @return Map<String, AggCtRevertVO> 还原后数据
     * @throws BusinessException
     */
    protected Map<String, AggCtRevertVO> dealProductRevertCost(Map<String, AggCtRevertVO> actualProdCostMap,
            Map<String, MaterialProdVO> materialProdVOs, Map<String, MaterialCostmodeVO> materialCostmodeVOs,
            Map<String, AggCtRevertVO> ctRevertRatioMap, Map<String, AggCtRevertVO> ctRevertRatioPreMap)
                    throws BusinessException {
        Map<String, AggCtRevertVO> aggCtRevertVOs = new HashMap<String, AggCtRevertVO>();
        boolean isOutRework = false;// 是否委外返工
        for (Entry<String, AggCtRevertVO> entry : actualProdCostMap.entrySet()) {
            // 某个产品还原后耗用的明细
            Map<String, CtRevertItemVO> items = new HashMap<String, CtRevertItemVO>();
            CtRevertHeadVO headvo = (CtRevertHeadVO) entry.getValue().getParentVO();
            if (headvo.getNnum() != null) {
                headvo.setNnum(headvo.getNnum().setScale(
                        this.scaleUtil.getNumScaleMap().get(headvo.getCmeasureid()).intValue(), UFDouble.ROUND_HALF_UP));
            }
            headvo.setNcost(headvo.getNcost().setScale(this.scaleUtil.getScaleVO().getMoneyScale(),
                    this.getMoneyRoundType()));
            for (CtRevertItemVO item : (CtRevertItemVO[]) entry.getValue().getChildrenVO()) {
                if (CMValueCheck.isNotEmpty(item.getCmaterialid())
                        && headvo.getFinstoragetype().equals(Integer.valueOf(4))
                        && item.getCmaterialid().equals(headvo.getCmaterialid())) {
                    isOutRework = true;
                }
                else {
                    isOutRework = false;
                }
                // 半成品
                String pk_halfProduct =
                        this.isHalfProduct(item, materialProdVOs, true, materialCostmodeVOs.get(item.getCmaterialid()),
                                ctRevertRatioMap);
                // 材料的处理逻辑
                if (CMStringUtil.isEmpty(pk_halfProduct)) {
                    this.setItemVOtoMap(this.reSetStuffItems(item), items);
                }// 非材料的处理
                else {
                    AggCtRevertVO revertRatioAggVO =
                            CtRevertUtil.getMatchCtRevertRatioVOByKey(ctRevertRatioMap, pk_halfProduct,
                                    materialCostmodeVOs.get(item.getCmaterialid()), isOutRework);
                    if (CMValueCheck.isEmpty(revertRatioAggVO)) {
                        revertRatioAggVO =
                                CtRevertUtil.getMatchCtRevertRatioVOByKey(ctRevertRatioPreMap, pk_halfProduct,
                                        materialCostmodeVOs.get(item.getCmaterialid()), isOutRework);
                    }
                    if (revertRatioAggVO == null) {
                        this.setItemVOtoMap(this.reSetStuffItems(item), items);
                    }
                    else {
                        // 还原半成品明细并汇总
                        this.revertHalfProductCost(revertRatioAggVO, item, items);
                        // 半成品本身也作为一条负的记录存在明细map里
                        this.dealHalfProductCost(item, items);
                    }
                }
            }
            entry.getValue().setChildrenVO(items.values().toArray(new CtRevertItemVO[0]));
            aggCtRevertVOs.put(entry.getKey(), entry.getValue());
        }
        return aggCtRevertVOs;
    }

    /**
     * 获得合并后多个还原前成本还原数据，参数可多个且可空
     * 表体还原后为0的数据过滤掉，表体还原前的数据清空
     *
     * @param ctRevertAggVOs 成本还原涉及到的所有vo
     * @return AggCtRevertVO 合并后的单个vo
     */
    protected AggCtRevertVO getSumAggCtRevertDatasByRatio(CtRevertItemVO item, AggCtRevertVO revertRatioAggVO) {
        AggCtRevertVO sumAggVO = null;
        sumAggVO = (AggCtRevertVO) revertRatioAggVO.clone();
        CtRevertHeadVO headVo = null;
        headVo = (CtRevertHeadVO) sumAggVO.getParentVO();
        headVo.setNnum(item.getAfternumber());
        headVo.setNcost(item.getAftercost());
        for (CtRevertItemVO vo : (CtRevertItemVO[]) sumAggVO.getChildrenVO()) {
            // 过滤还原后产品中的耗用的半成品数据,还原后的金额是空 或者金额是0
            if (vo.getAftercost() == null || UFDouble.ZERO_DBL.equals(vo.getAftercost())) {
                continue;
            }
            vo.setBeforecost(null);
            vo.setBeforenumber(null);
            vo.setProcesscost(null);
            vo.setProcessnumber(null);
            vo.setThiscost(null);
            vo.setThisnumber(null);
            vo.setSubcost(null);
            vo.setSubnumber(null);
            vo.setAftercost(CMNumberUtil.multiply(item.getAftercost(), vo.getAftercost()));
            vo.setAfternumber(CMNumberUtil.multiply(item.getAfternumber(), vo.getAfternumber()));
        }
        return sumAggVO;
    }

    /**
     * 末级产品树还原
     *
     * @param levelRevertList 待还原数据
     * @return
     */
    protected Map<String, AggCtRevertVO> dealLastProductRevertCost(Map<String, AggCtRevertVO> levelRevertList) {
        for (AggCtRevertVO aggVO : levelRevertList.values()) {
            CtRevertHeadVO headvo = (CtRevertHeadVO) aggVO.getParentVO();
            headvo.setNnum(headvo.getNnum().setScale(
                    this.scaleUtil.getNumScaleMap().get(headvo.getCmeasureid()).intValue(), UFDouble.ROUND_HALF_UP));
            headvo.setNcost(headvo.getNcost().setScale(this.scaleUtil.getScaleVO().getMoneyScale(),
                    this.getMoneyRoundType()));
            for (CtRevertItemVO item : (CtRevertItemVO[]) aggVO.getChildrenVO()) {
                this.reSetStuffItems(item);
            }
        }
        return levelRevertList;
    }

    /**
     * 形态转换还原
     *
     * @param iaStuffPerOrderAggMap 形态转换待还原结果集
     * @param materialProdVOs 物料的生产信息
     * @param materialProdVOs 物料成本信息页签集合
     * @param ctRevertRatioMap 本期还原比率集合
     * @param ctRevertRatioPreMap 上期还原比率集合
     * @return Map<形态转换key, List<形态转换还原结果>>
     * @throws BusinessException
     */
    protected Map<String, List<AggCtRevertVO>> dealLastIaTransformPerOrderRevert(
            Map<String, List<AggCtRevertVO>> iaStuffPerOrderAggMap, Map<String, MaterialProdVO> materialProdVOs,
            Map<String, MaterialCostmodeVO> materialCostmodeVOs, Map<String, AggCtRevertVO> ctRevertRatioMap,
            Map<String, AggCtRevertVO> ctRevertRatioPreMap) throws BusinessException {
        Map<String, List<AggCtRevertVO>> revertedTransformAggVOMap = new HashMap<String, List<AggCtRevertVO>>();
        for (Entry<String, List<AggCtRevertVO>> transformPerOrderAggVOEntry : iaStuffPerOrderAggMap.entrySet()) {
            List<AggCtRevertVO> revertedTransformList = new ArrayList<AggCtRevertVO>();
            for (AggCtRevertVO transformAggVO : transformPerOrderAggVOEntry.getValue()) {
                AggCtRevertVO transformAggVOClone = (AggCtRevertVO) transformAggVO.clone();
                AggCtRevertVO revertedTransformAggVO = null;// 还原后的AggVO
                CtRevertHeadVO revertedTransformHeadVO = null;// 还原后的表头VO
                Map<String, CtRevertItemVO> revertedTransformItemVOMap = null;
                String pk_halfProduct = null;
                revertedTransformHeadVO = (CtRevertHeadVO) transformAggVOClone.getParentVO();
                revertedTransformItemVOMap = new HashMap<String, CtRevertItemVO>();
                for (CtRevertItemVO transformItemVO : (CtRevertItemVO[]) transformAggVOClone.getChildrenVO()) {
                    pk_halfProduct =
                            this.isHalfProduct(transformItemVO, materialProdVOs, true,
                                    materialCostmodeVOs.get(transformItemVO.getCmaterialid()), ctRevertRatioMap);
                    if (CMValueCheck.isEmpty(pk_halfProduct)) {// 材料或要素的处理
                        this.setItemVOtoMap(this.reSetStuffItems(transformItemVO), revertedTransformItemVOMap);
                    }
                    else {
                        // 半成品的处理
                        AggCtRevertVO revertRatioAggVO =
                                CtRevertUtil.getMatchCtRevertRatioVOByKey(ctRevertRatioMap, pk_halfProduct,
                                        materialCostmodeVOs.get(transformItemVO.getCmaterialid()), false);
                        if (revertRatioAggVO == null) {
                            // 如果本期无还原比例，存在循环情况，按照取价参数进行还原
                            // 取价参数
                            String code = CMSysInitParamConst.CYCLE_CONSUME_PRICE_TYPE + "_V";
                            String cirGetPriceMethod =
                                    CMSysInitAdapter.queryParamValueByCode(this.paramVo.getPk_org(), code);
                            if (CMValueCheck.isNotEmpty(cirGetPriceMethod)) {
                                String[] cirGetPriceMethodArr = cirGetPriceMethod.split(",");
                                for (String method : cirGetPriceMethodArr) {
                                    if (CMSysInitParamConst.CYCLE_CONSUME_PRICE_TYPE_NAB.equals(method)) {// 上期结存价格，按上期还原比率进行还原
                                        revertRatioAggVO =
                                                CtRevertUtil.getMatchCtRevertRatioVOByKey(ctRevertRatioPreMap,
                                                        pk_halfProduct,
                                                        materialCostmodeVOs.get(transformItemVO.getCmaterialid()),
                                                        false);
                                    }
                                    else {
                                        // 按标准成本还原
                                        // ExceptionUtils.wrappBusinessException(CMMLangConstCostRevert.getERR_REVERT_METHOD());
                                    }
                                }
                            }
                        }
                        if (revertRatioAggVO == null) {
                            this.setItemVOtoMap(this.reSetStuffItems(transformItemVO), revertedTransformItemVOMap);
                        }
                        else {
                            // 还原半成品明细并汇总
                            this.revertHalfProductCost(revertRatioAggVO, transformItemVO, revertedTransformItemVOMap);
                        }
                    }
                }
                // 组装聚合VO
                revertedTransformAggVO = new AggCtRevertVO();
                revertedTransformAggVO.setParentVO(revertedTransformHeadVO);
                revertedTransformAggVO.setChildrenVO(revertedTransformItemVOMap.values().toArray(
                        new CtRevertItemVO[revertedTransformItemVOMap.size()]));
                revertedTransformList.add(revertedTransformAggVO);
                // revertedTransformAggVOMap.add(revertedTransformAggVO);
            }
            revertedTransformAggVOMap.put(transformPerOrderAggVOEntry.getKey(), revertedTransformList);
        }
        return revertedTransformAggVOMap;
    }

    /**
     * 处理期末结存数据
     *
     * @param materialProdVOs 物料生产信息
     * @param ctRevertRatioAggVOMap 还原过的比率
     * @throws BusinessException
     */
    protected void dealBalanceDatas(CtRevertContainer container) throws BusinessException {
        if (!IAAdapter.isIAModuleEnabled(AppContext.getInstance().getPkGroup())) {// 判断是否安装了存货核算
            return;
        }
        List<RevertBgAggVO> bgAggVos = new ArrayList<RevertBgAggVO>();
        String nextPeriod = BDAdapter.getNextPeriod(this.paramVo.getPk_org(), this.paramVo.getCperiod());
        String pk_book = CtRevertUtil.getOrgAccount(this.paramVo.getPk_org());
        // 特征物料
        Map<String, String> costclassMatMap = null;
        // 设置本位币
        String pk_currency = this.getCurreny();
        // 获得自由辅助属性成本域查询参数
        Set<QueryInventoryParmVO> matParamvos =
                this.getAssitAttrRegionQryVOSet(container.getSumCtRevertRatioAggVOMap());
        // 带有辅助属性的存货核算的结存数量、结存金额
        Map<String, InventoryBalanceInfo> inventoryBalanceMap =
                IAAdapter.queryNumAndMoneyForPeriodAss(this.paramVo.getPk_org(), pk_book, this.paramVo.getCperiod(),
                        matParamvos, this.paramVo.getCostclassid());
        if (this.paramVo.getCostclassid() != null && this.paramVo.getCostclassid().size() > 0) {
            costclassMatMap =
                    BDAdapter.queryFeatureMaterial(this.paramVo.getPk_group(), this.paramVo.getPk_org(), this.paramVo
                            .getCostclassid().toArray(new String[0]), false);
        }
        String[] codes =
                NCLocator
                        .getInstance()
                        .lookup(IBillcodeManage.class)
                        .getBatchBillCodesNoMeta_RequiresNew("CMMP", this.paramVo.getPk_group(),
                                this.paramVo.getPk_org(), new EntityValueVO(), inventoryBalanceMap.size());
        int i = 0;// 单据号记号
        for (Entry<String, InventoryBalanceInfo> entry : inventoryBalanceMap.entrySet()) {
            // 获取存货
            InventoryBalanceInfo inventoryBalanceInfo = entry.getValue();
            // 获取还原后和期初汇总数据
            AggCtRevertVO sumAggVO = container.getSumCtRevertRatioAggVOMap().get(entry.getKey());
            if (sumAggVO == null || CMArrayUtil.isEmpty(sumAggVO.getChildrenVO())) {
                continue;
            }
            if (inventoryBalanceInfo.getNnum().equals(UFDouble.ZERO_DBL)
                    || inventoryBalanceInfo.getNcost().equals(UFDouble.ZERO_DBL)) {
                continue;
            }
            RevertBgHeadVO bgHeadVo = new RevertBgHeadVO();
            CtRevertHeadVO headVo = (CtRevertHeadVO) sumAggVO.getParentVO();
            // 设置表头数据到期初还原vo
            for (String attrName : bgHeadVo.getAttributeNames()) {
                bgHeadVo.setAttributeValue(attrName, headVo.getAttributeValue(attrName));
            }
            bgHeadVo.setVbillcode(codes[i]);
            bgHeadVo.setNcost(inventoryBalanceInfo.getNcost());
            bgHeadVo.setNnum(inventoryBalanceInfo.getNnum());
            bgHeadVo.setCperiod(nextPeriod);
            bgHeadVo.setCcurrency(pk_currency);
            bgHeadVo.setCreator(AppContext.getInstance().getPkUser());
            bgHeadVo.setCreationtime(AppContext.getInstance().getServerTime());
            bgHeadVo.setIsourcetype(Integer.valueOf(RevertSourceTypeEnum.COSTREVERT.getEnumValue().getValue()
                    .toString()));
            if (bgHeadVo.getCmarcostclassid() != null && costclassMatMap != null) {
                bgHeadVo.setCfeaturematerialvid(costclassMatMap.get(bgHeadVo.getCmarcostclassid()));
            }
            // 设置表体数据到期初还原vo
            Map<String, RevertBgItemVO> items = new HashMap<String, RevertBgItemVO>();
            // 需要补差的VO
            RevertBgItemVO maxCostItem = null;
            // 最终的补差数据(最初是总成本,每次循环会逐步减)
            UFDouble remainCost = inventoryBalanceInfo.getNcost();
            for (CtRevertItemVO item : (CtRevertItemVO[]) sumAggVO.getChildrenVO()) {
                RevertBgItemVO bgItem = new RevertBgItemVO();
                for (String attrName : bgItem.getAttributeNames()) {
                    bgItem.setAttributeValue(attrName, item.getAttributeValue(attrName));
                }
                bgItem.setCcustomerid(item.getCcustomerid());
                bgItem.setCprojectid(item.getCprojectid());
                bgItem.setCproductorid(item.getCproductorid());
                bgItem.setCvendorid(item.getCvendorid());
                UFDouble nmoney =
                        this.calculateNumber(inventoryBalanceInfo.getNcost(), item.getAftercost(), headVo.getNcost());
                if (null != nmoney && CMStringUtil.isNotEmpty(nmoney.toString())) {
                    bgItem.setAttributeValue(RevertBgItemVO.NMONEY,
                            nmoney.setScale(this.scaleUtil.getScaleVO().getMoneyScale(), this.getMoneyRoundType()));
                }
                if (CMValueCheck.isNotEmpty(item.getAfternumber()) && CMValueCheck.isNotEmpty(item.getCmaterialid())) {// 如果表体数量不为空且为材料项，才计算数量
                    UFDouble number =
                            this.calculateNumber(inventoryBalanceInfo.getNnum(), item.getAfternumber(),
                                    headVo.getNnum());
                    if (CMStringUtil.isNotEmpty(item.getCmeasureid())) {
                        if (null != number && CMStringUtil.isNotEmpty(number.toString())) {
                            bgItem.setAttributeValue(
                                    RevertBgItemVO.NNUM,
                                    number.setScale(this.scaleUtil.getNumScaleMap().get(item.getCmeasureid())
                                            .intValue(), UFDouble.ROUND_HALF_UP));
                        }
                    }
                    else {
                        bgItem.setAttributeValue(RevertBgItemVO.NNUM, number);
                    }
                }
                UFDouble nprice = CMNumberUtil2.div(bgItem.getNmoney(), bgItem.getNnum());
                if (null != nprice && CMStringUtil.isNotEmpty(nprice.toString())) {
                    bgItem.setAttributeValue(RevertBgItemVO.NPRICE,
                            nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(), UFDouble.ROUND_HALF_UP));
                }
                bgItem.setPk_group(bgHeadVo.getPk_group());
                bgItem.setPk_org(bgHeadVo.getPk_org());
                bgItem.setPk_org_v(bgHeadVo.getPk_org_v());
                remainCost = CMNumberUtil.sub(remainCost, bgItem.getNmoney());
                if (maxCostItem == null) {
                    maxCostItem = bgItem;
                }
                else if (bgItem.getNmoney().compareTo(maxCostItem.getNmoney()) > 0) {
                    maxCostItem = bgItem;
                }
                items.put(this.getUniqueKeyForBg(bgItem), bgItem);
            }
            // 如果存在补差金额,那么把需要补差的金额设置回map里
            if (maxCostItem != null) {
                maxCostItem = items.get(this.getUniqueKeyForBg(maxCostItem));
                maxCostItem.setNmoney(CMNumberUtil2.add(remainCost, maxCostItem.getNmoney()));
                UFDouble nprice = CMNumberUtil2.div(maxCostItem.getNmoney(), maxCostItem.getNnum());
                if (null != nprice && CMStringUtil.isNotEmpty(nprice.toString())) {
                    maxCostItem.setAttributeValue(RevertBgItemVO.NPRICE,
                            nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(), UFDouble.ROUND_HALF_UP));
                }
                items.put(this.getUniqueKeyForBg(maxCostItem), maxCostItem);
            }
            RevertBgAggVO aggVo = new RevertBgAggVO();
            aggVo.setParentVO(bgHeadVo);
            aggVo.setChildrenVO(items.values().toArray(new RevertBgItemVO[0]));
            bgAggVos.add(aggVo);
        }
        if (CMCollectionUtil.isNotEmpty(bgAggVos)) {
            CostRevertDBMaintian.insertBgRevertDatas(bgAggVos.toArray(new RevertBgAggVO[bgAggVos.size()]));
        }
    }

    /**
     * 获得自由辅助属性成本域查询参数
     *
     * @param levelMap 已还原结果集
     * @param queryVOsSet 自由辅助属性成本域查询参数集
     * @param relationQueryVOsOids 成本域与物料集合对应关系
     */
    protected Set<QueryInventoryParmVO> getAssitAttrRegionQryVOSet(Map<String, AggCtRevertVO> levelMap) {
        Set<QueryInventoryParmVO> matParamvos = new HashSet<QueryInventoryParmVO>();
        for (AggCtRevertVO vo : levelMap.values()) {
            CtRevertHeadVO revertHeadVO = (CtRevertHeadVO) vo.getParent();
            if (revertHeadVO.getCmaterialid() == null) {
                continue;
            }
            QueryInventoryParmVO parmVO = new QueryInventoryParmVO();
            parmVO.setCinventoryid(revertHeadVO.getCmaterialid());
            parmVO.setCasscustid(revertHeadVO.getCcustomerid());
            parmVO.setCproductorid(revertHeadVO.getCproductorid());
            parmVO.setCprojectid(revertHeadVO.getCprojectid());
            parmVO.setCvendorid(revertHeadVO.getCvendorid());
            parmVO.setVfree1(revertHeadVO.getVfree1());
            parmVO.setVfree2(revertHeadVO.getVfree2());
            parmVO.setVfree3(revertHeadVO.getVfree3());
            parmVO.setVfree4(revertHeadVO.getVfree4());
            parmVO.setVfree5(revertHeadVO.getVfree5());
            parmVO.setVfree6(revertHeadVO.getVfree6());
            parmVO.setVfree7(revertHeadVO.getVfree7());
            parmVO.setVfree8(revertHeadVO.getVfree8());
            parmVO.setVfree9(revertHeadVO.getVfree9());
            parmVO.setVfree10(revertHeadVO.getVfree10());
            matParamvos.add(parmVO);
        }
        return matParamvos;
    }

    private String getCurreny() {
        String pk_accountingbook = null;
        String pk_currency = null;
        try {
            pk_accountingbook = BDAdapter.getMainAccountBookByStockOrgId(this.paramVo.getPk_org());
        }
        catch (BusinessException ex) {
        }
        if (CMStringUtil.isEmpty(pk_accountingbook)) {
            return pk_currency;
        }
        CurrencyRateUtilHelper currencyHelper = CurrencyRateUtilHelper.getInstance();
        // 取财务核算账簿本位币
        pk_currency = currencyHelper.getLocalCurrtypeByAccountingbookID(pk_accountingbook);

        return pk_currency;
    }

    /**
     * 获得CtRevertItemVO的唯一键值(物料和核算要素)
     *
     * @param item
     *            CtRevertItemVO
     * @return CtRevertItemVO的唯一键值
     */
    private String getUniqueKeyForBg(RevertBgItemVO item) {
        StringBuffer key = new StringBuffer();
        key.append(item.getCmaterialid());
        key.append(item.getCelementid());
        key.append(CtRevertUtil.getAssInfoItemKeyForBg(item));
        return key.toString();
    }

    /**
     * 检查成本类型参数
     *
     * @param paramVo
     * @return
     */
    protected String checkCostType(CtRevertParamVO param) {
        String error = "";
        if (CMStringUtil.isEmpty(param.getCcosttypeid())) {
            error = CMMLangConstCostRevert.getERR_PARAM_COSTTYPE();
        }
        return error;
    }

    /**
     * 调用其他入库消耗单的接口，重新从存货核算获取单价金额
     *
     * @throws BusinessException
     */
    public void updateIastuffVO() throws BusinessException {
        IIastuffRewriteByCostrevert service = NCLocator.getInstance().lookup(IIastuffRewriteByCostrevert.class);
        service.updateIastuffVOByIA(this.paramVo.getPk_group(), this.paramVo.getPk_org(), this.paramVo.getCperiod());
    }

}

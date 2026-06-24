/**
 *
 */
package nc.bs.cm.costrevert.revert.batchrevert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMNumberUtil;
import nc.bd.framework.base.CMNumberUtil2;
import nc.bd.framework.base.CMStringUtil;
import nc.bd.framework.base.CMValueCheck;
import nc.bs.cm.costrevert.CtRevertScaleUtil;
import nc.bs.cm.costrevert.CtRevertUtil;
import nc.bs.cm.costrevert.maintain.CostRevertDBMaintian;
import nc.bs.cm.costrevert.revert.numcalculate.BgRevertNumCal;
import nc.bs.cm.costrevert.revert.numcalculate.IaStuffNumCal;
import nc.bs.cm.costrevert.revert.numcalculate.NumCalUtil;
import nc.bs.cm.costrevert.revert.numcalculate.ProdCostNumCal;
import nc.bs.cm.costrevert.revert.numcalculate.SubitemCostNumCal;
import nc.bs.framework.common.NCLocator;
import nc.cmpub.business.adapter.BDAdapter;
import nc.pubitf.resa.factor.IFactorPubService;
import nc.vo.bd.material.IMaterialEnumConst;
import nc.vo.bd.material.cost.MaterialCostmodeVO;
import nc.vo.bd.material.prod.MaterialProdVO;
import nc.vo.cm.costrevert.AggCtRevertVO;
import nc.vo.cm.costrevert.CtRevertContainer;
import nc.vo.cm.costrevert.CtRevertHeadVO;
import nc.vo.cm.costrevert.CtRevertItemVO;
import nc.vo.cm.costrevert.CtRevertParamVO;
import nc.vo.cm.costrevert.revertradio.CtRevertRatioAggVO;
import nc.vo.cm.costrevert.revertradio.CtRevertRatioHeadVO;
import nc.vo.cm.costrevert.revertradio.CtRevertRatioItemVO;
import nc.vo.cm.costrevert.revertradio.enumeration.CtRevertRatioIsourcetypeEnum;
import nc.vo.cm.subitemcost.entity.AggSubitemcostVO;
import nc.vo.cm.subitemcost.entity.SubitemcostHeadVO;
import nc.vo.cm.subitemcost.entity.SubitemcostItemVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.resa.factor.FactorVO;

/**
 * @since v6.3
 * @version 2014-1-22 下午04:32:00
 * @author shuzhan
 */
public abstract class AbstractCostRevert {

    /**
     * 参数vo
     */
    protected CtRevertParamVO paramVo;

    /**
     * 业务日期
     */
    protected UFDate businessDatePre = null;

    protected Map<String, List<String>> costClassMaterialMap = null;

    protected Map<String, FactorVO> factorVOMap = new HashMap<String, FactorVO>();

    /**
     * 标准成本还原的结果
     */
    Map<String, AggCtRevertVO> stdCostRevrtResultMap = null;

    protected CtRevertScaleUtil scaleUtil = new CtRevertScaleUtil();

    protected AbstractCostRevert(CtRevertParamVO paramVo) {
        this.paramVo = paramVo;
    }

    /**
     * 还原
     */
    public abstract void doRevertCost() throws BusinessException;

    /**
     * 删除
     */
    public abstract void doDeleteRevertDatas(List<String> matList) throws BusinessException;

    /**
     * 删除数据
     *
     * @throws BusinessException
     */
    protected void deleteRevertDatas(List<String> matList) throws BusinessException {
        // 执行到这里需要 删除当前参数中已经还原数据
        CostRevertDBMaintian.deleteRevertDatas(this.paramVo, matList);
        // 删除期初
        CtRevertUtil.delRevertBgAggVOs(this.paramVo, matList);
        // 删除形态转换数据，dingyma
        CostRevertDBMaintian.deleteTransformRevertDatas(this.paramVo, matList);
        // 删除已有的还原比率，dingyma
        CostRevertDBMaintian.deleteRevertRatioDatas(this.paramVo, matList);
    }

    /**
     * 判断某个表体明细是否是半成品
     *
     * @param item 表体材料
     * @param materialProdVOs 物料的生产信息
     * @param isKey 是否返回带有辅助属性的key
     * @return 半成品物料id或者成本分类id
     * @throws BusinessException
     */
    protected String isHalfProduct(CtRevertItemVO item, Map<String, MaterialProdVO> materialProdVOs, boolean isKey,
            MaterialCostmodeVO costmodeVO, Map<String, AggCtRevertVO> ctRevertRatioMap) throws BusinessException {
        String pk_material = item.getCmaterialid();
        // 如果子项材料为空,可能是费用或者作业等,不需要还原
        if (StringUtil.isEmptyWithTrim(pk_material)) {
            return null;
        }
        MaterialProdVO prodvo = materialProdVOs.get(pk_material);
        if (prodvo == null) {
            return null;
        }
        if (!prodvo.getSfcbdx().booleanValue() || !prodvo.getCostrestore().booleanValue()) {
            return null;
        }
        if (CMValueCheck.isNotEmpty(ctRevertRatioMap)) {// 通过还原比率判断半成品是否是副产品，副产品不还原，作材料处理
            String key = "null|null|" + pk_material + "|" + CtRevertUtil.getAssInfoItemKeyByCostmode(item, costmodeVO);
            if (ctRevertRatioMap.containsKey(key) && CMValueCheck.isEmpty(ctRevertRatioMap.get(key).getChildrenVO())) {
                return null;
            }
        }
        // 如果当前材料在工厂对应的生产信息页签下是否还原为是并且是成本对象,那么此产品需要还原
        if (prodvo.getCostrestore().booleanValue()
                && !prodvo.getSfcbdxtype().equals(IMaterialEnumConst.SFCBDXTYPE_COSTCLASS)) {
            // 如果半成品包含此材料,那么需要再判断当前物料所属的成本分类是否被包含
            if (isKey) {
                return "null|null|" + pk_material + "|" + CtRevertUtil.getAssInfoItemKeyByCostmode(item, costmodeVO);
            }
            return pk_material;
        }
        // 如果半成品不包含此材料,那么需要再判断当前物料所属的成本分类是否被包含
        for (Entry<String, List<String>> entry : this.costClassMaterialMap.entrySet()) {
            if (entry.getValue().contains(pk_material)) {
                if (isKey) {
                    return "null|" + entry.getKey() + "|null|" + CtRevertUtil.getAssInfoItemKey(new CtRevertItemVO());
                }
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 得到物料oid对应的生产信息
     *
     * @param actualProdCostMap 当前层完工成本
     * @param revertBGAggMap 当前层期初成本还原数据
     * @return Map<String, MaterialProdVO> Map<oid, MaterialProdVO>
     * @throws BusinessException
     */
    protected void getMaterialProdVOOidMap(CtRevertContainer container) throws BusinessException {
        // 物料oid集合
        Set<String> matSet = new HashSet<String>();
        // 核算要素id集合
        Set<String> elemIDSet = new HashSet<String>();
        this.getMaterialOidAndElementid(container.getAllAggVOList(), matSet, elemIDSet);
        // 查询物料oid对应的vid
        Map<String, String> oidVidRelation = BDAdapter.convertMaterialid2Vid(matSet.toArray(new String[matSet.size()]));
        // 查询物料vid对应的oid
        Map<String, String> vidOidRelation =
                BDAdapter.convertMaterialvid2Oid(oidVidRelation.values().toArray(new String[oidVidRelation.size()]));
        // 得到物料的生产信息
        if (container.getMaterialProdVOs() != null && oidVidRelation.size() > 0) {
            Map<String, MaterialProdVO> materialProdVOs =
                    BDAdapter.queryMaterialProduceInfoByPks(
                            oidVidRelation.values().toArray(new String[oidVidRelation.size()]),
                            this.paramVo.getPk_org(), CtRevertUtil.CMATERIAL_PROD_QUERY_COLUMN);
            for (Entry<String, MaterialProdVO> entry : materialProdVOs.entrySet()) {
                container.getMaterialProdVOs().put(vidOidRelation.get(entry.getKey()), entry.getValue());
            }
        }
        // 得到物料的成本信息
        if (container.getMaterialCostmodeVOs() != null && oidVidRelation.size() > 0) {
            Map<String, MaterialCostmodeVO> materialCostmodeVOs =
                    BDAdapter.queryMaterialCostmodeInfoByPks(
                            oidVidRelation.values().toArray(new String[oidVidRelation.size()]),
                            this.paramVo.getPk_org(), CtRevertUtil.CMATERIAL_COSTMODE_FREE_COLUMN);
            for (Entry<String, MaterialCostmodeVO> entry : materialCostmodeVOs.entrySet()) {
                container.getMaterialCostmodeVOs().put(vidOidRelation.get(entry.getKey()), entry.getValue());
            }
        }
        // 得到核算要素信息
        if (elemIDSet.size() > 0) {
            this.factorVOMap =
                    NCLocator.getInstance().lookup(IFactorPubService.class)
                            .batchQueryFactorVOByAsoaPKs(elemIDSet.toArray(new String[0]), null);
        }

    }

    /**
     * 查询物料id对应的最新版本id
     *
     * @param levelRevertList
     * @param revertBGAggMap
     * @return
     */
    protected void getMaterialOidAndElementid(List<AggCtRevertVO> allAggVOList, Set<String> matSet,
            Set<String> elemIDSet) {
        if (allAggVOList.size() > 0) {
            for (AggCtRevertVO aggvo : allAggVOList) {
                CtRevertHeadVO headvo = (CtRevertHeadVO) aggvo.getParentVO();
                if (CMStringUtil.isNotEmpty(headvo.getCmaterialid())) {
                    matSet.add(headvo.getCmaterialid());
                }
                for (CircularlyAccessibleValueObject vo : aggvo.getChildrenVO()) {
                    CtRevertItemVO itemvo = (CtRevertItemVO) vo;
                    if (CMStringUtil.isNotEmpty(itemvo.getCmaterialid())) {
                        matSet.add(itemvo.getCmaterialid());
                    }
                    if (CMStringUtil.isNotEmpty(itemvo.getCelementid())) {
                        elemIDSet.add(itemvo.getCelementid());
                    }
                }
            }
        }
    }

    /**
     * 根据物料得到是否成本对象为是，是否还原为是的数据
     *
     * @param materialProdVOs 物料生产信息页签集合
     * @param vidOidRelation 查询物料vid对应的oid
     * @return List<String> 还原为是的oid集合
     * @throws BusinessException
     */
    // protected void getIsCostrestoreMaterials(CtRevertContainer container) throws BusinessException {
    // List<String> costrestoreMaterials = new ArrayList<String>();
    // for (Entry<String, MaterialProdVO> entry : container.getMaterialProdVOs().entrySet()) {
    // if (entry.getValue().getSfcbdx().booleanValue() && entry.getValue().getCostrestore().booleanValue()
    // && !entry.getValue().getSffzfw().booleanValue()) {
    // costrestoreMaterials.add(entry.getKey());
    // }
    // }
    // container.setCostrestoreMaterials(costrestoreMaterials);
    // }

    /**
     * 获取物料oid
     *
     * @param bgBanlanceAggMap
     * @return
     */
    // protected Set<String> getMeaterialOid(AggCtRevertVO[] aggRevertArr) {
    // Set<String> measdocSet = new HashSet<String>();
    // if (aggRevertArr.length > 0) {
    // for (AggCtRevertVO aggvo : aggRevertArr) {
    // CtRevertHeadVO headvo = (CtRevertHeadVO) aggvo.getParentVO();
    // if (CMStringUtil.isNotEmpty(headvo.getCmaterialid())) {
    // measdocSet.add(headvo.getCmaterialid());
    // }
    // for (CircularlyAccessibleValueObject vo : aggvo.getChildrenVO()) {
    // CtRevertItemVO itemvo = (CtRevertItemVO) vo;
    // if (CMStringUtil.isNotEmpty(itemvo.getCmaterialid())) {
    // measdocSet.add(itemvo.getCmaterialid());
    // }
    // }
    // }
    // }
    // return measdocSet;
    // }

    /**
     * 获得半成品集合
     *
     * @param actualProdCostMap 遍历本层完工成本数据
     * @param costrestoreMaterials 成本还原为是的集合
     * @return
     */
    public void getHalfProductList(CtRevertContainer container) {
        // 某个产品消耗的半成品
        List<String> halfProductList = new ArrayList<String>();
        try {
            for (AggCtRevertVO actualProdVO : container.getAllAggVOList()) {
                for (CtRevertItemVO item : (CtRevertItemVO[]) actualProdVO.getChildrenVO()) {
                    // 是否半成品
                    String pk_halfProduct = this.isHalfProduct(item, container.getMaterialProdVOs(), false, null, null);
                    if (pk_halfProduct != null && !halfProductList.contains(pk_halfProduct)) {
                        halfProductList.add(pk_halfProduct);
                    }
                }
            }
        }
        catch (BusinessException e) {
            ExceptionUtils.wrappException(e);
        }
        container.setHalfProductList(halfProductList);
        Set<String> productIdSet = new HashSet<String>();
        productIdSet.addAll(halfProductList);
        container.setProductIdSet(productIdSet);
    }

    /**
     * 耗用下层为材料 不需要还原
     *
     * @param item 表体耗用明细
     */
    protected CtRevertItemVO reSetStuffItems(CtRevertItemVO item) {
        // 本阶成本 = 还原前成本
        item.setThiscost(item.getBeforecost());
        // 本阶数量 = 还原前数量
        item.setThisnumber(item.getBeforenumber());
        // 还原后成本 = 还原前成本
        item.setAftercost(CMNumberUtil2.add(item.getThiscost(), item.getSubcost()));
        // 还原后数量 = 还原前数量
        item.setAfternumber(CMNumberUtil2.add(item.getThisnumber(), item.getSubnumber()));
        return item;
    }

    protected void setItemVOtoMap(CtRevertItemVO item, Map<String, CtRevertItemVO> items) {
        // 把当前childVo放到map里,如果map存在数据则进行汇总(考虑到半成品和产成品之间存在公用材料或者半成品和半成品有公用材料)
        String key = CtRevertUtil.getUniqueKey(item, this.factorVOMap);
        if (items.containsKey(key)) {
            items.put(key, this.sumBodyItemNumber(items.get(key), item));
        }
        else {
            items.put(key, item);
        }
    }

    /**
     * 汇总还原后表体按照要素或者材料的明细数据
     *
     * @param item
     * @param newItem
     */
    private CtRevertItemVO sumBodyItemNumber(CtRevertItemVO item, CtRevertItemVO newItem) {
        // 还原前数量,金额
        newItem.setBeforecost(CMNumberUtil2.add(item.getBeforecost(), newItem.getBeforecost()));
        newItem.setBeforenumber(CMNumberUtil2.add(item.getBeforenumber(), newItem.getBeforenumber()));
        // 还原过程数量,金额
        newItem.setProcesscost(CMNumberUtil2.add(item.getProcesscost(), newItem.getProcesscost()));
        newItem.setProcessnumber(CMNumberUtil2.add(item.getProcessnumber(), newItem.getProcessnumber()));
        // 还原本阶数量,金额
        newItem.setThiscost(CMNumberUtil2.add(item.getThiscost(), newItem.getThiscost()));
        newItem.setThisnumber(CMNumberUtil2.add(item.getThisnumber(), newItem.getThisnumber()));
        // 还原下阶数量,金额
        newItem.setSubcost(CMNumberUtil2.add(item.getSubcost(), newItem.getSubcost()));
        newItem.setSubnumber(CMNumberUtil2.add(item.getSubnumber(), newItem.getSubnumber()));
        // 还原后总数量,金额
        newItem.setAftercost(CMNumberUtil2.add(item.getAftercost(), newItem.getAftercost()));
        newItem.setAfternumber(CMNumberUtil2.add(item.getAfternumber(), newItem.getAfternumber()));
        return newItem;
    }

    /**
     * 还原某个产品下的半成品明细,并且汇总到产品的表体明细数据,并补差
     *
     * @param sumAggVO 半成品本期 期初+本期还原后数据汇总
     * @param item 半成品表体消耗明细
     * @param items 产品还原后耗用的明细
     */
    protected void revertHalfProductCost(AggCtRevertVO sumAggVO, CtRevertItemVO item, Map<String, CtRevertItemVO> items) {
        if (CMValueCheck.isEmpty(sumAggVO)) {
            return;
        }
        CtRevertHeadVO headVo = (CtRevertHeadVO) sumAggVO.getParentVO();
        // 需要补差的VO
        CtRevertItemVO maxCostItem = null;
        // 最终的补差数据(最初是总成本,每次循环会逐步减)
        UFDouble remainCost = item.getBeforecost();
        for (CtRevertItemVO childVo : (CtRevertItemVO[]) sumAggVO.getChildrenVO()) {
            CtRevertItemVO newItem = (CtRevertItemVO) childVo.clone();
            newItem.setPk_ctrevert_b(null);
            UFDouble cost = this.calculateNumber(item.getBeforecost(), newItem.getAftercost(), headVo.getNcost());
            // 还原过程金额
            if (null != cost) {
                cost = cost.setScale(this.scaleUtil.getScaleVO().getMoneyScale(), this.getMoneyRoundType());
            }
            newItem.setProcesscost(cost);
            // 还原过程数量
            UFDouble number = this.calculateNumber(item.getBeforenumber(), newItem.getAfternumber(), headVo.getNnum());
            if (CMStringUtil.isNotEmpty(newItem.getCmeasureid())) {
                if (null != number && CMStringUtil.isNotEmpty(number.toString())) {
                    if (this.scaleUtil.getNumScaleMap().get(newItem.getCmeasureid()) != null) {
                        newItem.setProcessnumber(number.setScale(
                                this.scaleUtil.getNumScaleMap().get(newItem.getCmeasureid()).intValue(),
                                UFDouble.ROUND_HALF_UP));
                    }
                }
            }
            else {
                newItem.setProcessnumber(number);
            }
            // 还原下阶金额
            newItem.setSubcost(newItem.getProcesscost());
            // 还原下阶数量
            newItem.setSubnumber(newItem.getProcessnumber());
            // 还原后金额
            newItem.setAftercost(newItem.getSubcost());
            // 还原后数量
            newItem.setAfternumber(newItem.getSubnumber());
            // 设置物料当前最新版本id
            // if (StringUtil.isEmptyWithTrim(newItem.getCmaterialvid())) {
            // newItem.setCmaterialvid(this.materialRations.get(newItem.getCmaterialid()));
            // }
            remainCost = CMNumberUtil.sub(remainCost, cost);
            if (maxCostItem == null) {
                maxCostItem = newItem;
            }
            else if (maxCostItem.getAftercost() != null && cost != null
                    && cost.compareTo(maxCostItem.getAftercost()) > 0) {
                maxCostItem = newItem;
            }
            // 把当前newItem放到map里,如果map存在数据则进行汇总(考虑到半成品和产成品之间存在公用材料或者半成品和半成品有公用材料)
            this.setItemVOtoMap(newItem, items);
        }
        // 把需要补差的金额设置回map里
        if (null != maxCostItem) {
            maxCostItem = items.get(CtRevertUtil.getUniqueKey(maxCostItem, this.factorVOMap));
            if (maxCostItem.getProcesscost() != null) {
                maxCostItem.setProcesscost(new UFDouble(remainCost).add(maxCostItem.getProcesscost()));
            }
            if (maxCostItem.getSubcost() != null) {
                maxCostItem.setSubcost(new UFDouble(remainCost).add(maxCostItem.getSubcost()));
            }
            if (maxCostItem.getAftercost() != null) {
                maxCostItem.setAftercost(new UFDouble(remainCost).add(maxCostItem.getAftercost()));
            }
            items.put(CtRevertUtil.getUniqueKey(maxCostItem, this.factorVOMap), maxCostItem);
        }

    }

    /**
     * 半成品耗用明细数据作为一条负的记录数据处理
     *
     * @param item 半成品表体消耗明细
     * @param items 产品还原后耗用的明细
     */
    protected void dealHalfProductCost(CtRevertItemVO item, Map<String, CtRevertItemVO> items) {
        item.setPk_org(this.paramVo.getPk_org());
        item.setPk_org_v(this.paramVo.getPk_org_v());
        // 设置还原过程数量,金额
        item.setProcesscost(this.setNegativeForData(item.getBeforecost()));
        item.setProcessnumber(this.setNegativeForData(item.getBeforenumber()));
        // 设置还原后总数量,金额
        item.setAftercost(new UFDouble(0));
        item.setAfternumber(new UFDouble(0));
        // 把当前childVo放到map里,如果map存在数据则进行汇总(考虑到半成品和产成品之间存在公用材料或者半成品和半成品有公用材料)
        String key = this.getUniqueKeyNegative(item);
        if (items.containsKey(key)) {
            items.put(key, this.sumBodyItemNumber(items.get(key), item));
        }
        else {
            items.put(key, item);
        }
        // items.put(this.getUniqueKeyNegative(item), item);
    }

    protected String getUniqueKeyNegative(CtRevertItemVO item) {
        StringBuffer key = new StringBuffer();
        key.append(item.getCmaterialid());
        // key.append(item.getCmaterialvid());
        key.append(item.getCelementid());
        key.append(CtRevertUtil.getAssInfoItemKey(item));
        key.append("-Negative");
        return key.toString();
    }

    /**
     * 对数据取负数(*(-1))
     *
     * @param number
     * @return
     */
    private UFDouble setNegativeForData(UFDouble number) {
        return CMNumberUtil.multiply(number, new UFDouble(-1));
    }

    /**
     * 还原过程计算
     *
     * @param actualItemNumber 实际消耗半成品明细数量/金额
     * @param sumItemNumber 半成品本期期初+本期还原汇总耗用明细材料的数量/金额
     * @param sumHeadNumber 半成品本期期初+本期还原汇总数量/金额
     * @return
     */
    protected UFDouble calculateNumber(UFDouble actualItemNumber, UFDouble sumItemNumber, UFDouble sumHeadNumber) {
        UFDouble ratio = CMNumberUtil2.div(sumItemNumber, sumHeadNumber);
        if (ratio == null) {
            return null;
        }
        return CMNumberUtil.multiply(actualItemNumber, CMNumberUtil2.div(sumItemNumber, sumHeadNumber));
    }

    /**
     * 生成比率信息
     *
     * @param revertMap 已还原完工结果
     * @param revertLfMap 联副产品已还原完工结果
     * @param revertReworkMap 返工已还原完工结果
     * @param iaTransformPerOrderRevertMap 已还原形态转换结果
     * @param revertBGAggMap 本期期初
     * @param subitemCostAggMap 产成品其他入库分项成本数据
     * @return Map<String, AggCtRevertVO> 处理后的比率
     */
    protected void dealCtRevertRatio(CtRevertContainer container) {
        Map<String, AggCtRevertVO> iaTransformPerOrderRevertSumMap = new HashMap<String, AggCtRevertVO>();
        // 合并带成本中心已还原结果
        Map<String, AggCtRevertVO> sumCtRevertByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getRevertMap(), false);
        // Map<String, AggCtRevertVO> sumCtRevertLfByCostcenterMap =
        // this.getSumCtRevertByCostcenterMap(containerClone.getRevertLfMap());
        Map<String, AggCtRevertVO> sumCtRevertReworkByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getRevertReworkMap(), false);
        // Map<String, AggCtRevertVO> sumCtRevertReworkLfByCostcenterMap =
        // this.getSumCtRevertByCostcenterMap(containerClone.getRevertReworkLfMap());
        Map<String, AggCtRevertVO> sumCtRevertOutMapByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getRevertOutMap(), false);
        // 委外返工
        Map<String, AggCtRevertVO> sumCtRevertOutReWorkMapByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getRevertOutReWorkMap(), false);
        // Map<String, AggCtRevertVO> sumCtRevertOutLfByCostcenterMap =
        // this.getSumCtRevertByCostcenterMap(containerClone.getRevertOutLfMap());
        for (Entry<String, List<AggCtRevertVO>> iaTransformPerOrderRevertEntry : container
                .getIaTransformPerOrderRevertMap().entrySet()) {
            iaTransformPerOrderRevertSumMap.put(
                    iaTransformPerOrderRevertEntry.getKey(),
                    this.getSumAggCtRevertDatas(iaTransformPerOrderRevertEntry.getValue().toArray(
                            new AggCtRevertVO[iaTransformPerOrderRevertEntry.getValue().size()])));
        }
        // 期初
        Map<String, AggCtRevertVO> sumRevertBgMapByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getRevertBGAggMap(), false);
        // 其他出入库分项成本
        Map<String, AggCtRevertVO> sumSubitemcostMapByCostcenterMap =
                this.getSumCtRevertByCostcenterMap(container.getSubitemCostAggMap(), false);
        Set<String> sumKey = new HashSet<String>();
        sumKey.addAll(sumCtRevertByCostcenterMap.keySet());
        // sumKey.addAll(sumCtRevertLfByCostcenterMap.keySet());
        sumKey.addAll(sumCtRevertReworkByCostcenterMap.keySet());
        // sumKey.addAll(sumCtRevertReworkLfByCostcenterMap.keySet());
        sumKey.addAll(sumCtRevertOutMapByCostcenterMap.keySet());
        sumKey.addAll(sumCtRevertOutReWorkMapByCostcenterMap.keySet());
        // sumKey.addAll(sumCtRevertOutLfByCostcenterMap.keySet());
        sumKey.addAll(iaTransformPerOrderRevertSumMap.keySet());
        sumKey.addAll(sumRevertBgMapByCostcenterMap.keySet());
        sumKey.addAll(sumSubitemcostMapByCostcenterMap.keySet());
        Map<String, AggCtRevertVO> sumCtRevertMap = new HashMap<String, AggCtRevertVO>();
        for (String key : sumKey) {
            AggCtRevertVO sumCtRevert =
                    this.getSumAggCtRevertDatas(sumCtRevertByCostcenterMap.get(key),
                            sumCtRevertReworkByCostcenterMap.get(key), sumCtRevertOutMapByCostcenterMap.get(key),
                            sumCtRevertOutReWorkMapByCostcenterMap.get(key), iaTransformPerOrderRevertSumMap.get(key),
                            sumRevertBgMapByCostcenterMap.get(key), sumSubitemcostMapByCostcenterMap.get(key));
            CtRevertHeadVO headvo = (CtRevertHeadVO) sumCtRevert.getParent();
            for (CtRevertItemVO ctRevertItemVO : (CtRevertItemVO[]) sumCtRevert.getChildrenVO()) {
                if (CMValueCheck.isNotEmpty(ctRevertItemVO.getCmaterialid())) {
                    ctRevertItemVO.setAfternumber(CMNumberUtil2.div(ctRevertItemVO.getAfternumber(), headvo.getNnum()));
                }
                ctRevertItemVO.setAftercost(CMNumberUtil2.div(ctRevertItemVO.getAftercost(), headvo.getNcost()));
            }
            headvo.setCcostcenterid(null);
            headvo.setNnum(UFDouble.ONE_DBL);
            headvo.setNcost(UFDouble.ONE_DBL);
            sumCtRevertMap.put(key, sumCtRevert);
        }
        container.setSumCtRevertRatioAggVOMap(sumCtRevertMap);
    }

    /**
     * 合并已还原结果
     *
     * @param revertMap 已还原结果集
     * @param isOut 是否委外
     * @return Map<String, AggCtRevertVO> 合并后结果集
     */
    protected Map<String, AggCtRevertVO> getSumCtRevertByCostcenterMap(Map<String, AggCtRevertVO> revertMap,
            boolean isOut) {
        Map<String, AggCtRevertVO> sumCtRevertByCostcenter = new HashMap<String, AggCtRevertVO>();
        for (Entry<String, AggCtRevertVO> entry : revertMap.entrySet()) {
            AggCtRevertVO aggvoClone = (AggCtRevertVO) entry.getValue().clone();
            CtRevertHeadVO headvo = (CtRevertHeadVO) aggvoClone.getParent();
            CtRevertItemVO[] itemvos = (CtRevertItemVO[]) aggvoClone.getAllChildrenVO();
            String headKey =
                    isOut ? "null|" + headvo.getCmarcostclassid() + "|" + headvo.getCmaterialid() + "|"
                            + CtRevertUtil.getAssInfoHeadKey(headvo) + "-SC" : "null|" + headvo.getCmarcostclassid()
                            + "|" + headvo.getCmaterialid() + "|" + CtRevertUtil.getAssInfoHeadKey(headvo);
            if (sumCtRevertByCostcenter.containsKey(headKey)) {
                AggCtRevertVO sumAggVo = sumCtRevertByCostcenter.get(headKey);
                CtRevertHeadVO sumHeadVo = (CtRevertHeadVO) sumAggVo.getParent();
                CtRevertItemVO[] sumItemVos = (CtRevertItemVO[]) sumAggVo.getAllChildrenVO();
                Map<String, CtRevertItemVO> sumItemVosMap = new HashMap<String, CtRevertItemVO>();
                for (CtRevertItemVO itemvo : sumItemVos) {
                    if (itemvo == null || itemvo.getAftercost() != null && itemvo.getAftercost().doubleValue() == 0) {
                        continue;
                    }
                    String itemKey =
                            "" + itemvo.getCmaterialid() + "|" + itemvo.getCelementid() + "|"
                                    + CtRevertUtil.getAssInfoItemKey(itemvo);
                    sumItemVosMap.put(itemKey, itemvo);
                }
                sumHeadVo.setNnum(CMNumberUtil2.add(sumHeadVo.getNnum(), headvo.getNnum()));
                sumHeadVo.setNcost(CMNumberUtil2.add(sumHeadVo.getNcost(), headvo.getNcost()));
                for (CtRevertItemVO itemvo : itemvos) {
                    if (itemvo == null || itemvo.getAftercost() != null && itemvo.getAftercost().doubleValue() == 0) {
                        continue;
                    }
                    String itemKey =
                            "" + itemvo.getCmaterialid() + "|" + itemvo.getCelementid() + "|"
                                    + CtRevertUtil.getAssInfoItemKey(itemvo);
                    if (sumItemVosMap.get(itemKey) != null) {
                        CtRevertItemVO ctRevertItemVO = sumItemVosMap.get(itemKey);
                        ctRevertItemVO.setAftercost(CMNumberUtil2.add(ctRevertItemVO.getAftercost(),
                                itemvo.getAftercost()));
                        ctRevertItemVO.setAfternumber(CMNumberUtil2.add(ctRevertItemVO.getAfternumber(),
                                itemvo.getAfternumber()));
                    }
                    else {
                        itemvo.setBeforecost(null);
                        itemvo.setBeforenumber(null);
                        itemvo.setProcesscost(null);
                        itemvo.setProcessnumber(null);
                        itemvo.setThiscost(null);
                        itemvo.setThisnumber(null);
                        sumItemVosMap.put(itemKey, itemvo);
                    }
                }
                sumAggVo.setChildrenVO(sumItemVosMap.values().toArray(new CtRevertItemVO[sumItemVosMap.size()]));
            }
            else {
                AggCtRevertVO sumAggVo = new AggCtRevertVO();
                sumAggVo.setParentVO(headvo);
                List<CtRevertItemVO> itemList = new ArrayList<CtRevertItemVO>();
                for (CtRevertItemVO itemvo : itemvos) {
                    if (itemvo == null || itemvo.getAftercost() != null && itemvo.getAftercost().doubleValue() == 0) {
                        continue;
                    }
                    itemvo.setBeforecost(null);
                    itemvo.setBeforenumber(null);
                    itemvo.setProcesscost(null);
                    itemvo.setProcessnumber(null);
                    itemvo.setThiscost(null);
                    itemvo.setThisnumber(null);
                    itemList.add(itemvo);
                }
                sumAggVo.setChildrenVO(itemList.toArray(new CtRevertItemVO[itemList.size()]));
                sumCtRevertByCostcenter.put(headKey, sumAggVo);
            }
        }
        return sumCtRevertByCostcenter;
    }

    /**
     * 保存还原结果，包括实际成本还原和形态转换成本还原
     *
     * @throws BusinessException
     */
    protected void saveCtRevertDatas(CtRevertContainer container) throws BusinessException {
        // 保存自制还原结果
        if (container.getRevertMap() != null && container.getRevertMap().size() > 0) {
            AggCtRevertVO[] aggRevertArr =
                    container.getRevertMap().values().toArray(new AggCtRevertVO[container.getRevertMap().size()]);
            CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        }
        // // 保存自制、联副还原结果
        // if (container.getRevertLfMap() != null && container.getRevertLfMap().size() > 0) {
        // AggCtRevertVO[] aggRevertArr =
        // container.getRevertLfMap().values().toArray(new AggCtRevertVO[container.getRevertLfMap().size()]);
        // CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        // }
        // 保存返工、主产品还原结果
        if (container.getRevertReworkMap() != null && container.getRevertReworkMap().size() > 0) {
            AggCtRevertVO[] aggRevertArr =
                    container.getRevertReworkMap().values()
                    .toArray(new AggCtRevertVO[container.getRevertReworkMap().size()]);
            CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        }
        // // 保存返工、联副还原结果
        // if (container.getRevertReworkLfMap() != null && container.getRevertReworkLfMap().size() > 0) {
        // AggCtRevertVO[] aggRevertArr =
        // container.getRevertReworkLfMap().values()
        // .toArray(new AggCtRevertVO[container.getRevertReworkLfMap().size()]);
        // CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        // }
        // 保存委外、主产品还原结果
        if (container.getRevertOutMap() != null && container.getRevertOutMap().size() > 0) {
            AggCtRevertVO[] aggRevertArr =
                    container.getRevertOutMap().values().toArray(new AggCtRevertVO[container.getRevertOutMap().size()]);
            CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        }

        // 保存委外返工还原结果
        if (container.getRevertOutReWorkMap() != null && container.getRevertOutReWorkMap().size() > 0) {
            AggCtRevertVO[] aggRevertArr =
                    container.getRevertOutReWorkMap().values()
                    .toArray(new AggCtRevertVO[container.getRevertOutReWorkMap().size()]);
            CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        }
        // // 保存委外、联副还原结果
        // if (container.getRevertOutLfMap() != null && container.getRevertOutLfMap().size() > 0) {
        // AggCtRevertVO[] aggRevertArr =
        // container.getRevertOutLfMap().values()
        // .toArray(new AggCtRevertVO[container.getRevertOutLfMap().size()]);
        // CostRevertDBMaintian.insertRevertDatas(aggRevertArr);
        // }
        // 形态转换数据的还原结果
        List<AggSubitemcostVO> tempList = new ArrayList<AggSubitemcostVO>();
        for (List<AggCtRevertVO> transformAggRevertVOList : container.getIaTransformPerOrderRevertMap().values()) {
            for (AggCtRevertVO transformAggRevertVO : transformAggRevertVOList) {
                tempList.addAll(this.getAggSubitemcostVOFromAggCtRevertVO(transformAggRevertVO,
                        container.getIaStuffPerOrderAggMap()));
            }
        }
        if (tempList.size() > 0) {
            CostRevertDBMaintian.insertSumitemcostDatas(tempList.toArray(new AggSubitemcostVO[tempList.size()]),
                    this.paramVo);
        }
        UFDate dbusinessdate = BDAdapter.getEndDateByPeriod(this.paramVo.getPk_org(), this.paramVo.getCperiod());
        // 还原比率
        List<CtRevertRatioAggVO> ctRevertRatioAggVOList = new ArrayList<CtRevertRatioAggVO>();
        for (AggCtRevertVO sumRevertVo : container.getSumCtRevertRatioAggVOMap().values()) {// 对于所有物料和成本分类
            if (CMValueCheck.isNotEmpty(sumRevertVo) && CMValueCheck.isNotEmpty(sumRevertVo.getChildrenVO())) {
                ctRevertRatioAggVOList.add(this.buildCtRevertRatioAggVO(sumRevertVo, dbusinessdate));
            }
        }
        if (ctRevertRatioAggVOList.size() > 0) {
            CostRevertDBMaintian.insertRevertRatioDatas(ctRevertRatioAggVOList
                    .toArray(new CtRevertRatioAggVO[ctRevertRatioAggVOList.size()]));
        }
    }

    /**
     * 从成本还原VO转化为其他入库分项成本的VO
     * 形态转换的还原结果保存在“产成品其他入库分项成本”表中，下面将成本还原的VO转化为产成品其他入库分项成本VO
     * 由于需要分单保存还原结果，对于分类法的产品，这里需要按照分类法的还原比率还原各单的数量和金额
     *
     * @param aggCtRevertVO
     * @param costClassMaterialMap
     * @param iaTransformPerOrderAggMap
     * @return
     */
    private List<AggSubitemcostVO> getAggSubitemcostVOFromAggCtRevertVO(AggCtRevertVO aggCtRevertVO,
            Map<String, List<AggCtRevertVO>> iaStuffPerOrderAggMap) {
        List<AggSubitemcostVO> resultList = new ArrayList<AggSubitemcostVO>();
        AggSubitemcostVO aggSubitemcostVO = null;
        CtRevertHeadVO ctRevertHeadVO = (CtRevertHeadVO) aggCtRevertVO.getParentVO();
        CtRevertItemVO[] ctRevertItemVOArr = (CtRevertItemVO[]) aggCtRevertVO.getChildrenVO();
        SubitemcostHeadVO subitemcostHeadVO = null;
        SubitemcostItemVO subitemcostItemVO = null;
        List<SubitemcostItemVO> subitemcostItemVOList = null;
        UFDouble nmoney = null;
        UFDouble nnum = null;
        UFDouble nprice = null;
        UFDate dbusinessdate = BDAdapter.getEndDateByPeriod(this.paramVo.getPk_org(), this.paramVo.getCperiod());
        if (CMValueCheck.isEmpty(ctRevertHeadVO.getCmaterialid())
                && CMValueCheck.isNotEmpty(ctRevertHeadVO.getCmarcostclassid())) {// 分类法
            List<String> costClassMaterialList = this.costClassMaterialMap.get(ctRevertHeadVO.getCmarcostclassid());// 成本分类对应的物料
            if (CMValueCheck.isNotEmpty(costClassMaterialList)) {
                // 按照成本分类对应的物料在形态转换数据中找到对应的分单数据
                List<AggCtRevertVO> transformAggVOList = null;
                CtRevertHeadVO transformHeadVO = null;
                UFDouble leftCost = null;// 尾差
                SubitemcostItemVO maxItemVO = null;// 占比最大项
                for (String costClassMaterialId : costClassMaterialList) {
                    transformAggVOList = iaStuffPerOrderAggMap.get(costClassMaterialId);
                    if (CMValueCheck.isNotEmpty(transformAggVOList)) {
                        for (AggCtRevertVO transformAggVO : transformAggVOList) {
                            transformHeadVO = (CtRevertHeadVO) transformAggVO.getParentVO();// 取出表头VO，后面按照成本分类的还原结果还原表头的数量和金额
                            subitemcostHeadVO = new SubitemcostHeadVO();
                            subitemcostHeadVO.setCmaterialid(transformHeadVO.getCmaterialid());
                            subitemcostHeadVO.setCmeasureid(transformHeadVO.getCmeasureid());
                            subitemcostHeadVO.setCperiod(transformHeadVO.getCperiod());
                            subitemcostHeadVO.setCreationtime(AppContext.getInstance().getServerTime());
                            subitemcostHeadVO.setCreator(AppContext.getInstance().getPkUser());
                            subitemcostHeadVO.setDbusinessdate(dbusinessdate);
                            subitemcostHeadVO.setCtrantypeid(transformHeadVO.getCtrantypeid());
                            subitemcostHeadVO.setIsfromrevert(Integer.valueOf(0));// 来源于成本还原的标记
                            nmoney = transformHeadVO.getNcost();
                            nnum = transformHeadVO.getNnum();
                            nprice = CMNumberUtil2.div(nmoney, nnum);
                            if (CMValueCheck.isNotEmpty(nprice)) {
                                nprice =
                                        nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(),
                                                UFDouble.ROUND_HALF_UP);
                            }
                            subitemcostHeadVO.setNmoney(nmoney);
                            subitemcostHeadVO.setNnumber(nnum);
                            subitemcostHeadVO.setNprice(nprice);
                            subitemcostHeadVO.setPk_group(transformHeadVO.getPk_group());
                            subitemcostHeadVO.setPk_org(transformHeadVO.getPk_org());
                            subitemcostHeadVO.setVsourcecode(transformHeadVO.getVbillcode());
                            subitemcostItemVOList = new ArrayList<SubitemcostItemVO>();
                            leftCost = transformHeadVO.getNcost();
                            maxItemVO = null;
                            for (CtRevertItemVO ctRevertItemVO : ctRevertItemVOArr) {
                                subitemcostItemVO = new SubitemcostItemVO();
                                subitemcostItemVO.setCelementid(ctRevertItemVO.getCelementid());
                                subitemcostItemVO.setCmaterialid(ctRevertItemVO.getCmaterialid());
                                subitemcostItemVO.setCmeasdocid(ctRevertItemVO.getCmeasureid());
                                nmoney =
                                        CtRevertUtil.calculate(transformHeadVO.getNcost(),
                                                ctRevertItemVO.getAftercost(), ctRevertHeadVO.getNcost());// 分配金额
                                if (CMValueCheck.isNotEmpty(nmoney)) {
                                    nmoney =
                                            nmoney.setScale(this.scaleUtil.getScaleVO().getMoneyScale(),
                                                    this.getMoneyRoundType());// 精度
                                }
                                subitemcostItemVO.setNmoney(nmoney);
                                if (CMValueCheck.isNotEmpty(ctRevertItemVO.getAfternumber())) {// 存在数量，进行分配
                                    nnum =
                                            CtRevertUtil.calculate(transformHeadVO.getNnum(),
                                                    ctRevertItemVO.getAfternumber(), ctRevertHeadVO.getNnum());// 分配数量
                                    if (CMValueCheck.isNotEmpty(nnum)) {
                                        nnum =
                                                nnum.setScale(this.scaleUtil.getScaleVO().getMoneyScale(),
                                                        UFDouble.ROUND_HALF_UP);// 精度
                                    }
                                    nprice = CMNumberUtil2.div(nmoney, nnum);
                                    if (CMValueCheck.isNotEmpty(nprice)) {
                                        nprice =
                                                nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(),
                                                        UFDouble.ROUND_HALF_UP);// 精度
                                    }
                                    subitemcostItemVO.setNnumber(nnum);
                                    subitemcostItemVO.setNprice(nprice);
                                }
                                else {// 不存在数量
                                    subitemcostItemVO.setNnumber(null);
                                    subitemcostItemVO.setNprice(null);
                                }
                                subitemcostItemVO.setPk_group(ctRevertItemVO.getPk_group());
                                subitemcostItemVO.setPk_org(ctRevertItemVO.getPk_org());
                                subitemcostItemVO.setCperiod(transformHeadVO.getCperiod());
                                subitemcostItemVOList.add(subitemcostItemVO);
                                if (CMValueCheck.isEmpty(maxItemVO)) {
                                    maxItemVO = subitemcostItemVO;
                                }
                                else {
                                    if (maxItemVO.getNmoney().compareTo(subitemcostItemVO.getNmoney()) < 0) {
                                        maxItemVO = subitemcostItemVO;// 设置占比最大项
                                    }
                                }
                                leftCost = CMNumberUtil.sub(leftCost, subitemcostItemVO.getNmoney());// 计算剩余的金额，最后即为尾差
                            }
                            if (CMValueCheck.isNotEmpty(maxItemVO)) {
                                maxItemVO.setNmoney(CMNumberUtil2.add(maxItemVO.getNmoney(), leftCost));// 设置尾差到占比最大项
                            }
                            aggSubitemcostVO = new AggSubitemcostVO();
                            aggSubitemcostVO.setParentVO(subitemcostHeadVO);
                            aggSubitemcostVO.setChildrenVO(subitemcostItemVOList
                                    .toArray(new SubitemcostItemVO[subitemcostItemVOList.size()]));
                            resultList.add(aggSubitemcostVO);
                        }
                    }
                }
            }
        }
        else {// 品种法
            subitemcostHeadVO = new SubitemcostHeadVO();
            subitemcostHeadVO.setCmaterialid(ctRevertHeadVO.getCmaterialid());
            subitemcostHeadVO.setCmeasureid(ctRevertHeadVO.getCmeasureid());
            subitemcostHeadVO.setCperiod(ctRevertHeadVO.getCperiod());
            subitemcostHeadVO.setCreationtime(AppContext.getInstance().getServerTime());
            subitemcostHeadVO.setCreator(AppContext.getInstance().getPkUser());
            subitemcostHeadVO.setDbusinessdate(dbusinessdate);
            subitemcostHeadVO.setCtrantypeid(ctRevertHeadVO.getCtrantypeid());
            subitemcostHeadVO.setIsfromrevert(Integer.valueOf(0));// 来源于成本还原的标记
            nmoney = ctRevertHeadVO.getNcost();
            nnum = ctRevertHeadVO.getNnum();
            nprice = CMNumberUtil2.div(nmoney, nnum);
            if (CMValueCheck.isNotEmpty(nprice)) {
                nprice = nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(), UFDouble.ROUND_HALF_UP);
            }
            subitemcostHeadVO.setNmoney(nmoney);
            subitemcostHeadVO.setNnumber(nnum);
            subitemcostHeadVO.setNprice(nprice);
            subitemcostHeadVO.setPk_group(ctRevertHeadVO.getPk_group());
            subitemcostHeadVO.setPk_org(ctRevertHeadVO.getPk_org());
            subitemcostHeadVO.setVsourcecode(ctRevertHeadVO.getVbillcode());
            subitemcostHeadVO.setCcustomerid(ctRevertHeadVO.getCcustomerid());
            subitemcostHeadVO.setCproductorid(ctRevertHeadVO.getCproductorid());
            subitemcostHeadVO.setCprojectid(ctRevertHeadVO.getCprojectid());
            subitemcostHeadVO.setCvendorid(ctRevertHeadVO.getCvendorid());
            subitemcostHeadVO.setVfree1(ctRevertHeadVO.getVfree1());
            subitemcostHeadVO.setVfree2(ctRevertHeadVO.getVfree2());
            subitemcostHeadVO.setVfree3(ctRevertHeadVO.getVfree3());
            subitemcostHeadVO.setVfree4(ctRevertHeadVO.getVfree4());
            subitemcostHeadVO.setVfree5(ctRevertHeadVO.getVfree5());
            subitemcostHeadVO.setVfree6(ctRevertHeadVO.getVfree6());
            subitemcostHeadVO.setVfree7(ctRevertHeadVO.getVfree7());
            subitemcostHeadVO.setVfree8(ctRevertHeadVO.getVfree8());
            subitemcostHeadVO.setVfree9(ctRevertHeadVO.getVfree9());
            subitemcostHeadVO.setVfree10(ctRevertHeadVO.getVfree10());
            subitemcostItemVOList = new ArrayList<SubitemcostItemVO>();
            for (CtRevertItemVO ctRevertItemVO : ctRevertItemVOArr) {
                subitemcostItemVO = new SubitemcostItemVO();
                subitemcostItemVO.setCelementid(ctRevertItemVO.getCelementid());
                subitemcostItemVO.setCmaterialid(ctRevertItemVO.getCmaterialid());
                subitemcostItemVO.setCmeasdocid(ctRevertItemVO.getCmeasureid());
                subitemcostItemVO.setCcustomerid(ctRevertItemVO.getCcustomerid());
                subitemcostItemVO.setCproductorid(ctRevertItemVO.getCproductorid());
                subitemcostItemVO.setCprojectid(ctRevertItemVO.getCprojectid());
                subitemcostItemVO.setCvendorid(ctRevertItemVO.getCvendorid());
                subitemcostItemVO.setVbfree1(ctRevertItemVO.getVbfree1());
                subitemcostItemVO.setVbfree2(ctRevertItemVO.getVbfree2());
                subitemcostItemVO.setVbfree3(ctRevertItemVO.getVbfree3());
                subitemcostItemVO.setVbfree4(ctRevertItemVO.getVbfree4());
                subitemcostItemVO.setVbfree5(ctRevertItemVO.getVbfree5());
                subitemcostItemVO.setVbfree6(ctRevertItemVO.getVbfree6());
                subitemcostItemVO.setVbfree7(ctRevertItemVO.getVbfree7());
                subitemcostItemVO.setVbfree8(ctRevertItemVO.getVbfree8());
                subitemcostItemVO.setVbfree9(ctRevertItemVO.getVbfree9());
                subitemcostItemVO.setVbfree10(ctRevertItemVO.getVbfree10());
                nmoney = ctRevertItemVO.getAftercost();
                nnum = ctRevertItemVO.getAfternumber();
                nprice = CMNumberUtil2.div(nmoney, nnum);
                if (CMValueCheck.isNotEmpty(nprice)) {
                    nprice = nprice.setScale(this.scaleUtil.getScaleVO().getPriceScale(), UFDouble.ROUND_HALF_UP);
                }
                subitemcostItemVO.setNmoney(nmoney);
                subitemcostItemVO.setNnumber(nnum);
                subitemcostItemVO.setNprice(nprice);
                subitemcostItemVO.setPk_group(ctRevertItemVO.getPk_group());
                subitemcostItemVO.setPk_org(ctRevertItemVO.getPk_org());
                subitemcostItemVO.setCperiod(ctRevertHeadVO.getCperiod());
                subitemcostItemVOList.add(subitemcostItemVO);
            }
            aggSubitemcostVO = new AggSubitemcostVO();
            aggSubitemcostVO.setParentVO(subitemcostHeadVO);
            aggSubitemcostVO.setChildrenVO(subitemcostItemVOList.toArray(new SubitemcostItemVO[subitemcostItemVOList
                    .size()]));
            resultList.add(aggSubitemcostVO);
        }
        return resultList;
    }

    /**
     * 构建还原比率AggVO
     *
     * @param aggCtRevertVO
     * @return
     */
    protected CtRevertRatioAggVO buildCtRevertRatioAggVO(AggCtRevertVO aggCtRevertVO, UFDate dbusinessdate) {
        CtRevertHeadVO ctRevertHeadVO = (CtRevertHeadVO) aggCtRevertVO.getParentVO();
        CtRevertRatioHeadVO ctRevertRatioHeadVO = new CtRevertRatioHeadVO();
        ctRevertRatioHeadVO.setPk_group(ctRevertHeadVO.getPk_group());
        ctRevertRatioHeadVO.setPk_org(ctRevertHeadVO.getPk_org());
        ctRevertRatioHeadVO.setPk_org_v(this.paramVo.getPk_org_v());
        ctRevertRatioHeadVO.setCperiod(ctRevertHeadVO.getCperiod());
        ctRevertRatioHeadVO.setCmaterialid(ctRevertHeadVO.getCmaterialid());
        ctRevertRatioHeadVO.setCmarcostclassid(ctRevertHeadVO.getCmarcostclassid());
        ctRevertRatioHeadVO.setIsourcetype(Integer.valueOf(CtRevertRatioIsourcetypeEnum.COSTREVERT.getEnumValue()
                .getValue()));
        ctRevertRatioHeadVO.setCreator(AppContext.getInstance().getPkUser());
        ctRevertRatioHeadVO.setCreationtime(AppContext.getInstance().getServerTime());
        if (this.getBusinessDatePre() != null) {
            ctRevertRatioHeadVO.setDbusinessdate(this.getBusinessDatePre());
        }
        else {
            ctRevertRatioHeadVO.setDbusinessdate(dbusinessdate);
        }
        ctRevertRatioHeadVO.setCmeasureid(ctRevertHeadVO.getCmeasureid());
        ctRevertRatioHeadVO.setCcustomerid(ctRevertHeadVO.getCcustomerid());
        ctRevertRatioHeadVO.setCproductorid(ctRevertHeadVO.getCproductorid());
        ctRevertRatioHeadVO.setCprojectid(ctRevertHeadVO.getCprojectid());
        ctRevertRatioHeadVO.setCvendorid(ctRevertHeadVO.getCvendorid());
        ctRevertRatioHeadVO.setVfree1(ctRevertHeadVO.getVfree1());
        ctRevertRatioHeadVO.setVfree2(ctRevertHeadVO.getVfree2());
        ctRevertRatioHeadVO.setVfree3(ctRevertHeadVO.getVfree3());
        ctRevertRatioHeadVO.setVfree4(ctRevertHeadVO.getVfree4());
        ctRevertRatioHeadVO.setVfree5(ctRevertHeadVO.getVfree5());
        ctRevertRatioHeadVO.setVfree6(ctRevertHeadVO.getVfree6());
        ctRevertRatioHeadVO.setVfree7(ctRevertHeadVO.getVfree7());
        ctRevertRatioHeadVO.setVfree8(ctRevertHeadVO.getVfree8());
        ctRevertRatioHeadVO.setVfree9(ctRevertHeadVO.getVfree9());
        ctRevertRatioHeadVO.setVfree10(ctRevertHeadVO.getVfree10());
        CtRevertRatioItemVO ctRevertRatioItemVO = null;
        List<CtRevertRatioItemVO> ctRevertRatioItemVOList = new ArrayList<CtRevertRatioItemVO>();
        if (CMValueCheck.isNotEmpty(aggCtRevertVO.getChildrenVO())) {
            for (CtRevertItemVO ctRevertItemVO : (CtRevertItemVO[]) aggCtRevertVO.getChildrenVO()) {
                ctRevertRatioItemVO = new CtRevertRatioItemVO();
                ctRevertRatioItemVO.setPk_group(ctRevertItemVO.getPk_group());
                ctRevertRatioItemVO.setPk_org(ctRevertItemVO.getPk_org());
                ctRevertRatioItemVO.setPk_org_v(ctRevertItemVO.getPk_org_v());
                ctRevertRatioItemVO.setCelementid(ctRevertItemVO.getCelementid());
                ctRevertRatioItemVO.setCmaterialid(ctRevertItemVO.getCmaterialid());
                if (CMValueCheck.isNotEmpty(ctRevertRatioItemVO.getCmaterialid())) {
                    ctRevertRatioItemVO.setNunitconsume(ctRevertItemVO.getAfternumber());
                }
                ctRevertRatioItemVO.setNunitcost(ctRevertItemVO.getAftercost());
                ctRevertRatioItemVO.setCreator(AppContext.getInstance().getPkUser());
                ctRevertRatioItemVO.setCreationtime(AppContext.getInstance().getServerTime());
                ctRevertRatioItemVO.setCmeasureid(ctRevertItemVO.getCmeasureid());
                ctRevertRatioItemVO.setCcustomerid(ctRevertItemVO.getCcustomerid());
                ctRevertRatioItemVO.setCproductorid(ctRevertItemVO.getCproductorid());
                ctRevertRatioItemVO.setCprojectid(ctRevertItemVO.getCprojectid());
                ctRevertRatioItemVO.setCvendorid(ctRevertItemVO.getCvendorid());
                ctRevertRatioItemVO.setVbfree1(ctRevertItemVO.getVbfree1());
                ctRevertRatioItemVO.setVbfree2(ctRevertItemVO.getVbfree2());
                ctRevertRatioItemVO.setVbfree3(ctRevertItemVO.getVbfree3());
                ctRevertRatioItemVO.setVbfree4(ctRevertItemVO.getVbfree4());
                ctRevertRatioItemVO.setVbfree5(ctRevertItemVO.getVbfree5());
                ctRevertRatioItemVO.setVbfree6(ctRevertItemVO.getVbfree6());
                ctRevertRatioItemVO.setVbfree7(ctRevertItemVO.getVbfree7());
                ctRevertRatioItemVO.setVbfree8(ctRevertItemVO.getVbfree8());
                ctRevertRatioItemVO.setVbfree9(ctRevertItemVO.getVbfree9());
                ctRevertRatioItemVO.setVbfree10(ctRevertItemVO.getVbfree10());
                ctRevertRatioItemVOList.add(ctRevertRatioItemVO);
            }
        }
        CtRevertRatioAggVO ctRevertRatioAggVO = new CtRevertRatioAggVO();
        ctRevertRatioAggVO.setParentVO(ctRevertRatioHeadVO);
        ctRevertRatioAggVO.setChildrenVO(ctRevertRatioItemVOList
                .toArray(new CtRevertRatioItemVO[ctRevertRatioItemVOList.size()]));
        return ctRevertRatioAggVO;
    }

    /**
     * 根据当前层的产品或成本分类，得到所有的产品，将其中的分类转化为产品，dingyma
     *
     * @param thisLevelProductOrCostClassIdList
     * @return
     */
    protected void getThisLevelProductIdSet(List<String> thisLevelProductOrCostClassIdList, CtRevertContainer container) {
        Set<String> resultSet = new HashSet<String>();
        for (String thisLevelProductOrCostClassId : thisLevelProductOrCostClassIdList) {
            if (this.costClassMaterialMap.containsKey(thisLevelProductOrCostClassId)) {
                resultSet.addAll(this.costClassMaterialMap.get(thisLevelProductOrCostClassId));
            }
            else {
                resultSet.add(thisLevelProductOrCostClassId);
            }
        }
        container.setThisLevelProductIdSet(resultSet);
    }

    /**
     * 获得合并后多个还原前成本还原数据，参数可多个且可空
     * 表体还原后为0的数据过滤掉，表体还原前的数据清空
     *
     * @param ctRevertAggVOs 成本还原涉及到的所有vo
     * @return AggCtRevertVO 合并后的单个vo
     */
    public AggCtRevertVO getSumAggCtRevertDatas(AggCtRevertVO... ctRevertAggVOs) {
        AggCtRevertVO sumAggVO = null;
        CtRevertHeadVO headVo = null;
        Map<String, CtRevertItemVO> items = new HashMap<String, CtRevertItemVO>();
        for (AggCtRevertVO ctRevertVO : ctRevertAggVOs) {
            if (ctRevertVO != null) {
                sumAggVO = (AggCtRevertVO) ctRevertVO.clone();
                if (headVo == null) {
                    headVo = (CtRevertHeadVO) sumAggVO.getParentVO();
                    for (CtRevertItemVO item : (CtRevertItemVO[]) sumAggVO.getChildrenVO()) {
                        // 过滤还原后产品中的耗用的半成品数据,还原后的金额是空 或者金额是0
                        if (item.getAftercost() == null || UFDouble.ZERO_DBL.equals(item.getAftercost())) {
                            continue;
                        }
                        items.put(CtRevertUtil.getUniqueKey(item, this.factorVOMap), item);
                    }
                }
                else {
                    headVo.setNcost(CMNumberUtil2.add(headVo.getNcost(),
                            ((CtRevertHeadVO) sumAggVO.getParentVO()).getNcost()));
                    headVo.setNnum(CMNumberUtil2.add(headVo.getNnum(),
                            ((CtRevertHeadVO) sumAggVO.getParentVO()).getNnum()));
                    sumAggVO.setParentVO(headVo);
                    for (CtRevertItemVO item : (CtRevertItemVO[]) sumAggVO.getChildrenVO()) {
                        // 过滤还原后产品中的耗用的半成品数据,还原后的金额是空 或者金额是0
                        if (item.getAftercost() == null || UFDouble.ZERO_DBL.equals(item.getAftercost())) {
                            continue;
                        }
                        if (items.containsKey(CtRevertUtil.getUniqueKey(item, this.factorVOMap))) {
                            CtRevertItemVO itemvo = items.get(CtRevertUtil.getUniqueKey(item, this.factorVOMap));
                            itemvo.setAftercost(CMNumberUtil2.add(item.getAftercost(), itemvo.getAftercost()));
                            itemvo.setAfternumber(CMNumberUtil2.add(item.getAfternumber(), itemvo.getAfternumber()));
                        }
                        else {
                            items.put(CtRevertUtil.getUniqueKey(item, this.factorVOMap), item);
                        }
                    }
                }
            }
        }
        if (sumAggVO != null && items.size() > 0) {
            for (CtRevertItemVO item : items.values()) {
                item.setBeforecost(null);
                item.setBeforenumber(null);
                item.setProcesscost(null);
                item.setProcessnumber(null);
                item.setThiscost(null);
                item.setThisnumber(null);
                item.setSubcost(null);
                item.setSubnumber(null);
            }
            sumAggVO.setChildrenVO(items.values().toArray(new CtRevertItemVO[0]));
        }
        return sumAggVO;
    }

    /**
     * 获得 businessDatePre 的属性值
     *
     * @return the businessDatePre
     * @since 2014-3-27
     * @author shuzhan
     */
    public UFDate getBusinessDatePre() {
        return this.businessDatePre;
    }

    /**
     * 设置 businessDatePre 的属性值
     *
     * @param businessDatePre the businessDatePre to set
     * @since 2014-3-27
     * @author shuzhan
     */
    public void setBusinessDatePre(UFDate businessDatePre) {
        this.businessDatePre = businessDatePre;
    }

    // 获得完工与期初数据
    public void getPrepareDatas(List<String> batchMtrPKList, CtRevertContainer container) throws BusinessException {
        // 获取本期当前层自制、主产品完工成本
        this.paramVo.setProdCostSource(Integer.valueOf(1));
        container.setActualProdCostMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // // 获取本期当前层自制、联副完工成本
        // this.paramVo.setProdCostSource(Integer.valueOf(2));
        // container.setActualProdCostLfMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // 获取本期当前层返工、主产品完工成本
        this.paramVo.setProdCostSource(Integer.valueOf(2));
        container.setActualProdCostReworkMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // // 获取本期当前层返工、联副完工成本
        // this.paramVo.setProdCostSource(Integer.valueOf(4));
        // container.setActualProdCostReworkLfMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // 获取本期当前层委外、主产品完工成本
        this.paramVo.setProdCostSource(Integer.valueOf(3));
        container.setActualProdCostOutMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // 获取本期委外返工成本
        this.paramVo.setProdCostSource(Integer.valueOf(4));
        container.setActualProdCostOutReWorkMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // // 获取本期当前层委外、联副完工成本
        // this.paramVo.setProdCostSource(Integer.valueOf(6));
        // container.setActualProdCostOutLfMap(ProdCostNumCal.calNum(this.paramVo, batchMtrPKList));
        // 获得当前层期初成本还原数据
        container.setRevertBGAggMap(BgRevertNumCal.calNum(this.paramVo, batchMtrPKList));
    }

    // 获得其他出入库消耗单与产成品其他入库分项成本数据
    public void getIaStuffAndSubitem(CtRevertContainer container) throws BusinessException {
        // 获得当前层的其他出入库消耗单数据
        container.setIaStuffPerOrderAggMap(IaStuffNumCal.calPerOrderNum(this.paramVo,
                container.getThisLevelProductIdSet()));
        // 获得当前层的其他出入库消耗单数据，对成本分类进行合并的数据，如果存在成本分类List中仅有一条数据
        container.setIaStuffPerOrderSumAggMap(NumCalUtil.dealPerOrderCostClassDatas(
                container.getIaStuffPerOrderAggMap(), this.costClassMaterialMap));
        // 获得当前层的产成品其他入库分项成本数据
        container
                .setSubitemCostAggMap(NumCalUtil.dealCostClassDatas(
                        SubitemCostNumCal.calNum(this.paramVo, container.getThisLevelProductIdSet()),
                        this.costClassMaterialMap));
    }

    // 汇总完工成本、期初成本还原数据、其他出入库消耗单和产成品其它入库分项成本数据
    public void getAllAggVOList(CtRevertContainer container) {
        List<AggCtRevertVO> allAggVOList = new ArrayList<AggCtRevertVO>();
        allAggVOList.addAll(container.getActualProdCostMap().values());
        // allAggVOList.addAll(container.getActualProdCostLfMap().values());
        allAggVOList.addAll(container.getActualProdCostReworkMap().values());
        // allAggVOList.addAll(container.getActualProdCostReworkLfMap().values());
        allAggVOList.addAll(container.getActualProdCostOutMap().values());
        // allAggVOList.addAll(container.getActualProdCostOutLfMap().values());
        allAggVOList.addAll(container.getActualProdCostOutReWorkMap().values());
        allAggVOList.addAll(container.getRevertBGAggMap().values());
        for (List<AggCtRevertVO> tempList : container.getIaStuffPerOrderAggMap().values()) {
            allAggVOList.addAll(tempList);
        }
        for (List<AggCtRevertVO> tempList : container.getIaStuffPerOrderSumAggMap().values()) {
            allAggVOList.addAll(tempList);
        }
        allAggVOList.addAll(container.getSubitemCostAggMap().values());
        container.setAllAggVOList(allAggVOList);
    }

    private int moneyRoundType = -1;

    protected int getMoneyRoundType() {
        if (-1 == this.moneyRoundType) {
            this.moneyRoundType = BDAdapter.getCurrencyRoundType(this.paramVo.getPk_org());
        }
        return this.moneyRoundType;
    }

}

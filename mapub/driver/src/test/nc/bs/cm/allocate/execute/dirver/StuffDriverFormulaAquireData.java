/**
 *
 */
package nc.bs.cm.allocate.execute.dirver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMCollectionUtil;
import nc.bd.framework.base.CMStringUtil;
import nc.bs.cm.allocate.execute.dirver.handler.ActualActivityNumberHandler;
import nc.bs.cm.allocate.execute.dirver.handler.ActualStuffNumberHandler;
import nc.bs.cm.allocate.execute.dirver.handler.AllocfacHandler;
import nc.bs.cm.allocate.execute.dirver.handler.AssignStuffActualHandler;
import nc.bs.cm.allocate.execute.dirver.handler.BOMActivityNumberHandler;
import nc.bs.cm.allocate.execute.dirver.handler.BomStuffConsumeQuotaHandler;
import nc.bs.cm.allocate.execute.dirver.handler.FactorHandler;
import nc.bs.cm.allocate.execute.dirver.handler.KGIcMaterilOutHandler;
import nc.bs.cm.allocate.execute.dirver.handler.MOStuffConsumeQuotaHandler;
import nc.bs.cm.allocate.execute.dirver.handler.MainStuffConsumeQuotaHandler;
import nc.bs.cm.allocate.execute.dirver.handler.OtherHandler;
import nc.bs.cm.allocate.execute.dirver.handler.PriceLibraryHandler;
import nc.bs.cm.allocate.execute.dirver.handler.PriceSourceHandler;
import nc.bs.cm.allocate.execute.dirver.handler.ProductNumberHandler;
import nc.bs.cm.allocate.execute.dirver.handler.RTActivityNumberHandler;
import nc.bs.cm.allocate.execute.dirver.handler.RTStuffConsumeQuotaHandler;
import nc.bs.cm.allocate.execute.dirver.handler.ReferenceSalePriceHandler;
import nc.bs.cm.allocate.execute.dirver.handler.StandardCostHandler;
import nc.bs.cm.allocate.execute.dirver.util.AssInfoConverterUtil;
import nc.cmpub.business.adapter.BDAdapter;
import nc.cmpub.business.enumeration.CMPubPriceSourceEnum;
import nc.pubitf.mapub.driver.cm.allocate.IDriverFormulaAquireData;
import nc.vo.cm.allocate.entity.AllocateLangConst;
import nc.vo.cm.allocate.entity.record.AllocateRecordHeadVO;
import nc.vo.cm.costobject.entity.CostObjectVO;
import nc.vo.cm.costobject.enumeration.CostObjInStorageTypeEnum;
import nc.vo.cmpub.framework.assistant.CMAssInfoParamVO;
import nc.vo.cmpub.framework.assistant.CMMarAssInfoVO;
import nc.vo.cmpub.framework.assistant.StdCostParamVO;
import nc.vo.mapub.allocfac.enumeration.AllocfacEnum;
import nc.vo.mapub.driver.entity.CMDriverLangConst;
import nc.vo.mapub.driver.entity.CMDriverParameterEnum;
import nc.vo.mapub.driver.entity.CMDriverParameterEnum_JZ;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.pub.MapSet;

/**
 * IDriverFormulaAquireData的默认实现类
 * 动因计算取数使用
 * 已使用树状缓存、分级优化
 *
 * @since 6.0
 * @version 2012-8-29 上午09:01:02
 * @author liyjf
 */
public class StuffDriverFormulaAquireData implements IDriverFormulaAquireData {

    protected String pkGroup;

    protected String pkOrg;

    protected String period;

    protected AssInfoConverterUtil converter;

    public StuffDriverFormulaAquireData(String pkGroup, String pkOrg, String period) {
        this.pkGroup = pkGroup;
        this.pkOrg = pkOrg;
        this.period = period;
        this.converter = new AssInfoConverterUtil(pkGroup, pkOrg);
    }

    private boolean isCTCSign = false;

    public boolean isCTCSign() {
        return this.isCTCSign;
    }

    public void setCTCSign(boolean isCTCSign) {
        this.isCTCSign = isCTCSign;
    }

    /**
     * 分配记录表头VO
     */
    private AllocateRecordHeadVO beAllocateInfo;

    public AllocateRecordHeadVO getBeAllocateInfo() {
        return this.beAllocateInfo;
    }

    public void setBeAllocateInfo(AllocateRecordHeadVO beAllocateVO) {
        this.beAllocateInfo = beAllocateVO;
    }

    @Override
    public boolean hasMaterialId() {
        return CMStringUtil.isNotEmpty(this.beAllocateInfo.getCmaterialid());
    }

    /**
     * 成本对象
     */
    private MapSet<String, String> centerObjectMapSet;

    public void setcenterObjectMapSet(MapSet<String, String> centerObjectMapSet) {
        this.centerObjectMapSet = centerObjectMapSet;
    }

    public MapSet<String, String> getcenterObjectMapSet() {
        return this.centerObjectMapSet;
    }

    private String[] getCostcenterIds() {
        Set<String> costcenterIdSet = new HashSet<String>();
        for (Entry<String, Set<String>> entry : this.centerObjectMapSet.toMap().entrySet()) {
            costcenterIdSet.add(entry.getKey());
        }
        return costcenterIdSet.toArray(new String[costcenterIdSet.size()]);
    }

    protected String[] getCostobjIds() {
        Set<String> costobjIdSet = new HashSet<String>();
        for (Entry<String, Set<String>> entry : this.centerObjectMapSet.toMap().entrySet()) {
            costobjIdSet.addAll(entry.getValue());
        }
        return costobjIdSet.toArray(new String[costobjIdSet.size()]);
    }

    private String prePeriod = null;

    public String getPrePeriod() {
        return this.prePeriod;
    }

    public void setPrePeriod(String prePeriod) {
        this.prePeriod = prePeriod;
    }

    protected UFDate useDate;

    public void setUseDate(UFDate useDate) {
        this.useDate = useDate;
    }

    /**
     * Map<类型，Object> Object:
     * 1.UFDouble动因量;
     * 2.Map<成本中心，UFDouble动因量>;
     * 3.Map<成本中心，Map<成本对象，UFDouble动因量>>;
     * 4.Map<AssInfoVO，UFDouble动因量>;
     */
    protected Map<Object, Object> variableMap;

    @Override
    public Map<Object, Object> getVariableMap() {
        return this.variableMap;
    }

    @Override
    public void setVariableMap(Map<Object, Object> variableMap) {
        this.variableMap = variableMap;
    }

    /**
     * 工作中心缓存
     */
    private Map<String, String[]> Center2WorkCenterMap = new HashMap<String, String[]>();

    private String checkCostType(String costType) throws BusinessException {
        if (costType == null) {
            ExceptionUtils.wrappBusinessException(AllocateLangConst.getERR_NO_COSTTYPE());
        }
        return costType;
    }

    @Override
    public void aquireQualifiedNumber(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.QUALIFIED_NUMBER));
    }

    @Override
    public void aquireOnProductRate(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.ON_PRODUCT_RATE));
    }

    @Override
    public void aquireJointQualifiedNumber(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.JOINT_QUALIFIED_NUMBER));
    }

    @Override
    public void aquireByProductNumber(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.BY_PRODUCT_NUMBER));
    }

    @Override
    public void aquireWasteProductNumber(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.WASTEPRODUCT_NUMBER));
    }

    @Override
    public void aquireWasteProductRate(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.WASTEPRODUCT_RATE));
    }

    @Override
    public void aquireJointWasteProductNumber(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.JOINT_WASTEPRODUCT_NUMBER));
    }

    @Override
    public void aquireJointWasteProductRate(String variable) throws BusinessException {
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(this.period,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.JOINT_WASTEPRODUCT_RATE));
    }

    @Override
    public void aquireBomStuffConsumeQuota(String variable) throws BusinessException {
        this.variableMap.put(variable, new BomStuffConsumeQuotaHandler().aquireBomStuffConsumeQuota(this.pkGroup,
                this.pkOrg, this.useDate, this.converter.convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds()), this
                        .getBeAllocateInfo().getCmaterialid()));
    }

    @Override
    public void aquireAssinStuffBomQuota(String stuffId, String variable) throws BusinessException {
        this.variableMap.put(variable, new BomStuffConsumeQuotaHandler().aquireAssinStuffBomQuota(this.pkGroup,
                this.pkOrg, this.useDate, this.converter.convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds()),
                stuffId));
    }

    @Override
    public void aquireRTStuffConsumeQuota(String variable) throws BusinessException {
        this.variableMap.put(variable, new RTStuffConsumeQuotaHandler().aquireRTStuffConsumeQuota(this.pkGroup,
                this.pkOrg, this.useDate, this.getCostcenterIds(), this.Center2WorkCenterMap, this.converter
                        .convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds()), this.getBeAllocateInfo()
                        .getCmaterialid()));
    }

    @Override
    public void aquireRTActivityNumber(String activityID, String variable) throws BusinessException {
        this.variableMap.put(variable, new RTActivityNumberHandler().aquireRTActivityNumber(this.pkGroup, this.pkOrg,
                activityID, this.useDate, this.getCostcenterIds(),
                this.converter.convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds())));
    }

    @Override
    public void aquireMOStuffConsumeQuota(String variable) throws BusinessException {
        this.variableMap.put(variable, new MOStuffConsumeQuotaHandler().aquireMOStuffConsumeQuota(this.pkGroup,
                this.pkOrg, this.converter.convertCostobjs2MoCodeSet(this.getCostobjIds()), this.getBeAllocateInfo()
                        .getCmaterialid()));
    }

    @Override
    public void aquireMainStuffConsumeQuota(String variable) throws BusinessException {
        this.variableMap.put(variable, new MainStuffConsumeQuotaHandler().aquireMainStuffConsumeQuota(this.pkGroup,
                this.pkOrg, this.useDate, this.converter.convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds())));
    }

    @Override
    public void aquireActualStuffNumber(String variable) throws BusinessException {
        if (this.isCTCSign) {
            this.variableMap.put(variable, new ActualStuffNumberHandler().aquireActualStuffNumberForCenter(
                    this.pkGroup, this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet().keySet(), this
                            .getBeAllocateInfo().getCmaterialid()));
        }
        else {
            this.variableMap.put(variable, new ActualStuffNumberHandler().aquireActualStuffNumber(this.pkGroup,
                    this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet(), this.getBeAllocateInfo()
                            .getCmaterialid()));
        }
    }

    @Override
    public void aquireAssignStuffActualNumber(String materialvid, String variable) throws BusinessException {
        //2020-12-08 liyf  九州药业扩展成本动因
    	//接口有多个实现类型，改动影响范围太大，因此在此处变通实现
    	if(materialvid.equals(CMDriverParameterEnum_JZ.ACTUAL_STUFF_NUMBER_KG.getCode())){
    		 if (this.isCTCSign) {
    	            this.variableMap.put(variable, new KGIcMaterilOutHandler().aquireAssignStuffActualNumberForCenter(
    	                    this.pkGroup, this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet().keySet(), this
    	                            .getBeAllocateInfo().getCmaterialid()));
    	        }
    	        else {
    	            this.variableMap.put(variable, new KGIcMaterilOutHandler().aquireAssignStuffActualNumber(this.pkGroup,
    	                    this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet(), this.getBeAllocateInfo()
    	                            .getCmaterialid()));
    	        }
   	
    		
    	}else{
    		 if (this.isCTCSign) {
    	            this.variableMap.put(variable, new AssignStuffActualHandler().aquireAssignStuffActualNumberForCenter(
    	                    this.pkGroup, this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet().keySet(),
    	                    materialvid));
    	        }
    	        else {
    	            this.variableMap.put(variable, new AssignStuffActualHandler().aquireAssignStuffActualNumber(this.pkGroup,
    	                    this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet(), materialvid));
    	        }
    	}
       
    }

    @Override
    public void aquireAssignStuffActualMoney(String materialvid, String variable) throws BusinessException {
        if (this.isCTCSign) {
            this.variableMap.put(variable, new AssignStuffActualHandler().aquireAssignStuffActualMoneyForCenter(
                    this.pkGroup, this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet().keySet(),
                    materialvid));
        }
        else {
            this.variableMap.put(variable, new AssignStuffActualHandler().aquireAssignStuffActualMoney(this.pkGroup,
                    this.pkOrg, this.period, this.prePeriod, this.getcenterObjectMapSet(), materialvid));
        }
    }

    @Override
    public void aquirePlanPrice(String variable) throws BusinessException {
        this.variableMap.put(variable, new PriceSourceHandler().aquirePrice(CMPubPriceSourceEnum.PLAN, this.pkGroup,
                this.pkOrg, this.converter.convertCostobjs2MaroIds(this.getCostobjIds())));
    }

    @Override
    public void aquireReferenceCost(String variable) throws BusinessException {
        this.variableMap.put(variable, new PriceSourceHandler().aquirePrice(CMPubPriceSourceEnum.REFERENCE,
                this.pkGroup, this.pkOrg, this.converter.convertCostobjs2MaroIds(this.getCostobjIds())));
    }

    @Override
    public void aquireReferenceSalePrice(String[] saleOrgIDs, String variable) throws BusinessException {
        this.variableMap.put(
                variable,
                new ReferenceSalePriceHandler().aquirePrice(saleOrgIDs,
                        this.converter.convertCostobjs2MarvIds(this.getCostobjIds())));
    }

    @Override
    public void aquireStandardCost(String costType, String variable) throws BusinessException {
        List<StdCostParamVO> paramList = new ArrayList<StdCostParamVO>();
        for (Entry<String, Set<String>> entry : this.getcenterObjectMapSet().entrySet()) {
            Set<CostObjectVO> costObjectVOSet =
                    this.converter.convertCostobjs2CostobjInfoSet(entry.getValue().toArray(
                            new String[entry.getValue().size()]));
            if (CMCollectionUtil.isEmpty(costObjectVOSet)) {
                return;
            }
            for (CostObjectVO costObjectVO : costObjectVOSet) {
                StdCostParamVO stdParamVO = new StdCostParamVO();
                stdParamVO.setCcostcenterid(entry.getKey());
                stdParamVO.setCmaterialid(costObjectVO.getCmaterialid());
                if (costObjectVO.getFinstoragetype().intValue() == CostObjInStorageTypeEnum.HOME_MAKE.toIntValue()
                        || costObjectVO.getFinstoragetype().intValue() == CostObjInStorageTypeEnum.RE_WORK.toIntValue()) {
                    // 枚举转换，标准成本暂时没有常量可用
                    stdParamVO.setFinstoragetype(Integer.valueOf(1));
                }
                else {
                    // 枚举转换，标准成本暂时没有常量可用
                    stdParamVO.setFinstoragetype(Integer.valueOf(3));
                }
                stdParamVO.setCcustomerid(costObjectVO.getCcustomerid());
                stdParamVO.setCprojectid(costObjectVO.getCprojectid());
                stdParamVO.setCvendorid(costObjectVO.getCvendorid());
                stdParamVO.setCproductorid(costObjectVO.getCproductorid());
                stdParamVO.setVfree1(costObjectVO.getVfree1());
                stdParamVO.setVfree2(costObjectVO.getVfree2());
                stdParamVO.setVfree3(costObjectVO.getVfree3());
                stdParamVO.setVfree4(costObjectVO.getVfree4());
                stdParamVO.setVfree5(costObjectVO.getVfree5());
                stdParamVO.setVfree6(costObjectVO.getVfree6());
                stdParamVO.setVfree7(costObjectVO.getVfree7());
                stdParamVO.setVfree8(costObjectVO.getVfree8());
                stdParamVO.setVfree9(costObjectVO.getVfree9());
                stdParamVO.setVfree10(costObjectVO.getVfree10());
                paramList.add(stdParamVO);
            }
        }

        this.variableMap.put(variable,
                new StandardCostHandler().aquireStandardCost(this.pkOrg, this.checkCostType(costType), paramList));
    }

    @Override
    public void aquirePriceLibrary(String[] priceLibraryIDs, String variable) throws BusinessException {
        this.variableMap.put(
                variable,
                new PriceLibraryHandler().aquirePrice(priceLibraryIDs,
                        this.converter.convertCostobjs2MarAssInfoSet(this.getCostobjIds())));
    }

    @Override
    public void aquireActualActivityNumber(String activityID, String variable) throws BusinessException {
        if (this.isCTCSign) {
            this.variableMap.put(variable, new ActualActivityNumberHandler().aquireActualActivityNumberForCenter(
                    this.pkOrg, this.period, activityID, this.getcenterObjectMapSet().keySet()));
        }
        else {
            this.variableMap.put(variable, new ActualActivityNumberHandler().aquireActualActivityNumber(this.pkOrg,
                    this.period, activityID, this.getcenterObjectMapSet()));
        }
    }

    @Override
    public void aquireActualActivityFinishNumber(String activityID, String variable) throws BusinessException {
        if (this.isCTCSign) {
            this.variableMap.put(variable, new ActualActivityNumberHandler().aquireActualActivityFinishNumberForCenter(
                    this.pkOrg, this.period, activityID, this.getcenterObjectMapSet().keySet()));
        }
        else {
            this.variableMap.put(variable, new ActualActivityNumberHandler().aquireActualActivityFinishNumber(
                    this.pkOrg, this.period, activityID, this.getcenterObjectMapSet()));
        }
    }

    @Override
    public void aquireStandardActivityNumber(String activityID, String costType, String variable)
            throws BusinessException {
        ExceptionUtils.wrappBusinessException("暂不支持标准成本作业");
    }

    @Override
    public void aquireBOMActivityNumber(String activityID, String variable) throws BusinessException {
        this.variableMap.put(variable, new BOMActivityNumberHandler().aquireBOMActivityNumber(this.pkOrg, activityID,
                this.useDate, this.converter.convertCostobjs2MaroIdvIdParamSet(this.getCostobjIds())));
    }

    @Override
    public void aquireAllocfac(String allocfacID, String allocfacType, String variable) throws BusinessException {
        this.variableMap.put(
                variable,
                new AllocfacHandler().aquireAllocfac(this.pkGroup, this.pkOrg, allocfacID, allocfacType,
                        this.getCostcenterIds(), this.getCostobjIds(), this.converter));
    }

    @Override
    public void aquireFactor(String factorID, String variable) throws BusinessException {
        if (this.isCTCSign) {
            this.variableMap.put(
                    variable,
                    new FactorHandler().aquireFactorForCenter(this.pkGroup, this.pkOrg, this.period,
                            this.getPrePeriod(), factorID, this.getcenterObjectMapSet().keySet()));
        }
        else {
            this.variableMap.put(variable, new FactorHandler().aquireFactor(this.pkGroup, this.pkOrg, this.period,
                    this.getPrePeriod(), factorID, this.getcenterObjectMapSet()));
        }
    }

    @Override
    public void aquireOther(String code, String variable) throws BusinessException {
        this.variableMap.put(variable,
                new OtherHandler().aquireOther(code, this.converter.convertCostobjs2MarvIds(this.getCostobjIds())));

    }

    @Override
    public void aquireBomJointByOutputQuota(String type) throws BusinessException {
        ExceptionUtils.wrappBusinessException(AllocateLangConst.getERR_NO_SURPORT_BOMOUTPUT());
        return;
    }

    @Override
    public void aquireInproApproNum(String variable) throws BusinessException {
        String beginPeriod = BDAdapter.getBeginAccount(this.pkOrg, Boolean.TRUE);
        // 设置上期在产约当量默认为期初期间(如2015-00)
        String prePeriod = BDAdapter.getBeginAccount(this.pkOrg, Boolean.FALSE);
        if (this.period.compareTo(beginPeriod) > 0) {
            prePeriod = BDAdapter.getPrePeriod(this.pkOrg, this.period);
        }
        this.variableMap.put(variable, new ProductNumberHandler().aquireProductNumber(prePeriod,
                this.getcenterObjectMapSet(), CMDriverParameterEnum.INPRO_APPRONUM));
    }

    @Override
    public List<Object> getVariableKeyList(String variable, String costcenterId, String costobjId, String celementId,
            CMMarAssInfoVO itemMarAssInfoVO, Map<String, Object> formulaMap) {
        List<Object> variableKeyList = new ArrayList<Object>();
        String paramcode = (String) formulaMap.get("paramCode");
        String[] paramValue1 = (String[]) formulaMap.get("paramValue1");
        String singelValue1 = null;
        if (paramValue1 != null) {
            singelValue1 = paramValue1[0];
        }
        if (paramcode.equals(CMDriverParameterEnum.REFERENCE_SALE_PRICE.getCode()) // 参考销售价
                || paramcode.equals(CMDriverLangConst.OTHER)) { // 其他
            variableKeyList.add(variable);
            variableKeyList.add(this.converter.convertCostobj2MarvId(costobjId));
        }
        else if (paramcode.equals(CMDriverParameterEnum.PLAN_PRICE.getCode()) // 计划价
                || paramcode.equals(CMDriverParameterEnum.REFERENCE_COST.getCode())) {// 参考成本
            // || paramcode.equals(CMDriverParameterEnum.STANDARD_ACTIVITY_NUMBER.getCode())) { // 成本BOM标准作业
            variableKeyList.add(variable);
            variableKeyList.add(this.converter.convertCostobj2MaroId(costobjId));
        }
        else if (paramcode.equals(CMDriverParameterEnum.MAIN_STUFF_CONSUME_QUOTA.getCode()) // 主材料消耗定额
                || paramcode.equals(CMDriverParameterEnum.ASSIN_STUFF_BOM_QUOTA.getCode())// 指定材料子项BOM消耗定额
                || paramcode.equals(CMDriverParameterEnum.PRICE_LIBRARY.getCode())// 标准成本.价格库
                || paramcode.equals(CMDriverParameterEnum.BOM_ACTIVITY_NUMBER.getCode()))// 生产BOM标准作业
        {
            variableKeyList.add(variable);
            variableKeyList.add(this.converter.convertCostobj2MarAssInfo(costobjId));
        }
        else if (paramcode.equals(CMDriverParameterEnum.BOM_STUFF_CONSUME_QUOTA.getCode())) // Bom材料消耗定额
        {
            variableKeyList.add(variable);
            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setMarAssInfoVO(this.converter.convertCostobj2MarAssInfo(costobjId));
            paramVO.setItemMarAssInfoVO(itemMarAssInfoVO);
            variableKeyList.add(paramVO);
        }
        else if (paramcode.equals(CMDriverParameterEnum.RT_STUFF_CONSUME_QUOTA.getCode())) // 工艺路线材料消耗定额
        {
            variableKeyList.add(variable);
            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setCcostcenterid(costcenterId);
            paramVO.setMarAssInfoVO(this.converter.convertCostobj2MarAssInfo(costobjId));
            paramVO.setItemMarAssInfoVO(itemMarAssInfoVO);
            variableKeyList.add(paramVO);
        }
        else if (CMDriverParameterEnum.RT_ACTIVITY_NUMBER.getCode().equals(paramcode)) {// 工艺路线单位标准作业量
            variableKeyList.add(variable);
            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setCcostcenterid(costcenterId);
            paramVO.setMarAssInfoVO(this.converter.convertCostobj2MarAssInfo(costobjId));
            variableKeyList.add(paramVO);
        }
        else if (paramcode.equals(CMDriverParameterEnum.MO_STUFF_CONSUME_QUOTA.getCode())) // 生产订单材料消耗定额
        {
            variableKeyList.add(variable);
            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setMarAssInfoVO(this.converter.convertCostobj2MarAssInfo(costobjId));
            paramVO.setMocode(this.converter.convertCostobj2Mo(costobjId));
            paramVO.setItemMarAssInfoVO(itemMarAssInfoVO);
            variableKeyList.add(paramVO);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ALLOC_FAC.getCode())) { // 分配系数
            variableKeyList.add(variable);
            if (AllocfacEnum.costcenter.toStringValue().equals(singelValue1)) {
                variableKeyList.add(costcenterId);
            }
            else if (AllocfacEnum.product.toStringValue().equals(singelValue1)) {
                variableKeyList.add(this.converter.convertCostobj2MaroId(costobjId));
            }
            else if (AllocfacEnum.productvbfree.toStringValue().equals(singelValue1)) {
                variableKeyList.add(this.converter.convertCostobj2MarAssInfo(costobjId));
            }
            else if (AllocfacEnum.costclass.toStringValue().equals(singelValue1)) {
                variableKeyList.add(this.converter.convertCostObj2MarCostClass(costobjId));
            }
            else if (AllocfacEnum.baseclass.toStringValue().equals(singelValue1)) {
                variableKeyList.add(this.converter.convertCostObj2MarBaseClass(costobjId));
            }
            else if (AllocfacEnum.stuff.toStringValue().equals(singelValue1)) {
                variableKeyList.add(itemMarAssInfoVO.getCmaterialid());
            }
        }
        else if (paramcode.equals(CMDriverParameterEnum.ACTUAL_STUFF_NUMBER.getCode())) { // 实际材料消耗
            variableKeyList.add(variable);

            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setCcostcenterid(costcenterId);
            // CTC时，按成本中心取数
            if (!this.isCTCSign) {
                paramVO.setCcostobjectid(costobjId);
            }
            paramVO.setItemMarAssInfoVO(itemMarAssInfoVO);
            variableKeyList.add(paramVO);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_NUMBER.getCode())// 指定材料子项实际消耗数量
                || paramcode.equals(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_MONEY.getCode())) { // 指定材料子项实际消耗金额
            variableKeyList.add(variable);
            String key = costcenterId;
            // CTC时，按成本中心取数
            if (!this.isCTCSign) {
                key += costobjId;
            }
            variableKeyList.add(key);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ON_PRODUCT_RATE.getCode()) // 在产约当量
                || paramcode.equals(CMDriverParameterEnum.WASTEPRODUCT_RATE.getCode())// 废品约当量
                || paramcode.equals(CMDriverParameterEnum.JOINT_WASTEPRODUCT_RATE.getCode()) // 联废品约当量
                || CMDriverParameterEnum.INPRO_APPRONUM.getCode().equals(paramcode)) {// 期初在产品约当产量
            variableKeyList.add(variable);
            variableKeyList.add(costcenterId);
            variableKeyList.add(costobjId + celementId);
        }
        else if (paramcode.equals(CMDriverParameterEnum.FACTOR.getCode())// 实际要素金额
                || paramcode.equals(CMDriverParameterEnum.ACTUAL_ACTIVITY_NUMBER.getCode())) {// 实际作业量
            variableKeyList.add(variable);
            variableKeyList.add(costcenterId);
            // CTC时，按成本中心取数
            if (!this.isCTCSign) {
                variableKeyList.add(costobjId);
            }
        }
        else if (CMDriverParameterEnum.BOM_JOINTBY_OUTPUT_QUOTA.getCode().equals(paramcode)) // BOM联副产品产出定额
        {
            variableKeyList.add(variable);
            CMAssInfoParamVO paramVO = new CMAssInfoParamVO();
            paramVO.setMarAssInfoVO(this.converter.convertCostobj2MarAssInfo(costobjId));
            paramVO.setItemMarAssInfoVO(itemMarAssInfoVO);
            variableKeyList.add(paramVO);
        }
        else if (paramcode.equals(CMDriverParameterEnum.STANDARD_COST.getCode())) {// 标准成本
            variableKeyList.add(variable);
            CostObjectVO costObjectVO = this.converter.convertCostobj2CostObjectInfo(costobjId);
            StdCostParamVO stdParamVO = new StdCostParamVO();
            if (costObjectVO != null) {
                stdParamVO.setCcostcenterid(costcenterId);
                stdParamVO.setCmaterialid(costObjectVO.getCmaterialid());
                if (costObjectVO.getFinstoragetype().intValue() == CostObjInStorageTypeEnum.HOME_MAKE.toIntValue()
                        || costObjectVO.getFinstoragetype().intValue() == CostObjInStorageTypeEnum.RE_WORK.toIntValue()) {
                    // 枚举转换，标准成本暂时没有常量可用
                    stdParamVO.setFinstoragetype(Integer.valueOf(1));
                }
                else {
                    // 枚举转换，标准成本暂时没有常量可用
                    stdParamVO.setFinstoragetype(Integer.valueOf(3));
                }
                stdParamVO.setCcustomerid(costObjectVO.getCcustomerid());
                stdParamVO.setCprojectid(costObjectVO.getCprojectid());
                stdParamVO.setCvendorid(costObjectVO.getCvendorid());
                stdParamVO.setCproductorid(costObjectVO.getCproductorid());
                stdParamVO.setVfree1(costObjectVO.getVfree1());
                stdParamVO.setVfree2(costObjectVO.getVfree2());
                stdParamVO.setVfree3(costObjectVO.getVfree3());
                stdParamVO.setVfree4(costObjectVO.getVfree4());
                stdParamVO.setVfree5(costObjectVO.getVfree5());
                stdParamVO.setVfree6(costObjectVO.getVfree6());
                stdParamVO.setVfree7(costObjectVO.getVfree7());
                stdParamVO.setVfree8(costObjectVO.getVfree8());
                stdParamVO.setVfree9(costObjectVO.getVfree9());
                stdParamVO.setVfree10(costObjectVO.getVfree10());
            }
            variableKeyList.add(stdParamVO);
        }//2020-12-08 liyf  九州药业扩展成本动因
    	//接口有多个实现类型，改动影响范围太大，因此在此处变通实现
        else if (paramcode.equals(CMDriverParameterEnum_JZ.ACTUAL_STUFF_NUMBER_KG.getCode())
          ) { // 指定材料子项实际消耗金额
            variableKeyList.add(variable);
            String key = costcenterId;
            // CTC时，按成本中心取数
            if (!this.isCTCSign) {
                key += costobjId;
            }
            variableKeyList.add(key);
        }
        else {
            variableKeyList.add(variable);
            variableKeyList.add(costcenterId);
            variableKeyList.add(costobjId);
        }

        return variableKeyList;
    }

    public String getDriverNumKey(AllocateRecordHeadVO head, boolean onlyCenter) {
        if (onlyCenter) {
            return head.getCcostcenterid();
        }
        return head.getCcostcenterid() + head.getCcostobjectid();
    }

    @Override
    public boolean isProductAllocate() {
        return false;
    }
}

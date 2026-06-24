package nc.pubimpl.mapub.driver.cm.allocate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bd.framework.base.CMStringUtil;
import nc.bs.mapub.driver.util.AnalysisFormulaTools;
import nc.bs.mapub.driver.util.ExpressionTools;
import nc.bs.mapub.driver.util.VariableMapTreeUtil;
import nc.pubitf.mapub.driver.cm.allocate.ICalFormulaDriverNumber;
import nc.pubitf.mapub.driver.cm.allocate.IDriverFormulaAquireData;
import nc.vo.cmpub.framework.assistant.CMMarAssInfoVO;
import nc.vo.mapub.allocfac.enumeration.AllocfacEnum;
import nc.vo.mapub.driver.entity.CMDriverLangConst;
import nc.vo.mapub.driver.entity.CMDriverParameterEnum;
import nc.vo.mapub.driver.entity.CMDriverParameterEnum_JZ;
import nc.vo.mapub.driver.entity.DriverVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * <b> 根据公式计算动因实现类 </b>
 * <p>
 * 详细描述功能
 * </p>
 * 创建日期:2010-4-20
 *
 * @author:wangtf
 */
public class CalFormulaDriverNumberImpl implements ICalFormulaDriverNumber {

    AnalysisFormulaTools analysisTools;

    private AnalysisFormulaTools getAnalysisTools() {
        if (this.analysisTools == null) {
            this.analysisTools = new AnalysisFormulaTools();
        }
        return this.analysisTools;
    }

    @Override
    public void aquireDriverNum(DriverVO driver, IDriverFormulaAquireData aquireData, String costType)
            throws BusinessException {
        String formula = driver.getVformulacode();
        Set<String> variableList = this.getAnalysisTools().analyseFormula(formula);
        for (String variable : variableList) {
            this.aquireVariableNum(variable, aquireData, costType);
        }
    }

    @Override
    public UFDouble calculate(String formula, IDriverFormulaAquireData aquireData, String costcenterId,
            String costobjId, String celementId, CMMarAssInfoVO itemMarAssInfoVO) throws BusinessException {
        if (aquireData == null) {
            throw new BusinessException(CMDriverLangConst.getERR_NO_FETCHDATA_CLASS());
        }
        if (null == aquireData.getVariableMap() || aquireData.getVariableMap().size() == 0) {
            // 动因公式中变量值为空的时候不能继续进行计算，会出错
            throw new BusinessException(CMDriverLangConst.getERR_EXIST_NULL_VARIABLE());
        }

        Map<String, UFDouble> variableMap = this.getAnalysisTools().analysis(formula);
        for (Entry<String, UFDouble> entry : variableMap.entrySet()) {
            UFDouble value =
                    this.getVariable(aquireData, entry.getKey(), costcenterId, costobjId, celementId, itemMarAssInfoVO);
            if (value == null) {
                // 动因公式中变量值为空的时候不能继续进行计算，会出错
                throw new BusinessException(CMDriverLangConst.getERR_EXIST_NULL_VARIABLE());
            }
            entry.setValue(value);
        }

        // 计算公式的值
        UFDouble retValue = this.calFormula(formula, variableMap);

        return retValue;
    }

    /**
     * 准备公式变量值
     *
     * @param formula
     *            公式
     * @param aquireData
     *            取数类
     * @param costType
     *            成本类型
     */
    private void aquireVariableNum(String variable, IDriverFormulaAquireData aquireData, String costType)
            throws BusinessException {
        // 获取公式中的编码
        Map<String, Object> formulaMap = this.getAnalysisTools().analyseCode(variable);
        String paramcode = (String) formulaMap.get("paramCode");
        String[] paramValue1 = (String[]) formulaMap.get("paramValue1");
        String[] paramValue2 = (String[]) formulaMap.get("paramValue2");
        String singelValue1 = null;
        if (paramValue1 != null) {
            singelValue1 = paramValue1[0];
        }
        String singelValue2 = null;
        if (paramValue2 != null) {
            singelValue2 = paramValue2[0];
        }
        if (paramcode.equals(CMDriverParameterEnum.QUALIFIED_NUMBER.getCode())) { // 合格品
            aquireData.aquireQualifiedNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ON_PRODUCT_RATE.getCode())) { // 在产
            aquireData.aquireOnProductRate(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.JOINT_QUALIFIED_NUMBER.getCode())) { // 联产品
            aquireData.aquireJointQualifiedNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.BY_PRODUCT_NUMBER.getCode())) { // 副产品
            aquireData.aquireByProductNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.WASTEPRODUCT_NUMBER.getCode())) { // 废品
            aquireData.aquireWasteProductNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.WASTEPRODUCT_RATE.getCode())) { // 废品约当
            aquireData.aquireWasteProductRate(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.JOINT_WASTEPRODUCT_NUMBER.getCode())) { // 联废品
            aquireData.aquireJointWasteProductNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.JOINT_WASTEPRODUCT_RATE.getCode())) { // 联废品约当
            aquireData.aquireJointWasteProductRate(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.BOM_STUFF_CONSUME_QUOTA.getCode())) { // Bom材料消耗定额
            aquireData.aquireBomStuffConsumeQuota(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.RT_STUFF_CONSUME_QUOTA.getCode())) { // 工艺路线材料消耗定额
            aquireData.aquireRTStuffConsumeQuota(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.MO_STUFF_CONSUME_QUOTA.getCode())) { // 生产订单材料消耗定额
            aquireData.aquireMOStuffConsumeQuota(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.MAIN_STUFF_CONSUME_QUOTA.getCode())) { // 主材料消耗定额
            aquireData.aquireMainStuffConsumeQuota(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ACTUAL_STUFF_NUMBER.getCode())) { // 材料子项实际消耗数量
            aquireData.aquireActualStuffNumber(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_NUMBER.getCode())) { // 指定材料子项实际消耗数量
            aquireData.aquireAssignStuffActualNumber(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_MONEY.getCode())) { // 指定材料子项实际消耗金额
            aquireData.aquireAssignStuffActualMoney(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.PLAN_PRICE.getCode())) { // 计划价
            aquireData.aquirePlanPrice(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.REFERENCE_COST.getCode())) { // 参考成本
            aquireData.aquireReferenceCost(variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.REFERENCE_SALE_PRICE.getCode())) { // 参考销售价
            aquireData.aquireReferenceSalePrice(paramValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.STANDARD_COST.getCode())) { // 标准成本
            aquireData.aquireStandardCost(costType, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.PRICE_LIBRARY.getCode())) { // 价格库
            aquireData.aquirePriceLibrary(paramValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ACTUAL_ACTIVITY_NUMBER.getCode())) { // 实际作业量
            aquireData.aquireActualActivityNumber(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ACTUAL_ACTIVITY_FINISH_NUMBER.getCode())) { // 实际作业完工量
            aquireData.aquireActualActivityFinishNumber(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.STANDARD_ACTIVITY_NUMBER.getCode())) { // 成本BOM标准作业量
            aquireData.aquireStandardActivityNumber(singelValue1, costType, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.BOM_ACTIVITY_NUMBER.getCode())) { // 生产BOM标准作业量
            aquireData.aquireBOMActivityNumber(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.ALLOC_FAC.getCode())) { // 分配系数
            if (singelValue1.equals("6")) {
                ExceptionUtils.wrappBusinessException(CMDriverLangConst.getERR_NO_SUPORT_DRIVER()
                        + CMDriverLangConst.getCOSTOBJECT_ALLOCFAC() + CMDriverLangConst.getALLOCFAC());
            }
            aquireData.aquireAllocfac(singelValue2, singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverParameterEnum.FACTOR.getCode())) { // 成本要素
            aquireData.aquireFactor(singelValue1, variable);
        }
        else if (paramcode.equals(CMDriverLangConst.OTHER)) { // 其他
            aquireData.aquireOther(singelValue1, variable);
        }
        else if (CMDriverParameterEnum.BOM_JOINTBY_OUTPUT_QUOTA.getCode().equals(paramcode)) {// BOM联副产品产出定额
            aquireData.aquireBomJointByOutputQuota(variable);
        }
        else if (CMDriverParameterEnum.RT_ACTIVITY_NUMBER.getCode().equals(paramcode)) {// 工艺路线单位标准作业量
            aquireData.aquireRTActivityNumber(singelValue1, variable);
        }
        else if (CMDriverParameterEnum.INPRO_APPRONUM.getCode().equals(paramcode)) {// 期初在产品约当产量
            aquireData.aquireInproApproNum(variable);
        }
        else if (CMDriverParameterEnum.ASSIN_STUFF_BOM_QUOTA.getCode().equals(paramcode)) {// 指定材料子项BOM消耗定额
            aquireData.aquireAssinStuffBomQuota(singelValue1, variable);
        }
        else if (CMDriverParameterEnum.STANDARD_ACTIVITY_NUMBER.getCode().equals(paramcode)) {
            ExceptionUtils.wrappBusinessException(CMDriverLangConst.getERR_NO_SUPORT_DRIVER()
                    + CMDriverLangConst.getSTANDARD_ACTIVITY_NUMBER());
        }
        //2020-12-08 liyf  九州药业扩展成本动因
        else if (paramcode.equals(CMDriverParameterEnum_JZ.ACTUAL_STUFF_NUMBER_KG.getCode())) { // KG的产成品材料子项实际消耗数量
            aquireData.aquireAssignStuffActualNumber(paramcode, variable);

        }
        else {
            ExceptionUtils.wrappBusinessException(CMDriverLangConst.getERR_NO_SUPORT_DRIVER() + paramcode);
        }
    }

    /**
     * 获得变量的值
     *
     * @param formula
     *            公式
     * @param variableMap
     *            变量Map
     * @return 变量的值
     */
    private UFDouble getVariable(IDriverFormulaAquireData aquireData, String variable, String costcenterId,
            String costobjId, String celementId, CMMarAssInfoVO itemMarAssInfoVO) {
        // 获取公式中的编码
        Map<String, Object> formulaMap = this.getAnalysisTools().analyseCode(variable);
        List<Object> variableKeyList =
                aquireData.getVariableKeyList(variable, costcenterId, costobjId, celementId, itemMarAssInfoVO,
                        formulaMap);

        return new VariableMapTreeUtil(aquireData.getVariableMap(), variableKeyList).getValue(
                aquireData.hasMaterialId(), aquireData.isProductAllocate());
    }

    /**
     * 计算公式
     *
     * @param formula
     *            公式
     * @param variable
     *            变量
     * @return 计算后的值
     */
    private UFDouble calFormula(String formula, Map<String, UFDouble> variableMap) {
        String newformula = formula.replaceAll("\\{", "");
        newformula = newformula.replaceAll("\\}", "");

        // 复杂运算类，暂时不支持复杂运算，为提高效率，废弃
        // FormulaParseFather f = new FormulaParse();
        // f.setExpress(newformula);
        //
        // for (String[] strings : variable) {
        // String fml = strings[0];
        // fml = fml.replaceAll("\\[", "\\$");
        // fml = fml.replaceAll("\\]", "\\$");
        // f.addVariable(fml, new UFDouble(strings[1]));
        // }
        //
        // String result = f.getValue();
        // 四则运算简化计算类
        ExpressionTools et = new ExpressionTools(newformula);
        String result = et.getResult(variableMap);
        return new UFDouble(result);
    }

    @Override
    public UFBoolean checkOnlyCenter(String formula, UFBoolean beCoIsNull) {
        UFBoolean onlyCenter = UFBoolean.FALSE;
        if (CMStringUtil.isEmpty(formula)) {
            ExceptionUtils.wrappBusinessException(CMDriverLangConst.getERR_NO_FOMULA());
        }
        else {
            Set<String> itemList = this.getAnalysisTools().analyseFormula(formula);
            for (String item : itemList) {
                if (item.startsWith(CMDriverParameterEnum.ALLOC_FAC.getCode() + "["
                        + AllocfacEnum.costcenter.toIntValue() + "]")) {
                    onlyCenter = UFBoolean.TRUE;
                }
                else if (item.equals(CMDriverParameterEnum.ACTUAL_STUFF_NUMBER.getCode())
                        || item.startsWith(CMDriverParameterEnum.ACTUAL_ACTIVITY_NUMBER.getCode())
                        || item.startsWith(CMDriverParameterEnum.FACTOR.getCode())
                        || item.startsWith(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_NUMBER.getCode())
                        || item.startsWith(CMDriverParameterEnum.ASSIGN_STUFF_ACTUAL_MONEY.getCode())
                        || item.startsWith(CMDriverParameterEnum_JZ.ACTUAL_STUFF_NUMBER_KG.getCode())) {
                    onlyCenter = beCoIsNull;
                }
                else {
                    onlyCenter = UFBoolean.FALSE;
                    break;
                }
            }
        }
        return onlyCenter;
    }

}

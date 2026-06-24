package nc.vo.mapub.driver.entity;

/**
 * 成本动因变量
 *
 * @author liyf
 */
public enum CMDriverParameterEnum_JZ {


    /**
     * 材料出库单产成品主单位是KG的材料子项实际消耗数量
     */
	ACTUAL_STUFF_NUMBER_KG("ACTUAL_STUFF_NUMBER_KG");

    private String code;

    private CMDriverParameterEnum_JZ(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
    
}

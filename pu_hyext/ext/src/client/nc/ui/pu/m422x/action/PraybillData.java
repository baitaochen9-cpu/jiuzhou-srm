package nc.ui.pu.m422x.action;

import java.math.BigDecimal;

public class PraybillData {
	private String materialCode;
	private String unitId;
	private BigDecimal nastNum;
	
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public BigDecimal getNastNum() {
		return nastNum;
	}
	public void setNastNum(BigDecimal nastNum) {
		this.nastNum = nastNum;
	}
}

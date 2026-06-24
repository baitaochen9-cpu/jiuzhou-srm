package nc.bs.qc.supplierqualitystatus.ace.bp.rule;

import nc.bs.qc.supplierqualitystatus.rule.SupplyVerionBeforeRule;
import nc.vo.pub.ISuperVO;

public class AceSupplierqualitystatusSupplyVerionBeforeRule extends SupplyVerionBeforeRule {

	@Override
	public String getOidField() {
		return "pk_srcmaterial";
	}

	@Override
	public String getVidField() {
		return "pk_material";
	}
	
	@Override
	public Class<? extends ISuperVO>[] getBodyClass() {
		return null;
	}
}

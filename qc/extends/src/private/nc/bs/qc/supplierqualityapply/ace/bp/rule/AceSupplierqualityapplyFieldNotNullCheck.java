package nc.bs.qc.supplierqualityapply.ace.bp.rule;

import nc.bs.qc.supplierqualitystatus.rule.FieldNotNullCheck;
import nc.vo.pub.ISuperVO;

public class AceSupplierqualityapplyFieldNotNullCheck extends FieldNotNullCheck {

	@Override
	public String[] checkHeadNames() {
		return new String[] { "pk_group", "pk_org", "pk_org_v", "maketime",
				"pk_supplier"};
	}

	@Override
	public String[][] checkBodyNames() {
		return null;
	}

	@Override
	public Class<? extends ISuperVO>[] checkBodyClass() {
		return  null;
	}

}

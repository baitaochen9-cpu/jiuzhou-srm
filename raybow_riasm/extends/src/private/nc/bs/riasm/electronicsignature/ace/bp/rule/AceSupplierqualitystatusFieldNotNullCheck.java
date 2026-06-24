package nc.bs.riasm.electronicsignature.ace.bp.rule;

import nc.vo.pub.ISuperVO;

public class AceSupplierqualitystatusFieldNotNullCheck extends FieldNotNullCheck {

	@Override
	public String[] checkHeadNames() {
		return new String[] { "pk_group", "pk_org", "pk_org_v", "maketime",
				"pk_parent"};
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

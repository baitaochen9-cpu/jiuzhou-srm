package nc.bs.riasm.electronicsignature.ace.bp.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public abstract class FieldNotNullCheck implements IRule {

	@Override
	public void process(Object[] vos) {
		String[] names = checkHeadNames();
		if (names != null && names.length > 0) {
			String msg = VONotNullCheck.aggvodataNotNullInfo((IBill[]) vos,
					names);
			if (!StringUtil.isEmpty(msg)) {
				ExceptionUtils.wrappBusinessException(msg);
			}
		}
		String[][] names1 = checkBodyNames();
		Class<? extends ISuperVO>[] clazzs = checkBodyClass();

		if (names1 != null && names1.length > 0 && clazzs != null
				&& clazzs.length > 0) {
			String msg = VONotNullCheck.vodataNotNullInfo((IBill[]) vos,
					clazzs, names1);
			if (!StringUtil.isEmpty(msg)) {
				ExceptionUtils.wrappBusinessException(msg);
			}
		}
	}

	public abstract String[] checkHeadNames();

	public abstract String[][] checkBodyNames();

	public abstract Class<? extends ISuperVO>[] checkBodyClass();

}

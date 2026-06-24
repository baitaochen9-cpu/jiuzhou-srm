package nc.bs.jzqc.labelprint.ace.bp.rule;

import nc.bs.pf.pub.PfDataCache;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;

public class FillBillTailInfoRuleForIns<E extends IBill> implements IRule<E> {
	public void process(E[] vos) {
		for (IBill vo : vos) {
			BilltypeVO typevo = PfDataCache.getBillType((String) vo.getParent()
					.getAttributeValue("srcbilltype"));
			if (typevo != null && typevo.getPk_billtypeid() !=null) {
				vo.getParent().setAttributeValue("srcbilltype",
						typevo.getPk_billtypeid());
			}
		}
	}
}
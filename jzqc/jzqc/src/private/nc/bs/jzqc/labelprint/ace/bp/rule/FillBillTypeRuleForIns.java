package nc.bs.jzqc.labelprint.ace.bp.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.pub.PubAppTool;

public class FillBillTypeRuleForIns<E extends IBill> implements IRule<E> {
	public void process(E[] vos) {
		for (IBill vo : vos) {
			setBillMakeInfo(vo);
		}
	}

	private void setBillMakeInfo(IBill vo) {
		ISuperVO head = vo.getParent();
		AppContext proxy = AppContext.getInstance();
		String billmaker = (String) head.getAttributeValue("billmaker");
		if (PubAppTool.isNull(billmaker)) {
			head.setAttributeValue("billmaker", proxy.getPkUser());
		}
		Object makedate = head.getAttributeValue("dmakedate");
		if (null == makedate) {
			head.setAttributeValue("dmakedate", proxy.getBusiDate());
		}
	}
}
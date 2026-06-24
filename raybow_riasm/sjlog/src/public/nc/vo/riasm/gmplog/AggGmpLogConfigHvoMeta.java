package nc.vo.riasm.gmplog;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggGmpLogConfigHvoMeta extends AbstractBillMeta{
	
	public AggGmpLogConfigHvoMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.riasm.gmplog.GmpLogConfigHvo.class);
		this.addChildren(nc.vo.riasm.gmplog.GmpLogConfigBvo.class);
	}
}
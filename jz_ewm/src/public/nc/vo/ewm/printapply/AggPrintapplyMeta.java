package nc.vo.ewm.printapply;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggPrintapplyMeta extends AbstractBillMeta{
	
	public AggPrintapplyMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.ewm.printapply.Printapply.class);
	}
}
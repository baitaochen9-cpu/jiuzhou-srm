package nc.vo.ewm.printapply;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggprintapplyHVOMeta extends AbstractBillMeta{
	
	public AggprintapplyHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.ewm.printapply.PrintapplyHVO.class);
	}
}
package nc.vo.jzqc.labelprintapply;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggLabelprintapplyHVOMeta extends AbstractBillMeta{
	
	public AggLabelprintapplyHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.jzqc.labelprintapply.LabelprintapplyHVO.class);
	}
}
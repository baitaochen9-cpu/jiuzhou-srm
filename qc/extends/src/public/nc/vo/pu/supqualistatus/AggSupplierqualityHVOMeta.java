package nc.vo.pu.supqualistatus;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSupplierqualityHVOMeta extends AbstractBillMeta{
	
	public AggSupplierqualityHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.pu.supqualistatus.SupplierqualityHVO.class);
	}
}
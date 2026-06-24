package nc.vo.qc.supplierqualityapply;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSupplierQualityApplyVOMeta extends AbstractBillMeta{
	
	public AggSupplierQualityApplyVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.qc.supplierqualityapply.SupplierQualityApplyVO.class);
	}
}
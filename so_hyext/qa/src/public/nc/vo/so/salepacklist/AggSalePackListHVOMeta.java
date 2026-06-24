package nc.vo.so.salepacklist;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSalePackListHVOMeta extends AbstractBillMeta{
	
	public AggSalePackListHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.so.salepacklist.SalePackListHVO.class);
		this.addChildren(nc.vo.so.salepacklist.SalePackListBVO.class);
	}
}
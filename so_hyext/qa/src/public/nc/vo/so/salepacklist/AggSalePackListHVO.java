package nc.vo.so.salepacklist;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.so.salepacklist.SalePackListHVO")

public class AggSalePackListHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSalePackListHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SalePackListHVO getParentVO(){
	  	return (SalePackListHVO)this.getParent();
	  }
	  
}
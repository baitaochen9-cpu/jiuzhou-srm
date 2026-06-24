package nc.vo.pu.supqualistatus;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.pu.supqualistatus.SupplierqualityHVO")

public class AggSupplierqualityHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSupplierqualityHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SupplierqualityHVO getParentVO(){
	  	return (SupplierqualityHVO)this.getParent();
	  }
	  
}
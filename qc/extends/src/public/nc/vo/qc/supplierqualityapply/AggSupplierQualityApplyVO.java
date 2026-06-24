package nc.vo.qc.supplierqualityapply;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.qc.supplierqualityapply.SupplierQualityApplyVO")

public class AggSupplierQualityApplyVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSupplierQualityApplyVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SupplierQualityApplyVO getParentVO(){
	  	return (SupplierQualityApplyVO)this.getParent();
	  }
	  
}
package nc.vo.ewm.printapply;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.ewm.printapply.Printapply")

public class AggPrintapply extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggPrintapplyMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Printapply getParentVO(){
	  	return (Printapply)this.getParent();
	  }
	  
}
package nc.vo.ewm.printapply;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.ewm.printapply.PrintapplyHVO")

public class AggprintapplyHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggprintapplyHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public PrintapplyHVO getParentVO(){
	  	return (PrintapplyHVO)this.getParent();
	  }
	  
}
package nc.vo.riasm.electronicsignaturehis;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO")

public class AggElectronicsignatureHisVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggElectronicsignatureHisVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ElectronicsignatureHisVO getParentVO(){
	  	return (ElectronicsignatureHisVO)this.getParent();
	  }
	  
}
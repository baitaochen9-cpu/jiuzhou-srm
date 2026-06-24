package nc.vo.riasm.electronicsignature;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.riasm.electronicsignature.ElectronicSignatureVO")

public class AggElectronicSignatureVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggElectronicSignatureVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ElectronicSignatureVO getParentVO(){
	  	return (ElectronicSignatureVO)this.getParent();
	  }
	  
}
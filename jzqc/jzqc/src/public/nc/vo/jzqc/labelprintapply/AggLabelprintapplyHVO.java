package nc.vo.jzqc.labelprintapply;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.jzqc.labelprintapply.LabelprintapplyHVO")

public class AggLabelprintapplyHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggLabelprintapplyHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public LabelprintapplyHVO getParentVO(){
	  	return (LabelprintapplyHVO)this.getParent();
	  }
	  
}
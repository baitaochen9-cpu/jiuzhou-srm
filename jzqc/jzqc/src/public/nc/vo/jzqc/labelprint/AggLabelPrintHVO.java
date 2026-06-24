package nc.vo.jzqc.labelprint;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.jzqc.labelprint.LabelPrintHVO")

public class AggLabelPrintHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggLabelPrintHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public LabelPrintHVO getParentVO(){
	  	return (LabelPrintHVO)this.getParent();
	  }
	  
}
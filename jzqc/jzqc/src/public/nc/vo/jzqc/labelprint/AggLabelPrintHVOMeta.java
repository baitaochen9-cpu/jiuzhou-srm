package nc.vo.jzqc.labelprint;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggLabelPrintHVOMeta extends AbstractBillMeta{
	
	public AggLabelPrintHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.jzqc.labelprint.LabelPrintHVO.class);
	}
}
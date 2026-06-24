package nc.vo.riasm.electronicsignaturehis;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggElectronicsignatureHisVOMeta extends AbstractBillMeta{
	
	public AggElectronicsignatureHisVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.riasm.electronicsignaturehis.ElectronicsignatureHisVO.class);
	}
}
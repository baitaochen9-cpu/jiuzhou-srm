package nc.vo.riasm.electronicsignature;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggElectronicSignatureVOMeta extends AbstractBillMeta{
	
	public AggElectronicSignatureVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.riasm.electronicsignature.ElectronicSignatureVO.class);
		this.addChildren(nc.vo.riasm.electronicsignature.ElectronicSignatureBVO.class);
	}
}
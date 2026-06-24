package nc.ui.bd.material.pf.model;

import nc.ui.bd.pub.orginfo.model.AbstractPFModelMediator;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.material.pf.AggMaterialPfVO;
import nc.vo.bd.material.pf.MaterialPfVO;

public class MaterialPFModelMediator extends AbstractPFModelMediator {
	protected void initBaseModel() {
		AggMaterialPfVO selectedData = (AggMaterialPfVO) getPfModel()
				.getSelectedData();
		if (selectedData == null)
			return;
		MaterialVO vo = (MaterialVO) ((MaterialPfVO) selectedData.getParentVO())
				.getMaterial();
		if (vo != null)
			getBaseModel().initModel(vo);
	}

	protected void initPfModel() {
		AggMaterialPfVO selectedData = (AggMaterialPfVO) getPfModel()
				.getSelectedData();
		if (selectedData == null)
			return;
		((MaterialPfVO) selectedData.getParentVO()).setMaterial(getBaseModel()
				.getSelectedData());
		getPfModel().directlyUpdate(selectedData);
	}
}
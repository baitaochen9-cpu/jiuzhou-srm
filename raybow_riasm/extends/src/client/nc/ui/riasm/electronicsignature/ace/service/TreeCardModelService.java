package nc.ui.riasm.electronicsignature.ace.service;

import nc.bs.mmgp.common.MMGPVoUtils;
import nc.itf.scmpub.reference.uap.pf.PfServiceScmUtil;
import nc.ui.mmgp.uif2.service.MMGPTreeCardModelService;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.riasm.electronicsignature.AggElectronicSignatureVO;
import nc.vo.riasm.electronicsignature.ElectronicSignatureVO;

public class TreeCardModelService extends MMGPTreeCardModelService {

	protected SuperVO castSuperVO(Object object) {
		if ((object instanceof SuperVO)) {
			return (SuperVO) object;
		}

		if (object instanceof AggElectronicSignatureVO) {
			return ((AggElectronicSignatureVO) object).getParentVO();
		}
		return null;
	}

	public void delete(Object object) throws Exception {
		ElectronicSignatureVO bill = (ElectronicSignatureVO) object;
		AggElectronicSignatureVO aggvo = new AggElectronicSignatureVO();
		aggvo.setParent(bill);
		AggElectronicSignatureVO[] aggvos = new AggElectronicSignatureVO[] { aggvo };
		PfServiceScmUtil.processBatch("DELETE", "22GN", aggvos, null, null);
	}

	public Object insert(Object object) throws Exception {
		MMGPVoUtils.setHeadVOStatus(object, VOStatus.NEW);
		AggElectronicSignatureVO bill = (AggElectronicSignatureVO) object;
		AggElectronicSignatureVO[] aggvos = new AggElectronicSignatureVO[] { bill };
		AggElectronicSignatureVO[] vos = (AggElectronicSignatureVO[]) PfServiceScmUtil
				.processBatch("SAVEBASE", "22GN", aggvos, null, null);
		return vos[0];

	}

	public Object update(Object object) throws Exception {
		MMGPVoUtils.setHeadVOStatus(object, VOStatus.UPDATED);
//		ElectronicSignatureVO bill = (ElectronicSignatureVO) object;
//		AggElectronicSignatureVO aggvo = new AggElectronicSignatureVO();
//		aggvo.setParent(bill);
		AggElectronicSignatureVO bill = (AggElectronicSignatureVO) object;
		AggElectronicSignatureVO[] aggvos = new AggElectronicSignatureVO[] { bill };
		AggElectronicSignatureVO[] vos = (AggElectronicSignatureVO[]) PfServiceScmUtil
				.processBatch("SAVEBASE", "22GN", aggvos, null, null);
		return vos[0];
	}

}

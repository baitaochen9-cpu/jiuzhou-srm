package nc.bs.mateiralclass.mdm.listener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.NCLocator;
import nc.bs.ncc.mdm.util.VOCollectUtil;
import nc.itf.material.mdm.SendMdmItf;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;

public class MaterialClassEventListenerFoMdm implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent event)
			throws BusinessException {
		// TODO Auto-generated method stub
		DefdocVO[] defdocs =  VOCollectUtil.process((BDCommonEvent) event,DefdocVO.class);
		SendMdmItf sever = NCLocator.getInstance().lookup(SendMdmItf.class);
		sever.insetMdmMaterialClass(defdocs);
		
	}

}

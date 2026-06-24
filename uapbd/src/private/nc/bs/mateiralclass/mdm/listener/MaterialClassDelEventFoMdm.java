package nc.bs.mateiralclass.mdm.listener;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.framework.common.NCLocator;
import nc.bs.ncc.mdm.util.VOCollectUtil;
import nc.itf.material.mdm.SendMdmItf;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.pub.BusinessException;

public class MaterialClassDelEventFoMdm implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent event)
			throws BusinessException {
		// TODO Auto-generated method stub
		DefdocVO[] defdocs =  VOCollectUtil.process((BDCommonEvent) event,DefdocVO.class);
		if(null  == defdocs || defdocs.length == 0 ){
			return;
		}
		
		for(DefdocVO defdoc : defdocs){
			defdoc.setDr(1);
		}
		SendMdmItf sever = NCLocator.getInstance().lookup(SendMdmItf.class);
		sever.insetMdmMaterialClass(defdocs);
		
	}

}
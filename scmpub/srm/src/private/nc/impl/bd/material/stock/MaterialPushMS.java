 package nc.impl.bd.material.stock;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestClientException;

import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.businessevent.bd.BDCommonEvent;
import nc.bs.ebpur.purorder.listener.AfterSendPub;
//import nc.bs.logging.Logger;
import nc.bs.ncc.mdm.util.VOCollectUtil;
import nc.bs.srm.pub.MakeNcLog;
import nc.vo.bd.material.MaterialVO;
import nc.vo.pub.BusinessException;

public class MaterialPushMS extends AfterSendPub implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent event)
			throws BusinessException {
		Logger log = MakeNcLog.setParam("GWLog", "log");
		log.info("物料推送关务");
		MaterialVO[] materialVOs = VOCollectUtil.process((BDCommonEvent) event,MaterialVO.class);
		String eventType = event.getEventType();
		if(IsToGW(materialVOs[0].getPk_org())){
			log.info("开始推送");
			materialSendMS(materialVOs,eventType);
			log.info("推送完成");
		}else{
			log.info("单据未推送");
		}
	}
	public void sendGW(Object obj) throws RestClientException, BusinessException{
		MaterialVO[] materialVOs = (MaterialVO[])obj;
		materialSendMS(materialVOs,null);
	}

}

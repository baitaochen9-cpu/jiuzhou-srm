package nc.impl.so.delivery;

import java.util.Map;

import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.ebpur.purorder.listener.AfterSendPub;
import nc.bs.scmpub.yunfeng.utils.LogUtils;
import nc.vo.pub.BusinessException;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import com.alibaba.fastjson.JSONObject;



public class DeliveryManageListener extends AfterSendPub implements IBusinessListener{

	@Override
	public void doAction(IBusinessEvent arg0) throws BusinessException {
		Object objArray = ((BusinessEvent) arg0).getObject();
		String eventType = arg0.getEventType();
		LogUtils logs = new LogUtils();
		logs.sendTolog("----进入发货单推送----");
		if (objArray instanceof Object[]) {
			if (((Object[]) (Object[]) objArray)[0] instanceof DeliveryVO) {
				DeliveryVO ordObj = (DeliveryVO) ((Object[]) (Object[]) objArray)[0];
				DeliveryHVO hvo = (DeliveryHVO) ordObj.getParentVO();
				String typeCode = getTypeCode(ordObj.getChildrenVO()[0].getVsrctrantype());
				logs.sendTolog("----上游销售订单类型编码：" + typeCode);
				if ("30-Cxx-10-08".equals(typeCode) && IsToGW(hvo.getPk_org())
						&& fhIsApprove(hvo.getFstatusflag(),eventType)) {
					logs.sendTolog("----发货单符合推送：" + ("30-Cxx-10-08".equals(typeCode) && IsToGW(hvo.getPk_org())) );
//					Map<String, Object> poSend = delivertSend(ordObj,eventType);
//					logs.sendTolog("----发货单推送报文：" + JSONObject.toJSONString(poSend));
//					restTemplate(poSend);
//					logs.sendTolog("----发货单推送完成----");
				}
			}
			
		}
		
		
	}
	
	
}
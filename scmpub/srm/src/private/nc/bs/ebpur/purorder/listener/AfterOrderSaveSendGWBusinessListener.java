package nc.bs.ebpur.purorder.listener;

import java.util.Map;

import org.apache.log4j.Logger;

import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.srm.pub.MakeNcLog;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;

public class AfterOrderSaveSendGWBusinessListener extends AfterSendPub
		implements IBusinessListener {
	public void doAction(IBusinessEvent event) throws BusinessException {
		Object objArray = ((BusinessEvent) event).getObject();
		String eventType = event.getEventType();
		Logger log = MakeNcLog.setParam("GWLog", "log");
		log.info("采购订单推送");
		if (objArray instanceof Object[]) {
			if (((Object[]) (Object[]) objArray)[0] instanceof OrderVO) {
				OrderVO ordObj = (OrderVO) ((Object[]) (Object[]) objArray)[0];
				OrderHeaderVO hvo = (OrderHeaderVO) ordObj.getParentVO();
				if (IsToGW(hvo.getPk_org()) && isApprove(hvo.getForderstatus(),eventType)
						&& "Y".equals(hvo.getVdef9())) {
					Map<String, Object> poSend = poSend(ordObj, eventType);
					restTemplate(poSend);
					log.info("采购订单推送成功");
				}
			}
		}
	}
}

package nc.bs.ebpur.purorder.listener;

import java.util.Map;

import nc.bs.businessevent.BusinessEvent;
import nc.bs.businessevent.IBusinessEvent;
import nc.bs.businessevent.IBusinessListener;
import nc.bs.srm.pub.EsbUtils;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;

import org.springframework.web.client.RestTemplate;

public class AfterOrderUnSaveSendGWBusinessListener extends AfterSendPub
		implements IBusinessListener {
	public void doAction(IBusinessEvent event) throws BusinessException {
		Object objArray = ((BusinessEvent) event).getObject();

		if (objArray instanceof Object[]) {
			if (((Object[]) (Object[]) objArray)[0] instanceof OrderVO) {
				OrderVO ordObj = (OrderVO) ((Object[]) (Object[]) objArray)[0];
				OrderHeaderVO hvo = (OrderHeaderVO) ordObj.getParentVO();
				String eventType = event.getEventType();
				if ((ordObj.getBVO()[0].getFbuysellflag().intValue() == 4)) {

					Map<String, Object> poSend = poSend(ordObj,eventType);

					if (IsToGW(hvo.getPk_org())) {
						restTemplate(poSend);
					}
				}
			}

		}
	}

}

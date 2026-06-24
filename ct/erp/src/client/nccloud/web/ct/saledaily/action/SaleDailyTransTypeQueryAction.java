package nccloud.web.ct.saledaily.action;

import java.util.HashMap;
import java.util.Map;

import nc.vo.ct.business.entity.BusinessSetVO;
import nc.vo.ct.util.CtTransBusitypes;
import nccloud.dto.ct.saledaily.entity.SaleDailyQueryInfo;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;

/**
 * @description 销售合同交易类型查询
 * @author cuijun
 * @date 2019-3-15 上午11:30:40
 * @version ncc1.0
 */
public class SaleDailyTransTypeQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {

		String read = request.read();
		IJson json = JsonFactory.create();
		SaleDailyQueryInfo queryInfo = json.fromJson(read, SaleDailyQueryInfo.class);
		// 交易类型VO
		BusinessSetVO businessvo = CtTransBusitypes.getBusinessSetVO(queryInfo.getPk());
		return BusinessSetVOForNCC(businessvo);
	}

	private Map BusinessSetVOForNCC(BusinessSetVO businessvo) {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (businessvo == null) {
			return null;
		}
		for (String field : businessvo.getAttributeNames()) {
			valueMap.put(field, businessvo.getAttributeValue(field));
		}
		return valueMap;
	}
}

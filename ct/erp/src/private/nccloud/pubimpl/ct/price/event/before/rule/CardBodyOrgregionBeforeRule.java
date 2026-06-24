package nccloud.pubimpl.ct.price.event.before.rule;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.pubitf.initgroup.InitGroupQuery;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

public class CardBodyOrgregionBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean ebpurEnabled = isEbpurEnabled();
		map.put("isEbpurEnabled", ebpurEnabled);
		return map;
	}

	public boolean isEbpurEnabled() {
		try {
			return InitGroupQuery.isEnabled(InvocationInfoProxy.getInstance()
					.getGroupId(), "EC20");
		} catch (BusinessException e) {
			// »’÷æ“Ï≥£
			ExceptionUtils.wrappException(e);
		}
		return false;
	}

}

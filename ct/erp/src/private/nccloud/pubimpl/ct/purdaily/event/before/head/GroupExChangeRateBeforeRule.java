package nccloud.pubimpl.ct.purdaily.event.before.head;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 集团本位币汇率
 * @author xiahui
 * @date 创建时间：2019-1-23 下午2:26:40
 * @version ncc1.0
 **/
public class GroupExChangeRateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);

		String ccurrencyid = (String) userobject.get(CtAbstractVO.CCURRENCYID); // 本位币
		String corigcurrencyid = (String) userobject.get(CtAbstractVO.CORIGCURRENCYID); // 原币币种

		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		// 集团本位币
		String groupLocalCurrency = CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(pk_group);

		// 基于本币计算
		int groupValue = PubSysParamUtil.getNC001IntValue(pk_group);
		if (PubSysParamUtil.GROUP_CURRY_BASE == groupValue) {
			returnMap.put(WebEventConst.ISEDIT, !ValueUtil.equals(groupLocalCurrency, ccurrencyid));
		}
		// 基于原币计算
		else if (PubSysParamUtil.GROUP_ORIG_BASE == groupValue) {
			returnMap.put(WebEventConst.ISEDIT, !ValueUtil.equals(groupLocalCurrency, corigcurrencyid));
		}

		return returnMap;
	}

}

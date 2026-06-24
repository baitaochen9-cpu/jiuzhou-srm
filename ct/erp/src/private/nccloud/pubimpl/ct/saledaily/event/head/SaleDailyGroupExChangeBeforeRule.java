package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.HashMap;
import java.util.Map;

import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

import nccloud.commons.lang.StringUtils;

/**
 * @description 销售合同表头汇率编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午10:43:53
 * @version ncc1.0
 */
public class SaleDailyGroupExChangeBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, false);
		String ccurrencyid = (String) userobject.get(CtAbstractVO.CCURRENCYID); // 本位币
		String corigcurrencyid = (String) userobject
				.get(CtAbstractVO.CORIGCURRENCYID); // 原币币种
		String pk_group = AppContext.getInstance().getPkGroup();
		int groupValue = PubSysParamUtil.getNC001IntValue(pk_group);
		String groupLocalCurrency = CurrencyRateUtilHelper.getInstance()
				.getLocalCurrtypeByOrgID(pk_group);
		// 集团本位币为空
		if (StringUtils.isEmpty(groupLocalCurrency)) {
			returnMap.put(WebEventConst.ISEDIT, false);
			return returnMap;
		}
		if (PubSysParamUtil.GROUP_CURRY_BASE == groupValue) {
			returnMap.put(WebEventConst.ISEDIT,
					!ValueUtil.equals(groupLocalCurrency, ccurrencyid));
		}
		// 基于原币计算
		else if (PubSysParamUtil.GROUP_ORIG_BASE == groupValue) {
			returnMap.put(WebEventConst.ISEDIT,
					!ValueUtil.equals(groupLocalCurrency, corigcurrencyid));
		}
		return returnMap;
	}

}

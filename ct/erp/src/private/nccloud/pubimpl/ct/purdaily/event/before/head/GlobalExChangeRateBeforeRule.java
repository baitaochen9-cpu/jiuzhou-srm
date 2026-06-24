package nccloud.pubimpl.ct.purdaily.event.before.head;

import java.util.HashMap;
import java.util.Map;

import nccloud.commons.lang.StringUtils;

import nc.itf.org.IOrgConst;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 全局本位币汇率
 * @author xiahui
 * @date 创建时间：2019-1-23 下午2:26:49
 * @version ncc1.0
 **/
public class GlobalExChangeRateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);

		String ccurrencyid = (String) userobject.get(CtAbstractVO.CCURRENCYID); // 本位币
		String corigcurrencyid = (String) userobject.get(CtAbstractVO.CORIGCURRENCYID); // 原币币种
		String globeLocalCurrency = CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(IOrgConst.GLOBEORG); // 全局本位币

		if (StringUtils.isEmpty(globeLocalCurrency)) {
			returnMap.put(WebEventConst.ISEDIT, false);
			return returnMap;
		}
		
		int gloableValue = PubSysParamUtil.getNC002IntValue();
		// 基于本币计算
		if (PubSysParamUtil.GLOBAL_CURRY_BASE == gloableValue) {
			returnMap.put(WebEventConst.ISEDIT, !ValueUtil.equals(ccurrencyid, globeLocalCurrency));
		}
		// 基于原币计算
		else if (PubSysParamUtil.GLOBAL_ORIG_BASE == gloableValue) {
			returnMap.put(WebEventConst.ISEDIT, !ValueUtil.equals(corigcurrencyid, globeLocalCurrency));
		}
		
		return returnMap;
	}

}

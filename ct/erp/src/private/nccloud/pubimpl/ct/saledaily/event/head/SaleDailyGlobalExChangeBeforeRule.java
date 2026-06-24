package nccloud.pubimpl.ct.saledaily.event.head;

import java.util.HashMap;
import java.util.Map;

import nc.itf.org.IOrgConst;
import nc.pubitf.uapbd.CurrencyRateUtilHelper;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pu.pub.util.PubSysParamUtil;
import nc.vo.pub.BusinessException;
import nccloud.commons.lang.StringUtils;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 销售合同表头汇率编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午10:43:53
 * @version ncc1.0
 */
public class SaleDailyGlobalExChangeBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, false);
		int gloableValue = PubSysParamUtil.getNC002IntValue();
		// 原币
		String corigcurrencyid = (String) userobject.get(CtAbstractVO.CORIGCURRENCYID);
		// 本位币
		String ccurrencyid = (String) userobject.get(CtAbstractVO.CCURRENCYID);
		// 全局本位币
		String globeLocalCurrency = CurrencyRateUtilHelper.getInstance().getLocalCurrtypeByOrgID(IOrgConst.GLOBEORG);

		// 全局本位币为空
		if (StringUtils.isEmpty(globeLocalCurrency)) {
			returnMap.put(WebEventConst.ISEDIT, false);
			return returnMap;
		}
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

package nccloud.pubimpl.ct.purdaily.event.before.head;

import java.util.HashMap;
import java.util.Map;

import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pu.pub.util.APSysParamUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 预付款限额编辑前
 * @author xiahui
 * @date 创建时间：2019-1-24 上午9:04:12
 * @version ncc1.0
 **/
public class CTNoriprepaylimitBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);

		String apfinanceorg = (String) userobject.get(CtAbstractBVO.PK_FINANCEORG); // 财务组织原始版本
		if (StringUtil.isEmptyWithTrim(apfinanceorg)) {
			returnMap.put(WebEventConst.ISEDIT, false);
			return returnMap;
		}

		UFBoolean ap17 = APSysParamUtil.getAP17(apfinanceorg);
		if (ap17 == null || !ap17.booleanValue()) {
			returnMap.put(WebEventConst.ISEDIT, false);
			return returnMap;
		}

		return returnMap;
	}

}

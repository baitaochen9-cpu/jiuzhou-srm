package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.itf.bd.incomeperiod.IIncomePeriodQueryService;
import nc.vo.bd.incomeperiod.IncomePeriodVO;
import nc.vo.ct.entity.CtAbstractPayTermVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nccloud.commons.lang.ArrayUtils;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 销售和同生效日期前规则
 * @author wangshrc
 * @date 2019年2月19日 下午1:48:04
 * @version ncc1.0
 */
public class SaleDailyEffectDateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);
		String pk_incomeperiod = (String) userobject.get(CtAbstractPayTermVO.PK_INCOMEPERIOD);
		IIncomePeriodQueryService service = NCLocator.getInstance().lookup(IIncomePeriodQueryService.class);
		IncomePeriodVO[] incomePeriodVOs = service.queryVOsByPKS(new String[] { pk_incomeperiod });
		if (!ArrayUtils.isEmpty(incomePeriodVOs) && UFBoolean.FALSE.equals(incomePeriodVOs[0].getIspredata())) {
			returnMap.put(WebEventConst.ISEDIT, true);
		}else {
			returnMap.put(WebEventConst.ISEDIT, false);
		}
		return returnMap;
	}

}

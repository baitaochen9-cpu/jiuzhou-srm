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
 * @description 实际起效日和实际到期日编辑前事件
 * @author cuijun
 * @date 2019年6月24日 下午1:20:08
 * @version ncc1.0
 */
public class SaleDailydrealdateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);
		String pk_incomeperiod = (String) userobject.get(CtAbstractPayTermVO.PK_INCOMEPERIOD);
		 IIncomePeriodQueryService service =
			        NCLocator.getInstance().lookup(IIncomePeriodQueryService.class);
		IncomePeriodVO[] incomePeriodVOs = service.queryVOsByPKS(new String[] { pk_incomeperiod });
		if (!ArrayUtils.isEmpty(incomePeriodVOs) && UFBoolean.FALSE.equals(incomePeriodVOs[0].getIspredata())) {
			returnMap.put(WebEventConst.ISEDIT, true);
		} else {
			returnMap.put(WebEventConst.ISEDIT, false);
		}
		return returnMap;
	}

}

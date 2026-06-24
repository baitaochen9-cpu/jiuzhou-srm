package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 特征码编辑前
 * @author wangshrc
 * @date 2019年2月19日 下午1:20:08
 * @version ncc1.0
 */
public class SaleDailyUnitRateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);
		String cqtunitid = (String) userobject.get(CtAbstractBVO.CQTUNITID);
		String mat = (String) userobject.get(CtAbstractBVO.PK_MATERIAL);
		boolean fixedRate = MaterialPubService
				.queryIsFixedRateByMaterialAndMeasdoc(mat, cqtunitid);
		if (fixedRate)
			returnMap.put(WebEventConst.ISEDIT, false);
		return returnMap;
	}

}

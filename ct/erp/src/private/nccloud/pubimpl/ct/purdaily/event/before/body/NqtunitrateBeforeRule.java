package nccloud.pubimpl.ct.purdaily.event.before.body;

import java.util.HashMap;
import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.material.MaterialPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 报价换算率编辑前事件
 * @author xiahui
 * @date 创建时间：2019-1-22 下午6:13:16
 * @version ncc1.0
 **/
public class NqtunitrateBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);

		String pk_material = (String) userobject.get(CtAbstractBVO.PK_MATERIAL); // 物料
		String cqtunitid = (String) userobject.get(CtAbstractBVO.CQTUNITID); // 报价单位
		String cunitid = (String) userobject.get(CtAbstractBVO.CUNITID); // 主单位
		
		boolean fixedRate = MaterialPubService.queryIsFixedRateByMaterialAndMeasdoc(pk_material, cqtunitid); // 是否固定换算率
		if (StringUtil.isEqual(cqtunitid, cunitid) || fixedRate) {
			returnMap.put(WebEventConst.ISEDIT, false);
		}

		return returnMap;
	}

}

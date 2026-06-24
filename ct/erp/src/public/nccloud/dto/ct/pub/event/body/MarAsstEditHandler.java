package nccloud.dto.ct.pub.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.dto.ct.pub.util.marasst.MarAsstEditUtils;
import nccloud.dto.ct.pub.util.marasst.MarAsstFieldConst;

/**
 * @description 物料辅助属性编辑前控制
 * @author guozhq
 * @date 2018-8-15 下午1:20:10
 * @version ncc1.0
 */
public class MarAsstEditHandler implements IBeforeRule {

	private String editKey;

	public MarAsstEditHandler(String editKey) {
		this.editKey = editKey;
	}

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		// 获取物料版本主键
		String pk_material = (String) userobject.get(MarAsstFieldConst.PK_MATERIAL);
		MarAsstEditUtils utils = new MarAsstEditUtils();
		boolean flag = utils.checkFreeEdit(pk_material, editKey);
		// 构建返回值
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, flag);
		return returnMap;
	}
}

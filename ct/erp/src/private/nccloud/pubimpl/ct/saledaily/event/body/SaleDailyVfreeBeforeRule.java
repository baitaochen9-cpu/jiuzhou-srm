package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;
import nccloud.pubimpl.scmpub.event.helper.MarAsstBeforeEditHelper;

/**
 * @description 销售合同自由辅助属性编辑前
 * @author wangshrc
 * @date 2019年3月11日 上午9:31:38
 * @version ncc1.0
 */
public class SaleDailyVfreeBeforeRule implements IBeforeRule {
	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if (userobject == null || userobject.size() == 0) {
			returnMap.put(WebEventConst.ISEDIT, false);
			returnMap
					.put(WebEventConst.MESSAGE,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("4006013_0", "04006013-0025")/*
																			 * @res
																			 * "传入数据为空"
																			 */);
		}
		String cmaterialvid = (String) userobject.get("cmaterialvid");
		if (cmaterialvid == null) {
			returnMap.put(WebEventConst.ISEDIT, false);
			returnMap
					.put(WebEventConst.MESSAGE,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("4006013_0", "04006013-0026")/*
																			 * @res
																			 * "传入物料为空"
																			 */);
		}
		String pk_org = (String) userobject.get("pk_org");
		if (pk_org == null) {
			returnMap.put(WebEventConst.ISEDIT, false);
			returnMap
					.put(WebEventConst.MESSAGE,
							nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
									.getStrByID("4006013_0", "04006013-0028")/*
																			 * @res
																			 * "传入主组织为空"
																			 */);
		}

		String vfree = (String) userobject.get("key");
		MarAsstBeforeEditHelper helper = MarAsstBeforeEditHelper
				.getDefaultInstance(vfree, cmaterialvid);
		returnMap.put(WebEventConst.ISEDIT, helper.beforeEdit());

		return returnMap;
	}
}

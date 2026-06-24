package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 销售和同客户物料编辑前
 * @author wangshrc
 * @date 2019年2月19日 下午1:34:06
 * @version ncc1.0
 */
public class SaleDailyCustMaterialBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);
		String ctrantypeid = (String) userobject.get(CtAbstractVO.CTRANTYPEID);
		Integer ninvctlstyle = this.getNinvctlstyle(ctrantypeid);
		if (!ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value()))
			returnMap.put(WebEventConst.ISEDIT, false);
		return returnMap;
	}

	private Integer getNinvctlstyle(String ctrantypeid) {
		Integer intValue = null;
		try {
			IBusinessTypeService iBusiness = (IBusinessTypeService) NCLocator
					.getInstance().lookup(IBusinessTypeService.class.getName());
			intValue = iBusiness.queryMaterial(ctrantypeid);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return intValue;
	}
}

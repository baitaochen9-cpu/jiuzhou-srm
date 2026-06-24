package nccloud.pubimpl.ct.saledaily.event.body;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.business.enumeration.Ninvctlstyle;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.ValueUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nccloud.dto.scmpub.event.constance.WebEventConst;
import nccloud.dto.scmpub.pub.event.rule.IBeforeRule;

/**
 * @description 销售合同单位编辑前
 * @author wangshrc
 * @date 2019年2月19日 上午11:04:44
 * @version ncc1.0
 */
public class SaleDailyUnitBeforeRule implements IBeforeRule {

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject)
			throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);
		String ctrantypeid = (String) userobject.get(CtAbstractVO.CTRANTYPEID);
		String mat = (String) userobject.get(CtAbstractBVO.PK_MATERIAL);
		;
		// 物料控制方式
		Integer ninvctlstyle = this.getNinvctlstyle(ctrantypeid);
		if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())) {
			if (ValueUtil.isEmpty(mat)) {
				// 不能编辑
				returnMap.put(WebEventConst.ISEDIT, false);
			} else {
				// 如果是物料的时候 过滤报价单位;
				returnMap.put(WebEventConst.ISEDIT, true);
			}
		} else {
			returnMap.put(WebEventConst.ISEDIT, false);
		}
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

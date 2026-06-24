package nccloud.pubimpl.ct.purdaily.event.before.body;

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
 * @description 物料及物料分类公共编辑前
 * @author xiahui
 * @date 创建时间：2019-1-22 下午4:43:15
 * @version ncc1.0
 **/
public class MaterialAndMarbasclassBeforeRule implements IBeforeRule {

	private String key;

	public MaterialAndMarbasclassBeforeRule(String key) {
		this.key = key;
	}

	@Override
	public Map<String, Object> beforeEdit(Map<String, Object> userobject) throws BusinessException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(WebEventConst.ISEDIT, true);

		String ctrantypeid = (String) userobject.get(CtAbstractVO.CTRANTYPEID); // 交易类型
		Integer ninvctlstyle = this.getNinvctlstyle(ctrantypeid);		// 物料控制方式

		if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.WITHOUT.value())) {
			returnMap.put(WebEventConst.ISEDIT, false);
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MATERIAL.value())
				&& CtAbstractBVO.PK_MARBASCLASS.equals(this.key)) {
			returnMap.put(WebEventConst.ISEDIT, false);
		} else if (ValueUtil.equals(ninvctlstyle, Ninvctlstyle.MARBASCLASS.value())
				&& CtAbstractBVO.PK_MATERIAL.equals(this.key)) {
			returnMap.put(WebEventConst.ISEDIT, false);
		}

		return returnMap;
	}

	/**
	 * 获取交易类型控制方式
	 * 
	 * @param ctrantypeid
	 *          交易类型
	 * @return
	 */
	private Integer getNinvctlstyle(String ctrantypeid) {
		Integer intValue = null;
		try {
			IBusinessTypeService iBusiness = (IBusinessTypeService) NCLocator.getInstance().lookup(
					IBusinessTypeService.class.getName());
			intValue = iBusiness.queryMaterial(ctrantypeid);
		} catch (Exception e) {
			ExceptionUtils.wrappException(e);
		}
		return intValue;
	}

}

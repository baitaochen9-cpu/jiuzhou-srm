package nc.api.rest.ct.utils;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import com.google.gdata.util.common.base.StringUtil;

/**
 * 获取物料控制方式工具类
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-4-28 下午5:06:40   
 * @version NCC1909
 */
public class NinvctlsTypeUtil {

	/**
	 * 物料控制方式
	 * @param parentVO
	 * @return
	 */
	public static Object getNinvctlstyle(CtAbstractVO parentVO) {
	    // 物料控制方式
	    Object ninvctlstyle = parentVO.getNinvctlstyle();

	    if (null != ninvctlstyle) {
	      return ninvctlstyle;
	    }
	    // 合同类型
	    String ctrantypeid = parentVO.getCtrantypeid();
	    if (StringUtil.isEmptyOrWhitespace(ctrantypeid)) {
	      try {
	        IBusinessTypeService iBusiness =
	            (IBusinessTypeService) NCLocator.getInstance().lookup(
	                IBusinessTypeService.class.getName());
	        ninvctlstyle = iBusiness.queryMaterial(ctrantypeid);
	        // 物料控制方式
	        parentVO.setAttributeValue(CtAbstractVO.NINVCTLSTYLE, ninvctlstyle);
	      }
	      catch (Exception e1) {
	        ExceptionUtils.wrappException(e1);
	      }
	    }
	    return ninvctlstyle;
	  }
}

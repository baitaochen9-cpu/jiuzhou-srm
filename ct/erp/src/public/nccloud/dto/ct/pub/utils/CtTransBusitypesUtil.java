package nccloud.dto.ct.pub.utils;

import nc.bs.framework.common.NCLocator;
import nc.pubitf.ct.business.IBusinessTypeService;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.uitl.StringUtil;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * @description 物料控制方式工具类
 * @author xiahui
 * @date 创建时间：2019-1-29 上午10:58:22
 * @version ncc1.0
 **/
public class CtTransBusitypesUtil {

	/**
	 * 取得物料控制方式
	 * 
	 * @param util
	 * @return
	 */
	public static Integer getNinvctlstyle(ExtBillUtil util) {
		// 物料控制方式
		Integer ninvctlstyle = (Integer) util.getHeadValue(CtAbstractVO.NINVCTLSTYLE);

		if (null != ninvctlstyle) {
			return ninvctlstyle;
		}
		// 合同类型
		String ctrantypeid = util.getHeadTailStringValue(CtAbstractVO.CTRANTYPEID);
		if (!StringUtil.isEmptyTrimSpace(ctrantypeid)) {
			try {
				IBusinessTypeService iBusiness = (IBusinessTypeService) NCLocator.getInstance().lookup(
						IBusinessTypeService.class.getName());
				ninvctlstyle = iBusiness.queryMaterial(ctrantypeid);
				// 物料控制方式
				util.setHeadValue(CtAbstractVO.NINVCTLSTYLE, ninvctlstyle);
			} catch (Exception e1) {
				ExceptionUtils.wrappException(e1);
			}
		}

		return ninvctlstyle;
	}

	/**
	 * 根据合同交易类型设置物料控制方式
	 * 
	 * @param util
	 * @return
	 */
	public static Integer setNinvctlstyle(ExtBillUtil util) {
		Integer ninvctlstyle = null;
		// 合同类型
		String ctrantypeid = util.getHeadTailStringValue(CtAbstractVO.CTRANTYPEID);
		if (StringUtil.isEmptyTrimSpace(ctrantypeid)) {
			try {
				IBusinessTypeService iBusiness = (IBusinessTypeService) NCLocator.getInstance().lookup(
						IBusinessTypeService.class.getName());
				ninvctlstyle = iBusiness.queryMaterial(ctrantypeid);
				// 物料控制方式
				util.setHeadValue(CtAbstractVO.NINVCTLSTYLE, ninvctlstyle);
			} catch (Exception e1) {
				ExceptionUtils.wrappException(e1);
			}
		}

		return ninvctlstyle;
	}

}

package nccloud.web.ct.payplan.event;

import nc.vo.ct.purdaily.entity.PayPlanViewVO;
import nccloud.framework.core.reflect.Constructor;
import nccloud.framework.web.ui.config.Area;
import nccloud.framework.web.ui.config.PageTemplet;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.event.AbstractTableAfterAction;
import nccloud.web.scmpub.pub.utils.billcard.BillCardUtil;

/**
 * @description 表体编辑后Action
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:16:25
 * @version ncc1.0
 **/
public class BodyAfterEditAction extends AbstractTableAfterAction<PayPlanViewVO> {
	
//	@Override
//	protected PayPlanViewVO[] convertToVOs(PageTemplet templet, Grid grid) {
//		String areacode = grid.getModel().getAreacode();
//		Area area = templet.getArea(areacode);
//		if (area == null) {
//			return null;
//		}
//		
//		Class<?> clazz = Constructor.load(area.getClazz());
//		return (PayPlanViewVO[]) BillCardUtil.convertVO(clazz, grid);
//	}

	@Override
	public String getClassName() {
		return "nccloud.pubimpl.ct.payplan.event.BodyAfterEventHandler";
	}
}

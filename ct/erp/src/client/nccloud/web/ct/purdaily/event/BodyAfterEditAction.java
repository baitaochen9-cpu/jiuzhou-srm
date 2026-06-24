package nccloud.web.ct.purdaily.event;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;
import nccloud.web.scmpub.pub.event.ExtAbstractBodyAfterAction;

/**
 * @description 表体编辑后Action
 * @author xiahui
 * @date 创建时间：2019-1-17 下午4:16:25
 * @version ncc1.0
 **/
public class BodyAfterEditAction extends ExtAbstractBodyAfterAction<AggCtPuVO> {

	@Override
	protected String getClassName() {
		return "nccloud.pubimpl.ct.purdaily.event.BodyAfterEventHandler";
	}

	@Override
	protected ExtBillCard doAfterForExtBillCard(ExtBillCard extbillCard, Map<String, Object> userObject) {
		// 精度处理
		PrecisionUtil.setExtCardPrecision(extbillCard);
		return extbillCard;
	}

}

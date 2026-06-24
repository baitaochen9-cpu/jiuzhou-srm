package nccloud.web.ct.purdaily.action;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.purdaily.utils.Ref20TransferUtil;

/**
 * @description 采购合同维护拉请购单转换
 * @author xiahui
 * @date 创建时间：2019-2-18 上午10:53:47
 * @version ncc1.0
 **/
public class QueryCard20Action implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		ExtBillCard[] cards = null;
		try {
			Ref20TransferUtil transferUtils = new Ref20TransferUtil();
			AggCtPuVO[] vos = transferUtils.getAggCtPuVO(request);
			
			ExtBillCardOperator operator = new ExtBillCardOperator(transferUtils.getTransferInfo(request).getPagecode());
			if (vos != null && vos.length > 0) {
				cards = new ExtBillCard[vos.length];
				for (int i = 0; i < vos.length; i++) {
					cards[i] = operator.toCard(vos[i]);

					// 特殊字段翻译处理
					// FieldsTransferUtils.specialFeildsTransfer(cards[i]);
					// 精度处理
					// PrecisionUtil.setCardPrecision(cards[i]);
				}
			}
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return cards;
	}

}

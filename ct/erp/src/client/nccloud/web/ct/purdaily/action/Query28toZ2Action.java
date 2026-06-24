package nccloud.web.ct.purdaily.action;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.pubitf.purp.priceaudit.service.IPriceAuditMaintainService;
import nccloud.web.ct.pub.action.QueryInfo;
import nccloud.web.ct.purdaily.utils.PrecisionUtil;

/**
 * 
 * @description 通过盘pk查询价格审批单转为采购合同
 * @author zhangchqf
 * @date 2019年5月16日 下午7:04:22
 * @version ncc1.0
 */
public class Query28toZ2Action implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// 获取前台json
		String read = request.read();
		IJson json = JsonFactory.create();
		QueryInfo queryInfo = json.fromJson(read, QueryInfo.class);
		ExtBillCard[] cards = null;
		try {
			IPriceAuditMaintainService find = ServiceLocator
					.find(IPriceAuditMaintainService.class);
			AggCtPuVO[] vos = (AggCtPuVO[]) find
					.m28PriceAuditZ2(queryInfo.getPks());
			ExtBillCardOperator operator = new ExtBillCardOperator(
					queryInfo.getPagecode());
			cards = new ExtBillCard[vos.length];
			for (int i = 0; i < vos.length; i++) {
				cards[i] = operator.toCard(vos[i]);
				// 精度处理
				PrecisionUtil.setExtCardPrecision(cards[i]);
				// 特殊字段翻译处理
				// FieldsTransferUtils.specialFeildsTransfer(cards[i]);
			}
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return cards;

	}

}

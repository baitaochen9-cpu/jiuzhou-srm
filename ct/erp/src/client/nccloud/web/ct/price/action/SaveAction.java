package nccloud.web.ct.price.action;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;
/**
 * 
 * @description 价格信息表新增保存action 
 * @author zhaoypm
 * @time 2019-3-28 下午12:45:47
 * @since ncc1.0
 */
public class SaveAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		BillCardOperator operator = new BillCardOperator();
		AggCtPriceVO bill = (AggCtPriceVO) operator.toBill(request);

		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		try {
			AggCtPriceVO[] vos = service.insert(new AggCtPriceVO[] { bill });
			if (null == vos || vos.length == 0) {
				return null;
			}
			BillCard card = operator.toCard(vos[0]);
			this.processScale(card);
			return card;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}
	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body, CtPriceHeaderVO.CORIGCURRENCYID,
				PositionType.Head);
		operator.processPrecision();
	}
}

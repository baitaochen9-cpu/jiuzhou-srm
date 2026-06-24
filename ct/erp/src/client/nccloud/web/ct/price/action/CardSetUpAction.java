package nccloud.web.ct.price.action;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.ct.price.action.dto.QueryInfo;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;

/**
 *
 * @description 价格信息表，进入卡片态，通过主表pk查询主子表信息
 * @author zhaoypm
 * @time 2019-3-22 上午10:46:16
 * @since ncc1.0
 */
public class CardSetUpAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String read = request.read();
		IJson factory = JsonFactory.create();
		QueryInfo queryInfo = factory.fromJson(read, QueryInfo.class);
		String pk = null;
		// 获取pk
		String[] pks = queryInfo.getPks();
		if (null == pks || pks.length == 0) {
			return null;
		} else {
			pk = pks[0];
		}
		ISCMPubQueryService service = ServiceLocator.find(ISCMPubQueryService.class);
		AggCtPriceVO[] vos;
		try {
			vos = service.billquery(AggCtPriceVO.class, new String[] { pk });
			if (null == vos || vos.length == 0) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0000")/*@res "数据已经被删除，请返回列表界面！"*/);
			}
			BillCardOperator operator = new BillCardOperator("400400602_card");
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
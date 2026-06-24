package nccloud.web.ct.price.action;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
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
import nccloud.pubitf.ct.purdaily.service.ILinkCtPriceInfo;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;

/**
 *
 * @description 根据pk_ct_pu和pk_ct_pu_b联查价格信息表
 * @author zhaoypm
 * @time 2019-3-28 上午10:23:34
 * @since ncc1.0
 */
public class LinkQueryByPuAction implements ICommonAction {
	private static final String PAGEID = "pageId";

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		Map<String, Object> map = factory.fromJson(json, Map.class);
		String nqtorigtaxprice = (String) map.get(CtPuBVO.NQTORIGTAXPRICE);
		map.put(CtPuBVO.NQTORIGTAXPRICE, new UFDouble(nqtorigtaxprice));
		String pageId = (String) map.get(PAGEID);
		// 接口由@xiahui提供
		ILinkCtPriceInfo service = ServiceLocator.find(ILinkCtPriceInfo.class);
		try {
			AggCtPriceVO ctPriceAggVO = service.getCtPriceAggVO(map);
			if (null == ctPriceAggVO) {
				throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0",
						"04004132-0003")/* @res "没有联查到相关数据！" */);
			}
			BillCardOperator operator = new BillCardOperator(pageId);
			BillCard card = operator.toCard(ctPriceAggVO);
			this.processScale(card);
			setUserJson(card, ctPriceAggVO);
			return card;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private void setUserJson(BillCard card, AggCtPriceVO ctPriceAggVO) {
		UFBoolean isFromPurdaily = ctPriceAggVO.getIsFromPurdaily();
		UFBoolean isMultiPriceType = ctPriceAggVO.getIsMultiPriceType();
		JSONObject userobj = new JSONObject();
		userobj.put("isFromPurdaily", isFromPurdaily.booleanValue());
		userobj.put("isMultiPriceType", isMultiPriceType.booleanValue());
		card.setUserjson(userobj.toJSONString());
	}
	
	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body, CtPriceHeaderVO.CORIGCURRENCYID,
				PositionType.Head);
		operator.processPrecision();
	}

}
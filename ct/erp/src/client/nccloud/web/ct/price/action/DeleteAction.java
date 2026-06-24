package nccloud.web.ct.price.action;

import java.util.List;
import java.util.Map;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.scmpub.util.ArrayUtil;
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
import nccloud.pubitf.ct.price.service.ICtPriceQueryRecentlyService;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;

public class DeleteAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		Map<String, Object> map = factory.fromJson(json, Map.class);
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) map.get("pks");
		List<String> tss = (List<String>) map.get("tss");
		// 没有主键直接返回null
		if (null == list || list.size() == 0) {
			return null;
		}
		// 查询完整vo
		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		AggCtPriceVO[] aggvos = null;
		try {
			aggvos = ServiceLocator.find(ISCMPubQueryService.class).billquery(AggCtPriceVO.class,
					list.toArray(new String[] {}));
			if (ArrayUtil.isEmpty(aggvos)) {
				ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4004132_0","04004132-0002")/*@res "数据不存在，请刷新后重试"*/);
			}
			//合并新的ts
			for(AggCtPriceVO vo:aggvos) {
				for (int i = 0; i < list.size(); i++) {
					if(vo.getParentVO().getPk_ct_price().equals(list.get(i))) {
						vo.getParentVO().setTs(new UFDateTime(tss.get(i)));
					}
				}
			}
			service.delete(aggvos);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		// 是否查询最新版本
		Boolean queryRecently = (Boolean) map.get("queryRecently");
		// 查询最新版本（即上一版本）
		if (queryRecently) {
			// 仅在卡片态删除会有次过程，因此只取第一个元素，事实上也只有一个元素
			String pk_oid = aggvos[0].getParentVO().getPk_oid();
			AggCtPriceVO recentlyBill = queryRecentlyBill(pk_oid);
			if (null == recentlyBill) {
				return null;
			}
			BillCardOperator operator = new BillCardOperator("400400602_card");
			BillCard card = operator.toCard(recentlyBill);
			this.processScale(card);
			return card;
		}
		return null;
	}

	private AggCtPriceVO queryRecentlyBill(String pk_oid) {
		if (null == pk_oid || "".equals(pk_oid)) {
			return null;
		}
		ICtPriceQueryRecentlyService service = ServiceLocator.find(ICtPriceQueryRecentlyService.class);
		AggCtPriceVO recentlyBill = null;
		try {
			recentlyBill = service.queryRecentlyBillByPkOid(pk_oid);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		if (null == recentlyBill) {
			return null;
		}
		return recentlyBill;
	}

	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body,
				CtPriceHeaderVO.CORIGCURRENCYID, PositionType.Head);
		operator.processPrecision();
	}

}
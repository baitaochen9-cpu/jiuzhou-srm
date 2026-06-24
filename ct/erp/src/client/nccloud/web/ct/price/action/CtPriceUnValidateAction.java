package nccloud.web.ct.price.action;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.framework.web.ui.pattern.grid.GridOperator;
import nccloud.pubitf.scmpub.pub.service.ISCMPubQueryService;
import nccloud.web.ct.price.action.dto.CtPriceDTO;
import nccloud.web.ct.price.action.dto.CtPriceHeadDTO;

/**
 * 
 * @description 价格信息表失效
 * @author zhaoypm
 * @time 2019-3-26 下午2:04:54
 * @since ncc1.0
 */
public class CtPriceUnValidateAction implements ICommonAction {
	private static final String PAGECODE_CARD = "400400602_card";

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		CtPriceDTO dto = factory.fromJson(json, CtPriceDTO.class);
		// 页面类型，判断返回的是表头list还是单个card
		String pageId = dto.getPageId();
		CtPriceHeadDTO[] heads = dto.getHeads();
		if (null == heads || heads.length == 0) {
			return null;
		}
		AggCtPriceVO[] aggVOs = this.getAggVOs(heads);
		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		try {
			AggCtPriceVO[] vos = service.invalidate(aggVOs);
			if (PAGECODE_CARD.equals(pageId)) {
				// 卡片态操作返回
				BillCardOperator operator = new BillCardOperator(pageId);
				BillCard card = operator.toCard(vos[0]);
				return card;
			} else {
				// 列表态操作返回
				CtPriceHeaderVO[] headVOs = getHeadVOs(vos);
				GridOperator operator = new GridOperator();
				Grid grid = operator.toGrid(headVOs);
				return grid;
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	private AggCtPriceVO[] getAggVOs(CtPriceHeadDTO[] heads) {
		ISCMPubQueryService service = ServiceLocator
				.find(ISCMPubQueryService.class);
		String[] pks = getPks(heads);
		AggCtPriceVO[] aggVOs = null;
		try {
			aggVOs = service.billquery(AggCtPriceVO.class, pks);
			for (int i = 0; i < heads.length; i++) {
				CtPriceHeadDTO headDTO = heads[i];
				AggCtPriceVO aggVO = aggVOs[i];
				String pk_ct_price_dto = headDTO.getPk_ct_price();
				String pk_ct_price = aggVO.getParentVO().getPk_ct_price();
				if (pk_ct_price.equals(pk_ct_price_dto)) {
					aggVO.getParentVO().setTs(new UFDateTime(headDTO.getTs()));
				}
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}

		return aggVOs;
	}

	
	private String[] getPks(CtPriceHeadDTO[] heads) {
		String[] pks = new String[heads.length];
		for (int i = 0; i < heads.length; i++) {
			String pk_ct_price = heads[i].getPk_ct_price();
			pks[i] = pk_ct_price;
		}
		return pks;
	}
	
	private CtPriceHeaderVO[] getHeadVOs(AggCtPriceVO[] aggVOs) {
		CtPriceHeaderVO[] headVOs = new CtPriceHeaderVO[aggVOs.length];
		for (int i = 0; i < aggVOs.length; i++) {
			AggCtPriceVO aggVO = aggVOs[i];
			CtPriceHeaderVO headVO = (CtPriceHeaderVO) aggVO.getParent();
			headVOs[i] = headVO;
		}
		return headVOs;
	}
}

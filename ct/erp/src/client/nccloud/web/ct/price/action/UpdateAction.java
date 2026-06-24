package nccloud.web.ct.price.action;

import nc.itf.ct.price.ICtPriceMaintain;
import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.BusinessException;
import nccloud.dto.ct.price.constants.CtPriceConstants;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.convert.precision.PositionType;
import nccloud.framework.web.ui.model.row.Row;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.framework.web.ui.pattern.grid.Grid;
import nccloud.web.scmpub.pub.utils.scale.SCMBillCardPrecisionOperator;

/**
 * 
 * @description 价格信息表修改后保存action
 * @author zhaoypm
 * @time 2019-3-28 下午12:45:26
 * @since ncc1.0
 */
public class UpdateAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		BillCardOperator operator = new BillCardOperator();
		AggCtPriceVO bill = operator.toBill(request);
		this.addHeadPkToChildren(bill);
		BillCard originBillcard = operator.getOriginBillcard();
		ICtPriceMaintain service = ServiceLocator.find(ICtPriceMaintain.class);
		try {
			AggCtPriceVO[] vos = service.update(new AggCtPriceVO[] { bill });
			if (null == vos || vos.length == 0) {
				return null;
			}
			BillCard card = operator.toCard(vos[0]);
			fillRowId(originBillcard, card);
			this.processScale(card);
			return card;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 给要返回的card填充原有的rowid，供ncc前台更新ts用
	 * 
	 * @param originBillcard
	 * @param card
	 */
	private void fillRowId(BillCard originBillcard, BillCard card) {
		Grid originBody = originBillcard.getBody();
		Row[] originRows = originBody.getModel().getRows();
		if(null==card.getBody()) {
			// 没有表体了
			return;
		}
		Row[] rows = card.getBody().getModel().getRows();
		for (Row row : rows) {
			// 表体主键
			String pk_ct_price = row.getCell(CtPriceBodyVO.PK_CT_PRICE_B).getValue().toString();
			for (Row originRow : originRows) {
				// 原表体主键
				String origin_pk_ct_price = originRow.getCell(CtPriceBodyVO.PK_CT_PRICE_B).getValue() != null
						? originRow.getCell(CtPriceBodyVO.PK_CT_PRICE_B).getValue().toString()
						: "";
				if (origin_pk_ct_price.equals(pk_ct_price)) {
					// 找出主键对应的rowid
					String rowid = originRow.getRowid();
					row.setRowid(rowid);
					break;
				}
			}
		}
	}

	private void addHeadPkToChildren(AggCtPriceVO aggVO) {
		String pk_ct_price = aggVO.getParentVO().getPk_ct_price();
		CtPriceBodyVO[] children = aggVO.getChildrenVO();
		for (CtPriceBodyVO child : children) {
			child.setPk_ct_price(pk_ct_price);
		}
	}

	private void processScale(BillCard card) {
		SCMBillCardPrecisionOperator operator = new SCMBillCardPrecisionOperator(card);
		operator.addCurrencyPricePrecision(CtPriceConstants.pricekeys, PositionType.Body, CtPriceHeaderVO.CORIGCURRENCYID,
				PositionType.Head);
		operator.processPrecision();
	}
}

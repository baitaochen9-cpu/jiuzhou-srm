package nccloud.web.ct.saledaily.action;

import nc.itf.ct.bill.IQueryHistory;
import nc.itf.ct.saledaily.ISaledailyMaintainApp;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmpub.util.ArrayUtil;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;
import nccloud.web.ct.pub.action.QueryInfo;
import nccloud.web.ct.saledaily.utils.SaleDailyPrecisionUtil;

/**
 * @description 变更历史
 * @author cuijun
 * @date 创建时间：2019-5-09 上午9:27:04
 * @version ncc1.0
 **/
public class SaleDailyModifyHistoryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		QueryInfo info = json.fromJson(str, QueryInfo.class);

		try {
			ISaledailyMaintainApp service = ServiceLocator.find(ISaledailyMaintainApp.class);
			AggCtSaleVO[] bills = service.queryMZ3App(info.getPks());

			IQueryHistory queryHis = (IQueryHistory) ServiceLocator.find(IQueryHistory.class);
			AbstractBill[] hisBills = queryHis.queryCtSaleHistory(bills[0]);

			if (ArrayUtil.isEmpty(hisBills)) {
				ExceptionUtils.wrapBusinessException(
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0000")); // 此合同还没有变更历史！
			}

			return this.convertBillToCards(hisBills, info.getPagecode());
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * convert billVO to Card
	 * 
	 * @param bills
	 * @param pagecode
	 * @return
	 */
	private ExtBillCard[] convertBillToCards(AbstractBill[] bills, String pagecode) {
		ExtBillCard[] cards = new ExtBillCard[bills.length];
		ExtBillCardOperator operator = new ExtBillCardOperator(pagecode);
		for (int i = 0; i < bills.length; i++) {
			cards[i] = operator.toCard(bills[i]);
			// 精度处理
			SaleDailyPrecisionUtil.dealPrecision(cards[i]);
		}

		return cards;
	}

}

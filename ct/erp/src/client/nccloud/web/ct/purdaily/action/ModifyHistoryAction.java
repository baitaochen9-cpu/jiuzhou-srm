package nccloud.web.ct.purdaily.action;

import nc.itf.ct.bill.IQueryHistory;
import nc.itf.ct.purdaily.IPurdailyMaintainApp;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
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
import nccloud.web.ct.purdaily.utils.PrecisionUtil;

/**
 * @description 变更历史
 * @author xiahui
 * @date 创建时间：2019-4-28 上午9:27:04
 * @version ncc1.0
 **/
public class ModifyHistoryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		QueryInfo info = json.fromJson(str, QueryInfo.class);

		try {
			IPurdailyMaintainApp service = ServiceLocator.find(IPurdailyMaintainApp.class);
			AggCtPuVO[] bills = service.queryMZ2App(info.getPks());

			IQueryHistory queryHis = (IQueryHistory) ServiceLocator.find(IQueryHistory.class);
			AbstractBill[] hisBills = queryHis.queryCtPuHistory(bills[0]);

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
			PrecisionUtil.setExtCardPrecision(cards[i]);
		}

		return cards;
	}

}

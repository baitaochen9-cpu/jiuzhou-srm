package nccloud.web.ct.price.action;

import nc.vo.ct.price.entity.AggCtPriceVO;
import nc.vo.ct.price.entity.CtPriceBodyVO;
import nc.vo.ct.price.entity.CtPriceHeaderVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.container.SessionContext;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.billcard.BillCard;
import nccloud.framework.web.ui.pattern.billcard.BillCardOperator;
import nccloud.web.ct.price.action.dto.QueryInfo;

/**
 * 
 * @description 新增单据的时候，默认值设置和参照翻译action
 * @author zhaoypm
 * @time 2019-3-28 下午2:03:01
 * @since ncc1.0
 */
public class DefaultValuesQueryAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String json = request.read();
		IJson factory = JsonFactory.create();
		QueryInfo info = factory.fromJson(json, QueryInfo.class);
		String pk_org = info.getPk_org();
		String pageId = info.getPageId();
		String pk_group = SessionContext.getInstance().getClientInfo()
				.getPk_group();
		AggCtPriceVO aggVO = new AggCtPriceVO();
		CtPriceHeaderVO headVO = new CtPriceHeaderVO();
		headVO.setPk_group(pk_group);
		headVO.setPk_org(pk_org);
		headVO.setIversion(new UFDouble(1));
		headVO.setBvalidateflag(UFBoolean.valueOf(false));
		headVO.setBlatest(UFBoolean.valueOf(true));
		CtPriceBodyVO bodyVO = new CtPriceBodyVO();
		bodyVO.setPk_org(pk_org);
		bodyVO.setPk_group(pk_group);
		aggVO.setParent(headVO);
		aggVO.setChildrenVO(new CtPriceBodyVO[] { bodyVO });
		BillCardOperator operator = new BillCardOperator(pageId);
		BillCard card = operator.toCard(aggVO);
		return card;
	}

}

package nccloud.web.ct.pub.action;

import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.dto.ct.pub.entity.OperateType;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCard;
import nccloud.framework.web.ui.pattern.extbillcard.ExtBillCardOperator;

/**
 * @description 一主多子基础卡片查询Action
 * @author guozhq
 * @date 2018-8-8 下午3:56:56
 * @version ncc1.0
 */
public abstract class ExtBaseQueryCardAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			String str = request.read();
			IJson json = JsonFactory.create();
			QueryInfo info = json.fromJson(str, QueryInfo.class);
			// 查询Bill
			IBill[] bills = this.queryBill(info.getPks(), info.getUserObj());
			// 并发
			OperateExceptionUtils.checkVo(bills, OperateType.Refresh);
			// 转换
			ExtBillCardOperator operator = new ExtBillCardOperator(info.getPagecode());
			ExtBillCard retcard = operator.toCard(bills[0]);
			// 精度处理
			this.processPrecision(retcard);
			return retcard;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 
	 * 精度处理
	 * 
	 * @param retCard
	 * 
	 */
	public void processPrecision(ExtBillCard retCard) {
	}

	/**
	 * 
	 * 查询单据
	 * 
	 * @param map
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public abstract IBill[] queryBill(String[] ids, Map<String, Object> userObj) throws BusinessException;

}

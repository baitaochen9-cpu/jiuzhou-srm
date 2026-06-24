package nccloud.web.ct.saledaily.action;

import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.scmpub.pub.action.DataPermissionAction;

/**
 * @description 销售合同打印权限
 * @author cuijun
 * @date 2019-5-21 上午11:30:40
 * @version ncc1.0
 */
public class SaleDailyPrintDataPermissionAction extends DataPermissionAction {

	@Override
	public Object doAction(IRequest request) {
		try {
			String str = request.read();
			IJson json = JsonFactory.create();
			String[] ids = json.fromJson(str, String[].class);
			BillQuery<AggCtSaleVO> query = new BillQuery<AggCtSaleVO>(
					AggCtSaleVO.class);
			AggCtSaleVO[] bills = query.query(ids);
			this.checkPermission(bills);
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	public String getPermissioncode() {
		return "Z3";
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.PRINT.getActioncode();
	}

	public String getBillCodeField() {
		return CtSaleVO.VBILLCODE;
	}

}

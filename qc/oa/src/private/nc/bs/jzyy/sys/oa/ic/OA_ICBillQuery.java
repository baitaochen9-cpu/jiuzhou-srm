package nc.bs.jzyy.sys.oa.ic;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.oa.ic.m45.PurchaseInQuery;
import nc.bs.jzyy.sys.oa.saleorder.SaleOrderDelete;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.res.billtype.ICBillType;

import com.alibaba.fastjson.JSONObject;

public class OA_ICBillQuery extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException {

		JSONObject jsonObject = (JSONObject) billvo;
		JSONObject bill = jsonObject.getJSONObject("bill");
		String billtype = ICBillType.PurchaseIn.getCode();
		AbstracAdapter4Ext adpter = null;
		if (ICBillType.PurchaseIn.getCode().equals(billtype)) {
			adpter = new PurchaseInQuery();
		} else if ("".equals(billtype)) {
			adpter = new SaleOrderDelete();
		} else {
			throw new BusinessException("单据类型不存在");
		}

		JSONObject result = adpter.sys(billvo);
		// 将结果返回
		return result;
	}
}

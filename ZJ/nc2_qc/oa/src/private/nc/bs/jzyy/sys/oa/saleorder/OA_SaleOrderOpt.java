package nc.bs.jzyy.sys.oa.saleorder;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class OA_SaleOrderOpt extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException, DAOException

	{
		JSONObject jsonObject = (JSONObject) billvo;
		JSONObject bill = jsonObject.getJSONObject("bill");
		int action = bill.getIntValue("Action");
		// 0 增加 1 删除
		AbstracAdapter4Ext adpter = null;
		if (action == 0) {
			adpter = new SaleOrderSave();
		} else if (action == 1) {
			adpter = new SaleOrderDelete();
		} else {
			throw new BusinessException("同步动作类型不存在");
		}

		JSONObject result = adpter.sys(billvo);
		// 将结果返回
		return result;
	}

}

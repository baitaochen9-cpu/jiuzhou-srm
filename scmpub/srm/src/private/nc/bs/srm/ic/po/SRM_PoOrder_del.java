package nc.bs.srm.ic.po;

import nc.bs.logging.Logger;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class SRM_PoOrder_del extends PoOrderPublic {

	@Override
	public JSONObject process(Object billvo) throws BusinessException {
		// SaleOrderResource
		// nc.ui.so.m30.billui.editor.bodyevent.BodyBeforeEditHandler
		JSONObject jsonObject = (JSONObject) billvo;
		// 检索数据
		OrderVO[] order = null;
		try {
			JSONObject reqJson = jsonObject.getJSONObject("bill");
			JSONObject hjson = reqJson.getJSONObject("head");
			String vbillcode = hjson.getString("vbillcode");
			String pk_org = hjson.getString("pk_org");

			order = queryVOByCode(vbillcode, pk_org);
			del(order[0]);	
		
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(e.getMessage(), e);
			throw new BusinessException("操作采购订单出错:" + e.getMessage());
		}
		
		return getRsultJsonSuccess("删除采购订单成功" );

	}


	
	
	
}

package nc.bs.srm.sys;

import java.util.Map;

import nc.bs.logging.Log;
import nc.bs.srm.purchasein.PurchaseCancleSlgnPlugin;
import nc.bs.srm.purchasein.PurchaseInBodyAddPlugin;
import nc.bs.srm.qc.QuanlityAddPlugin;
import nc.bs.srm.qc.QuanlityDelPlugin;
import nc.vo.pub.BusinessException;
import nccloud.api.srm.ISysDispatcher;

import com.alibaba.fastjson.JSONObject;

public class SysDispatcherImpl implements ISysDispatcher {

	@Override
	public Object dispatch_RequiresNew(Object obj, String billltypecode, Map<String, Object> otherpms)
			throws BusinessException {
		Object rs = null;

		if ("SYS_DISPATCHER".equalsIgnoreCase(billltypecode)) {
			return new SYS_DISPATCHER().process(billltypecode, (JSONObject) obj, otherpms);
		} 
		else {
			throw new BusinessException("未支持的业务类型");
		}
	}

	@Override
	public Object dispatch(Object obj, String billltypecode, Map<String, Object> otherpms) throws BusinessException {
		Object rs = null;
		try{
			// 采购入库签字推srm
			if ("srm_m45_sign".equalsIgnoreCase(billltypecode)) {
				PurchaseInBodyAddPlugin plugin = new PurchaseInBodyAddPlugin();
				plugin.process(billltypecode, obj, otherpms);

			}
			// 采购入库取消签字推srm
			if ("srm_m45_cancelSign".equalsIgnoreCase(billltypecode)) {
				PurchaseCancleSlgnPlugin plugin = new PurchaseCancleSlgnPlugin();
				plugin.process(billltypecode, obj, otherpms);

			}
			// 生产厂商推srm
			if ("srm_qc_quanlity".equalsIgnoreCase(billltypecode)) {
				QuanlityAddPlugin plugin = new QuanlityAddPlugin();
				plugin.sendSrm(billltypecode, obj, otherpms);

			}
			// 生产厂商delete推srm
			if ("srm_qc_quanlitydel".equalsIgnoreCase(billltypecode)) {
				QuanlityDelPlugin plugin = new QuanlityDelPlugin();
				plugin.sendSrm(billltypecode, obj, otherpms);
			}
			
			
		} catch (Exception e) {
			Log.getInstance("jd-ncc").error(String.format("接口调用失败:%s", e));
			throw new BusinessException(e.getMessage());
		}

		return rs;
	}

	@Override
	public JSONObject process(JSONObject rqJsonObj) throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}

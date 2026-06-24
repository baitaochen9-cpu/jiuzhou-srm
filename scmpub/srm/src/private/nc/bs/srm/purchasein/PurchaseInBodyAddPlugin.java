package nc.bs.srm.purchasein;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.srm.pub.AbstractSender4Mdm;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 采购入库签字推送srm
 * 
 * @author sea
 *
 */
public class PurchaseInBodyAddPlugin {

	BaseDAO dao;

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	public void process(String billltypecode, Object obj, Map<String, Object> otherpms) throws BusinessException {
		AbstractSender4Mdm sender = new PurchaseInBodyAddSender();
		otherpms = new HashMap<String, Object>();
		try {
			
			// 新增日志
			JSONObject resp = (JSONObject) sender.process(billltypecode, obj, otherpms);
			if (resp != null) {
				// 判断一下返回报文，如果是更成功，则更新一下同步OA状态
				 JSONArray jsonArray = resp.getJSONArray("restResponseDtlDTOList");
				 JSONObject json = jsonArray.getJSONObject(0);
				String ERROR = json.getString("responseMessage");
				String responseStatus = json.getString("responseStatus");

				if (!"ERROR".equals(responseStatus)) {


				} else {
					throw new BusinessException("srm拒收." + ERROR!=null?ERROR:"未知的错误");
				}
			} else {
				throw new BusinessException("采购入库签字推送srm返回结果为空.");
			}
			// 更新日志
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException("采购入库签字推送srm同步失败:" + e.getMessage());
		}

	}

}

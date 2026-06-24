package nc.bs.srm.purchasein;

import java.text.ParseException;

import nc.bs.dao.DAOException;
import nc.bs.srm.pub.AbstractSender4Mdm;
import nc.bs.srm.pub.ApiProxy;
import nc.bs.srm.pub.EsbUtils;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pub.lang.UFDate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 采购入库取消签字报文
 * 
 * @author sea
 *
 */
public class PurchaseSlgnSender extends AbstractSender4Mdm {
	private static String URL_CODE = "purchasein";
	
	
	@Override
	public Object afterSend(Object response) throws Exception {
		// TODO Auto-generated method stub
		return response;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		return ApiProxy.httpPost(EsbUtils.getPostUrl(this.URL_CODE), sendJson);
	}

	public String getSendJson() throws DAOException, ParseException, UifException {
		PurchaseInVO bill = (PurchaseInVO) getParam();
		PurchaseInHeadVO head = (PurchaseInHeadVO) bill.getParent();
		PurchaseInBodyVO[] bodyarry = (PurchaseInBodyVO[]) bill.getChildrenVO();

		
		JSONObject supJson = new JSONObject();
		supJson.put("srccode", "NC");
		supJson.put("srcappkey", "4f128e5820ce494fb38fdaf8fbb92fb3");
		supJson.put("targetcode", "SRM");
		supJson.put("targetrule", "SINV_RCV_TRX_IMP_C");
		supJson.put("vbillcode", "DB-20230919-007");
		JSONObject data = new JSONObject();
		JSONObject headData = new JSONObject();
		String applicationCode = EsbUtils.getPostUrl("srmcode");
		headData.put("applicationCode", applicationCode);
		headData.put("applicationGroupCode", "PUBLIC_CLOUD");
		headData.put("batchCount", "1");
		headData.put("batchNum", System.currentTimeMillis());
		headData.put("externalSystemCode", "JIUZHOU_7YTVO0DJ");
		headData.put("interfaceCode", "SINV_RCV_TRX_IMP");
		headData.put("userName", "");


		// 表头报文
		JSONArray bjson = new JSONArray();
		JSONObject codeJson = new JSONObject();
		codeJson.put("externalReverseFlag", 1);// 取消标识
		codeJson.put("esTrxNum", head.getVbillcode());//单据号
		 UFDate dbilldate = head.getDbilldate();
		 String substring = dbilldate.toString().substring(0, 4);
		codeJson.put("trxYear", substring);//入库日期前四位
		


		// 表体报文
		JSONArray bodys = new JSONArray();
		for (PurchaseInBodyVO item : bodyarry) {
			JSONObject bodyJson = new JSONObject();
			// 采购入库
			bodyJson.put("esTrxLineNum", item.getCrowno());//行号
			bodyJson.put("reverseFlag", 0);//按行取消标识 1取消 0不取消
			bodyJson.put("trxYear", substring);//入库日期前四位

			
		
			bodys.add(bodyJson);
		}
	codeJson.put("rcvTrxLineList", bodys);
		
		bjson.add(codeJson);
		/* //组装 */
		data.put("header", headData);
		data.put("body", bjson);
		supJson.put("data", data);
		String reJson = supJson.toString();
		return reJson;
	}

}

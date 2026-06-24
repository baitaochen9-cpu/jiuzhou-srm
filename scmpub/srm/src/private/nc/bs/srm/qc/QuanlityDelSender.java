package nc.bs.srm.qc;

import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.srm.pub.AbstractSender4Mdm;
import nc.bs.srm.pub.ApiProxy;
import nc.bs.srm.pub.EsbUtils;
import nc.bs.srm.pub.SenderQuerys;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pu.supqualistatus.SupplierqualityHVO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 生产厂商del推srm
 * @author yinsen.zhang
 *
 */
public class QuanlityDelSender extends AbstractSender4Mdm {
	private static String URL_CODE = "purchasein";

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getSendJson() throws Exception {
		// TODO Auto-generated method stub
		SupplierqualityHVO bill = (SupplierqualityHVO) getParam();
		SenderQuerys qry = new SenderQuerys();
		JSONObject zjJson = new JSONObject();
		zjJson.put("srccode", "NC");
		zjJson.put("srcappkey", "4f128e5820ce494fb38fdaf8fbb92fb3");
		zjJson.put("targetcode", "SRM");
		zjJson.put("targetrule", "SSLM_SUPPLY_ABILITY_IMP");
		zjJson.put("vbillcode", bill.getBillno());
		JSONObject data = new JSONObject();
		JSONObject header = new JSONObject();
		String applicationCode = EsbUtils.getPostUrl("srmcode");
		header.put("applicationCode", applicationCode);
		header.put("applicationGroupCode", "PUBLIC_CLOUD");
		header.put("batchCount", "1");
		header.put("batchNum", System.currentTimeMillis());
		header.put("externalSystemCode", "JIUZHOU_7YTVO0DJ");
		header.put("interfaceCode", "SSLM_SUPPLY_ABILITY_IMP");
		
		// --------------------以上基本为固定参数----------------------------------
		JSONArray body = new JSONArray();
		JSONObject bodyChildren = new JSONObject();
		// 供应商pk
		String pk_supplier = bill.getPk_supplier();
		Map<String, Object> supplier = qry.getSupplier(pk_supplier);
		bodyChildren.put("supplierCode",supplier.get("code"));
		bodyChildren.put("supplierName",supplier.get("name"));
		String pk_org = bill.getPk_org();
		String pkorgName = qry.getPkorgName(pk_org);
		String pkorgCode = qry.getPkorgCode(pk_org);
		bodyChildren.put("companyCode",pkorgCode);
		bodyChildren.put("companyName",pkorgName);
//		bodyChildren.put("supplierUnifiedSocialCode",supplier.get("taxpayerid"));
		
		JSONArray supplyAbilityLineList = new JSONArray();
		JSONObject supplyAbilityLineListChildren = new JSONObject();
		// 物料 pk 
		String pk_material = bill.getPk_material();
		Map<String, Object> material = qry.getMaterial(pk_material);
		supplyAbilityLineListChildren.put("itemCode", material.get("code"));
		supplyAbilityLineListChildren.put("itemName", material.get("name"));
		supplyAbilityLineListChildren.put("attributeVarchar1", material.get("materialspec"));
		supplyAbilityLineListChildren.put("attributeVarchar2", material.get("materialtype"));
		String pk_vendor = bill.getPk_vendor();
		String defdoc = qry.getDefdoc(pk_vendor);
		Map<String,Object> defdocCode = this.getDefdocCode(pk_vendor);
		supplyAbilityLineListChildren.put("manufacturer", defdocCode.get("code"));
		supplyAbilityLineListChildren.put("attributeVarchar3", defdocCode.get("name"));
		
		supplyAbilityLineListChildren.put("supplyFlag", "0");

		
		supplyAbilityLineList.add(supplyAbilityLineListChildren);
		bodyChildren.put("supplyAbilityLineList", supplyAbilityLineList);
		
		body.add(bodyChildren);
		data.put("header", header);
		data.put("body", body);
		zjJson.put("data", data);
		return zjJson.toString();
	}

	@Override
	protected Object send(String sendJson) throws Exception {
		// TODO Auto-generated method stub
		return ApiProxy.httpPost(EsbUtils.getPostUrl(this.URL_CODE), sendJson);
	}

	@Override
	protected Object afterSend(Object response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	private Map<String,Object> getDefdocCode(String pk) throws DAOException {
		String sql = "SELECT code,name from  bd_defdoc  where pk_defdoc ='" + pk
				+ "' and dr = 0 ;";
		Map<String,Object> map = (Map<String,Object>) getDao().executeQuery(sql, new MapProcessor());
		return map;
	}
}

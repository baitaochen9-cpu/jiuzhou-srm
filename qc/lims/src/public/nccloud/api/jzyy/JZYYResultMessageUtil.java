package nccloud.api.jzyy;

import java.util.List;
import java.util.Map;

import org.json.JSONString;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * @ClassName:
 * @author: 云峰网络 411072655
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public abstract class JZYYResultMessageUtil {

	public static String SUCCESSCODE = "201";

	public static String FAILED_CODE = "-1";

	/**
	 * 返回结果集处理
	 * 
	 * @param requestId
	 * @param status
	 * @param masg
	 * @param executeQuery
	 * @return
	 */
	public static JSONObject getSuccessQryRsultJson(
			List<Map<String, Object>> respData) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("status", "created");
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", "获取到" + respData.size() + "条数据");
		String datas = JSON.toJSONString(respData);
		JSONArray datasjson = JSONArray.parseArray(datas);
		jsobj.put("data_item", datasjson);
		return jsobj;
	}

	/**
	 * @param respData
	 * @param rqJosn
	 *            对方传入参数
	 * @return
	 */
	public static JSONObject getSuccessQryRsultJson(
			List<Map<String, Object>> respData, JSONObject rqJosn) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("status", "created");
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("id", rqJosn.getJSONObject("data").getString("id"));
		jsobj.put("message", "获取到" + respData.size() + "条数据");
		String datas = JSON.toJSONString(respData,
				SerializerFeature.WriteMapNullValue);
		// JSONArray datasjson = JSONArray.parseArray(Json);
		jsobj.put("data_item", datas);
		return jsobj;
	}

	/**
	 * @param respData
	 * @param rqJosn
	 *            对方传入参数
	 * @return
	 */
	public static JSONObject getSuccessQryRsultJson(JSONString respData,
			JSONObject rqJosn) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("status", "created");
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("id", rqJosn.getJSONObject("data").getString("id"));
		// jsobj.put("message", "获取到"+respData.size()+"条数据");
		String datas = JSON.toJSONString(respData,
				SerializerFeature.WriteMapNullValue);
		// JSONArray datasjson = JSONArray.parseArray(Json);
		jsobj.put("data_item", datas);
		return jsobj;
	}

	public static JSONObject getFailedRsultJson(String masg) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", FAILED_CODE);
		jsobj.put("status", "failed");
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", masg);
		return jsobj;
	}

	public static JSONObject getFailedRsultJson(String masg, String code) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", code);
		jsobj.put("status", "failed");
		jsobj.put("success", Boolean.FALSE);// Boolean.TRUE/FALSE
		jsobj.put("message", masg);
		return jsobj;
	}

	public static JSONObject getSuccessRsultJson(String message, String code) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", code);
		jsobj.put("status", "created");
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("message", message);
		return jsobj;
	}

	public static JSONObject getSuccessRsultJson(String message,
			JSONObject rqJosn) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("status", "created");
		jsobj.put("id", rqJosn.getJSONObject("data").getString("id"));
		jsobj.put("message", message);
		return jsobj;
	}

	public static JSONObject getSuccessRsultJson(String message) {
		JSONObject jsobj = new JSONObject();
		jsobj.put("code", SUCCESSCODE);
		jsobj.put("success", Boolean.TRUE);// Boolean.TRUE/FALSE
		jsobj.put("status", "created");
		jsobj.put("message", message);
		return jsobj;
	}

}

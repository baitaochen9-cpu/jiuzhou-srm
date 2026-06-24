package nccloud.api.srm;

import com.alibaba.fastjson.JSONObject;

public abstract class JzPdaResultMessageUtil {
	
	public static String SUCCESSCODE = "201";
	public static String FAILED_CODE = "-1";
	
	public static JSONObject getFailedRsuiltJson(String masg){
		JSONObject json = new JSONObject();
		json.put("code", FAILED_CODE);
		json.put("status", "failed");
		json.put("success", Boolean.FALSE);
		json.put("message", masg);
		return json;
	}
	public static boolean isJson(String str){
		boolean result = true;
		try{
			JSONObject.parse(str);
		}catch(Exception e){
			result =false;
		}
		return result;
	}
}

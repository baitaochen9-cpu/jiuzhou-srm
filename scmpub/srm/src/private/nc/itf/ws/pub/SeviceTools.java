package nc.itf.ws.pub;

import java.lang.reflect.Field;

import org.json.JSONString;

import nc.vo.pub.BusinessException;
import nc.vo.scmpub.api.rest.utils.RestUtils;
import net.sf.json.JSONObject;
 public class  SeviceTools{
	 public static  String REQUESTID = "requestId";
	 public static String APPID ="appId";
	 public static String APPKEY ="appKey";
	 public static String ACTIONTYPE = "actionType" ;
	 public static String ACCOUNT = "account";
	 public static String PROJECTTYPE = "projectType";
	 public static String CODE = "code";
	/**
	 * 反射实体属性
	 * @param classname
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	 @SuppressWarnings("all")
	 public static Field[] getFild(String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException{

		Class clazz = Class.forName(classname);
		
			Object obj = clazz.newInstance();
			Field[] declaredFields = clazz.getDeclaredFields();
			return declaredFields;
			
	 }
	 
	 public static JSONString getRespRes(String requestId,Boolean returnFlag,String returnCode,String returnMessage){
		 JSONObject json = new JSONObject();
		 json.put("requestId", requestId);
		 json.put("returnFlag", returnFlag);
		 json.put("returnCode", returnCode);
		 json.put("returnMessage",returnMessage);
		 json.toString();
		 return RestUtils.toJSONString(json);
	 }
}
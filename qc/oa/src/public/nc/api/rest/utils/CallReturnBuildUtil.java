package nc.api.rest.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 第三方调用openapi返回信息组装工具
 * @Description: 
 *   
 * @author: 刘伟
 * @date:   2019-5-25 下午4:27:54   
 * @version NCC1909
 */
public class CallReturnBuildUtil {

  
  /**
   * json参数，组织、单据日期必输
   */
  private static String PARAM_ERROR = "传入参数错误,组织、单据日期条件必输";
  
  /**
   * json格式错误
   */
  private static String PARAM_FORMAT = "参数格式不正确,只能为字符串或数组";
  
  /**
   * json缺少表头表体信息
   */
  private static String PARAM_LACKHEADORBODY = "传入数据异常，参数要包含表头信息和表体信息";
  
  /**
   * 根据json参数在NC匹配不到单据
   */
  private static String PARAM_NOHADINFO = "根据传入数据未匹配到相关数据";
  
  /**
   * 参数为空
   */
  private static String PARAM_NULL = "传入参数为空,请检查";
  
  
  public static Map<String, Object> buildNullParamResult(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", PARAM_NULL);
    result.put("code", 1);
    return result;
  }
  
  /**
   * 常用条件查询 组织、单据日期条件必输校验信息
   * @param result
   * @return
   */
  public static Map<String, Object> buildOrgDateResult(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", PARAM_ERROR);
    result.put("code", 1);
    return result;
  }
  
  /**
   * json参数的格式跟方法定义类型不一致，报json参数格式错误
   * @param result
   * @return
   */
  public static Map<String, Object> buildParamFormatResult(String key){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", key + PARAM_FORMAT);
    result.put("code", 1);
    return result;
  }
  
  /**
   * json参数的格式异常，缺少表头表体信息
   * @param result
   * @return
   */
  public static Map<String, Object> buildHeadBodyResult(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", PARAM_LACKHEADORBODY);
    result.put("code", 1);
    return result;
  }
  
  /**
   * 根据json参数在NC匹配不到单据
   * @param result
   * @return
   */
  public static Map<String, Object> buildnoHadResult(){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", PARAM_NOHADINFO);
    result.put("code", 1);
    return result;
  }
  
  /**
   * 调用执行成功返回结果
   * @param result  结果map
   * @param data   返回的结果集
   * @param message 返回描述
   * @return
   */
  public static Map<String, Object> buildSuccessResult(Object data, String message){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", message);
    result.put("data", data);
    result.put("code", 1);
    return result;
  }
  
  /**
   * 调用执行失败返回结果
   * @param result  结果map
   * @param data   返回的结果集
   * @param message 返回描述
   * @return
   */
  public static Map<String, Object> buildFailResult(Object data, String message){
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("success", true);
    result.put("message", message);
    result.put("data", data);
    result.put("code", 0);
    return result;
  }
  
  
}

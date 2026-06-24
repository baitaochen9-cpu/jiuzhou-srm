package nccloud.api.srm;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import nc.vo.pub.BusinessException;


/**
 * 分发请求接口
 * @author liyf
 *
 */
public interface ISysDispatcher{
	
	/**
	 * 分发请求接口：单条控制事务
	 * @param o
	 * @param billltypecode
	 * @param param
	 * @return
	 * @throws BusinessException
	 * */
	public Object dispatch_RequiresNew(Object o,String billtypecode, Map<String,Object> param) throws BusinessException;
	
	
	/**
	 * 分发请求接口:注意如果是档案同步的，单条控制事务的，则用dispatch_RequiresNew
	 * @param o
	 * @param billltypecode
	 * @param param
	 * @return
	 * @throws BusinessException
	 * @throws Exception 
	 */
	public Object dispatch(Object o,String billltypecode, Map<String,Object> param) throws BusinessException;


	/**
	 * 分发请求接口:注意如果是档案同步的，单条控制事务的，则用dispatch_RequiresNew
	 * @param o
	 * @param billltypecode
	 * @param param
	 * @return
	 * @throws BusinessException
	 */
	public JSONObject process(JSONObject rqJsonObj) throws BusinessException;

		
}

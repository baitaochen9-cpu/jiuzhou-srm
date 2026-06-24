package nccloud.api.jzyy;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.jzyy.sys.ISysDispatcher;
import nc.itf.uap.cil.ICilService;
import nc.login.bs.INCUserQueryService;
import nc.vo.scmpub.api.rest.utils.RestUtils;
import nc.vo.scmpub.res.Module;
import nc.vo.sm.UserVO;
import uap.ws.rest.resource.AbstractUAPRestResource;

import org.json.JSONObject;
//import com.alibaba.fastjson.JSONObject;


/**
 * 九州药业API地址
 * http://ip:port/uapws/rest/jzyy/data/openapi
 * @author yunfeng.li
 *
 */
@Path("jzyy/data")
public class JzyyDataApi extends AbstractUAPRestResource {


	  @POST
	  @Produces("application/json")
	  @Path("openapi")
	  public Object operate(JSONObject qryData) {

		  com.alibaba.fastjson.JSONObject respJson = null;
		  if(qryData == null) {
			  Logger.error("输入数据异常:请求参数为空");
			  respJson = JZYYResultMessageUtil.getFailedRsultJson("输入数据异常:请求参数为空");
			  return respJson.toJSONString();
		  }
		  Logger.info("请求参数->>"+respJson);
		  com.alibaba.fastjson.JSONObject rqJsonObj = com.alibaba.fastjson.JSONObject.parseObject(qryData.toString());
		  try{
//				checkData(rqJsonObj);
				String dataSource= rqJsonObj.getString("dataSource");
				//创建一个用户名为:raybow_lims
				setNcToken("raybow_lims",dataSource);
			}catch(Exception e){
				Logger.error("NC回执-->>"+e);
				return  JZYYResultMessageUtil.getFailedRsultJson(e.getMessage());
			}
		  
		  //设置登录信息
		  RestUtils.initInvocationInfo();
		  try{
			  //调用接口单独事务出来，否则又回滚的问题
		      ISysDispatcher sys = NCLocator.getInstance().lookup(ISysDispatcher.class);
		      respJson =  (com.alibaba.fastjson.JSONObject) sys.dispatch_RequiresNew(rqJsonObj, "SYS_DISPATCHER", null);
			}catch(Exception e){
				Logger.error("NC回执-->>"+e);
				return  JZYYResultMessageUtil.getFailedRsultJson(e.getMessage());
			}
			return respJson;

	  }

	  
	  
	  /**
	     * 设置权限
	     * @throws nc.vo.pub.BusinessException 
	     * @throws nc.vo.pub.BusinessException 
	     */
	    public void setNcToken(String user_code,String datasourse) throws nc.vo.pub.BusinessException  {
	        //越过权限
	        InvocationInfoProxy.getInstance().setGroupId("0001V110000000000FH0");
	        InvocationInfoProxy.getInstance().setUserDataSource(datasourse);
	        InvocationInfoProxy.getInstance().setUserCode(user_code);
	        UserVO userVO = NCLocator.getInstance().lookup(INCUserQueryService.class)
	                .findUserVO(datasourse, user_code);	   
	        if (userVO != null) {
	            InvocationInfoProxy.getInstance().setGroupId(userVO.getPk_group());
	            InvocationInfoProxy.getInstance().setUserId(userVO.getPrimaryKey());
	        }
	        ISecurityTokenCallback tc = (ISecurityTokenCallback) NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
	        byte[] token = tc.token("__system".getBytes(), "annoymous".getBytes());
	        NetStreamContext.setToken(token);
	        ICilService service = (ICilService) NCLocator.getInstance().lookup(ICilService.class);
	        boolean ncdemo = service.isNCDEMO();
	    }
	  
	

	private void checkData(JSONObject rqJsonObj) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String getModule() {
		// TODO Auto-generated method stub
		return Module.QC.getName();
	}
}

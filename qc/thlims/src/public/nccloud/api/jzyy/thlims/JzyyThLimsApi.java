package nccloud.api.jzyy.thlims;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.jzyy.sys.thlims.ISysDispatcherThLims;
import nc.itf.uap.cil.ICilService;
import nc.login.bs.INCUserQueryService;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.api.rest.utils.RestUtils;
import nc.vo.scmpub.res.Module;
import nc.vo.sm.UserVO;
import nccloud.api.jzyy.JZYYResultMessageUtil;
import uap.ws.rest.resource.AbstractUAPRestResource;

import com.alibaba.fastjson.JSONObject;


/**
 * 九州药业泰华limis API地址
 * http://ip:port/uapws/rest/jzyy/thlims/openapi
 * @author yunfeng.li
 *
 */
@Path("jzyy/thlims")
public class JzyyThLimsApi extends AbstractUAPRestResource {


	  @POST
	  @Produces("application/json")
	  @Path("openapi")
	  public Object operate(JSONObject qryData) {
          
		  JSONObject respJson = null;
		  if(qryData == null) {
			  Logger.error("输入数据异常:请求参数为空");
			  respJson = JZYYResultMessageUtil.getFailedRsultJson("输入数据异常:请求参数为空");
			  return respJson.toJSONString();
		  }
		  Logger.info("请求参数->>"+respJson);
		  JSONObject rqJsonObj = JSONObject.parseObject(qryData.toJSONString());
		  try{
				checkData(rqJsonObj);
				//NC65-NCC1903里面请求的时候 么有严格的token和数据源校验机制，可能不会传，因此考虑从报文强制传一下
				String dataSource= rqJsonObj.getString("dataSource");
				if(StringUtils.isEmpty(dataSource)){
					throw new BusinessException("请求报文未填 dataSource");
				}
//				//创建一个用户名为:LIMS
				setNcToken("LIMS",dataSource);
			}catch(Exception e){
				Logger.error("NC回执-->>"+e);
				return  JZYYResultMessageUtil.getFailedRsultJson(e.getMessage());
			}
		  
		  //设置登录信息
		  RestUtils.initInvocationInfo();
		  try{
			  //调用接口单独事务出来，否则又回滚的问题
		      ISysDispatcherThLims sys = NCLocator.getInstance().lookup(ISysDispatcherThLims.class);
		      respJson =  (JSONObject) sys.dispatch_RequiresNew(rqJsonObj, "THLIM_NC", null);
			}catch(Exception e){
				Logger.error("NC回执-->>"+e);
				return  JZYYResultMessageUtil.getFailedRsultJson(e.getMessage());
			}
			return respJson;

	  }

	  
	  
	  /**
	     * 设置权限
	     * @throws nc.vo.pub.BusinessException 
	     */
	    public void setNcToken(String user_code,String datasourse) throws BusinessException  {
	        //越过权限
	        InvocationInfoProxy.getInstance().setGroupId("0001V110000000000FH0");
	        InvocationInfoProxy.getInstance().setUserDataSource(datasourse);
	        InvocationInfoProxy.getInstance().setUserCode(user_code);
	        UserVO userVO = NCLocator.getInstance().lookup(INCUserQueryService.class)
	                .findUserVO(datasourse, user_code);	   
	        if (userVO != null) {
	            InvocationInfoProxy.getInstance().setGroupId(userVO.getPk_group());
	            InvocationInfoProxy.getInstance().setUserId(userVO.getPrimaryKey());
	        }else{
	        	throw new BusinessException("未查询到NC用户:"+user_code);
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

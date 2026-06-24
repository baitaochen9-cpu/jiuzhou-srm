package nccloud.api.srm;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.comn.NetStreamContext;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.uap.cil.ICilService;
import nc.login.bs.INCUserQueryService;
import nc.vo.pub.BusinessException;
import nc.vo.scmpub.api.rest.utils.RestUtils;
import nc.vo.scmpub.res.Module;
import nc.vo.sm.UserVO;

import org.json.JSONString;

import uap.ws.rest.resource.AbstractUAPRestResource;

import com.alibaba.fastjson.JSONObject;

@Path("jzsrm/srm")
public class YonyouDataOperate extends AbstractUAPRestResource {

	String falseCode = "-1";

	@POST
	@Produces("application/json")
	@Path("api") 
	public Object operate(JSONString qryData) {
		boolean json = JzPdaResultMessageUtil.isJson(qryData.toString());
		if (qryData == null || !json) {
			Logger.error("输入数据异常:请求参数为空，或格式不正确");
			return  JzPdaResultMessageUtil.getFailedRsuiltJson("输入数据异常:请求参数为空，或格式不正确");

		}
		JSONObject rqJsonObj = null;
		try {
			rqJsonObj = JSONObject.parseObject(qryData.toJSONString());
			checkData(rqJsonObj);
			String dataSource = rqJsonObj.getString("dataSource");
			setNcToken("SRM",dataSource );
			

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
//			return RestUtils.toString(new BusinessException(e.getMessage()));
			return  JzPdaResultMessageUtil.getFailedRsuiltJson(e.getMessage());

		}

		// 设置登录信息
		RestUtils.initInvocationInfo();
		//
		if(InvocationInfoProxy.getInstance().getGroupId() == null) {
			InvocationInfoProxy.getInstance().setGroupId("0001V110000000000FH0");//
		}

		JSONString respJson = null;
		try {
			ISysDispatcher sys = NCLocator.getInstance().lookup(ISysDispatcher.class);
			// 每一次分发到单独的事务里面去处理,否则可能出现推进来A，自动推下游B，B单单不能回滚的情况
			respJson = (JSONString) sys.dispatch_RequiresNew(rqJsonObj, "SYS_DISPATCHER", null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return JzPdaResultMessageUtil.getFailedRsuiltJson(e.getMessage());

		}

		return respJson;

	}

	
	private void setNcToken(String user_code,String datasource) throws BusinessException{
		//越过权限
		InvocationInfoProxy.getInstance().setGroupId("0001V110000000000FH0");
		InvocationInfoProxy.getInstance().setUserDataSource(datasource);
		InvocationInfoProxy.getInstance().setUserCode(user_code);
		UserVO userVO = NCLocator.getInstance().lookup(INCUserQueryService.class).findUserVO(datasource, user_code);
		if(userVO != null){
				InvocationInfoProxy.getInstance().setGroupId(userVO.getPk_group());
				InvocationInfoProxy.getInstance() .setUserId(userVO.getPrimaryKey()) ;
		}
		ISecurityTokenCallback tc = (ISecurityTokenCallback) NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		byte[] token = tc.token("_system".getBytes (),"annoymous" .getBytes () );
		NetStreamContext.setToken(token) ;
		ICilService service = (ICilService) NCLocator.getInstance().lookup(ICilService.class);
		boolean ncdemo = service.isNCDEMO() ;
	}
	
	private void checkData(JSONObject rqJsonObj) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getModule() {
		
		return Module.SCMPUB .getName();
	}
}

package nc.bs.jzyy.sys.thlims2nc;

import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.jdbc.framework.processor.MapListProcessor;
import nccloud.api.jzyy.JZYYResultMessageUtil;
import nccloud.base.exception.BusinessException;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: MDM_WULIAO
 * @Description:TODO(查询物料数据)
 * @author: 云峰网络 411072655 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */


/*
 * 传入参数
 * 
 * {
		"functype": "TH_LIMS_Q_MATERIAL",
	    "data":{
	        "id":"rq123123",
	        "startTime": "2022-06-18 10:39:05",
	        "endTime": "2022-06-18 11:23:38"
	    }
	}
 * 
 * */
public class TH_LIMS_Q_MATERIAL extends AbstracAdapter4Ext{

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject rqJosn = (JSONObject) billvo;
		//检索数据
		List<Map<String,Object>> respData = null; 
		try {
			respData = queryRespData(rqJosn);
		} catch (Exception e) {
			return JZYYResultMessageUtil.getFailedRsultJson( "获取数据0条:"+e.getMessage());
		}
		if(respData == null || respData.size() ==0){
			return JZYYResultMessageUtil.getFailedRsultJson( "查询成功,匹配条件的0条");
		}
		//将结果返回			
		return JZYYResultMessageUtil.getSuccessQryRsultJson(respData,rqJosn);
	}

	

	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	private List<Map<String, Object>> queryRespData(JSONObject rqJosn) throws DAOException {
		
		List<Map<String,Object>> respData = null;  
		//查询语句
		String sql="select *  from V_TH_LIMS_Q_MATERIAL where 1=1 ";		
		//时间戳
		String startTime = rqJosn.getJSONObject("data").getString("startTime");
		
		String endTime = rqJosn.getJSONObject("data").getString("endTime");
		


		if(!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
			sql = sql+" and (TS BETWEEN '"+startTime+"' and '"+endTime+"')";
		}else if(!StringUtils.isEmpty(startTime)) {
			sql = sql+" and (TS >= '"+startTime+"' )";
		}else if(!StringUtils.isEmpty(endTime)) {
			sql = sql+" and (TS <= '"+endTime+"' )";
		}
		String  code = rqJosn.getJSONObject("data").getString("code");
		if(!StringUtils.isEmpty(code)) {
			sql = sql+" and code = '"+code+"' ";
		}
	    Object rs = getDao().executeQuery(sql,  new MapListProcessor());
 
		respData = (List<Map<String,Object>>)rs;
		
		return respData;
	}
	
	
	
}

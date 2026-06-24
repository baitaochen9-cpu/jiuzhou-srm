package nc.bs.jzyy.sys.oa.qry;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSONObject;



/**
 * 
 * @ClassName: 
 * @Description:TODO
 * @author: 云峰网络 411072655 
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public class OA_getProject extends AbstracAdapter4Ext{

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject rqJosn = (JSONObject) billvo;
		//检索数据
		List<Map<String,Object>> respData = null; 
		try {
			respData = queryRespData(rqJosn);
		} catch ( Exception e) {
			return getRsultJsonFailed( "获取数据0条:"+e.getMessage());
		}
		if(respData == null || respData.size() ==0){
			return getRsultJsonFailed( "查询成功,匹配条件的0条");
		}
		//将结果返回			
		return this.getRsultDataSuccess( respData);
	}

	

	/**
	 * 
	 * @return
	 * @throws DAOException
	 * @throws UnsupportedEncodingException 
	 */
	private List<Map<String, Object>> queryRespData(JSONObject rqJosn) throws DAOException, UnsupportedEncodingException {
		
		
		List<Map<String,Object>> respData = null;  
		
		
		//查询语句
		String sql=" select * from VIEW_JZ_project where 1=1 ";
		
	    Object rs = getDao().executeQuery(sql, maplistProcessor);
 
		respData = (List<Map<String,Object>>)rs;
		
		
		return respData;
	}
	
	
}

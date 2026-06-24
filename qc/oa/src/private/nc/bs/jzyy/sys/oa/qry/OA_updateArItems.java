package nc.bs.jzyy.sys.oa.qry;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:
 * @Description:TODO
 * @author: 云峰网络 411072655
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public class OA_updateArItems extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject rqJosn = (JSONObject) billvo;
		try {
			updateRespData(rqJosn);
		} catch (Exception e) {
			return getRsultJsonFailed("执行出错" + e.getMessage());
		}
		// 将结果返回
		return this.getRsultJsonSuccess("更新成功！");
	}
	/**
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws BusinessException 
	 */
	private void updateRespData(JSONObject rqJosn)
			throws UnsupportedEncodingException, BusinessException {	
		
		JSONArray list = rqJosn.getJSONArray("data");
		for(int i = 0 ; i < list.size() ;i++){
			Map<String, Object> map = (Map) list.get(i);
			String pk = (String)map.get("pk");
			String billno = (String)map.get("billno");
			String invoice = (String) map.get("invoice");
			String sql = "update ar_gatheritem set invoiceno = '" + invoice
					+ "',def20 = '" + billno + "' where pk_gatheritem = '" + pk
					+ "' ";
			getDao().executeUpdate(sql);
		}
	}

}

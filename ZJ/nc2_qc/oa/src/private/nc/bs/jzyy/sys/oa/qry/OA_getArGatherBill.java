package nc.bs.jzyy.sys.oa.qry;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:
 * @Description:TODO
 * @author: 云峰网络 411072655
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public class OA_getArGatherBill extends AbstracAdapter4Ext {

	@Override
	public JSONObject sys(Object billvo) throws BusinessException {
		// TODO Auto-generated method stub
		JSONObject rqJosn = (JSONObject) billvo;
		// 检索数据
		List<Map<String, Object>> respData = null;
		try {
			respData = queryRespData(rqJosn);
		} catch (Exception e) {
			return getRsultJsonFailed("获取数据0条:" + e.getMessage());
		}
		if (respData == null || respData.size() == 0) {
			return getRsultJsonFailed("查询成功,匹配条件的0条");
		}
		// 将结果返回
		return this.getRsultDataSuccess(respData);
	}
	/**
	 * 
	 * @return
	 * @throws DAOException
	 * @throws UnsupportedEncodingException
	 */
	private List<Map<String, Object>> queryRespData(JSONObject rqJosn)
			throws DAOException, UnsupportedEncodingException {
		List<Map<String, Object>> respData = null;
		// 获取查询条件
		String orgcode = rqJosn.getString("orgcode");
		String billno = rqJosn.getString("billno");
		String psncode = rqJosn.getString("psncode");
		String cuscode = rqJosn.getString("cuscode");
		String macode = rqJosn.getString("macode");
		// 查询语句
		String sql = "  select ar.*,rownum rn from VIEW_JZ_AR ar where ar.def20 ='N' and ar.invoiceno ='N' and 1=1 ";
		// 财务组织，单据号，客户，时间范围，业务员
		if (StringUtils.isNotEmpty(orgcode)) {
			sql = sql + "and ar.cwzz = '" + orgcode + "' ";
		}  
		if (StringUtils.isNotEmpty(billno)) {
			sql = sql + "and ar.djh = '" + billno + "' ";
		}  
		if (StringUtils.isNotEmpty(psncode)) {
			sql = sql + "and ar.psncode = '" + psncode + "' ";
		}  
		if (StringUtils.isNotEmpty(cuscode)) {
			sql = sql + "and ar.cuscode = '" + cuscode + "' ";
		}
		if (StringUtils.isNotEmpty(macode)) {
			sql = sql + "and ar.macode = '" + macode + "' ";
		}
		 //时间戳
 		String startTime = rqJosn.getString("startTime");
 		String endTime = rqJosn.getString("endTime");
         if(!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
 			sql = sql+" and (ar.djrq BETWEEN '"+startTime+"' and '"+endTime+"')";
 		}else if(!StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
 			sql = sql+" and ar.djrq >= '"+startTime+"'  ";
 		}else if(StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
 			sql = sql+" and ar.djrq <= '"+endTime+"' ";
 		}
        // pageSize 和 pageNo
 		String pageNo = rqJosn.getString("pageNo");
 		String pageSize = rqJosn.getString("pageSize");
 		if (!StringUtils.isEmpty(pageNo) && !StringUtils.isEmpty(pageSize)) {
 			sql =  "select a.* from  " + "(" + sql + ")a  " + " where rn>('" + pageNo + "'-1)*'" + pageSize
 	 				+ "' and rn < = '" + pageNo + "' * '" + pageSize + "' order by a.djrq desc,a.djh desc ";
 		} 
		Object rs = getDao().executeQuery(sql, maplistProcessor);
		respData = (List<Map<String, Object>>) rs;
		return respData;
	}

}

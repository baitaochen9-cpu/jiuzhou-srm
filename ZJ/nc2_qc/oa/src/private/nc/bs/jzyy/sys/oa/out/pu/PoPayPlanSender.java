package nc.bs.jzyy.sys.oa.out.pu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.bs.logging.Log;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.vo.pu.m21.entity.PayPlanViewVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class PoPayPlanSender extends AbstractSender4OA {
	
	private static final String H_VIEW="v_nc2oa_payplan_h";
	private static final String B_VIEW="v_nc2oa_payplan_b";
	

	@Override
	public Object afterSend(Object response) throws Exception {
		return response;
	}

	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub

	}

	

	@Override
	protected Object send(String sendJson) throws Exception {
		String result = "";
		String url =getSysOAIp();
		result = ApiProxy.httpPost(url, sendJson);
		return result;
	}

	@Override
	public String getSendJson() throws DAOException {
		
		PayPlanViewVO planViewVO=(PayPlanViewVO)getParam();
		
		if(StringUtils.isEmpty(planViewVO.getPk_order())){
			return "";
		}
		//1.查询表头数据
		HashMap<String,Object> headMap=(HashMap<String, Object>)this.QueryData(H_VIEW, planViewVO.getPk_order(), new MapProcessor());
		if(null==headMap){
			ExceptionUtils.wrappBusinessException("未查询到关联的订单信息!");
		}
		//2.查询表体数据
		List<HashMap<String,Object>> listMaps=(List<HashMap<String,Object>>)this.QueryData(B_VIEW, planViewVO.getPk_order(), new MapListProcessor());
		if(null==headMap){
			ExceptionUtils.wrappBusinessException("未查询到关联的订单明细信息!");
		}
		
		/*公共的查询接口
		JSONObject result = new JSONObject();
		result.put("srccode", "NC");
		result.put("srcappkey", "5671ab0ff745478898b9846a8a926be6");
		result.put("targetcode", "OA");
		result.put("targetrule", "addPlan");
		result.put("billCode", planViewVO.getVbillcode());
		JSONObject res = new JSONObject();
		res.put("hvo", new JSONObject(headMap));
		res.put("bvo", JSONArray.parseArray(JSON.toJSONString(listMaps)));
		result.put("data", res);
		String reJson = JSON.toJSONString(result);
		Logger.error("付款计划同步OA："+reJson);*/
		String reJson=this.Convert2OA_Value(headMap, listMaps);
		Log.getInstance("付款计划同步OA").error(reJson);
		return reJson;
	}
	
	
	/**
	 * 转换为OA数据格式
	 * @param headMap
	 * @param listMaps
	 */
	private String  Convert2OA_Value(HashMap<String,Object> headMap,List<HashMap<String,Object>> listMaps){
		// 主字段// 字段信息
		Set<Entry<String, Object>> H_keySet = headMap.entrySet();
		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
		for (Entry<String, Object> hashMap : H_keySet) {
			if("pk_order".equals(hashMap.getKey())){
				continue;
			}
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName", hashMap.getKey());
			detail.put("fieldValue", null!=hashMap.getValue()?hashMap.getValue().toString():"");
			
			mainData.add(detail);
		}
		//行集合数据
		List<Object> listrow_detailData = new ArrayList<Object>();
		//行索引
		int row_index=0;
		for (HashMap<String, Object> bodyMap : listMaps) {
			//行明细数据
			List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
			//每行字段数量
			Map<String, Object> row_detail = new HashMap<String, Object>();
			for(Entry<String, Object> hashMap :bodyMap.entrySet()){
				if("pk_order".equals(hashMap.getKey())){
					continue;
				}
				//每行字段数量
				Map<String, Object> detail = new HashMap<String, Object>();
				
				detail.put("fieldName", hashMap.getKey());
				detail.put("fieldValue", null!=hashMap.getValue()?hashMap.getValue().toString():"");
				
				detailData.add(detail);
			}
			row_detail.put("recordOrder", row_index);
			row_detail.put("workflowRequestTableFields", detailData);
			listrow_detailData.add(row_detail);
			row_index++;
		}
		  //行数据
		 
		  Map<String, Object> rowfirst = new HashMap<String, Object>();
		  rowfirst.put("tableDBName","formtable_main_176_dt1");
		  rowfirst.put("workflowRequestTableRecords",listrow_detailData);
		  
		  //第二层
		  //行明细
		  List<Object> rows_detailData = new ArrayList<Object>();
	      Map<String,Object> request = new HashMap<String, Object>();
	      request.put("requestName","采购付款计划");
	      request.put("workflowId","165");
	      rows_detailData.add(rowfirst);
	      request.put("detailData",JSONArray.parseArray(JSON.toJSONString(rows_detailData))); 
	      request.put("mainData",mainData); 
      
	      //第三层
	      Map<String,Object> data = new HashMap<String, Object>();
	      data.put("srccode","NC");
	      data.put("srcappkey","5671ab0ff745478898b9846a8a926be6");
	      data.put("targetcode","OA"); 
	      data.put("targetrule","addPlan");
	      data.put("data",request); 
          
	      //组装
		  String reJson = JSON.toJSONString(data);
		  Log.getInstance("同步OA").error(reJson);
		  return  reJson;
	}
	/**
	 * 查询数据
	 * @param sql
	 * @param resultSetProcessor
	 * @return
	 */
	private Object QueryData(String v_name,String pk_order,ResultSetProcessor resultSetProcessor ){
		String query_sql="SELECT * FROM "+v_name+" WHERE pk_order='"+pk_order+"'";
		try {
			return this.getQueryBS().executeQuery(query_sql,resultSetProcessor);
		} catch (BusinessException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return null;
	}
	
	
	private IUAPQueryBS iuapQueryBS;
	private IUAPQueryBS getQueryBS(){
		if(null==iuapQueryBS){
			iuapQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		}
		return iuapQueryBS;
	}
}

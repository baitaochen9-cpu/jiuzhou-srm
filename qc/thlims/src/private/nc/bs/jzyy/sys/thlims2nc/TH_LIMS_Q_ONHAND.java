package nc.bs.jzyy.sys.thlims2nc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.material.MaterialVO;
import nc.vo.scmf.ic.mbatchcode.BatchcodeVO;
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
	        "org_code": "",
	        "material_code": "",
	        "vbatchcode": ""
	    }
	}
 * 
 * */


public class TH_LIMS_Q_ONHAND extends AbstracAdapter4Ext{

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
		String sql="select *  from V_TH_LIMS_Q_ONHAND_SUM where 1=1 ";		
//		是否批次汇总
		String def1 = rqJosn.getJSONObject("data").getString("def1");
		if(def1!=null&&def1.equals("Y")){
			 sql="select *  from V_TH_LIMS_Q_ONHAND_SUM where 1=1 ";		
		}
		
		//物料
		String material_code = rqJosn.getJSONObject("data").getString("material_code");
		//批次
		String vbatchcode = rqJosn.getJSONObject("data").getString("vbatchcode");
		//公司
		String org_code = rqJosn.getJSONObject("data").getString("org_code");
		String group_no = rqJosn.getJSONObject("data").getString("group_no");
		
		String def2 = rqJosn.getJSONObject("data").getString("def2");
		String def3 = rqJosn.getJSONObject("data").getString("def3");
		String def4 = rqJosn.getJSONObject("data").getString("def4");
		String def5 = rqJosn.getJSONObject("data").getString("def5");
	
		
		if(!StringUtils.isEmpty(material_code)&&!StringUtils.isEmpty(material_code)){
			sql = sql+" and (material_code='"+material_code+"')";
		}else{
			throw  new DAOException("物料不能为空");
		}
		if(!StringUtils.isEmpty(vbatchcode)&&!StringUtils.isEmpty(vbatchcode)){
			sql = sql+" and (vbatchcode='"+vbatchcode+"')";
		}else{
			throw  new DAOException("批次号不能为空");
		}
		if(!StringUtils.isEmpty(org_code)&&!StringUtils.isEmpty(org_code)){
			sql = sql+" and (org_code='"+org_code+"')";
		}else{
			throw  new DAOException("所属公司编码不能为空");
		}
		if(!StringUtils.isEmpty(group_no)&&!StringUtils.isEmpty(group_no)){
			sql = sql+" and (group_no='"+group_no+"')";
		}
//		if(!StringUtils.isEmpty(def1)&&!StringUtils.isEmpty(def1)){
//			sql = sql+" and (def1='"+def1+"')";
//		}
		if(!StringUtils.isEmpty(def2)&&!StringUtils.isEmpty(def2)){
			sql = sql+" and (def2='"+def2+"')";
		}
		if(!StringUtils.isEmpty(def3)&&!StringUtils.isEmpty(def3)){
			sql = sql+" and (def3='"+def3+"')";
		}
		if(!StringUtils.isEmpty(def4)&&!StringUtils.isEmpty(def4)){
			sql = sql+" and (def4='"+def4+"')";
		}
		if(!StringUtils.isEmpty(def5)&&!StringUtils.isEmpty(def5)){
			sql = sql+" and (def5='"+def5+"')";
		}
	
	    Object rs = getDao().executeQuery(sql,  new MapListProcessor());
		respData = (List<Map<String,Object>>)rs;
		//1. 有现存量返回数据处理
		if(null!=respData && respData.size()>0){
			//根据批次自定义15 判断是否放行 2023年1月9日
			for (Map<String, Object> map : respData) {
				if(null!=map.get("vdef15")&& StringUtils.isNotEmpty(map.get("vdef15").toString())){
					
					if("VAR".equals(map.get("vdef15").toString())){
						//有现存量时 N
//						if(null!=map.get("num") && Double.valueOf(map.get("num").toString())>0){
//						}
						map.put("pass", "Y");

					}else{
						//无现存量时N
						if(null!=map.get("num") && Double.valueOf(map.get("num").toString())==0){
							map.put("pass", "N");
						}
					}
				}
			}
		}else{
			respData=new ArrayList<Map<String,Object>>();
			MaterialVO[] materialVOs=null;
			//1. 物料转换PK
			try {
				materialVOs=(MaterialVO[])this.getPubBO().queryByCondition(MaterialVO.class, "code='"+material_code+"' and dr=0");
			} catch (UifException e) {
				e.printStackTrace();
				throw  new DAOException(e.getMessage());
			}
			if(null==materialVOs || materialVOs.length==0){
				throw  new DAOException(material_code+ " 未查询到物料信息");
			}
			BatchcodeVO[] batchCodeVOs=null;
			//2.查批次信息
			try {
				batchCodeVOs=(BatchcodeVO[])this.getPubBO().queryByCondition(BatchcodeVO.class, "CMATERIALOID='"+materialVOs[0].getPrimaryKey()+"' AND VBATCHCODE='"+vbatchcode+"' AND DR=0");
			} catch (UifException e) {
				e.printStackTrace();
			}
			if(null==batchCodeVOs || batchCodeVOs.length==0){
				throw  new DAOException(material_code+ " "+vbatchcode +" 未查询到物料批次信息");
			}
			
			Map<String,Object> itemMap=new HashMap<String, Object>();
			itemMap.put("castunitid", materialVOs[0].getPk_measdoc());
			itemMap.put("cunit_name", "");
			itemMap.put("group_no", "G");
			itemMap.put("material_code", materialVOs[0].getCode());
			itemMap.put("num", "0");
			itemMap.put("org_code", "28");
			itemMap.put("pk_onhanddim", "");
			itemMap.put("storcode", "");
			itemMap.put("storname", "");
			itemMap.put("stordoc", "");
			itemMap.put("vbatchcode", "");
			itemMap.put("vdef15", batchCodeVOs[0].getVdef15());
			itemMap.put("pass", "N");
			if(null!=batchCodeVOs[0].getVdef15() && StringUtils.isNotEmpty(batchCodeVOs[0].getVdef15())){
				if("VAR".equals(batchCodeVOs[0].getVdef15())){
					itemMap.put("pass", "Y");
				}
			}
			respData.add(itemMap);
		}
		return respData;
	}
	
	
	private HYPubBO pubBO;
	public  HYPubBO getPubBO(){
		if(null==pubBO){
			pubBO=new HYPubBO();
		}
		return pubBO;
	}
	
}

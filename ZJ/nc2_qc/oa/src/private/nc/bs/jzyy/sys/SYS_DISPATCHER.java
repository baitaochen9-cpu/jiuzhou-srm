package nc.bs.jzyy.sys;

import java.util.Map;

import nc.bs.jzyy.sys.oa.OA2NCfuncRuleEnum;
import nccloud.base.exception.BusinessException;

import com.alibaba.fastjson.JSONObject;

public class SYS_DISPATCHER {
	
	public JSONObject process( String billltypecode, JSONObject rqJsonObj,Map<String,Object> otherpms) throws BusinessException {
		// TODO Auto-generated method stub
		//处理返回结果
		JSONObject rs;
		  try{
			    String funcType = rqJsonObj.getString("functype");
				AbstracAdapter4Ext adpter = null;
				/**
				 * OA--NC的分发处理
				 */
				if(funcType.startsWith("OA_")){
					OA2NCfuncRuleEnum ruleEnum = OA2NCfuncRuleEnum.match(funcType);
					if(ruleEnum != null ){
						adpter =ruleEnum.getAdapter1();
					}
				}
				if(adpter == null){
					throw new BusinessException("未支持的业务功能 functype="+funcType);
				}
				//
				rs= (JSONObject) adpter.process(rqJsonObj);
			}catch(Exception e){
				throw new BusinessException(e.getMessage());
			}
		  
		
		return rs;
	}

}

package nc.bs.jzyy.sys.oa.priceaudit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.bs.jzyy.sys.oa.out.SenderQuerys;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;

import com.alibaba.fastjson.JSON;

public class OA_PriceAuditSender extends AbstractSender4OA {

	@Override
	public Object afterSend(Object response) throws Exception {
		// TODO Auto-generated method stub
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

	public String getSendJson() throws DAOException {
		PriceAuditVO bill = (PriceAuditVO) getParam();
		PriceAuditHeaderVO  head = bill.getParentVO();
		PriceAuditItemVO[] bodys = bill.getChildrenVO();
		SenderQuerys query = new SenderQuerys();
		//报文
		//表头
		
		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
		String[] headkey = new String[]{"cgzz","jgsplx","jgspdh","cgy","cgbm","fkxy","xm","cq","erpcode"};
		for(String key : headkey) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName",key);
			Object value = null;
			if("cgzz".equalsIgnoreCase(key)) {
				value = getStringNullAssEmpoty(query.getPurchaseorg(head.getPk_org()));//采购组织
			}
			if("erpcode".equalsIgnoreCase(key)) {
				value = head.getVbillcode();//单据号
			}
			if("jgsplx".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(query.getBilltype(head.getCtrantypeid()));//价格审批单类型
			}
			if("jgspdh".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(head.getVbillcode());//审批单据号
			}
			if("cgy".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(query.getCemployername(head.getPk_bizpsn()));//采购员
			}
			if("cgbm".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(query.getDept(head.getPk_dept()));//采购编码
			}
			if("fkxy".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(query.getPayment(head.getPk_payterm()));//付款协议
			}
			if("xm".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(head.getVdef2());//项目
			}
			if("cq".equalsIgnoreCase(key)) {
				value =  getStringNullAssEmpoty(query.getDefdoc(head.getVdef1()));//厂区
			}
			
			detail.put("fieldValue", value);
			mainData.add(detail);
		}
        
        //-------------------------------------------------------------------------------------------------------------------------------------------
        
        //表体
        List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
        Map<String, Object> first = new HashMap<String, Object>();
        List<Map<String, Object>> workflowRequestTableRecords = new ArrayList<Map<String, Object>>(); 
         for(PriceAuditItemVO body : bodys) { 
            //表体第二层
            Map<String, Object> second = new HashMap<String, Object>();
            List<Map<String, Object>>  workflowRequestTableRecords2 = new ArrayList<Map<String, Object>>();
            String[] bodykey = new String[]{"hh","wlbm","wlmc","xh","gg","gys",
            		"sccs","bz","zdw","zsl","zwsdj","zhsdj","dw","sl","wsdj","hsdj","bjsxrq","bjsxrq1"};
            for(String key : bodykey) {
            	Map<String, Object> secondDetail = new HashMap<String, Object>();
				secondDetail.put("fieldName", key);
				Object value = null;
				if("hh".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(body.getCrowno());//行号
				}
				Map<String,Object> ma = query.getMaterial(body.getPk_material());//物料编码名称规格型号
				if("wlbm".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(ma.get("code"));
				}
				if("wlmc".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(ma.get("name"));
				}
				if("xh".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(ma.get("materialtype"));
				}
				if("gg".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(ma.get("materialspec"));
				}
				if("gys".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(query.getSuppliername(body.getPk_supplier()));//供应商
				}
				if("sccs".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(query.getDefdoc(body.getCproductorid()));//生产厂商
				}
				if("bz".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(query.getCurrtype(body.getCorigcurrencyid()));//币种
				}
				if("zdw".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(query.getCastunitid(body.getCunitid_show()));//主单位
				}
				if("dw".equalsIgnoreCase(key)) {
					value = getStringNullAssEmpoty(query.getCastunitid(body.getCastunitid()));//单位
				}
				if("sl".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNastnum());//数量
				}
				if("zsl".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNnum_show());//主数量
				}
				if("zwsdj".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigprice());//主无税单价
				}
				if("zhsdj".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigtaxprice());//主含税单价
				}
				if("wsdj".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigmny());//无税单价
				}
				if("hsdj".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigtaxmny());//含税单价
				}
				if("bjsxrq".equalsIgnoreCase(key)) {
					String timeFormat2 = body.getDqtvaliddate().toString();
	                String[] time = timeFormat2.split(" ");
					value = time[0];//报价生效日期
				}
				if("bjsxrq1".equalsIgnoreCase(key)) {
					if(body.getDqtinvaliddate()!=null){
						String timeFormat2 = body.getDqtinvaliddate().toString();
		                String[] time = timeFormat2.split(" ");
						value = time[0];//报价失效日期
					}else{
						value = " ";
					}
					
				}
				secondDetail.put("fieldValue", value);
				workflowRequestTableRecords2.add(secondDetail);
            }
            
            //组装单据
            second.put("recordOrder","0");
            second.put("workflowRequestTableFields",workflowRequestTableRecords2);
            workflowRequestTableRecords.add(second);
           
            }
            first.put("tableDBName","formtable_main_170_dt1");
            first.put("workflowRequestTableRecords",workflowRequestTableRecords);
            detailData.add(first);	
        
        //第二层
        Map<String,Object> request = new HashMap<String, Object>();
        request.put("requestName","价格审批单");
        request.put("workflowId","159");
        request.put("detailData",detailData); 
        request.put("mainData",mainData); 
        
        //第三层
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("srccode","NC");
        data.put("billCode",head.getVbillcode());
        data.put("srcappkey","5671ab0ff745478898b9846a8a926be6");
        data.put("targetcode","OA"); 
        data.put("targetrule","price_approval_form");
        data.put("data",request); 
            
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	return  reJson;
	}
}

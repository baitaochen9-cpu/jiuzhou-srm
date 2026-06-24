package nc.bs.jzyy.sys.oa.out.ctsale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.FileUtil;
import nc.bs.jzyy.sys.FileVO;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.bs.jzyy.sys.oa.out.SenderQuerys;
import nc.vo.ct.saledaily.entity.AggCtSaleVO;
import nc.vo.ct.saledaily.entity.CtSaleBVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.pp.m28.entity.PriceAuditHeaderVO;
import nc.vo.pp.m28.entity.PriceAuditItemVO;
import nc.vo.pp.m28.entity.PriceAuditVO;

import com.alibaba.fastjson.JSON;

public class OA_CtSaleSender extends AbstractSender4OA {

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getSendJson() throws DAOException {
		AggCtSaleVO bill = (AggCtSaleVO) getParam();
		CtSaleVO  head = bill.getParentVO();
		CtSaleBVO[] bodys = bill.getCtSaleBVO();
		SenderQuerys query = new SenderQuerys();
		//报文
		//表头
		
		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
		String[] headkey = new String[]{"pk_org","vbillcode","ctrantypeid","personnelid","pk_customer","ctname","ntotalorigmny","subscribedate","valdate","invallidate",
				"depid_v","corigcurrencyid","pk_payterm","vdef1"};
		for(String key : headkey) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName",key);
			Object value = null;
			if("pk_org".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getOrg(head.getPk_org()));//组织
			}
			if("vbillcode".equalsIgnoreCase(key)) {
				value = head.getVbillcode();//单据号
			}
			if("ctname".equalsIgnoreCase(key)) {
				value = head.getCtname();//合同名称
			}
			if("subscribedate".equalsIgnoreCase(key)) {
				String timeFormat2 = head.getSubscribedate().toString();
				if(timeFormat2!=null){
					String[] time = timeFormat2.split(" ");
					value = time[0];//合同签订日期
				}else{
					value = " ";
				}
	            
			}
			if("valdate".equalsIgnoreCase(key)) {
				String timeFormat2 = head.getValdate().toString();
				if(timeFormat2!=null){
	               String[] time = timeFormat2.split(" ");
				   value = time[0];//计划生效日期
			    }else{
				   value = " ";
			    }
			}
			if("invallidate".equalsIgnoreCase(key)) {
				String timeFormat2 = head.getInvallidate().toString();
				if(timeFormat2!=null){
	               String[] time = timeFormat2.split(" ");
				   value = time[0];//计划失效日期
			    }else{
				   value = " ";
			    }
			}
			if("ctrantypeid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getBilltype(head.getCtrantypeid()));//类型
			}
			if("depid_v".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getDept(head.getDepid()));//部门
			}
			if("personnelid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getBillmakercode(head.getBillmaker()));//业务员
			}
			if("corigcurrencyid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getCurrtype(head.getCorigcurrencyid()));//币种
			}
			if("pk_customer".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getCus(head.getPk_customer()));//客户
			}
			if("ntotalorigmny".equalsIgnoreCase(key)) {
				value =  getNullAsZero(head.getNtotalorigmny());//价税合计
			}
			if("pk_payterm".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getIncome(head.getPk_payterm()));//收款协议
			}
			if("vdef1".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getDefdoc(head.getVdef1()));//厂区
			}
			detail.put("fieldValue", value);
			mainData.add(detail);
		}
		// 附件
		FileVO[] ss = FileUtil.queryFiles(head.getPk_ct_sale(), true);
		if (ss.length > 0) {
			Map detail = new HashMap();
			List array = new ArrayList();
			for (FileVO vo : ss) {
				Map files = new HashMap();
				String base64 = vo.getBase64Str();
				String name = vo.getName();
				files.put("fileName", name);
				files.put("filePath", "base64:"+base64);
				array.add(files);
			}
			detail.put("fieldName", "xgfj");
			detail.put("fieldValue", array);
			mainData.add(detail);
		}
        //-------------------------------------------------------------------------------------------------------------------------------------------
        
        //表体
        List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
        Map<String, Object> first = new HashMap<String, Object>();
        List<Map<String, Object>> workflowRequestTableRecords = new ArrayList<Map<String, Object>>(); 
         for(CtSaleBVO body : bodys) { 
            //表体第二层
            Map<String, Object> second = new HashMap<String, Object>();
            List<Map<String, Object>>  workflowRequestTableRecords2 = new ArrayList<Map<String, Object>>();
            String[] bodykey = new String[]{"pk_material_name","pk_material","cunitid","pk_marbasclass","pk_material_vmanufacturer_148","nnum",
            		"norigtaxmny","nqtorigtaxprice","norigmny","ntaxrate","vmemo"};
            for(String key : bodykey) {
            	Map<String, Object> secondDetail = new HashMap<String, Object>();
				secondDetail.put("fieldName", key);
				Object value = null;
				Map<String,Object> ma = query.getMaterial(body.getPk_material());//物料编码名称规格型号
				if("pk_material".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("code"));
				}
				if("pk_material_name".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("name"));
				}
				if("pk_marbasclass".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getMarbasclass(body.getPk_marbasclass()));//物料分类
				}
				if("pk_material_vmanufacturer_148".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getDefdoc(body.getCproductorid()));//生产厂商
				}
				if("cunitid".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getCastunitid(body.getCunitid()));//主单位
				}
				if("nnum".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNnum());//主数量
				}
				if("norigtaxmny".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigtaxmny());//价税合计
				}
				if("nqtorigtaxprice".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNqtorigtaxprice());//含税单价
				}
				if("norigmny".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNorigmny());//无税金额
				}
				if("ntaxrate".equalsIgnoreCase(key)) {
					value = getNullAsZero(body.getNtaxrate());//税率
				}
				if("vmemo".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVmemo());//备注
				}
				secondDetail.put("fieldValue", value);
				workflowRequestTableRecords2.add(secondDetail);
            }
            
            //组装单据
            second.put("recordOrder","0");
            second.put("workflowRequestTableFields",workflowRequestTableRecords2);
            workflowRequestTableRecords.add(second);
           
            }
            first.put("tableDBName","formtable_main_194_dt1");
            first.put("workflowRequestTableRecords",workflowRequestTableRecords);
            detailData.add(first);	
        
        //第二层
        Map<String,Object> request = new HashMap<String, Object>();
        request.put("requestName","采购合同");
        request.put("workflowId","168");
        request.put("detailData",detailData); 
        request.put("mainData",mainData); 
        
        //第三层
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("srccode","NC");
        data.put("billCode",head.getVbillcode());
        data.put("billmaker",getNullAsEmpty(query.getBillmakercode(head.getBillmaker())));
        data.put("srcappkey","5671ab0ff745478898b9846a8a926be6");
        data.put("targetcode","OA"); 
        data.put("targetrule","ct_sale_form");
        data.put("data",request); 
            
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	return  reJson;
	}
}

package nc.bs.jzyy.sys.oa.saleordersync;

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
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;

import com.alibaba.fastjson.JSON;

public class OA_SaleOrderSender extends AbstractSender4OA {

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

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public String getSendJson() throws DAOException {
		SaleOrderVO bill = (SaleOrderVO) getParam();
		SaleOrderHVO  head = bill.getParentVO();
		SaleOrderBVO[] bodys = bill.getChildrenVO();
		SenderQuerys query = new SenderQuerys();
		//报文
		//表头
		
		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
		String[] headkey = new String[]{"pk_org","vbillcode","vdef14","cjdh","ctrantypeid","dbilldate","cemployeeid","cdeptvid","corigcurrencyid","ctransporttypeid","ccustomerid"
				,"cinvoicecustid","cpaytermid","ntotalnum","ntotalorigmny","badvfeeflag"};
		for(String key : headkey) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName",key);
			Object value = null;
			if("pk_org".equalsIgnoreCase(key)) {
				value = getNullAsEmpty(query.getOrg(head.getPk_org()));//销售组织
			}
			if("vbillcode".equalsIgnoreCase(key)) {
				value = head.getVbillcode();//单据号
			}
			if("vdef14".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(head.getVdef14());//客户订单号
			}
			if("cjdh".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(head.getVdef6());//成交单号
			}
			if("ctrantypeid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getBilltype(head.getCtrantypeid()));//订单类型
			}
			if("dbilldate".equalsIgnoreCase(key)) {
				String timeFormat2 = head.getDbilldate().toString();
                String[] time = timeFormat2.split(" ");
				value = time[0];//单据日期
			}
			if("cemployeeid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getBillmakercode(head.getBillmaker()));//业务员
			}
			if("cdeptvid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getDept(head.getCdeptvid()));//部门
			}
			if("corigcurrencyid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getCurrtype(head.getCorigcurrencyid()));//币种
			}
			if("ctransporttypeid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getTransport(head.getCtransporttypeid()));//运输方式
			}
			if("ccustomerid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getTransport(head.getCcustomerid()));//客户
			}
			if("cinvoicecustid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getTransport(head.getCinvoicecustid()));//开票客户
			}
			if("cpaytermid".equalsIgnoreCase(key)) {
				value =  getNullAsEmpty(query.getIncome(head.getCpaytermid()));//收款协议
			}
			if("ntotalnum".equalsIgnoreCase(key)) {
				value =  String.valueOf(getNullAsZero(head.getNtotalnum()));//总数量
			}
			if("ntotalorigmny".equalsIgnoreCase(key)) {
				value =  String.valueOf(getNullAsZero(head.getNtotalorigmny()));//价税合计
			}
			if("badvfeeflag".equalsIgnoreCase(key)) {
				value =  head.getBadvfeeflag().toString();//代垫运费，是or否
			}
			detail.put("fieldValue", value);
			mainData.add(detail);
		}
        //附件
		FileVO[] ss = FileUtil.queryFiles(head.getCsaleorderid(), true);
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
         for(SaleOrderBVO body : bodys) { 
            //表体第二层
            Map<String, Object> second = new HashMap<String, Object>();
            List<Map<String, Object>>  workflowRequestTableRecords2 = new ArrayList<Map<String, Object>>();
            String[] bodykey = new String[]{"cmaterialvid","pk_material_name","cmaterialvid_materialspec","cunitid","nnum","nqtorigprice","nqtorigtaxprice",
            		"norigtaxmny","nqtorigtaxnetprc","nqtorignetprice","ctaxcodeid","ntaxrate","vrownote","isgift_148","vbdef6","vbdef2","vbdef3"};
            for(String key : bodykey) {
            	Map<String, Object> secondDetail = new HashMap<String, Object>();
				secondDetail.put("fieldName", key);
				Object value = null;
				Map<String,Object> ma = query.getMaterial(body.getCmaterialid());//物料编码名称规格型号
				if("cmaterialvid".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("code"));
				}
				if("pk_material_name".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("name"));
				}
				if("cmaterialvid_materialspec".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(ma.get("materialspec"));
				}
				if("cunitid".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getCastunitid(body.getCunitid()));//主单位
				}
				if("nnum".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNnum()));//主数量
				}
				if("nqtorigprice".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNqtorigprice()));//不含税单价
				}
				if("nqtorigtaxprice".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNqtorigtaxprice()));//含税单价
				}
				if("norigtaxmny".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNorigtaxmny()));//含税总价
				}
				if("nqtorigtaxnetprc".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNqtorigtaxnetprc()));//含税净价
				}
				if("nqtorignetprice".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNqtorignetprice()));//无税净价
				}
				if("ctaxcodeid".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(query.getTaxcode(body.getCtaxcodeid()));//税码
				}
				if("ntaxrate".equalsIgnoreCase(key)) {
					value = String.valueOf(getNullAsZero(body.getNtaxrate()));//税率
				}
				if("vrownote".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVrownote());//备注
				}
				if("isgift_148".equalsIgnoreCase(key)) {
					String gift = body.getBlargessflag().toString();//是否赠品
					if(gift.equalsIgnoreCase("Y")){
						value = "是";
					}else{
						value = "否";	
					}
							
				}
				if("vbdef6".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVbdef6());//质量市场
				}
				if("vbdef2".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVbdef2());//要求交货日期
				}
				if("vbdef3".equalsIgnoreCase(key)) {
					value = getNullAsEmpty(body.getVbdef3());//预计发货日期
				}
				secondDetail.put("fieldValue", value);
				workflowRequestTableRecords2.add(secondDetail);
            }
            
            //组装单据
            second.put("recordOrder","0");
            second.put("workflowRequestTableFields",workflowRequestTableRecords2);
            workflowRequestTableRecords.add(second);
           
            }
            first.put("tableDBName","formtable_main_195_dt1");
            first.put("workflowRequestTableRecords",workflowRequestTableRecords);
            detailData.add(first);	
        
        //第二层
        Map<String,Object> request = new HashMap<String, Object>();
        request.put("requestName","销售订单");
        request.put("workflowId","169");
        request.put("detailData",detailData); 
        request.put("mainData",mainData); 
        
        //第三层
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("srccode","NC");
        data.put("billCode",head.getVbillcode());
        data.put("billmaker",getNullAsEmpty(query.getBillmakercode(head.getBillmaker())));
        data.put("srcappkey","5671ab0ff745478898b9846a8a926be6");
        data.put("targetcode","OA"); 
        data.put("targetrule","so_saleorder_form");
        data.put("data",request); 
            
        //组装
	  	String reJson = JSON.toJSONString(data);
	  	return  reJson;
	}
}

package nc.bs.jzyy.sys.oa.out.ct;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuBVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.SuperVOUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CtpuSaveSender extends AbstractSender4OA {
	

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

	@Override
	public String getSendJson() throws DAOException {
		AggCtPuVO   Data = (AggCtPuVO )((AggCtPuVO ) getParam()).clone();
		fillData(Data);
		CtPuVO    head = Data.getParentVO();
		CtPuBVO[] bodys = Data.getCtPuBVO();
		// 公共的查询接口
		JSONObject result = new JSONObject();
		result.put("srccode", "NC");
		result.put("srcappkey", "5671ab0ff745478898b9846a8a926be6");
		result.put("targetcode", "OA");
		result.put("targetrule", "addCustomer");
		result.put("billCode", head.getVbillcode());
		//addCustomer-- 添加；updCustomer--更新；
		JSONObject res = new JSONObject();

		

	//	
//		1	采购组织	cgzz  pk_org
//		2	合同类型	htlx   ctrantypeid
//		3	合同号	hth     vbillcode 
//		4	供应商	 gys   cvendorid
//		5	联系人	lxr
//		6	电话	dh
//		7	合同计划生效日期	htjhssrq
//		8	合同计划终止日期	htjhzzrq
//		9	合同签订日期	htqdrq
//		10	采购员	cgy    personnelid
//		11	合同金额	htje
//		11	合同金额	htje  ccurrencyid
		Map<String,String>hmap=new HashMap<String,String>(){{
			put("pk_org","cgzz");
			put("ctrantypeid","htlx");
			put("ctname","htmc");
		
			put("cvendorid","gys");
			put("cprojectid","xm");
//			TODO 5	联系人	lxr
//			6	电话	dh
			put("depid","cgbm");
			put("personnelid","cgy");
			put("valdate","htjhsxrq");
			put("invallidate","htjhzzrq");
			put("subscribedate","htqdrq");
			put("personnelid","cgy");
			put("vbillcode","erpcode");
			put("ntotalorigmny","htje");
		}
	
		};
		JSONArray hArray =new JSONArray();
		for(String key:hmap.keySet()){
			JSONObject hvo = new JSONObject();
			if("htjhsxrq".equalsIgnoreCase(hmap.get(key))) {
				String timeFormat2 = head.getAttributeValue(key)==null?"":head.getAttributeValue(key).toString();
                String[] time = timeFormat2.split(" ");
                hvo.put("fieldName", hmap.get(key));
    			hvo.put("fieldValue",time[0]);
			}else if("htjhzzrq".equalsIgnoreCase(hmap.get(key))){
				String timeFormat2 = head.getAttributeValue(key)==null?"":head.getAttributeValue(key).toString();
                String[] time = timeFormat2.split(" ");
                hvo.put("fieldName", hmap.get(key));
    			hvo.put("fieldValue",time[0]);
			}else if("htqdrq".equalsIgnoreCase(hmap.get(key))){
				String timeFormat2 = head.getAttributeValue(key)==null?"":head.getAttributeValue(key).toString();
                String[] time = timeFormat2.split(" ");
                hvo.put("fieldName", hmap.get(key));
    			hvo.put("fieldValue",time[0]);
			}else{
				hvo.put("fieldName", hmap.get(key));
				hvo.put("fieldValue",head.getAttributeValue(key)==null?"":head.getAttributeValue(key).toString());
			}
			hArray.add(hvo);
		}
		JSONObject hvo = new JSONObject();
		hvo.put("fieldName", "hth");
		hvo.put("fieldValue",head.getAttributeValue("vbillcode")==null?"":head.getAttributeValue("vbillcode").toString());
		hArray.add(hvo);
//		
//		1	行号	hh	
//		2	物料编码	wlbm	pk_srcmaterial
//		3	物料名称	wlmc	  pk_material
//		4	型号	xh	         vbdef3
//		5	规格	gg	          vbdef2 
//		6	生产厂商	sccs	 cproductorid 
//		7	币种	bz	            表头
//		8	主单位	zdw	
//		9	主数量	zsl	
//		10	主无税单价	zwsdj	
//		11	主含税单价	zhsdj	
//		12	单位	dw	    cunitid
//		13	数量	sl	
//		14	无税单价	wsdj	
//		15	含税单价	hsdj	
//		16	无税金额	wsje	
//		17	价税合计	jshj	
//		18	税码	sm	            ctaxcodeid 
//		19	税率	sl1	
//		20	税额	se	
//		21	备注	bz1	
//		22	项目	xm	
//		23	费用部门	fybm	
//		24	维修工单号	wxdgh	
//		25	收货库存组织	shkczz	
		
		
		Map<String,String>bmap=new HashMap<String,String>(){{
			put("crowno","hh");
			put("pk_srcmaterial","wlbm");
			put("pk_material","wlmc");
			put("vbdef2","gg");
			put("vbdef3","xh");
			put("cproductorid","sccs");
			put("cunitid","zdw");
			put("nnum","zsl");
			put("castunitid","dw");
			put("nastnum","sl");
			put("ngprice","zwsdj");
			put("ngtaxprice","zhsdj");
//			无税单价	wsdj
//			含税单价	hsdj
			put("norigprice","wsdj");
			put("norigtaxprice","hsdj");
			
			
			put("norigmny","wsje");
			put("norigtaxmny","jshj");
			put("ctaxcodeid","sm");
			put("ntaxrate","sl1");
			put("ntax","se");
			put("vmemo","bz1");
			
//			put("vbdef1","fybm");
//			put("vbdef19","wxgdh");
			
			put("pk_arrvstoorg","shkczz");
			
		}
	
		};
		JSONArray childArray =new JSONArray();
		for(int i=0;i<bodys.length;i++){
			JSONObject bvo1 = new JSONObject();
			bvo1.put("recordOrder", i);
			JSONArray barr =new JSONArray();
			
			for(String key:bmap.keySet()){
				JSONObject bvo = new JSONObject();
				bvo.put("fieldName", bmap.get(key));
				bvo.put("fieldValue",bodys[i].getAttributeValue(key)==null?"": bodys[i].getAttributeValue(key).toString());
				barr.add(bvo);
			}
//			存放表头币种
			JSONObject bvo = new JSONObject();
			bvo.put("fieldName", "bz");
			bvo.put("fieldValue",head.getAttributeValue("ccurrencyid")==null?"":head.getAttributeValue("ccurrencyid").toString());
			barr.add(bvo);
			bvo1.put("workflowRequestTableFields", barr);
			childArray.add(bvo1);
		}
		
		
		JSONArray childArray2 =new JSONArray();
		JSONObject bvo2 = new JSONObject();
		bvo2.put("tableDBName", "formtable_main_171_dt1");
		bvo2.put("workflowRequestTableRecords", childArray);
		childArray2.add(bvo2);	
        res.put("mainData", hArray);
		res.put("detailData", childArray2);
		res.put("workflowId", "160");
		res.put("formid", "171");
		res.put("requestName", "采购合同");
		res.put("opt", "approvectpu");
		result.put("data", res);
		String reJson = JSON.toJSONString(result);
		return reJson;
	}
	
	
	
	private void fillData(AggCtPuVO  order) throws DAOException {

		//
		String[] formulas_H46 = {
				// 最新的库存组织
				// 部门
				"depid->getColValue2(org_dept,name,pk_dept,depid,pk_org,pk_org)",
				"cprojectid ->getColValue(bd_project,project_name  ,pk_project ,cprojectid )",
				//	采购组织
				"pk_org->getColValue2(org_stockorg,  code , pk_stockorg,pk_org,islastversion ,\"Y\")",
//				供应商
				"cvendorid ->getColValue(bd_supplier,name,pk_supplier,cvendorid)",
//				业务员
				"personnelid ->getColValue(bd_psndoc,name,pk_psndoc,personnelid )",
				
				
				
//				币别
				"ccurrencyid->getColValue(bd_currtype,code,pk_currtype,ccurrencyid)",
				
//				合同类型
				"ctrantypeid ->getColValue2(bd_billtype,billtypename , pk_billtypeid,ctrantypeid ,pk_group,pk_group)"
				
		};
	

		SuperVOUtil.execFormulaWithVOs(new CtPuVO[] { order.getParentVO() },
				formulas_H46, null);
		
		
	
		
		// 填充表体付款协议
		String[] formulas_B46 = {
				// 物料编码
				"pk_srcmaterial->getColValue(bd_material,code,pk_material,pk_material)",
				// 物料名称
				"pk_material->getColValue(bd_material,name,pk_material,pk_material)",
				
//				规格
				"vbdef3->getColValue(bd_material,materialspec ,pk_material,pk_material)",
//				型号
				"vbdef2->getColValue(bd_material_v,materialtype ,pk_material,pk_material)",

				// 生产厂商
				"cproductorid  ->getColValue(bd_defdoc,name,pk_defdoc,cproductorid )",
				
				// 单位
//				"cunitid->getColValue(bd_measdoc, code,pk_measdoc,cunitid)",
				 "cunitid->getColValue(bd_measdoc, code,pk_measdoc,cunitid)",
				 "castunitid->getColValue(bd_measdoc, code,pk_measdoc,castunitid)",
				 
				 
				
//				/ 税码
				"ctaxcodeid ->getColValue(bd_taxcode,code ,pk_taxcode,ctaxcodeid)",
				
				
//				/ 项目
				"cbprojectid ->getColValue(bd_project,project_name  ,pk_project ,cbprojectid )",
				
				// 费用部门
				"vbdef1 ->getColValue(bd_defdoc,name,pk_defdoc,vbdef1)",
				
				
				// 需求库存组织
//				"pk_reqstoorg->getColValue2(org_purchaseorg,name,pk_purchaseorg ,pk_reqstoorg,islastversion ,\"Y\")",
				
				// 收货库存组织
				"pk_arrvstock->getColValue2(org_purchaseorg,name,pk_purchaseorg ,pk_arrvstock,islastversion ,\"Y\")"
				
				
				// 收货c仓库
//				"pk_recvstordoc->getColValue(bd_stordoc,name  ,pk_stordoc ,pk_recvstordoc)"
			

		};

		SuperVOUtil.execFormulaWithVOs(order.getCtPuBVO(), formulas_B46, null);

	

	}


}

package nc.bs.jzyy.sys.oa.out.pu;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.SuperVOUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PoOrderSaveSender extends AbstractSender4OA {
	

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
		OrderVO  Data = (OrderVO)((OrderVO) getParam()).clone();
		fillData(Data);
		OrderHeaderVO  head = Data.getHVO();
		OrderItemVO [] bodys = Data.getBVO();
		// 公共的查询接口
		JSONObject result = new JSONObject();
		result.put("srccode", "NC");
		result.put("srcappkey", "5671ab0ff745478898b9846a8a926be6");
		result.put("targetcode", "OA");
		result.put("targetrule", "addOrder");
		result.put("billCode", head.getVbillcode());
		//addCustomer-- 添加；updCustomer--更新；
		JSONObject res = new JSONObject();

		
		
		Map<String,String>hmap=new HashMap<String,String>(){{
			put("pk_org","cgzz");
			put("ctrantypeid","ddlx");
//			put("vbillcode","ddbh");
			put("dbilldate","ddrq");
//			todo 5	联系人	lxr
//			6	电话	dh
//			put("lxr","lxr");
//			put("dh","dh");
			put("pk_supplier","gys");
			put("vdef2","dj");
			put("forderstatus","zt");
			put("pk_invcsupllier","kpgys");
			put("pk_payterm","fkxy");
			put("vdef3","xynr");
			put("vbillcode","erpcode");
			put("ntotalorigmny","jshj");
			put("pk_balatype","jsfs");
			put("corigcurrencyid","bz");
			put("cemployeeid","cgy");
			put("pk_dept","cgbm");
			put("vdef1","cq");
			put("pk_dept","cgbm");
			put("vmemo ","bz1");
			
		}
	
		};
		JSONArray hArray =new JSONArray();
		for(String key:hmap.keySet()){
			JSONObject hvo = new JSONObject();
			if("ddrq".equalsIgnoreCase(hmap.get(key))) {
				String timeFormat2 = head.getAttributeValue(key).toString();
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
		hvo.put("fieldName", "ddbh");
		hvo.put("fieldValue",head.getAttributeValue("vbillcode")==null?"":head.getAttributeValue("vbillcode").toString());
		hArray.add(hvo);
		
//		JSONArray childArray =new JSONArray();
//		JSONObject hvo1 = new JSONObject();
//		hvo.put("cgzz", head.getPk_org());
//		hvo.put("ddlx", head.getCtrantypeid());
//		hvo.put("ddbh", head.getVbillcode());
//		hvo.put("ddrq", head.getDbilldate().toString());
//		hvo.put("gys", head.getPk_supplier());
//		hvo.put("dj", head.getVdef2());
//		hvo.put("zt", head.getForderstatus());
//		hvo.put("kpgys", head.getPk_invcsupllier());
//		hvo.put("fkxy", head.getVdef3());
//		
//		hvo.put("jsfs", head.getPk_balatype());
//		hvo.put("bz", head.getCorigcurrencyid());
//		hvo.put("cgy", head.getCemployeeid());
//		hvo.put("cgbm", head.getPk_dept());
//		hvo.put("cq", head.getVdef1());
//		hvo.put("cgbm", head.getPk_dept());
////		hvo.put("jshj", head.gett);
//		hvo.put("bz1", head.getVmemo());
//		res.put("hvo", hvo);
		
		Map<String,String>bmap=new HashMap<String,String>(){{
			put("pk_material","wlmc");
			put("pk_srcmaterial","wlbm");
			put("vbdef2","xh");
			put("vbdef3","gg");
			put("vbdef18","sccs");
			put("cunitid","jldw");
			put("nnum","sl");
			put("norigprice","wsdj");
			put("norigtaxprice","hsdj");
			put("norigmny","wsje");
			put("norigtaxmny","hsje");
			put("ctaxcodeid","sm");
			put("ntaxrate","sl1");
			put("ntax","se");
			put("vbmemo","bz");
			put("cprojectid","xm");
			put("vbdef1","fybm");
			put("vbdef19","wxgdh");
			put("crowno","hh");
			put("crowno","hh1");
			put("pk_reqstoorg","xqkczz");
			put("pk_arrvstoorg","shkczz");
			put("pk_recvstordoc","shck");
		}
	
		};
		JSONArray childArray =new JSONArray();
		JSONArray childArray3 =new JSONArray();
		for(int i=0;i<bodys.length;i++){
			JSONObject bvo1 = new JSONObject();
			JSONObject bvo2 = new JSONObject();
			bvo1.put("recordOrder", i);
			bvo2.put("recordOrder", i);
			JSONArray barr =new JSONArray();
			JSONArray barr1 =new JSONArray();
			for(String key:bmap.keySet()){
				JSONObject bvo = new JSONObject();
				JSONObject bvo3 = new JSONObject();
				if("xqkczz".equalsIgnoreCase(bmap.get(key))||"shkczz".equalsIgnoreCase(bmap.get(key)) ||"shck".equalsIgnoreCase(bmap.get(key))|| "hh1".equalsIgnoreCase(bmap.get(key)) ) {
					bvo3.put("fieldName", bmap.get(key));
					bvo3.put("fieldValue",bodys[i].getAttributeValue(key)==null?"": bodys[i].getAttributeValue(key).toString());
					barr1.add(bvo3);
				}else{
					bvo.put("fieldName", bmap.get(key));
					bvo.put("fieldValue",bodys[i].getAttributeValue(key)==null?"": bodys[i].getAttributeValue(key).toString());
					barr.add(bvo);
				}
				
			}
			bvo1.put("workflowRequestTableFields", barr);
			bvo2.put("workflowRequestTableFields", barr1);
			childArray3.add(bvo2);
			childArray.add(bvo1);
		}
		
		
		JSONArray childArray2 =new JSONArray();
		JSONObject bvo2 = new JSONObject();
		JSONObject bvo4 = new JSONObject();
		bvo2.put("tableDBName", "formtable_main_172_dt1");
		bvo2.put("workflowRequestTableRecords", childArray);
		bvo4.put("tableDBName", "formtable_main_172_dt2");
		bvo4.put("workflowRequestTableRecords", childArray3);
		childArray2.add(bvo2);
		childArray2.add(bvo4);
		res.put("mainData", hArray);
		
		res.put("detailData", childArray2);
		res.put("workflowId", "161");
		res.put("formid", "172");
		res.put("requestName", "采购订单");
//		"": "采购付款计划",
//		res.put("bvo", childArray);

		
		String tb = " ";
		if(tb.equalsIgnoreCase("Y")) {
			res.put("opt", "updCustomer");
		}else {
			res.put("opt", "addCustomer");
		}
		result.put("data", res);
		String reJson = JSON.toJSONString(result);
		return reJson;
	}
	
	
	
	private void fillData(OrderVO order) throws DAOException {

		//
		String[] formulas_H46 = {
				// 最新的库存组织
				// 部门
				"pk_dept->getColValue2(org_dept,name,pk_dept,pk_dept,pk_org,pk_org)",
				
//				等级
				
				"vdef2->getColValue2(bd_supstock,   supgrade  , pk_supplier,pk_supplier,pk_org ,pk_org)",
				"vdef2->getColValue(bd_supplier_grade,   suppliergrade  , pk_grade_info ,vdef2)",
				
				"pk_org->getColValue2(org_stockorg,  code , pk_stockorg,pk_org,islastversion ,\"Y\")",

				"pk_supplier->getColValue(bd_supplier,name,pk_supplier,pk_supplier)",
//				业务员
				"cemployeeid->getColValue(bd_psndoc,name,pk_psndoc,cemployeeid)",
				
				"ctrantypeid->getColValue2(bd_billtype,billtypename , pk_billtypeid,ctrantypeid,pk_group,pk_group)",
				
				// 开票供应商
				"pk_invcsupllier->getColValue(bd_supplier,name,pk_supplier,pk_invcsupllier)",
				
				
//				协议内容
				"vdef3->getColValue(med_payment_148,vcontent_148,pk_payment,pk_payterm)",

				// 付款协议
				"pk_payterm->getColValue(bd_payment,name,pk_payment,pk_payterm)",
				// 结算方式
				"pk_balatype->getColValue(bd_balatype,name ,pk_balatype,pk_balatype)",

				// 币种
				"corigcurrencyid->getColValue(bd_currtype,name,pk_currtype,corigcurrencyid)",
				
			
			
				// 厂区
			

				"vdef1 ->getColValue(bd_defdoc,name , pk_defdoc ,vdef1 )"

				
				// / "vtrantypecode->\"46-01\"",
				// 业务类型 TODO
				

		};

		SuperVOUtil.execFormulaWithVOs(new OrderHeaderVO[] { order.getHVO() },
				formulas_H46, null);
		
		
		

		
		// 填充表体付款协议
		String[] formulas_B46 = {
				"pk_srcmaterial->getColValue(bd_material,code,pk_material,pk_material)",
				// 物料编码
				"pk_material->getColValue(bd_material,name,pk_material,pk_material)",
				
//				规格
				"vbdef3->getColValue(bd_material,materialspec ,pk_material,pk_material)",
//				型号
				"vbdef2->getColValue(bd_material_v,materialtype ,pk_material,pk_material)",

				// 生产厂商
				"cproductorid ->getColValue(bd_defdoc,name,pk_defdoc,cproductorid)",
				
				// 单位
				"cunitid->getColValue(bd_measdoc, code,pk_measdoc,cunitid)",
				
				
//				/ 税码
				"ctaxcodeid->getColValue(bd_taxcode,code ,pk_taxcode,ctaxcodeid)",
				
				
//				/ 项目
				"cprojectid->getColValue(bd_project,project_name  ,pk_project ,cprojectid)",
				
				// feiyongbumen
//				"vbdef1 ->getColValue(bd_defdoc,name,pk_defdoc,vbdef1)",
				
				"vbdef1->getColValue2(org_dept,name,pk_dept,vbdef1,pk_org,pk_org)",
				// 需求库存组织
				"pk_reqstoorg->getColValue2(org_purchaseorg,name,pk_purchaseorg ,pk_reqstoorg,islastversion ,\"Y\")",
				
				// 收货库存组织
				"pk_arrvstoorg->getColValue2(org_purchaseorg,name,pk_purchaseorg ,pk_arrvstoorg,islastversion ,\"Y\")",
				
				
				// 收货c仓库
				"pk_recvstordoc->getColValue(bd_stordoc,name  ,pk_stordoc ,pk_recvstordoc)"
			
				
				

		// "pk_arrvstoorg->getColValue(org_stockorg,pk_stockorg,code,pk_arrvstoorg)",

		};

		SuperVOUtil.execFormulaWithVOs(order.getBVO(), formulas_B46, null);

	

	}


}

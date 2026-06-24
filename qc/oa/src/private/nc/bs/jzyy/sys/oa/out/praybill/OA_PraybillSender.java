package nc.bs.jzyy.sys.oa.out.praybill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.ArrayUtil;
import org.apache.commons.lang.ArrayUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.jzyy.sys.FileUtil;
import nc.bs.jzyy.sys.FileVO;
import nc.bs.jzyy.sys.oa.out.AbstractSender4OA;
import nc.bs.jzyy.sys.oa.out.ApiProxy;
import nc.bs.jzyy.sys.oa.out.SenderQuerys;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillItemVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;

public class OA_PraybillSender extends AbstractSender4OA {

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
		String url = getSysOA();
		result = ApiProxy.httpPost(url, sendJson);
		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getSendJson() throws Exception {
		PraybillVO bill = (PraybillVO) getParam();
		PraybillHeaderVO head = bill.getHVO();
		PraybillItemVO[] bodys = bill.getBVO();
		SenderQuerys query = new SenderQuerys();
		// 报文
		// 表头

		List<Map<String, Object>> mainData = new ArrayList<Map<String, Object>>();
	
		
//		String[] headkey = new String[] { "sqr", "sqsj", "jhy", "zsl", "zsl2",
//				"erpcode", "qglx", "qglxzxx","qglxmc","jhbm", "orgcode","sm","ygje","ck","sqgs","Field1","Field2","Field3"};
		
		
//		String[] headkey = PrayBillFoOA_propertyUtil.OA_Itiem_H;
		Map<String, List<Object>> headmap = PrayBillFoOA_propertyUtil.headMap;
		
		for (String key : PrayBillFoOA_propertyUtil.OA_Itiem_H) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("fieldName", key);
			Object value = null;
//			List<Object> list2 = headmap.get(key);
			List<Object> list = PrayBillFoOA_propertyUtil.headMap.get(key);
			if(null == list) continue;
			int indexOf_h = ArrayUtils.indexOf(PrayBillFoOA_propertyUtil.ERP_Itiem_H,(String)list.get(1));
			int indexOf_b = ArrayUtils.indexOf(PrayBillFoOA_propertyUtil.ERP_Itiem_B,(String)list.get(1));
			if(indexOf_h > -1){
				value = PrayBillFoOA_propertyUtil.getMapValue(list, head);
				 if(value != null){
					 value.toString().trim();//去空格
				 }
			} else 
			if(indexOf_b > -1){
				value = PrayBillFoOA_propertyUtil.getMapValue(list, bodys[0]);
				 if(value != null){
					 value.toString().trim();//去空格
				 }
			}
			if(-1 <ArrayUtils.indexOf(PrayBillFoOA_propertyUtil.isNum, key) ){
				value = getNullAsZero((UFDouble) value);
			}else {
				
				value = getNullAsEmpty(value);
			}
			detail.put("fieldValue", value);
			mainData.add(detail);
		}
		// 附件
//		FileVO[] ss = FileUtil.queryFiles(head.getPk_praybill(), true);
//		if (ss.length > 0) {
//			Map detail = new HashMap();
//			List array = new ArrayList();
//			for (FileVO vo : ss) {
//				Map files = new HashMap();
//				String base64 = vo.getBase64Str();
//				String name = vo.getName();
//				files.put("fileName", name);
//				files.put("filePath", "base64:" + base64);
//				array.add(files);
//			}
//			detail.put("fieldName", "xgfj");
//			detail.put("fieldValue", array);
//			mainData.add(detail);
//		}
		// -------------------------------------------------------------------------------------------------------------------------------------------

		// 表体
		List<Map<String, Object>> detailData = new ArrayList<Map<String, Object>>();
		Map<String, Object> first = new HashMap<String, Object>();
		List<Map<String, Object>> workflowRequestTableRecords = new ArrayList<Map<String, Object>>();
		for (PraybillItemVO body : bodys) {
			// 表体第二层
			Map<String, Object> second = new HashMap<String, Object>();
			List<Map<String, Object>> workflowRequestTableRecords2 = new ArrayList<Map<String, Object>>();
//			String[] bodykey = new String[] { "wlmc",
//					"wlbm", "xh", "gg","jldw", "sl", "sl2","bz", "jhdhrq","pk_praybill_b","Field16","Field1","Field2","Field3"};
			String[] bodykey =PrayBillFoOA_propertyUtil.OA_Itiem_B;
			for (String key : bodykey) {
				Map<String, Object> secondDetail = new HashMap<String, Object>();
				secondDetail.put("fieldName", key);
				Object value = null;
				List<Object> bodyItiemList = PrayBillFoOA_propertyUtil.bodyMap.get(key);
				if (null == bodyItiemList) continue ; 
				if( -1 < ArrayUtils.indexOf(PrayBillFoOA_propertyUtil.ERP_Itiem_B,bodyItiemList.get(1))){
					 value = PrayBillFoOA_propertyUtil.getMapValue(PrayBillFoOA_propertyUtil.bodyMap.get(key), body);
					 if(value != null){
						 value.toString().trim();//去空格
					 }
				}
				if(-1 <ArrayUtils.indexOf(PrayBillFoOA_propertyUtil.isNum, key) ){
					value = getNullAsZero((UFDouble) value);
				}else {
					
					value = getNullAsEmpty(value);
				}
				secondDetail.put("fieldValue", value);
				workflowRequestTableRecords2.add(secondDetail);
			}

			// 组装单据
			second.put("recordOrder", "0");
			second.put("workflowRequestTableFields",
					workflowRequestTableRecords2);
			workflowRequestTableRecords.add(second);

		}
		first.put("tableDBName", "formtable_main_279_dt1");
		first.put("workflowRequestTableRecords", workflowRequestTableRecords);
		detailData.add(first);

		// 第二层
		Map<String, Object> request = new HashMap<String, Object>();
		request.put("requestName", "请购单");
		request.put("detailData", detailData);
		request.put("mainData", mainData);

		// 第三层
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("srccode",getSrccode());
		data.put("billCode", head.getVbillcode());
		data.put("billmaker",
				getNullAsEmpty(query.getBillmakercode(head.getBillmaker())));
		data.put("srcappkey",getSrcappkey());
		data.put("targetcode",getTargetcode());
		data.put("targetrule", "praybillform");
		data.put("data", request);

		// 组装
		String reJson = JSON.toJSONString(data);
		return reJson;
	}
	public String getMeasdocByPk(String pk_measdoc) throws DAOException {
		BaseDAO dao = new BaseDAO();
		String sql = "select code from bd_measdoc  where pk_measdoc = '" + pk_measdoc + "';";
		String pk_org = (String) dao.executeQuery(sql, new ColumnProcessor());
		return pk_org;
	}
}

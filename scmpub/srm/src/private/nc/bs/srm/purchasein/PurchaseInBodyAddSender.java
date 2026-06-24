package nc.bs.srm.purchasein;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.srm.pub.AbstractSender4Mdm;
import nc.bs.srm.pub.ApiProxy;
import nc.bs.srm.pub.EsbUtils;
import nc.bs.srm.pub.SenderQuerys;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.m45.entity.PurchaseInBodyVO;
import nc.vo.ic.m45.entity.PurchaseInHeadVO;
import nc.vo.ic.m45.entity.PurchaseInVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import nc.itf.jzyy.sys.srm.IProcessService;

/**
 * 采购入库报文
 * 
 * @author sea
 *
 */
public class PurchaseInBodyAddSender extends AbstractSender4Mdm {
	private static String URL_CODE = "purchasein";
	
//	IProcessService proc = NCLocator.getInstance().lookup(IProcessService.class);
	
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
		return ApiProxy.httpPost(EsbUtils.getPostUrl(PurchaseInBodyAddSender.URL_CODE), sendJson);
	}

	public String getSendJson() throws DAOException, ParseException, UifException {
		PurchaseInVO bill = (PurchaseInVO) getParam();
		PurchaseInHeadVO head = (PurchaseInHeadVO) bill.getParent();
		PurchaseInBodyVO[] bodyarry = (PurchaseInBodyVO[]) bill.getChildrenVO();
	

		SenderQuerys sqy = new SenderQuerys();
		JSONObject supJson = new JSONObject();
		supJson.put("srccode", "NC");
		supJson.put("srcappkey", "4f128e5820ce494fb38fdaf8fbb92fb3");
		supJson.put("targetcode", "SRM");
		supJson.put("targetrule", "SINV_RCV_TRX_IMP");
		supJson.put("vbillcode", "DB-20230919-007");
		JSONObject data = new JSONObject();
		JSONObject headData = new JSONObject();
		String applicationCode = EsbUtils.getPostUrl("srmcode");
		headData.put("applicationCode", applicationCode);
		headData.put("applicationGroupCode", "PUBLIC_CLOUD");
		headData.put("batchCount", "1");
		headData.put("batchNum", System.currentTimeMillis());
		headData.put("externalSystemCode", "JIUZHOU_7YTVO0DJ");
		headData.put("interfaceCode", "SINV_RCV_TRX_IMP");
		headData.put("userName", "");


		// 表头报文
		JSONArray bjson = new JSONArray();
		JSONObject codeJson = new JSONObject();
		codeJson.put("esTrxNum", head.getVbillcode());// 单据号
		 UFDate dbilldate = head.getDbilldate();
		 String substring = dbilldate.toString().substring(0, 4);
		codeJson.put("trxYear", substring);//入库单日期前四位
		String pk_org = head.getPk_org();
		String stockorgCode = sqy.getStockorgCode(pk_org);
		codeJson.put("esOuCode", stockorgCode);//库存组织

		codeJson.put("trxDate", dbilldate.toLocalString());//入库单日期
		String purchaseorg = sqy.getPurchaseorg(head.getCpurorgoid());
		codeJson.put("esPurchaseOrgCode", purchaseorg);//采购组织组织编码
		codeJson.put("esSupplierNum", sqy.getPksuppliercode(head.getCvendorid()));//供应商编码
		String Creationtime = head.getCreationtime().toLocalString();
		codeJson.put("erpCreationDate",Creationtime);//创建时间
		codeJson.put("erpLastUpdateDate", head.getModifiedtime()==null?head.getCreationtime().toLocalString():head.getModifiedtime().toLocalString()  );//组后更新时间
		codeJson.put("remark", head.getVnote());//备注
//		codeJson.put("trxDate", head.getTs());//事务时间sourceCode
		codeJson.put("sourceCode", "ncc");//数据来源
		//表体退货标识
		UFBoolean flag = head.getFreplenishflag();
		boolean booleanValue = flag.booleanValue();
		String esTrxTypeCode = "RECEIVE_DELIVER";
		if(booleanValue){
			esTrxTypeCode = "RECEIVE_DELIVER";
		}
		// 表体报文
		JSONArray bodys = new JSONArray();
		for (PurchaseInBodyVO item : bodyarry) {
			JSONObject bodyJson = new JSONObject();
			// 采购入库
			
			bodyJson.put("esTrxLineNum", item.getCrowno());//行号
			
			
			bodyJson.put("trxDate", dbilldate.toLocalString());//入库单日期
			bodyJson.put("trxYear", substring);//入库单日期前四位
			/**
			 * RECEIVE_DELIVER入库标识
			 * RETURN_DIRECT 退货标识
			 */
			
			bodyJson.put("esTrxTypeCode", esTrxTypeCode);//入库/退货标志？后面要改 
			double abs = Math.abs(item.getNnum().doubleValue());
			bodyJson.put("quantity", abs);//主数量
			double nassistnum =  Math.abs(item.getNassistnum().doubleValue());
			bodyJson.put("secondaryQuantity", nassistnum);//辅数量
			bodyJson.put("stockType","OWN_STOCK");//库存类型
			bodyJson.put("currencyCode", sqy.getCurrtypeCode(item.getCsettlecurrencyid()));//币种
			bodyJson.put("netPrice", item.getNorigprice().doubleValue());//不含税单价
			bodyJson.put("taxIncludedPrice", item.getNorigtaxprice().doubleValue());//含税单价
			bodyJson.put("taxIncludedAmount", item.getNorigtaxmny().doubleValue());//含税金额 取得含税净价
			double abs2 = Math.abs(item.getNmny().doubleValue());
			bodyJson.put("netAmount", abs2);//无税金额
			String taxcodeByPk = sqy.getTaxcodeByPk(item.getCtaxcodeid());
			bodyJson.put("esTaxCode", taxcodeByPk);//税率
			Map<String, Object> material = sqy.getMaterial(item.getCmaterialoid());
			
			bodyJson.put("esItemCode", material.get("code"));//物料编码
			bodyJson.put("esItemName", material.get("name"));//名称
			bodyJson.put("esCategoryCode", material.get("materialtype"));//型号
			bodyJson.put("esUomCode",sqy.getMeasdocByPk(item.getCunitid()) );//单位
			bodyJson.put("esSecondaryUomCode", sqy.getMeasdocByPk(item.getCastunitid()));//辅单位
			bodyJson.put("esOuCode", stockorgCode);//库存组织
			bodyJson.put("esPurOrganizationCode", purchaseorg);//采购组织

//			bodyJson.put("esInventoryCode", item.getCrowno());//外部系统收货库房代码 不知道什么字段
//			bodyJson.put("esLocatorCode", item.getCrowno());//外部系统收货库位代码
			bodyJson.put("esSupplierNum", sqy.getPksuppliercode(head.getCvendorid()));//供应商
			String orderPk = getOrderPk(item.getCgeneralbid());
			String arriveorderBid = getArriveorderBid(item.getCsourcebillbid());
			//是否退货
			if(booleanValue){
				orderPk = item.getVsourcebillcode();
				arriveorderBid = item.getCsourcebillbid();
			}
			String orderCrowno = getCrowno(arriveorderBid);

			bodyJson.put("srmPoNum", orderPk);//来源采购订单号

			bodyJson.put("srmPoLineNum", orderCrowno);//来源采购订单行号pk
			String stockOrg = getStockOrg(orderPk);
			bodyJson.put("esInvOrganizationCode", stockOrg);//收货组织 
			String csourcebillbid = item.getCsourcebillbid();
			if(booleanValue){
				Map purchasein = getPurchasein(csourcebillbid).get(0);
				String crowno = (String)purchasein.get("crowno");
				String cgeneralhid = (String)purchasein.get("cgeneralhid");
				Map purchaseinHead = getPurchaseinHead(cgeneralhid);
				String vbillcode = (String)purchaseinHead.get("vbillcode");
				String dbilldate1 = (String)purchaseinHead.get("dbilldate");
				
				String substring2 = dbilldate1.substring(0, 4);
				bodyJson.put("erpParentTrxNum",vbillcode);//上游单据号?（根据入库单退货时需有）
				bodyJson.put("erpParentTrxLineNum",crowno);//上游单据行号？（根据入库单退货时需有）
				bodyJson.put("erpParentTrxYear",substring2);//上游单据年度（根据入库单退货时需有）
				
			}
	
			bodyJson.put("erpCreationDate", dbilldate.toLocalString());//erp创建日期
			bodyJson.put("erpLastUpdateDate", head.getModifiedtime()==null?head.getCreationtime().toLocalString():head.getModifiedtime().toLocalString());//erp最后更新日期
			bodyJson.put("remark", item.getVnotebody());//备注说明
			codeJson.put("esOuCode", stockorgCode);//库存组织

			bodys.add(bodyJson);
		}

		codeJson.put("rcvTrxLineList", bodys);
		
		bjson.add(codeJson);
		/* //组装 */
		data.put("header", headData);
		data.put("body", bjson);
		supJson.put("data", data);
		String reJson = supJson.toString();
		return reJson;
	}
	//收货库存组织
	private String getStockOrg(String str) throws DAOException{
		String sql = "select   pk_arrvstoorg  from po_order_b where pk_order = (select pk_order from po_order where vbillcode = '"+str+"' and dr = 0 and bislatest = 'Y')";
		String executeQuery = (String)getDao().executeQuery(sql, new ColumnProcessor());
		return executeQuery;
	}
	//查采购订单
	private String getOrderPk(String str) throws DAOException{
		String sql = "select vsourcecode  from po_arriveorder_b where pk_arriveorder_b =  (select csourcebillbid   from ic_purchasein_b where cgeneralbid = '"+str+"')";
		String vsourcecode = (String)getDao().executeQuery(sql, new ColumnProcessor());
		return vsourcecode;
	}

	
//	退货查采购订单行号
	private String getCrowno(String str) throws DAOException{
		String sql = "select vbdef2  from po_order_b where pk_order_b = '"+str+"'";
		String vsourcecode = (String)getDao().executeQuery(sql, new ColumnProcessor());
		return vsourcecode;
	}
//	查来源到货单表体主键
	private String getArriveorderBid(String str) throws DAOException{
		String sql = "select csourcebid   from po_arriveorder_b where   pk_arriveorder_b  = '"+str+"'";
		String vsourcecode = (String)getDao().executeQuery(sql, new ColumnProcessor());
		return vsourcecode;
	}
	
	
	//查对应入库单行号表体主键 str表体主键
	private List<Map<String,Object>> getPurchasein(String str) throws DAOException{
		String sql = "select crowno,cgeneralhid   from ic_purchasein_b where csourcebillbid  = (select   pk_arriveorder_b  from po_arriveorder_b where  csourcebid  = '"+str+"')";
		List<Map<String,Object>> vsourcecode = (List<Map<String,Object>>)getDao().executeQuery(sql, new   MapListProcessor());
		return vsourcecode;
	}
	//查对应入库单单据号 单据日期
		private Map getPurchaseinHead(String str) throws DAOException{
			String sql = "select   vbillcode,dbilldate    from ic_purchasein_h where  cgeneralhid   = '"+str+"'";
			Map vsourcecode = (Map)getDao().executeQuery(sql, new MapProcessor());
			return vsourcecode;
		}
}

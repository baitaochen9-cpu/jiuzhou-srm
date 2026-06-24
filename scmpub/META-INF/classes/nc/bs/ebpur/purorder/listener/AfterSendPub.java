/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package nc.bs.ebpur.purorder.listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.scmpub.yunfeng.utils.LogUtils;
import nc.bs.srm.pub.EsbUtils;
import nc.bs.srm.pub.MakeNcLog;
import nc.bs.srm.pub.SenderQuerys;
import nc.cmp.tools.StringUtil;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.aum.sale.SaleBodyVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.material.MaterialVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.pu.m21.entity.OrderHeaderVO;
import nc.vo.pu.m21.entity.OrderItemVO;
import nc.vo.pu.m21.entity.OrderVO;
import nc.vo.pub.BusinessException;
import nc.vo.so.m30.entity.SaleOrderBVO;
import nc.vo.so.m30.entity.SaleOrderHVO;
import nc.vo.so.m30.entity.SaleOrderVO;
import nc.vo.so.m4331.entity.DeliveryBVO;
import nc.vo.so.m4331.entity.DeliveryHVO;
import nc.vo.so.m4331.entity.DeliveryVO;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class AfterSendPub {
	SenderQuerys query = null;
	BaseDAO dao = null;
	LogUtils logs = new LogUtils();

	ColumnProcessor processor = null;
	{
		try {
			getHead();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public SenderQuerys getQuery() {
		if (query == null) {
			query = new SenderQuerys();
		}
		return query;
	}

	public ColumnProcessor getProcessor() {
		if (processor == null) {
			processor = new ColumnProcessor();
		}
		return processor;
	}

	public String OwnerOrgId;
	public String OwnerTeamId;
	public String gwMiddleCode;

	/**
	 * 采购推送
	 *
	 * @throws BusinessException
	 */
	protected Map<String, Object> poSend(OrderVO ordObj, String eventType)
			throws BusinessException {
		OrderHeaderVO hvo = (OrderHeaderVO) ordObj.getParentVO();
		OrderItemVO[] bvos = (OrderItemVO[]) (OrderItemVO[]) ordObj
				.getChildrenVO();
		HashMap<String, Object> map = new HashMap<>();
		map.put("OwnerOrgId", OwnerOrgId);// 归属企业
		map.put("OwnerTeamId", OwnerTeamId);// 归属团队
		map.put("AccountNo", hvo.getVbillcode());// 订单号
		String OptionFlag = "C";
		if ("1022".equals(eventType)) {
			OptionFlag = "D";
		}
		map.put("OptionFlag", OptionFlag);// C-创建 D-删除
		map.put("IEFlag", "I");// 进出口标志“I”：进口，“E”：出口
		if (StringUtil.isNotEmpty(hvo.getPk_recvcustomer())) {
			CustomerVO cust = getCust(hvo.getPk_recvcustomer());
			map.put("OverseasConsignorEname", cust.getEname());// 客户英文名称
			map.put("OverseasConsigneeEname", cust.getName());// 客户档案名称
		}
		map.put("TradeAreaCode", getCountry(hvo.getPk_recvcustomer()));// 客户国家档案中文名称
		map.put("SpecialRequirements", hvo.getVmemo());// 备注
		String orgName = getQuery().getPkorgName(hvo.getPk_org());
		map.put("OwnerName", orgName);// 组织名称
		SupplierVO supp = getSupp(hvo.getPk_supplier());
		map.put("TraderAreaAliasName", "");// 客户或者供应商的国家的英文全称 TraderAddress
		String addressByPk = getQuery().getAddressByPk(supp.getCorpaddress());
		map.put("TraderAddress", addressByPk);// 客户或者供应商的地址
		Map<String, Object> suppLink = getSuppLink(supp.getPk_supplier());
		if (suppLink != null) {
			map.put("TraderContactName", suppLink.get("name"));// 客户或者供应商的联系人名称
			map.put("TraderTelphone", suppLink.get("phone"));// 客户或者供应商的联系人电话
			map.put("TradeCoScc", suppLink.get("phone"));// TODO当前组织纳税人识别号

		}
		map.put("EoriNo", supp.getTaxpayerid());// 客户或者供应商的纳税识别号
		map.put("CreateUser", hvo.getBillmaker());// 单据制单人名称
		map.put("OrderNo", ordObj.getPrimaryKey());// 表头主键
		map.put("TradeName", orgName);// TODO组织英文名称

		map.put("TradeAddress", getOrgAddr(hvo.getPk_org()));// 组织的详细地址
		int custFlag = 0;
		if (supp.getIscustomer() == null ? false : supp.getIscustomer()
				.booleanValue()) {
			custFlag = 1;
		}
		map.put("PromiseItmes1", custFlag);// 供应商或者客户的是否关联方，是=1，否=0
		map.put("PromiseItmes2", 0);// 客户或者供应商的纳税识别号
//		map.put("OwnerCode", )//海关编码后加

		// 表体
		List<Map<String, Object>> body = new ArrayList<>();
		for (OrderItemVO bvo : bvos) {
			HashMap<String, Object> bMap = new HashMap<>();

			bMap.put("AccountDId", bvo.getPk_order_b());// 表体主键
			bMap.put("SeqNo", bvo.getCrowno());// 行号
			MaterialVO mater = getMater(bvo.getPk_material());
			bMap.put("Sku", mater.getCode());// 物料编码
			bMap.put("GoodName", mater.getName());// 物料名称
			bMap.put("GoodEnName", mater.getEname());// 物料英文名称
			bMap.put("TradeQty", bvo.getNnum().doubleValue());// 主数量
			bMap.put("TradeUnit", getUnitCode(bvo.getCastunitid()));// 计量单位编码
			bMap.put("Price", bvo.getNqtorigtaxprice().doubleValue());// 含税单价
			bMap.put("TotalAmt", bvo.getNorigtaxmny().doubleValue());// 价税合计
//			map.put("TradeCode", orgName);// TODO海关编码
			bMap.put("Curr",
					getQuery().getCurrtypeCode(bvo.getCorigcurrencyid()));// 币种编码
			bMap.put("ContractNo", getCtCode(bvo.getCcontractid()));// 合同号
			bMap.put("PurchaseNo", hvo.getVbillcode());// 采购订单号
			bMap.put("PurchaseSeqNo", bvo.getCrowno());// 行号
			Map<String, Object> purchaseorgs = getPurchaseorgs(bvo.getPk_org());
			bMap.put("SalesCompany", purchaseorgs.get("name"));// 组织名称
			bMap.put("MergeFlag", 0);// 是否可归并 0:非归并,1:归并
			bMap.put("ProduceDate", getBatch(bvo.getPk_batchcode()));// 批次号的生产日期
			bMap.put("BatchNo", bvo.getVbatchcode());// 批次号
			body.add(bMap);
		}
		map.put("AccountList", body);// 单据制单人名称
		Map<String, Object> accountImportHead = new HashMap<>();

		accountImportHead.put("AccountImportHead", map);

		// 最外层中间平台数据
		Map<String, Object> outside = new HashMap<>();
		outside.put("srccode", "NC");
		outside.put("srcappkey", gwMiddleCode);
		outside.put("targetcode", "GW");
		outside.put("targetrule", "gwInOut");
		outside.put("vbillcode", "");
		outside.put("data", accountImportHead);
		return outside;
	}

	/**
	 * 发货推送
	 *
	 * @throws BusinessException
	 */
	protected Map<String, Object> saleSend(SaleOrderVO aggVo,
										   String eventType) throws BusinessException {
		logs.sendTolog("----发货单报文拼接开始----");
		Logger log = MakeNcLog.setParam("GWLog", "log");
		log.info("发货单报文拼接开始-");
		SaleOrderHVO hvo = aggVo.getParentVO();
		SaleOrderBVO[] bvos = aggVo.getChildrenVO();
		HashMap<String, Object> map = new HashMap<>();
		map.put("OwnerOrgId", OwnerOrgId);// 归属企业 srcappkey
		map.put("OwnerTeamId", OwnerTeamId);// 归属团队
		map.put("AccountNo", hvo.getVbillcode());// 订单号
		String OptionFlag = "C";
		if ("1022".equals(eventType)) {
			OptionFlag = "D";
		}
		map.put("OptionFlag", OptionFlag);// C-创建 D-删除
		map.put("IEFlag", "E");// 进出口标志“I”：进口，“E”：出口
		if (StringUtil.isNotEmpty(bvos[0].getCreceivecustid())) {
			CustomerVO cust = getCust(bvos[0].getCreceivecustid());
			map.put("OverseasConsignorEname", cust.getEname());// 客户英文名称
			map.put("OverseasConsigneeEname", cust.getName());// 客户档案名称
		}
		map.put("TradeAreaCode", getCountry(bvos[0].getCreceivecustid()));// 客户国家档案中文名称
		map.put("SpecialRequirements", hvo.getVnote());// 备注
		String orgName = getQuery().getPkorgName(hvo.getPk_org());
		map.put("OwnerName", orgName);// 组织名称
//		map.put("OwnerCode", )//海关编码后加
		SupplierVO supp = getSupp(bvos[0].getCvendorid());
		if (supp != null) {
			map.put("TraderAreaAliasName", "");// 客户或者供应商的国家的英文全称 TraderAddress
			String addressByPk = getQuery().getAddressByPk(
					supp.getCorpaddress());
			map.put("TraderAddress", addressByPk);// 客户或者供应商的地址
			Map<String, Object> suppLink = getSuppLink(supp.getPk_supplier());
			map.put("EoriNo", supp.getTaxpayerid());// 客户或者供应商的纳税识别号
			if (suppLink != null) {
				map.put("TraderContactName", suppLink.get("name"));// 客户或者供应商的联系人名称
				map.put("TraderTelphone", suppLink.get("phone"));// 客户或者供应商的联系人电话
				map.put("TradeCoScc", suppLink.get("phone"));// TODO当前组织纳税人识别号
			}
			int custFlag = 0;
			if (supp.getIscustomer() == null ? false : supp.getIscustomer()
					.booleanValue()) {
				custFlag = 1;
			}
			map.put("PromiseItmes1", custFlag);// 供应商或者客户的是否关联方，是=1，否=0
		}
		map.put("CreateUser", hvo.getBillmaker());// 单据制单人名称
		map.put("OrderNo", hvo.getPrimaryKey());// 表头主键
		map.put("TradeName", orgName);// TODO组织英文名称
//		map.put("TradeCode", orgName);// TODO海关编码
		map.put("TradeAddress", getOrgAddr(hvo.getPk_org()));// 组织的详细地址
		map.put("PromiseItmes2", 0);// 客户或者供应商的纳税识别号
		// 表头合同号，多个合同号逗号分割
		map.put("ContractNo", getSaleCtCode(hvo.getVbillcode()));
		System.out.println("表头合同号是:"+getSaleCtCode(hvo.getVbillcode()));
		log.info("表头合同号是-"+getSaleCtCode(hvo.getPrimaryKey()));
		// 表体
		List<Map<String, Object>> body = new ArrayList<>();
		for (SaleOrderBVO bvo : bvos) {
			HashMap<String, Object> bMap = new HashMap<>();

			bMap.put("AccountDId", bvo.getCsaleorderbid ());// 表体主键
			bMap.put("SeqNo", bvo.getCrowno());// 行号
			MaterialVO mater = getMater(bvo.getCmaterialvid());
			if (mater == null) {
				throw new BusinessException("未找到对应的物料信息");
			}
//			bMap.put("HsCode", bvo.getNnum().doubleValue());// 税号
			bMap.put("Sku", mater.getCode());// 物料编码
			bMap.put("GoodName", mater.getName());// 物料名称
			bMap.put("GoodEnName", mater.getEname());// 物料英文名称
			bMap.put("TradeQty", bvo.getNnum().doubleValue());// 主数量
			bMap.put("TradeUnit", getUnitCode(bvo.getCastunitid()));// 计量单位编码
			bMap.put("GUnit", getUnitCode(bvo.getCastunitid()));// 计量单位编码
			bMap.put("Price", bvo.getNqtorigtaxprice().doubleValue());// 含税单价
			bMap.put("TotalAmt", bvo.getNorigtaxmny().doubleValue());// 价税合计
			bMap.put("Curr",
					getQuery().getCurrtypeCode(hvo.getCorigcurrencyid ()));// 币种编码
			bMap.put("ContractNo", map.get("ContractNo")); // 使用表头合同号
			bMap.put("SalesNo", hvo.getVbillcode());// 销售订单号
			bMap.put("SalesSeqNo", bvo.getCrowno());// 行号
			Map<String, Object> purchaseorgs = getPurchaseorgs(bvo.getPk_org());
			bMap.put("SalesCompany", purchaseorgs.get("name"));// 组织名称
			bMap.put("MergeFlag", 0);// 是否可归并 0:非归并,1:归并
			bMap.put("ProduceDate", getBatch(bvo.getPk_batchcode()));// 批次号的生产日期
			bMap.put("BatchNo", bvo.getVbatchcode());// 批次号
			body.add(bMap);
		}
		map.put("AccountList", body);// 单据制单人名称
		Map<String, Object> accountImportHead = new HashMap<>();
		accountImportHead.put("AccountImportHead", map);
		// 最外层中间平台数据
		Map<String, Object> outside = new HashMap<>();
		outside.put("srccode", "NC");
		outside.put("srcappkey", gwMiddleCode);
		outside.put("targetcode", "GW");
		outside.put("targetrule", "gwInOut");
		outside.put("vbillcode", "");
		outside.put("data", accountImportHead);
		logs.sendTolog("----发货单报文拼接结束----");
		return outside;
	}
	protected void materialSendMS(MaterialVO[] materialVOs,String eventType) throws RestClientException, BusinessException{
//		MaterialVO[] materialVOs = VOCollectUtil.process((BDCommonEvent) event,MaterialVO.class);
		Map<String,Object> map = new  HashMap<>();
		//1002 新增 ，1006 删除，1修改
		String def19 = materialVOs[0].getDef19();
		if(StringUtil.isNotEmpty(def19)){
			getDao().executeUpdate("update bd_material set def19 = '' where pk_material = '"+materialVOs[0].getPk_material()+"'");
			return;
		}
		String OptionFlag = "U";
		if(StringUtil.isNotEmpty(eventType)){
			if("1006".equals(eventType)){
				OptionFlag = "D";
			}else if("1069".equals(eventType) || "1071".equals(eventType)){
				OptionFlag = "U";
			}
		}else{
			OptionFlag = "C";
		}
		map.put("OptionFlag",OptionFlag);

		List<Map<String,Object>> SkuList = new ArrayList<>();
		for (MaterialVO vo : materialVOs) {
			String nDef19 = vo.getDef19();
			if(StringUtil.isEmpty(nDef19) || nDef19 != "Y"){
				continue;
			}
			Map<String,Object> item = new  HashMap<>();
			item.put("OwnerOrgId", OwnerOrgId);//归属企业固定值
			item.put("OwnerTeamId", OwnerTeamId);//归属团队固定值
			item.put("CommonFlag", 1);//归属企业固定值

			item.put("Sku",vo.getCode() );//物料编码
			item.put("GoodEnName", vo.getEname());//英文名称
			String unitCode = getUnitCode(vo.getPk_measdoc());
			item.put("GUnitEn", unitCode);// 计量单位编码
			item.put("GUnit",unitCode);//成交单位
			item.put("GoodName", vo.getGoodsprtname());//物料名称
			item.put("DangerousType", vo.getDef4());//物料def4
			SkuList.add(item);
		}
		map.put("SkuList", SkuList);
		if(SkuList == null || SkuList.size() == 0){
			return;
		}
		//最外层中间平台数据
		Map<String,Object> outside = new HashMap<>();

		outside.put("srccode", "NC");
		outside.put("srcappkey", gwMiddleCode);
		outside.put("targetcode", "GW");
		outside.put("targetrule", "gwMaterial");
		outside.put("vbillcode", "");
		outside.put("data", map);
		restTemplate(outside);
	}

	/**
	 * 请求方法
	 *
	 * @throws RestClientException
	 * @throws BusinessException
	 */
	protected void restTemplate(Map poSend) throws RestClientException,
			BusinessException {
		JSONObject json = new JSONObject(poSend);
		logs.sendTolog("----发货单推送关务之前：" + json);
		RestTemplate rest = new RestTemplate();
		JSONObject postForObject = rest.postForObject(
				EsbUtils.getPostUrl("GWURL"), json, JSONObject.class);
		logs.sendTolog("----发货单推送关务之后："+ postForObject);
		if (!"000000".equals(postForObject.getString("Code"))) {
			JSONObject jsonObject = postForObject.getJSONObject("Body");
			if (jsonObject != null) {
				JSONArray jsonArray = jsonObject.getJSONArray("SkuList");
				if (jsonArray != null && jsonArray.size() > 0
						&& jsonArray.getJSONObject(0) != null) {
					throw new BusinessException(jsonArray.getJSONObject(0)
							.getString("ErrorMessage"));
				}
			}
			throw new BusinessException("同步关务失败:"
					+ postForObject.getString("Msg"));
		}
	}

	/**
	 *
	 * 组织参数 是否推送关务
	 *
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public boolean IsToMS(String pk_org) throws DAOException {
		String sql = "select distinct value  from  pub_sysinit where initcode='YF_ISSRM' and pk_org = '"
				+ pk_org + "'";

		String is2crm = (String) getDao().executeQuery(sql, getProcessor());
		if (is2crm == null || StringUtil.isEmpty(is2crm)) {
			return false;
		}

		if ("Y".equalsIgnoreCase(is2crm)) {
			return true;
		}

		return false;
	}

	/**
	 * 查国家档案
	 *
	 * @throws DAOException
	 */
	protected String getCountry(String pk) throws DAOException {
		String sql = "select name from  bd_countryzone where pk_country = (select pk_country  from bd_customer where pk_customer = '"
				+ pk + "');";
		return (String) getDao().executeQuery(sql, getProcessor());
	}

	/**
	 * 查客户
	 *
	 * @throws DAOException
	 */

	protected CustomerVO getCust(String pk) throws DAOException {
		ArrayList<CustomerVO> vo = (ArrayList<CustomerVO>) getDao()
				.retrieveByClause(CustomerVO.class,
						" pk_customer = '" + pk + "'");
		if (vo == null || vo.size() == 0) {
			return null;
		}
		return vo.get(0);
	}

	/**
	 * 查供应商
	 *
	 * @throws DAOException
	 */

	protected SupplierVO getSupp(String pk) throws DAOException {
		ArrayList<SupplierVO> vo = (ArrayList<SupplierVO>) getDao()
				.retrieveByClause(SupplierVO.class,
						" pk_supplier  = '" + pk + "'");
		if (vo == null || vo.size() == 0) {
			return null;
		}
		return vo.get(0);
	}

	/**
	 * 查供应商联系人
	 *
	 * @throws DAOException
	 */

	protected Map<String, Object> getSuppLink(String pk) throws DAOException {
		String sql = "  select name,phone from  bd_linkman  where pk_linkman  in (select pk_linkman from bd_suplinkman where pk_supplier = '"
				+ pk + "');";
		return (Map<String, Object>) getDao().executeQuery(sql,
				new MapProcessor());
	}

	/**
	 * 查组织地址
	 *
	 * @throws DAOException
	 */

	protected String getOrgAddr(String pk) throws DAOException {
		String sql = " select address  from org_orgs where pk_org = '" + pk
				+ "';";
		return (String) getDao().executeQuery(sql, getProcessor());
	}

	/**
	 * 查物料
	 *
	 * @throws DAOException
	 */
	protected MaterialVO getMater(String pk) throws DAOException {
		ArrayList<MaterialVO> vo = (ArrayList<MaterialVO>) getDao()
				.retrieveByClause(MaterialVO.class,
						" pk_material  = '" + pk + "'");
		if (vo == null || vo.size() == 0) {
			return null;
		}
		return vo.get(0);
	}

	/**
	 * 查计量单位
	 *
	 * @throws DAOException
	 */

	protected String getUnitCode(String pk) throws DAOException {
		String sql = " select code  from bd_measdoc where pk_measdoc = '" + pk
				+ "';";
		return (String) getDao().executeQuery(sql, getProcessor());
	}

	/**
	 * 查合同编码
	 *
	 * @throws DAOException
	 */

	protected String getCtCode(String pk) throws DAOException {
		String sql = " select vbillcode   from ct_pu where pk_ct_pu  = '" + pk
				+ "';";
		return (String) getDao().executeQuery(sql, getProcessor());
	}

	/**
	 * 查批次生产日期
	 *
	 * @throws DAOException
	 */

	protected String getBatch(String pk) throws DAOException {
		String sql = " select dproducedate    from scm_batchcode where pk_batchcode   = '"
				+ pk + "';";
		return (String) getDao().executeQuery(sql, getProcessor());
	}

	// 查询采购组织名称，编码
	public Map<String, Object> getPurchaseorgs(String pk) throws DAOException {
		String sql = "select name,code from  org_purchaseorg   where   pk_purchaseorg 	 = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	/**
	 * 查head信息
	 *
	 * @throws BusinessException
	 */
	public void getHead() throws BusinessException {
		String head = (String) getDao().executeQuery(
				"select name from bd_defdoc where code = 'gwcode'",
				getProcessor());
		if (StringUtil.isEmpty(head)) {
			throw new BusinessException(
					"关务归属企业与归属团队未配置配置编码：gwcode 企业在前团队在后用,分割");
		}
		String[] split = head.split(",");
		OwnerOrgId = split[0];
		OwnerTeamId = split[1];
		String gwCode = (String) getDao().executeQuery(
				"select name from bd_defdoc where code = 'gwMiddleCode'",
				getProcessor());
		if (StringUtil.isEmpty(head)) {
			throw new BusinessException(
					"关务归属企业与归属团队未配置配置编码：gwcode 企业在前团队在后用,分割");
		}
		gwMiddleCode = gwCode;
	}

	/**
	 *
	 * 组织参数
	 *
	 * @param pk_org
	 * @return
	 * @throws DAOException
	 */
	public boolean IsToGW(String pk_org) throws DAOException {
		String sql = "select distinct value  from  pub_sysinit where initcode='YF_TOGW' and pk_org = '"
				+ pk_org + "'";
		BaseDAO dao = new BaseDAO();
		String is2crm = (String) dao.executeQuery(sql, new ColumnProcessor());
		if (is2crm == null || StringUtil.isEmpty(is2crm)) {
			return false;
		}
		if ("Y".equalsIgnoreCase(is2crm)) {
			return true;
		}
		return false;
	}
	/**
	 * 发货单校验
	 * @throws DAOException
	 */
	public String getTypeCode(String pk) throws DAOException{
		return (String)getDao().executeQuery("select   pk_billtypecode  from bd_billtype where pk_billtypeid  = '"+pk+"';", processor);

	}
	/**
	 * 	判断是否未审批
	 */
	public Boolean isApprove(Integer status,String eventType){
		if((status == 0 && "1022".equals(eventType))
				|| (status == 3 && "1020".equals(eventType))){
			return true;
		}
		return false;
	}
	/**
	 *
	 * 发货单是否审批
	 */
	public Boolean fhIsApprove(Integer status,String eventType){
		if((status == 1 && "1022".equals(eventType))
				|| (status == 2 && "1019".equals(eventType))){
			return true;
		}
		return false;
	}

	public Boolean bachCode(String pk) throws DAOException{
		String sql = "select   pk_billtypecode  from  bd_billtype where   pk_billtypeid  = '"+pk+"'";
		String executeQuery = (String)getDao().executeQuery(sql,processor );
		if("30-Cxx-10-08".equals(executeQuery)){
			return true;
		}
		return false;
	}

	/**
	 * 查销售订单关联的合同编码，多个合同号逗号分割
	 * 根据销售订单vbillcode查询
	 * @param vbillcode 销售订单单据号
	 * @throws DAOException
	 */
	protected String getSaleCtCode(String vbillcode) throws DAOException {
		String sql = "select distinct b.vctcode from so_saleorder_b b " +
				"inner join so_saleorder h on b.csaleorderid = h.csaleorderid " +
				"where h.vbillcode = '" + vbillcode + "' and b.vctcode is not null";
		List results = (List) getDao().executeQuery(sql, new MapListProcessor());
		if (results == null || results.isEmpty()) {
			return "";
		}
		StringBuilder contractNos = new StringBuilder();
		for (int i = 0; i < results.size(); i++) {
			Map map = (Map) results.get(i);
			String vctcode = (String) map.get("vctcode");
			if (StringUtil.isNotEmpty(vctcode)) {
				if (contractNos.length() > 0) {
					contractNos.append(",");
				}
				contractNos.append(vctcode);
			}
		}
		return contractNos.toString();
	}
}

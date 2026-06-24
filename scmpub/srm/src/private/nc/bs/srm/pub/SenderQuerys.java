package nc.bs.srm.pub;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.bd.defdoc.DefdocVO;

public class SenderQuerys {
	private BaseDAO dao = null;
	ColumnProcessor columprocessor = new ColumnProcessor();
	public MapListProcessor maplistProcessor = new MapListProcessor();

	protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 查询组织名称
	public String getPkorgName(String pk) throws DAOException {
		String sql = "SELECT   name from org_orgs where   pk_org= '" + pk + "'";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询组织编码
	public String getPkorgCode(String pk) throws DAOException {
		String sql = "SELECT code from org_orgs where   pk_org= '" + pk + "'";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询报检单单据pk
	public String getPk(String pk) throws DAOException {
		String sql = " select pk_applybill from qc_applybill   where vbillcode = '"
				+ pk + "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询磅单pk
	public Object getPkpu(Object pk) throws DAOException {
		String sql = " select PK_ORDER_BB1 from HYPU_MEASUERBILL where measureid  = '"
				+ pk + "' and dr = 0 ";
		Object pk_org = getDao().executeQuery(sql, new ColumnProcessor());
		return pk_org;
	}

	// 查询制单人
	public String getCusdept(String pk) throws DAOException {
		String sql = " select d2.name AS sjbm from bd_customer cus left join bd_custsale sale on cus.pk_customer = sale.pk_customer left join org_dept d1 on sale.respdept = d1.pk_dept left join org_dept d2 on d1.pk_fatherorg = d2.pk_dept where cus.code = '"
				+ pk + "' ";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制单人
	public String getCusdeptl(String pk) throws DAOException {
		String sql = " select d1.name AS sjbm from bd_customer cus left join bd_custsale sale on cus.pk_customer = sale.pk_customer left join org_dept d1 on sale.respdept = d1.pk_dept where cus.code = '"
				+ pk + "' ";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制单人
	public String getBillmaker(String pk) throws DAOException {
		String sql = "select user_name from sm_user  where cuserid = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询采购编码
	public String getOrgpk(String pk) throws DAOException {
		String sql = "select pk_purchaseorg from org_purchaseorg   where code = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 集团
	public String getPkgroup(String pk) throws DAOException {
		String sql = "select pk_group from org_group where code = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 组织
	public String getObPkOrg(String pk) throws DAOException {
		String sql = "select pk_org from org_orgs where def1 = '" + pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 供应商pk
	public String getPksupplier(String pk) throws DAOException {
		String sql = "select pk_supplier from bd_supplier  where code = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 供应商pk
	public String getPksuppliercode(String pk) throws DAOException {
		String sql = "select code from bd_supplier  where pk_supplier = '" + pk
				+ "';";
		String code = (String) getDao().executeQuery(sql, columprocessor);
		return code;
	}

	// 供应商分类pk
	public String getPKsupplierclass(String code) throws DAOException {
		String sql = "select   pk_supplierclass  from bd_supplierclass  where code = '"
				+ code + "';";
		String pk_supplierclass = (String) getDao().executeQuery(sql,
				columprocessor);
		return pk_supplierclass;
	}

	// 材料出库类别
	public String getMaterialOutDef3(String code) throws DAOException {
		String sql = "SELECT pk_defdoc FROM bd_defdoc WHERE code = '"
				+ code
				+ "' and dr = 0 and  pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD008');";
		String pk_defdoc = (String) getDao().executeQuery(sql, columprocessor);
		return pk_defdoc;
	}

	// 销售合同探伤分类（中板）
	public String getCtsaleTs(String code) throws DAOException {
		String sql = "SELECT pk_defdoc FROM bd_defdoc WHERE code = '"
				+ code
				+ "' and dr = 0 and  pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD_TSFL');";
		String pk_defdoc = (String) getDao().executeQuery(sql, columprocessor);
		return pk_defdoc;
	}

	// 销售合同厚度偏差分类JD_PCFW
	public String getCtsalePc(String code) throws DAOException {
		String sql = "SELECT pk_defdoc FROM bd_defdoc WHERE code = '"
				+ code
				+ "' and dr = 0 and  pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD_PCFW');";
		String pk_defdoc = (String) getDao().executeQuery(sql, columprocessor);
		return pk_defdoc;
	}

	// 销售合同订单切边分类（中板）
	public String getCtsaleQb(String code) throws DAOException {
		String sql = "SELECT pk_defdoc FROM bd_defdoc WHERE code = '"
				+ code
				+ "' and dr = 0 and  pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD_DDQBFL');";
		String pk_defdoc = (String) getDao().executeQuery(sql, columprocessor);
		return pk_defdoc;
	}

	// 查询物料编码、pk
	public List<Map<String, Object>> getMaterialsByCodes(String materialCodes)
			throws DAOException {
		Assert.assertNotNull("物料编码不能为空", materialCodes);
		String sql = "select pk_material,code from bd_material where code in "
				+ materialCodes;
		List<Map<String, Object>> infos = (List<Map<String, Object>>) getDao()
				.executeQuery(sql, maplistProcessor);
		return infos;

	}

	// 物料pk
	public String getPkmaterial(String pk) throws DAOException {
		String sql = "select pk_material from bd_material  where code = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制单人编码
	public String getBillmakercode(String pk) throws DAOException {
		String sql = "select user_code from sm_user  where cuserid = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 根据用户名查询制单人编码
	public String getUser(String pk) throws DAOException {
		String sql = "select user_code from sm_user  where user_name = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 根据用编码查询用户pk
	public String getUserPK(String code) throws DAOException {
		String sql = "select cuserid  from sm_user  where user_code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制单人pk
	public String getCreator(String code) throws DAOException {
		String sql = "select cuserid from sm_user  where user_code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制单人编码
	public String getCbshift(String pk) throws DAOException {
		String sql = "select name from bd_shift   where pk_shift = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询客户申请单客户编码
	public String getCustomerPfCode(String code) throws DAOException {
		String sql = "select customercode from bd_customer_pf  where customercode = '"
				+ code + "' and dr='0';";
		String customercode = (String) getDao().executeQuery(sql,
				columprocessor);
		return customercode;
	}

	// 查询客户申请单客户名称
	public String getCustomerPfName(String name) throws DAOException {
		String sql = "select customername from bd_customer_pf  where customername = '"
				+ name + "' and dr='0';";
		String customername = (String) getDao().executeQuery(sql,
				columprocessor);
		return customername;
	}

	// 查询收款单是否存在
	public String getArapGat(Object def32) throws DAOException {
		String sql = "SELECT  def32 from ar_gatherbill  where def32='" + def32
				+ " 'and dr='0'";
		String arapgat = (String) getDao().executeQuery(sql, columprocessor);
		return arapgat;
	}

	// 查询付款单是否存在
	public String getArapPay(Object def21) throws DAOException {
		String sql = "SELECT  def21 from ap_paybill  where def21='" + def21
				+ "' and dr='0' ";
		String arappay = (String) getDao().executeQuery(sql, columprocessor);
		return arappay;
	}

	// 查询付款单是否存在
	public String getHysoTariff(Object def20) throws DAOException {
		String sql = "SELECT  def20 from HYSO_TARIFF  where def20='" + def20
				+ "' and dr='0' ";
		String hysotariff = (String) getDao().executeQuery(sql, columprocessor);
		return hysotariff;
	}

	// 查询制单人
	public String getDef11(String pk) throws DAOException {
		String sql = "select def11 from bd_customer  where code = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 仓库
	public String getStordoc(String pk) throws DAOException {
		String sql = "select name from bd_stordoc  where pk_stordoc = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 仓库主键
	public String getStordocpk(String code) throws DAOException {
		String sql = "select pk_stordoc from bd_stordoc  where code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 仓库主键
	public String getStordocpks(String pk_org, String code) throws DAOException {
		String sql = "select pk_stordoc from bd_stordoc  where pk_org='"
				+ pk_org + "' and code = '" + code + "';";
		String pk_stordoc = (String) getDao().executeQuery(sql, columprocessor);
		return pk_stordoc;
	}

	// 供应商
	public Map<String, Object> getSupplier(String pk) throws DAOException {
		String sql = "select name,code,taxpayerid,memo from bd_supplier  where pk_supplier = '"
				+ pk + "';";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 查询请购单表头主键
	public String getPkPraybill(String pk) throws DAOException {
		String sql = "SELECT  pk_praybill  from po_praybill where  pk_praybill='"
				+ pk + "'  and  dr=0";
		String pk_praybill = (String) getDao()
				.executeQuery(sql, columprocessor);
		return pk_praybill;
	}

	// 查询请购单表头主键
	public String getPkPraybillB(String pk) throws DAOException {
		String sql = "SELECT  pk_praybill_b  from po_praybill_b where  pk_praybill_b='"
				+ pk + "'  and  dr=0";
		String pk_praybill = (String) getDao()
				.executeQuery(sql, columprocessor);
		return pk_praybill;
	}

	// 仓库
	public String getStordoccode(String pk) throws DAOException {
		String sql = "select code from bd_stordoc  where pk_stordoc = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 仓库组织名称
	public String getStordocname(String pk) throws DAOException {
		String sql = "select name from bd_stordoc where pk_stordoc = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 公司
	public Map<String, Object> getOrgcorp(String pk) throws DAOException {
		String sql = "select name,code from org_corp where pk_corp = '" + pk
				+ "';";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 币种
	public String getCurrtype(String pk) throws DAOException {
		String sql = "select name  from bd_currtype where pk_currtype = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 币种id
	public String getCurrtypeid(String code) throws DAOException {
		String sql = "select pk_currtype   from bd_currtype where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 币种code
	public String getCurrtypeCode(String pk_currtype) throws DAOException {
		String sql = "select  code  from bd_currtype where pk_currtype = '"
				+ pk_currtype + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 销售出库币种id
	public String getCorigcurrencyid(String code) throws DAOException {
		String sql = "select pk_currtype as corigcurrencyid   from bd_currtype where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 国家地区
	public String getcountry(String code) throws DAOException {
		String sql = "select pk_country  from bd_countryzone where code = '"
				+ code + "';";
		String pk_country = (String) getDao().executeQuery(sql, columprocessor);
		return pk_country;
	}

	// 销售合同币种
	public String getCtsale(String code) throws DAOException {
		String sql = "select pk_currtype from bd_currtype where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织
	public String getStockorgPk(Object pk) throws DAOException {
		String sql = "select pk_stockorg from org_stockorg  where code = '"
				+ pk + "' and dr = 0 and islastversion = 'Y'";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织
	public String getStockorg(String pk) throws DAOException {
		String sql = "select name from org_stockorg  where pk_stockorg = '"
				+ pk + "' and dr = 0";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织
	public String getStockorgCode(String pk) throws DAOException {
		String sql = "select code from org_stockorg  where pk_stockorg = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织V
	public String getStockorgvCode(String pk) throws DAOException {
		String sql = "select code from org_stockorg_v  where name = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询检测中心
	public String getOrg_qccenter(String pk) throws DAOException {
		String sql = "select name from org_qccenter where pk_qccenter = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询部门
	public String getDept(String pk) throws DAOException {
		String sql = "select name from org_dept where pk_dept = '" + pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}
	// 查询部门最新版本
		public String getDept_v(String pk) throws DAOException {
			String sql = "select pk_vid from org_dept_v where islastversion = 'Y' and pk_dept = '" + pk + "';";
			String pk_vid = (String) getDao().executeQuery(sql, columprocessor);
			return pk_vid;
		}

	// 查询部门id 通过人员id查询
	public String getDeptidByPsnPK(String pk) throws DAOException {
		String sql = "SELECT  pk_dept from  bd_psnjob   where  pk_psndoc= '"
				+ pk + "';";
		String pk_dept = (String) getDao().executeQuery(sql, columprocessor);
		return pk_dept;
	}

	// 查询部门id 通过人员编码查询
	public String getDeptidByUserCode(String pk) throws DAOException {
		String sql = "SELECT  pk_dept from  bd_psnjob   where pk_psndoc= '"
				+ pk + "'and ismainjob = 'Y'";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 人员编码查询部门
	public String getSaleDept(String code) throws DAOException {
		String sql = "select code from org_dept where pk_dept = "
				+ "(SELECT  pk_dept from  bd_psnjob   where pk_psndoc= "
				+ "(SELECT  pk_psndoc from  bd_psndoc where code = '" + code
				+ "') " + "and enddutydate is null ) ";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询部门id
	public String getDeptPk(String code) throws DAOException {
		String sql = "select pk_dept from org_dept where code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单位名称
	public String getCastunitid(String pk) throws DAOException {
		String sql = "select name from bd_measdoc  where pk_measdoc = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询检验方案
	public String getCheckstandard(String pk) throws DAOException {
		String sql = "select vchkstandardname from qc_checkstandard   where pk_checkstandard = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 表体查询检验方案
	public Map<String, Object> getBodycheckstandard(String pk)
			throws DAOException {
		String sql = "select qcc.vchkstandardcode as vchkstandardcode,qc.vcheckmodename as vcheckmodename from qc_checkstandard qcc LEFT JOIN qc_checkmode qc ON qcc.pk_checkmode =  qc.pk_checkmode where qcc.pk_checkstandard =  '"
				+ pk + "';";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 查询检验项目
	public Object getCheckitemPk(Object pk, Object pk_org) throws DAOException {
		String sql = "select pk_checkitem from qc_checkitem  where vcheckitemcode = '"
				+ pk + "' and pk_org = '" + pk_org + "'and dr = 0";
		Object pk_checkitem = getDao().executeQuery(sql, columprocessor);
		return pk_checkitem;
	}

	// 查询检验项目
	public Map<String, Object> getCheckitem(String pk) throws DAOException {
		String sql = "select vcheckitemcode,vcheckitemname,pk_measdoc,ichecktype from qc_checkitem  where pk_checkitem = '"
				+ pk + "' and dr = 0 ";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 查询检验批次
	public String getChkbatch(String pk) throws DAOException {
		String sql = "select vbatchcode from qc_chkbatch  where pk_chkbatch = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询司机姓名、联系方式、身份证号
	public Map<String, Object> getDriver(String pk) throws DAOException {
		String sql = "select vdrivername,vidcard,vmobile from  dm_driver  where cdriverid = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询采购员
	public Map<String, Object> getCemployer(String pk) throws DAOException {
		String sql = "select name,mobile,code from  bd_psndoc where pk_psndoc = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询物料编码、名称、规格、型号、
	public Map<String, Object> getMaterial(String pk) throws DAOException {
		String sql = "select materialspec,materialtype, pk_marbasclass,name,code,NVL(materialspec,'') AS materialspec,NVL(materialtype,'') AS materialtype from bd_material_v where pk_material = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询销售组织
	public String getOrg(String pk) throws DAOException {
		String sql = "select name from  org_salesorg  where pk_salesorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织编码
	public String getOrgcode(String pk) throws DAOException {
		String sql = "select code from  org_salesorg  where name = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织编码 根据主键查
	public String getOrgBycode(String pk) throws DAOException {
		String sql = "select code from  org_salesorg  where pk_salesorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织PK
	public String getOrgPk(String code) throws DAOException {
		String sql = "select pk_salesorg from  org_salesorg  where code = '"
				+ code + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询集团编码
	public String getGroupcode(String pk) throws DAOException {
		String sql = "select code from  org_group   where pk_group = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询发货部门
	public String getDpt(String pk) throws DAOException {
		String sql = "select name from org_dept_v where pk_vid = '" + pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询销售组织
	public String getPkorg(String na) throws DAOException {
		String sql = "select pk_salesorg from  org_salesorg  where name = '"
				+ na + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 名称查询销售组织编码
	public String getNameCode(String na) throws DAOException {
		String sql = "select code from  org_salesorg  where name = '" + na
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 名称查询财务组织编码
	public String getFiorgCode(String na) throws DAOException {
		String sql = "select code from  org_financeorg   where name = '" + na
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织
	public String getSoCode(String na) throws DAOException {
		String sql = "select code from  org_salesorg  where name = '" + na
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织
	public String getOrgCode(String pk) throws DAOException {
		String sql = "select code from  org_salesorg_v  where pk_salesorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询物流组织名称
	public String getName(String pk) throws DAOException {
		String sql = "select name from  org_trafficorg  where pk_financeorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询物流组织code
	public String getCode(String pk) throws DAOException {
		String sql = "select code from  org_trafficorg  where pk_financeorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询物料
	public Map<String, Object> getMa(String pk) throws DAOException {
		String sql = "select mv.code,mv.name,mac.code AS mcode,mac.name AS mname from bd_material_v  mv LEFT JOIN bd_marbasclass mac ON mv.pk_marbasclass = mac.pk_marbasclass where  pk_material = '"
				+ pk + "';";
		Map<String, Object> name = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return name;
	}

	// 查询地区名称
	public String getCountryzone(String pk) throws DAOException {
		String sql = "select name from  bd_countryzone where pk_country = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询结算组织
	public String getPsfinanceorg(String pk) throws DAOException {
		String sql = "select name from  org_financeorg where pk_financeorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询结算组织
	public String getAreacl(String pk) throws DAOException {
		String sql = "select name from  bd_areacl  where pk_areacl = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询地区分类
	public String getAreaclPK(String pk) throws DAOException {
		String sql = "select pk_areacl from  bd_areacl  where code = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询结算组织名称，根据编码
	public String getAreaclNameByCode(String code) throws DAOException {
		String sql = "select name from  bd_areacl  where code = '" + code
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查对应的结算方式编码
	public String getBalatypePK(String code) throws DAOException {
		String sql = "select   pk_balatype  from bd_balatype where code ='"
				+ code + "';";
		String pk_balatype = (String) getDao()
				.executeQuery(sql, columprocessor);
		return pk_balatype;
	}

	// 查询自定义项
	public String getDefdoc(String pk) throws DAOException {
		String sql = "select name from BD_DEFDOC where pk_defdoc = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}
	// 查询自定义项
	public String getDefdocPkSup(String pk) throws DAOException {
		String sql = "select pk_defdoc from bd_defdoc d left join bd_defdoclist l on d.pk_defdoclist = l.pk_defdoclist where d.code = '02' and l.code = 'JZZDY03';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}
	// 查询自定义项主键
	public String getDefdocPK(String code) throws DAOException {
		String sql = "select   pk_defdoc  from BD_DEFDOC where code = '" + code
				+ "';";
		String pk = (String) getDao().executeQuery(sql, columprocessor);
		return pk;
	}

	// 九州srm查询厂区主键
	public String getDefdocPKByList(String code) throws DAOException {
		String sql = "select   d.pk_defdoc  from BD_DEFDOC d left join bd_defdoclist l on d.pk_defdoclist = l.pk_defdoclist  where d.code = '"+code+"' and l.code = 'JZZDY01';";
		String pk = (String) getDao().executeQuery(sql, columprocessor);
		return pk;
	}

	// 查询自定义项code
	public String getDefdocCode(String pk) throws DAOException {
		String sql = "SELECT code from  bd_defdoc  where pk_defdoc ='"
				+ pk
				+ "' and dr = 0 AND pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD_JLWZFS');";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询自定义项code采购订单20
	public String getDefdocCode20(String pk) throws DAOException {
		String sql = "SELECT code from  bd_defdoc  where pk_defdoc ='"
				+ pk
				+ "' and dr = 0 AND pk_defdoclist in (select pk_defdoclist from BD_DEFDOCLIST where code = 'JD_SHFS');";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询承运商
	public String getCarrier(String pk) throws DAOException {
		String sql = "select bs.name from dm_carrier ca LEFT JOIN bd_supplier bs ON ca.csupplierid = bs.pk_supplier where ca.ccarrierid = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询客户名称
	public String getCusname(String pk) throws DAOException {
		String sql = "select name from  bd_customer   where pk_customer = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询客户编码
	public String getCuscode(String pk) throws DAOException {
		String sql = "select code from  bd_customer   where pk_customer = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询客户名称
	public String getCustomerName(String code) throws DAOException {
		String sql = "select name from  bd_customer   where code = '" + code
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 根据客户编码，查询客户名称
	public String getCusByCode(String pk) throws DAOException {
		String sql = "select name from  bd_customer   where code = '" + pk
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询客户名称编码
	public Map<String, Object> getCustom(String pk) throws DAOException {
		String sql = "select name,code from  bd_customer   where pk_customer = '"
				+ pk + "';";
		Map<String, Object> name = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return name;
	}

	// 查询销售单结算单信息
	public Map<String, Object> getCalseId(String pk) throws DAOException {
		String sql = "SELECT hysoh.vbillcode as vbillcode,hysob.crowno as crowno,icb.vbdef12 as def12,icb.vbdef13 as def13 FROM so_saleinvoice_b sob "
				+ "INNER JOIN ic_saleout_b icb on sob.csrcbid =icb.cgeneralbid  INNER JOIN hyso_saleinvoice_b hysob on hysob.csrcbid =icb.cgeneralbid  "
				+ " LEFT JOIN hyso_saleinvoice hysoh on hysoh.csaleinvoiceid=hysob.csaleinvoiceid where sob.csaleinvoicebid = '"
				+ pk + "' and sob.dr='0';";
		Map<String, Object> name = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return name;
	}

	// 查询单据类型
	public String getBilltype(String pk) throws DAOException {
		String sql = "select billtypename from  bd_billtype  where pk_billtypeid = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询单据类型
	public String getBillcode(String pk) throws DAOException {
		String sql = "select pk_billtypecode from  bd_billtype  where pk_billtypeid = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 根据单据编码，查询单据类型的Pk
	public String getBilltypePkByCode(String pk_billtypecode)
			throws DAOException {
		String sql = "select pk_billtypeid from  bd_billtype  where pk_billtypecode = '"
				+ pk_billtypecode + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询最新收货库存组织
	public String getArrvstoorg(String pk) throws DAOException {
		String sql = "select name from  org_stockorg where pk_stockorg = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询库存组织—v
	public String getConsignee(String pk) throws DAOException {
		String sql = "select pk_stockorg from  org_stockorg_v where code = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询产品大类pk
	public String getProdline(String code) throws DAOException {
		String sql = "select pk_prodline from  bd_prodline  where code = '"
				+ code + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询应付组织
	public String getApfinanceorg(String pk) throws DAOException {
		String sql = "select name from  org_financeorg where pk_financeorg  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询采购组织
	public String getOrg_purchaseorg(String pk) throws DAOException {
		String sql = "select pk_purchaseorg from  Org_purchaseorg where code  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询采购组织
	public String getPurchaseorg(String pk) throws DAOException {
		String sql = "select code from  Org_purchaseorg where pk_purchaseorg  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	/**
	 * 查询物料分类信息
	 * 
	 * @param pk_marbasclass
	 *            物料id
	 * @return
	 */
	public String getMarbasclass(String pk_marbasclass) throws DAOException {
		String sql = "select code from  bd_marbasclass where pk_marbasclass  = '"
				+ pk_marbasclass + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	/**
	 * 查询计量单位信息
	 * 
	 * @param pk_measdoc
	 *            单位id
	 * @return
	 */
	public Object getMeasdoc(String pk_measdoc) throws DAOException {
		String sql = "select name from  bd_measdoc where pk_measdoc  = '"
				+ pk_measdoc + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;

	}

	// 查询物料
	public Map<String, Object> getMabac(String pk) throws DAOException {
		String sql = "SELECT NVL(class.code,' ') AS type,NVL(doc.code,' ') AS unit, NVL(convert.measrate,' ') AS conversionRate  FROM bd_material_v  ish LEFT JOIN bd_marbasclass class ON ish.pk_marbasclass = class.pk_marbasclass "
				+ " LEFT JOIN bd_measdoc  doc ON ish.pk_measdoc = doc.pk_measdoc LEFT JOIN bd_materialconvert  convert ON ish.pk_material = convert.pk_material WHERE ish.dr = 0 and ish.code = '"
				+ pk + "';";
		Map<String, Object> name = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return name;
	}

	// 销售出库查询物料
	public Map<String, Object> getSaleoutma(String pk) throws DAOException {
		String sql = "select NVL(mav.pk_marbasclass,' ')AS pk_marbasclass,NVL(mav.code,' ') AS code,NVL(mav.name,' ') AS name,NVL(mav.materialspec,' ') AS spec ,NVL(doc.name,' ') AS docname from bd_material_v mav LEFT JOIN bd_measdoc doc ON mav.pk_measdoc = doc.pk_measdoc  where mav.pk_material = '"
				+ pk + "';";
		Map<String, Object> list = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return list;
	}

	// 查询提货方式
	public String getThfs(String pk) throws DAOException {
		String sql = "select name from BD_DEFDOC where pk_defdoclist =( select  pk_defdoclist from BD_DEFDOCLIST where code='DKTHFS') and pk_DEFDOC = '"
				+ pk + "' ";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询提货方式
	public String getBz(String pk) throws DAOException {
		String sql = "select vteamname from bd_team  where cteamid = '" + pk
				+ "' ";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 物料分类主键
	public String getPkmarbasclass() throws DAOException {
		String sql = "SELECT pk_marbasclass from bd_marbasclass  where name = '产成品'; ";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 获取单据类型
	public Map<String, Object> getBillTypeByCode(String code)
			throws DAOException {
		String sql = "select pk_billtypeid, billtypename,pk_billtypecode from bd_billtype  where  pk_billtypecode='"
				+ code + "';";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 查询客户pk
	public String getMakercode(String code) throws DAOException {
		String sql = "select pk_customer from bd_customer   where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制客户编码
	public String getCcustomerid(String pk) throws DAOException {
		String sql = "select pk_customer from bd_customer   where code = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制物料pk
	public String getMmaterial(String code) throws DAOException {
		String sql = "select pk_material from bd_material    where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询制物料分类pk
	public String getMarbaseclass(String code) throws DAOException {
		String sql = "select pk_marbasclass from bd_material    where code = '"
				+ code + "';";
		String pk_marbasclass = (String) getDao().executeQuery(sql,
				columprocessor);
		return pk_marbasclass;
	}

	// 根据编码查询采购员pk
	public Map<String, Object> getCemployerPkByCode(String code)
			throws DAOException {
		String sql = "select name,code,pk_psndoc from  bd_psndoc where code = '"
				+ code + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 根据编码查询采购员pk
	public String getCemployerByCode(Object code) throws DAOException {
		String sql = "select pk_psndoc from bd_psndoc where code = '" + code
				+ "' and dr = 0 ";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询审批人
	public Map<String, Object> getSmUser(String approver) throws DAOException {
		String sql = "select user_name,user_code from  sm_user  where cuserid = '"
				+ approver + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询财务组织名称,编码
	public Map<String, Object> getFinanceOrg(String pk) throws DAOException {
		String sql = "select name,code from  org_financeorg   where pk_financeorg = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询财务组织编码
	public String getFinanceOrgCode(String pk) throws DAOException {
		String sql = "select code from  org_financeorg   where pk_financeorg = '"
				+ pk + "' and dr = 0 ";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询财务组织名称,编码
	public Map<String, Object> getBilltypes(String pk) throws DAOException {
		String sql = "select billtypename,pk_billtypecode from  bd_billtype    where pk_billtypeid = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询部门
	public Map<String, Object> getDepts(String pk) throws DAOException {
		String sql = "select code,name from  org_dept  where pk_dept = '" + pk
				+ "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询采购组织名称，编码
	public Map<String, Object> getPurchaseorgs(String pk) throws DAOException {
		String sql = "select name,code from  org_financeorg   where pk_vid	 = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询物料编码、名称、规格、型号、
	public Map<String, Object> getMaterials(String pk) throws DAOException {
		String sql = "select name,code,NVL(materialspec,' ') AS materialspec,NVL(materialtype,' ') AS materialtype from bd_material where pk_material = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;

	}

	// 查询销售组织
	public String getMaterialV(String code) throws DAOException {
		String sql = "select pk_source from  bd_material_v  where code = '"
				+ code + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 根据物料编码查询物料名称
	public String getMaterialVcode(String code) throws DAOException {
		String sql = "select name from  bd_material_v  where code = '" + code
				+ "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询单位
	public String getMeasdocPk(String name) throws DAOException {
		String sql = "select pk_measdoc from bd_measdoc  where name = '" + name
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单位pk
	public String getMeasdocPkByCode(String name) throws DAOException {
		String sql = "select pk_measdoc from bd_measdoc  where code = '" + name
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单位编码
	public String getMeasdocByPk(String pk_measdoc) throws DAOException {
		String sql = "select code from bd_measdoc  where pk_measdoc = '"
				+ pk_measdoc + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询领料人pk
	public String getCbizidPk(String code) throws DAOException {
		String sql = "select pk_psndoc from bd_psndoc   where code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询销售组织PK
	public String getOrgVPk(String code) throws DAOException {
		String sql = "select pk_vid from  org_salesorg_v   where code = '"
				+ code + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询销售组织PK
	public String getPkOrgV(String name) throws DAOException {
		String sql = "select pk_vid from  org_salesorg_v   where name = '"
				+ name + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单据类型
	public String getBillName(String name) throws DAOException {
		String sql = "select pk_billtypeid from  bd_billtype  where billtypename = '"
				+ name + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询税码PK
	public String getTaxcode(String code) throws DAOException {
		String sql = "select pk_taxcode from bd_taxcode   where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询税码code
	public String getTaxcodeByPk(String pk_taxcode) throws DAOException {
		String sql = "select code from bd_taxcode   where pk_taxcode = '"
				+ pk_taxcode + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单位
	public String getMeasdocPkId(String code) throws DAOException {
		String sql = "select pk_measdoc from bd_measdoc  where code = '" + code
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询成本域编码
	public String getCostregionPk(String pk) throws DAOException {
		String sql = "select code from org_costregion  where pk_costregion = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询财务组织pk
	public String getPkFinanceorg(String code) throws DAOException {
		String sql = "select pk_financeorg from org_financeorg  where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询物料分类名称
	public String getMarbasc(String materialpk) throws DAOException {
		String sql = "select name from bd_marbasclass  where pk_marbasclass = '"
				+ materialpk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询发货单单据号
	public Map<String, Object> getDeliveryBillCode(String id)
			throws DAOException {
		String sql = "select vbillcode,fstatusflag,modifiedtime,ctrantypeid from so_delivery  where cdeliveryid = '"
				+ id + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询采购订单
	public Map<String, String> getOrderHead(String id) throws DAOException {
		String sql = "select vbillcode,fstatusflag,ctrantypeid,pk_supplier,dclosedate,dbilldate from po_order  where pk_order = '"
				+ id + "';";
		Map<String, String> info = (Map<String, String>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询产品线
	public String getCprodlineid(Object prodlineCode) throws DAOException {
		String sql = " select pk_prodline from bd_prodline where code ='"
				+ prodlineCode + "' and dr = 0";
		String cprodlineid = (String) getDao()
				.executeQuery(sql, columprocessor);
		return cprodlineid;
	}

	// 查询收款单
	public Map<String, Object> getGatheringBill(String id) throws DAOException {
		String sql = "select billno,billdate from ar_gatherbill   where pk_gatherbill = '"
				+ id + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 采购订单查上级业务单元
	public String getOrgUpCode(String id) throws DAOException {
		String sql = "select code from org_orgs where pk_org= (select pk_fatherorg from org_orgs where pk_org = '"
				+ id + "');";
		String code = (String) getDao().executeQuery(sql, columprocessor);
		return code;
	}

	// 查询付款单业务类型主键
	public String getArapRecpaytype(String code) throws DAOException {
		String sql = "select pk_recpaytype from fi_recpaytype  where code = '"
				+ code + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 获取合同类型
	public String getCtBusiness(String vtrantypecode) throws DAOException {
		String sql = "select ctrantypeid from ct_business where  vtrantypecode IN ('"
				+ vtrantypecode + "')";
		String code = (String) getDao().executeQuery(sql, columprocessor);
		return code;
	}

	/**
	 * 根据价目表编码获取pk
	 * 
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getPkTarrifByCode(String code) throws DAOException {
		Assert.assertNotNull("tariff code can not be null", code);
		String sql = "select pk_tariff from hyso_tariff t where t.def20='"
				+ code + "'";
		String pk_tariff = (String) getDao().executeQuery(sql, columprocessor);
		return pk_tariff;
	}

	// 获取生产部门主键
	public String getOrg_dept_v(String code) throws DAOException {
		String sql = "select pk_vid from org_dept_v  where  code='" + code
				+ "';";
		String pk_vid = (String) getDao().executeQuery(sql, columprocessor);
		return pk_vid;
	}

	// 获取产品类别自定义项
	public String getSaleOrderdef(String pk_defdoc) throws DAOException {
		String sql = "SELECT code FROM bd_defdoc WHERE pk_defdoc='" + pk_defdoc
				+ "'";
		String code = (String) getDao().executeQuery(sql, columprocessor);
		return code;
	}

	// 获取销售订单运输方式
	public String getTransporttype(String pk_transporttype) throws DAOException {
		String sql = "SELECT code FROM bd_transporttype WHERE pk_transporttype='"
				+ pk_transporttype + "'";
		String code = (String) getDao().executeQuery(sql, columprocessor);
		return code;
	}

	// 查询制物料单位
	public String getMmaterialmeas(String id) throws DAOException {
		String sql = "select pk_measdoc from bd_material    where pk_material = '"
				+ id + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 获取货位条码的所有信息
	public Map<String, Object> getPdaPkMap(String billno) throws DAOException {
		String sql = "select * from jzqc_labelprint  where  billno = '"
				+ billno + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 根据其他出库单的表体数据找到对应的其他出库单表头
	public Map<String, Object> getIa_i7billMap(String cbillid)
			throws DAOException {
		String sql = "select * from ia_i7bill    where  cbillid = '" + cbillid
				+ "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查公司最新版主键
	public String getCorpoid(String code) throws DAOException {
		String sql = "select pk_corp  from org_corp    where  code = '" + code
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查公司主键
	public String getCorpoid_v(String code) throws DAOException {
		String sql = "select  pk_vid from org_corp_v    where  code = '" + code
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 根据其他入库单的单据号找到对应的其他入库单
	public String getMM_wr(String vbillcode) throws DAOException {
		String sql = "SELECT pk_wr from mm_wr_product  where pk_wr_product in （select pk_wr_product from mm_wr_quality  where vginstockbcode in '"
				+ vbillcode + "');";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 根据货位编码查货位主键
	public String getRackPk(String code) throws DAOException {
		String sql = "SELECT  pk_rack from bd_rack  where code = '" + code
				+ "'";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 根据货位编码和组织查货位主键
	public String getRackPkByOrg(String code, String pk_stordoc)
			throws DAOException {
		String sql = "SELECT  pk_rack from bd_rack  where code = '" + code
				+ "' and pk_stordoc  = '" + pk_stordoc + "'";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询物料最新版
	public String getCmatPk(String code) throws DAOException {
		String sql = "SELECT    pk_source  from bd_material_v  where code = '"
				+ code + "'";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询库存状态
	public String getCstateidPk(String code, String pk_org) throws DAOException {
		String sql = "select pk_storestate from ic_storestate where pk_org = '"
				+ pk_org + "' and vcode  = '" + code + "'";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询库存状态
	public Map<String, Object> getScmByCode(String vbatchcode)
			throws DAOException {
		String sql = "select pk_batchcode,dproducedate,dvalidate  from scm_batchcode where vbatchcode = '"
				+ vbatchcode + "'";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询银行类别
	public String getbamkPk(String code) throws DAOException {
		String sql = " select pk_banktype  from bd_banktype where code = '"
				+ code + "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 根据名称查询银行类别
	public String getbamkPkByName(String code) throws DAOException {
		String sql = " select pk_banktype  from bd_banktype where name = '"
				+ code + "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询组织主键
	public String getOrgsPk(String code) throws DAOException {
		String sql = " select pk_org  from org_orgs where code = '" + code
				+ "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}
	
	/**
	 * 通过组织编码查询库存组织
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getOrgpkByCode(String code) throws DAOException{
		String sql =" select pk_org from org_orgs where orgtype9 ='Y' and code = '"+code+"'";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
		
	}

	// 查询付款协议
	public String getPk_paytermPk(String code) throws DAOException {
		String sql = " select pk_payment   from bd_payment where code = '"
				+ code + "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询付款协议pk
	public String getCodeBypk_payment(String pk_payment) throws DAOException {
		String sql = " select code   from bd_payment where pk_payment = '"
				+ pk_payment + "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 查询人员主键 bd_billtype
	public String getPsnPk(String code) throws DAOException {
		String sql = " select pk_psndoc  from bd_psndoc where code = '" + code
				+ "' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	// 订单类型
	public String getBilltypePk(String code) throws DAOException {
		String sql = " select   pk_billtypeid  from bd_billtype where code = '"
				+ code + "' ";
		String pk_billtypeid = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_billtypeid;
	}

	// 查询详细地址
	public String getAddress(String code) throws DAOException {
		String sql = " select pk_address  from bd_address where detailinfo  like '%"
				+ code + "%' ";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}
	
	// 查询详细地址
		public String getAddressByPk(String pk) throws DAOException {
			String sql = " select detailinfo   from bd_address where pk_address  = '"
					+ pk + "' ";
			String pk_org = (String) getDao().executeQuery(sql,
					new ColumnProcessor());
			return pk_org;
		}

	// 查询业务流程编码
	public String getBusiType(String code, String orgcode) throws DAOException {
		String sql = "select pk_busitype  from bd_busitype  where primarybilltype = '"
				+ code
				+ "' and validity = 1 and  busicode like '%"
				+ orgcode
				+ "%'";
		String pk_org = (String) getDao().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	/**
	 * 自定义项档案，查助记码
	 * 
	 * @param value
	 * @return
	 * @throws DAOException
	 * @throws UifException
	 */
	public Object getMnecodeByCode(String listcode, String doccode,
			String pk_org) throws DAOException, UifException {
		String where = " bd_defdoc.pk_defdoclist in (select t.pk_defdoclist from bd_defdoclist t where t.code ='"
				+ listcode + "') and bd_defdoc.code = '" + doccode + "'";

		if (!nc.vo.jcom.lang.StringUtil.isEmpty(pk_org)) {
			where = where + "and bd_defdoc.pk_org = '" + pk_org + "'";
		}

		DefdocVO[] vos = (DefdocVO[]) new HYPubBO().queryByCondition(
				DefdocVO.class, where);

		if (vos != null && vos.length > 0) {
			return vos[0].getName();
		} else {
			return null;
		}
	}
}

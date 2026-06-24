package nc.bs.jzyy.sys.oa.out;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;

public class SenderQuerys {
	private BaseDAO dao = null;
	ColumnProcessor columprocessor = new ColumnProcessor();

	protected BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	// 查询制单人
	public String getCusdept(String pk) throws DAOException {
		String sql = " select d2.name AS sjbm from bd_customer cus left join bd_custsale sale on cus.pk_customer = sale.pk_customer left join org_dept d1 on sale.respdept = d1.pk_dept left join org_dept d2 on d1.pk_fatherorg = d2.pk_dept where cus.code = '"
				+ pk + "' ";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询单据类型编码
	public String getBilltypeCode(String pk) throws DAOException {
		String sql = "select pk_billtypecode from  bd_billtype  where pk_billtypeid = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 请购单外系统部门
	public String getDeptEx(String pk) throws DAOException {
		String sql = "select exsysval from xx_bdcontra_b where PK_CONTRA = (select pk_contra from xx_bdcontra where bdclass2 = '部门') and bdcode = (select code from org_dept where pk_dept = '"
				+ pk + "' );";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 运输方式
	public String getTransport(String pk) throws DAOException {
		String sql = "select name from bd_transporttype where pk_transporttype = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 税码
	public String getTaxcode(String pk) throws DAOException {
		String sql = "select code from bd_taxcode where pk_taxcode = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 收款协议
	public String getIncome(String pk) throws DAOException {
		String sql = "select name from bd_income where pk_income = '" + pk
				+ "';";
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

	// 查询制单人编码
	public String getBillmakercode(String pk) throws DAOException {
		String sql = "select user_code from sm_user  where cuserid = '" + pk
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

	// 供应商
	public Map<String, Object> getSupplier(String pk) throws DAOException {
		String sql = "select name,code from bd_supplier  where pk_supplier = '"
				+ pk + "';";
		Map<String, Object> pk_org = (Map<String, Object>) getDao()
				.executeQuery(sql, new MapProcessor());
		return pk_org;
	}

	// 供应商
	public String getSuppliername(String pk) throws DAOException {
		String sql = "select name from bd_supplier  where pk_supplier = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 仓库
	public String getStordoccode(String pk) throws DAOException {
		String sql = "select code from bd_stordoc  where pk_stordoc = '" + pk
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
		String sql = "select name from bd_currtype where pk_currtype = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织
	public String getStockorg(String pk) throws DAOException {
		String sql = "select name from org_stockorg  where pk_stockorg = '"
				+ pk + "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 库存组织编码
	public String getStockorgCode(String pk) throws DAOException {
		String sql = "select code from org_stockorg  where pk_stockorg = '"
				+ pk + "';";
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

	// 查询付款协议
	public String getPayment(String pk) throws DAOException {
		String sql = "select name from bd_payment where pk_payment = '" + pk
				+ "';";
		String pk_org = (String) getDao().executeQuery(sql, columprocessor);
		return pk_org;
	}

	// 查询采购员编码
	public String getCemployercode(String code) throws DAOException {
		String sql = "select pk_psndoc from  bd_psndoc where code = '" + code
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询单位
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
	public Map<String, Object> getCheckitem(String pk) throws DAOException {
		String sql = "select vcheckitemcode,vcheckitemname,pk_measdoc,ichecktype from qc_checkitem  where pk_checkitem = '"
				+ pk + "';";
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

	// 查询采购员
	public Map<String, Object> getCemployer(String pk) throws DAOException {
		String sql = "select name,mobile from  bd_psndoc where pk_psndoc = '"
				+ pk + "';";
		Map<String, Object> info = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return info;
	}

	// 查询采购员
	public String getCemployername(String pk) throws DAOException {
		String sql = "select name from  bd_psndoc where pk_psndoc = '" + pk
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询采购员
	public String getCemployercodes(String pk) throws DAOException {
		String sql = "select code from  bd_psndoc where pk_psndoc = '" + pk
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}
	
	// 查询采购员pk
	public String getCemployerpk(String pk) throws DAOException {
		String sql = "select pk_psndoc from  bd_psndoc where code = '" + pk
				+ "';";
		String info = (String) getDao().executeQuery(sql, columprocessor);
		return info;
	}

	// 查询物料编码、名称、规格、型号、
	public Map<String, Object> getMaterial(String pk) throws DAOException {
		String sql = "select name,code,NVL(materialspec,' ') AS materialspec,NVL(materialtype,' ') AS materialtype from bd_material_v where pk_material = '"
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

	// 查询物料
	public Map<String, Object> getMa(String pk) throws DAOException {
		String sql = "select mv.code,mv.name,mac.code AS mcode from bd_material_v  mv LEFT JOIN bd_marbasclass mac ON mv.pk_marbasclass = mac.pk_marbasclass where  pk_material = '"
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

	// 查询自定义项
	public String getDefdoc(String pk) throws DAOException {
		String sql = "select name from BD_DEFDOC where pk_defdoc = '" + pk
				+ "';";
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
	public String getCus(String pk) throws DAOException {
		String sql = "select name from  bd_customer   where pk_customer = '"
				+ pk + "';";
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

	// 查询单据类型
	public String getBilltype(String pk) throws DAOException {
		String sql = "select billtypename from  bd_billtype  where pk_billtypeid = '"
				+ pk + "';";
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

	// 查询应付组织
	public String getApfinanceorg(String pk) throws DAOException {
		String sql = "select name from  org_financeorg where pk_financeorg  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询采购组织
	public String getOrg_purchaseorg(String pk) throws DAOException {
		String sql = "select pk_purchaseorg from  Org_purchaseorg where name  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	// 查询采购组织名称
	public String getPurchaseorg(String pk) throws DAOException {
		String sql = "select code from  org_purchaseorg where pk_purchaseorg  = '"
				+ pk + "';";
		String name = (String) getDao().executeQuery(sql, columprocessor);
		return name;
	}

	public String getPurchaseOrgCode(String pk) throws DAOException {
		String sql = "select code from  org_purchaseorg where pk_purchaseorg  = '"
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

	// 根据编码，查询OA参数
	public Map<String, Object> getOaParms(String code) throws DAOException {
		String sql = "select name,mnecode from BD_DEFDOC where pk_defdoclist =( select  pk_defdoclist from BD_DEFDOCLIST where code='JZYY_PZQD') and code = '"
				+ code + "' ";
		Map<String, Object> list = (Map<String, Object>) getDao().executeQuery(
				sql, new MapProcessor());
		return list;
	}
}

package nc.api.rest.ic.utils;

import nc.vo.bc.org.CostOrgVO;
import nc.vo.bd.address.AddressVO;
import nc.vo.bd.countryzone.CountryZoneVO;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.cust.areaclass.AreaclassVO;
import nc.vo.bd.cust.channeltype.ChannelTypeVO;
import nc.vo.bd.freecustom.FreeCustomVO;
import nc.vo.bd.incoterm.IncotermVO;
import nc.vo.bd.material.MaterialVersionVO;
import nc.vo.bd.material.marbasclass.MarBasClassVO;
import nc.vo.bd.material.measdoc.MeasdocVO;
import nc.vo.bd.payment.PaymentVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.bd.rack.RackVO;
import nc.vo.bd.rt.rt0004.entity.RcVO;
import nc.vo.bd.stordoc.StordocVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.supplier.supplierclass.SupplierclassVO;
import nc.vo.bd.taxcode.TaxcodeVO;
import nc.vo.bd.transporttype.TransportTypeVO;
import nc.vo.corg.CostRegionVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.org.CorpVO;
import nc.vo.org.DeptVO;
import nc.vo.org.FinanceOrgVO;
import nc.vo.org.GroupVO;
import nc.vo.org.LiabilityCenterVO;
import nc.vo.org.OrgVO;
import nc.vo.org.PurchaseOrgVO;
import nc.vo.org.SalesOrgVO;
import nc.vo.org.StockOrgVO;
import nc.vo.org.TrafficOrgVO;
import nc.vo.pmpub.project.ProjectBillVO;
import nc.vo.pub.SuperVO;
import nc.vo.scmf.bc.mbatchcode.BatchcodeVO;
import nc.vo.scmf.ic.mpacktype.PackTypeVO;
import nc.vo.scmf.pu.backreason.entity.BackReasonVO;
import nc.vo.scmf.qc.qualitylevel.entity.QualityLevelVO;
import nc.vo.sm.UserVO;

public enum JsonParamBDTranslateEnum {

	/**
	 * 集团
	 */
	pk_group(new GroupVO()),
	/**
	 * 组织档案
	 */
	pk_org(new OrgVO()),
	/**
	 * 组织成本域档案
	 */
	pk_costregion(new CostOrgVO()),
	/**
	 * 库存组织档案
	 */
	pk_stockOrg(new StockOrgVO()),
	/**
	 * 采购组织档案
	 */
	pk_purchaseOrg(new PurchaseOrgVO()),
	/**
	 * 财务组织档案
	 */
	pk_financeOrg(new FinanceOrgVO()),
	/**
	 * 销售组织档案
	 */
	pk_salesorg(new SalesOrgVO()),
	/**
	 * 国家地区档案
	 */
	pk_country(new CountryZoneVO()),
	/**
	 * 公司档案
	 */
	pk_corp(new CorpVO()),
	/**
	 * 客户档案
	 */
	pk_customer(new CustomerVO()),
	/**
	 * 组织部门档案
	 */
	pk_dept(new DeptVO()),
	/**
	 * 仓库档案
	 */
	pk_warehouseid(new StordocVO()),
	/**
	 * 人员档案
	 */
	pk_psndoc(new PsndocVO()),
	/**
	 * 供应商档案
	 */
	pk_supplier(new SupplierVO()),
	/**
	 * 供应商分类档案
	 */
	pk_supplierclass(new SupplierclassVO()),
	/**
	 * 币种档案
	 */
	ccurrencyid(new CurrtypeVO()),
	/**
	 * 税码税率档案
	 */
	ctaxcodeid(new TaxcodeVO()),
	/**
	 * 物料档案
	 */
	pk_material(new MaterialVersionVO()),
	/**
	 * 物料分类档案
	 */
	pk_marbasclass(new MarBasClassVO()),
	/**
	 * 利润中心档案
	 */
	pk_apliabcenter(new LiabilityCenterVO()),
	/**
	 * 结算成本域
	 */
	ccostdomainid(new CostRegionVO()),
	/**
	 * 用户档案
	 */
	pk_user(new UserVO()),
	/**
	 * 运输方式档案
	 */
	pk_transPortType(new TransportTypeVO()),
	/**
	 * 贸易术语档案
	 */
	pk_incoteerm(new IncotermVO()),
	/**
	 * 退货理由设置档案
	 */
	pk_backReason(new BackReasonVO()),
	/**
	 * 质量等级档案
	 */
	pk_qualityLevel(new QualityLevelVO()),
	/**
	 * 物料计量单位档案
	 */
	pk_measdoc(new MeasdocVO()),
	/**
	 * 物料包装类型档案
	 */
	pk_packType(new PackTypeVO()),
	/**
	 * 货位档案
	 */
	pk_clocationid(new RackVO()),
	/**
	 * 项目档案
	 */
	cprojectid(new ProjectBillVO()),
	/**
	 * 批次档案
	 */
	pk_batchcode(new BatchcodeVO()),
	/**
	 * 工序档案
	 */
	pk_rc(new RcVO()),
	/**
	 * 财务核算账簿
	 */
	pk_book(new AccountingBookVO()),
	/**
	 * 渠道类型
	 */
	pk_type(new ChannelTypeVO()),
	/**
	 * 物流组织
	 */
	pk_trafficorg(new TrafficOrgVO()),
	/**
	 * 地区分类
	 */
	pk_areacl(new AreaclassVO()),
	/**
	 * 付款协议
	 */
	pk_payment(new PaymentVO()),
	/**
	 * 地址簿
	 */
	pk_address(new AddressVO()),
	/**
	 * 散户
	 */
	pk_freecustom(new FreeCustomVO());

	// 成员变量
	private Object bdVO;

	// 构造方法
	private JsonParamBDTranslateEnum(Object bdVO) {
		this.bdVO = bdVO;
	}

	public Object getBdVO() {
		return bdVO;
	}

	public void setBdVO(SuperVO bdVO) {
		this.bdVO = bdVO;
	}

}

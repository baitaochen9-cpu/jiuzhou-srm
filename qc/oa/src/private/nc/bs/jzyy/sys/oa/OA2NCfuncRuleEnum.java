package nc.bs.jzyy.sys.oa;

import nc.bs.jzyy.sys.AbstracAdapter4Ext;
import nc.bs.jzyy.sys.oa.arap.OA_GaterBillQuery;
import nc.bs.jzyy.sys.oa.arap.OA_PayableBillQuery;
import nc.bs.jzyy.sys.oa.ic.OA_ICBillQuery;
import nc.bs.jzyy.sys.oa.out.ctsale.OA_CtSaleFlow;
import nc.bs.jzyy.sys.oa.out.praybill.OA_PraybillFlow;
import nc.bs.jzyy.sys.oa.priceaudit.OA_PriceAuditFlow;
import nc.bs.jzyy.sys.oa.pu.OA_AggCtPuVOApprove;
import nc.bs.jzyy.sys.oa.pu.OA_AggCtPuVOSave;
import nc.bs.jzyy.sys.oa.pu.OA_AggGatheringBillVOSave;
import nc.bs.jzyy.sys.oa.pu.OA_Ap_PaybillSave;
import nc.bs.jzyy.sys.oa.pu.OA_InvoiceVOSave;
import nc.bs.jzyy.sys.oa.pu.OA_Oa2F3;
import nc.bs.jzyy.sys.oa.pu.OA_PayPlanApprove;
import nc.bs.jzyy.sys.oa.pu.Oa2M20;
import nc.bs.jzyy.sys.oa.pu.OA_PoOrderApprove;
import nc.bs.jzyy.sys.oa.pu.OA_PoOrderOpt;
import nc.bs.jzyy.sys.oa.pu.OA_PuOrderToPaybillSave;
import nc.bs.jzyy.sys.oa.qry.OA_getProject;
import nc.bs.jzyy.sys.oa.qry.OA_getStordoc;
import nc.bs.jzyy.sys.oa.qry.OA_getArGatherBill;
import nc.bs.jzyy.sys.oa.qry.OA_updateArItems;
import nc.bs.jzyy.sys.oa.saleorder.OA_SaleOrderOpt;
import nc.bs.jzyy.sys.oa.saleordersync.OA_SaleOrderFlow;
public enum OA2NCfuncRuleEnum {
	/**
	 * 查询工程信息
	 */

	OA_getProject("OA_getProject", new OA_getProject()),

	/**
	 * 查询仓库信息
	 */

	OA_getStordoc("OA_getStordoc", new OA_getStordoc()),
	
	/**
	 * 查询收款单信息
	 */

	OA_getArGatherBill("OA_getArGatherBill", new OA_getArGatherBill()),
	/**
	 *收款单回写
	 */

	OA_updateArItems("OA_updateArItems",new OA_updateArItems()),

	/**
	 * 应付款单据查询
	 */
	OA_PAYBILLQUERY("OA_PAYBILLQUERY", new OA_PayableBillQuery()),

	/**
	 * 收款单据查询
	 */
	OA_GATHERBILLQUERY("OA_GATHERBILLQUERY", new OA_GaterBillQuery()),

	/**
	 * 入库单据查询
	 */
	OA_ICBILLQUERY("OA_ICBILLQUERY", new OA_ICBillQuery()),

	/**
	 * 销售订单
	 */
	OA_SALEORDER("OA_SALEORDER", new OA_SaleOrderOpt()),

	/**
	 * 采购订单
	 */
	OA_POORDER("OA_POORDER", new OA_PoOrderOpt()),

	OA_AGGCTPUVOSAVE("OA_AGGCTPUVOSAVE", new OA_AggCtPuVOSave()),
	// 请购单回写审核
	OA_PURCHAUDITFLOW("OA_PURCHAUDITFLOW", new OA_PraybillFlow()),
	// 合同审核
	OA_AGGCTPUVOAPPROVE("OA_AGGCTPUVOAPPROVE", new OA_AggCtPuVOApprove()),
	// 销售订单回写审核
	OA_SALEAPPROVE("OA_SALEAPPROVE", new OA_SaleOrderFlow()),
	// 销售合同回写审核
	OA_SPUAPPROVE("OA_SPUAPPROVE", new OA_CtSaleFlow()),
	// 价格审批单
	OA_PRICEAUDITFLOW("OA_PRICEAUDITFLOW", new OA_PriceAuditFlow()),
	// 订单审核
	OA_POORDERAPPROVE("OA_POORDERAPPROVE", new OA_PoOrderApprove()),

	OA_PUORDERTOPAYBILLSAVE("OA_PUORDERTOPAYBILLSAVE",
			new OA_PuOrderToPaybillSave()),

	/**
	 * 请购单 add by xuchong 2022-07-06
	 */
	OA_M20("OA_M20", new Oa2M20()),

	/**
	 * 采购付款计划生成付款单 add by xuchong 2022-07-06
	 */
	OA_F3BYPLAN("OA_F3BYPLAN", new OA_Oa2F3()),

	/**
	 * 采购付款计划审批结果回传 add by xuchong 2022-08-05
	 */
	OA_PAYPLAN_APPROVE("OA_PAYPLAN_APPROVE", new OA_PayPlanApprove()),

	/**
	 * 采购发票
	 */
	OA_INVOICEVO("OA_INVOICEVO", new OA_InvoiceVOSave()),

	/**
	 * 付款单
	 */
	OA_AP_PAYBILLSAVE("AP_PAYBILLSAVE", new OA_Ap_PaybillSave()),
	/**
	 * 收款单
	 */
	OA_AGGGATHERINGBILLVOSAVE("OA_AGGGATHERINGBILLVOSAVE",
			new OA_AggGatheringBillVOSave()),

	;
	public String name;
	public AbstracAdapter4Ext adapter1;

	OA2NCfuncRuleEnum(String name, AbstracAdapter4Ext adapter1) {
		this.name = name;
		this.adapter1 = adapter1;
	}

	/**
	 * 根据单据类型匹配到对应的业务处理类
	 * 
	 * @param name
	 * @return
	 */
	public static OA2NCfuncRuleEnum match(String name) {
		OA2NCfuncRuleEnum[] values = OA2NCfuncRuleEnum.values();
		for (OA2NCfuncRuleEnum value : values) {
			if (value.getName().equals(name)) {
				return value;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AbstracAdapter4Ext getAdapter1() {
		return adapter1;
	}

	public void setAdapter1(AbstracAdapter4Ext adapter1) {
		this.adapter1 = adapter1;
	}

}

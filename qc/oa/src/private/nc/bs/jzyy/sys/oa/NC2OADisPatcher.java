package nc.bs.jzyy.sys.oa;

import java.util.Map;

import nc.bs.jzyy.sys.oa.priceaudit.OA_PriceAuditPlugin;
import nc.bs.jzyy.sys.oa.saleordersync.OA_SaleOrderPlugin;
import nc.bs.jzyy.sys.oa.out.ct.CtPuSavePlugin;
import nc.bs.jzyy.sys.oa.out.ctsale.OA_CtSalePlugin;
import nc.bs.jzyy.sys.oa.out.praybill.OA_PraybillPlugin;
import nc.bs.jzyy.sys.oa.out.pu.PoOrderSavePlugin;
import nc.bs.jzyy.sys.oa.out.pu.PoPayPlanPlugin;
import nc.vo.pub.BusinessException;

/**
 * NC同步OA
 * 
 * @author yunfeng.li
 * 
 */
public class NC2OADisPatcher {

	public Object dispatch(Object obj, String billltypecode,
			Map<String, Object> param) throws BusinessException {
		Object rs = null;

		// 价格审批单
		if ("OA_PriceAudit_APPROVE".equalsIgnoreCase(billltypecode)) {
			OA_PriceAuditPlugin plugin = new OA_PriceAuditPlugin();

			plugin.sys(billltypecode, obj, param);

		}
		// 请购单
		if ("OA_Praybill_APPROVE".equalsIgnoreCase(billltypecode)) {
			OA_PraybillPlugin plugin = new OA_PraybillPlugin();
			plugin.sys(billltypecode, obj, param);
		}
		// 销售订单
		if ("OA_SaleOrder_APPROVE".equalsIgnoreCase(billltypecode)) {
			OA_SaleOrderPlugin plugin = new OA_SaleOrderPlugin();
			plugin.sys(billltypecode, obj, param);
		}

		// 销售合同
		if ("OA_SaleCt".equalsIgnoreCase(billltypecode)) {
			OA_CtSalePlugin plugin = new OA_CtSalePlugin();
			plugin.sys(billltypecode, obj, param);
		}

		// 采购付款计划 2022年7月19日 xuchong
		if ("OA_PAYPLAN".equalsIgnoreCase(billltypecode)) {
			PoPayPlanPlugin plugin = new PoPayPlanPlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 采购订单单
		if ("OA_POORDER".equalsIgnoreCase(billltypecode)) {
			PoOrderSavePlugin plugin = new PoOrderSavePlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		// 采购合同单
		if ("OA_CT".equalsIgnoreCase(billltypecode)) {
			CtPuSavePlugin plugin = new CtPuSavePlugin();
			return plugin.sys(billltypecode, obj, param);
		}

		return rs;

	}
}

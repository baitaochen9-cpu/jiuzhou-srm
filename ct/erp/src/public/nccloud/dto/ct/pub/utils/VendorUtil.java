package nccloud.dto.ct.pub.utils;

import nc.itf.scmpub.reference.uap.bd.supplier.SupplierPubService;
import nc.itf.scmpub.reference.uap.org.OrgUnitPubService;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.supplier.stock.SupStockVO;
import nc.vo.ct.query.vendor.VendorInfo;

/**
 * @description 供应商基本信息查询工具
 * @author xiahui
 * @date 创建时间：2019-1-24 下午2:01:59
 * @version ncc1.0
 **/
public class VendorUtil {

	public static VendorInfo queryCvendor(String cvendorid, String pk_org) {
		// 默认发货地址
		String address = null;
		// 组织信息，为了获取散户标识
		SupplierVO[] supplier = null;
		// 获得供应商采购组织页签
		SupStockVO[] supvo = null;
		// 获得组织本位币
		String pk_currtype = null;
		address = SupplierPubService.getDefaultConsignAddress(cvendorid, pk_org);
		supplier = SupplierPubService.getSupplierVO(new String[] { cvendorid }, new String[] { SupplierVO.ISFREECUST,
				SupplierVO.PK_COUNTRY });
		supvo = SupplierPubService.getSupStockVO(new String[] { cvendorid }, pk_org, new String[] { SupStockVO.RESPDEPT,
				SupStockVO.RESPPERSON, SupStockVO.CURRENCYDEFAULT, SupStockVO.PAYTERMDEFAULT, SupStockVO.BILLINGSUP,
				SupStockVO.SHIPPINGTYPE });
		pk_currtype = OrgUnitPubService.queryOrgCurrByPk(pk_org);
		VendorInfo si = new VendorInfo();
		if (supplier != null && supplier.length > 0) {
			si.setIsFreeCust(supplier[0].getIsfreecust());
			si.setPk_country(supplier[0].getPk_country());
		}
		if (supvo != null && supvo.length > 0) {
			si.setRespDepartment(supvo[0].getRespdept());
			si.setRespPerson(supvo[0].getRespperson());
			si.setDefaultCurrency(supvo[0].getCurrencydefault());
			si.setDefaultPaymentTerm(supvo[0].getPaytermdefault());
			si.setBillingSupplier(supvo[0].getBillingsup());
			si.setTransportType(supvo[0].getShippingtype());
		}
		si.setAddress(address);
		// 如果供应商默认交易币种为空，则取组织本位币
		if (si.getDefaultCurrency() == null) {
			si.setDefaultCurrency(pk_currtype);
		}
		return si;
	}
}

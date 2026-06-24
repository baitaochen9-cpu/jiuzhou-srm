package nccloud.dto.ct.saledaily.utils;

import java.util.Map;

import nc.itf.scmpub.reference.uap.bd.addrdoc.AddrdocPubService;
import nc.itf.scmpub.reference.uap.bd.customer.CustomerPubService;
import nc.vo.ct.entity.CtAbstractBVO;
import nc.vo.ct.entity.CtAbstractVO;
import nc.vo.ct.saledaily.entity.CtSaleVO;
import nc.vo.scmf.pub.keyvalue.IKeyValue;

/**
 * @description 默认地址设置
 * @author wangshrc
 * @date 2019年2月15日 下午6:23:17
 * @version ncc1.0
 */
public class CtSaleDefaultAddressUtil {

	/**
	 * 方法功能描述：设置表体默认收货地址、收货地区、收货地点
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param edit
	 *            <p>
	 * @since 6.3
	 * @author mafeic
	 * @time 2013-7-13 下午05:11:08
	 */
	public static void setBodyDefaultAddress(IKeyValue keyValue) {
		String[] defaultadds = getDefaultAddress(keyValue);
		int count = keyValue.getBodyCount();
		// 表头只有一个客户，所以只会返回一个默认地址
		if (null != defaultadds && defaultadds.length == 1) {
			// 取默认收货地区
			String[] defaultareapks = CustomerPubService
					.getAreaPksByConsignAddress(defaultadds);
			// 取默认收货地点
			Map<String, String> defaultaddoc = AddrdocPubService
					.getAddressDocPksByConsignAddress(defaultadds);
			for (int i = 0; i < count; i++) {
				// 设置默认收货地址
				keyValue.setBodyValue(i, CtAbstractBVO.PK_RECEIVEADDRESS,
						defaultadds[0]);
				// 设置默认收货地区
				if (null != defaultareapks && defaultareapks.length == 1) {
					keyValue.setBodyValue(i, CtAbstractBVO.CDEVAREAID,
							defaultareapks[0]);
				}
				// 设置默认收货地点
				if (null != defaultaddoc && !defaultaddoc.isEmpty()
						&& defaultaddoc.size() == 1) {
					keyValue.setBodyValue(i, CtAbstractBVO.CDEVADDRID,
							defaultaddoc.get(defaultadds[0]));
				}
			}

		} else {
			for (int i = 0; i < count; i++) {
				keyValue.setBodyValue(i, CtAbstractBVO.CDEVAREAID, null);
				keyValue.setBodyValue(i, CtAbstractBVO.PK_RECEIVEADDRESS, null);
				keyValue.setBodyValue(i, CtAbstractBVO.CDEVADDRID, null);
			}
		}
	}

	/**
	 * 方法功能描述：方法功能描述：设置行上默认收货地址、收货地区、收货地点
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param edit
	 * @param linenum
	 *            <p>
	 * @since 6.3
	 * @author mafeic
	 * @time 2013-7-13 下午05:11:43
	 */
	public static void setLineDefaultAddress(IKeyValue keyValue, int linenum) {

		String[] defaultadds = getDefaultAddress(keyValue);
		// 表头只有一个客户，所以只会返回一个默认地址
		if (null != defaultadds && defaultadds.length == 1) {
			// 取默认收货地区
			String[] defaultareapks = CustomerPubService
					.getAreaPksByConsignAddress(defaultadds);
			// 取默认收货地点
			Map<String, String> defaultaddoc = AddrdocPubService
					.getAddressDocPksByConsignAddress(defaultadds);
			// 设置默认收货地址
			keyValue.setBodyValue(linenum, CtAbstractBVO.PK_RECEIVEADDRESS,
					defaultadds[0]);
			// 设置默认收货地区
			if (null != defaultareapks && defaultareapks.length == 1) {
				keyValue.setBodyValue(linenum, CtAbstractBVO.CDEVAREAID,
						defaultareapks[0]);
			}
			// 设置默认收货地点
			if (null != defaultaddoc && !defaultaddoc.isEmpty()
					&& defaultaddoc.size() == 1) {
				keyValue.setBodyValue(linenum, CtAbstractBVO.CDEVADDRID,
						defaultaddoc.get(defaultadds[0]));
			}
		}

	}

	/**
	 * 方法功能描述：根据客户取默认收货地址
	 * <p>
	 * <b>参数说明</b>
	 * 
	 * @param edit
	 * @return <p>
	 * @since 6.3
	 * @author mafeic
	 * @time 2013-7-13 下午03:35:28
	 */
	private static String[] getDefaultAddress(IKeyValue keyValue) {
		// 客户pk
		String[] cvendorids = new String[] { keyValue
				.getHeadStringValue(CtSaleVO.PK_CUSTOMER) };
		// 主组织pk
		String pk_org = keyValue.getHeadStringValue(CtAbstractVO.PK_ORG);

		// 取客户默认收货地址
		String[] defaultadds = CustomerPubService.getDefaultAddresses(
				cvendorids, pk_org);

		return defaultadds;
	}

}

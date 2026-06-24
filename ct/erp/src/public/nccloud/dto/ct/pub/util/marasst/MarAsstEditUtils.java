package nccloud.dto.ct.pub.util.marasst;

import nccloud.pubimpl.scmpub.event.helper.MarAsstBeforeEditHelper;

/**
 * @description 物料自由辅助属性编辑前控制类
 * @author chaiwx
 * @date 2018-8-15 上午11:45:31
 * @version ncc1.0
 */
public class MarAsstEditUtils {

	/**
	 * 自由辅助项前缀
	 */
	private static final String PREFIX = "vfree";

	public MarAsstEditUtils() {
	}

	/**
	 * 
	 * 检查正常自由辅助属性
	 * 
	 * @param cinventoryvid
	 * @param editKey
	 * @return
	 * 
	 */
	public boolean checkFreeEdit(String pk_material, String editKey) {
		MarAsstBeforeEditHelper helper = this.createFreeEditHelper();
		helper.setKey(editKey);
		helper.setMaterialvid(pk_material);
		return helper.beforeEdit();
	}

	/**
	 * 
	 * 创建标准辅助属性控制
	 * 
	 * @return
	 * 
	 */
	protected MarAsstBeforeEditHelper createFreeEditHelper() {
		MarAsstBeforeEditHelper helper = new MarAsstBeforeEditHelper();
		// 前缀
		helper.setPrefix(PREFIX);
		// 客户
		helper.setCustomerField(MarAsstFieldConst.CASSCUSTID);
		// 供应商
		helper.setSupplierField(MarAsstFieldConst.CVENDORID);
		// 项目
		helper.setProjectField(MarAsstFieldConst.CPROJECTID);
		// 生产厂商
		helper.setProductorField(MarAsstFieldConst.CPRODUCTORID);
		// 特征码
		helper.setSignatureField(MarAsstFieldConst.CFFILEID);
		// 库存状态 无 不设置
		return helper;
	}

	/**
	 * 
	 * 是否是自由辅助项
	 * 
	 * @param editKey
	 * @return
	 * 
	 */
	public static boolean isFree(String editKey) {
		if (editKey != null
				&& (editKey.startsWith(PREFIX) || MarAsstFieldConst.CASSCUSTID.equals(editKey)
						|| MarAsstFieldConst.CVENDORID.equals(editKey) || MarAsstFieldConst.CPROJECTID.equals(editKey)
						|| MarAsstFieldConst.CPRODUCTORID.equals(editKey) || MarAsstFieldConst.CFFILEID.equals(editKey))) {
			return true;
		} else {
			return false;
		}
	}
}

package nccloud.dto.ct.pub.utils;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ISuperVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

/**
 * @description 一主多子单据操作工具类
 * @author xiahui
 * @date 创建时间：2019-1-28 上午9:40:41
 * @version ncc1.0
 **/
public class ExtBillUtil extends AbstractExtVOUtil {

	private AbstractBill extBill;

	public ExtBillUtil(AbstractBill extBill) {
		this.extBill = extBill;
	}

	/**
	 * 获取表头VO
	 * 
	 * @return
	 */
	public CircularlyAccessibleValueObject getHead() {
		return (CircularlyAccessibleValueObject) extBill.getParentVO();
	}

	/**
	 * 获取首个表体VO
	 * 
	 * @param index
	 * @return
	 */
	public CircularlyAccessibleValueObject getBody(int index) {
		CircularlyAccessibleValueObject[] childVos = this.extBill.getChildrenVO();
		if (childVos == null || childVos.length == 0) {
			return null;
		} else {
			return childVos[index];
		}
	}
	
	/**
	 * 设置表体
	 * @param clazz
	 * @param vos
	 */
	public void setBody(Class<? extends ISuperVO> clazz, ISuperVO[] vos) {
		this.extBill.setChildren(clazz, vos);
	}

	/**
	 * 获取指定表体VO
	 * 
	 * @param index
	 * @param clazz
	 * @return
	 */
	public ISuperVO getBody(int index, Class<? extends ISuperVO> clazz) {
		ISuperVO[] childVos = this.extBill.getChildren(clazz);
		if (childVos == null || childVos.length == 0) {
			return null;
		} else {
			return childVos[index];
		}
	}

	/**
	 * 获取表体行数
	 * 
	 * @return
	 */
	public int getBodyRowCount() {
		CircularlyAccessibleValueObject[] childVos = this.extBill.getChildrenVO();
		if (childVos == null || childVos.length == 0) {
			return 0;
		} else {
			return childVos.length;
		}
	}

	/**
	 * 获取指定表体行数
	 * 
	 * @param clazz
	 * @return
	 */
	private int getBodyRowCount(Class<? extends ISuperVO> clazz) {
		ISuperVO[] childVos = this.extBill.getChildren(clazz);
		if (childVos == null || childVos.length == 0) {
			return 0;
		} else {
			return childVos.length;
		}
	}

	/**
	 * 将行号以数组形式输出
	 * 
	 * @param rows
	 * @return
	 */
	public int[] getRows(int rows) {
		int[] row = new int[rows];
		for (int i = 0; i < rows; i++) {
			row[i] = i;
		}
		return row;
	}

	@Override
	public Object getHeadValue(String key) {
		return this.extBill.getParentVO().getAttributeValue(key);
	}

	@Override
	public void setHeadValue(String key, Object value) {
		this.extBill.getParentVO().setAttributeValue(key, value);
	}

	@Override
	public Object getBodyValue(int index, String key) {
		CircularlyAccessibleValueObject[] childVos = this.extBill.getChildrenVO();
		if (childVos == null || childVos.length == 0) {
			return null;
		} else {
			return childVos[index].getAttributeValue(key);
		}
	}

	@Override
	public void setBodyValue(int index, String key, Object value) {
		CircularlyAccessibleValueObject[] childVos = this.extBill.getChildrenVO();
		if (childVos != null && childVos.length > 0) {
			childVos[index].setAttributeValue(key, value);
		}
	}

	@Override
	public Object getBodyValue(int index, String key, Class<? extends ISuperVO> clazz) {
		ISuperVO[] childVos = this.extBill.getChildren(clazz);
		if (childVos == null || childVos.length == 0) {
			return null;
		} else {
			return childVos[index].getAttributeValue(key);
		}
	}

	@Override
	public void setBodyValue(int index, String key, Object value, Class<? extends ISuperVO> clazz) {
		ISuperVO[] childVos = this.extBill.getChildren(clazz);
		if (childVos != null && childVos.length > 0) {
			childVos[index].setAttributeValue(key, value);
		}
	}

	/**
	 * 清空表体数据
	 * 
	 * @param strings
	 */
	public void clearBodyValue(String[] keys) {
		for (int row = 0, rows = this.getBodyRowCount(); row < rows; row++) {
			this.clearRowValueByItemKeys(row, keys);
		}
	}

	public void clearBodyValue(String[] keys, Class<? extends ISuperVO> clazz) {
		for (int row = 0, rows = this.getBodyRowCount(clazz); row < rows; row++) {
			this.clearRowValueByItemKeys(row, keys, clazz);
		}
	}

	public void clearRowValueByItemKeys(int index, String[] keys) {
		for (String key : keys) {
			this.setBodyValue(index, key, null);
		}
	}

	public void clearRowValueByItemKeys(int index, String[] keys, Class<? extends ISuperVO> clazz) {
		for (String key : keys) {
			this.setBodyValue(index, key, null, clazz);
		}
	}

	/**
	 * 给表体所有行某字段射值
	 * 
	 * @param vqtunitrate
	 * @param nchangerate
	 */
	public void setBodyValue(String itemKey, String value) {
		for (int row = 0, rows = this.getBodyRowCount(); row < rows; row++) {
			this.setBodyValue(row, itemKey, value);
		}
	}

	/**
	 * 删除表体数据
	 * 
	 * @param class1
	 */
	public void deleteBodyValue(Class<? extends ISuperVO> clazz) {
		this.extBill.setChildren(clazz, null);
	}
}

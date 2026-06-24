package nccloud.dto.ct.pub.utils;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;

/**
 * @description 视图VO工具类(主要用于表体编辑后)
 * @author xiahui
 * @date 创建时间：2019-3-11 上午9:10:26
 * @version ncc1.0
 **/
public class ViewVOUtil {
	private CircularlyAccessibleValueObject viewVO;

	public ViewVOUtil(CircularlyAccessibleValueObject viewVO) {
		this.viewVO = viewVO;
	}

	public void setViewValue(String key, Object value) {
		viewVO.setAttributeValue(key, value);
	}

	public Integer getIntegerValue(String key) {
		return ValueUtils.getInteger(viewVO.getAttributeValue(key));
	}

	public UFDouble getUFDoubleValue(String key) {
		return ValueUtils.getUFDouble(viewVO.getAttributeValue(key));
	}

	public String getStringValue(String key) {
		return ValueUtils.getString(viewVO.getAttributeValue(key));
	}

	public UFDate getUFDateValue(String key) {
		return ValueUtils.getUFDate(viewVO.getAttributeValue(key));
	}
}

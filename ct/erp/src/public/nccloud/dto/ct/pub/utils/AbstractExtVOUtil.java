package nccloud.dto.ct.pub.utils;

import nc.vo.pub.ISuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;

/**
 * @description 一主多子vo操作抽象类
 * @author xiahui
 * @date 创建时间：2019-1-28 上午9:46:08
 * @version ncc1.0
 **/
public abstract class AbstractExtVOUtil {

	public String getHeadTailStringValue(String key) {
		Object obj = this.getHeadValue(key);
		return ValueUtils.getString(obj);
	}

	public String getBodyStringValue(int index, String key) {
		Object obj = this.getBodyValue(index, key);
		return ValueUtils.getString(obj);
	}

	public String getBodyStringValue(int index, String key, Class<? extends ISuperVO> clazz) {
		Object obj = this.getBodyValue(index, key, clazz);
		return ValueUtils.getString(obj);
	}

	public UFDate getHeadTailUFDateValue(String key) {
		Object obj = this.getHeadValue(key);
		return ValueUtils.getUFDate(obj);
	}

	public UFBoolean getBodyUFBooleanValue(int index, String key) {
		Object obj = this.getBodyValue(index, key);
		return ValueUtils.getUFBoolean(obj);
	}

	public UFBoolean getBodyUFBooleanValue(int index, String key, Class<? extends ISuperVO> clazz) {
		Object obj = this.getBodyValue(index, key, clazz);
		return ValueUtils.getUFBoolean(obj);
	}

	public Integer getBodyIntegerValue(int index, String key) {
		Object obj = this.getBodyValue(index, key);
		return ValueUtils.getInteger(obj);
	}

	public Integer getBodyIntegerValue(int index, String key, Class<? extends ISuperVO> clazz) {
		Object obj = this.getBodyValue(index, key, clazz);
		return ValueUtils.getInteger(obj);
	}

	public UFDouble getBodyUFDoubleValue(int index, String key) {
		Object obj = this.getBodyValue(index, key);
		return ValueUtils.getUFDouble(obj);
	}

	public UFDouble getBodyUFDoubleValue(int index, String key, Class<? extends ISuperVO> clazz) {
		Object obj = this.getBodyValue(index, key, clazz);
		return ValueUtils.getUFDouble(obj);
	}

	public abstract Object getHeadValue(String key);

	public abstract void setHeadValue(String key, Object value);

	public abstract Object getBodyValue(int index, String key);

	public abstract void setBodyValue(int index, String key, Object value);

	public abstract Object getBodyValue(int index, String key, Class<? extends ISuperVO> clazz);

	public abstract void setBodyValue(int index, String key, Object value, Class<? extends ISuperVO> clazz);

}

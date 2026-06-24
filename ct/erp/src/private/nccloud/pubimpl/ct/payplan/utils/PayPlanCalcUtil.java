package nccloud.pubimpl.ct.payplan.utils;

import java.util.Map;

import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.data.ValueUtils;

/**
 * @description 比率金额联动工具
 * @author xiahui
 * @date 创建时间：2019-3-12 上午10:16:16
 * @version ncc1.0
 **/
public class PayPlanCalcUtil {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setValue(Map data, String key, Object value) {
		Map values = getMapValues(data);
		((Map) values.get(key)).put("value", value);
	}

	@SuppressWarnings("rawtypes")
	public static UFDouble getUFDouble(Map data, String key) {
		Map values = getMapValues(data);
		return ValueUtils.getUFDouble(((Map) values.get(key)).get("value"));
	}

	@SuppressWarnings("rawtypes")
	private static Map getMapValues(Map data) {
		return (Map) data.get("values");
	}
}

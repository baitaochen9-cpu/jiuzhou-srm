package nccloud.web.ct.pub.entry;

import java.io.Serializable;
import java.util.Map;

/**
 * 简单查询条件传输VO
 * 
 * @author wangceb
 * @date 2018-5-18 下午2:30:01
 * @version ncc1.0
 */
public class SimpleQueryConditon implements Serializable {

	private static final long serialVersionUID = 2368994211129179196L;
	private Map<String, Object> conditions;

	public Map<String, Object> getConditions() {
		return conditions;
	}

	public void setConditions(Map<String, Object> conditions) {
		this.conditions = conditions;
	}

	/**
	 * 
	 * 根据Key值获取条件
	 * 
	 * @param key
	 * @return
	 * 
	 */
	public Object getConditonByKey(String key) {
		return conditions.get(key);
	}
}

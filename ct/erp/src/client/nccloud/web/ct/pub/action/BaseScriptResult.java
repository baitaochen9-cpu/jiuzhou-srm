package nccloud.web.ct.pub.action;

import java.util.Map;

/**
 * @description 动作脚本返回结果
 * @author guozhq
 * @date 2018-8-20 下午3:00:35
 * @version ncc1.0
 */
public class BaseScriptResult extends BaseBatchResult {
	
	/**
	 * 动作脚本执行中产生的返回结果
	 */
	private Map<String,Object> userObj;

	public Map<String, Object> getUserObj() {
		return userObj;
	}

	public void setUserObj(Map<String, Object> userObj) {
		this.userObj = userObj;
	}
	
}

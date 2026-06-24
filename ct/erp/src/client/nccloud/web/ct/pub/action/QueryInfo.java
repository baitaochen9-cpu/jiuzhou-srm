package nccloud.web.ct.pub.action;

import java.util.Map;

public class QueryInfo {
	private String[] pks;
	private String pagecode;
	private Map<String, Object> userObj;

	public String[] getPks() {
		return pks;
	}

	public void setPks(String[] pks) {
		this.pks = pks;
	}

	public String getPagecode() {
		return pagecode;
	}

	public void setPagecode(String pagecode) {
		this.pagecode = pagecode;
	}

	public Map<String, Object> getUserObj() {
		return userObj;
	}

	public void setUserObj(Map<String, Object> userObj) {
		this.userObj = userObj;
	}

}

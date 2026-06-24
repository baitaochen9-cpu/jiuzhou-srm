package nccloud.web.ct.pub.action;

import java.util.Map;

public class BaseOperateInfo {

	private OperateInfo[] infos;
	
	private Map<String,Object> userObj;

	public OperateInfo[] getOperateInfos() {
		return infos;
	}

	public void setOperateInfos(OperateInfo[] operateInfos) {
		this.infos = operateInfos;
	}

	public Map<String, Object> getUserObj() {
		return userObj;
	}

	public void setUserObj(Map<String, Object> userObj) {
		this.userObj = userObj;
	}

}

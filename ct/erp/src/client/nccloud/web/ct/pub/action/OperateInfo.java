package nccloud.web.ct.pub.action;

import java.util.LinkedHashMap;
import java.util.Map;

import nc.vo.pub.lang.UFDateTime;

/**
 * @description 公共操作Info
 * @author guozhq
 * @date 2018-8-8 下午9:15:50
 * @version ncc1.0
 */
public class OperateInfo {

	private String id;
	private UFDateTime ts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public static Map<String, UFDateTime> convertToMap(OperateInfo[] infos) {
		Map<String, UFDateTime> map = new LinkedHashMap<String, UFDateTime>();
		for (OperateInfo info : infos) {
			map.put(info.getId(), info.getTs());
		}
		return map;
	}

}

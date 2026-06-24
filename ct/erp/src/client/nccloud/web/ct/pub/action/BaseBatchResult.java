package nccloud.web.ct.pub.action;

import java.util.Map;

/**
 * @description 批量处理结果
 * @author guozhq
 * @date 2018-8-9 上午9:41:44
 * @version ncc1.0
 */
public class BaseBatchResult {
	
	private String message;
	
	private String[] errMsg;
	
	private String[] successIds;
	
	private Map<String,Object> successResult;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String[] errMsg) {
		this.errMsg = errMsg;
	}

	public String[] getSuccessIds() {
		return successIds;
	}

	public void setSuccessIds(String[] successIds) {
		this.successIds = successIds;
	}

	public Map<String, Object> getSuccessResult() {
		return successResult;
	}

	public void setSuccessResult(Map<String, Object> successResult) {
		this.successResult = successResult;
	}
	
}

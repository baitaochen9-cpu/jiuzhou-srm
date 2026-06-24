package nccloud.dto.scmpub.pflow;

import nccloud.pubitf.riart.pflow.CloudPFlowContext;

/**
 * @description
 *
 * @scene
 * 
 * @param
 * 
 *
 * @since NCC 1909
 * @version 2019年3月21日上午9:48:43
 * @author wangceb
 */
public class SCMCloudPFlowContext extends CloudPFlowContext {
	
	// 动作名称数组，必须与billVos数组长度相同，如果不传，默认按照actionName执行动作
	private String[] actionNames;

	/**
	 * @return the actionNames
	 */
	public String[] getActionNames() {
		return actionNames;
	}

	/**
	 * @param actionNames the actionNames to set
	 */
	public void setActionNames(String[] actionNames) {
		this.actionNames = actionNames;
	}
}

package nccloud.api.jzsrm;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:
 * @author: 云峰网络 411072655
 * 
 * @Copyright: 2021 www.yunfeng-net.com Inc. All rights reserved. 山东云峰网络科技有限公司
 */
public abstract class AbstracAdapter4Ext {
	
	public abstract JSONObject sys(Object billvo) throws Exception;
}

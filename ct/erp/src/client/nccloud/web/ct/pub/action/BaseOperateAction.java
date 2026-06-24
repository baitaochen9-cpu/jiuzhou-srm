package nccloud.web.ct.pub.action;

import java.util.HashMap;
import java.util.Map;

import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.ct.purdaily.utils.CombinTsTool;
import nccloud.web.scmpub.pub.utils.power.DataPermissionUtil;

/**
 * @description 基础操作Action(例如：删除、提交、审批等操作动作)
 * @author guozhq
 * @date 2018-8-8 下午6:16:27
 * @version ncc1.0
 */
public abstract class BaseOperateAction extends BaseAction {

	@Override
	public Object excute(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		// 转换成操作信息
		BaseOperateInfo baseOperateInfo = json.fromJson(str, BaseOperateInfo.class);
		OperateInfo[] infos = baseOperateInfo.getOperateInfos();
		// 转换成对象
		Object[] bills = this.combinToOriginal(infos);
		// 自定义参数
		Map<String, Object> userObj = baseOperateInfo.getUserObj();
		if (userObj == null) {
			userObj = new HashMap<String, Object>();
		}
		// 执行数据权限控制
		if (hasNeedCheckPermission()) {
			DataPermissionUtil.checkPermission(bills, getPermissioncode(), getActioncode(), getBillCodeField());
		}
		return this.excute(bills, userObj);
	}

	/**
	 * 
	 * 是否需要检查数据权限
	 * 
	 * @return
	 * 
	 */
	private boolean hasNeedCheckPermission() {
		if (getPermissioncode() == null || getActioncode() == null || getBillCodeField() == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 权限资源编码
	 */
	protected String getPermissioncode() {
		return null;
	}

	/**
	 * 操作编码
	 */
	protected String getActioncode() {
		return null;
	}

	/**
	 * 单据 单据号字段名称
	 */
	protected String getBillCodeField() {
		return null;
	}

	/**
	 * 
	 * 合并原始VO (如果不是IBill，可以覆写)
	 * 
	 * @param infos
	 * @return
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object[] combinToOriginal(OperateInfo[] infos) {
		CombinTsTool tool = new CombinTsTool(getClazz());
		IBill[] bills = tool.combinToOriginal(infos);
		return bills;
	}

	@SuppressWarnings("rawtypes")
	public abstract Class getClazz();

	/**
	 * 
	 * 执行
	 * 
	 * @param infos
	 * @return
	 * 
	 */
	public abstract Object excute(Object[] objs, Map<String, Object> userObj);
}

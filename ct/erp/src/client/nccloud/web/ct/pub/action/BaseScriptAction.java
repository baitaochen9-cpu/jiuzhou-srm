package nccloud.web.ct.pub.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.tool.performance.DeepCloneTool;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;

/**
 * @description 基础脚本执行Action
 * @author guozhq
 * @date 2018-8-10 上午10:31:33
 * @version ncc1.0
 */
public abstract class BaseScriptAction extends BaseBatchOperateAction {

	@Override
	public BaseBatchResult batchOperate(Object[] objs, Map<String, Object> userObj) {
		AbstractBill[] bills = (AbstractBill[]) objs;
		// 深拷贝一份数据，避免当前bills被后续操作改变
		DeepCloneTool tool = new DeepCloneTool();
		AbstractBill[] cloneBills = (AbstractBill[]) tool.deepClone(bills);
		this.beforeProcess(cloneBills, userObj);
		// 执行动作脚本
		SCMScriptResultDTO ret = this.execScript(cloneBills);
		// 执行结果转换
		BaseBatchResult result = this.convertToResult(ret, bills);
		return result;
	}

	/**
	 * 执行前操作
	 * 
	 * @param objs
	 * @param userObj
	 * @return
	 */
	public void beforeProcess(Object[] objs, Map<String, Object> userObj) {

	};

	/**
	 * 
	 * 处理成功结果集
	 * 
	 * @param bills
	 * @param orginalBills
	 *          原始VO
	 * @return
	 * 
	 */
	public abstract Map<String, Object> processSuccessResult(AbstractBill[] bills, AbstractBill[] orginalBills);

	/**
	 * 
	 * 执行脚本
	 * 
	 * @param bills
	 * @return
	 * 
	 */
	public abstract SCMScriptResultDTO execScript(AbstractBill[] bills);

	/**
	 * 
	 * 由脚本结果VO,转换成最终返回前台VO
	 * 
	 * @param dto
	 * @param bills
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public BaseBatchResult convertToResult(SCMScriptResultDTO dto, AbstractBill[] bills) {
		BaseScriptResult result = new BaseScriptResult();
		Object obj = dto.getData();
		if (obj != null && obj instanceof Map) {
			result.setUserObj((Map<String, Object>) obj);
		} else {
			if (bills.length == 1) {
				// 获取成功的Key
				AbstractBill bill = dto.getSucessVOs()[0];
				result.setMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0", "04009012-0024")/*
																																																							 * @
																																																							 * res
																																																							 * "操作成功!"
																																																							 */);
				result.setSuccessIds(new String[] { bill.getParent().getPrimaryKey() });
				result.setSuccessResult(this.processSuccessResult(dto.getSucessVOs(), bills));
			} else {
				Map<Integer, String> errMsgs = dto.getErrorMessageMap();
				AbstractBill[] successVos = dto.getSucessVOs();
				if (errMsgs != null && errMsgs.size() > 0) {
					List<String> msgs = new ArrayList<String>();
					for (Entry<Integer, String> entry : errMsgs.entrySet()) {
						msgs.add(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0", "04009012-0022", null,
								new String[] { String.valueOf(entry.getKey() + 1) })/*
																																		 * @res
																																		 * "第{0}条记录失败："
																																		 */
								+ entry.getValue());
					}
					result.setErrMsg(msgs.toArray(new String[0]));
				}
				if (successVos != null && successVos.length > 0) {
					String[] successIds = new String[successVos.length];
					for (int j = 0; j < successVos.length; j++) {
						successIds[j] = successVos[j].getPrimaryKey();
					}
					result.setSuccessIds(successIds);
					result.setSuccessResult(this.processSuccessResult(successVos, bills));
				}
			}
		}
		return result;
	}
}
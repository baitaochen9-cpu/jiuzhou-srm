package nccloud.web.ct.pub.action;

import java.util.Map;

import nc.vo.scmpub.util.ArrayUtil;

/**
 * @description 批量执行Action(部分成功、部分失败)
 * @author guozhq
 * @date 2018-8-10 上午11:30:07
 * @version ncc1.0
 */
public abstract class BaseBatchOperateAction extends BaseOperateAction {

	@Override
	public Object excute(Object[] objs, Map<String, Object> userObj) {
		BaseBatchResult result = this.batchOperate(objs, userObj);
		this.processFinalMessage(result);
		return result;
	}

	/**
	 * 
	 * 执行批量
	 * 
	 * @param bills
	 * @return
	 * 
	 */
	public abstract BaseBatchResult batchOperate(Object[] objs, Map<String, Object> userObj);

	/**
	 * 
	 * 处理最终消息
	 * 
	 * @param result
	 * 
	 */
	public void processFinalMessage(BaseBatchResult result) {
		if (result.getMessage() != null) {
			return;
		}
		if (result instanceof BaseScriptResult) {
			// 处理提交时 指派信息问题，直接返回
			if (((BaseScriptResult) result).getUserObj() != null) {
				return;
			}
		}
		if (ArrayUtil.isEmpty(result.getSuccessIds()) && !ArrayUtil.isEmpty(result.getErrMsg())
				&& result.getErrMsg().length > 0) {
			this.processAllFailureMsg(result);
		} else if (!ArrayUtil.isEmpty(result.getSuccessIds()) && result.getSuccessIds().length > 0
				&& ArrayUtil.isEmpty(result.getErrMsg())) {
			this.processAllSuccessMsg(result);
		} else if (!ArrayUtil.isEmpty(result.getSuccessIds()) && !ArrayUtil.isEmpty(result.getErrMsg())
				&& result.getSuccessIds().length > 0 && result.getErrMsg().length > 0) {
			this.processSomeSuccessSomeFailMsg(result);
		}
	}

	/**
	 * 
	 * 全部失败消息提示
	 * 
	 * @param result
	 * 
	 */
	protected void processAllFailureMsg(BaseBatchResult result) {
		int errNum = result.getErrMsg().length;
		result.setMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0", "04009012-0016", null,
				new String[] { String.valueOf(errNum), String.valueOf(errNum) })/*
																																				 * @ res
																																				 * "共{0}条，处理失败{1}条！"
																																				 */);
	}

	/**
	 * 
	 * 全部成功消息提示
	 * 
	 * @param result
	 * 
	 */
	protected void processAllSuccessMsg(BaseBatchResult result) {
		int successNum = result.getSuccessIds().length;
		result.setMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4009012_0", "04009012-0017", null,
				new String[] { String.valueOf(successNum) })/*
																										 * @ res "处理成功{0}条！"
																										 */);
	}

	/**
	 * 
	 * 处理部分成功部分失败消息
	 * 
	 * @param result
	 * 
	 */
	protected void processSomeSuccessSomeFailMsg(BaseBatchResult result) {
		result.setMessage(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"4009012_0",
				"04009012-0018",
				null,
				new String[] { String.valueOf(result.getSuccessIds().length + result.getErrMsg().length),
						String.valueOf(result.getSuccessIds().length), String.valueOf(result.getErrMsg().length) }));/*
																																																					 * @
																																																					 * res
																																																					 * "共{0}条，成功{1}条,失败{2}条!"
																																																					 */

	}
}
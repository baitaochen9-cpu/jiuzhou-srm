package nccloud.web.ct.pub.action;

import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.web.scmpub.pub.assign.CommitAssignUtils;
import nccloud.web.scmpub.pub.resexp.PfResumeExceptionNccUtils;

/**
 * @description 基础Action
 * @author guozhq
 * @date 2018-8-10 下午12:24:00
 * @version ncc1.0
 */
public abstract class BaseAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		// 判断是否处理交互式异常，默认为否
		Boolean isHandleResumeException = this.isHandleResumeException();	
		try {
			// 交互式异常处理
			if(Boolean.TRUE.equals(isHandleResumeException)){
				PfResumeExceptionNccUtils.before(request);				
			}
			// 提交指派处理
			CommitAssignUtils.before(request);
			// 执行
			return this.excute(request);
		} catch (Exception e) {
			if(Boolean.TRUE.equals(isHandleResumeException)){
				return PfResumeExceptionNccUtils.handleResumeException(e);				
			}else{
				ExceptionUtils.wrapException(e);
			}
		}
		return null;
	}

	/**
	 * 
	 * 是否处理交互式异常，默认为否
	 * @return
	 *
	 */
	protected Boolean	isHandleResumeException(){
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * 执行
	 * 
	 * @param request
	 * @return
	 * 
	 */
	public abstract Object excute(IRequest request);
}

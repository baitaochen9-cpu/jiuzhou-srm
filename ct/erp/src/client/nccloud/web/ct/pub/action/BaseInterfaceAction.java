package nccloud.web.ct.pub.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.scmpub.util.CollectionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchOprContext;
import nccloud.pubitf.scmpub.pub.batchopr.dto.SCMBatchResultDTO;
import nccloud.pubitf.scmpub.pub.batchopr.service.ISCMBatchOperatorService;
import nccloud.web.scmpub.pub.resexp.SCMResumeExceptionInvokerNcc;

/**
 * @description 基础批量操作（接口、独立事务）
 * @author guozhq
 * @date 2018-9-4 上午8:42:34
 * @version ncc1.0
 */
public abstract class BaseInterfaceAction extends BaseBatchOperateAction {

	@Override
	public BaseBatchResult batchOperate(Object[] objs, Map<String, Object> userObj) {
		SCMBatchOprContext context = this.createContext(objs);
		BaseBatchResult result = new BaseBatchResult();
		if (objs == null) {
			return result;
		}
		// 判断是否是一个还是多个
		if (objs.length == 1) {
			result = execOne(context, objs);
		} else {
			result = execMulti(context, objs);
		}
		return result;
	}

	/**
	 * 
	 * 处理批量的情况
	 * 
	 * @param context
	 * @param objs
	 * @return
	 * 
	 */
	protected BaseBatchResult execMulti(SCMBatchOprContext context, Object[] objs) {
		try {
			BaseBatchResult result = new BaseBatchResult();
			// 执行批量操作
			SCMBatchResultDTO ret = ServiceLocator.find(ISCMBatchOperatorService.class).operateExtend(context);
			// 设值错误信息
			result.setErrMsg(ret.getErrorMessages());
			// 处理返回结果
			this.processInterfaceResult(ret.getSucessRetObj(), objs, result);
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 
	 * 处理接口返回的结果（如果需要自己处理，可覆写）
	 * 
	 * @param ret
	 * @param result
	 * 
	 */
	protected void processInterfaceResult(Object[] ret, Object[] objs, BaseBatchResult result) {
		// 设值成功的主键ID
		result.setSuccessIds(this.processSuccessIds(ret, objs));
		// 设值成功的结果记录
		result.setSuccessResult(this.processSuccessResult(ret, objs));
	}

	/**
	 * 
	 * 执行一个操作
	 * 
	 * @param context
	 * @param objs
	 * @return
	 * 
	 */
	protected BaseBatchResult execOne(SCMBatchOprContext context, Object[] objs) {
		try {
			Class<?> interfaceClass = Class.forName(context.getInterfaceName());
			// 支持交互式异常
			Object service = new SCMResumeExceptionInvokerNcc().getService(interfaceClass);
			Method mtd = service.getClass().getMethod(context.getMethodName(), context.getParamTypes());
			Object retObject = mtd.invoke(service, context.getParams());

			List<Object> sucessList = new ArrayList<Object>();
			if (retObject != null) {
				if (retObject.getClass().isArray()) {
					sucessList.add(((Object[]) retObject)[0]);
				} else if (retObject instanceof List) {
					sucessList.add(((List) retObject).get(0));
				} else {
					sucessList.add(retObject);
				}
			} else {
				sucessList.add(objs[0]);
			}
			BaseBatchResult result = new BaseBatchResult();
			this.processInterfaceResult(CollectionUtils.listToArray(sucessList), objs, result);
			return result;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 
	 * 处理成功的Id(默认按照单据处理，如果有需要可以自己覆写)
	 * 
	 * @param bills
	 * @param orginalBills
	 * @return
	 * 
	 */
	protected String[] processSuccessIds(Object[] bills, Object[] orginalBills) {
		List<String> ids = new ArrayList<String>();
		if (bills != null && bills instanceof AbstractBill[]) {
			AbstractBill[] successBills = (AbstractBill[]) bills;
			for (int i = 0; i < bills.length; i++) {
				ids.add(successBills[i].getPrimaryKey());
			}
		}
		return ids.toArray(new String[0]);
	}

	/**
	 * 
	 * 处理成功结果集
	 * 
	 * @param bills
	 * @param orginalBills 原始VO
	 * @return
	 * 
	 */
	public abstract Map<String, Object> processSuccessResult(Object[] bills, Object[] orginalBills);

	/**
	 * 
	 * 创建执行上下文
	 * 
	 * @param objs
	 * @return
	 * 
	 */
	public abstract SCMBatchOprContext createContext(Object[] objs);
}

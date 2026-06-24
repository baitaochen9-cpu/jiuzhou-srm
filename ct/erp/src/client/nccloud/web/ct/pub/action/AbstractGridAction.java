package nccloud.web.ct.pub.action;

import java.util.LinkedHashMap;
import java.util.Map;

import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nccloud.dto.so.pub.entity.BatchOprInfo;
import nccloud.dto.so.pub.entity.SimpleQueryInfo;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.scmpub.pub.assign.CommitAssignUtils;
import nccloud.web.scmpub.pub.resexp.PfResumeExceptionNccUtils;
import nccloud.web.scmpub.pub.utils.power.DataPermissionUtil;

/**
 * @description 单据列表界面按钮处理
 * @author zhangjyp
 * @date 2018-5-18 下午2:07:48
 * @version ncc1.0
 */
public abstract class AbstractGridAction<E extends AbstractBill> implements
		ICommonAction {

	@Override
	public Object doAction(IRequest request) {

		// 提交指派处理
		CommitAssignUtils.before(request);
		// 交互式异常处理

		PfResumeExceptionNccUtils.before(request);

		// 1.解析前台的请求
		SimpleQueryInfo[] infos = this.buildParams(request);

		Map<String, UFDateTime> pkMap = this.buildPkMap(infos);

		if (null == pkMap || pkMap.isEmpty())
			return null;

		// 2.查询aggvo
		E[] vos = this.queryVos(pkMap.keySet().toArray(new String[0]));
		if(null==vos||vos.length!=pkMap.keySet().size()){
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4006013_0","04006013-0006")/*@res "数据已被他人修改，请重新查询"*/);
		}else {
			// 3、将时间戳ts封装进vo中
			this.fillTs(vos, pkMap);

			// 4.动作
			Object obj = this.action(vos);

			return obj;
		}
		return null;
	}

	/**
	 * 权限资源编码
	 */
	protected String getPermissioncode() {
		return null;
	};

	/**
	 * 操作编码
	 */
	protected String getActioncode() {
		return null;
	};

	/**
	 * 单据 单据号字段名称
	 */
	protected String getBillCodeField() {
		return null;
	};

	/**
	 * 权限控制
	 *
	 * @param bills
	 *          单据vo
	 *
	 */
	protected void checkPermission(IBill[] bills) {
		DataPermissionUtil.checkPermission(bills, getPermissioncode(),
				getActioncode(), getBillCodeField());
	}

	protected void fillTs(E[] vos, Map<String, UFDateTime> pkMap) {
		for (E vo : vos) {
			CircularlyAccessibleValueObject hvo = vo.getParentVO();
			try {
				hvo.setAttributeValue("ts", pkMap.get(hvo.getPrimaryKey()));
			} catch (BusinessException e) {
				ExceptionUtils.wrapException(e);
			}

		}

	}

	/**
	 *
	 * 从请求中获取表头pk和ts的关系
	 *
	 * @param request
	 * @return
	 *
	 */
	protected SimpleQueryInfo[] buildParams(IRequest request) {
		String read = request.read();
		IJson json = JsonFactory.create();
		BatchOprInfo infos = json.fromJson(read, BatchOprInfo.class);
		return infos.getQryinfo();

	}

	private Map<String, UFDateTime> buildPkMap(SimpleQueryInfo[] infos) {
		Map<String, UFDateTime> pkMap = new LinkedHashMap<String, UFDateTime>();
		for (SimpleQueryInfo info : infos) {
			pkMap.put(info.getPk(), info.getTs());
		}
		return pkMap;
	}

	/**
	 * 查询aggvo
	 *
	 * @param pks
	 * @return
	 *
	 */
	protected abstract E[] queryVos(String[] ids);

	/**
	 * 动作处理
	 *
	 * @param vos
	 *
	 */
	protected abstract Object action(E[] vos);

}
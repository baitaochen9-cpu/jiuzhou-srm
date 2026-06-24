package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.action.itf.ICommonAction;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.pubitf.ct.purdaily.service.IControlScope;

/**
 * @description 合同控制范围
 * @author xiahui
 * @date 创建时间：2019-2-25 下午1:22:36
 * @version ncc1.0
 **/
public class ControlScopeAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		String pk = json.fromJson(str, String.class);
		try {
			// 查询原始数据
			AggCtPuVO[] bills = ServiceLocator.find(IPurdailyMaintain.class).queryCtPuVoByIds(new String[] { pk });

			OperateExceptionUtils.checkVo(bills, null); // 检验单据删除
			this.checkData(bills[0]);

			Map<String, Object> retData = ServiceLocator.find(IControlScope.class).getCtrlScopeList(bills[0]);
			return retData;
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 校验最新版本、是否总括订单
	 * 
	 * @param vos
	 */
	private void checkData(AggCtPuVO bill) {
		// 非最新版本的合同不允许编辑控制范围
		UFBoolean bshowlatest = bill.getParentVO().getBshowLatest();
		if (UFBoolean.FALSE.equals(bshowlatest)) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0341", null, new String[] { bill.getParentVO().getVbillcode() }));// 合同{0}不是最新版本，不允许编辑控制范

		}

		// 判断是否是总括订单
		UFBoolean bBracketOrder = bill.getParentVO().getBbracketOrder();
		if (bBracketOrder.booleanValue()) {
			ExceptionUtils.wrapBusinessException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0",
					"04020003-0302")); // 总括订单不允许编辑合同控制范围
		}
	}
}

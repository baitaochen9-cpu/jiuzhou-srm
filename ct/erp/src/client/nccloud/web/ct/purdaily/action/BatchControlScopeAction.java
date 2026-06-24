package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ml.NCLangRes4VoTransl;
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
 * @description 合同控制范围批量调整
 * @author xiahui
 * @date 创建时间：2019-2-25 下午1:23:46
 * @version ncc1.0
 **/
public class BatchControlScopeAction implements ICommonAction {

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		String[] pks = json.fromJson(str, String[].class);
		try {
			// 查询原始数据
			IPurdailyMaintain service = ServiceLocator.find(IPurdailyMaintain.class);
			AggCtPuVO[] bills = service.queryCtPuVoByIds(pks);
			// 检验单据删除
			OperateExceptionUtils.checkVo(bills, null);
			this.checkData(bills);

			Map<String, Object> retData = ServiceLocator.find(IControlScope.class).getBatchCtrlScopeList(bills);
			return retData;
		} catch (Exception e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	/**
	 * 校验最新版本
	 * 
	 * @param bills
	 */
	private void checkData(AggCtPuVO[] bills) {
		for (AggCtPuVO bill : bills) {
			UFBoolean bshowlatest = bill.getParentVO().getBshowLatest();
			if (UFBoolean.FALSE.equals(bshowlatest)) {
				ExceptionUtils.wrapBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("4020003_0", "04020003-0341",
						null, new String[] { bill.getParentVO().getVbillcode() })); // 合同{0}不是最新版本，不允许编辑控制范围
			}
		}
	}
}

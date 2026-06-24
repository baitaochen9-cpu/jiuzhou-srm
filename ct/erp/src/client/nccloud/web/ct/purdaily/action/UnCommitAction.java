package nccloud.web.ct.purdaily.action;

import java.util.Map;

import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.scmpub.script.entity.SCMScriptResultDTO;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.service.ServiceLocator;
import nccloud.pubitf.ct.purdaily.service.IPurdailyService;
import nccloud.web.ct.pub.action.BaseScriptAction;
import nccloud.web.ct.purdaily.utils.ResultUtil;

/**
 * @description 收回
 * @author xiahui
 * @date 创建时间：2019-1-18 上午8:46:07
 * @version ncc1.0
 **/
public class UnCommitAction extends BaseScriptAction {

	@Override
	public Map<String, Object> processSuccessResult(AbstractBill[] bills, AbstractBill[] orginalBills) {
		return ResultUtil.processScriptResult(bills, orginalBills);
	}

	@Override
	public SCMScriptResultDTO execScript(AbstractBill[] bills) {
		try {
			return ServiceLocator.find(IPurdailyService.class).uncommit((AggCtPuVO[]) bills);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getClazz() {
		return AggCtPuVO.class;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.PurDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return PowerActionEnum.UNCOMMIT.getActioncode();
	}

	@Override
	public String getBillCodeField() {
		return CtPuVO.VBILLCODE;
	}

	@Override
	protected Boolean isHandleResumeException() {
		return Boolean.TRUE;
	}

}

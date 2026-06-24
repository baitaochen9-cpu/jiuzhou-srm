package nccloud.web.ct.purdaily.action;

import nc.itf.ct.purdaily.IPurdailyMaintain;
import nc.vo.ct.enumeration.CtFlowEnum;
import nc.vo.ct.purdaily.entity.AggCtPuVO;
import nc.vo.ct.purdaily.entity.CtPuVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pub.power.PowerActionEnum;
import nc.vo.scmpub.res.billtype.CTBillType;
import nccloud.dto.ct.pub.utils.OperateExceptionUtils;
import nccloud.framework.core.exception.ExceptionUtils;
import nccloud.framework.core.json.IJson;
import nccloud.framework.service.ServiceLocator;
import nccloud.framework.web.container.IRequest;
import nccloud.framework.web.json.JsonFactory;
import nccloud.web.scmpub.pub.action.DataPermissionAction;
import nccloud.web.scmpub.pub.utils.SCMEditCheckUtils;

/**
 * @description 编辑/变更
 * @author xiahui
 * @date 创建时间：2019-1-15 上午9:55:05
 * @version ncc1.0
 **/
public class EditAction extends DataPermissionAction {
	private static final String ACTIONCODE_MODIFY = "modify";

	private String actionCode = PowerActionEnum.EDIT.getActioncode();

	@Override
	public Object doAction(IRequest request) {
		String str = request.read();
		IJson json = JsonFactory.create();
		String id = json.fromJson(str, String.class);

		try {
			IPurdailyMaintain service = ServiceLocator.find(IPurdailyMaintain.class);
			AggCtPuVO[] vos = service.queryCtPuVoByIds(new String[] { id });
			// 检验单据删除
			OperateExceptionUtils.checkVo(vos, null);

			int fstatusflag = vos[0].getParentVO().getFstatusflag();
			if (fstatusflag == CtFlowEnum.APPROVING.toIntValue() || fstatusflag == CtFlowEnum.APPROVE.toIntValue()) {
				// 当前审批人审批中可修改
				SCMEditCheckUtils.checkEdit(vos[0].getParentVO().getPk_ct_pu(), vos[0].getParentVO().getVtrantypecode());
			}

			double version = vos[0].getParentVO().getVersion().doubleValue();
			if (fstatusflag == CtFlowEnum.VALIDATE.toIntValue() || version > 1) {
				// 变更
				this.actionCode = ACTIONCODE_MODIFY;
			}
			// 数据权限
			this.checkPermission(vos);
		} catch (BusinessException e) {
			ExceptionUtils.wrapException(e);
		}
		return null;
	}

	@Override
	public String getPermissioncode() {
		return CTBillType.PurDaily.getCode();
	}

	@Override
	public String getActioncode() {
		return this.actionCode;
	}

	@Override
	public String getBillCodeField() {
		return CtPuVO.VBILLCODE;
	}

}

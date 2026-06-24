package nc.ui.pu.m20.action;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzyy.sys.IProcessService;
import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;
import nccloud.base.exception.BusinessException;

public class PraybillApproveAction extends ApproveScriptAction {
	private static final long serialVersionUID = 4171051148975331842L;
	IProcessService check = (IProcessService) NCLocator.getInstance().lookup(
			IProcessService.class.getName());
	boolean outSystem = false;

	protected boolean isActionEnable() {
		Object[] objs = this.model.getSelectedOperaDatas();
		if ((objs != null) && (objs.length > 1)) {
			return true;
		}
		PraybillVO vos = (PraybillVO) this.model.getSelectedData();
		try {
			if (vos != null) {
				outSystem = check.is2oa(vos.getHVO().getPk_org());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new BusinessException("检查同步oa参数出错");

		}
		if (outSystem) {
			return false;
		}
		boolean enabled1 = false;
		if (this.model.getSelectedData() != null) {
			PraybillVO vo = (PraybillVO) this.model.getSelectedData();
			int status = vo.getHVO().getFbillstatus().intValue();
			enabled1 = (((status == POEnumBillStatus.FREE.toInt())
					|| (status == POEnumBillStatus.APPROVING.toInt()) || (status == POEnumBillStatus.COMMIT
					.toInt())))
					&& (this.model.getAppUiState() == AppUiState.NOT_EDIT);
		}

		return enabled1;
	}
}
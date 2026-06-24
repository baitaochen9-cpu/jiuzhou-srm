/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package nc.ui.pu.m20.action;

import java.awt.event.ActionEvent;

import nc.ui.pubapp.uif2app.AppUiState;
import nc.ui.pubapp.uif2app.actions.pflow.ApproveScriptAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.vo.pu.m20.entity.PraybillHeaderVO;
import nc.vo.pu.m20.entity.PraybillVO;
import nc.vo.pu.pub.enumeration.POEnumBillStatus;

public class PraybillApproveAction extends ApproveScriptAction {
	private static final long serialVersionUID = 4171051148975331842L;

	protected boolean isActionEnable() {
		Object[] objs = this.model.getSelectedOperaDatas();
		if ((objs != null) && (objs.length > 1)) {
			return true;
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
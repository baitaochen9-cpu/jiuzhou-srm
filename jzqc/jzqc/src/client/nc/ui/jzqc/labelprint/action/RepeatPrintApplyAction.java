package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.ui.jzqc.labelprint.ace.view.LabelPrintApplyDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;

public class RepeatPrintApplyAction extends NCAction {
	private static final long serialVersionUID = 1L;
	private AbstractAppModel model;

	public RepeatPrintApplyAction() {
		String str = "重复打印申请";
		this.setBtnName(str);
		this.setCode("repeatprint");
	}

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {

			LabelPrintApplyDialog dialog = new LabelPrintApplyDialog(this
					.getModel().getContext().getEntranceUI());
			if (dialog.showModal() != UIDialog.ID_OK) {
				return;
			}

			String reason = dialog.getM_Reason();
			AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;

			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getBprintstatus() != null
					&& hvo.getParentVO().getBprintstatus().booleanValue()) {
				 throw new BusinessException("标签可打印状态为是不能重复打印申请！");
			}

			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getBlabelstatus() != null
					&& !hvo.getParentVO().getBlabelstatus().booleanValue()) {
				 throw new BusinessException("标签已失效不能重复打印申请！");
			}

			ILabelprintMaintain operator = NCLocator.getInstance().lookup(
					ILabelprintMaintain.class);
			Object[] objs = ((BillManageModel) getModel())
					.getSelectedOperaDatas();
			List<AggLabelPrintHVO> list = new ArrayList<>();
			for (Object obj : objs) {
				AggLabelPrintHVO hvo1 = (AggLabelPrintHVO) obj;
				hvo1.getParentVO().setVnote(reason);
				list.add(hvo1);
			}
			AggLabelPrintHVO[] bills = operator.repeatPrintApply(list
					.toArray(new AggLabelPrintHVO[list.size()]));
			RefreshSingleAction srefresh = new RefreshSingleAction();
			srefresh.setModel((AbstractAppModel) getModel());
			ActionEvent event = new ActionEvent(srefresh, 1001, "刷新");
			srefresh.doAction(event);
			ShowStatusBarMsgUtil.showStatusBarMsg("重复打印申请成功！", getModel()
					.getContext());

		}
	}

	protected boolean isActionEnable() {
		Object obj = getModel().getSelectedData();
		if (obj == null)
			return false;
		return model.getUiState() == UIState.NOT_EDIT;
	}

	public AbstractAppModel getModel() {
		return model;
	}

	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}
}

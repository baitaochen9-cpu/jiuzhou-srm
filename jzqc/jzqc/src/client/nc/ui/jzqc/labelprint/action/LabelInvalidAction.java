package nc.ui.jzqc.labelprint.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.jzqc.ILabelprintMaintain;
import nc.ui.pubapp.uif2app.actions.RefreshSingleAction;
import nc.ui.pubapp.uif2app.model.BillManageModel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.ShowStatusBarMsgUtil;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.AbstractAppModel;
import nc.vo.jzqc.labelprint.AggLabelPrintHVO;
import nc.vo.pub.BusinessException;

public class LabelInvalidAction extends NCAction {
	private static final long serialVersionUID = 1L;
	private AbstractAppModel model;

	public LabelInvalidAction() {
		String str = "标签失效";
		this.setBtnName(str);
		this.setCode("invalid");
	}

	public void doAction(ActionEvent e) throws Exception {

		Object o = getModel().getSelectedData();
		if (o == null) {
			throw new BusinessException("请先选中数据！");
		} else {
			AggLabelPrintHVO hvo = (AggLabelPrintHVO) o;
			Object[] objs = ((BillManageModel) getModel())
					.getSelectedOperaDatas();
			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getIprintcount() != null
					&& hvo.getParentVO().getIprintcount().intValue() == 0) {
				throw new BusinessException("打印次数等于零的数据不能失效！");
			}

			if (hvo.getParentVO() != null
					&& hvo.getParentVO().getBlabelstatus() != null
					&& !hvo.getParentVO().getBlabelstatus().booleanValue()) {
				throw new BusinessException("标签已失效不能再次失效！");
			}
			List<AggLabelPrintHVO> list = new ArrayList<>();
			for (Object obj : objs) {
				AggLabelPrintHVO hvo1 = (AggLabelPrintHVO) obj;
				list.add(hvo1);
			}
			ILabelprintMaintain operator = NCLocator.getInstance().lookup(
					ILabelprintMaintain.class);
			AggLabelPrintHVO[] bills = operator.printInvalid(list
					.toArray(new AggLabelPrintHVO[list.size()]));
			RefreshSingleAction srefresh = new RefreshSingleAction();
			srefresh.setModel(getModel());
			ActionEvent event = new ActionEvent(srefresh, 1001, "刷新");
			srefresh.doAction(event);
			ShowStatusBarMsgUtil.showStatusBarMsg("标签失效成功！", getModel()
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

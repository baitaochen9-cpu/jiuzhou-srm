package nc.ui.jzqc.labelcontrol.action;

import nc.buzimsg.vo.MsgresRcvConfVO;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.batch.BatchAddLineAction;
import nc.ui.uif2.model.HierachicalDataAppModel;

public class ReceiverConfBatchAddLineAction extends BatchAddLineAction {
	private static final long serialVersionUID = -4390605662094159750L;
	private HierachicalDataAppModel treeModel;

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

	protected void processLineOperate(Object obj) throws Exception {
		MsgresRcvConfVO receiver = (MsgresRcvConfVO) obj;
		receiver.setMsgres_code("labelcontrol");
		receiver.setPk_group(WorkbenchEnvironment.getInstance().getGroupVO()
				.getPk_group());
		receiver.setPk_billtypecode("JZ01-Cxx-25");
		receiver.setPk_billtypeid("1001AA100000000BWXGT");
		receiver.setReceivers(null);
		super.processLineOperate(receiver);
//		getModel().setUiState(UIState.ADD);
	}

	protected boolean isActionEnable() {
		return (super.isActionEnable());
	}
}

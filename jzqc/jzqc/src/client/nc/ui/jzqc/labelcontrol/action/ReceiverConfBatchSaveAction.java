package nc.ui.jzqc.labelcontrol.action;

import java.awt.event.ActionEvent;

import nc.buzimsg.view.RcvconfBatchBillTable;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.batch.BatchSaveAction;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.meta.BatchOperateVO;

public class ReceiverConfBatchSaveAction extends BatchSaveAction {
	private static final long serialVersionUID = -4649517040154182036L;
	private HierachicalDataAppModel treeModel;

	public void setTreeModel(HierachicalDataAppModel treeModel) {
		this.treeModel = treeModel;
	}

	public void doAction(ActionEvent e) throws Exception {
		BatchOperateVO vo = new BatchOperateVO();

		boolean beforeSave = getEditor().beforeSave();
		if (beforeSave) {

			if ((getValidationService() != null)
					&& (getModel().getValidationService() == null)) {
				getModel().setValidationService(getValidationService());
			}

			getModel().beforeSaveProcess();
			getEditor().getBillCardPanel().dataNotNullValidate();
			vo = getModel().getCurrentSaveObject();
			vo = getModel().getService().batchSave(vo);
			getModel().directSave(vo);
			showSuccessInfo();
		}

		super.doAction(e);

		((RcvconfBatchBillTable) getEditor()).displayReceiverColumnValue();
		 getModel().setUiState(UIState.NOT_EDIT);
	}
}

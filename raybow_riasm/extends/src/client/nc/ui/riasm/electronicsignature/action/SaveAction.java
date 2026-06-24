package nc.ui.riasm.electronicsignature.action;

import java.awt.event.ActionEvent;

import nc.ui.mmgp.uif2.actions.MMGPSaveAction;
import nc.ui.uif2.UIState;
import nc.ui.uif2.model.IQueryAndRefreshManager;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.pubapp.pattern.model.transfer.bill.ClientBillToServer;

public class SaveAction extends MMGPSaveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3135871724828169193L;

	private IQueryAndRefreshManager dataManager = null;
	/**
	 * 
	 * 照着下面（see）的类扒的。。。
	 * 
	 * @see nc.ui.scmf.qc.qualitylevel.action.QualityLevelSaveAction
	 */
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object value = this.getEditor().getValue();
		setDefault(value);
		if (!(value instanceof IBill)) {
			super.doAction(e);
			return;
		}

		this.validate(value);

		IBill billVO = (IBill) value;

		ClientBillToServer<IBill> tool = new ClientBillToServer<IBill>();

		IBill[] clientVOs = new IBill[] { billVO };

		// 增加按钮
		if (UIState.ADD == this.getModel().getUiState()) {

			doAddSave(value);
		}
		// 修改按钮
		else {
			IBill[] oldVO = new IBill[] { (IBill)value};
			// 取得轻量级VO
//			IBill[] lightVOs = tool.construct(oldVO, clientVOs);
//			tool.constructDelete(clientVOs);
			doEditSave(clientVOs[0]);
		}
		getDataManager().refresh();
		showSuccessInfo();
	}

	protected void setDefault(Object value) {
		
	}

	public IQueryAndRefreshManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IQueryAndRefreshManager dataManager) {
		this.dataManager = dataManager;
	}
	
	

}
